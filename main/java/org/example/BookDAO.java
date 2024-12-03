package org.example;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.*;
import java.util.concurrent.*;
import javafx.concurrent.Task;
import javafx.util.Pair;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Future;

public class BookDAO {
    private LibraryManagement library;
    private boolean Available;
    private AtomicBoolean check;
    private AtomicBoolean checkYear;
    private AtomicBoolean checkQuantity;
    private Integer year;
    private Integer quantity;
    private Task<Void> currentTask;
    private List<ExecutorService> executorServices = new ArrayList<>();
    private List<Task<Void>> tasks = new ArrayList<>();
    private ExecutorService executorService;

    public BookDAO(LibraryManagement library, Task<Void> currentTask, ExecutorService executorService) {
        check=new AtomicBoolean(false);
        checkYear=new AtomicBoolean(false);
        checkQuantity=new AtomicBoolean(false);
        year = null;
        quantity = null;
        this.library = library;
        this.currentTask = currentTask;
        this.executorService = executorService;
        executorServices.add(executorService);
        tasks.add(currentTask);
    }

    public boolean addBook(Book book) {
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
                    System.out.println("Sách đã được thêm thành công!");
                    return true;
                } else {
                    System.out.println("Lỗi khi thêm sách!");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm sách: " + e.getMessage());
            return false;
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
            Noti.showFailureMessage("Lỗi:" + e.getMessage());
            System.out.println("Lỗi khi cập nhật thông tin sách.");
        }
    }
    public void removeBook(String id) {
        try (Connection connection = DatabaseConnection.connectToLibrary()) {
            String sql = "DELETE FROM books WHERE id = " + "'" + id + "'";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Sách đã được xóa thành công!");
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa sách: " + e.getMessage());
        }
    }
    // input 1 giá trị
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
    // tìm sách cho admin cụ thể
    public void dataFindBooks(ObservableList<Book> data, Book book, LibraryManagement library,
                              List<String> a, TableView<Book> tableView) {
        shutdownCurrentTask();
        executorService = Executors.newSingleThreadExecutor();
        a.clear();
        data.clear();
        boolean checkAnd = false;
        if(!checkYear.get()&&!checkQuantity.get())
            try  {
                Connection connection = DatabaseConnection.connectToLibrary();
                String sql = "SELECT * FROM books WHERE ";
                if (book.getId()!=null&&!book.getId().isEmpty()) {
                    String sqlId = "id = '" + book.getId() + "'";
                    sql += sqlId;
                    checkAnd = true;
                }
                if (book.getTitle()!=null&&!book.getTitle().isEmpty()) {
                    if (checkAnd)
                        sql += " AND ";
                    String sqlTitle = "title = '" + book.getTitle() + "'";
                    sql += sqlTitle;
                    checkAnd = true;
                }
                if (book.getAuthor()!=null&&!book.getAuthor().isEmpty()) {
                    if (checkAnd)
                        sql += " AND ";
                    String sqlAuthor = "author = '" + book.getAuthor() + "'";
                    sql += sqlAuthor;
                    checkAnd = true;
                }
                if (book.getStatus()!=null&&!book.getStatus().isEmpty()&&book.getStatus().compareTo("all")!=0) {
                    if (checkAnd)
                        sql += " AND ";
                    String sqlPublisher = "status = '" + book.getStatus() + "'";
                    sql += sqlPublisher;
                    checkAnd = true;
                }
                if (year!=null) {
                    System.out.println(sql + " year");
                    if (checkAnd)
                        sql += " AND ";
                    String sqlYear = "year = " + year;
                    sql += sqlYear;
                    checkAnd = true;
                }
                if (book.getGenre()!=null&&!book.getGenre().isEmpty()) {
                    if (checkAnd)
                        sql += " AND ";
                    String sqlGenre = "genre = '" + book.getGenre() + "'";
                    sql += sqlGenre;
                    checkAnd = true;
                    System.out.println("hoa1");
                }
                if (quantity!=null) {
                    if (checkAnd)
                        sql += " AND ";
                    String sqlQuantity = "quantity = " + quantity;
                    sql += sqlQuantity;
                    checkAnd = true;
                    System.out.println("hoa2");
                }
                if (!checkAnd) {
                    sql = "SELECT * FROM books";
                }
               PreparedStatement statement = connection.prepareStatement(sql);
                  ResultSet resultSet = statement.executeQuery();
                // Tạo Task để xử lý trong luồng nền
                currentTask = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            while (resultSet.next()) {
                                Book book = new Book(
                                        false,
                                        resultSet.getString("id"),
                                        resultSet.getString("title"),
                                        resultSet.getString("author"),
                                        resultSet.getString("status"),
                                        library.getInt(resultSet, "year"),
                                        resultSet.getString("genre"),
                                        resultSet.getInt("quantity"),
                                        "chi tiết"
                                );

                                Platform.runLater(() -> data.add(book));
                                Thread.sleep(10);
                                // Thêm lắng nghe vào TableView
                                Platform.runLater(() -> {
                                    book.selectedProperty().addListener((observable, oldValue, newValue) -> {
                                        if (newValue) {
                                            a.add(book.getId());
                                        } else {
                                            a.remove(book.getId());

                                        }
                                    });
                                });
                            }
                        } finally {

                            try {
                                if (resultSet != null) resultSet.close();
                                if (statement != null) statement.close();
                                if (connection != null) connection.close();
                            } catch (SQLException e) {
                                System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
                            }

                        }
                        return null;
                    }
                };


                currentTask.setOnSucceeded(event -> {
                    System.out.println("Hoàn thành tải dữ liệu sách người dùng đã mượn!");
                    executorService.shutdown();
                    shutdownCurrentTask();
                });

                currentTask.setOnFailed(event -> {
                    System.err.println("Có lỗi xảy ra: " + currentTask.getException().getMessage());
                    executorService.shutdown();
                    shutdownCurrentTask();
                });

                // Gửi Task vào ExecutorService
                executorService.submit(currentTask);

            } catch (SQLException e) {
                System.err.println("Lỗi khi kết nối cơ sở dữ liệu: " + e.getMessage());
            }
    }
    // tìm sách cho admin khi nhấn vào quản lý tài liệu ko cụ thể
    public void dataFindBooks(ObservableList<Book> data, LibraryManagement library,
                          List<String> a, TableView<Book> tableView) {
        shutdownCurrentTask();
        executorService = Executors.newSingleThreadExecutor();
        a.clear();
        data.clear();
        String sql = "SELECT * FROM books LIMIT 1000";
            try  {
                Connection connection = DatabaseConnection.connectToLibrary();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
                // Tạo Task để xử lý trong luồng nền
                currentTask = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            while (resultSet.next()) {
                                Book book = new Book(
                                        false,
                                        resultSet.getString("id"),
                                        resultSet.getString("title"),
                                        resultSet.getString("author"),
                                        resultSet.getString("status"),
                                        library.getInt(resultSet, "year"),
                                        resultSet.getString("genre"),
                                        resultSet.getInt("quantity"),
                                        "chi tiết"
                                );

                                Platform.runLater(() -> data.add(book));
                                Thread.sleep(10);
                                // Thêm lắng nghe vào TableView
                                Platform.runLater(() -> {
                                    book.selectedProperty().addListener((observable, oldValue, newValue) -> {
                                        if (newValue) {
                                            a.add(book.getId());
                                        } else {
                                            a.remove(book.getId());

                                        }
                                    });
                                });
                            }
                        } finally {

                            try {
                                if (resultSet != null) resultSet.close();
                                if (statement != null) statement.close();
                                if (connection != null) connection.close();
                            } catch (SQLException e) {
                                System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
                            }

                        }
                        return null;
            }
    };

        currentTask.setOnSucceeded(event -> {
            System.out.println("Hoàn thành tải dữ liệu sách đã mượn!");
            executorService.shutdown();
            shutdownCurrentTask();
        });
                currentTask.setOnSucceeded(event -> {
        System.out.println("Hoàn thành tải dữ liệu sách người dùng đã mượn!");
        executorService.shutdown();
        shutdownCurrentTask();
    });

               currentTask.setOnFailed(event -> {
        System.err.println("Có lỗi xảy ra: " + currentTask.getException().getMessage());
        executorService.shutdown();
    });

    // Gửi Task vào ExecutorService
               executorService.submit(currentTask);

} catch (SQLException e) {
        System.err.println("Lỗi khi kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }
    // tìm thể loại khác
    public VBox findOthergenres(Node node) {

        VBox vBox = new VBox(10);
        // Hủy task cũ nếu đang chạy
        shutdownCurrentTask();
        executorService = Executors.newSingleThreadExecutor();

        // Kiểm tra nếu node là Button, vô hiệu hóa Button
        if (node instanceof Button) {
            Button loadButton = (Button) node;
            Platform.runLater(() -> loadButton.setDisable(true));
        }

        try {
            Connection connection = DatabaseConnection.connectToLibrary();
            List<String> genres = new FileHandler("genre.txt").readFromFile();
            StringBuilder sql = new StringBuilder("SELECT id,title,author,cover_image,  AVG(r.rating) AS average_rating FROM books b\n" +
                    "LEFT JOIN book_reviews r ON b.id = r.book_id WHERE genre NOT IN (");

            for (int i = 0; i < genres.size(); i++) {
                sql.append("?");
                if (i < genres.size() - 1) {
                    sql.append(", ");
                }
            }

            sql.append(")GROUP BY  b.id,  b.title,  b.author\n" +
                    "ORDER BY b.downloads DESC,average_rating DESC, b.date_added DESC,  b.id;");
            PreparedStatement statement = connection.prepareStatement(sql.toString());

                for (int i = 0; i < genres.size(); i++) {
                    statement.setString(i + 1, genres.get(i));
                }

                ResultSet resultSet = statement.executeQuery();

            currentTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    int i = 0;  // Biến đếm số lượng sách trong mỗi HBox
                    HBox hBox = new HBox(30);  // Tạo HBox để chứa các sách
                    final HBox tempHBox0 = hBox;  // Lưu HBox vào biến tạm
                    Platform.runLater(() -> vBox.getChildren().add(tempHBox0));  // Thêm HBox vào VBox

                    while (resultSet.next()) {
                        String id = resultSet.getString("id");
                        String title = resultSet.getString("title");
                        String author = resultSet.getString("author");
                        Image coverImage;

                        if (resultSet.getBytes("cover_image") != null) {
                            coverImage = new Image(new ByteArrayInputStream(resultSet.getBytes("cover_image")));
                        } else {
                            coverImage = new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/book.jpg");
                        }

                        VBox bookVBox = bookVBox(id, title, author, coverImage);
                        // Tạo hiệu ứng FadeTransition cho bookVBox
                        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), bookVBox);
                        fadeTransition.setFromValue(0);  // Bắt đầu từ mờ
                        fadeTransition.setToValue(1);    // Kết thúc là rõ
                        fadeTransition.setCycleCount(1);
                        fadeTransition.setAutoReverse(false);

                        // Thêm hiệu ứng vào Timeline để đồng bộ hóa việc thêm sách
                        final HBox lastHBox = hBox;
                        Platform.runLater(() -> {
                            lastHBox.getChildren().add(bookVBox);
                            fadeTransition.play();  // Chạy hoạt hình mờ dần
                        });
                       Thread.sleep(100);
                        i++;

                        // Nếu có đủ 6 phần tử trong hBox, thêm hBox vào vBox và tạo hBox mới
                        if (i == 6) {
                            final HBox tempHBox = new HBox(30);  // Lưu HBox vào biến tạm
                            Platform.runLater(() -> vBox.getChildren().add(tempHBox));  // Thêm HBox vào VBox
                            hBox = tempHBox;  // Tạo một HBox mới
                            i = 0;  // Đặt lại biến đếm
                        }
                    }
                    return null;
                }
            };
            // Chạy Task trong thread nền
            executorService.submit(currentTask);

            // Sau khi hoàn thành, kích hoạt lại nút nếu là Button
            currentTask.setOnSucceeded(event -> {
                if (node instanceof Button) {
                    Button loadButton = (Button) node;
                    Platform.runLater(() -> loadButton.setDisable(false)); // Kích hoạt lại Button
                    shutdownCurrentTask();
                }
                Platform.runLater(() ->System.out.println(vBox.getChildren().size() + " hoa"));
                try {
                    connection.close();
                    statement.close();
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

            });
            currentTask.setOnFailed(event -> {
                System.out.println("Task failed: " + currentTask.getException().getMessage());
                shutdownCurrentTask();
            });
            currentTask.setOnCancelled(event -> {
                System.out.println("Task was cancelled.");
                shutdownCurrentTask();
            });

            return vBox;

        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn sách đề xuất: " + e.getMessage());
        }
        return vBox;
    }
    //tìm thể loại
    public VBox findGenre(String genre, Node node) {

        VBox vBox = new VBox(10);

        shutdownCurrentTask();
        executorService = Executors.newSingleThreadExecutor();

        // Kiểm tra nếu node là Button, vô hiệu hóa Button
        if (node instanceof Button) {
            Button loadButton = (Button) node;
            Platform.runLater(() -> loadButton.setDisable(true));
        }
            try {
                Connection connection = DatabaseConnection.connectToLibrary();

                String sql = "SELECT id,title,author,cover_image,  AVG(r.rating) AS average_rating FROM books b\n" +
                        "LEFT JOIN book_reviews r ON b.id = r.book_id \n" +
                        "WHERE genre = ?\n" +
                        "GROUP BY  b.id,  b.title,  b.author\n" +
                        "ORDER BY b.downloads DESC,average_rating DESC, b.date_added DESC,  b.id; ";

               PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1, genre);
                    ResultSet resultSet = statement.executeQuery();

                currentTask = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        int i = 0;  // Biến đếm số lượng sách trong mỗi HBox
                        HBox hBox = new HBox(30);  // Tạo HBox để chứa các sách
                        final HBox tempHBox0 = hBox;  // Lưu HBox vào biến tạm
                        Platform.runLater(() -> vBox.getChildren().add(tempHBox0));  // Thêm HBox vào VBox

                        while (resultSet.next()) {
                            String id = resultSet.getString("id");
                            String title = resultSet.getString("title");
                            String author = resultSet.getString("author");
                            Image coverImage;

                            if (resultSet.getBytes("cover_image") != null) {
                                coverImage = new Image(new ByteArrayInputStream(resultSet.getBytes("cover_image")));
                            } else {
                                coverImage = new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/book.jpg");
                            }

                            VBox bookVBox = bookVBox(id, title, author, coverImage);
                            // Tạo hiệu ứng FadeTransition cho bookVBox
                            FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), bookVBox);
                            fadeTransition.setFromValue(0);  // Bắt đầu từ mờ
                            fadeTransition.setToValue(1);    // Kết thúc là rõ
                            fadeTransition.setCycleCount(1);
                            fadeTransition.setAutoReverse(false);

                            // Thêm hiệu ứng vào Timeline để đồng bộ hóa việc thêm sách
                            final HBox lastHBox = hBox;
                            Platform.runLater(() -> {
                                lastHBox.getChildren().add(bookVBox);
                                fadeTransition.play();  // Chạy hoạt hình mờ dần
                            });
                           Thread.sleep(100);
                            i++;

                            // Nếu có đủ 6 phần tử trong hBox, thêm hBox vào vBox và tạo hBox mới
                            if (i == 6) {
                                final HBox tempHBox = new HBox(30);  // Lưu HBox vào biến tạm
                                Platform.runLater(() -> vBox.getChildren().add(tempHBox));  // Thêm HBox vào VBox
                                hBox = tempHBox;  // Tạo một HBox mới
                                i = 0;  // Đặt lại biến đếm
                            }
                        }
                        return null;
                    }
                };
                // Chạy Task trong thread nền
                executorService.submit(currentTask);

                // Sau khi hoàn thành, kích hoạt lại nút nếu là Button
                currentTask.setOnSucceeded(event -> {
                    if (node instanceof Button) {
                        Button loadButton = (Button) node;
                        Platform.runLater(() -> loadButton.setDisable(false)); // Kích hoạt lại Button
                        shutdownCurrentTask();
                    }
                    Platform.runLater(() ->System.out.println(vBox.getChildren().size() + " hoa"));
                    try {
                        connection.close();
                        statement.close();
                        resultSet.close();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }

                });
                currentTask.setOnFailed(event -> {
                    System.out.println("Task failed: " + currentTask.getException().getMessage());
                    shutdownCurrentTask();
                });
                currentTask.setOnCancelled(event -> {
                    System.out.println("Task was cancelled.");
                    shutdownCurrentTask();
                });

                return vBox;

            } catch (SQLException e) {
                System.err.println("Lỗi khi truy vấn sách đề xuất: " + e.getMessage());
            }
        return vBox;
    }
    //15 cuốn sách mượn nhều nhất
    public HBox findTopBorrownedBooks() {

        HBox hBox = new HBox(30);
        executorService = Executors.newSingleThreadExecutor();

        String sql = "SELECT id,title,author,cover_image FROM books ORDER BY downloads DESC LIMIT 15";

        try {
            Connection connection = DatabaseConnection.connectToLibrary();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            currentTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    while (resultSet.next()) {
                        String id = resultSet.getString("id");
                        String title = resultSet.getString("title");
                        String author = resultSet.getString("author");
                        Image coverImage;
                        // Kiểm tra ảnh bìa
                        if (resultSet.getBytes("cover_image") != null) {
                            coverImage = new Image(new ByteArrayInputStream(resultSet.getBytes("cover_image")));
                        } else {
                            coverImage = new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/book.jpg");
                        }
                        VBox bookVBox = bookVBox(id, title, author, coverImage);

                        // Tạo hiệu ứng mờ dần cho mỗi cuốn sách
                        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), bookVBox);
                        fadeTransition.setFromValue(0);  // Bắt đầu từ mờ
                        fadeTransition.setToValue(1);    // Kết thúc là rõ
                        fadeTransition.setCycleCount(1);
                        fadeTransition.setAutoReverse(false);

                        // Cập nhật giao diện trên UI thread
                        Platform.runLater(() -> {
                            hBox.getChildren().add(bookVBox);  // Thêm VBox vào HBox
                            fadeTransition.play();  // Chạy hoạt hình mờ dần
                        });
                       Thread.sleep(20);
                    }
                    return null;
                }
            };
            // Vô hiệu hóa nút khi bắt đầu tải


            // Chạy Task trong background thread
            executorService.submit(currentTask);

            // Xóa vòng tròn loading khi Task hoàn thành và kích hoạt lại nút
           /* currentTask.setOnSucceeded(event -> {
                System.out.println("Task was thanh công.");
            });
            currentTask.setOnFailed(event -> {
                System.out.println("Task failed: " + currentTask.getException().getMessage());
            });
            currentTask.setOnCancelled(event -> {
                System.out.println("Task was cancelled.");
            });
*/
            return hBox;
        } catch (SQLException e) {
            System.out.println("Lỗi khi tìm sách mới: " + e.getMessage());
        }
        return hBox;
    }
    //15 cuốn sách mới nhất
    public HBox findNewBooks() {

        HBox hBox = new HBox(30);
        executorService = Executors.newSingleThreadExecutor();

        String sql = "SELECT id,title,author,cover_image FROM books ORDER BY date_added DESC LIMIT 15";

        try {
            Connection connection = DatabaseConnection.connectToLibrary();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            currentTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                Image coverImage;
                // Kiểm tra ảnh bìa
                if (resultSet.getBytes("cover_image") != null) {
                    coverImage = new Image(new ByteArrayInputStream(resultSet.getBytes("cover_image")));
                } else {
                    coverImage = new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/book.jpg");
                }
                VBox bookVBox = bookVBox(id, title, author, coverImage);

                // Tạo hiệu ứng mờ dần cho mỗi cuốn sách
                FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), bookVBox);
                fadeTransition.setFromValue(0);  // Bắt đầu từ mờ
                fadeTransition.setToValue(1);    // Kết thúc là rõ
                fadeTransition.setCycleCount(1);
                fadeTransition.setAutoReverse(false);

                // Cập nhật giao diện trên UI thread
                Platform.runLater(() -> {
                    hBox.getChildren().add(bookVBox);  // Thêm VBox vào HBox
                    fadeTransition.play();  // Chạy hoạt hình mờ dần
                });
               Thread.sleep(20);
                // Giả lập độ trễ (nếu cần kiểm tra UI)
            }
            return null;
        }
    };
            // Vô hiệu hóa nút khi bắt đầu tải

            // Chạy Task trong background thread
            executorService.submit(currentTask);

            // Xóa vòng tròn loading khi Task hoàn thành và kích hoạt lại nút
            /*currentTask.setOnSucceeded(event -> {
                System.out.println("Task was thành công.");
            });
            currentTask.setOnFailed(event -> {
                System.out.println("Task failed: " + currentTask.getException().getMessage());
            });
            currentTask.setOnCancelled(event -> {
                System.out.println("Task was cancelled.");
            });*/

            return hBox;
        } catch (SQLException e) {
            System.out.println("Lỗi khi tìm sách mới: " + e.getMessage());
        }
        return hBox;
    }
    //15 cuốn sách đề xuất
    public HBox findRelatedBooksLimit15(String userId) {

        ExecutorService executorServiceSQl = Executors.newFixedThreadPool(10);
        HBox hBox = new HBox(30);
        executorService = Executors.newSingleThreadExecutor();


        try {
            Connection connection = DatabaseConnection.connectToLibrary();
            // Đọc các file SQL
            Callable<String>[] fileReaders = new Callable[]{
                    () -> new FileHandler("table11.txt").readFromFileAsString(),
                    () -> new FileHandler("table12.txt").readFromFileAsString(),
                    () -> new FileHandler("table21.txt").readFromFileAsString(),
                    () -> new FileHandler("table22.txt").readFromFileAsString(),
                    () -> new FileHandler("table31.txt").readFromFileAsString(),
                    () -> new FileHandler("table32.txt").readFromFileAsString(),
                    () -> new FileHandler("table41.txt").readFromFileAsString(),
                    () -> new FileHandler("table42.txt").readFromFileAsString(),
                    () -> new FileHandler("table51.txt").readFromFileAsString(),
                    () -> new FileHandler("table52.txt").readFromFileAsString(),
                    () -> new FileHandler("sql.txt").readFromFileAsString()
            };
            // Thực thi đọc file song song
            List<Future<String>> fileContents = executorServiceSQl.invokeAll(List.of(fileReaders));

            // Thu thập nội dung các tệp
            String table11 = fileContents.get(0).get();
            String table12 = fileContents.get(1).get();
            String table21 = fileContents.get(2).get();
            String table22 = fileContents.get(3).get();
            String table31 = fileContents.get(4).get();
            String table32 = fileContents.get(5).get();
            String table41 = fileContents.get(6).get();
            String table42 = fileContents.get(7).get();
            String table51 = fileContents.get(8).get();
            String table52 = fileContents.get(9).get();
            String sql = fileContents.get(10).get();
            String[] tableQueries = {table41, table42, table51, table52};

            try (PreparedStatement tempStatement = connection.prepareStatement(table11)) {
                tempStatement.executeUpdate();
            }
            try (PreparedStatement tempStatement = connection.prepareStatement(table21)) {
                tempStatement.executeUpdate();
            }
            try (PreparedStatement tempStatement = connection.prepareStatement(table31)) {
                tempStatement.executeUpdate();
            }
            try (PreparedStatement tempStatement = connection.prepareStatement(table12)) {
                for(int i = 1; i <= 6; i++)
                    tempStatement.setString(i, userId);
                tempStatement.executeUpdate();
            }
            try (PreparedStatement tempStatement = connection.prepareStatement(table22)) {
                tempStatement.setString(1, userId);
                tempStatement.executeUpdate();
            }
            try (PreparedStatement tempStatement = connection.prepareStatement(table32)) {
                tempStatement.setString(1, userId);
                tempStatement.executeUpdate();
            }
            // Thực thi từng câu lệnh tạo bảng tạm
            for (String query : tableQueries) {
                try (PreparedStatement tempStatement = connection.prepareStatement(query)) {
                    tempStatement.executeUpdate();
                }
            }
            sql+= "LIMIT 15";

            // Thực thi truy vấn chính
            PreparedStatement finalStatement = connection.prepareStatement(sql);
            ResultSet resultSet = finalStatement.executeQuery();


            currentTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    while (resultSet.next()) {
                        String id = resultSet.getString("id");
                        String title = resultSet.getString("title");
                        String author = resultSet.getString("author");
                        Image coverImage;
                        // Kiểm tra ảnh bìa
                        if (resultSet.getBytes("cover_image") != null) {
                            coverImage = new Image(new ByteArrayInputStream(resultSet.getBytes("cover_image")));
                        } else {
                            coverImage = new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/book.jpg");
                        }
                        VBox bookVBox = bookVBox(id, title, author, coverImage);

                        // Tạo hiệu ứng mờ dần cho mỗi cuốn sách
                        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), bookVBox);
                        fadeTransition.setFromValue(0);  // Bắt đầu từ mờ
                        fadeTransition.setToValue(1);    // Kết thúc là rõ
                        fadeTransition.setCycleCount(1);
                        fadeTransition.setAutoReverse(false);

                        // Cập nhật giao diện trên UI thread
                        Platform.runLater(() -> {
                            hBox.getChildren().add(bookVBox);  // Thêm VBox vào HBox
                            fadeTransition.play();  // Chạy hoạt hình mờ dần
                        });
                       Thread.sleep(20);
                        // Giả lập độ trễ (nếu cần kiểm tra UI)
                    }
                    return null;
                }
            };
            // Chạy Task trong thread nền
            executorService.submit(currentTask);

            // Sau khi hoàn thành, kích hoạt lại nút nếu là Button
           /* currentTask.setOnSucceeded(event -> {
                Platform.runLater(() ->System.out.println(hBox.getChildren().size() + " hoa"));
                try {
                    connection.close();
                    finalStatement.close();
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            });
            currentTask.setOnFailed(event -> {
                System.out.println("Task failed: " + currentTask.getException().getMessage());
            });
            currentTask.setOnCancelled(event -> {
                System.out.println("Task was cancelled.");
            });*/

            return hBox;

        } catch (SQLException | InterruptedException | ExecutionException e) {
            System.err.println("Lỗi khi truy vấn sách đề xuất: " + e.getMessage());
        } finally {
            executorServiceSQl.shutdown();
        }
        return hBox;
    }
    //sách đề xuất
    public VBox findRelatedBooks(String userId, Node node) {

        ExecutorService executorServiceSQl = Executors.newFixedThreadPool(10);
        VBox vBox = new VBox(10);
       shutdownCurrentTask();
        executorService = Executors.newSingleThreadExecutor();

        // Kiểm tra nếu node là Button, vô hiệu hóa Button
        if (node instanceof Button) {
            Button loadButton = (Button) node;
            Platform.runLater(() -> loadButton.setDisable(true));
        }
        try {
            Connection connection = DatabaseConnection.connectToLibrary();
            // Đọc các file SQL
            Callable<String>[] fileReaders = new Callable[]{
                    () -> new FileHandler("table11.txt").readFromFileAsString(),
                    () -> new FileHandler("table12.txt").readFromFileAsString(),
                    () -> new FileHandler("table21.txt").readFromFileAsString(),
                    () -> new FileHandler("table22.txt").readFromFileAsString(),
                    () -> new FileHandler("table31.txt").readFromFileAsString(),
                    () -> new FileHandler("table32.txt").readFromFileAsString(),
                    () -> new FileHandler("table41.txt").readFromFileAsString(),
                    () -> new FileHandler("table42.txt").readFromFileAsString(),
                    () -> new FileHandler("table51.txt").readFromFileAsString(),
                    () -> new FileHandler("table52.txt").readFromFileAsString(),
                    () -> new FileHandler("sql.txt").readFromFileAsString()
            };
            // Thực thi đọc file song song
            List<Future<String>> fileContents = executorServiceSQl.invokeAll(List.of(fileReaders));

            // Thu thập nội dung các tệp
            String table11 = fileContents.get(0).get();
            String table12 = fileContents.get(1).get();
            String table21 = fileContents.get(2).get();
            String table22 = fileContents.get(3).get();
            String table31 = fileContents.get(4).get();
            String table32 = fileContents.get(5).get();
            String table41 = fileContents.get(6).get();
            String table42 = fileContents.get(7).get();
            String table51 = fileContents.get(8).get();
            String table52 = fileContents.get(9).get();
            String sql = fileContents.get(10).get();
            String[] tableQueries = {table41, table42, table51, table52};

            try (PreparedStatement tempStatement = connection.prepareStatement(table11)) {
                tempStatement.executeUpdate();
            }
            try (PreparedStatement tempStatement = connection.prepareStatement(table21)) {
                tempStatement.executeUpdate();
            }
            try (PreparedStatement tempStatement = connection.prepareStatement(table31)) {
                tempStatement.executeUpdate();
            }
            try (PreparedStatement tempStatement = connection.prepareStatement(table12)) {
                for(int i = 1; i <= 6; i++)
                    tempStatement.setString(i, userId);
                tempStatement.executeUpdate();
            }
            try (PreparedStatement tempStatement = connection.prepareStatement(table22)) {
                tempStatement.setString(1, userId);
                tempStatement.executeUpdate();
            }
            try (PreparedStatement tempStatement = connection.prepareStatement(table32)) {
                tempStatement.setString(1, userId);
                tempStatement.executeUpdate();
            }
            // Thực thi từng câu lệnh tạo bảng tạm
            for (String query : tableQueries) {
                try (PreparedStatement tempStatement = connection.prepareStatement(query)) {
                    tempStatement.executeUpdate();
                }
            }

            // Thực thi truy vấn chính
                PreparedStatement finalStatement = connection.prepareStatement(sql);
                ResultSet resultSet = finalStatement.executeQuery();

                    currentTask = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            int i = 0;  // Biến đếm số lượng sách trong mỗi HBox
                            HBox hBox = new HBox(30);  // Tạo HBox để chứa các sách
                            final HBox tempHBox0 = hBox;  // Lưu HBox vào biến tạm
                            Platform.runLater(() -> vBox.getChildren().add(tempHBox0));  // Thêm HBox vào VBox

                            while (resultSet.next()) {
                                String id = resultSet.getString("id");
                                String title = resultSet.getString("title");
                                String author = resultSet.getString("author");
                                Image coverImage;

                                if (resultSet.getBytes("cover_image") != null) {
                                    coverImage = new Image(new ByteArrayInputStream(resultSet.getBytes("cover_image")));
                                } else {
                                    coverImage = new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/book.jpg");
                                }

                                VBox bookVBox = bookVBox(id, title, author, coverImage);
                                // Tạo hiệu ứng FadeTransition cho bookVBox
                                FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), bookVBox);
                                fadeTransition.setFromValue(0);  // Bắt đầu từ mờ
                                fadeTransition.setToValue(1);    // Kết thúc là rõ
                                fadeTransition.setCycleCount(1);
                                fadeTransition.setAutoReverse(false);

                                // Thêm hiệu ứng vào Timeline để đồng bộ hóa việc thêm sách
                                final HBox lastHBox = hBox;
                                Platform.runLater(() -> {
                                    lastHBox.getChildren().add(bookVBox);
                                    fadeTransition.play();  // Chạy hoạt hình mờ dần
                                });
                               Thread.sleep(100);
                                i++;

                                // Nếu có đủ 6 phần tử trong hBox, thêm hBox vào vBox và tạo hBox mới
                                if (i == 6) {
                                    final HBox tempHBox = new HBox(30);  // Lưu HBox vào biến tạm
                                    Platform.runLater(() -> vBox.getChildren().add(tempHBox));  // Thêm HBox vào VBox
                                    hBox = tempHBox;  // Tạo một HBox mới
                                    i = 0;  // Đặt lại biến đếm
                                }
                            }
                            return null;
                        }
                    };
            // Chạy Task trong thread nền
            executorService.submit(currentTask);
            // Sau khi hoàn thành, kích hoạt lại nút nếu là Button
                currentTask.setOnSucceeded(event -> {
                    if (node instanceof Button) {
                        Button loadButton = (Button) node;
                        Platform.runLater(() -> loadButton.setDisable(false)); // Kích hoạt lại Button
                        shutdownCurrentTask();
                    }
                    Platform.runLater(() ->System.out.println(vBox.getChildren().size() + " hoa"));
                    try {
                        connection.close();
                        finalStatement.close();
                        resultSet.close();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                    shutdownCurrentTask();

                });
            currentTask.setOnFailed(event -> {
                    System.out.println("Task failed: " + currentTask.getException().getMessage());
                shutdownCurrentTask();
                });
            currentTask.setOnCancelled(event -> {
                    System.out.println("Task was cancelled.");
                shutdownCurrentTask();
                });

                return vBox;

        } catch (Exception e) {
            System.err.println("Lỗi khi truy vấn sách đề xuất: " + e.getMessage());
        } finally {
executorServiceSQl.shutdown();
        }
        return vBox;
    }
    //tìm sách theo keyWord
    public VBox findBooksUtimate(String keyword, Node node) {
        VBox vBox = new VBox(10);
       shutdownCurrentTask();
        executorService = Executors.newSingleThreadExecutor();

        // Kiểm tra nếu node là Button, vô hiệu hóa Button
        if (node instanceof Button) {
            Button loadButton = (Button) node;
            Platform.runLater(() -> loadButton.setDisable(true));
        }
        try {
            Connection connection = DatabaseConnection.connectToLibrary();
            String sql = "SELECT id,title,author,cover_image FROM books \n" +
                    "WHERE CONCAT_WS(' ', id, title, author, publisher, year," +
                    " genre, quantity, edition, reprint, price, language," +
                    " status) LIKE ";
            if (keyword.isEmpty()) {
                sql = "SELECT id,title,author,cover_image FROM books WHERE 1=0";
            } else {
                sql += "'%" + keyword + "%'";
            }
            PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery();
            currentTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    int i = 0;  // Biến đếm số lượng sách trong mỗi HBox
                    HBox hBox = new HBox(30);  // Tạo HBox để chứa các sách
                    final HBox tempHBox0 = hBox;  // Lưu HBox vào biến tạm
                    Platform.runLater(() -> vBox.getChildren().add(tempHBox0));  // Thêm HBox vào VBox

                    while (resultSet.next()) {
                        String id = resultSet.getString("id");
                        String title = resultSet.getString("title");
                        String author = resultSet.getString("author");
                        Image coverImage;

                        if (resultSet.getBytes("cover_image") != null) {
                            coverImage = new Image(new ByteArrayInputStream(resultSet.getBytes("cover_image")));
                        } else {
                            coverImage = new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/book.jpg");
                        }

                        VBox bookVBox = bookVBox(id, title, author, coverImage);
                        // Tạo hiệu ứng FadeTransition cho bookVBox
                        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), bookVBox);
                        fadeTransition.setFromValue(0);  // Bắt đầu từ mờ
                        fadeTransition.setToValue(1);    // Kết thúc là rõ
                        fadeTransition.setCycleCount(1);
                        fadeTransition.setAutoReverse(false);

                        // Thêm hiệu ứng vào Timeline để đồng bộ hóa việc thêm sách
                        final HBox lastHBox = hBox;
                        Platform.runLater(() -> {
                            lastHBox.getChildren().add(bookVBox);
                            fadeTransition.play();  // Chạy hoạt hình mờ dần
                        });
                        i++;
                       Thread.sleep(100);
                        // Nếu có đủ 6 phần tử trong hBox, thêm hBox vào vBox và tạo hBox mới
                        if (i == 6) {
                            final HBox tempHBox = new HBox(30);  // Lưu HBox vào biến tạm
                            Platform.runLater(() -> vBox.getChildren().add(tempHBox));  // Thêm HBox vào VBox
                            hBox = tempHBox;  // Tạo một HBox mới
                            i = 0;  // Đặt lại biến đếm
                        }
                    }
                    return null;
                }
            };
            // Chạy Task trong thread nền
            executorService.submit(currentTask);

            // Sau khi hoàn thành, kích hoạt lại nút nếu là Button
            currentTask.setOnSucceeded(event -> {
                if (node instanceof Button) {
                    Button loadButton = (Button) node;
                    Platform.runLater(() -> loadButton.setDisable(false)); // Kích hoạt lại Button
                    shutdownCurrentTask();
                }
                Platform.runLater(() ->System.out.println(vBox.getChildren().size() + " hoa"));
                try {
                    connection.close();
                    statement.close();
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

            });
            currentTask.setOnFailed(event -> {
                System.out.println("Task failed: " + currentTask.getException().getMessage());
            });
            currentTask.setOnCancelled(event -> {
                System.out.println("Task was cancelled.");
            });

            return vBox;

        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn sách đề xuất: " + e.getMessage());
        }
        return vBox;
    }
    // tìm sách đã mươn, đã trả cho user
    public void findBooksBorrowed(ObservableList<BookAndBorrow> data, String userId, List<String> arr,
                                  Book book, String keyword, int localTable) {
        shutdownCurrentTask();
        executorService = Executors.newSingleThreadExecutor();
        data.clear();
        arr.clear();
        try {
            // Kết nối và chuẩn bị câu lệnh SQL trước khi khởi chạy Task
            Connection connection = DatabaseConnection.connectToLibrary();
            StringBuilder sql = new StringBuilder(
                    "SELECT books.id, books.title, books.author, books.genre, " +
                            "borrow.borrow_id, borrow.borrow_date, borrow.return_date, borrow.due_date, " +
                            "borrow.status, users.user_id, users.full_name " +
                            "FROM books " +
                            "JOIN borrow ON books.id = borrow.book_id " +
                            "JOIN users ON users.user_id = borrow.user_id " +
                            "WHERE users.user_id = ?"
            );

            List<Object> parameters = new ArrayList<>();
            parameters.add(userId);

            // Thêm điều kiện tìm kiếm sách
            if (book != null) {
                if (book.getTitle() != null && !book.getTitle().isEmpty()) {
                    sql.append(" AND books.title LIKE ?");
                    parameters.add("%" + book.getTitle() + "%");
                }
                if (book.getAuthor() != null && !book.getAuthor().isEmpty()) {
                    sql.append(" AND books.author LIKE ?");
                    parameters.add("%" + book.getAuthor() + "%");
                }
                if (book.getGenre() != null && !book.getGenre().isEmpty()) {
                    sql.append(" AND books.genre LIKE ?");
                    parameters.add("%" + book.getGenre() + "%");
                }
            }

            // Thêm điều kiện trạng thái
            if (localTable == 0) {
                sql.append(" AND borrow.status = 'đã mượn'");
            } else if (localTable == 1) {
                sql.append(" AND borrow.status = 'trả đúng hạn'");
            } else if (localTable == 2) {
                sql.append(" AND borrow.status = 'trả quá hạn'");
            }

            // Thêm điều kiện tìm kiếm từ khóa
            if (keyword != null && !keyword.isEmpty()) {
                sql.append(" AND CONCAT_WS(' ', books.id, books.title, books.author, books.genre) LIKE ?");
                parameters.add("%" + keyword + "%");
            }

            PreparedStatement statement = connection.prepareStatement(sql.toString());
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }

            ResultSet resultSet = statement.executeQuery();

            // Tạo Task để xử lý trong luồng nền
            currentTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    while (resultSet.next()) {
                        BookAndBorrow bookAndBorrow = new BookAndBorrow(
                                resultSet.getInt("borrow_id"),
                                resultSet.getString("id"),
                                resultSet.getString("title"),
                                resultSet.getString("author"),
                                resultSet.getString("genre"),
                                getDate(resultSet, "borrow_date"),
                                getDate(resultSet, "due_date"),
                                getDate(resultSet, "return_date"),
                                resultSet.getString("user_id"),
                                resultSet.getString("full_name"),
                                "chi tiết"
                        );

                        Platform.runLater(() -> data.add(bookAndBorrow));
                        Platform.runLater(() -> {
                            bookAndBorrow.selectedProperty().addListener((observable, oldValue, newValue) -> {
                                if (newValue) {
                                    arr.add(bookAndBorrow.getId());
                                } else {
                                    arr.remove(bookAndBorrow.getId());
                                }
                            });
                        });
                    }
                    return null;
                }
            };

            // Đóng tài nguyên sau khi Task hoàn thành
            currentTask.setOnSucceeded(event -> {
                System.out.println("Hoàn thành tải dữ liệu sách đã mượn!");
                executorService.shutdown();
                try {
                    if (resultSet != null) resultSet.close();
                    if (statement != null) statement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
                }
                shutdownCurrentTask();
            });

            currentTask.setOnFailed(event -> {
                System.err.println("Có lỗi xảy ra: " + currentTask.getException().getMessage());
                executorService.shutdown();
                try {
                    if (resultSet != null) resultSet.close();
                    if (statement != null) statement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
                }
                shutdownCurrentTask();
            });

            // Gửi Task vào ExecutorService
            executorService.submit(currentTask);
        } catch (SQLException e) {
            System.err.println("Lỗi khi kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }
    // tìm sách dã mượn, đã trả cho admin
    public void findBooksUserBorrowed(ObservableList<BookAndBorrow> data, Book book, List<String> a, String userId, String fullName, String keyword, int localTable) {
        shutdownCurrentTask();
        executorService = Executors.newSingleThreadExecutor();
        a.clear();
        data.clear();
        try {
            Connection connection = DatabaseConnection.connectToLibrary();
            // Khởi tạo câu lệnh SQL cơ bản
            StringBuilder sql = new StringBuilder(
                    "SELECT borrow.borrow_id, books.id, books.title, books.author, books.genre, " +
                            "borrow.borrow_date, borrow.return_date, borrow.due_date, " +
                            "users.user_id, users.full_name, borrow.status " +
                            "FROM books " +
                            "JOIN borrow ON books.id = borrow.book_id " +
                            "JOIN users ON users.user_id = borrow.user_id "
            );

            // Cờ kiểm tra xem đã thêm điều kiện `WHERE` hay chưa
            boolean hasWhere = false;

            // Danh sách tham số
            List<Object> parameters = new ArrayList<>();

            // Thêm điều kiện tìm kiếm theo thuộc tính của sách (nếu có)
            if (book != null) {
                if (book.getTitle() != null && !book.getTitle().isEmpty()) {
                    if (!hasWhere) {
                        sql.append(" WHERE");
                        hasWhere = true;
                    }
                    sql.append(" books.title LIKE ?");
                    parameters.add("%" + book.getTitle() + "%");
                }
                if (book.getAuthor() != null && !book.getAuthor().isEmpty()) {
                    if (!hasWhere) {
                        sql.append(" WHERE");
                        hasWhere = true;
                    } else {
                        sql.append(" AND");
                    }
                    sql.append(" books.author LIKE ?");
                    parameters.add("%" + book.getAuthor() + "%");
                }
                if (book.getGenre() != null && !book.getGenre().isEmpty()) {
                    if (!hasWhere) {
                        sql.append(" WHERE");
                        hasWhere = true;
                    } else {
                        sql.append(" AND");
                    }
                    sql.append(" books.genre LIKE ?");
                    parameters.add("%" + book.getGenre() + "%");
                }
            }

            // Thêm điều kiện tìm kiếm userId
            if (userId != null && !userId.isEmpty()) {
                if (!hasWhere) {
                    sql.append(" WHERE");
                    hasWhere = true;
                } else {
                    sql.append(" AND");
                }
                sql.append(" users.user_id LIKE ?");
                parameters.add("%" + userId + "%");
            }

            // Thêm điều kiện tìm kiếm fullName
            if (fullName != null && !fullName.isEmpty()) {
                if (!hasWhere) {
                    sql.append(" WHERE");
                    hasWhere = true;
                } else {
                    sql.append(" AND");
                }
                sql.append(" users.full_name LIKE ?");
                parameters.add("%" + fullName + "%");
            }

            // Thêm điều kiện trạng thái mượn trả
            if (localTable == 0) {
                if (!hasWhere) {
                    sql.append(" WHERE");
                    hasWhere = true;
                } else {
                    sql.append(" AND");
                }
                sql.append(" borrow.status = 'đã mượn'");
            } else if (localTable == 1) {
                if (!hasWhere) {
                    sql.append(" WHERE");
                    hasWhere = true;
                } else {
                    sql.append(" AND");
                }
                sql.append(" borrow.status = 'trả đúng hạn'");
            } else if (localTable == 2) {
                if (!hasWhere) {
                    sql.append(" WHERE");
                    hasWhere = true;
                } else {
                    sql.append(" AND");
                }
                sql.append(" borrow.status = 'trả quá hạn'");
            } else if (localTable == 3) {
                if (!hasWhere) {
                    sql.append(" WHERE");
                    hasWhere = true;
                } else {
                    sql.append(" AND");
                }
                sql.append(" borrow.status = 'đang chờ duyệt'");
            }

            // Thêm điều kiện tìm kiếm từ khóa (nếu có)
            if (keyword != null && !keyword.isEmpty()) {
                if (!hasWhere) {
                    sql.append(" WHERE");
                    hasWhere = true;
                } else {
                    sql.append(" AND");
                }
                sql.append(" CONCAT_WS(' ', books.id, books.title, books.author, books.genre) LIKE ?");
                parameters.add("%" + keyword + "%");
            }

           PreparedStatement statement = connection.prepareStatement(sql.toString());

               // Gán các tham số
               for (int i = 0; i < parameters.size(); i++)
                   statement.setObject(i + 1, parameters.get(i));


               ResultSet resultSet = statement.executeQuery();
               // Tạo Task xử lý dữ liệu
               currentTask = new Task<Void>() {
                   @Override
                   protected Void call() throws Exception {
                       try {
                           while (resultSet.next()) {
                               BookAndBorrow bookAndBorrow = new BookAndBorrow(
                                       resultSet.getInt("borrow_id"),
                                       resultSet.getString("id"),
                                       resultSet.getString("title"),
                                       resultSet.getString("author"),
                                       resultSet.getString("genre"),
                                       getDate(resultSet, "borrow_date"),
                                       getDate(resultSet, "due_date"),
                                       getDate(resultSet, "return_date"),
                                       resultSet.getString("user_id"),
                                       resultSet.getString("full_name"),
                                       "chi tiết"
                               );
                               Platform.runLater(() -> data.add(bookAndBorrow));
                               Thread.sleep(10);
                               // Thêm lắng nghe vào TableView
                               Platform.runLater(() -> {
                                   bookAndBorrow.selectedProperty().addListener((observable, oldValue, newValue) -> {
                                           if (newValue) {
                                               a.add(bookAndBorrow.getId());
                                           } else {
                                               a.remove(bookAndBorrow.getId());

                                           }
                                       });
                                   });

                           }
                       } finally {
                           // Đảm bảo đóng tài nguyên sau khi tải dữ liệu xong
                           try {
                               if (resultSet != null) resultSet.close();
                               if (statement != null) statement.close();
                               if (connection != null) connection.close();
                           } catch (SQLException e) {
                               System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
                           }
                       }
                       return null;
                   }
               };

               currentTask.setOnSucceeded(event -> {
                   System.out.println("Hoàn thành tải dữ liệu sách người dùng đã mượn!");
                   executorService.shutdown();
               });

               currentTask.setOnFailed(event -> {
                   System.err.println("Có lỗi xảy ra: " + currentTask.getException().getMessage());
                   executorService.shutdown();
               });

               // Gửi Task vào ExecutorService
               executorService.submit(currentTask);

           } catch (SQLException e) {
               System.err.println("Lỗi khi kết nối cơ sở dữ liệu: " + e.getMessage());
           }
    }
    // yêu cầu
    public void requestFromUser(ObservableList<BookAndBorrow> data, String title, String author, String genre,
                                List<Triple<String, String, Integer>> a, String userId, String fullName, String keyword) {

        shutdownCurrentTask();
        executorService = Executors.newSingleThreadExecutor();
        data.clear();
        a.clear();
        try {
            // Kết nối và chuẩn bị câu lệnh SQL
            Connection connection = DatabaseConnection.connectToLibrary();

            StringBuilder sql = new StringBuilder(
                    "SELECT borrow.borrow_id, books.id, books.title, books.author, books.genre, \n" +
                            "borrow.borrow_date, borrow.return_date, borrow.due_date, \n" +
                            "users.user_id, users.full_name, borrow.status \n" +
                            "FROM books \n" +
                            "JOIN borrow ON books.id = borrow.book_id \n" +
                            "JOIN users ON users.user_id = borrow.user_id\n" +
                            "WHERE borrow.status = 'đang chờ duyệt'"
            );

            List<Object> parameters = new ArrayList<>();

            // Thêm điều kiện tìm kiếm sách

                if (title != null && !title.isEmpty()) {
                    sql.append(" AND books.title LIKE ?");
                    parameters.add("%" + title + "%");
                }
                if (author != null && !author.isEmpty()) {
                    sql.append(" AND books.author LIKE ?");
                    parameters.add("%" + author + "%");
                }
                if (genre != null && !genre.isEmpty()) {
                    sql.append(" AND books.genre LIKE ?");
                    parameters.add("%" +genre + "%");
                }


            // Thêm điều kiện userId
            if (userId != null && !userId.isEmpty()) {
                sql.append(" AND users.user_id LIKE ?");
                parameters.add("%" + userId + "%");
            }

            // Thêm điều kiện fullName
            if (fullName != null && !fullName.isEmpty()) {
                sql.append(" AND users.full_name LIKE ?");
                parameters.add("%" + fullName + "%");
            }

            // Thêm điều kiện tìm kiếm từ khóa
            if (keyword != null && !keyword.isEmpty()) {
                sql.append(" AND CONCAT_WS(' ', books.id, books.title, books.author, books.genre) LIKE ?");
                parameters.add("%" + keyword + "%");
            }

            PreparedStatement statement = connection.prepareStatement(sql.toString());
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }
            ResultSet resultSet = statement.executeQuery();

            // Tạo Task xử lý dữ liệu
            currentTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try {
                        while (resultSet.next()) {
                            BookAndBorrow bookAndBorrow = new BookAndBorrow(
                                    resultSet.getInt("borrow_id"),
                                    resultSet.getString("id"),
                                    resultSet.getString("title"),
                                    resultSet.getString("author"),
                                    resultSet.getString("genre"),
                                    getDate(resultSet, "borrow_date"),
                                    getDate(resultSet, "due_date"),
                                    getDate(resultSet, "return_date"),
                                    resultSet.getString("user_id"),
                                    resultSet.getString("full_name"),
                                    "chi tiết"
                            );
                            Platform.runLater(() -> data.add(bookAndBorrow));
                            Thread.sleep(10);
                            // Thêm lắng nghe vào TableView
                            Platform.runLater(() -> {
                                bookAndBorrow.selectedProperty().addListener((observable, oldValue, newValue) -> {
                                        if (newValue) {
                                            a.add(new Triple<>(bookAndBorrow.getUserId(),bookAndBorrow.getId(), bookAndBorrow.getBorrowId()));
                                        } else {
                                            a.removeIf(triple -> triple.getThird().equals(bookAndBorrow.getBorrowId()));
                                        }
                                    });
                            });
                        }

                    } finally {
                        // Đảm bảo đóng tài nguyên sau khi tải dữ liệu xong
                        try {
                            if (resultSet != null) resultSet.close();
                            if (statement != null) statement.close();
                            if (connection != null) connection.close();
                        } catch (SQLException e) {
                            System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
                        }
                    }
                    return null;
                }
            };

            currentTask.setOnSucceeded(event -> {
                System.out.println("Hoàn thành tải dữ liệu sách người dùng đã mượn!");
                executorService.shutdown();
                shutdownCurrentTask();
            });

            currentTask.setOnFailed(event -> {
                System.err.println("Có lỗi xảy ra: " + currentTask.getException().getMessage());
                executorService.shutdown();
                shutdownCurrentTask();
            });

            // Gửi Task vào ExecutorService
            executorService.submit(currentTask);

        } catch (SQLException e) {
            System.err.println("Lỗi khi kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }

    //kiểu của từng cuốn sách
    public VBox bookVBox(String id, String title, String author, Image image) {
        Label labelTitle = new Label(title);
        Label labelAuthor = new Label(author);
        ImageView imageView = new ImageView(image);
        // Bo tròn ảnh
        Circle clip = new Circle(10, 10, 10); // Tạo hình tròn có bán kính 75 (nửa chiều rộng/chiều cao của ImageView)
        //imageView.setClip(clip);

        imageView.setFitWidth(150);
        imageView.setFitHeight(200);
        labelTitle.setMaxHeight(40); // Giới hạn chiều cao để Label chỉ hiển thị 2 dòng
        labelTitle.setMaxWidth(150); // Giới hạn chiều cao để Label chỉ hiển thị 2 dòng
        labelTitle.setWrapText(true); // Cho phép xuống dòng khi vượt quá chiều rộng
        labelTitle.setEllipsisString("..."); // Thêm dấu "..." khi chữ bị cắt
        labelTitle.setStyle("-fx-text-fill: #000000; -fx-font-family: Arial; -fx-font-size: 15px; -fx-font-weight: normal;");

        labelAuthor.setMaxHeight(40);
        labelAuthor.setWrapText(true);
        labelAuthor.setStyle("-fx-text-fill: #808080; -fx-font-family: Arial; -fx-font-size: 15px; -fx-font-weight: normal;");
        labelAuthor.setMaxHeight(40); // Giới hạn chiều cao để Label chỉ hiển thị 2 dòng
        labelAuthor.setMaxWidth(150); // Giới hạn chiều cao để Label chỉ hiển thị 2 dòng

        labelTitle.setOnMouseEntered(event -> {
            labelTitle.setStyle("-fx-text-fill: #0000FF; -fx-font-family: Arial; -fx-font-size: 15px; -fx-underline: true;");
        });

        labelTitle.setOnMouseExited(event -> {
            labelTitle.setStyle("-fx-text-fill: #000000; -fx-font-family: Arial; -fx-font-size: 15px; -fx-font-weight: normal;");
        });

        VBox vBox = new VBox(0, imageView, labelTitle, labelAuthor);
        vBox.setMinWidth(150);
        AtomicBoolean check = new AtomicBoolean(true);
        vBox.setOnMouseClicked(event -> {
            check.set(true);
        });
        vBox.setOnMouseDragged(e->{
            check.set(false);
        });
        vBox.setOnMouseReleased(e->{
            if(check.get())
                library.showBook(id);
        });
        return vBox;
    }
    // update lại số lượng cho sách
    public static void updateDownload(String bookId, int downloads) {
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
    // khóa sách
    public void lockBook(String bookId) {
        // Câu lệnh SQL để cập nhật thông tin sách
        String sql = "UPDATE books SET status = ? WHERE id = ?";

        // Kết nối cơ sở dữ liệu và thực thi câu lệnh
        try (Connection conn = DatabaseConnection.connectToLibrary(); // Sử dụng kết nối từ DatabaseConnection
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Gán giá trị cho các tham số từ đối tượng Book
            stmt.setString(1, "not available");  // quantity
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
    // mở khóa sách
    public void unlockBook(String bookId) {
        // Câu lệnh SQL để cập nhật thông tin sách
        String sql = "UPDATE books SET status = ? WHERE id = ?";

        // Kết nối cơ sở dữ liệu và thực thi câu lệnh
        try (Connection conn = DatabaseConnection.connectToLibrary(); // Sử dụng kết nối từ DatabaseConnection
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Gán giá trị cho các tham số từ đối tượng Book
            stmt.setString(1, "available");  // quantity
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
    // dữ liệu BXH sách
    public static ObservableList<TopBook> dataTopBook() {
        ObservableList<TopBook> data =  FXCollections.observableArrayList();

        String sql = "SELECT \n" +
                "    b.book_id, \n" +
                "    bo.title, \n" +
                "    COALESCE(avg_rating, 0) AS average_rating, \n" +
                "    COUNT(b.book_id) AS borrow_count\n" +
                "FROM \n" +
                "    borrow b\n" +
                "JOIN \n" +
                "    books bo ON b.book_id = bo.id\n" +
                "LEFT JOIN \n" +
                "    (\n" +
                "        SELECT \n" +
                "            book_id, \n" +
                "             ROUND(AVG(rating), 1) AS avg_rating\n" +
                "        FROM \n" +
                "            book_reviews\n" +
                "        GROUP BY \n" +
                "            book_id\n" +
                "    ) bv ON bv.book_id = b.book_id\n" +
                "GROUP BY \n" +
                "    b.book_id, bo.title\n" +
                "ORDER BY \n" +
                "    borrow_count DESC\n" +
                "LIMIT 0, 100;";

        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                data.add(new TopBook(rs.getString("book_id"),
                                    rs.getString("title"),
                                    rs.getInt("borrow_count"),
                                    rs.getDouble("average_rating")));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }

    public static int totalBooks() {
        String sql = "select count(*) as total_books from books";

        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet resultSet = pstmt.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
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

    public AtomicBoolean getCheckYear() {return checkYear;}
    public void setCheckYear(AtomicBoolean checkYear) {this.checkYear = checkYear;}

    public AtomicBoolean getCheckQuantity() {return checkQuantity;}
    public void setCheckQuantity(AtomicBoolean checkQauntity) {this.checkQuantity = checkQauntity;}

    public Integer getYear() {return year;}
    public void setYear(Integer year) {this.year = year;}

    public Integer getQuantity() {return quantity;}
    public void setQuantity(Integer quantity) {this.quantity = quantity;}

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

    public LocalDate getDate(ResultSet resultSet, String type) throws SQLException {
        java.sql.Date sqlDate = resultSet.getDate(type);
        return resultSet.wasNull() ? null : sqlDate.toLocalDate();
    }

    public void shutdownAll() {
        // Hủy tất cả các task
        for (Task<Void> task : tasks) {
            if (task != null && !task.isDone()) {
                task.cancel(true);  // Hủy task hiện tại
            }
        }

        // Hủy tất cả các ExecutorService
        for (ExecutorService executorService : executorServices) {
            if (executorService != null && !executorService.isShutdown()) {
                executorService.shutdownNow();  // Dừng và hủy tất cả các luồng
            }
        }

    }

    public void shutdownCurrentTask() {
        if (currentTask != null && !currentTask.isDone()) {
            currentTask.cancel(true);  // Hủy task hiện tại
        }
        // Dọn dẹp ExecutorService cũ và tạo mới
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
        }
    }

}
