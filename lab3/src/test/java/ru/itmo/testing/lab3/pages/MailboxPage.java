package ru.itmo.testing.lab3.pages;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import ru.itmo.testing.lab3.support.MailFolder;
import ru.itmo.testing.lab3.support.MailRuSelectors;
import ru.itmo.testing.lab3.support.UiActions;

import java.time.Duration;

public final class MailboxPage {

    public MailboxPage openInbox() {
        return openFolder(MailFolder.INBOX);
    }

    public MailboxPage openFolder(MailFolder folder) {
        UiActions.openAuthenticatedPage(folder.url());
        return this;
    }

    public MailboxPage shouldShowShell() {
        UiActions.visible(MailRuSelectors.MAIL_APP_WRITE_BUTTON);
        for (MailFolder folder : MailFolder.values()) {
            UiActions.visible(folder.sidebarXpath());
        }
        return this;
    }

    public MailboxPage openMessageBySubject(MailFolder folder, String subject, Duration timeout) {
        waitForMessageInFolder(folder, subject, timeout);
        messageLink(folder, subject).click();
        UiActions.waitForPageReady();
        UiActions.dismissBlockingOverlays();
        waitForOpenedItemView(folder);
        return this;
    }

    public MailboxPage shouldContainMessageText(String token, Duration timeout, String errorMessage) {
        UiActions.waitForOpenedItemContains(token, timeout, errorMessage);
        return this;
    }

    public MailboxPage openSearchResult(String query, String resultToken, Duration timeout) {
        executeSearch(query);
        UiActions.waitForVisibleWithRetry(
            searchResultXpath(resultToken),
            timeout,
            Duration.ofSeconds(15),
            () -> executeSearch(query),
            "Search result containing '" + resultToken + "' was not found for query '" + query + "'"
        );

        searchResult(resultToken).click();
        UiActions.waitForPageReady();
        UiActions.dismissBlockingOverlays();
        UiActions.waitForOpenedMessageView();
        return this;
    }

    public MailboxPage setMessageFlag(MailFolder folder, String subject, boolean flagged, Duration timeout) {
        waitForMessageInFolder(folder, subject, timeout);
        openFolder(folder);
        messageLink(folder, subject).hover();
        if (!UiActions.hasVisibleElement(messageFlagButtonXpath(folder, subject, flagged))) {
            messageFlagButton(folder, subject).click();
        }
        waitForMessageFlagState(folder, subject, flagged, Duration.ofSeconds(20));
        return this;
    }

    public MailboxPage createCustomFolder(String folderName) {
        openInbox();
        UiActions.click(MailRuSelectors.NEW_FOLDER_BUTTON);
        UiActions.replaceText(MailRuSelectors.NEW_FOLDER_NAME_INPUT, folderName);
        UiActions.click(MailRuSelectors.NEW_FOLDER_SUBMIT_BUTTON);
        waitForCustomFolderVisible(folderName, Duration.ofSeconds(20));
        return this;
    }

    public MailboxPage deleteCustomFolder(String folderName) {
        waitForCustomFolderVisible(folderName, Duration.ofSeconds(20));
        UiActions.contextClick(customFolderXpath(folderName));
        UiActions.waitForVisibleMenuItem("Удалить папку");
        if (!UiActions.clickVisibleMenuItem("Удалить папку")) {
            throw new AssertionError("Folder delete action is not available for '" + folderName + "'");
        }
        UiActions.click(MailRuSelectors.DELETE_FOLDER_CONFIRM_BUTTON);
        waitForCustomFolderMissing(folderName, Duration.ofSeconds(20));
        return this;
    }

    public MailboxPage waitForCustomFolderVisible(String folderName, Duration timeout) {
        openInbox();
        UiActions.waitForVisibleWithRetry(
            customFolderXpath(folderName),
            timeout,
            Duration.ofSeconds(5),
            this::openInbox,
            "Custom folder '" + folderName + "' did not become visible"
        );
        return this;
    }

    public MailboxPage waitForCustomFolderMissing(String folderName, Duration timeout) {
        openInbox();
        UiActions.waitForHiddenWithRetry(
            customFolderXpath(folderName),
            timeout,
            Duration.ofSeconds(5),
            this::openInbox,
            "Custom folder '" + folderName + "' is still visible"
        );
        return this;
    }

