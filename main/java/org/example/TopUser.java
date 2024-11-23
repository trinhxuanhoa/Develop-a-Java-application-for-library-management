package org.example;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TopUser {

    private SimpleStringProperty userId;
    private SimpleStringProperty fullName;
    private SimpleIntegerProperty totalBooksBorrowed;

    // Constructor
    public TopUser(String userId, String fullName, int totalBooksBorrowed) {
        this.userId = new SimpleStringProperty(userId);
        this.fullName = new SimpleStringProperty(fullName);
        this.totalBooksBorrowed = new SimpleIntegerProperty(totalBooksBorrowed);
    }

    // Getter và Setter cho userId
    public String getUserId() {
        return userId.get();
    }

    public void setUserId(String userId) {
        this.userId.set(userId);
    }

    // Getter và Setter cho fullName
    public String getFullName() {
        return fullName.get();
    }

    public void setFullName(String fullName) {
        this.fullName.set(fullName);
    }

    // Getter và Setter cho totalBooksBorrowed
    public int getTotalBooksBorrowed() {
        return totalBooksBorrowed.get();
    }

    public void setTotalBooksBorrowed(int totalBooksBorrowed) {
        this.totalBooksBorrowed.set(totalBooksBorrowed);
    }

    // Các phương thức để truy xuất các SimpleProperty
    public SimpleStringProperty userIdProperty() {
        return userId;
    }

    public SimpleStringProperty fullNameProperty() {
        return fullName;
    }

    public SimpleIntegerProperty totalBooksBorrowedProperty() {
        return totalBooksBorrowed;
    }
}
