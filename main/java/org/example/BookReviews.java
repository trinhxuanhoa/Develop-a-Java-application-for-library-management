package org.example;

import javafx.beans.property.*;

import java.time.LocalDate;

public class BookReviews {
    private final IntegerProperty reviewId;
    private final StringProperty bookId;
    private final StringProperty userId;
    private final DoubleProperty rating;
    private final ObjectProperty<LocalDate> reviewDate;

    // Constructor

    public BookReviews() {
        this.reviewId = new SimpleIntegerProperty();
        this.bookId = new SimpleStringProperty();
        this.userId = new SimpleStringProperty();
        this.rating = new SimpleDoubleProperty();
        this.reviewDate = new SimpleObjectProperty<>();
    }

    public BookReviews(int reviewId, String bookId, String userId, double rating, LocalDate reviewDate) {
        this.reviewId = new SimpleIntegerProperty(reviewId);
        this.bookId = new SimpleStringProperty(bookId);
        this.userId = new SimpleStringProperty(userId);
        this.rating = new SimpleDoubleProperty(rating);
        this.reviewDate = new SimpleObjectProperty<>(reviewDate);
    }

    // Getters
    public int getReviewId() {return reviewId.get();}
    public String getBookId() {return bookId.get();}
    public String getUserId() {return userId.get();}
    public double getRating() {return rating.get();}
    public LocalDate getReviewDate() {return reviewDate.get();}
    // Setters
    public void setReviewId(int reviewId) {this.reviewId.set(reviewId);}
    public void setBookId(String bookId) {this.bookId.set(bookId);}
    public void setUserId(String userId) {this.userId.set(userId);}
    public void setRating(double rating) {this.rating.set(rating);}
    public void setReviewDate(LocalDate reviewDate) {this.reviewDate.set(reviewDate);}
    // Property methods
    public IntegerProperty reviewIdProperty() {return reviewId;}
    public StringProperty bookIdProperty() {return bookId;}
    public StringProperty userIdProperty() {return userId;}
    public DoubleProperty ratingProperty() {return rating;}
    public ObjectProperty<LocalDate> reviewDateProperty() {return reviewDate;}
}
