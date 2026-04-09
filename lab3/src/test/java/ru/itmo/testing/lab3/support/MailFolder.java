package ru.itmo.testing.lab3.support;

public enum MailFolder {
    INBOX("inbox", "https://e.mail.ru/inbox", "//a[contains(@href, '/inbox/')]"),
    TO_MYSELF("tomyself", "https://e.mail.ru/tomyself/", "//a[contains(@href, '/tomyself/')]"),
    SENT("sent", "https://e.mail.ru/sent/", "//a[contains(@href, '/sent/')]"),
    DRAFTS("drafts", "https://e.mail.ru/drafts/", "//a[contains(@href, '/drafts/')]"),
    SPAM("spam", "https://e.mail.ru/spam/", "//a[contains(@href, '/spam/')]"),
    TRASH("trash", "https://e.mail.ru/trash/", "//a[contains(@href, '/trash/')]");

    private final String segment;
    private final String url;
    private final String sidebarXpath;

    MailFolder(String segment, String url, String sidebarXpath) {
        this.segment = segment;
        this.url = url;
        this.sidebarXpath = sidebarXpath;
    }

    public String segment() {
        return segment;
    }

    public String url() {
        return url;
    }

    public String sidebarXpath() {
        return sidebarXpath;
    }
}
