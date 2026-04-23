package ru.itmo.testing.lab3.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record AuthMessage(String subject, String body) {

    private static final DateTimeFormatter ID_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public static AuthMessage newMessage(String prefix) {
        String suffix = LocalDateTime.now().format(ID_FORMAT);
        return new AuthMessage("LAB3_" + prefix + "_" + suffix, "LAB3_" + prefix + "_BODY_" + suffix);
    }
}
