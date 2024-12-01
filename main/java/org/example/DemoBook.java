package org.example;

import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;

public class DemoBook {
    private String id ;
    private String title;
    private String author;
    private Image coverImage;

    public DemoBook(String id, String title, String author, Image coverImage) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.coverImage = coverImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Image getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(Image coverImage) {
        this.coverImage = coverImage;
    }
}
