package org.example;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TopBook {

    private SimpleStringProperty bookId;
    private SimpleStringProperty title;
    private SimpleIntegerProperty totalBorrows;
    private SimpleDoubleProperty rating;
    // Constructor
    public TopBook(String bookId, String title, int totalBorrows, double rating) {
        this.bookId = new SimpleStringProperty(bookId);
        this.title = new SimpleStringProperty(title);
        this.totalBorrows = new SimpleIntegerProperty(totalBorrows);
        this.rating = new SimpleDoubleProperty(rating);
    }

    // Getter và Setter cho bookId
    public String getBookId() {
        return bookId.get();
    }

    public void setBookId(String bookId) {
        this.bookId.set(bookId);
    }

    // Getter và Setter cho title
    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    // Getter và Setter cho totalBorrows
    public int getTotalBorrows() {
        return totalBorrows.get();
    }

    public void setTotalBorrows(int totalBorrows) {
        this.totalBorrows.set(totalBorrows);
    }

    // Các phương thức để truy xuất các SimpleProperty
    public SimpleStringProperty bookIdProperty() {
        return bookId;
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public SimpleIntegerProperty totalBorrowsProperty() {
        return totalBorrows;
    }

    public double getRating() {
        return rating.get();
    }

    public SimpleDoubleProperty ratingProperty() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating.set(rating);
    }
}
