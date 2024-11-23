package org.example;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Borrow {
    private SimpleIntegerProperty borrowId;        // ID duy nhất cho mỗi lần mượn
    private SimpleStringProperty userId;           // ID của người mượn
    private SimpleStringProperty bookId;           // ID của sách mượn
    private ObjectProperty<LocalDate> borrowDate;  // Ngày mượn
    private ObjectProperty<LocalDate> returnDate;  // Ngày trả
    private ObjectProperty<LocalDate> dueDate;     // ngày hết hạn
    private SimpleStringProperty status;           // Trạng thái: chưa mượn, đang mượn, đã trả

    // Constructor
    public Borrow() {
        this.borrowId = new SimpleIntegerProperty();
        this.userId = new SimpleStringProperty();
        this.bookId = new SimpleStringProperty();
        this.borrowDate = new SimpleObjectProperty<>();
        this.returnDate = new SimpleObjectProperty<>();
        this.status = new SimpleStringProperty();
        this.dueDate = new SimpleObjectProperty<>();
    }
    public Borrow(int borrowId, String userId, String bookId, LocalDate borrowDate,
                  LocalDate returnDate, String status, LocalDate dueDate) {
        this.borrowId = new SimpleIntegerProperty(borrowId);
        this.userId = new SimpleStringProperty(userId);
        this.bookId = new SimpleStringProperty(bookId);
        this.borrowDate = new SimpleObjectProperty<>(borrowDate);
        this.returnDate = new SimpleObjectProperty<>(returnDate);
        this.dueDate = new SimpleObjectProperty<>(dueDate);
        this.status = new SimpleStringProperty(status);
    }

    public LocalDate calculateDueDate(LocalDate borrowDate, int loanPeriodDays) {
        return borrowDate.plusDays(loanPeriodDays);
    }
    // Getters
    public int getBorrowId() {return borrowId.get();}
    public String getUserId() {return userId.get();}
    public String getBookId() {return bookId.get();}
    public LocalDate getBorrowDate() {return borrowDate.get();}
    public LocalDate getReturnDate() {return returnDate.get();}
    public String getStatus() {return status.get();}
    public LocalDate getDueDate() {return dueDate.get();}
    // Setters
    public void setBorrowId(int borrowId) {this.borrowId.set(borrowId);}
    public void setUserId(String userId) {this.userId.set(userId);}
    public void setBookId(String bookId) {this.bookId.set(bookId);}
    public void setBorrowDate(LocalDate borrowDate) {this.borrowDate.set(borrowDate);}
    public void setReturnDate(LocalDate returnDate) {this.returnDate.set(returnDate);}
    public void setDueDate(LocalDate dueDate) {this.dueDate.set(dueDate);}
    public void setStatus(String status) {this.status.set(status);}
    // Property Getters (for bindings)
    public IntegerProperty borrowIdProperty() {return borrowId;}
    public StringProperty userIdProperty() {return userId;}
    public StringProperty bookIdProperty() {return bookId;}
    public ObjectProperty<LocalDate> borrowDateProperty() {return borrowDate;}
    public ObjectProperty<LocalDate> returnDateProperty() {return returnDate;}
    public ObjectProperty<LocalDate> dueDateProperty() {return dueDate;}
    public StringProperty statusProperty() {return status;}
}
