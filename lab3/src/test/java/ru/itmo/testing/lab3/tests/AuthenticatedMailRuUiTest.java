package ru.itmo.testing.lab3.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assumptions;
import ru.itmo.testing.lab3.model.AuthMessage;
import ru.itmo.testing.lab3.pages.ComposePage;
import ru.itmo.testing.lab3.pages.MailboxPage;
import ru.itmo.testing.lab3.support.BaseUiTest;
import ru.itmo.testing.lab3.support.BrowserType;
import ru.itmo.testing.lab3.support.Lab3Properties;
import ru.itmo.testing.lab3.support.MailFolder;

import java.time.Duration;

@Tag("auth")
class AuthenticatedMailRuUiTest extends BaseUiTest {

    @BeforeEach
    void requirePreparedProfile() {
        Assumptions.assumeTrue(
            Lab3Properties.authConfigured(),
            "Authenticated tests require -Dlab3.auth.login and -Dlab3.auth.profileDir"
        );
        useBrowser(BrowserType.CHROME, Lab3Properties.authProfileDir());
    }

    @Test
    void authenticatedInboxLayout() {
        new MailboxPage()
            .openInbox()
            .shouldShowShell();
    }

    @Test
    void authenticatedSendAndDeleteSelfMail() {
        String mailbox = Lab3Properties.resolvedMailboxAddress();
        AuthMessage message = AuthMessage.newMessage("SELF");
        MailboxPage mailboxPage = new MailboxPage();

        runWithCleanup(() -> {
            new ComposePage().sendMessageTo(mailbox, message);

            mailboxPage
                .openMessageBySubject(MailFolder.SENT, message.subject(), Duration.ofMinutes(2))
                .shouldContainMessageText(message.subject(), Duration.ofSeconds(20), "Sent message subject is not visible")
                .shouldContainMessageText(message.body(), Duration.ofSeconds(20), "Sent message body marker is not visible");

            mailboxPage
                .openMessageBySubject(MailFolder.TO_MYSELF, message.subject(), Duration.ofMinutes(3))
                .shouldContainMessageText(message.subject(), Duration.ofSeconds(20), "Self-mail subject is not visible")
                .shouldContainMessageText(message.body(), Duration.ofSeconds(20), "Self-mail body marker is not visible")
                .deleteOpenedMessage()
                .waitForMessageInFolder(MailFolder.TRASH, message.subject(), Duration.ofMinutes(2));
        }, () -> mailboxPage.cleanupMessage(message.subject()));
    }

    @Test
    void authenticatedDraftsFlow() {
        String mailbox = Lab3Properties.resolvedMailboxAddress();
        AuthMessage originalMessage = AuthMessage.newMessage("DRAFT");
        String updatedSubject = originalMessage.subject() + "_EDITED";
        String updatedBody = originalMessage.body() + "_EDITED";
        MailboxPage mailboxPage = new MailboxPage();

        runWithCleanup(() -> {
            new ComposePage().saveDraftFor(mailbox, originalMessage);

            mailboxPage
                .openMessageBySubject(MailFolder.DRAFTS, originalMessage.subject(), Duration.ofMinutes(2))
                .shouldContainMessageText(originalMessage.subject(), Duration.ofSeconds(20), "Draft subject is not visible")
                .shouldContainMessageText(originalMessage.body(), Duration.ofSeconds(20), "Draft body marker is not visible");

            new ComposePage()
                .typeSubject(updatedSubject)
                .typeBody(updatedBody)
                .send();

            mailboxPage
                .waitForMessageMissing(MailFolder.DRAFTS, originalMessage.subject(), Duration.ofMinutes(1))
                .openMessageBySubject(MailFolder.SENT, updatedSubject, Duration.ofMinutes(2))
                .shouldContainMessageText(updatedSubject, Duration.ofSeconds(20), "Edited draft subject is not visible in Sent")
                .shouldContainMessageText(updatedBody, Duration.ofSeconds(20), "Edited draft body marker is not visible in Sent")
                .openMessageBySubject(MailFolder.TO_MYSELF, updatedSubject, Duration.ofMinutes(3))
                .shouldContainMessageText(updatedSubject, Duration.ofSeconds(20), "Edited draft subject is not visible in To Myself")
                .shouldContainMessageText(updatedBody, Duration.ofSeconds(20), "Edited draft body marker is not visible in To Myself");
        }, () -> mailboxPage.cleanupMessage(updatedSubject), () -> mailboxPage.cleanupMessage(originalMessage.subject()));
    }

