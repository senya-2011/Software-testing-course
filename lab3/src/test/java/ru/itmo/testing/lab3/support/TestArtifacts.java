package ru.itmo.testing.lab3.support;

import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class TestArtifacts {

    private TestArtifacts() {
    }

    public static void captureForTest(BrowserType browser, TestInfo testInfo) {
        String artifactBaseName = Lab3Properties.sanitizedName(browser.id() + "-" + testInfo.getDisplayName());
        capture(browser, artifactBaseName, Lab3Properties.browserArtifactsRoot(browser));
    }

    public static void capture(BrowserType browser, String artifactBaseName, Path rootDir) {
        if (!WebDriverRunner.hasWebDriverStarted()) {
            return;
        }

        Path screenshotsDir = Lab3Properties.screenshotsRoot(rootDir);
        Path pagesDir = Lab3Properties.pagesRoot(rootDir);
        Path metadataDir = Lab3Properties.metadataRoot(rootDir);

        try {
            Files.write(
                screenshotsDir.resolve(artifactBaseName + ".png"),
                ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES)
            );
            Files.writeString(
                pagesDir.resolve(artifactBaseName + ".html"),
                WebDriverRunner.source(),
                StandardCharsets.UTF_8
            );
            Files.writeString(
                metadataDir.resolve(artifactBaseName + ".txt"),
                "Browser: " + browser.id() + System.lineSeparator()
                    + "Title: " + WebDriverRunner.getWebDriver().getTitle() + System.lineSeparator()
                    + "Current URL: " + WebDriverRunner.url() + System.lineSeparator(),
                StandardCharsets.UTF_8
            );
        } catch (IOException ioException) {
            throw new IllegalStateException("Unable to persist test artifacts for " + artifactBaseName, ioException);
        }
    }
}
