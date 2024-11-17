package org.example;

import java.time.LocalDate;
import javafx.beans.property.*;
import javafx.scene.image.Image;

public class User {
    private StringProperty userId;
    private StringProperty fullName;
    private ObjectProperty<LocalDate> dateOfBirth;
    private StringProperty address;
    private StringProperty phoneNumber;
    private StringProperty email;
    private IntegerProperty totalBooksBorrowed;
    private StringProperty username;
    private StringProperty passwordHash;
    private StringProperty membershipId; // -- Mã định danh của thẻ thư viện, duy nhất cho mỗi người dùng
    private ObjectProperty<LocalDate> joinDate;
    private StringProperty membershipStatus; // -- Trạng thái thẻ thư viện: có thể là 'active' (hoạt động), 'expired' (hết hạn), hoặc 'locked' (bị khóa). Giá trị mặc định là 'active'
    private IntegerProperty overdueCount; //-- Số lần người dùng đã trả sách trễ hạn, kiểu số nguyên với giá trị mặc định là 0
    private StringProperty role; //-- Vai trò của người dùng, có thể là 'member' (thành viên), 'librarian' (thủ thư), hoặc 'admin' (quản trị viên). Giá trị mặc định là 'member'
    private ObjectProperty<LocalDate> cardRegistrationDate;
    private ObjectProperty<LocalDate> expiryDate;
    private StringProperty accountStatus;
    private StringProperty gender;
    private StringProperty department;
    private StringProperty className;
    private byte[] avatars;
    private Image avatar;
    private SimpleStringProperty detail;
    private BooleanProperty selected;

