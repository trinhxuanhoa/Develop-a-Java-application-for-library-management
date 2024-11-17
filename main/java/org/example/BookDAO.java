package org.example;

import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.*;

public class BookDAO {
    private boolean Available;
    private AtomicBoolean check;
    private Connection connection;
    private PreparedStatement statement;
    public BookDAO() {
        check=new AtomicBoolean(false);
    }

    public void addBook(Book book) {
        try (Connection connection = DatabaseConnection.connectToLibrary()) {
            String sql = "INSERT INTO books (id, title, author, publisher, year, genre, quantity, edition, reprint, price," +
                    " language, status, summary, qr_code, cover_image, chapter, pages, downloads" +
                    ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
                setIntOrNull(statement, 17, book.getPages());
                setIntOrNull(statement, 18, book.getDownloads());

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
    public void updateBook(Book book) {
        // Câu lệnh SQL để cập nhật thông tin sách
        String sql = "UPDATE books SET title = ?, author = ?, publisher = ?, genre = ?, year = ?, " +
                "quantity = ?, edition = ?, reprint = ?, price = ?, language = ?, status = ?, " +
                "chapter = ?, summary = ?, qr_code = ?, cover_image = ?, pages = ?, downloads = ? WHERE id = ?";

        // Kết nối cơ sở dữ liệu và thực thi câu lệnh
        try (Connection conn = DatabaseConnection.connectToLibrary(); // Sử dụng kết nối từ DatabaseConnection
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Gán giá trị cho các tham số từ đối tượng Book
            stmt.setString(1, book.getTitle());  // title
            stmt.setString(2, book.getAuthor());  // author
            stmt.setString(3, book.getPublisher());  // publisher
            stmt.setString(4, book.getGenre());  // genre
            setIntOrNull(stmt, 5, book.getYear()); // year
            stmt.setInt(6, book.getQuantity());  // quantity
            stmt.setString(7, book.getEdition());  // edition
            setIntOrNull(stmt, 8, book.getReprint());// reprint
            setDoubleOrNull(stmt, 9, book.getPrice());// price
            stmt.setString(10, book.getLanguage());  // language
            stmt.setString(11, book.getStatus());  // status
            setDoubleOrNull(stmt, 12, book.getChapter()); // chapter
            stmt.setString(13, book.getSummary());  // summary
            stmt.setBytes(14, book.getQrCode());  // qrCode
            stmt.setBytes(15, book.getCoverImages());  // coverImages
            setIntOrNull(stmt, 16, book.getPages()); // year
            stmt.setInt(17, book.getDownloads());  // quantity
            stmt.setString(18, book.getId());  // id

            // Thực thi câu lệnh cập nhật
            int rowsAffected = stmt.executeUpdate();

            // Kiểm tra kết quả
            if (rowsAffected > 0) {
                Noti.showSuccessMessage("Cập nhật thông tin sách thành công!");
                System.out.println("Cập nhật thông tin sách thành công!");
            } else {
                Noti.showFailureMessage("Không tìm thấy sách với ID: " + book.getId());
                System.out.println("Không tìm thấy sách với ID: " + book.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Noti.showFailureMessage("Lỗi khi cập nhật thông tin sách!");
            System.out.println("Lỗi khi cập nhật thông tin sách.");
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
                book.setPages(getInt(resultSet,"pages"));
                book.setDownloads(resultSet.getInt("downloads"));
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
        if(check.get()==false)
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
            if (book.getYear()!=null) {
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
                System.out.println("hoa1");
            }
            if (book.getQuantity()!= 0) {
                if (checkAnd)
                    sql += " AND ";
                String sqlQuantity = "quantity = " + book.getQuantity();
                sql += sqlQuantity;
                checkAnd = true;
                System.out.println("hoa2");
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
    public ResultSet findBooksBorrow(Book book, String user_id, int localTable) {
        try {
            connection = DatabaseConnection.connectToLibrary();

            // Khởi tạo câu lệnh SQL cơ bản
            StringBuilder sql = new StringBuilder(
                    "SELECT books.*, borrow.status FROM books " +
                            "JOIN borrow ON books.id = borrow.book_id " +
                            "JOIN users ON users.user_id = borrow.user_id " +
                            "WHERE users.user_id = ?"
            );

            // Danh sách tham số
            List<Object> parameters = new ArrayList<>();
            parameters.add(user_id);

            // Thêm điều kiện tìm kiếm theo tiêu chí sách
            if (!book.getTitle().isEmpty()) {
                sql.append(" AND books.title = ?");
                parameters.add(book.getTitle());
            }
            if (!book.getAuthor().isEmpty()) {
                sql.append(" AND books.author = ?");
                parameters.add(book.getAuthor());
            }
            if (!book.getGenre().isEmpty()) {
                sql.append(" AND books.genre = ?");
                parameters.add(book.getGenre());
            }

            // Thêm điều kiện trạng thái mượn trả
            if (localTable == 0) {
                sql.append(" AND borrow.status = 'borrowed'");
            } else if (localTable == 1) {
                sql.append(" AND borrow.status = 'returned'");
            } else if (localTable == 2) {
                sql.append(" AND borrow.status = 'not borrowed'");
            }

            // Chuẩn bị câu lệnh
            statement = connection.prepareStatement(sql.toString());

            // Gán các tham số
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }

            // Thực thi câu lệnh
            return statement.executeQuery();

        } catch (SQLException e) {
            System.out.println("Lỗi khi tìm sách: " + e.getMessage());
        }
        return null;
    }

    public ResultSet findBooksUtimate(String keyword) {

            try  {
                connection = DatabaseConnection.connectToLibrary();
                /*CAST(id AS CHAR), ' ', title, ' ', author, ' ', publisher, ' ',
                        CAST(year AS CHAR), ' ', genre, ' ',
                        CAST(quantity AS CHAR), ' ', edition, ' ',
                        CAST(reprint AS CHAR), ' ', CAST(price AS CHAR), ' ',
                        language, ' ', status, ' ', summary, ' ', CAST(chapter AS CHAR)*/

                String   sql = "SELECT id,title,author,cover_image FROM books \n" +
                            "WHERE CONCAT_WS(' ', id, title, author, publisher, year," +
                            " genre, quantity, edition, reprint, price, language," +
                            " status, summary, chapter) LIKE ";
                if (keyword.isEmpty()) {
                    sql = "SELECT id,title,author,cover_image FROM books";
                }
                else {
                    sql += "'%" + keyword + "%'";
                }
                statement = connection.prepareStatement(sql);

                ResultSet resultSet = statement.executeQuery();
                return resultSet;
            } catch (SQLException e) {
                System.out.println("Lỗi khi tìm sách: " + e.getMessage());
            }
        return null;
    }
    public ResultSet findBooksUltimateBorrow(String keyword, int localTable) {
        try {
            connection = DatabaseConnection.connectToLibrary();

            // Base SQL query
            StringBuilder sql = new StringBuilder(
                    "SELECT books.*, borrow.status " +
                            "FROM books " +
                            "LEFT JOIN borrow ON books.id = borrow.book_id"
            );

            // Danh sách tham số
            List<Object> parameters = new ArrayList<>();

            // Điều kiện tìm kiếm
            List<String> conditions = new ArrayList<>();

            // Thêm điều kiện trạng thái nếu cần
            if (localTable == 0) {
                conditions.add("borrow.status = 'borrowed'");
            } else if (localTable == 1) {
                conditions.add("borrow.status = 'returned'");
            } else if (localTable == 2) {
                conditions.add("borrow.status = 'not borrowed'");
            }

            // Thêm điều kiện tìm kiếm từ khóa nếu có
            if (!keyword.isEmpty()) {
                conditions.add("CONCAT_WS(' ', books.id, books.title, books.author, books.publisher, " +
                        "books.year, books.genre, books.quantity, books.edition, books.reprint, " +
                        "books.price, books.language, books.status, books.summary, books.chapter) LIKE ?");
                parameters.add("%" + keyword + "%");
            }

            // Gắn các điều kiện
            if (!conditions.isEmpty()) {
                sql.append(" WHERE ").append(String.join(" AND ", conditions));
            }

            // Chuẩn bị câu lệnh
            statement = connection.prepareStatement(sql.toString());

            // Gán các tham số
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }

            // Thực thi câu lệnh
            return statement.executeQuery();

        } catch (SQLException e) {
            System.out.println("Lỗi khi tìm sách: " + e.getMessage());
        }
        return null;
    }

    public void updateDownload(String bookId, int downloads) {
        // Câu lệnh SQL để cập nhật thông tin sách
        String sql = "UPDATE books SET downloads = ? WHERE id = ?";

        // Kết nối cơ sở dữ liệu và thực thi câu lệnh
        try (Connection conn = DatabaseConnection.connectToLibrary(); // Sử dụng kết nối từ DatabaseConnection
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Gán giá trị cho các tham số từ đối tượng Book
            stmt.setInt(1, downloads);  // quantity
            stmt.setString(2, bookId);  // id

            // Thực thi câu lệnh cập nhật
            int rowsAffected = stmt.executeUpdate();

            // Kiểm tra kết quả
            if (rowsAffected > 0) {
                System.out.println("Cập nhật thông tin sách thành công!");
            } else {
                System.out.println("Không tìm thấy sách với ID: " + bookId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi khi cập nhật thông tin sách.");
        }
    }

    public boolean isAvailable() {
        return Available;
    }
    public void setAvailable(boolean available) {
        Available = available;
    }

    public AtomicBoolean isCheck() {return check;}
    public void setCheck(AtomicBoolean check) {this.check = check;}

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
        Integer type0 = resultSet.getInt(type);
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
