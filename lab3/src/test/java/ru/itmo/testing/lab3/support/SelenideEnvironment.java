package ru.itmo.testing.lab3.support;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;

import java.nio.file.Path;
import java.time.Duration;

public final class SelenideEnvironment {

    private SelenideEnvironment() {
    }

    public static void configureFor(BrowserType browser, Path profileDir) {
        Selenide.closeWebDriver();
        Configuration.browser = browser.browserName();
        Configuration.browserCapabilities = browser.createCapabilities(profileDir);
        Configuration.browserSize = "1600x1000";
        Configuration.timeout = Duration.ofSeconds(25).toMillis();
        Configuration.pageLoadTimeout = Duration.ofSeconds(30).toMillis();
        Configuration.headless = Lab3Properties.headless();
        Configuration.reportsFolder = Lab3Properties.createDirectory(
            Lab3Properties.browserArtifactsRoot(browser).resolve("reports")
        ).toString();
        Configuration.savePageSource = false;
        Configuration.reopenBrowserOnFail = false;
    }

}
