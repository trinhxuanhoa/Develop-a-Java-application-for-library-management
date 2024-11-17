package org.example;

import javafx.scene.image.Image;
import javafx.beans.property.*;
import java.io.ByteArrayInputStream;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.sql.*;
import java.sql.SQLException;
import java.util.*;
import java.io.*;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.geometry.Pos;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.ByteArrayInputStream;
import java.util.concurrent.atomic.AtomicBoolean;

public class Book {
    private SimpleStringProperty id;
    private SimpleStringProperty title;
    private SimpleStringProperty author;
    private SimpleStringProperty publisher;
    private SimpleStringProperty genre;
    private SimpleIntegerProperty year;
    private SimpleIntegerProperty quantity;
    private SimpleStringProperty edition;
    private SimpleIntegerProperty reprint;
    private SimpleDoubleProperty price;
    private SimpleStringProperty language;
    private SimpleStringProperty status;
    private SimpleIntegerProperty pages;
    private SimpleIntegerProperty downloads;
    private SimpleDoubleProperty chapter;
    private SimpleStringProperty summary;
    private SimpleStringProperty detail;
    private byte[] qrCode;
    private byte[] coverImages;
    private Image qrCodeImage;
    private Image coverImage;
    private BooleanProperty selected;

    public Book() {
        this.id = new SimpleStringProperty();
        this.title = new SimpleStringProperty();
        this.author = new SimpleStringProperty();
        this.publisher = new SimpleStringProperty();
        this.genre = new SimpleStringProperty();
        this.year = new SimpleIntegerProperty();
        this.quantity = new SimpleIntegerProperty();
        this.edition = new SimpleStringProperty();
        this.reprint = new SimpleIntegerProperty();
        this.price = new SimpleDoubleProperty();
        this.language = new SimpleStringProperty();
        this.status = new SimpleStringProperty();
        this.chapter = new SimpleDoubleProperty();
        this.pages = new SimpleIntegerProperty();
        this.downloads = new SimpleIntegerProperty();
        this.summary = new SimpleStringProperty();
    };

