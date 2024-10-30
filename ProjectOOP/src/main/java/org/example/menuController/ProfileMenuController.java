package org.example.menuController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.model.DatabaseUtil;
import org.example.model.ProfileMenu;
import org.example.model.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ProfileMenuController {
    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label usernameLabel, levelLabel, xpLabel, hpLabel, coinsLabel, captchaLabel;

    @FXML
    private TextField newUsernameField, newNicknameField, newEmailField, captchaInputField;

    @FXML
    private PasswordField oldPasswordField, newPasswordField;

    @FXML
    private Button saveChangesButton, backButton, generateCaptchaButton;

    private User currentUser;

    private static final Random random = new Random();
    private static final Map<Character, String[]> asciiArtMap = new HashMap<>();

    public void initialize() {
        initializeAsciiArt();
        // Fetch current user data
        try (Connection conn = DatabaseUtil.getConnection()) {
//            currentUser = ProfileMenu.showInformation(conn, LoginMenuController.currentUser.getUsername());
            currentUser = LoginMenuController.currentUser;
            if (currentUser != null) {
                updateUserInfo();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "You have to log in first!");
                // Add logic to redirect to main menu if needed
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Load the back button icon
        Image backImage = new Image(getClass().getResourceAsStream("/images/back.png"));
        ImageView backIcon = new ImageView(backImage);
        backIcon.setFitWidth(30);
        backIcon.setFitHeight(30);
        backButton.setGraphic(backIcon);
    }

    private void updateUserInfo() {
        usernameLabel.setText(currentUser.getUsername());
        levelLabel.setText("Level: " + currentUser.getLevel());
        xpLabel.setText("XP: " + currentUser.getXP());
        hpLabel.setText("HP: " + currentUser.getHP());
        coinsLabel.setText("Coins: " + currentUser.getCoin());
    }

    @FXML
    private void saveChanges() {
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "You have to log in first!");
            return;
        }

        if (!verifyCaptcha()) {
            showAlert(Alert.AlertType.ERROR, "Error", "CAPTCHA verification failed. Please try again.");
            return;
        }

        try (Connection conn = DatabaseUtil.getConnection()) {
            // Change username
            if (!newUsernameField.getText().isEmpty()) {
                processChangeUsername(conn, newUsernameField.getText());
            }

            // Change nickname
            if (!newNicknameField.getText().isEmpty()) {
                processChangeNickname(conn, newNicknameField.getText());
            }

            // Change password
            if (!oldPasswordField.getText().isEmpty() && !newPasswordField.getText().isEmpty()) {
                processChangePassword(conn, oldPasswordField.getText(), newPasswordField.getText());
            }

            // Change email
            if (!newEmailField.getText().isEmpty()) {
                processChangeEmail(conn, newEmailField.getText());
            }

            updateUserInfo(); // Refresh the displayed user info
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void processChangeUsername(Connection conn, String newUsername) throws SQLException {
        int result = ProfileMenu.canChangeUsername(conn, currentUser.getUsername(), newUsername);
        if (result == 1) {
            ProfileMenu.changeUsername(conn, currentUser, newUsername);
            currentUser.setUsername(newUsername);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Username updated successfully!");
        } else if (result == 0) {
            showAlert(Alert.AlertType.ERROR, "Error", "No change in the username!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "User not found.");
        }
    }

    private void processChangeNickname(Connection conn, String newNickname) throws SQLException {
        int result = ProfileMenu.canChangeNickname(conn, currentUser.getUsername(), newNickname);
        if (result == 1) {
            ProfileMenu.changeNickname(conn, currentUser.getUsername(), newNickname);
            currentUser.setNickname(newNickname);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Nickname updated successfully!");
        } else if (result == 0) {
            showAlert(Alert.AlertType.ERROR, "Error", "No change in the nickname!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "User not found.");
        }
    }

    @FXML
    private void processChangePassword(Connection conn, String oldPassword, String newPassword) throws SQLException {
        // Check if the password change is possible and return corresponding error codes
        int result = ProfileMenu.canChangePassword(conn, currentUser.getUsername(), oldPassword, newPassword);

        switch (result) {
            case 1:
                showAlert(Alert.AlertType.ERROR, "Error", "Current password is incorrect!");
                break;
            case 2:
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter a new password!");
                break;
            case 31:
                showAlert(Alert.AlertType.ERROR, "Error", "Weak password. Must be at least 8 characters long.");
                break;
            case 32:
                showAlert(Alert.AlertType.ERROR, "Error", "Weak password. Must contain uppercase, lowercase, and a special character.");
                break;
            case 4:
                launchCaptchaVerification(); // Launch CAPTCHA verification scene
                break;
            case 5:
                showAlert(Alert.AlertType.INFORMATION, "Success", "Password updated successfully!");
                launchPasswordConfirmation(newPassword); // Ask user to confirm new password
                break;
            case 6:
                showAlert(Alert.AlertType.ERROR, "Error", "Password update failed.");
                break;
            case 7:
                showAlert(Alert.AlertType.ERROR, "Error", "User not found.");
                break;
            default:
                showAlert(Alert.AlertType.ERROR, "Error", "Unexpected error occurred.");
                break;
        }
    }


    private void launchCaptchaVerification() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CaptchaVerification.fxml"));
            AnchorPane captchaPane = loader.load();

            // Get the current stage
            Stage stage = (Stage) rootPane.getScene().getWindow();

            // Set the scene to the CAPTCHA verification
            Scene scene = new Scene(captchaPane);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void launchPasswordConfirmation(String newPassword) {
        // Implement logic to confirm password again if needed
        // This might involve user input in the existing scene or another step.
    }

    private void processChangeEmail(Connection conn, String newEmail) throws SQLException {
        int result = ProfileMenu.changeEmail(conn, currentUser.getUsername(), newEmail);
        if (result == 1) {
            currentUser.setEmail(newEmail);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Email updated successfully!");
        } else if (result == 0) {
            showAlert(Alert.AlertType.ERROR, "Error", "No change in the email!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "User not found!");
        }
    }

    @FXML
    private void handleBackButtonClick() {
        try {
            // Load the main menu FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            AnchorPane mainMenuPane = loader.load();

            // Get the current stage
            Stage stage = (Stage) rootPane.getScene().getWindow();

            // Set the scene to the main menu
            Scene scene = new Scene(mainMenuPane);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGenerateCaptchaButtonClick() {
        String captcha = generateCaptcha();
        displayCaptcha(captcha);
    }

    private static String generateRandomPassword() {
        int length = 12;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }
        return password.toString();
    }

    private static void initializeAsciiArt() {
        asciiArtMap.put('0', new String[]{
                " .d8888b. ",
                "d88P  Y88b",
                "888    888",
                "888    888",
                "888    888",
                "888    888",
                "Y88b  d88P",
                " 'Y8888P' "
        });
        asciiArtMap.put('1', new String[]{
                "  .d888   ",
                " d88P     ",
                "  888     ",
                "  888     ",
                "  888     ",
                "  888     ",
                "  888     ",
                "  888     "
        });
        asciiArtMap.put('2', new String[]{
                " .d8888b. ",
                "d88P  Y88b",
                "      .d88",
                "    .d88P ",
                "  .d88P   ",
                " .d88P    ",
                "d88P  .d88",
                "8888888P' "
        });
        asciiArtMap.put('3', new String[]{
                " .d8888b. ",
                "d88P  Y88b",
                "     .d88P",
                "   .d88P  ",
                "    8888' ",
                "     Y8b. ",
                "Y88b  d88P",
                " 'Y8888P' "
        });
        asciiArtMap.put('4', new String[]{
                "    d8888 ",
                "   d8P888 ",
                "  d8P 888 ",
                " d8P  888 ",
                "d88   888 ",
                "8888888888",
                "      888 ",
                "      888 "
        });
        asciiArtMap.put('5', new String[]{
                "8888888888",
                "888       ",
                "888       ",
                "8888888b. ",
                "     'Y88b",
                "       888",
                "Y88b  d88P",
                " 'Y8888P' "
        });
        asciiArtMap.put('6', new String[]{
                " .d8888b. ",
                "d88P  Y88b",
                "888       ",
                "888d888b. ",
                "888P 'Y88b",
                "888    888",
                "Y88b  d88P",
                " 'Y8888P' "
        });
        asciiArtMap.put('7', new String[]{
                "8888888888",
                "      d88P",
                "     d88P ",
                "    d88P  ",
                "   d88P   ",
                "  d88P    ",
                " d88P     ",
                "d88P      "
        });
        asciiArtMap.put('8', new String[]{
                " .d8888b. ",
                "d88P  Y88b",
                "Y88b. d88P",
                " 'Y8888P' ",
                " .d88P88b.",
                "888  8888",
                "Y88b  d88P",
                " 'Y8888P' "
        });
        asciiArtMap.put('9', new String[]{
                " .d8888b. ",
                "d88P  Y88b",
                "888    888",
                "Y88b. d888",
                " 'Y888P888",
                "     .d88P",
                "    8888P ",
                "    888P  "
        });
    }

    private boolean verifyCaptcha() {
        String generatedCaptcha = captchaLabel.getText().replaceAll("[^0-9]", "");
        String inputCaptcha = captchaInputField.getText();
        return inputCaptcha.equals(generatedCaptcha);
    }

    private static String generateCaptcha() {
        int captchaLength = random.nextInt(3) + 5;
        String captchaCharacters = "0123456789";
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < captchaLength; i++) {
            captcha.append(captchaCharacters.charAt(random.nextInt(captchaCharacters.length())));
        }
        return captcha.toString();
    }

    private void displayCaptcha(String captcha) {
        StringBuilder captchaDisplay = new StringBuilder();
        for (char ch : captcha.toCharArray()) {
            if (asciiArtMap.containsKey(ch)) {
                String[] asciiArt = asciiArtMap.get(ch);
                for (String line : asciiArt) {
                    for (char c : line.toCharArray()) {
                        if (random.nextBoolean()) {
                            // Introduce some noise
                            if (random.nextDouble() < 0.1) {
                                captchaDisplay.append("*"); // 10% chance for noise
                            } else if (random.nextDouble() < 0.2) {
                                captchaDisplay.append("!"); // 20% chance for noise
                            } else {
                                captchaDisplay.append(random.nextBoolean() ? Character.toLowerCase(c) : Character.toUpperCase(c));
                            }
                        } else {
                            captchaDisplay.append(c);
                        }
                    }
                    captchaDisplay.append("\n");
                }
            } else {
                captchaDisplay.append(ch).append("\n"); // If no ASCII art, print the character directly
            }
            captchaDisplay.append("\n"); // Add some spacing between characters
        }
        captchaLabel.setText(captchaDisplay.toString());
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
