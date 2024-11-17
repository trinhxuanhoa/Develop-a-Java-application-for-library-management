package org.example;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class Comments {
    private SimpleIntegerProperty commentId;
    private SimpleStringProperty userId;
    private SimpleStringProperty bookId;
    private SimpleStringProperty comment;
    private ObjectProperty<LocalDateTime> timestamp;

    public Comments() {
        this.commentId = new SimpleIntegerProperty();
        this.userId = new SimpleStringProperty();
        this.bookId = new SimpleStringProperty();
        this.comment = new SimpleStringProperty();
        this.timestamp = new SimpleObjectProperty<>();
    }

    public Comments(Integer commentId, String userId, String bookId, String comment, LocalDateTime timestamp) {
        this.commentId = new SimpleIntegerProperty(commentId);
        this.userId = new SimpleStringProperty(userId);
        this.bookId = new SimpleStringProperty(bookId);
        this.comment = new SimpleStringProperty(comment);
        this.timestamp = new SimpleObjectProperty<>(timestamp);
    }

    // Tạo VBox chứa bình luận với tên, thời gian, và nội dung

    public int getCommentId() {return commentId.get();}
    public SimpleIntegerProperty commentIdProperty() {return commentId;}
    public void setCommentId(int commentId) {this.commentId.set(commentId);}

    public String getUserId() {return userId.get();}
    public SimpleStringProperty userIdProperty() {return userId;}
    public void setUserId(String userId) {this.userId.set(userId);}

    public String getBookId() {return bookId.get();}
    public SimpleStringProperty bookIdProperty() {return bookId;}
    public void setBookId(String bookId) {this.bookId.set(bookId);}

    public String getComment() {return comment.get();}
    public SimpleStringProperty commentProperty() {return comment;}
    public void setComment(String comment) {this.comment.set(comment);}

    public LocalDateTime getTimestamp() {return timestamp.get();}
    public ObjectProperty<LocalDateTime> timestampProperty() {return timestamp;}
    public void setTimestamp(LocalDateTime timestamp) {this.timestamp.set(timestamp);}
}