    public MailboxPage deleteOpenedMessage() {
        UiActions.click(MailRuSelectors.MESSAGE_DELETE_BUTTON);
        return this;
    }

    public MailboxPage moveOpenedMessageToFolder(String folderName) {
        UiActions.click(MailRuSelectors.MESSAGE_MOVE_TO_FOLDER_BUTTON);
        UiActions.waitForVisibleMenuItem(folderName);
        if (!UiActions.clickVisibleMenuItem(folderName)) {
            throw new AssertionError("Folder '" + folderName + "' is not available in the move menu");
        }
        return this;
    }

    public MailboxPage permanentlyDeleteOpenedMessage() {
        if (UiActions.clickIfVisible(MailRuSelectors.MESSAGE_DELETE_FOREVER_BUTTON)) {
            return this;
        }

        if (UiActions.clickIfVisible(MailRuSelectors.MESSAGE_MORE_BUTTON)) {
            if (UiActions.waitForVisibleMenuItem("Удалить навсегда", Duration.ofSeconds(5))
                && UiActions.clickVisibleMenuItem("Удалить навсегда")) {
                return this;
            }
        }

        if (isTrashMessagePage() && UiActions.clickIfVisible(MailRuSelectors.MESSAGE_DELETE_BUTTON)) {
            return this;
        }

        throw new AssertionError("The 'Delete forever' action is not available in the message view");
    }

    public MailboxPage waitForMessageInFolder(MailFolder folder, String subject, Duration timeout) {
        openFolder(folder);
        UiActions.waitForVisibleWithRetry(
            messageLinkXpath(folder, subject),
            timeout,
            Duration.ofSeconds(15),
            () -> openFolder(folder),
            "Message with subject '" + subject + "' was not found in folder " + folder.segment()
        );
        return this;
    }

    public MailboxPage waitForMessageMissing(MailFolder folder, String subject, Duration timeout) {
        openFolder(folder);
        UiActions.waitForHiddenWithRetry(
            messageLinkXpath(folder, subject),
            timeout,
            Duration.ofSeconds(15),
            () -> openFolder(folder),
            "Message with subject '" + subject + "' is still visible in folder " + folder.segment()
        );
        return this;
    }

    public MailboxPage cleanupMessage(String subject) {
        deleteMessageIfPresent(MailFolder.DRAFTS, subject);
        deleteMessageIfPresent(MailFolder.SPAM, subject);
        deleteMessageIfPresent(MailFolder.TO_MYSELF, subject);
        deleteMessageIfPresent(MailFolder.SENT, subject);
        permanentlyDeleteMessagesIfPresent(MailFolder.TRASH, subject, Duration.ofSeconds(45));
        if (isMessageVisibleInFolder(MailFolder.TRASH, subject)) {
            waitForMessageMissing(MailFolder.TRASH, subject, Duration.ofSeconds(10));
        }
        return this;
    }

    public MailboxPage cleanupCustomFolder(String folderName) {
        openInbox();
        if (UiActions.hasVisibleElement(customFolderXpath(folderName))) {
            deleteCustomFolder(folderName);
        }
        return this;
    }

    private void deleteMessageIfPresent(MailFolder folder, String subject) {
        openFolder(folder);
        if (!isMessageVisibleInFolder(folder, subject)) {
            return;
        }

        messageLink(folder, subject).contextClick();
        if (UiActions.waitForVisibleMenuItem("Удалить", Duration.ofSeconds(5))
            && UiActions.clickVisibleMenuItem("Удалить")
            && waitForMessageMissingQuietly(folder, subject, Duration.ofSeconds(10))) {
            return;
        }

        messageLink(folder, subject).click();
        UiActions.waitForPageReady();
        UiActions.dismissBlockingOverlays();
        waitForOpenedItemView(folder);
        deleteOpenedMessage();
        waitForMessageMissing(folder, subject, Duration.ofSeconds(30));
    }

    private void permanentlyDeleteMessagesIfPresent(MailFolder folder, String subject, Duration timeout) {
        long deadline = System.nanoTime() + timeout.toNanos();

        while (System.nanoTime() < deadline) {
            openFolder(folder);
            if (!isMessageVisibleInFolder(folder, subject)) {
                return;
            }

            messageLink(folder, subject).contextClick();
            if (clickVisibleDeletePermanentlyFromList()
                && waitForMessageMissingQuietly(folder, subject, Duration.ofSeconds(10))) {
                return;
            }

            messageLink(folder, subject).click();
            UiActions.waitForPageReady();
            UiActions.dismissBlockingOverlays();
            waitForOpenedItemView(folder);
            permanentlyDeleteOpenedMessage();
            if (waitForMessageMissingQuietly(folder, subject, Duration.ofSeconds(10))) {
                return;
            }
        }

        throw new AssertionError("Timed out while permanently deleting messages with subject '" + subject + "'");
    }