    public Book(String id, String title, String author, String publisher,
                Integer year, String genre, Integer quantity) {
        this.id = new SimpleStringProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.publisher = new SimpleStringProperty(publisher);
        this.year = (year!=null) ? new SimpleIntegerProperty(year) : null;
        this.genre = new SimpleStringProperty(genre);
        this.quantity = (quantity!=null) ? new SimpleIntegerProperty(quantity) : new SimpleIntegerProperty();
    }
    public Book(boolean selected, String id, String title, String author, String publisher,
             Integer year, String genre, Integer quantity, String detail) {
        this.id = new SimpleStringProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.publisher = new SimpleStringProperty(publisher);
        this.year = (year != null) ? new SimpleIntegerProperty(year) : null;
        this.genre = new SimpleStringProperty(genre);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.detail = new SimpleStringProperty(detail);
        this.selected = new SimpleBooleanProperty(selected);
    }
    public Book(String id, String title, String author, String publisher, Integer year,
                String genre, Integer quantity, String edition, Integer reprint, Double price,
                String language, String status, Double chapter,Integer pages, String summary, byte[] qrCode,
                byte[] coverImages) {
        this.id = new SimpleStringProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.publisher = new SimpleStringProperty(publisher);
        this.genre = new SimpleStringProperty(genre);
        this.year = (year != null) ? new SimpleIntegerProperty(year) : null;
        this.quantity = (quantity != null) ? new SimpleIntegerProperty(quantity) : new SimpleIntegerProperty();
        this.edition = new SimpleStringProperty(edition);
        this.reprint = (reprint != null) ? new SimpleIntegerProperty(reprint) : null;
        this.price = (price != null) ? new SimpleDoubleProperty(price) : null;
        this.language = new SimpleStringProperty(language);
        this.status = new SimpleStringProperty(status);
        this.chapter = (chapter != null) ? new SimpleDoubleProperty(chapter) : null;
        this.summary = new SimpleStringProperty(summary);
        this.pages = (pages != null) ? new SimpleIntegerProperty(pages) : null;
        this.downloads = new SimpleIntegerProperty();
        this.coverImages = coverImages;
        this.qrCode = qrCode;
        //this.selected = new SimpleBooleanProperty(false); // Khởi tạo giá trị mặc định
        try {
            if(qrCode!=null)
            this.qrCodeImage = new Image(new ByteArrayInputStream(qrCode));
            if(coverImages!=null)
            this.coverImage = new Image(new ByteArrayInputStream(coverImages));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Book(String id, String title, String author, String publisher,
                String genre, int quantity, String edition, int reprint, double price,
                String language, String status, double chapter, String summary, byte[] qrCode,
                byte[] coverImages) {
        this.id = new SimpleStringProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.publisher = new SimpleStringProperty(publisher);
        this.genre = new SimpleStringProperty(genre);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.edition = new SimpleStringProperty(edition);
        this.reprint = new SimpleIntegerProperty(reprint);
        this.price = new SimpleDoubleProperty(price);
        this.language = new SimpleStringProperty(language);
        this.status = new SimpleStringProperty(status);
        this.chapter = new SimpleDoubleProperty(chapter);
        this.summary = new SimpleStringProperty(summary);
        this.coverImages = coverImages; // Không cần sử dụng Property cho Image
        this.qrCode = qrCode;
        this.selected = new SimpleBooleanProperty(false); // Khởi tạo giá trị mặc định
        try {
            this.qrCodeImage = new Image(new ByteArrayInputStream(qrCode));
            this.coverImage = new Image(new ByteArrayInputStream(coverImages));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Book(String id, String title, String author, String publisher, int year,
                String genre, int quantity, String edition, int reprint, double price,
                String language, String status, double chapter, String summary, String detail, byte[] qrCode,
                byte[] coverImages) {
        this.id = new SimpleStringProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.publisher = new SimpleStringProperty(publisher);
        this.genre = new SimpleStringProperty(genre);
        this.year = new SimpleIntegerProperty(year);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.edition = new SimpleStringProperty(edition);
        this.reprint = new SimpleIntegerProperty(reprint);
        this.price = new SimpleDoubleProperty(price);
        this.language = new SimpleStringProperty(language);
        this.status = new SimpleStringProperty(status);
        this.chapter = new SimpleDoubleProperty(chapter);
        this.summary = new SimpleStringProperty(summary);
        this.detail = new SimpleStringProperty(detail);
        this.coverImages = coverImages; // Không cần sử dụng Property cho Image
        this.qrCode = qrCode;
        this.selected = new SimpleBooleanProperty(false); // Khởi tạo giá trị mặc định
        try {
            this.qrCodeImage = new Image(new ByteArrayInputStream(qrCode));
            this.coverImage = new Image(new ByteArrayInputStream(coverImages));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public Integer  getYear() { return (year!=null) ? year.get() : null; }
    public void setYear(Integer year) { if(year!=null){this.year.set(year); } else this.year=null; }
    public SimpleIntegerProperty yearProperty() { return year; }

    // Getter và setter cho quantity
    public Integer getQuantity() { return quantity.get(); }
    public void setQuantity(Integer quantity) { if(quantity!=null)this.quantity.set(quantity);}
    public SimpleIntegerProperty quantityProperty() { return quantity; }

    public String getDetail() { return detail.get(); }
    public void setDetail(String detail) { this.detail.set(detail); }
    public SimpleStringProperty detailProperty() { return detail; }

    public boolean isSelected() {return selected.get();}
    public void setSelected(boolean selected) {this.selected.set(selected);}
    public BooleanProperty selectedProperty() {return selected;}

    public String getEdition() {return edition.get();}
    public void setEdition(String edition) {this.edition.set(edition);}
    public SimpleStringProperty editionProperty() {return edition;}

    public Integer getReprint() {return (reprint!=null)?reprint.get():null;}
    public void setReprint(Integer reprint) {if(reprint!=null) this.reprint.set(reprint);else this.reprint=null;}
    public SimpleIntegerProperty reprintProperty() {return reprint;}

    public Double getPrice() {return (price!=null)?price.get():null;}
    public void setPrice(Double price) {if(price!=null) this.price.set(price);else this.price=null;}
    public SimpleDoubleProperty priceProperty() {return price;}

    public String getLanguage() {return language.get();}
    public void setLanguage(String language) {this.language.set(language);}
    public SimpleStringProperty languageProperty() {return language;}

    public String getStatus() {return status.get();}
    public void setStatus(String status) {this.status.set(status);}
    public SimpleStringProperty statusProperty() {return status;}

    public Double getChapter() {return (chapter!=null) ? chapter.get() : null;}
    public SimpleDoubleProperty chapterProperty() {return chapter;}
    public void setChapter(Double chapter) {if(chapter!=null)this.chapter.set(chapter);else this.chapter=null;}

    public Integer getPages() {return (pages!=null) ? pages.get() : null; }
    public SimpleIntegerProperty pagesProperty() {return pages;}
    public void setPages(Integer pages) {if(pages!=null){this.pages.set(pages); } else this.pages=null; }

    public Integer getDownloads() {return downloads.get();}
    public SimpleIntegerProperty downloadsProperty() {return downloads;}
    public void setDownloads(Integer downloads) {if(downloads!=null)this.downloads.set(downloads);}

    public byte[] getQrCode() {return qrCode;}
    public void setQrCode(byte[] qrCodePath) {this.qrCode = qrCode;}

    public byte[] getCoverImages() {return coverImages;}
    public void setCoverImages(byte[] coverImagePath) {this.coverImages = coverImagePath;}

    public Image getCoverImage() {return coverImage;}
    public void setCoverImage(Image coverImage) {this.coverImage = coverImage;}

    public Image getQrCodeImage() {return qrCodeImage;}
    public void setQrCodeImage(Image qrCodeImage) {this.qrCodeImage = qrCodeImage;}

    public String getSummary() {return summary.get();}
    public void setSummary(String summary) {this.summary.set(summary);}
    public SimpleStringProperty summaryProperty() {return summary;}

}
