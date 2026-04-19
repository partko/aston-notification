package com.example.notificationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO запроса на отправку email.
 */
public class SendEmailRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String subject;

    @NotBlank
    private String text;

    public SendEmailRequest() {
    }

    public SendEmailRequest(String email, String subject, String text) {
        this.email = email;
        this.subject = subject;
        this.text = text;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}