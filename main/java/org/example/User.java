package org.example;

import java.time.LocalDate;
import javafx.beans.property.*;

public class User {
    private StringProperty userId;
    private StringProperty fullName;
    private ObjectProperty<LocalDate> dateOfBirth;
    private StringProperty address;
    private StringProperty phoneNumber;
    private StringProperty email;
    private StringProperty username;
    private StringProperty passwordHash;
    private StringProperty membershipId; // -- Mã định danh của thẻ thư viện, duy nhất cho mỗi người dùng
    private ObjectProperty<LocalDate> joinDate;
    private StringProperty membershipStatus; // -- Trạng thái thẻ thư viện: có thể là 'active' (hoạt động), 'expired' (hết hạn), hoặc 'locked' (bị khóa). Giá trị mặc định là 'active'
    private IntegerProperty totalBooksBorrowed;
    private IntegerProperty overdueCount; //-- Số lần người dùng đã trả sách trễ hạn, kiểu số nguyên với giá trị mặc định là 0
    private StringProperty role; //-- Vai trò của người dùng, có thể là 'member' (thành viên), 'librarian' (thủ thư), hoặc 'admin' (quản trị viên). Giá trị mặc định là 'member'

    // Constructor
    public User(String userId, String fullName, LocalDate dateOfBirth, String address,
                String phoneNumber, String email, String username, String passwordHash,
                String membershipId, LocalDate joinDate, String membershipStatus,
                int totalBooksBorrowed, int overdueCount, String role) {

        this.userId = new SimpleStringProperty(userId);
        this.fullName = new SimpleStringProperty(fullName);
        this.dateOfBirth = new SimpleObjectProperty<>(dateOfBirth);
        this.address = new SimpleStringProperty(address);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.email = new SimpleStringProperty(email);
        this.username = new SimpleStringProperty(username);
        this.passwordHash = new SimpleStringProperty(passwordHash);
        this.membershipId = new SimpleStringProperty(membershipId);
        this.joinDate = new SimpleObjectProperty<>(joinDate);
        this.membershipStatus = new SimpleStringProperty(membershipStatus);
        this.totalBooksBorrowed = new SimpleIntegerProperty(totalBooksBorrowed);
        this.overdueCount = new SimpleIntegerProperty(overdueCount);
        this.role = new SimpleStringProperty(role);
    }
    // Getters and Setters with JavaFX Property bindings
    public String getUserId() {return userId.get();}
    public void setUserId(String userId) {this.userId.set(userId);}

    public StringProperty userIdProperty() {return userId;}
    public String getFullName() {return fullName.get();}

    public void setFullName(String fullName) {this.fullName.set(fullName);}

    public StringProperty fullNameProperty() {return fullName;}
    public LocalDate getDateOfBirth() {return dateOfBirth.get();}

    public void setDateOfBirth(LocalDate dateOfBirth) {this.dateOfBirth.set(dateOfBirth);}
    public ObjectProperty<LocalDate> dateOfBirthProperty() {return dateOfBirth;}

    public String getAddress() {return address.get();}
    public void setAddress(String address) {this.address.set(address);}

    public StringProperty addressProperty() {return address;}
    public String getPhoneNumber() {return phoneNumber.get();}

    public void setPhoneNumber(String phoneNumber) {this.phoneNumber.set(phoneNumber);}
    public StringProperty phoneNumberProperty() {return phoneNumber;}

    public String getEmail() {return email.get();}
    public void setEmail(String email) {this.email.set(email);}

    public StringProperty emailProperty() {return email;}
    public String getUsername() {return username.get();}

    public void setUsername(String username) {this.username.set(username);}
    public StringProperty usernameProperty() {return username;}

    public String getPasswordHash() {return passwordHash.get();}
    public void setPasswordHash(String passwordHash) {this.passwordHash.set(passwordHash);}

    public StringProperty passwordHashProperty() {return passwordHash;}
    public String getMembershipId() {return membershipId.get();}

    public void setMembershipId(String membershipId) {this.membershipId.set(membershipId);}
    public StringProperty membershipIdProperty() {return membershipId;}

    public LocalDate getJoinDate() {return joinDate.get();}

    public void setJoinDate(LocalDate joinDate) {this.joinDate.set(joinDate);}
    public ObjectProperty<LocalDate> joinDateProperty() {return joinDate;}

    public String getMembershipStatus() {return membershipStatus.get();}
    public void setMembershipStatus(String membershipStatus) {this.membershipStatus.set(membershipStatus);}

    public StringProperty membershipStatusProperty() {return membershipStatus;}
    public int getTotalBooksBorrowed() {return totalBooksBorrowed.get();}

    public void setTotalBooksBorrowed(int totalBooksBorrowed) {this.totalBooksBorrowed.set(totalBooksBorrowed);}
    public IntegerProperty totalBooksBorrowedProperty() {return totalBooksBorrowed;}

    public int getOverdueCount() {return overdueCount.get();}
    public void setOverdueCount(int overdueCount) {this.overdueCount.set(overdueCount);}

    public IntegerProperty overdueCountProperty() {return overdueCount;}
    public String getRole() {return role.get();}

    public void setRole(String role) {this.role.set(role);}
    public StringProperty roleProperty() {return role;}
}
