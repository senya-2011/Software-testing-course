package ru.itmo.testing.lab3.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.itmo.testing.lab3.model.AuthMessage;
import ru.itmo.testing.lab3.support.MailFolder;
import ru.itmo.testing.lab3.support.MailRuSelectors;
import ru.itmo.testing.lab3.support.UiActions;

import java.time.Duration;

import static com.codeborne.selenide.Condition.interactable;

public final class ComposePage {

    public ComposePage open() {
        UiActions.openAuthenticatedPage(MailRuSelectors.COMPOSE_URL);
        return this;
    }

    public ComposePage typeRecipient(String mailbox) {
        SelenideElement input = UiActions.visible(MailRuSelectors.COMPOSE_TO_INPUT);
        input.shouldBe(interactable).setValue(mailbox).pressEnter();
        UiActions.visible(
            "//*[@data-name='to']//*[contains(normalize-space(.), " + UiActions.xpathLiteral(mailbox) + ")]"
        );
        return this;
    }

    public ComposePage typeSubject(String subject) {
        UiActions.replaceText(MailRuSelectors.COMPOSE_SUBJECT_INPUT, subject);
        UiActions.visible(MailRuSelectors.COMPOSE_SUBJECT_INPUT).shouldHave(Condition.value(subject));
        return this;
    }

    public ComposePage typeBody(String bodyText) {
        UiActions.fillComposeBody(bodyText);
        return this;
    }

    public ComposePage send() {
        UiActions.click(MailRuSelectors.COMPOSE_SEND_BUTTON);
        UiActions.waitForSendAcceptance();
        return this;
    }

    public ComposePage saveDraft() {
        UiActions.click(MailRuSelectors.COMPOSE_SAVE_BUTTON);
        return this;
    }

    public ComposePage sendMessageTo(String mailbox, AuthMessage message) {
        return open()
            .typeRecipient(mailbox)
            .typeSubject(message.subject())
            .typeBody(message.body())
            .send();
    }

    public ComposePage saveDraftFor(String mailbox, AuthMessage message) {
        ComposePage composePage = open()
            .typeRecipient(mailbox)
            .typeSubject(message.subject())
            .typeBody(message.body())
            .saveDraft();
        new MailboxPage().waitForMessageInFolder(MailFolder.DRAFTS, message.subject(), Duration.ofMinutes(2));
        return composePage;
    }
}
