package ru.itmo.testing.lab3.support;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInfo;

import java.nio.file.Path;

public abstract class BaseUiTest {

    private BrowserType activeBrowser;

    @FunctionalInterface
    protected interface ThrowingAction {
        void run() throws Exception;
    }

    protected void useBrowser(BrowserType browser, Path profileDir) {
        activeBrowser = browser;
        SelenideEnvironment.configureFor(browser, profileDir);
    }

    protected final void runWithCleanup(ThrowingAction body, ThrowingAction... cleanupActions) {
        Throwable failure = null;

        try {
            body.run();
        } catch (Throwable throwable) {
            failure = throwable;
        }

        for (ThrowingAction cleanupAction : cleanupActions) {
            if (cleanupAction == null) {
                continue;
            }
            try {
                cleanupAction.run();
            } catch (Throwable throwable) {
                if (failure == null) {
                    failure = throwable;
                } else {
                    failure.addSuppressed(throwable);
                }
            }
        }

        rethrowIfNeeded(failure);
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        try {
            if (activeBrowser != null && WebDriverRunner.hasWebDriverStarted()) {
                TestArtifacts.captureForTest(activeBrowser, testInfo);
            }
        } finally {
            Selenide.closeWebDriver();
            activeBrowser = null;
        }
    }

    private void rethrowIfNeeded(Throwable throwable) {
        if (throwable == null) {
            return;
        }
        if (throwable instanceof RuntimeException runtimeException) {
            throw runtimeException;
        }
        if (throwable instanceof Error error) {
            throw error;
        }
        throw new AssertionError("Test cleanup failed", throwable);
    }
}