    private boolean isMessageVisibleInFolder(MailFolder folder, String subject) {
        return UiActions.hasVisibleElement(messageLinkXpath(folder, subject));
    }

    private boolean isTrashMessagePage() {
        String currentUrl = WebDriverRunner.url();
        return currentUrl.contains("/trash") || currentUrl.contains("folder=trash");
    }

    private void waitForOpenedItemView(MailFolder folder) {
        if (folder == MailFolder.DRAFTS) {
            UiActions.waitForComposeEditor();
            return;
        }
        UiActions.waitForOpenedMessageView();
    }

    private void executeSearch(String query) {
        if (!UiActions.hasVisibleElement(MailRuSelectors.SEARCH_INPUT)) {
            openInbox();
            UiActions.click(MailRuSelectors.SEARCH_OPEN_BUTTON);
        }
        UiActions.replaceText(MailRuSelectors.SEARCH_INPUT, query);
        UiActions.pressEnter(MailRuSelectors.SEARCH_INPUT);
        UiActions.waitForSearchResultsPage();
        UiActions.dismissBlockingOverlays();
    }

    private void waitForMessageFlagState(MailFolder folder, String subject, boolean flagged, Duration timeout) {
        openFolder(folder);
        UiActions.waitForVisibleWithRetry(
            messageFlagButtonXpath(folder, subject, flagged),
            timeout,
            Duration.ofSeconds(5),
            () -> openFolder(folder),
            "Message '" + subject + "' did not switch to flag state " + flagged + " in folder " + folder.segment()
        );
    }

    private boolean waitForMessageMissingQuietly(MailFolder folder, String subject, Duration timeout) {
        try {
            waitForMessageMissing(folder, subject, timeout);
            return true;
        } catch (AssertionError ignored) {
            return false;
        }
    }

    private boolean clickVisibleDeletePermanentlyFromList() {
        if (UiActions.waitForVisibleMenuItem("Удалить навсегда", Duration.ofSeconds(5))
            && UiActions.clickVisibleMenuItem("Удалить навсегда")) {
            return true;
        }
        return UiActions.waitForVisibleMenuItem("Удалить", Duration.ofSeconds(2))
            && UiActions.clickVisibleMenuItem("Удалить");
    }

    private SelenideElement messageLink(MailFolder folder, String subject) {
        return UiActions.visible(messageLinkXpath(folder, subject), Duration.ofSeconds(20));
    }

    private SelenideElement searchResult(String token) {
        return UiActions.visible(searchResultXpath(token), Duration.ofSeconds(20));
    }

    private SelenideElement messageFlagButton(MailFolder folder, String subject) {
        return UiActions.visible(messageFlagButtonXpath(folder, subject), Duration.ofSeconds(20));
    }

    private String messageLinkXpath(MailFolder folder, String subject) {
        return "//a[contains(@href, '/" + folder.segment() + "/') and contains(., " + UiActions.xpathLiteral(subject) + ")]";
    }

    private String messageFlagButtonXpath(MailFolder folder, String subject) {
        return messageLinkXpath(folder, subject) + "//*[contains(@class, 'llc__item_flag')]//button[contains(@class, 'll-fs')]";
    }

    private String messageFlagButtonXpath(MailFolder folder, String subject, boolean flagged) {
        String baseXpath = messageFlagButtonXpath(folder, subject);
        if (flagged) {
            return baseXpath + "[contains(@class, 'll-fs_is-active') or contains(@title, 'Снять')]";
        }
        return baseXpath + "[not(contains(@class, 'll-fs_is-active')) and not(contains(@title, 'Снять'))]";
    }

    private String searchResultXpath(String token) {
        return "//a[contains(@href, '/search/') and contains(., " + UiActions.xpathLiteral(token) + ")]";
    }

    private String customFolderXpath(String folderName) {
        return "//a[contains(@class, 'nav__item') and .//*[normalize-space()=" + UiActions.xpathLiteral(folderName) + "]]";
    }
}
