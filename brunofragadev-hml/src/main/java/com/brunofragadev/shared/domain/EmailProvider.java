package com.brunofragadev.shared.domain;

public interface EmailProvider {
    void send(String targetEmail, String name, String subject, String body);
}
