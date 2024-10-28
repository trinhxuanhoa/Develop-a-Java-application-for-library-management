package org.example;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import java.sql.*;
import java.sql.SQLException;
import java.util.*;

public class Document {
   private String id;
   private String title;
   private String author;
   private int year;
   private String yearS;
   private String genre;
   private int quantity;
   private String quantityS;
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
    public Document(String id, String title, String author, String publisher, String yearS, String genre,String quantityS) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.yearS = yearS;
        this.genre = genre;
        this.quantityS = quantityS;
    }
    public void addBookToDatabase() {
        try (Connection connection = DatabaseConnection.connectToLibrary()) {
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
        try (Connection connection = DatabaseConnection.connectToLibrary()) {
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

    public void updateBookToDatabase(String id,String type, String data) {
        try (Connection connection = DatabaseConnection.connectToLibrary()) {
            String sql = "UPDATE books SET " + type + " = ? WHERE id = ? ";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                if(type=="year"||type=="quantity")
                statement.setInt(1,Integer.parseInt(data));
               else
                statement.setString(1,data);

                statement.setString(2,id);
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
        try (Connection connection = DatabaseConnection.connectToLibrary()) {
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

    public List<Document> findDocument() {
        List<Document> documents = new ArrayList<>();
        try (Connection connection = DatabaseConnection.connectToLibrary()) {
            boolean checkAnd = false;
            String sql = "SELECT * FROM books WHERE ";
            if(!id.isEmpty()) {
                String sqlId = "id = '" + id + "'";
                sql += sqlId;
                checkAnd = true;
            }
            if(!title.isEmpty()) {
                if(checkAnd)
                    sql+=" AND ";
                String sqlTitle = "title = '" + title + "'";
                sql += sqlTitle;
                checkAnd = true;
            }
            if(!author.isEmpty()) {
                if(checkAnd)
                    sql+=" AND ";
                String sqlAuthor = "author = '" + author + "'";
                sql += sqlAuthor;
                checkAnd = true;
            }
            if(!publisher.isEmpty()) {
                if(checkAnd)
                    sql+=" AND ";
                String sqlPublisher = "publisher = '" + publisher + "'";
                sql += sqlPublisher;
                checkAnd = true;
            }
            if(!yearS.isEmpty()) {
                if(checkAnd)
                    sql+=" AND ";
                String sqlYear = "year = " + yearS;
                sql += sqlYear;
                checkAnd = true;
            }
            if(!genre.isEmpty()) {
                if(checkAnd)
                    sql+=" AND ";
                String sqlGenre = "genre = '" + genre + "'";
                sql += sqlGenre;
                checkAnd = true;
            }
            if(!quantityS.isEmpty()) {
                if(checkAnd)
                    sql+=" AND ";
                String sqlQuantity = "quantity = " + quantityS;
                sql += sqlQuantity;
            }
            if(!checkAnd) {
                sql = "SELECT * FROM books";
            }
            try (PreparedStatement statement = connection.prepareStatement(sql)) {

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    documents.add(new Document (
                            resultSet.getString("id"),
                            resultSet.getString("title"),
                            resultSet.getString("author"),
                            resultSet.getString("publisher"),
                            resultSet.getInt("year"),
                            resultSet.getString("genre"),
                            resultSet.getInt("quantity")

                    ));
                }
            }
        } catch (SQLException e) {
            this.isAvailable = false;
            System.out.println("Lỗi: " + e.getMessage());
        }
        return documents;
    }

    public static TableView<Book> table(boolean open,double tableWidth) {
        Document document = new Document();
        TableView<Book> tableView = new TableView<>();
        tableView.setEditable(open); // Bật chế độ chỉnh sửa

        TableColumn<Book, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setStyle("-fx-alignment: CENTER;");
        idColumn.setMinWidth(55);
        idColumn.setMaxWidth(55);

        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setStyle("-fx-alignment: CENTER;");
        titleColumn.setMinWidth(tableWidth-580);
        titleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        titleColumn.setOnEditCommit(e -> {
            Book book = e.getRowValue();
            book.setTitle(e.getNewValue());
            document.updateBookToDatabase(book.getId(),"title", e.getNewValue());
        });

        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorColumn.setStyle("-fx-alignment: CENTER;");
        authorColumn.setMinWidth(150);
        authorColumn.setMaxWidth(150);
        authorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        authorColumn.setOnEditCommit(e -> {
            Book book = e.getRowValue();
            book.setAuthor(e.getNewValue());
            document.updateBookToDatabase(book.getId(),"author", e.getNewValue());
        });

        TableColumn<Book, String> publisherColumn = new TableColumn<>("Publisher");
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        publisherColumn.setStyle("-fx-alignment: CENTER;");
        publisherColumn.setMinWidth(150);
        publisherColumn.setMaxWidth(150);
        publisherColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        publisherColumn.setOnEditCommit(e -> {
            Book book = e.getRowValue();
            book.setPublisher(e.getNewValue());
            document.updateBookToDatabase(book.getId(),"publisher", e.getNewValue());
        });

        TableColumn<Book, Integer> yearColumn = new TableColumn<>("Year");
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        yearColumn.setStyle("-fx-alignment: CENTER;");
        yearColumn.setMinWidth(50);
        yearColumn.setMaxWidth(50);
        yearColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        yearColumn.setOnEditCommit(e -> {
            Book book = e.getRowValue();
            book.setYear(e.getNewValue());
            document.updateBookToDatabase(book.getId(),"year", e.getNewValue()+"");
        });

        TableColumn<Book, String> genreColumn = new TableColumn<>("Genre");
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreColumn.setStyle("-fx-alignment: CENTER;");
        genreColumn.setMinWidth(100);
        genreColumn.setMaxWidth(100);
        genreColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        genreColumn.setOnEditCommit(e -> {
            Book book = e.getRowValue();
            book.setGenre(e.getNewValue());
            document.updateBookToDatabase(book.getId(),"genre", e.getNewValue());
        });

        TableColumn<Book, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setStyle("-fx-alignment: CENTER;");
        quantityColumn.setMinWidth(75);
        quantityColumn.setMaxWidth(75);
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantityColumn.setOnEditCommit(e -> {
            Book book = e.getRowValue();
            book.setQuantity(e.getNewValue());
            document.updateBookToDatabase(book.getId(),"quantity", e.getNewValue()+"");
        });
        tableView.getColumns().addAll(idColumn, titleColumn, authorColumn,
                publisherColumn, yearColumn, genreColumn, quantityColumn);
        return tableView;
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
