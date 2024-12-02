package org.example;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import java.io.*;
import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.time.format.DateTimeFormatter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.application.Platform;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.collections.ObservableList;
import javafx.beans.value.ChangeListener;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.TableView;
import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserDAO {
    private boolean Available;
    private AtomicBoolean check;
    private AtomicBoolean checkBorrow;
    private AtomicBoolean checkReturn;
    private LocalDate dateOfBirth;
    private Integer totalBooksBorred;
    private Integer totalBooksReturned;
    private Task<Void> currentTask;
    private List<ExecutorService> executorServices = new ArrayList<>();
    private List<Task<Void>> tasks = new ArrayList<>();
    private ExecutorService executorService;
    public UserDAO( Task<Void> currentTask, ExecutorService executorService) {
        check = new AtomicBoolean(false);
        checkBorrow = new AtomicBoolean(false);
        checkReturn = new AtomicBoolean(false);
        dateOfBirth = null;
        totalBooksBorred = null;
        totalBooksReturned =null;
        this.currentTask = currentTask;
        this.executorService = executorService;
    }
    //thêm
    public boolean addUser(User user) {
        String sql = "INSERT INTO users (user_id, full_name, date_of_birth, address, " +
                "phone_number, email, username, password_hash, membership_id, " +
                "join_date, membership_status, total_books_borrowed, total_books_returned, role, " +
                "card_registration_date, expiry_date, account_status, avatar, gender, department, class_name)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getFullName());
            setDate(pstmt,3, user.getDateOfBirth());
            pstmt.setString(4, user.getAddress());
            pstmt.setString(5, user.getPhoneNumber());
            setString(pstmt, 6, user.getEmail());
            pstmt.setString(7, user.getUsername());
            pstmt.setString(8, user.getPasswordHash());
            setString(pstmt,9,user.getMembershipId());
            setDate(pstmt,10, user.getJoinDate());
            pstmt.setString(11, user.getMembershipStatus());
            pstmt.setInt(12, user.getTotalBooksBorrowed());
            pstmt.setInt(13, user.gettotalBooksReturned());
            pstmt.setString(14, user.getRole());
            setDate(pstmt,15, user.getCardRegistrationDate());
            setDate(pstmt,16, user.getExpiryDate());
            pstmt.setString(17, user.getAccountStatus());
            pstmt.setBytes(18, user.getAvatars());
            pstmt.setString(19, user.getGender());
            pstmt.setString(20, user.getDepartment());
            pstmt.setString(21, user.getClassName());

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Người dùng đã được thêm thành công!");
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }
    //sửa
    public void updateUser(User user) {
        // Câu lệnh SQL với tất cả các cột cần cập nhật
        String sql = "UPDATE users SET full_name = ?, date_of_birth = ?, address = ?, " +
                "phone_number = ?, email = ?, username = ?, password_hash = ?, " +
                "membership_id = ?, join_date = ?, membership_status = ?, " +
                "total_books_borrowed = ?, total_books_returned = ?, role = ?, " +
                "expiry_date = ?, card_registration_date = ?, account_status = ?, " +
                "gender = ?, department = ?, class_name = ?, avatar = ? " +
                "WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Gán giá trị cho các tham số trong câu lệnh SQL
            pstmt.setString(1, user.getFullName());
            pstmt.setDate(2, user.getDateOfBirth() != null ? java.sql.Date.valueOf(user.getDateOfBirth()) : null);
            pstmt.setString(3, user.getAddress());
            pstmt.setString(4, user.getPhoneNumber());
            setString(pstmt,5, user.getEmail());
            pstmt.setString(6, user.getUsername());
            pstmt.setString(7, user.getPasswordHash());
            pstmt.setString(8, user.getMembershipId());
            pstmt.setDate(9, user.getJoinDate() != null ? java.sql.Date.valueOf(user.getJoinDate()) : null);
            pstmt.setString(10, user.getMembershipStatus());
            pstmt.setInt(11, user.getTotalBooksBorrowed() != null ? user.getTotalBooksBorrowed() : 0);
            pstmt.setInt(12, user.gettotalBooksReturned() != null ? user.gettotalBooksReturned() : 0);
            pstmt.setString(13, user.getRole());
            pstmt.setDate(14, user.getExpiryDate() != null ? java.sql.Date.valueOf(user.getExpiryDate()) : null);
            pstmt.setDate(15, user.getCardRegistrationDate() != null ? java.sql.Date.valueOf(user.getCardRegistrationDate()) : null);
            pstmt.setString(16, user.getAccountStatus());
            pstmt.setString(17, user.getGender());
            pstmt.setString(18, user.getDepartment());
            pstmt.setString(19, user.getClassName());
            pstmt.setBytes(20, user.getAvatars());
            pstmt.setString(21, user.getUserId()); // `userId` dùng để xác định người dùng cần cập nhật


            // Thực thi câu lệnh SQL
            int rowsUpdated = pstmt.executeUpdate();

            // Kiểm tra số dòng bị ảnh hưởng
            if (rowsUpdated > 0) {
                Noti.showSuccessMessage("Cập nhật thông tin người dùng thành công!");
                System.out.println("Cập nhật thông tin người dùng thành công!");
            } else {
                Noti.showFailureMessage("Không tìm thấy người dùng để cập nhật.");
                System.out.println("Không tìm thấy người dùng để cập nhật.");
            }

        } catch (SQLException e) {
            // In lỗi chi tiết
            Noti.showFailureMessage("Lỗi khi cập nhật thông tin người dùng: " + e.getMessage());
            System.err.println("Lỗi khi cập nhật thông tin người dùng: " + e.getMessage());
            e.printStackTrace();
        }
    }
    //xoá
    public void deleteUser(String userId) {
    String sql = "DELETE FROM users WHERE user_id = ?";

    try (Connection conn = DatabaseConnection.connectToLibrary();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, userId);
        pstmt.executeUpdate(); // Thực thi câu lệnh SQL

    } catch (SQLException e) {
        System.out.println("Lỗi xóa:" + e.getMessage());
    }
}
    // lay 1 gia tri
    public User output1Value(String id) {
        User user = new User();
        try (Connection connection = DatabaseConnection.connectToLibrary()) {
            String sql = "SELECT * FROM users WHERE user_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1,id);
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                user.setUserId(resultSet.getString("user_id"));
                user.setFullName(resultSet.getString("full_name"));
                user.setDateOfBirth(getDate(resultSet,"date_of_birth"));
                user.setAddress(resultSet.getString("address"));
                user.setPhoneNumber(resultSet.getString("phone_number"));
                user.setEmail(resultSet.getString("email"));
                user.setUsername(resultSet.getString("username"));
                user.setPasswordHash(resultSet.getString("password_hash"));
                user.setMembershipId(resultSet.getString("membership_id"));
                user.setJoinDate(getDate(resultSet,"join_date"));
                user.setMembershipStatus(resultSet.getString("membership_status"));
                user.setTotalBooksBorrowed(resultSet.getInt("total_books_borrowed"));
                user.settotalBooksReturned(resultSet.getInt("total_books_returned"));
                user.setRole(resultSet.getString("role"));
                user.setCardRegistrationDate(getDate(resultSet,"card_registration_date"));
                user.setExpiryDate(getDate(resultSet,"expiry_date"));
                user.setAccountStatus(resultSet.getString("account_status"));
                user.setGender(resultSet.getString("gender"));
                user.setDepartment(resultSet.getString("department"));
                user.setClassName(resultSet.getString("class_name"));
                try {
                    if (resultSet.getBytes("avatar")!=null)
                        user.setAvatar(new Image(new ByteArrayInputStream(resultSet.getBytes("avatar"))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi truy xuất 1 giá trị: " + e.getMessage());
        }
        return user;
    }

    public static String gender(String userId) {
        try (Connection connection = DatabaseConnection.connectToLibrary()) {
            String sql = "SELECT gender FROM users WHERE user_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, userId);
                ResultSet rs = statement.executeQuery();
                rs.next();
                return rs.getString(1);
            }
            } catch (SQLException e) {
            System.out.println("loi khi set gioi tinh: "+e.getMessage());
            return "other";
        }
    }

    public static Image getAvatar(String userId) {
        try (Connection connection = DatabaseConnection.connectToLibrary()) {
            String sql = "SELECT avatar FROM users WHERE user_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, userId);
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                try {
                    if (resultSet.getBytes("avatar")!=null)
                   return new Image(new ByteArrayInputStream(resultSet.getBytes("avatar")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
        if(gender(userId).compareTo("female")==0)
            return new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/female1.jpg");
        else
            return new Image("file:C:/Users/Dell/IdeaProjects/library/src/main/image/male0.jpg");
    }
    // tim nguoi dung
    public void findUsers(ObservableList<User> data, User user, List<String> arr) {

        shutdownCurrentTask();
        executorService = Executors.newSingleThreadExecutor();
        data.clear();
        arr.clear();
        if(!checkBorrow.get()&&!checkBorrow.get())
        try {
            Connection connection = DatabaseConnection.connectToLibrary();
            // Khởi tạo câu lệnh SQL cơ bản
            StringBuilder sql = new StringBuilder("SELECT * FROM users");

            // List để lưu trữ các tham số cho PreparedStatement
            List<Object> parameters = new ArrayList<>();

            // Kiểm tra và thêm các điều kiện vào câu lệnh SQL
            if (user.getUserId()!=null&&!user.getUserId().isEmpty()) {
                sql.append(" WHERE user_id = ?");
                parameters.add(user.getUserId());
            }
            if (user.getFullName()!=null&&!user.getFullName().isEmpty()) {
                // Nếu đã có điều kiện WHERE, thêm AND, ngược lại, thêm WHERE
                sql.append(sql.indexOf("WHERE") == -1 ? " WHERE" : " AND").append(" full_name = ?");
                parameters.add(user.getFullName());
            }
            if (dateOfBirth != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String formattedDate = dateOfBirth.format(formatter); // Chuyển sang yyyy-MM-dd
                sql.append(sql.indexOf("WHERE") == -1 ? " WHERE" : " AND").append(" date_of_birth = ?");
                parameters.add(formattedDate);
            }
            if (user.getMembershipStatus()!=null&&!user.getMembershipStatus().isEmpty()
                    &&user.getMembershipStatus().compareTo("all")!=0) {
                sql.append(sql.indexOf("WHERE") == -1 ? " WHERE" : " AND").append(" membership_status = ?");
                parameters.add(user.getMembershipStatus());
            }
            if (totalBooksBorred != null) {
                sql.append(sql.indexOf("WHERE") == -1 ? " WHERE" : " AND").append(" total_books_borrowed = ?");
                parameters.add(totalBooksBorred);
            }
            if (totalBooksReturned != null) {
                sql.append(sql.indexOf("WHERE") == -1 ? " WHERE" : " AND").append(" total_books_returned = ?");
                parameters.add(totalBooksReturned);
            }
            if (user.getAccountStatus()!=null&&!user.getAccountStatus().isEmpty()
                    &&user.getAccountStatus().compareTo("all")!=0) {
                sql.append(sql.indexOf("WHERE") == -1 ? " WHERE" : " AND").append(" account_status = ?");
                parameters.add(user.getAccountStatus());
            }

            // Nếu không có điều kiện nào, câu lệnh SQL sẽ chỉ là "SELECT * FROM users"
            if (parameters.isEmpty()) {
                sql = new StringBuilder("SELECT * FROM users");
            }
            PreparedStatement statement = connection.prepareStatement(sql.toString());

                // Gán các tham số vào PreparedStatement
                for (int i = 0; i < parameters.size(); i++)
                    statement.setObject(i + 1, parameters.get(i));


               ResultSet resultSet = statement.executeQuery();
                    currentTask = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            try {

                    while (resultSet.next()) {
                        User user = new User(
                                false,
                                resultSet.getString("user_id"),
                                resultSet.getString("full_name"),
                                getDate(resultSet, "date_of_birth"),
                                resultSet.getInt("total_books_borrowed"),
                                resultSet.getInt("total_books_returned"),
                                resultSet.getString("membership_status"),
                                resultSet.getString("account_status"),
                                "chi tiết"
                        );
                        Thread.sleep(10);
                        // Thêm user vào ObservableList trên luồng giao diện
                        javafx.application.Platform.runLater(() -> {data.add(user);});
                        user.selectedProperty().addListener((observable, oldValue, newValue) -> {
                            if (newValue) {
                                // Kiểm tra xem ID đã có trong arr chưa trước khi thêm vào
                                if (!arr.contains(user.getUserId())) {
                                    arr.add(user.getUserId());
                                }
                            } else {
                                arr.remove(user.getUserId());
                            }
                        });

                    }
                                // Sau khi hoàn tất việc tải, thêm Listener cho TableView

                            } finally {
                                // Đảm bảo đóng tài nguyên khi hoàn tất
                                try {
                                    if (resultSet != null) resultSet.close();
                                    if (statement != null) statement.close();
                                    if (connection != null) connection.close();
                                } catch (SQLException e) {
                                    System.out.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
                                }
                            }
                            return null;
                        }
                    };

                    // Xử lý khi Task thành công
                    currentTask.setOnSucceeded(event -> {
                        System.out.println("Hoàn thành tải dữ liệu người dùng!");
                        executorService.shutdown();
                    });

                    // Xử lý khi Task thất bại
                    currentTask.setOnFailed(event -> {
                        System.err.println("Có lỗi xảy ra: " + currentTask.getException().getMessage());
                        executorService.shutdown();
                    });

                    // Gửi Task vào ExecutorService
                    executorService.submit(currentTask);

                } catch (SQLException e) {
                System.out.println("Lỗi khi tìm người dùng: " + e.getMessage());
            }
    }
    public void findUsers(ObservableList<User> data, List<String> arr) {
        // Hủy task hiện tại nếu có
        shutdownCurrentTask();
        executorService = Executors.newSingleThreadExecutor();

        String sql = "SELECT * FROM users LIMIT 1000";
        arr.clear();
        data.clear();
        try {
            Connection connection = DatabaseConnection.connectToLibrary();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            currentTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try {

                        while (resultSet.next()) {
                            User user = new User(
                                    false,
                                    resultSet.getString("user_id"),
                                    resultSet.getString("full_name"),
                                    getDate(resultSet, "date_of_birth"),
                                    resultSet.getInt("total_books_borrowed"),
                                    resultSet.getInt("total_books_returned"),
                                    resultSet.getString("membership_status"),
                                    resultSet.getString("account_status"),
                                    "chi tiết"
                            );
                            Thread.sleep(10);
                            javafx.application.Platform.runLater(() -> data.add(user));

                            user.selectedProperty().addListener((observable, oldValue, newValue) -> {
                                if (newValue) {
                                    // Kiểm tra xem ID đã có trong arr chưa trước khi thêm vào
                                    if (!arr.contains(user.getUserId())) {
                                        arr.add(user.getUserId());
                                    }
                                } else {
                                    arr.remove(user.getUserId());
                                }
                            });
                        }

                        // Sau khi hoàn tất việc tải, thêm Listener cho TableView

                    } finally {
                        // Đảm bảo đóng tài nguyên khi hoàn tất
                        try {
                            if (resultSet != null) resultSet.close();
                            if (statement != null) statement.close();
                            if (connection != null) connection.close();
                        } catch (SQLException e) {
                            System.out.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
                        }
                    }
                    return null;
                }
            };

            // Xử lý khi Task thành công
            currentTask.setOnSucceeded(event -> {
                System.out.println("Hoàn thành tải dữ liệu người dùng!");
                executorService.shutdown();
            });

            // Xử lý khi Task thất bại
            currentTask.setOnFailed(event -> {
                System.err.println("Có lỗi xảy ra: " + currentTask.getException().getMessage());
                executorService.shutdown();
            });

            // Gửi Task vào ExecutorService
            executorService.submit(currentTask);

        } catch (SQLException e) {
            System.out.println("Lỗi khi tìm người dùng: " + e.getMessage());
        }
    }

    //kiểm tra acc có tồn tại ko
    public static boolean checkAccount(String username, String password) {
        try (Connection connection = DatabaseConnection.connectToLibrary()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password_hash = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);

                // Thực thi truy vấn và kiểm tra kết quả
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {  // Nếu có kết quả trả về, tài khoản tồn tại
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Dell\\IdeaProjects\\library\\src\\main\\text\\user_id.txt"))) {
                            writer.write(rs.getString("user_id"));
                        } catch (IOException e) {
                            System.out.println("Lỗi khi ghi vào file: " + e.getMessage());
                        }
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
        return false;  // Nếu không có kết quả, tài khoản không tồn tại
    }
    //họ tên
    public static String accountName(String user_id) {
        String sql = "SELECT full_name FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user_id);

            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("full_name");
                } else {
                    System.out.println("Không tìm thấy người dùng với user_id: " + user_id);
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi tìm tên người dùng: " + e.getMessage());
        }
        return ""; // Trả về chuỗi rỗng nếu không tìm thấy người dùng
    }
    // thêm luợt mượn
    public static void updateBorrowed(String user_id, int borrowed) {
    String sql = "UPDATE users SET total_books_borrowed = ? WHERE user_id = ?";

    try (Connection conn = DatabaseConnection.connectToLibrary();

         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1,borrowed);
        pstmt.setString(2, user_id); // Sử dụng userId để xác định người dùng cần sửa

        if(pstmt.executeUpdate()>0) // Thực thi câu lệnh SQL
        System.out.println("update luot mươn thanh công");
        else
        System.out.println("update luot mươn ko thanh công");
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("update luot mươn ko thanh công" + e.getMessage());
    }
}
    // thêm luojt trả
    public static void updateReturned(String user_id, int returned) {
    String sql = "UPDATE users SET total_books_returned = ? WHERE user_id = ?";

    try (Connection conn = DatabaseConnection.connectToLibrary();

         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1,returned);
        pstmt.setString(2, user_id); // Sử dụng userId để xác định người dùng cần sửa

        if(pstmt.executeUpdate()>0) // Thực thi câu lệnh SQL
            System.out.println("update luot trả thanh công");
        else
            System.out.println("update luot trả ko thanh công");
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("update luot trả ko thanh công" + e.getMessage());
    }
}
    //lấy chức vụ
    public static String getRole(String userId) {
        String sql = "SELECT role FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return rs.getString("role");

        } catch (SQLException e) {
            System.out.println("loi khi lay chuc vu" + e.getMessage());
            return "";
        }
    }

    public static ObservableList<TopUser> dataTopUser() {
        ObservableList<TopUser> data =  FXCollections.observableArrayList();

        String sql = "SELECT b.user_id, u.full_name, COUNT(b.user_id) AS count\n" +
                "FROM borrow b\n" +
                "JOIN users u ON b.user_id = u.user_id\n" +
                "GROUP BY b.user_id, u.full_name\n" +
                "ORDER BY count DESC\n" +
                "LIMIT 0, 100;";

        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                data.add(new TopUser(rs.getString("user_id"),
                        rs.getString("full_name"), rs.getInt("count")));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }

    // Hàm cập nhật thông tin của người dùng khi thẻ đã active
    public static boolean updateUserAttributes(String cardId) {
        // SQL để lấy thông tin từ bảng cards
        String selectSql = "SELECT card_id, user_id, registration_date, status, expiry_date FROM cards " +
                "WHERE card_id = ?";

        try (Connection connection = DatabaseConnection.connectToLibrary();
             PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {
            selectStatement.setString(1, cardId);

            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                // Lấy các giá trị từ ResultSet
                String userId = resultSet.getString("user_id");
                LocalDate cardRegistrationDate = getDate(resultSet,"registration_date");
                String membershipStatus = resultSet.getString("status");
                LocalDate expiryDate = getDate(resultSet,"expiry_date");

                // SQL để cập nhật thông tin người dùng
                String updateSql = "UPDATE users SET card_registration_date = ?, membership_status = ?," +
                        " expiry_date = ?, membership_id = ? WHERE user_id = ?";
                try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                    setDate(updateStatement, 1, cardRegistrationDate);
                    updateStatement.setString(2, membershipStatus);
                    setDate(updateStatement,3, expiryDate);
                    updateStatement.setString(4, cardId);
                    updateStatement.setString(5, userId);

                    int rowsAffected = updateStatement.executeUpdate();
                    return rowsAffected > 0;

                }

            } else {
                System.out.println("ko tìm thấy the: " + cardId);
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void requestFromUser(ObservableList<Card> data, Card card, List<String> a, String keyword) {

        shutdownCurrentTask();
        executorService = Executors.newSingleThreadExecutor();
        data.clear();
        a.clear();

        try {
            // Kết nối và chuẩn bị câu lệnh SQL
            Connection connection = DatabaseConnection.connectToLibrary();
            StringBuilder sql = new StringBuilder(
                    "SELECT cards.card_id, cards.user_id, cards.registration_date, cards.expiry_date, " +
                            "cards.card_type, cards.status, users.full_name " + // Lấy full_name từ bảng users
                            "FROM cards " +
                            "JOIN users ON cards.user_id = users.user_id " + // Nối bảng cards với users
                            "WHERE cards.status = 'Pending'" // Sử dụng 1=1 để dễ dàng nối điều kiện sau này
            );

            List<Object> parameters = new ArrayList<>();
            if(card!=null) {
                // Thêm điều kiện tìm kiếm theo card_id
                if (card.getUserId() != null && !card.getUserId().isEmpty()) {
                    sql.append(" AND cards.user_id LIKE ?");
                    parameters.add("%" + card.getUserId() + "%");
                }

                // Thêm điều kiện userId
                if (card.getFullName() != null && !card.getFullName().isEmpty()) {
                    sql.append(" AND users.full_name LIKE ?");
                    parameters.add("%" + card.getFullName() + "%");
                }

                // Thêm điều kiện cardType
                if (card.getCardRegistrationDate() != null) {
                    sql.append(" AND cards.registration_date LIKE ?");
                    parameters.add("%" + card.getCardRegistrationDate().toString() + "%");
                }

                // Thêm điều kiện status
                if (card.getExpiryDate() != null) {
                    sql.append(" AND cards.expiry_date LIKE ?");
                    parameters.add("%" + card.getExpiryDate().toString() + "%");
                }
            }
            // Thêm điều kiện tìm kiếm từ khóa
            if (keyword != null && !keyword.isEmpty()) {
                sql.append(" AND CONCAT_WS(' ', cards.expiry_date, cards.user_id, cards.card_type, cards.registration_date, users.full_name) LIKE ?");
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
                            Card cardResult = new Card(
                                    resultSet.getString("card_id"),
                                    resultSet.getString("user_id"),
                                    resultSet.getString("full_name"),
                                    resultSet.getString("status"),
                                    resultSet.getString("card_type"),
                                    getDate(resultSet, "registration_date"),
                                    getDate(resultSet, "expiry_date"),
                                   false
                                   // Lấy full_name từ bảng users
                            );
                        Platform.runLater(() -> {data.add(cardResult);
                        System.out.println(data.size());
                                });
                            Thread.sleep(10);

                            // Lắng nghe thay đổi sự lựa chọn trong TableView
                            Platform.runLater(() -> {
                                cardResult.selectedProperty().addListener((observable, oldValue, newValue) -> {
                                    if (newValue) {
                                        // Nếu được chọn, thêm userId vào danh sách
                                        a.add(cardResult.getCardId()); // Hoặc bạn có thể thay thế bằng cardResult.getCardId() tùy vào yêu cầu
                                    } else {
                                        // Nếu không chọn, loại bỏ userId khỏi danh sách
                                        a.remove(cardResult.getCardId());
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
                System.out.println("Hoàn thành tải dữ liệu thẻ người dùng!");
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

    public void requestFromUser2(ObservableList<Card> data, Card card, List<String> a, String keyword) {

        shutdownCurrentTask();
        executorService = Executors.newSingleThreadExecutor();
        data.clear();
        a.clear();

        try {
            // Kết nối và chuẩn bị câu lệnh SQL
            Connection connection = DatabaseConnection.connectToLibrary();
            StringBuilder sql = new StringBuilder(
                    "SELECT cards.card_id, cards.user_id, cards.registration_date, cards.expiry_date, " +
                            "cards.card_type, cards.status, users.full_name " + // Lấy full_name từ bảng users
                            "FROM cards " +
                            "JOIN users ON cards.user_id = users.user_id " + // Nối bảng cards với users
                            "WHERE cards.status = 'Pending Renewal'" // Sử dụng 1=1 để dễ dàng nối điều kiện sau này
            );

            List<Object> parameters = new ArrayList<>();
            if(card!=null) {
                // Thêm điều kiện tìm kiếm theo card_id
                if (card.getUserId() != null && !card.getUserId().isEmpty()) {
                    sql.append(" AND cards.user_id LIKE ?");
                    parameters.add("%" + card.getUserId() + "%");
                }

                // Thêm điều kiện userId
                if (card.getFullName() != null && !card.getFullName().isEmpty()) {
                    sql.append(" AND users.full_name LIKE ?");
                    parameters.add("%" + card.getFullName() + "%");
                }

                // Thêm điều kiện cardType
                if (card.getCardRegistrationDate() != null) {
                    sql.append(" AND cards.registration_date LIKE ?");
                    parameters.add("%" + card.getCardRegistrationDate().toString() + "%");
                }

                // Thêm điều kiện status
                if (card.getExpiryDate() != null) {
                    sql.append(" AND cards.expiry_date LIKE ?");
                    parameters.add("%" + card.getExpiryDate().toString() + "%");
                }
            }
            // Thêm điều kiện tìm kiếm từ khóa
            if (keyword != null && !keyword.isEmpty()) {
                sql.append(" AND CONCAT_WS(' ', cards.expiry_date, cards.user_id, cards.card_type, cards.registration_date, users.full_name) LIKE ?");
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
                            Card cardResult = new Card(
                                    resultSet.getString("card_id"),
                                    resultSet.getString("user_id"),
                                    resultSet.getString("full_name"),
                                    resultSet.getString("status"),
                                    resultSet.getString("card_type"),
                                    getDate(resultSet, "registration_date"),
                                    getDate(resultSet, "expiry_date"),
                                    false
                                    // Lấy full_name từ bảng users
                            );
                            Platform.runLater(() -> {data.add(cardResult);
                                System.out.println(data.size()+"bjhvy");
                            });
                            Thread.sleep(10);

                            // Lắng nghe thay đổi sự lựa chọn trong TableView
                            Platform.runLater(() -> {
                                cardResult.selectedProperty().addListener((observable, oldValue, newValue) -> {
                                    if (newValue) {
                                        // Nếu được chọn, thêm userId vào danh sách
                                        a.add(cardResult.getCardId()); // Hoặc bạn có thể thay thế bằng cardResult.getCardId() tùy vào yêu cầu
                                    } else {
                                        // Nếu không chọn, loại bỏ userId khỏi danh sách
                                        a.remove(cardResult.getCardId());
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
                System.out.println("Hoàn thành tải dữ liệu thẻ người dùng!");
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

    public void requestFromUserHelp(ObservableList<Help> data, Help help, List<Integer> a, String keyword) {

        shutdownCurrentTask();
        executorService = Executors.newSingleThreadExecutor();
        data.clear();
        a.clear();

        try {
            // Kết nối và chuẩn bị câu lệnh SQL
            Connection connection = DatabaseConnection.connectToLibrary();
            StringBuilder sql = new StringBuilder(
                    "select h.request_id,h.user_id, u.full_name, h.title, h.request_date from help_request h\n" +
                            "join users u on u.user_id = h.user_id\n" +
                            "WHERE 1=1"
            );

            List<Object> parameters = new ArrayList<>();
            if(help!=null) {
                // Thêm điều kiện tìm kiếm theo card_id
                if (help.getUserId() != null && !help.getUserId().isEmpty()) {
                    sql.append(" AND help.user_id LIKE ?");
                    parameters.add("%" + help.getUserId() + "%");
                }

                // Thêm điều kiện userId
                if (help.getFullName() != null && !help.getFullName().isEmpty()) {
                    sql.append(" AND users.full_name LIKE ?");
                    parameters.add("%" + help.getFullName() + "%");
                }
                // Thêmđiều kiện userId
                if (help.getTitle() != null && !help.getTitle().isEmpty()) {
                    sql.append(" AND help.title LIKE ?");
                    parameters.add("%" + help.getTitle() + "%");
                }
                // Thêm điều kiện cardType
                if (help.getRequestDate() != null) {
                    sql.append(" AND help.request_date LIKE ?");
                    parameters.add("%" + help.getRequestDate().toString() + "%");
                }

            }
            // Thêm điều kiện tìm kiếm từ khóa
            if (keyword != null && !keyword.isEmpty()) {
                sql.append(" AND CONCAT_WS(' ', h.user_id, u.full_name, h.title, h.request_date) LIKE ?");
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
                            Help helpResult = new Help(
                                    resultSet.getInt("request_id"),
                                    resultSet.getString("user_id"),
                                    resultSet.getString("full_name"),
                                    resultSet.getString("title"),
                                    getDate(resultSet, "request_date"),
                                    false
                                    // Lấy full_name từ bảng users
                            );
                            Platform.runLater(() -> {data.add(helpResult);
                            });
                            Thread.sleep(10);

                            // Lắng nghe thay đổi sự lựa chọn trong TableView
                            Platform.runLater(() -> {
                                helpResult.selectedProperty().addListener((observable, oldValue, newValue) -> {
                                    if (newValue) {
                                        // Nếu được chọn, thêm userId vào danh sách
                                        a.add(helpResult.getHelpId()); // Hoặc bạn có thể thay thế bằng cardResult.getCardId() tùy vào yêu cầu
                                    } else {
                                        // Nếu không chọn, loại bỏ userId khỏi danh sách
                                        a.remove(helpResult.getHelpId());
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
                System.out.println("Hoàn thành tải dữ liệu trợ giúp người dùng!");
                executorService.shutdown();
            });

            currentTask.setOnFailed(event -> {
                System.err.println("Có lỗi xảy ra: " + currentTask.getException().getMessage());
                executorService.shutdown();
            });

            // Gửi Task vào ExecutorService
            executorService.submit(currentTask);

        } catch (SQLException e) {
            System.err.println("Lỗi khi kết nối cơ sở dữ liệu trợ giúp: " + e.getMessage());
        }
    }

    public String contenHelp(int helpId) {
        String sql = "select content from help_request where request_id = ?";
        try(Connection connection = DatabaseConnection.connectToLibrary();
        PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, helpId);
        try(ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            return resultSet.getString("content");
        }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }
    public String getFullName(int helpId) {
        String sql = "select user_id from help_request where request_id = ?";
        try(Connection connection = DatabaseConnection.connectToLibrary();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            String userId;
            statement.setInt(1, helpId);
            try (ResultSet resultSet = statement.executeQuery()){
                resultSet.next();
                userId = resultSet.getString("user_id");
             }


            String sql2 = "select full_name from users where user_id = ?";
           try(PreparedStatement statement2 = connection.prepareStatement(sql2)) {

               statement2.setString(1, userId);
               try( ResultSet resultSet2 = statement2.executeQuery()) {
                   resultSet2.next();
                   return resultSet2.getString("full_name");
               }
           }
        }catch (SQLException e) {
            System.out.println("Lỗi lấy tên: "+e.getMessage());
        }
        return "";
    }
    public String getUserId(int helpId) {
        String sql = "select user_id from help_request where request_id = ?";
        try(Connection connection = DatabaseConnection.connectToLibrary();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, helpId);

            try(ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getString("user_id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }
    public static String getEmail(String userId) {
        String sql = "select email from users where user_id = ?";
        try(Connection connection = DatabaseConnection.connectToLibrary();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, userId);
            try (ResultSet resultSet = statement.executeQuery()){
                resultSet.next();
                return resultSet.getString("email");
            }
        }catch (SQLException e) {
            System.out.println("Lỗi email: "+e.getMessage());
        }
        return "";
    }
    public void deleteHelp(String userId) {
        String sql = "DELETE FROM help_request WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.executeUpdate(); // Thực thi câu lệnh SQL

        } catch (SQLException e) {
            System.out.println("Lỗi xóa:" + e.getMessage());
        }
    }
    //khóa tài khoản
    public void lockUser(String userId) {
        // Câu lệnh SQL để cập nhật thông tin sách
        String sql = "UPDATE users SET account_status = ? WHERE user_id = ?";

        // Kết nối cơ sở dữ liệu và thực thi câu lệnh
        try (Connection conn = DatabaseConnection.connectToLibrary(); // Sử dụng kết nối từ DatabaseConnection
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Gán giá trị cho các tham số từ đối tượng Book
            stmt.setString(1, "inactive");  // quantity
            stmt.setString(2, userId);  // id

            // Thực thi câu lệnh cập nhật
            int rowsAffected = stmt.executeUpdate();

            // Kiểm tra kết quả
            if (rowsAffected > 0) {
                System.out.println("Khóa tài khoản thành công!");
            } else {
                System.out.println("Không tìm thấy tài khoản với ID: " + userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi khóa tài khoản.");
        }
    }
    // mở khóa ttài khoản
    public void unlocUser(String userId) {
        // Câu lệnh SQL để cập nhật thông tin sách
        String sql = "UPDATE users SET account_status = ? WHERE user_id = ?";

        // Kết nối cơ sở dữ liệu và thực thi câu lệnh
        try (Connection conn = DatabaseConnection.connectToLibrary(); // Sử dụng kết nối từ DatabaseConnection
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Gán giá trị cho các tham số từ đối tượng Book
            stmt.setString(1, "active");  // quantity
            stmt.setString(2, userId);  // id

            // Thực thi câu lệnh cập nhật
            int rowsAffected = stmt.executeUpdate();

            // Kiểm tra kết quả
            if (rowsAffected > 0) {
                System.out.println("Mở tài khoản thành công!");
            } else {
                System.out.println("Không tìm thấy tài khoản với ID: " + userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi Mở tài khoản.");
        }
    }
    //khóa thẻ
    public void lockCard(String userId) {
        // Câu lệnh SQL để cập nhật thông tin sách
        String sql = "UPDATE users SET membership_status = ? WHERE user_id = ?";

        // Kết nối cơ sở dữ liệu và thực thi câu lệnh
        try (Connection conn = DatabaseConnection.connectToLibrary(); // Sử dụng kết nối từ DatabaseConnection
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Gán giá trị cho các tham số từ đối tượng Book
            stmt.setString(1, "locked");  // quantity
            stmt.setString(2, userId);  // id

            // Thực thi câu lệnh cập nhật
            int rowsAffected = stmt.executeUpdate();

            // Kiểm tra kết quả
            if (rowsAffected > 0) {
                System.out.println("Khóa tài khoản thành công!");
            } else {
                System.out.println("Không tìm thấy tài khoản với ID: " + userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi khóa tài khoản.");
        }
    }
    //Mở thẻ
    public void unlockCard(String userId) {
        // Câu lệnh SQL để cập nhật thông tin sách
        String sql = "UPDATE users SET membership_status = ? WHERE user_id = ?";

        // Kết nối cơ sở dữ liệu và thực thi câu lệnh
        try (Connection conn = DatabaseConnection.connectToLibrary(); // Sử dụng kết nối từ DatabaseConnection
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Gán giá trị cho các tham số từ đối tượng Book
            stmt.setString(1, "active");  // quantity
            stmt.setString(2, userId);  // id

            // Thực thi câu lệnh cập nhật
            int rowsAffected = stmt.executeUpdate();

            // Kiểm tra kết quả
            if (rowsAffected > 0) {
                System.out.println("Khóa tài khoản thành công!");
            } else {
                System.out.println("Không tìm thấy tài khoản với ID: " + userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi khóa tài khoản.");
        }
    }
    //kiểm tra trạng thái tài khoản
    public static boolean checkStatusAccount(String userId) {
        String sql = "SELECT account_status FROM users WHERE user_id = ?";

        // Kết nối cơ sở dữ liệu và thực thi câu lệnh
        try (Connection conn = DatabaseConnection.connectToLibrary(); // Sử dụng kết nối từ DatabaseConnection
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()) {
                String currentStatus = resultSet.getString("account_status");
                return currentStatus.compareTo("active")==0;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public AtomicBoolean isCheck() {return check;}

    public void setCheck(AtomicBoolean check) {this.check=check;}
    public AtomicBoolean getCheck() {return check;}

    public AtomicBoolean getCheckBorrow() {return checkBorrow;}
    public void setCheckBorrow(AtomicBoolean checkBorrow) {this.checkBorrow = checkBorrow;}

    public AtomicBoolean getCheckReturn() {return checkReturn;}
    public void setCheckReturn(AtomicBoolean checkReturn) {this.checkReturn = checkReturn;}

    public LocalDate getDateOfBirth() {return dateOfBirth;}
    public void setDateOfBirth(LocalDate dateOfBirth) {this.dateOfBirth = dateOfBirth;}

    public Integer getTotalBooksBorred() {return totalBooksBorred;}
    public void setTotalBooksBorred(Integer totalBooksBorred) {this.totalBooksBorred = totalBooksBorred;}

    public Integer getTotalBooksReturned() {return totalBooksReturned;}
    public void setTotalBooksReturned(Integer totalBooksReturned) {this.totalBooksReturned = totalBooksReturned;}

    public static LocalDate getDate(ResultSet resultSet, String columnName) throws SQLException {
        java.sql.Date sqlDate = resultSet.getDate(columnName);
        return resultSet.wasNull() ? null : sqlDate.toLocalDate();
    }
    public static void setDate(PreparedStatement pstmt, int parameterIndex, LocalDate date) throws SQLException {
        if (date == null) {
            pstmt.setNull(parameterIndex, java.sql.Types.DATE); // Đẩy lên SQL là null
        } else {
            pstmt.setDate(parameterIndex, Date.valueOf(date)); // Đẩy ngày lên SQL
        }
    }

    public static int totalUsers() {
        String sql = "select count(*) as total_users from users\n" +
                "where role != 'admin'";

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

    public static void setString(PreparedStatement pstmt, int parameterIndex, String string) throws SQLException {
        if (string==null||string.isEmpty()) {
            pstmt.setNull(parameterIndex, Types.DISTINCT); // Đẩy lên SQL là null
        } else {
                pstmt.setString(parameterIndex, string);
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

    public static boolean registerAccount(String username, String password, String email, String phone) {
        String sqlCheck = "SELECT COUNT(*) FROM users WHERE username = ?";
        String sqlInsert = "INSERT INTO users (user_id, username, password_hash, role, email, phone_number) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement checkStmt = conn.prepareStatement(sqlCheck)) {

            // Kiểm tra xem tên đăng nhập đã tồn tại chưa
            checkStmt.setString(1, username);
            ResultSet resultSet = checkStmt.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count > 0) {
                return false;
            }


            String passwordPrefix = password.length() >= 3 ? password.substring(0, 3) : password;
            int userId = getAsciiProduct(passwordPrefix);

            // Hash mật khẩu trước khi lưu
            String hashedPassword = hashPassword(password);

            // Nếu tên đăng nhập chưa tồn tại, tiếp tục thêm vào cơ sở dữ liệu
            try (PreparedStatement insertStmt = conn.prepareStatement(sqlInsert)) {
                insertStmt.setInt(1, userId);
                insertStmt.setString(2, username);
                insertStmt.setString(3, hashedPassword);
                insertStmt.setString(4, "member");
                insertStmt.setString(5, email);
                insertStmt.setString(6, phone);

                int rowsInserted = insertStmt.executeUpdate();
                return rowsInserted > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static int getAsciiProduct(String str) {
        int product = 1;
        for (int i = 0; i < str.length(); i++) {
            product *= (int) str.charAt(i);
        }
        return product;
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean checkForgotPassword(String username, String email, String phone) {
        String sqlCheck = "SELECT COUNT(*) FROM users WHERE username = ? AND email = ? AND phone_number = ?";

        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement checkStmt = conn.prepareStatement(sqlCheck)) {

            checkStmt.setString(1, username);
            checkStmt.setString(2, email);
            checkStmt.setString(3, phone);

            ResultSet resultSet = checkStmt.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean resetPassword(String username, String email, String phone, String newPassword) {
        if (!checkForgotPassword(username, email, phone)) {
            return false;
        }

        String sqlUpdate = "UPDATE users SET password_hash = ? WHERE username = ?";

        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement updateStmt = conn.prepareStatement(sqlUpdate)) {

            String hashedPassword = hashPassword(newPassword);
            updateStmt.setString(1, hashedPassword);
            updateStmt.setString(2, username);

            int rowsUpdated = updateStmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private static int getAsciiSum(String str) {
        int sum = 0;
        for (int i = 0; i < str.length(); i++) {
            sum += (int) str.charAt(i);
        }
        return sum;
    }

    public static boolean updatePassword(String username, String newPassword) {
        String sql = "UPDATE users SET password_hash = ? WHERE username = ?";
        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
