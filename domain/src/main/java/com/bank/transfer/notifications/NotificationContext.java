package com.bank.transfer.notifications;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationContext {
    
    private List<Notification> notifications;

    public NotificationContext() {
        this.notifications = new ArrayList<>();
    }

    public List<Notification> getNotifications() {
        return notifications.stream().collect(Collectors.toList());
    }

    public void add(String message, ENotification notification) {
        notifications.add(new Notification(message, notification, Instant.now()));
    }
}
