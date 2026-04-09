package ru.itmo.testing.lab3.pages;

import com.codeborne.selenide.Selenide;

import java.time.Duration;

import static ru.itmo.testing.lab3.support.MailRuSelectors.LOGIN_CONTINUE_BUTTON;
import static ru.itmo.testing.lab3.support.MailRuSelectors.LOGIN_INPUT;
import static ru.itmo.testing.lab3.support.MailRuSelectors.LOGIN_PAGE;
import static ru.itmo.testing.lab3.support.MailRuSelectors.LOGIN_URL;
import static ru.itmo.testing.lab3.support.UiActions.click;
import static ru.itmo.testing.lab3.support.UiActions.replaceText;
import static ru.itmo.testing.lab3.support.UiActions.visible;
import static ru.itmo.testing.lab3.support.UiActions.waitForMailInbox;

public final class LoginPage {

    public LoginPage open() {
        Selenide.open(LOGIN_URL);
        visible(LOGIN_PAGE);
        return this;
    }

    public LoginPage typeMailboxName(String mailbox) {
        replaceText(LOGIN_INPUT, mailbox);
        return this;
    }

    public LoginPage continueWithMailbox() {
        click(LOGIN_CONTINUE_BUTTON);
        return this;
    }

    public void waitForManualLoginCompletion(Duration timeout) {
        waitForMailInbox(timeout);
    }
}
