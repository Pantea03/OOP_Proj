package org.example.model;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;
import org.example.menuController.MainMenuController;
public class ProfileMenu extends Menu {
    String name = "profile";
    public ProfileMenu(String name) {
        super(name);
    }

    public static User showInformation(Connection conn, String username) throws SQLException {
        User user = User.fetchByUsername(conn, username);
        if (user != null) {
            return user;
        } else {
            return null;
        }
    }

    public static int canChangeUsername(Connection conn, String oldUsername, String newUsername) throws SQLException {
        User user = User.fetchByUsername(conn, oldUsername);
        if (user != null) {
            if (!newUsername.equals(oldUsername)) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 2;
        }
    }
    public static void changeUsername(Connection conn, User currentUser, String newUsername) throws SQLException {
        String sql = "UPDATE USER SET username = ? WHERE username = ?";
        if (!newUsername.equals(currentUser.getUsername())) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, newUsername);
                stmt.setString(2, currentUser.getUsername());
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    currentUser.setUsername(newUsername);
                }
            }
        }
    }
    public static int canChangeNickname(Connection conn, String username, String newNickname) throws SQLException {
        User user = User.fetchByUsername(conn, username);
        if (user != null) {
            if (!newNickname.equals(user.getNickname())) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 2;
        }
    }
    public static void changeNickname(Connection conn, String username, String newNickname) throws SQLException {
        String sql = "UPDATE USER SET nickname = ? WHERE username = ?";
        User user = User.fetchByUsername(DatabaseUtil.getConnection(), username);
        if (user != null) {
            if (!newNickname.equals(user.getNickname())) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, newNickname);
                    stmt.setString(2, username);
                    stmt.executeUpdate();
                    user.setNickname(newNickname);
                }
            }
        }
    }

    // Check if the password change is possible and return corresponding error codes
    public static int canChangePassword(Connection conn, String username, String oldPassword, String newPassword) throws SQLException {
        // Check if user exists
        if (!userExists(conn, username)) {
            return 7; // User not found
        }

        // Check if current password is correct
        if (!isCurrentPasswordCorrect(conn, username, oldPassword)) {
            return 1; // Current password is incorrect
        }

        // Check if new password is provided
        if (newPassword == null || newPassword.isEmpty()) {
            return 2; // New password not provided
        }

        // Check if new password meets the requirements
        if (newPassword.length() < 8) {
            return 31; // Password too short
        }

        if (!hasRequiredCharacters(newPassword)) {
            return 32; // Password lacks required characters
        }

        // Optional: Implement CAPTCHA verification
        if (needsCaptchaVerification()) {
            return 4; // CAPTCHA verification required
        }

        // If all checks pass, proceed to change the password
        return changePassword(conn, username, oldPassword, newPassword);
    }

    private static boolean needsCaptchaVerification() {
        // Implement logic to determine if CAPTCHA verification is needed
        // This can depend on various factors like user behavior, number of failed attempts, etc.
        return false; // Example: Always return false for simplicity
    }


    private static boolean userExists(Connection conn, String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM user WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private static boolean isCurrentPasswordCorrect(Connection conn, String username, String oldPassword) throws SQLException {
        String query = "SELECT password FROM user WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String currentPassword = rs.getString(1);
                    return currentPassword.equals(oldPassword); // Assuming passwords are stored in plain text
                }
            }
        }
        return false;
    }

    private static boolean hasRequiredCharacters(String password) {
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUppercase = true;
            if (Character.isLowerCase(c)) hasLowercase = true;
            if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }

        return hasUppercase && hasLowercase && hasSpecial;
    }

    // Change the password and return appropriate code
    public static int changePassword(Connection conn, String username, String oldPassword, String newPassword) throws SQLException {
        String updateQuery = "UPDATE user SET password = ? WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            stmt.setString(3, oldPassword);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return 5; // Password updated successfully
            } else {
                return 6; // Password update failed
            }
        }
    }

    // Validate the password properties
    private static boolean isValidPassword1(String password) {
        return password.length() >= 8;
    }
    private static boolean isValidPassword2(String password){
        return password.matches(".*[A-Z].*") && password.matches(".*[a-z].*") && password.matches(".*\\d.*") && password.matches(".*[^a-zA-Z\\d].*");
    }
    // Simple CAPTCHA mechanism
    private static boolean verifyCaptcha() {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int a = random.nextInt(10);
        int b = random.nextInt(10);
        System.out.printf("CAPTCHA: What is %d + %d? ", a, b);
        int answer = scanner.nextInt();
        return answer == (a + b);
    }
    public static int changeEmail(Connection conn, String username, String newEmail) throws SQLException {
        String sql = "UPDATE USER SET email = ? WHERE username = ?";
        User user = User.fetchByUsername(DatabaseUtil.getConnection(), username);
        if (user != null) {
            if (!newEmail.equals(user.getEmail())) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, newEmail);
                    stmt.setString(2, username);
                    stmt.executeUpdate();
                    user.setEmail(newEmail);
                    return 1;
                }
            }
            else {
                return 0;
            }
        }
        else {
            return 2;
        }
    }

}

