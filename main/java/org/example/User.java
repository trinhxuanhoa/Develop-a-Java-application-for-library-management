package org.example;
import java.sql.*;
import java.util.*;
public class User {
    private String userId;
    private String name;
    private ArrayList<String> borrowedDocuments;

    public User(String userId, String name) {
        this.userId = userId;
        this.name = name;
        this.borrowedDocuments = new ArrayList<>();
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }

    public void borrowDocument(String docId) {
        borrowedDocuments.add(docId);
    }

    public void returnDocument(String docId) {
        borrowedDocuments.remove(docId);
    }

    public void displayInfo() {
        System.out.printf("User: %s, Borrowed: %s%n", name, borrowedDocuments);
    }
}
