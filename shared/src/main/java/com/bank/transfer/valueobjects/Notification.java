package com.bank.transfer.valueobjects;

import com.bank.transfer.enums.ENotification;

import java.time.Instant;

public class Notification {

    private String message;
    private ENotification notificationType;
    private Instant instant;

    public Notification(String message, ENotification notificationType, Instant instant) {
        this.message = message;
        this.notificationType = notificationType;
        this.instant = instant;
    }

    public String getMessage() {
        return message;
    }

    public ENotification getNotificationType() {
        return notificationType;
    }

    public Instant getInstant() {
        return instant;
    }
}
