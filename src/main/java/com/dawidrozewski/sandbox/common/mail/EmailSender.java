package com.dawidrozewski.sandbox.common.mail;

public interface EmailSender {
    void send(String to, String subject, String text);
}
