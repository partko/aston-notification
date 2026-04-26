package com.example.notificationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO запроса на отправку email.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String subject;

    @NotBlank
    private String text;

}