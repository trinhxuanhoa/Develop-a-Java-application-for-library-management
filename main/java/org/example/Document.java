package org.example;

import java.sql.*;

public class Document {
   private String id;
   private String title;
   private String author;
   private int year;
   private String genre;
    private int quantity;
   private boolean isAvailable;
   private String publisher;

   public  Document() {}
    public Document(String id, String title, String author, String publisher, int year, String genre,int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.genre = genre;
        this.quantity = quantity;
    }

    public void addBookToDatabase() {
        try (Connection connection = DatabaseConnection.connect()) {
            String sql = "INSERT INTO books (id, title, author, publisher, year, genre, quantity) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, id);
                statement.setString(2, title);
                statement.setString(3, author);
                statement.setString(4, publisher);
                statement.setInt(5, year);
                statement.setString(6, genre);
                statement.setInt(7,quantity);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    this.isAvailable = true;
                    System.out.println("Sách đã được thêm thành công!");
                }
                else {
                    this.isAvailable = false;
                }
            }
        } catch (SQLException e) {
            this.isAvailable = false;
            System.out.println("Lỗi khi thêm sách: " + e.getMessage());
        }
    }

    public void removeBookToDatabase(String id) {
        try (Connection connection = DatabaseConnection.connect()) {
            String sql = "DELETE FROM books WHERE id = " + "'" + id + "'";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    this.isAvailable = true;
                    System.out.println("Sách đã được xóa thành công!");
                } else {
                    this.isAvailable = false;
                }
            }
        } catch (SQLException e) {
            this.isAvailable = false;
            System.out.println("Lỗi khi xóa sách: " + e.getMessage());
        }
    }

    public void updateBookToDatabase() {
        try (Connection connection = DatabaseConnection.connect()) {
            String sql = "UPDATE books SET title = ?, author = ?, publisher = ?, year = ?, genre = ?, quantity = ? WHERE id = ? ";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, title);
                statement.setString(2, author);
                statement.setString(3, publisher);
                statement.setInt(4, year);
                statement.setString(5, genre);
                statement.setInt(6,quantity);
                statement.setString(7, id);
                this.isAvailable = true;
                int rowsInserted = statement.executeUpdate();
                if(rowsInserted > 0) {
                    this.isAvailable = true;
                    System.out.println("Sách đã được sửa thành công!");
                } else {
                    this.isAvailable = false;
                }
            }
        } catch (SQLException e) {
            this.isAvailable = false;
            System.out.println("Lỗi khi sửa sách: " + e.getMessage());
        }
    }

    public void output1Value(String id) {
        try (Connection connection = DatabaseConnection.connect()) {
            String sql = "SELECT * FROM books WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1,id);
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                this.id = resultSet.getString("id");
                this.title = resultSet.getString("title");
                this.author = resultSet.getString("author");
                this.publisher = resultSet.getString("publisher");
                this.genre = resultSet.getString("genre");
                this.year = resultSet.getInt("year");
                this.quantity = resultSet.getInt("quantity");
            }
        } catch (SQLException e) {
            this.isAvailable = false;
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public void printInfo() {
        System.out.printf("ID: %s, Title: %s, Author: %s, Quantity: %d%n",
                id, title, author, quantity);
    }
    boolean checkAvailability(){ return isAvailable;}
    void updateAvailability(boolean status) {

    }
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getAuthor() {return author;}
    public void setAuthor(String author) {this.author = author;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public int getYear() {return year;}
    public void setYear(int year) {this.year = year;}

    public int getQuantity() {return quantity;}
    public void setQuantity(int quantity) {this.quantity = quantity;}

    public String getGenre() {return genre;}
    public void setGenre(String genre) {this.genre = genre;}

    public String getPublisher() {return publisher;}
    public void setPublisher(String publisher) {this.publisher = publisher;}

    public boolean isAvailable() {return isAvailable;}
    public void setAvailable(boolean available) {isAvailable = available;}

}
