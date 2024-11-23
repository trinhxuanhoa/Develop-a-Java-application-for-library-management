package org.example;

import javafx.beans.property.*;

import java.time.LocalDate;

public class BookAndBorrow {

    private SimpleStringProperty genre;
    private SimpleStringProperty id;
    private SimpleStringProperty title;
    private SimpleStringProperty author;
    private ObjectProperty<LocalDate> borrowDate;
    private ObjectProperty<LocalDate> dueDate;
    private ObjectProperty<LocalDate> returnDate;
    private SimpleStringProperty detail;
    private BooleanProperty selected;
    private SimpleStringProperty fullName;
    private SimpleStringProperty userId;
    // Constructor
    public BookAndBorrow( String id, String title, String author, String genre, LocalDate borrowDate,
                          LocalDate dueDate, LocalDate returnDate, String detail) {

        this.id = new SimpleStringProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.genre = new SimpleStringProperty(genre);
        this.borrowDate = new SimpleObjectProperty<>(borrowDate);
        this.returnDate = new SimpleObjectProperty<>(returnDate);
        this.dueDate = new SimpleObjectProperty<>(dueDate);
        this.detail = new SimpleStringProperty(detail);
        this.selected = new SimpleBooleanProperty(false); // Mặc định chưa được chọn
    }

    public BookAndBorrow( String id, String title, String author, String genre, LocalDate borrowDate,
                          LocalDate dueDate, LocalDate returnDate,String userId, String fullName, String detail) {

        this.id = new SimpleStringProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.genre = new SimpleStringProperty(genre);
        this.borrowDate = new SimpleObjectProperty<>(borrowDate);
        this.returnDate = new SimpleObjectProperty<>(returnDate);
        this.dueDate = new SimpleObjectProperty<>(dueDate);
        this.userId = new SimpleStringProperty(userId);
        this.fullName = new SimpleStringProperty(fullName);
        this.detail = new SimpleStringProperty(detail);
        this.selected = new SimpleBooleanProperty(false); // Mặc định chưa được chọn
    }

    public String getId() {return id.get();}
    public SimpleStringProperty idProperty() {return id;}
    public void setId(String id) {this.id.set(id);}

    public String getTitle() {return title.get();}
    public SimpleStringProperty titleProperty() {return title;}
    public void setTitle(String title) {this.title.set(title);}

    public String getAuthor() {return author.get();}
    public SimpleStringProperty authorProperty() {return author;}
    public void setAuthor(String author) {this.author.set(author);}

    public String getGenre() {return genre.get();}
    public SimpleStringProperty genreProperty() {return genre;}
    public void setGenre(String genre) {this.genre.set(genre);}

    public LocalDate getBorrowDate() {return borrowDate.get();}
    public ObjectProperty<LocalDate> borrowDateProperty() {return borrowDate;}
    public void setBorrowDate(LocalDate borrowDate) {this.borrowDate.set(borrowDate);}

    public LocalDate getDueDate() {return dueDate.get();}
    public ObjectProperty<LocalDate> dueDateProperty() {return dueDate;}
    public void setDueDate(LocalDate dueDate) {this.dueDate.set(dueDate);}

    public LocalDate getReturnDate() {return returnDate.get();}
    public ObjectProperty<LocalDate> returnDateProperty() {return returnDate;}
    public void setReturnDate(LocalDate returnDate) {this.returnDate.set(returnDate);}

    public String getFullName() {return fullName.get();}
    public SimpleStringProperty fullNameProperty() {return fullName;}
    public void setFullName(String fullName) {this.fullName.set(fullName);}

    public String getUserId() {return userId.get();}
    public SimpleStringProperty userIdProperty() {return userId;}
    public void setUserId(String userId) {this.userId.set(userId);}

    public String getDetail() {return detail.get();}
    public SimpleStringProperty detailProperty() {return detail;}
    public void setDetail(String detail) {this.detail.set(detail);}

    public boolean isSelected() {return selected.get();}
    public BooleanProperty selectedProperty() {return selected;}
    public void setSelected(boolean selected) {this.selected.set(selected);}
}