    @Test
    void authenticatedTrashRestoreFlow() {
        String mailbox = Lab3Properties.resolvedMailboxAddress();
        AuthMessage message = AuthMessage.newMessage("TRASH_RESTORE");
        MailboxPage mailboxPage = new MailboxPage();

        runWithCleanup(() -> {
            new ComposePage().sendMessageTo(mailbox, message);

            mailboxPage
                .openMessageBySubject(MailFolder.TO_MYSELF, message.subject(), Duration.ofMinutes(3))
                .shouldContainMessageText(message.subject(), Duration.ofSeconds(20), "Self-mail subject is not visible before deletion")
                .shouldContainMessageText(message.body(), Duration.ofSeconds(20), "Self-mail body marker is not visible before deletion")
                .deleteOpenedMessage()
                .openMessageBySubject(MailFolder.TRASH, message.subject(), Duration.ofMinutes(2))
                .shouldContainMessageText(message.subject(), Duration.ofSeconds(20), "Trash message subject is not visible")
                .shouldContainMessageText(message.body(), Duration.ofSeconds(20), "Trash message body marker is not visible")
                .moveOpenedMessageToFolder("Письма себе")
                .waitForMessageMissing(MailFolder.TRASH, message.subject(), Duration.ofMinutes(1))
                .openMessageBySubject(MailFolder.TO_MYSELF, message.subject(), Duration.ofMinutes(2))
                .shouldContainMessageText(message.subject(), Duration.ofSeconds(20), "Restored self-mail subject is not visible")
                .shouldContainMessageText(message.body(), Duration.ofSeconds(20), "Restored self-mail body marker is not visible");
        }, () -> mailboxPage.cleanupMessage(message.subject()));
    }

    @Test
    void authenticatedPermanentDeleteFromTrashFlow() {
        String mailbox = Lab3Properties.resolvedMailboxAddress();
        AuthMessage message = AuthMessage.newMessage("TRASH_DELETE");
        MailboxPage mailboxPage = new MailboxPage();

        runWithCleanup(() -> {
            new ComposePage().sendMessageTo(mailbox, message);

            mailboxPage
                .openMessageBySubject(MailFolder.TO_MYSELF, message.subject(), Duration.ofMinutes(3))
                .shouldContainMessageText(message.subject(), Duration.ofSeconds(20), "Self-mail subject is not visible before deletion")
                .shouldContainMessageText(message.body(), Duration.ofSeconds(20), "Self-mail body marker is not visible before deletion")
                .deleteOpenedMessage()
                .openMessageBySubject(MailFolder.TRASH, message.subject(), Duration.ofMinutes(2))
                .shouldContainMessageText(message.subject(), Duration.ofSeconds(20), "Trash message subject is not visible before permanent deletion")
                .shouldContainMessageText(message.body(), Duration.ofSeconds(20), "Trash message body marker is not visible before permanent deletion")
                .permanentlyDeleteOpenedMessage()
                .waitForMessageMissing(MailFolder.TRASH, message.subject(), Duration.ofMinutes(1));
        }, () -> mailboxPage.cleanupMessage(message.subject()));
    }

    @Test
    void authenticatedSearchFindsMessage() {
        String mailbox = Lab3Properties.resolvedMailboxAddress();
        AuthMessage message = AuthMessage.newMessage("SEARCH");
        MailboxPage mailboxPage = new MailboxPage();

        runWithCleanup(() -> {
            new ComposePage().sendMessageTo(mailbox, message);

            mailboxPage
                .openSearchResult(message.subject(), message.subject(), Duration.ofMinutes(3))
                .shouldContainMessageText(message.subject(), Duration.ofSeconds(20), "Search result subject is not visible")
                .shouldContainMessageText(message.body(), Duration.ofSeconds(20), "Search result body marker is not visible");
        }, () -> mailboxPage.cleanupMessage(message.subject()));
    }

    @Test
    void authenticatedFlagToggleFlow() {
        String mailbox = Lab3Properties.resolvedMailboxAddress();
        AuthMessage message = AuthMessage.newMessage("FLAG");
        MailboxPage mailboxPage = new MailboxPage();

        runWithCleanup(() -> {
            new ComposePage().sendMessageTo(mailbox, message);

            mailboxPage
                .setMessageFlag(MailFolder.TO_MYSELF, message.subject(), true, Duration.ofMinutes(3))
                .setMessageFlag(MailFolder.TO_MYSELF, message.subject(), false, Duration.ofSeconds(30));
        }, () -> mailboxPage.cleanupMessage(message.subject()));
    }

    @Test
    void authenticatedCustomFolderLifecycle() {
        String folderName = AuthMessage.newMessage("FOLDER").subject();
        MailboxPage mailboxPage = new MailboxPage();

        runWithCleanup(() -> {
            mailboxPage
                .createCustomFolder(folderName)
                .deleteCustomFolder(folderName);
        }, () -> mailboxPage.cleanupCustomFolder(folderName));
    }
}
