package org.example;
import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookDAO {
    private boolean Available;
    private Connection connection;
    private PreparedStatement statement;
    public BookDAO() {

    }

    public void addBook(Book book) {
        try (Connection connection = DatabaseConnection.connectToLibrary()) {
            String sql = "INSERT INTO books (id, title, author, publisher, year, genre, quantity, edition, reprint, price, language, status, summary, qr_code, cover_image, chapter" +
                    ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, book.getId());
                statement.setString(2, book.getTitle());
                statement.setString(3, book.getAuthor());
                statement.setString(4, book.getPublisher());
                setIntOrNull(statement, 5, book.getYear());
                statement.setString(6, book.getGenre());
                statement.setInt(7, book.getQuantity());
                statement.setString(8,book.getEdition());
                setIntOrNull(statement, 9, book.getReprint());
                setDoubleOrNull(statement, 10, book.getPrice());
                statement.setString(11,book.getLanguage());
                statement.setString(12,book.getStatus());
                statement.setString(13, book.getSummary());
                statement.setBytes(14, book.getQrCode());
                statement.setBytes(15, book.getCoverImages());
                setDoubleOrNull(statement, 16, book.getChapter());

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    Noti.showSuccessMessage("Sách đã được thêm thành công");
                    System.out.println("Sách đã được thêm thành công!");
                } else {
                    Noti.showFailureMessage("Lỗi: không thể thêm sách");
                }
            }
        } catch (SQLException e) {
            Noti.showFailureMessage("Lỗi: " + e.getMessage());
            System.out.println("Lỗi khi thêm sách: " + e.getMessage());
        }
    }

    public void removeBook(String id) {
        try (Connection connection = DatabaseConnection.connectToLibrary()) {
            String sql = "DELETE FROM books WHERE id = " + "'" + id + "'";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    Available = true;
                    System.out.println("Sách đã được xóa thành công!");
                }
            }
        } catch (SQLException e) {
            Available = false;
            System.out.println("Lỗi khi xóa sách: " + e.getMessage());
        }
    }

    public Book output1Value(String id) {
        Book book = new Book();
        try (Connection connection = DatabaseConnection.connectToLibrary()) {
            String sql = "SELECT * FROM books WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1,id);
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                book.setId(resultSet.getString("id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setPublisher(resultSet.getString("publisher"));
                book.setGenre(resultSet.getString("genre"));
                book.setYear(getInt(resultSet,"year"));
                book.setQuantity(resultSet.getInt("quantity"));
                book.setEdition(resultSet.getString("edition"));
                book.setReprint(getInt(resultSet,"reprint"));
                book.setPrice(getDouble(resultSet,"price"));
                book.setLanguage(resultSet.getString("language"));
                book.setStatus(resultSet.getString("status"));
                book.setSummary(resultSet.getString("summary"));
                book.setChapter(getDouble(resultSet,"chapter"));
                try {
                    if (resultSet.getBytes("qr_code")!=null)
                    book.setQrCodeImage(new Image(new ByteArrayInputStream(resultSet.getBytes("qr_code"))));
                    if (resultSet.getBytes("cover_image")!=null)
                    book.setCoverImage(new Image(new ByteArrayInputStream(resultSet.getBytes("cover_image"))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
        return book;
    }

    public ResultSet findBooks(Book book) {

        boolean checkAnd = false;
        try  {
            connection = DatabaseConnection.connectToLibrary();
            String sql = "SELECT * FROM books WHERE ";
            if (!book.getId().isEmpty()) {
                String sqlId = "id = '" + book.getId() + "'";
                sql += sqlId;
                checkAnd = true;
            }
            if (!book.getTitle().isEmpty()) {
                if (checkAnd)
                    sql += " AND ";
                String sqlTitle = "title = '" + book.getTitle() + "'";
                sql += sqlTitle;
                checkAnd = true;
            }
            if (!book.getAuthor().isEmpty()) {
                if (checkAnd)
                    sql += " AND ";
                String sqlAuthor = "author = '" + book.getAuthor() + "'";
                sql += sqlAuthor;
                checkAnd = true;
            }
            if (!book.getPublisher().isEmpty()) {
                if (checkAnd)
                    sql += " AND ";
                String sqlPublisher = "publisher = '" + book.getPublisher() + "'";
                sql += sqlPublisher;
                checkAnd = true;
            }
            if (book.yearProperty()!=null) {
                System.out.println(sql + "vdv");
                if (checkAnd)
                    sql += " AND ";
                String sqlYear = "year = " + book.getYear();
                sql += sqlYear;
                checkAnd = true;
            }
            if (!book.getGenre().isEmpty()) {
                if (checkAnd)
                    sql += " AND ";
                String sqlGenre = "genre = '" + book.getGenre() + "'";
                sql += sqlGenre;
                checkAnd = true;
            }
            if (book.quantityProperty()!= null) {
                if (checkAnd)
                    sql += " AND ";
                String sqlQuantity = "quantity = " + book.getQuantity();
                sql += sqlQuantity;
                checkAnd = true;
            }
            if (!checkAnd) {
                sql = "SELECT * FROM books";
            }
                statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
                return resultSet;
        } catch (SQLException e) {
            System.out.println("Lỗi khi tìm sách: " + e.getMessage());
        }
        return null;
    }

    public boolean isAvailable() {
        return Available;
    }

    public void setAvailable(boolean available) {
        Available = available;
    }

    public void setIntOrNull(PreparedStatement statement, int parameterIndex, Integer value) throws SQLException {
        if (value != null) {
            statement.setInt(parameterIndex, value);
        } else {
            statement.setNull(parameterIndex, java.sql.Types.INTEGER);
        }
    }

    public void setDoubleOrNull(PreparedStatement statement, int parameterIndex, Double value) throws SQLException {
        if (value != null) {
            statement.setDouble(parameterIndex, value);
        } else {
            statement.setNull(parameterIndex, java.sql.Types.DOUBLE);
        }
    }

    public Integer getInt(ResultSet resultSet, String type) throws SQLException {
        int type0 = resultSet.getInt(type);
        return resultSet.wasNull() ? null : type0;
    }

    public Double getDouble(ResultSet resultSet, String type) throws SQLException {
        Double type0 = resultSet.getDouble(type);
        return resultSet.wasNull() ? null : type0;
    }
    public void closeDatabase() {
        try {
            connection.close();
            statement.close();
        } catch (SQLException e) {

        }
    }
}
