package org.example.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.example.menuController.MainMenuController;
public class DatabaseUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/project";
    private static final String USER = "root";
    private static final String PASSWORD = "lastgreatdynasty03";


    // Get a connection to the database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Close the connection (optional utility method)
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static int createUser(Connection conn, User user) throws SQLException {
        if (User.userExists(user.getUsername(), conn)) {
            return 0; // User already exists
        }

        String query = "INSERT INTO user (username, password, nickname, security_question, security_answer, email) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getNickname());
            pstmt.setString(4, user.getSecurityQuestion());
            pstmt.setString(5, user.getSecurityAnswer());
            pstmt.setString(6, user.getEmail());
            return pstmt.executeUpdate();
        }
    }
}
