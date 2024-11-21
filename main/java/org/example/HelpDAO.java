package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HelpDAO {

    // Method to get email of a user by userId
    public static String getEmail(String userId) {
        try (Connection connection = DatabaseConnection.connectToLibrary()) {
            String sql = "SELECT email FROM users WHERE user_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, userId);

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getString("email");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error when querying email: " + e.getMessage());
        }
        return "";
    }

    // Method to fetch help topics from the database for a specific role
    public static List<HelpTopic> getHelpTopics(String role) {
        List<HelpTopic> helpTopics = new ArrayList<>();
        try (Connection connection = DatabaseConnection.connectToLibrary()) {
            String sql = "SELECT question, answer FROM help_topics WHERE role = ? OR role = 'all'";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, role);

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String question = resultSet.getString("question");
                    String answer = resultSet.getString("answer");
                    helpTopics.add(new HelpTopic(question, answer));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error when fetching help topics: " + e.getMessage());
        }
        return helpTopics;
    }

    // Method to add a help request to the database
    public static boolean addHelpRequest(String userId, String title, String content) {
        String sql = "INSERT INTO help_request (user_id, title, content) VALUES (?, ?, ?)";
        
        try (Connection connection = DatabaseConnection.connectToLibrary();
             PreparedStatement statement = connection.prepareStatement(sql)) {
             
            statement.setString(1, userId);
            statement.setString(2, title);
            statement.setString(3, content);
            
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Yêu cầu trợ giúp mới đã được thêm thành công!");
                return true;
            } else {
                System.out.println("Không thể thêm yêu cầu trợ giúp.");
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi thêm yêu cầu trợ giúp: " + e.getMessage());
            // Có thể sử dụng logging framework như Log4j để quản lý log tốt hơn
            return false;
        }
    }
}
