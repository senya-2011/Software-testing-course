package ru.itmo.testing.lab3.support;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public enum BrowserType {
    CHROME("chrome") {
        @Override
        public MutableCapabilities createCapabilities(Path profileDir) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-search-engine-choice-screen");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");
            options.addArguments("--no-first-run");
            options.addArguments("--no-default-browser-check");
            if (profileDir != null) {
                options.addArguments("--user-data-dir=" + ensureDirectory(profileDir));
            }
            resolveBinary(System.getProperty("lab3.browser.binary.chrome"), CHROME_BINARIES)
                .ifPresent(path -> options.setBinary(path.toString()));
            return options;
        }
    },
    FIREFOX("firefox") {
        @Override
        public MutableCapabilities createCapabilities(Path profileDir) {
            FirefoxOptions options = new FirefoxOptions();
            resolveBinary(System.getProperty("lab3.browser.binary.firefox"), FIREFOX_BINARIES)
                .ifPresent(options::setBinary);
            return options;
        }
    };

    private static final List<Path> CHROME_BINARIES = List.of(
        Path.of("tools", "chrome", "chrome-linux64", "chrome"),
        Path.of("/snap/chromium/current/usr/lib/chromium-browser/chrome")
    );
    private static final List<Path> FIREFOX_BINARIES = List.of(
        Path.of("tools", "firefox", "firefox", "firefox"),
        Path.of("/snap/firefox/current/usr/lib/firefox/firefox")
    );

    private final String browserName;

    BrowserType(String browserName) {
        this.browserName = browserName;
    }

    public String browserName() {
        return browserName;
    }

    public String id() {
        return browserName;
    }

    public abstract MutableCapabilities createCapabilities(Path profileDir);

    private static Optional<Path> resolveBinary(String configuredPath, List<Path> candidates) {
        if (configuredPath != null && !configuredPath.isBlank()) {
            Path explicitPath = Path.of(configuredPath).toAbsolutePath();
            if (Files.exists(explicitPath)) {
                return Optional.of(explicitPath);
            }
        }

        return candidates.stream()
            .map(Path::toAbsolutePath)
            .filter(Files::exists)
            .findFirst();
    }

    private static String ensureDirectory(Path directory) {
        try {
            Files.createDirectories(directory);
            return directory.toAbsolutePath().toString();
        } catch (IOException ioException) {
            throw new IllegalStateException("Unable to prepare browser profile directory: " + directory, ioException);
        }
    }
}
