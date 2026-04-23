package ru.itmo.testing.lab3.tools;

import com.codeborne.selenide.Selenide;
import ru.itmo.testing.lab3.pages.LoginPage;
import ru.itmo.testing.lab3.support.BrowserType;
import ru.itmo.testing.lab3.support.Lab3Properties;
import ru.itmo.testing.lab3.support.SelenideEnvironment;
import ru.itmo.testing.lab3.support.TestArtifacts;

import java.time.Duration;

public final class MailRuLoginAssistant {

    private MailRuLoginAssistant() {
    }

    public static void main(String[] args) {
        if (!Lab3Properties.authConfigured()) {
            throw new IllegalArgumentException("assistLogin requires -Dlab3.auth.login and -Dlab3.auth.profileDir");
        }

        SelenideEnvironment.configureFor(BrowserType.CHROME, Lab3Properties.authProfileDir());
        try {
            new LoginPage()
                .open()
                .typeMailboxName(Lab3Properties.authLogin())
                .continueWithMailbox()
                .waitForManualLoginCompletion(Duration.ofMinutes(15));

            TestArtifacts.capture(
                BrowserType.CHROME,
                "chrome-after-login",
                Lab3Properties.assistLoginArtifactsRoot()
            );
            System.out.println("Login detected. Profile saved in: " + Lab3Properties.authProfileDir());
        } finally {
            Selenide.closeWebDriver();
        }
    }
}
