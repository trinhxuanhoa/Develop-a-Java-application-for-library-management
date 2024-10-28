package org.example;

import java.sql.*;
import java.time.LocalDate;
public class User {
    private int id;
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private String phoneNumber;
    private String email;
    private Timestamp registrationDate;
    private String status;
    private boolean isAdmin;

    public User() {
        this.registrationDate = new Timestamp(System.currentTimeMillis()); // Thời gian hiện tại
        this.status = "Active"; // Mặc định là Active
        this.isAdmin = false;   // Mặc định không phải admin
    }

    public User(int id, String fullName, LocalDate dateOfBirth, String gender,
                String address, String phoneNumber, String email,
                Timestamp registrationDate, String status, boolean isAdmin) {
        this.id = id;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.registrationDate = registrationDate;
        this.status = status;
        this.isAdmin = isAdmin;
    }

    public void addUser() {
        try (Connection connection = DatabaseConnection.connectToLibrary()) {
            String sql = "INSERT INTO users (full_name, date_of_birth, gender, address, phone_number, email, status, í_admin)" +
                    "VALUES  " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, fullName);
                statement.setDate(2,Date.valueOf(dateOfBirth));
                statement.setString(3, gender);
                statement.setString(4, address);
                statement.setString(5, phoneNumber);
                statement.setString(6, email);
                statement.setString(7,status);
                statement.setBoolean(8,isAdmin);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("người dùng đã được thêm thành công!");
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm người dùng: " + e.getMessage());
        }
    }

    public void removeUser(int id) {
        try (Connection connection = DatabaseConnection.connectToLibrary()) {
            String sql = "DELETE FROM users WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Người dùng đã đã được xóa thành công!");
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa người dùng: " + e.getMessage());
        }
    }

    /*public void updateUser(int id,String type, String data) {
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
    */
    // Getters và Setters cho từng thuộc tính
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Timestamp registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    // Phương thức toString() để in thông tin người dùng
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", registrationDate=" + registrationDate +
                ", status='" + status + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}