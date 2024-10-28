package org.example;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
public class Book {
    private SimpleStringProperty id;
    private SimpleStringProperty title;
    private SimpleStringProperty author;
    private SimpleStringProperty publisher;
    private SimpleStringProperty genre;
    private SimpleIntegerProperty year;
    private SimpleIntegerProperty quantity;
    private Image coverImage;

    public Book(String id, String title, String author, String publisher,
             int year, String genre, int quantity) {
        this.id = new SimpleStringProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.publisher = new SimpleStringProperty(publisher);
        this.year = new SimpleIntegerProperty(year);
        this.genre = new SimpleStringProperty(genre);
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    // Getter và setter cho id
    public String getId() { return id.get(); }
    public void setId(String id) { this.id.set(id); }
    public SimpleStringProperty idProperty() { return id; }

    // Getter và setter cho title
    public String getTitle() { return title.get(); }
    public void setTitle(String title) { this.title.set(title); }
    public SimpleStringProperty titleProperty() { return title; }

    // Getter và setter cho author
    public String getAuthor() { return author.get(); }
    public void setAuthor(String author) { this.author.set(author); }
    public SimpleStringProperty authorProperty() { return author; }

    // Getter và setter cho publisher
    public String getPublisher() { return publisher.get(); }
    public void setPublisher(String publisher) { this.publisher.set(publisher); }
    public SimpleStringProperty publisherProperty() { return publisher; }

    // Getter và setter cho genre
    public String getGenre() { return genre.get(); }
    public void setGenre(String genre) { this.genre.set(genre); }
    public SimpleStringProperty genreProperty() { return genre; }

    // Getter và setter cho year
    public int getYear() { return year.get(); }
    public void setYear(int year) { this.year.set(year); }
    public SimpleIntegerProperty yearProperty() { return year; }

    // Getter và setter cho quantity
    public int getQuantity() { return quantity.get(); }
    public void setQuantity(int quantity) { this.quantity.set(quantity); }
    public SimpleIntegerProperty quantityProperty() { return quantity; }

    public Image getCoverImage() {return coverImage;}
}
