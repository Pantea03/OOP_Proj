package org.example.menuController;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.model.DatabaseUtil;
import org.example.model.MainGameMenu;
import org.example.model.User;
import org.example.model.Menu;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import org.example.view.View;
import org.example.view.ViewSwitcher;

public class LoginMenuController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label loginMessageLabel;

    private int loginAttempts = 0;
    private Timer lockoutTimer;
    private boolean doublePlayer = false; // Example initial values
    private boolean gamblePlayer = false;
    private static boolean isSecondUser = false;
    public static User currentUser;
    static User secondUser;
    private Menu currentMenu;





    @FXML
    private void handleBackButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/InitialPage.fxml"));
            AnchorPane initialPagePane = loader.load();
            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene scene = new Scene(initialPagePane);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Button forgotPasswordButton;

    @FXML
    private Label countdownLabel;

//
//    @FXML
//    public void handleLogin() {
//        String username = usernameField.getText();
//        String password = passwordField.getText();
//
//        try (Connection conn = DatabaseUtil.getConnection()) {
//            if (!User.userExists(username, conn)) {
//                showAlert(Alert.AlertType.ERROR, "Error", "Username doesn't exist!");
//            } else {
//                User fetchedUser = User.fetchByUsername(conn, username);
//                if (!fetchedUser.getPassword().equals(password)) {
//                    showAlert(Alert.AlertType.ERROR, "Error", "Password and Username don’t match!");
//                } else {
//                    if (isSecondUser) {
//                        secondUser = fetchedUser;
//                        showAlert(Alert.AlertType.INFORMATION, "Success", "Second user logged in successfully!");
////                        ViewSwitcher.switchTo(...);
//                    } else {
//                        currentUser = fetchedUser;
//                        showAlert(Alert.AlertType.INFORMATION, "Success", "User logged in successfully!");
//                        ViewSwitcher.switchTo(View.MAIN_MENU);
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (!User.userExists(username, conn)) {
                showAlert(Alert.AlertType.ERROR, "Error", "Username doesn't exist!");
            } else {
                User fetchedUser = User.fetchByUsername(conn, username);
                if (!fetchedUser.getPassword().equals(password)) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Password and Username don’t match!");
                } else {
                    if (isSecondUser) {
                        secondUser = fetchedUser;
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Second user logged in successfully!");
                        showCharacterSelection();
                    } else {
                        currentUser = fetchedUser;
                        showAlert(Alert.AlertType.INFORMATION, "Success", "User logged in successfully!");
                        ViewSwitcher.switchTo(View.MAIN_MENU);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showCharacterSelection() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CharacterSelection.fxml"));
            AnchorPane characterSelectionPane = loader.load();
            CharacterSelectionController controller = loader.getController();
            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene scene = new Scene(characterSelectionPane);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public static void gotoTwoPlayerMode() {
        isSecondUser = true;
        ViewSwitcher.switchTo(View.LOGIN_MENU);
    }

    @FXML
    public void gotoBettingMode() {
        ViewSwitcher.switchTo(View.BETTING_MODE);
    }

    @FXML
    public void gotoSinglePlayerMode() {
        ViewSwitcher.switchTo(View.SINGLE_PLAYER_MODE);
    }
    @FXML
    public void handleForgotPassword() {
        String username = usernameField.getText();

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (!User.userExists(username, conn)) {
                loginMessageLabel.setText("Username doesn't exist!");
            } else {
                User user = User.fetchByUsername(conn, username);
                String securityQuestion = user.getSecurityQuestion();
                String securityAnswer = user.getSecurityAnswer();

                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Security Question");
                dialog.setHeaderText(securityQuestion);
                dialog.setContentText("Answer:");

                dialog.showAndWait().ifPresent(answer -> {
                    if (answer.equals(securityAnswer)) {
                        TextInputDialog newPasswordDialog = new TextInputDialog();
                        newPasswordDialog.setTitle("Reset Password");
                        newPasswordDialog.setHeaderText("Enter a new password:");
                        newPasswordDialog.showAndWait().ifPresent(newPassword -> {
                            try {
                                user.setPassword(newPassword);
                                user.updatePasswordInDatabase(conn);
                                loginMessageLabel.setText("Password updated successfully.");
                            } catch (SQLException e) {
                                e.printStackTrace();
                                loginMessageLabel.setText("Password update failed.");
                            }
                        });
                    } else {
                        loginMessageLabel.setText("Incorrect security answer.");
                    }
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void handleLockout(String username) {
        loginAttempts++;
        if (loginAttempts >= 2) {
            loginMessageLabel.setText("Too many failed attempts. Please wait 10 seconds.");
            startCountdown();
            loginAttempts = 0; // Reset the counter after starting the countdown
        } else {
            loginMessageLabel.setText("Incorrect password. Attempt " + loginAttempts + " of 2.");
        }
    }

    private void startCountdown() {
        countdownLabel.setVisible(true);
        countdownLabel.setText("10");
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            int seconds = 10;

            @Override
            public void run() {
                Platform.runLater(() -> countdownLabel.setText(String.valueOf(seconds)));
                seconds--;
                if (seconds < 0) {
                    timer.cancel();
                    Platform.runLater(() -> {
                        countdownLabel.setVisible(false);
                        loginMessageLabel.setText("");
                        usernameField.setDisable(false);
                        passwordField.setDisable(false);
                    });
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
        usernameField.setDisable(true);
        passwordField.setDisable(true);
    }


    private void redirectToMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            AnchorPane mainMenuPane = loader.load();
            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene scene = new Scene(mainMenuPane);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        setPaneBackground();
        forgotPasswordButton.setOnAction(e -> handleForgotPassword());
    }

    private void setPaneBackground() {
        Image backgroundImage = new Image(getClass().getResource("/images/mainMenu.jpg").toExternalForm());
        BackgroundImage bImage = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        Background background = new Background(bImage);
        rootPane.setBackground(background);
    }
}