    // Constructor
    public  User() {
        this.userId = new SimpleStringProperty();
        this.fullName = new SimpleStringProperty();
        this.dateOfBirth = new SimpleObjectProperty<>();
        this.address = new SimpleStringProperty();
        this.phoneNumber = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.username = new SimpleStringProperty();
        this.passwordHash = new SimpleStringProperty();
        this.membershipId = new SimpleStringProperty();
        this.joinDate = new SimpleObjectProperty<>();
        this.membershipStatus = new SimpleStringProperty();
        this.accountStatus = new SimpleStringProperty();
        this.totalBooksBorrowed = new SimpleIntegerProperty();
        this.overdueCount = new SimpleIntegerProperty();
        this.role = new SimpleStringProperty();
        this.gender = new SimpleStringProperty();
        this.department = new SimpleStringProperty();
        this.className = new SimpleStringProperty();
        this.cardRegistrationDate = new SimpleObjectProperty<>();
        this.expiryDate = new SimpleObjectProperty<>();
    }
    public  User(String userId, String fullName, LocalDate dateOfBirth, String membershipStatus,
                 String phoneNumber, String email, Integer totalBooksBorrowed) {
        this.userId = new SimpleStringProperty(userId);
        this.fullName = new SimpleStringProperty(fullName);
        this.dateOfBirth = new SimpleObjectProperty<>(dateOfBirth);
        this.membershipStatus = new SimpleStringProperty(membershipStatus);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.email = new SimpleStringProperty(email);
        this.totalBooksBorrowed = new SimpleIntegerProperty(totalBooksBorrowed);
    }
    public  User(boolean selected,String userId, String fullName, String phoneNumber, String email,
                 LocalDate dateOfBirth,  Integer totalBooksBorrowed, String membershipStatus, String detail) {
        this.selected = new SimpleBooleanProperty(selected);
        this.userId = new SimpleStringProperty(userId);
        this.fullName = new SimpleStringProperty(fullName);
        this.dateOfBirth = new SimpleObjectProperty<>(dateOfBirth);
        this.membershipStatus = new SimpleStringProperty(membershipStatus);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.email = new SimpleStringProperty(email);
        this.totalBooksBorrowed = new SimpleIntegerProperty(totalBooksBorrowed);
        this.detail = new SimpleStringProperty(detail);
    }
    public User(boolean selected, String userId, String fullName,LocalDate joinDate, String membershipStatus,
                Integer totalBooksBorrowed, Integer overdueCount, String role, String detail) {
        this.selected = new SimpleBooleanProperty(selected);
        this.userId = new SimpleStringProperty(userId);
        this.fullName = new SimpleStringProperty(fullName);
        this.joinDate = new SimpleObjectProperty<>(joinDate);
        this.membershipStatus = new SimpleStringProperty(membershipStatus);
        this.totalBooksBorrowed =new SimpleIntegerProperty(totalBooksBorrowed);
        this.overdueCount = new SimpleIntegerProperty(overdueCount);
        this.role = new SimpleStringProperty(role);
        this.detail = new SimpleStringProperty(detail);
    }
    public  User(String userId, String fullName,LocalDate joinDate, String membershipStatus,
                 String role, Integer totalBooksBorrowed, Integer overdueCount) {
        this.userId = new SimpleStringProperty(userId);
        this.fullName = new SimpleStringProperty(fullName);
        this.joinDate = new SimpleObjectProperty<>(joinDate);
        this.membershipStatus = new SimpleStringProperty(membershipStatus);
        this.totalBooksBorrowed = (totalBooksBorrowed!=null) ? new SimpleIntegerProperty(totalBooksBorrowed):null;
        this.overdueCount = (overdueCount!=null) ? new SimpleIntegerProperty(overdueCount):null;
        this.role = new SimpleStringProperty(role);
    }
    public User(String userId, String fullName, LocalDate dateOfBirth, String address,
                String phoneNumber, String email, String username, String passwordHash,
                String membershipId, LocalDate joinDate, String membershipStatus,
                Integer totalBooksBorrowed, Integer overdueCount, String role) {

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
    public User(String userId, String fullName, LocalDate dateOfBirth, String address, String phoneNumber,
                String email, String username, String passwordHash, String membershipId,
                LocalDate joinDate, String membershipStatus, String role, LocalDate cardRegistrationDate,
                LocalDate expiryDate, String accountStatus, String gender, String department,
                String className, byte[] avatars) {

        this.userId = new SimpleStringProperty(userId);
        this.fullName = new SimpleStringProperty(fullName);
        this.dateOfBirth = new SimpleObjectProperty<>(dateOfBirth);
        this.address = new SimpleStringProperty(address);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.email = new SimpleStringProperty(email);
        this.totalBooksBorrowed = new SimpleIntegerProperty(0);  // Default value
        this.username = new SimpleStringProperty(username);
        this.passwordHash = new SimpleStringProperty(passwordHash);
        this.membershipId = new SimpleStringProperty(membershipId);
        this.joinDate = new SimpleObjectProperty<>(joinDate);
        this.membershipStatus = new SimpleStringProperty(membershipStatus);
        this.overdueCount = new SimpleIntegerProperty(0);
        this.role = new SimpleStringProperty(role);
        this.cardRegistrationDate = new SimpleObjectProperty<>(cardRegistrationDate);  // Default value
        this.expiryDate = new SimpleObjectProperty<>(expiryDate);
        this.accountStatus = new SimpleStringProperty(accountStatus);
        this.gender = new SimpleStringProperty(gender);
        this.department = new SimpleStringProperty(department);
        this.className = new SimpleStringProperty(className);
        this.avatars = avatars;
    }
    // Getters and Setters with JavaFX Property bindings

    public String getUserId() {return userId.get();}
    public void setUserId(String userId) {this.userId.set(userId);}
    public StringProperty userIdProperty() {return userId;}

    public String getFullName() {return fullName.get();}
    public void setFullName(String fullName) {this.fullName.set(fullName);}
    public StringProperty fullNameProperty() {return fullName;}

    public LocalDate getDateOfBirth() {return (dateOfBirth!=null) ? dateOfBirth.get() : null;}
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

    public LocalDate getJoinDate() {return (joinDate!=null) ? joinDate.get() : null;}
    public void setJoinDate(LocalDate joinDate) {this.joinDate.set(joinDate);}
    public ObjectProperty<LocalDate> joinDateProperty() {return joinDate;}

    public String getMembershipStatus() {return membershipStatus.get();}
    public void setMembershipStatus(String membershipStatus) {this.membershipStatus.set(membershipStatus);}
    public StringProperty membershipStatusProperty() {return membershipStatus;}

    public Integer getTotalBooksBorrowed() {return (totalBooksBorrowed!=null)?totalBooksBorrowed.get():null;}
    public void setTotalBooksBorrowed(Integer totalBooksBorrowed) {this.totalBooksBorrowed.set(totalBooksBorrowed);}
    public IntegerProperty totalBooksBorrowedProperty() {return totalBooksBorrowed;}

    public Integer getOverdueCount() {return (overdueCount!=null)?overdueCount.get():null;}
    public void setOverdueCount(Integer overdueCount) {this.overdueCount.set(overdueCount);}
    public IntegerProperty overdueCountProperty() {return overdueCount;}

    public String getRole() {return role.get();}
    public void setRole(String role) {this.role.set(role);}
    public StringProperty roleProperty() {return role;}

    public LocalDate getExpiryDate() {return (expiryDate!=null) ? expiryDate.get() : null;}
    public ObjectProperty<LocalDate> expiryDateProperty() {return expiryDate;}
    public void setExpiryDate(LocalDate expiryDate) {this.expiryDate.set(expiryDate);}

    public String getAccountStatus() {return accountStatus.get();}
    public StringProperty accountStatusProperty() {return accountStatus;}
    public void setAccountStatus(String accountStatus) {this.accountStatus.set(accountStatus);}

    public LocalDate getCardRegistrationDate() {return (cardRegistrationDate!=null) ? cardRegistrationDate.get() : null;}
    public ObjectProperty<LocalDate> cardRegistrationDateProperty() { return cardRegistrationDate;}
    public void setCardRegistrationDate(LocalDate cardRegistrationDate) {this.cardRegistrationDate.set(cardRegistrationDate);}

    public String getGender() {return gender.get();}
    public StringProperty genderProperty() {return gender;}
    public void setGender(String gender) {this.gender.set(gender);}

    public String getDepartment() {return department.get();}
    public StringProperty departmentProperty() {return department;}
    public void setDepartment(String department) {this.department.set(department);}

    public String getClassName() {return className.get();}
    public StringProperty classNameProperty() {return className;}
    public void setClassName(String className) {this.className.set(className);}

    public byte[] getAvatars() {return avatars;}
    public void setAvatars(byte[] avatars) {this.avatars = avatars;}

    public Image getAvatar() {return avatar;}
    public void setAvatar(Image avatar) {this.avatar = avatar;}

    public String getDetail() {return detail.get();}
    public SimpleStringProperty detailProperty() {return detail;}
    public void setDetail(String detail) {this.detail.set(detail);}

    public boolean isSelected() {return selected.get();}
    public void setSelected(boolean selected) {this.selected.set(selected);}
    public BooleanProperty selectedProperty() {return selected;}
}
