package com.example.notificationservice.exception;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO ошибки API.
 */
public class ApiError {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private List<String> details;

    public ApiError() {
    }

    public ApiError(LocalDateTime timestamp, int status, String error, List<String> details) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }
}