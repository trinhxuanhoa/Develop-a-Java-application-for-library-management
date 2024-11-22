package org.example;

import java.util.Date;

public class NotificationItem {
    private final String message;
    private final Date timestamp;

    public NotificationItem(String message, Date timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
