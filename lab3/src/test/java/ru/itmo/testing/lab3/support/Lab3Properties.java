package ru.itmo.testing.lab3.support;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public final class Lab3Properties {

    private Lab3Properties() {
    }

    public static boolean headless() {
        return Boolean.parseBoolean(System.getProperty("lab3.headless", "true"));
    }

    public static Path artifactsRoot() {
        return createDirectory(Path.of(System.getProperty("lab3.artifactsDir", "artifacts")).toAbsolutePath());
    }

    public static Path junitArtifactsRoot() {
        return createDirectory(artifactsRoot().resolve("junit"));
    }

    public static Path browserArtifactsRoot(BrowserType browser) {
        return createDirectory(junitArtifactsRoot().resolve(browser.id()));
    }

    public static Path assistLoginArtifactsRoot() {
        return createDirectory(artifactsRoot().resolve("assist-login"));
    }

    public static Path screenshotsRoot(Path root) {
        return createDirectory(root.resolve("screenshots"));
    }

    public static Path pagesRoot(Path root) {
        return createDirectory(root.resolve("pages"));
    }

    public static Path metadataRoot(Path root) {
        return createDirectory(root.resolve("meta"));
    }

    public static String authLogin() {
        return System.getProperty("lab3.auth.login", "").trim();
    }

    public static Path authProfileDir() {
        String rawValue = System.getProperty("lab3.auth.profileDir", "").trim();
        return rawValue.isBlank() ? null : Path.of(rawValue).toAbsolutePath();
    }

    public static boolean authConfigured() {
        return !authLogin().isBlank() && authProfileDir() != null;
    }

    public static String resolvedMailboxAddress() {
        String login = authLogin();
        if (login.isBlank()) {
            return "";
        }
        return login.contains("@") ? login : login + "@mail.ru";
    }

    public static String sanitizedName(String rawValue) {
        return rawValue
            .replace("https://", "")
            .replace("http://", "")
            .replaceAll("[^a-zA-Z0-9._-]+", "_")
            .replaceAll("_+", "_")
            .replaceAll("^_|_$", "")
            .toLowerCase(Locale.ROOT);
    }

    public static Path createDirectory(Path directory) {
        try {
            Files.createDirectories(directory);
            return directory;
        } catch (IOException ioException) {
            throw new IllegalStateException("Unable to create directory: " + directory, ioException);
        }
    }
}
