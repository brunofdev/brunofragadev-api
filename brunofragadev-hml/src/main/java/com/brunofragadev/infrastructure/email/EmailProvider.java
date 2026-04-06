package com.brunofragadev.infrastructure.email;

public interface EmailProvider {
    void send(String targetEmail, String name, String subject, String body);
}
