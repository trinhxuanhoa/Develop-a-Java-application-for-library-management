package org.example;
import javafx.scene.image.Image;
import java.io.*;
import java.io.ByteArrayInputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicBoolean;
public class UserDAO {
    private boolean Available;
    private Connection connection;
    private PreparedStatement statement;
    private AtomicBoolean check;
    public UserDAO() {
        check = new AtomicBoolean(false);
    }

    //thêm
    public void addUser(User user) {
        String sql = "INSERT INTO users (user_id, full_name, date_of_birth, address, " +
                "phone_number, email, username, password_hash, membership_id, " +
                "join_date, membership_status, total_books_borrowed, overdue_count, role, " +
                "card_registration_date, expiry_date, account_status, avatar, gender, department, class)" +
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
            pstmt.setInt(13, user.getOverdueCount());
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
                Noti.showSuccessMessage("Người đã được thêm thành công");
                System.out.println("Người dùng đã được thêm thành công!");
            } else {
                Noti.showFailureMessage("Lỗi: không thể thêm Người dùng");
            }
        } catch (SQLException e) {
            Noti.showFailureMessage("Lỗi sql: " + e.getMessage());
            e.printStackTrace();
        }
    }
    //sửa
    public void updateUser(User user) {
        String sql = "UPDATE users SET full_name = ?, date_of_birth = ?, address = ?, " +
                "phone_number = ?, email = ?, username = ?, password_hash = ?, " +
                "membership_id = ?, join_date = ?, membership_status = ?, " +
                "total_books_borrowed = ?, overdue_count = ?, role = ? " +
                "WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getFullName());
            pstmt.setDate(2, java.sql.Date.valueOf(user.getDateOfBirth()));
            pstmt.setString(3, user.getAddress());
            pstmt.setString(4, user.getPhoneNumber());
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getUsername());
            pstmt.setString(7, user.getPasswordHash());
            pstmt.setString(8, user.getMembershipId());
            pstmt.setDate(9, java.sql.Date.valueOf(user.getJoinDate()));
            pstmt.setString(10, user.getMembershipStatus());
            pstmt.setInt(11, user.getTotalBooksBorrowed());
            pstmt.setInt(12, user.getOverdueCount());
            pstmt.setString(13, user.getRole());
            pstmt.setString(14, user.getUserId()); // Sử dụng userId để xác định người dùng cần sửa

            pstmt.executeUpdate(); // Thực thi câu lệnh SQL

        } catch (SQLException e) {
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
        e.printStackTrace();
    }
}

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
                user.setOverdueCount(resultSet.getInt("overdue_count"));
                user.setRole(resultSet.getString("role"));
                user.setCardRegistrationDate(getDate(resultSet,"card_registration_date"));
                user.setExpiryDate(getDate(resultSet,"expiry_date"));
                user.setAccountStatus(resultSet.getString("account_status"));
                user.setGender(resultSet.getString("gender"));
                user.setDepartment(resultSet.getString("department"));
                user.setClassName(resultSet.getString("class"));
                try {
                    if (resultSet.getBytes("avatar")!=null)
                        user.setAvatar(new Image(new ByteArrayInputStream(resultSet.getBytes("qr_code"))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
        return user;
    }

    public ResultSet findUsers(User user) {

        boolean checkAnd = false;
        if(check.get()==false)
        try  {
            connection = DatabaseConnection.connectToLibrary();
            String sql = "SELECT * FROM users WHERE ";
            if (!user.getUserId().isEmpty()) {
                String sqlId = "user_id = '" + user.getUserId() + "'";
                sql += sqlId;
                checkAnd = true;
                System.out.println("hoa1");
            }
            if (!user.getFullName().isEmpty()) {
                if (checkAnd)
                    sql += " AND ";
                String sqlTitle = "full_name = '" + user.getFullName() + "'";
                sql += sqlTitle;
                checkAnd = true;
                System.out.println("hoa2");
            }
            if (user.getJoinDate()!=null) {
                if (checkAnd)
                    sql += " AND ";
                String sqlAuthor = "join_date = '" + user.getJoinDate() + "'";
                sql += sqlAuthor;
                checkAnd = true;
                System.out.println("hoa3");
            }
            if (!user.getMembershipStatus().isEmpty()) {
                if (checkAnd)
                    sql += " AND ";
                String sqlPublisher = "membership_status = '" + user.getMembershipStatus() + "'";
                sql += sqlPublisher;
                checkAnd = true;
                System.out.println("hoa4");
            }
            if (user.getTotalBooksBorrowed()!=null) {
                if (checkAnd)
                    sql += " AND ";
                String sqlYear = "total_books_borrowed = " + user.getTotalBooksBorrowed();
                sql += sqlYear;
                checkAnd = true;
                System.out.println("hoa5");
            }
            if (user.getOverdueCount()!=null) {
                if (checkAnd)
                    sql += " AND ";
                String sqlGenre = "overdue_count = " + user.getOverdueCount();
                sql += sqlGenre;
                checkAnd = true;
                System.out.println("hoa6");
            }
            if (!user.getRole().isEmpty()) {
                if (checkAnd)
                    sql += " AND ";
                String sqlQuantity = "role = '" + user.getRole() + "'";
                sql += sqlQuantity;
                checkAnd = true;
                System.out.println("hoa7");
            }
            if (!checkAnd) {
                sql = "SELECT * FROM users";
            }
            statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            System.out.println("Lỗi khi tìm người dùng: " + e.getMessage());
        }
        return null;
    }

    public static boolean checkAccount(String username, String password) {
        try (Connection connection = DatabaseConnection.connectToLibrary()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password_hash = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);

                // Thực thi truy vấn và kiểm tra kết quả
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {  // Nếu có kết quả trả về, tài khoản tồn tại
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Dell\\IdeaProjects\\library\\src\\main\\user_id.txt"))) {
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


    public AtomicBoolean isCheck() {return check;}
    public void setCheck(AtomicBoolean check) {this.check=check;}

    public LocalDate getDate(ResultSet resultSet, String columnName) throws SQLException {
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

    public static void setString(PreparedStatement pstmt, int parameterIndex, String string) throws SQLException {
        if (string.isEmpty()) {
            pstmt.setNull(parameterIndex, Types.DISTINCT); // Đẩy lên SQL là null
        } else {
                pstmt.setString(parameterIndex, string); // Đẩy ngày lên SQL
        }
    }

    public void closeDatabase() {
        try {
            connection.close();
            statement.close();
        } catch (SQLException e) {

        }
    }

}
