package org.example;

public class Person {
    private String id;
    private String name;
    private String gender;
    private String address;
    private String numberPhone;
    private String email;
    private String userName;
    private String passWord;
    private String power;

    public Person() {}

    public Person(String id, String name, String address, String numberPhone,
                  String email, String userName, String passWord, String power) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.numberPhone = numberPhone;
        this.email = email;
        this.userName = userName;
        this.passWord = passWord;
        this.power =  power;
    }

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public void setAddress(String address) {this.address = address;}
    public String getAddress() {return address;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public void setNumberPhone(String numberPhone) {this.numberPhone = numberPhone;}
    public String getNumberPhone() {return numberPhone;}
    public void setEmail(String email) {this.email = email;}
    public String getEmail() {return email;}
    public void setPassWord(String passWord) {this.passWord = passWord;}
    public String getPassWord() {return passWord;}
    public void setUserName(String userName) {this.userName = userName;}
    public String getUserName() {return userName;}
    public String getPower() {return power;}
    public void setPower(String power) {this.power = power;}
}
