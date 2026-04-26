package com.example.notificationservice.service;

/**
 * Сервис отправки почты.
 */
public interface EmailService {

    /**
     * Отправляет email с заданными параметрами.
     *
     * @param to адрес получателя
     * @param subject тема письма
     * @param text текст сообщения
     */
    void sendEmail(String to, String subject, String text);

    /**
     * Отправляет уведомление о создании аккаунта пользователю.
     *
     * @param to адрес электронной почты пользователя
     */
    void sendUserCreatedEmail(String to);

    /**
     * Отправляет уведомление об удалении аккаунта пользователю.
     *
     * @param to адрес электронной почты пользователя
     */
    void sendUserDeletedEmail(String to);
}