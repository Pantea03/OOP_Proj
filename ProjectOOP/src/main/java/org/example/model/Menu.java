package org.example.model;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.menuController.MainMenuController;
public class Menu {
    String name;

    public void setName(String name) {
        this.name = name;
    }

    public static boolean userAlreadyExists(String username, Connection conn) throws SQLException {
        User user = User.fetchByUsername(DatabaseUtil.getConnection(), username);
        return user != null;
    }

    public static boolean passwordMatchesUsername(String username, String password, Connection conn) throws SQLException {
        User user = User.fetchByUsername(DatabaseUtil.getConnection(), username);
        return user != null && user.getPassword().equals(password);
    }
    // Method to check if any user other than the specified username is logged in
    public static boolean isAUserLoggedIn(String username, Connection connection) {
        String sql = "SELECT COUNT(*) AS count FROM USER WHERE username != ? AND logged_in = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setBoolean(2, true);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public Menu(String name) {
        this.name = name;
    }
}
