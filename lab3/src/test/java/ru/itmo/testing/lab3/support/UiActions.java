package ru.itmo.testing.lab3.support;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.interactable;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.actions;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public final class UiActions {

    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(25);

    private UiActions() {
    }

    public static void openAuthenticatedPage(String url) {
        open(url);
        waitForPageReady();
        waitForAuthenticatedShell(DEFAULT_TIMEOUT);
        dismissBlockingOverlays();
    }

    public static ElementsCollection elements(String xpath) {
        return $$x(xpath);
    }

    public static SelenideElement visible(String xpath) {
        return visible(xpath, DEFAULT_TIMEOUT);
    }

    public static SelenideElement visible(String xpath, Duration timeout) {
        return $x(xpath).shouldBe(Condition.visible, timeout);
    }

    public static SelenideElement anyVisible(String xpath, Duration timeout) {
        return elements(xpath)
            .filter(Condition.visible)
            .shouldHave(CollectionCondition.sizeGreaterThan(0), timeout)
            .first();
    }

    public static boolean hasVisibleElement(String xpath) {
        return !elements(xpath).filter(Condition.visible).isEmpty();
    }

    public static void click(String xpath) {
        visible(xpath).shouldBe(Condition.clickable, DEFAULT_TIMEOUT).click();
    }

    public static void contextClick(String xpath) {
        visible(xpath).contextClick();
    }

    public static boolean clickIfVisible(String xpath) {
        if (!hasVisibleElement(xpath)) {
            return false;
        }
        anyVisible(xpath, DEFAULT_TIMEOUT).shouldBe(Condition.clickable).click();
        return true;
    }

    public static void replaceText(String xpath, String value) {
        visible(xpath).shouldBe(Condition.editable, DEFAULT_TIMEOUT).setValue(value);
    }

    public static void pressEnter(String xpath) {
        visible(xpath).shouldBe(interactable, DEFAULT_TIMEOUT).pressEnter();
    }

    public static void waitForOpenedItemContains(String token, Duration timeout, String errorMessage) {
        if (hasVisibleElement(MailRuSelectors.COMPOSE_EDITOR_READY_MARKERS)
            || hasVisibleElement(MailRuSelectors.COMPOSE_SUBJECT_INPUT)) {
            waitForOpenedComposeContains(token, timeout, errorMessage);
            return;
        }
        waitForOpenedMessageContains(token, timeout, errorMessage);
    }

    public static void waitForOpenedComposeContains(String token, Duration timeout, String errorMessage) {
        try {
            anyVisible(composeContainsXpath(token), timeout);
        } catch (RuntimeException runtimeException) {
            throw new AssertionError(errorMessage, runtimeException);
        }
    }

    public static void waitForOpenedMessageContains(String token, Duration timeout, String errorMessage) {
        try {
            anyVisible(messageViewContainsXpath(token), timeout);
        } catch (RuntimeException runtimeException) {
            throw new AssertionError(errorMessage, runtimeException);
        }
    }

    public static void waitForMailInbox(Duration timeout) {
        waitForAuthenticatedShell(timeout);
        visible(MailFolder.INBOX.sidebarXpath(), timeout);
    }

    public static void waitForAuthenticatedShell(Duration timeout) {
        try {
            webdriver().shouldHave(urlContaining("e.mail.ru"), timeout);
            visible(MailRuSelectors.MAIL_APP_WRITE_BUTTON, timeout);
        } catch (RuntimeException runtimeException) {
            throw new AssertionError("Authenticated Mail.ru shell did not load", runtimeException);
        }
    }

    public static void waitForPageReady() {
        try {
            visible(MailRuSelectors.PAGE_BODY, DEFAULT_TIMEOUT);
        } catch (RuntimeException runtimeException) {
            throw new AssertionError("Page body did not become visible", runtimeException);
        }
    }

    public static void waitForSearchResultsPage() {
        try {
            webdriver().shouldHave(urlContaining("/search/"), DEFAULT_TIMEOUT);
            visible(MailRuSelectors.SEARCH_INPUT, DEFAULT_TIMEOUT);
        } catch (RuntimeException runtimeException) {
            throw new AssertionError("Search results page did not open", runtimeException);
        }
    }

    public static void waitForOpenedMessageView() {
        try {
            anyVisible(MailRuSelectors.MESSAGE_VIEW_ACTIONS, DEFAULT_TIMEOUT);
        } catch (RuntimeException runtimeException) {
            throw new AssertionError("Opened message view did not become interactive", runtimeException);
        }
    }

    public static void waitForComposeEditor() {
        try {
            visible(MailRuSelectors.COMPOSE_SUBJECT_INPUT, DEFAULT_TIMEOUT);
            anyVisible(MailRuSelectors.COMPOSE_EDITOR_READY_MARKERS, DEFAULT_TIMEOUT);
        } catch (RuntimeException runtimeException) {
            throw new AssertionError("Compose editor did not become interactive", runtimeException);
        }
    }

    public static void fillComposeBody(String bodyText) {
        SelenideElement editor = visible(MailRuSelectors.COMPOSE_BODY_EDITOR)
            .shouldBe(interactable, DEFAULT_TIMEOUT);

        Throwable lastFailure = null;
        for (int attempt = 0; attempt < 2; attempt++) {
            try {
                focusComposeEditor(editor);
                actions()
                    .pause(Duration.ofMillis(150))
                    .sendKeys(bodyText)
                    .perform();
                editor.shouldHave(Condition.text(bodyText), Duration.ofSeconds(10));
                return;
            } catch (RuntimeException | AssertionError exception) {
                lastFailure = exception;
                editor.shouldBe(interactable, Duration.ofSeconds(2));
            }
        }

        throw new AssertionError("Compose body marker was not inserted into the editor", lastFailure);
    }

    public static void waitForSendAcceptance() {
        try {
            anyVisible(MailRuSelectors.SEND_ACCEPTANCE_MARKERS, Duration.ofSeconds(20));
        } catch (RuntimeException runtimeException) {
            throw new AssertionError("Message sending was not accepted by the UI", runtimeException);
        }
    }

    public static void dismissBlockingOverlays() {
        if (clickIfVisible(MailRuSelectors.ACCEPT_ALL_BUTTON)) {
            waitForElementToDisappear(
                MailRuSelectors.ACCEPT_ALL_BUTTON,
                Duration.ofSeconds(10),
                "Cookie consent overlay did not close"
            );
        }
        if (clickIfVisible(MailRuSelectors.DISMISS_HELPER_BUTTON)) {
            waitForElementToDisappear(
                MailRuSelectors.DISMISS_HELPER_BUTTON,
                Duration.ofSeconds(10),
                "Blocking helper overlay did not close"
            );
        }
    }

    public static boolean clickVisibleMenuItem(String label) {
        String menuItemXpath = menuItemXpath(label);
        if (!hasVisibleElement(menuItemXpath)) {
            return false;
        }
        anyVisible(menuItemXpath, DEFAULT_TIMEOUT).shouldBe(Condition.clickable).click();
        return true;
    }

    public static void waitForVisibleMenuItem(String label) {
        if (!waitForVisibleMenuItem(label, DEFAULT_TIMEOUT)) {
            throw new AssertionError("Menu item '" + label + "' did not become visible");
        }
    }

    public static boolean waitForVisibleMenuItem(String label, Duration timeout) {
        try {
            anyVisible(menuItemXpath(label), timeout);
            return true;
        } catch (AssertionError ignored) {
            return false;
        }
    }

    public static String xpathLiteral(String value) {
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        if (!value.contains("\"")) {
            return "\"" + value + "\"";
        }

        StringBuilder builder = new StringBuilder("concat(");
        String[] parts = value.split("'");
        for (int index = 0; index < parts.length; index++) {
            if (index > 0) {
                builder.append(", \"'\", ");
            }
            builder.append("'").append(parts[index]).append("'");
        }
        builder.append(")");
        return builder.toString();
    }

    public static void waitForElementToDisappear(String xpath, Duration timeout, String errorMessage) {
        try {
            elements(xpath).filter(Condition.visible).shouldBe(CollectionCondition.empty, timeout);
        } catch (RuntimeException runtimeException) {
            throw new AssertionError(errorMessage, runtimeException);
        }
    }

    public static void waitForVisibleWithRetry(
        String xpath,
        Duration timeout,
        Duration attemptTimeout,
        Runnable retryAction,
        String errorMessage
    ) {
        retryAssertion(
            timeout,
            attemptTimeout,
            retryAction,
            currentAttemptTimeout -> anyVisible(xpath, currentAttemptTimeout),
            errorMessage
        );
    }

    public static void waitForHiddenWithRetry(
        String xpath,
        Duration timeout,
        Duration attemptTimeout,
        Runnable retryAction,
        String errorMessage
    ) {
        retryAssertion(
            timeout,
            attemptTimeout,
            retryAction,
            currentAttemptTimeout -> elements(xpath)
                .filter(Condition.visible)
                .shouldBe(CollectionCondition.empty, currentAttemptTimeout),
            errorMessage
        );
    }

    private static String menuItemXpath(String label) {
        String value = xpathLiteral(label);
        return "("
            + "//*[contains(@class, 'dropdown__menu')]"
            + "//*[contains(@class, 'list-item__text') or contains(@class, 'list-item__text-txt')]"
            + "[normalize-space()=" + value + "]/ancestor::*[contains(@class, 'list-item')][1]"
            + " | "
            + "//*[contains(@class, 'contextmenu')]"
            + "//*[contains(@class, 'list-item__text') or contains(@class, 'list-item__text-txt')]"
            + "[normalize-space()=" + value + "]/ancestor::*[contains(@class, 'list-item')][1]"
            + " | "
            + "//*[contains(@class, 'list-nested-moveto-folder')]"
            + "//*[contains(@class, 'list-item__text') or contains(@class, 'list-item__text-txt')]"
            + "[normalize-space()=" + value + "]/ancestor::*[contains(@class, 'list-item')][1]"
            + " | "
            + "//*[contains(@class, 'dropdown__menu')]//*[@title=" + value + "]/ancestor-or-self::*[contains(@class, 'list-item')][1]"
            + ")";
    }

    private static String composeContainsXpath(String token) {
        String tokenLiteral = xpathLiteral(token);
        return "("
            + MailRuSelectors.COMPOSE_SUBJECT_INPUT + "[contains(@value, " + tokenLiteral + ")]"
            + " | "
            + MailRuSelectors.COMPOSE_BODY_EDITOR + "[contains(., " + tokenLiteral + ")]"
            + " | "
            + MailRuSelectors.COMPOSE_RECIPIENTS_CONTAINER + "[contains(., " + tokenLiteral + ")]"
            + ")";
    }

    private static String messageViewContainsXpath(String token) {
        String tokenLiteral = xpathLiteral(token);
        return "("
            + MailRuSelectors.MESSAGE_VIEW_TEXT_ROOT + "[contains(., " + tokenLiteral + ")]"
            + " | "
            + MailRuSelectors.MESSAGE_VIEW_FALLBACK_ROOT + "[contains(., " + tokenLiteral + ")]"
            + ")";
    }

    private static void focusComposeEditor(SelenideElement editor) {
        try {
            actions()
                .moveToElement(editor, editorTopLeftOffset(editor.getRect().getWidth()), editorTopLeftOffset(editor.getRect().getHeight()))
                .pause(Duration.ofMillis(150))
                .click()
                .perform();
        } catch (RuntimeException | AssertionError ignored) {
            actions()
                .moveToElement(editor)
                .pause(Duration.ofMillis(150))
                .click()
                .perform();
        }
    }

    private static int editorTopLeftOffset(int size) {
        return -Math.max((size / 2) - 24, 0);
    }

    private static void retryAssertion(
        Duration totalTimeout,
        Duration attemptTimeout,
        Runnable retryAction,
        TimedAssertion assertion,
        String errorMessage
    ) {
        long deadlineNanos = System.nanoTime() + totalTimeout.toNanos();
        Throwable lastFailure = null;

        while (true) {
            Duration remaining = Duration.ofNanos(Math.max(deadlineNanos - System.nanoTime(), 0L));
            if (remaining.isZero() || remaining.isNegative()) {
                break;
            }

            Duration currentAttemptTimeout = remaining.compareTo(attemptTimeout) > 0 ? attemptTimeout : remaining;
            try {
                assertion.run(currentAttemptTimeout);
                return;
            } catch (RuntimeException | AssertionError throwable) {
                lastFailure = throwable;
            }

            if (retryAction == null || currentAttemptTimeout.equals(remaining)) {
                break;
            }

            retryAction.run();
        }

        throw new AssertionError(errorMessage, lastFailure);
    }

    @FunctionalInterface
    private interface TimedAssertion {
        void run(Duration timeout);
    }
}
