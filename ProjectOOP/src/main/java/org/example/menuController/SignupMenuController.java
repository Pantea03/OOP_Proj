package org.example.menuController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.model.DatabaseUtil;
import org.example.model.User;
import org.example.model.Menu;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

import javafx.scene.image.Image;

public class SignupMenuController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField usernameField, nicknameField, securityQuestionField, securityAnswerField, emailField, captchaInputField;

    @FXML
    private PasswordField passwordField, passwordConfirmationField;

    @FXML
    private Label captchaLabel;
    @FXML
    private ChoiceBox<String> securityQuestionChoiceBox;

    private static final Random random = new Random();
    private String generatedCaptcha;
    private Map<String, String> captchaImageAnswers = new HashMap<>();
    private String currentCaptchaCode;

    @FXML
    private HBox captchaBox;

    @FXML
    private Button generatePasswordButton;

    @FXML
    private void initialize() {
        initializeCaptchaImageAnswers();
        setPaneBackground();
        handleGenerateCaptcha();
    }

    private void initializeCaptchaImageAnswers() {
        // Replace with your captcha image answers
        captchaImageAnswers.put("captcha_training1.png", "pdw38");
        captchaImageAnswers.put("captcha_training2.png", "w46ep");
        captchaImageAnswers.put("captcha_training3.png", "gp22x");
        // Add more captcha codes and corresponding answers as needed
    }

    @FXML
    public void handleGeneratePassword() {
        String randomPassword = generateRandomPassword();
        passwordField.setText(randomPassword);
        passwordConfirmationField.setText(randomPassword);
        showAlert(Alert.AlertType.INFORMATION, "Generated Password", "Your random password: " + randomPassword);
    }

    @FXML
    public void handleGenerateCaptcha() {
        generatedCaptcha = generateCaptcha();
        displayCaptcha(generatedCaptcha);
    }

    @FXML
    public void handleSignup() {
        try {
            boolean success = true;
            String username = usernameField.getText();
            String password = passwordField.getText();
            String passwordConfirmation = passwordConfirmationField.getText();
            String email = emailField.getText();
            String nickname = nicknameField.getText();
            String securityQuestion = securityQuestionChoiceBox.getValue();
            String securityAnswer = securityAnswerField.getText();
            String enteredCaptcha = captchaInputField.getText().trim();
            String expectedCaptcha = captchaImageAnswers.get(currentCaptchaCode);

            System.out.println("Entered CAPTCHA: " + enteredCaptcha);
            System.out.println("Expected CAPTCHA: " + expectedCaptcha);
            System.out.println("Current CAPTCHA Code: " + currentCaptchaCode);

            try (Connection conn = DatabaseUtil.getConnection()) {
                if (username.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Username field is empty!");
                    success = false;
                } else if (nickname.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Nickname field is empty!");
                    success = false;
                } else if (password.isEmpty()) {
                    ButtonType generatePasswordButton = new ButtonType("Generate Random Password", ButtonBar.ButtonData.OTHER);
                    Alert passwordAlert = new Alert(Alert.AlertType.INFORMATION, "Password field is empty!", generatePasswordButton, ButtonType.CANCEL);
                    passwordAlert.setHeaderText("Password Generation");
                    Optional<ButtonType> result = passwordAlert.showAndWait();

                    if (result.isPresent() && result.get() == generatePasswordButton) {
                        password = generateRandomPassword();
                        Alert generatedPasswordAlert = new Alert(Alert.AlertType.INFORMATION, "Your random password: " + password + "\nPlease enter your password to confirm.");
                        generatedPasswordAlert.setHeaderText("Confirm Password");
                        TextField passwordConfirmationField = new TextField();
                        generatedPasswordAlert.getDialogPane().setContent(passwordConfirmationField);
                        Optional<ButtonType> confirmationResult = generatedPasswordAlert.showAndWait();

                        if (confirmationResult.isPresent() && confirmationResult.get() == ButtonType.OK) {
                            String enteredPassword = passwordConfirmationField.getText();
                            if (!password.equals(enteredPassword)) {
                                showAlert(Alert.AlertType.ERROR, "Error", "Wrong password entered!");
                                success = false;
                            }
                        } else {
                            success = false;
                        }
                    } else {
                        success = false;
                    }
                } else if (passwordConfirmation.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Password Confirmation field is empty!");
                    success = false;
                } else if (!password.equals(passwordConfirmation)) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match!");
                    success = false;
                } else if (email.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Email field is empty!");
                    success = false;
                } else if (Menu.userAlreadyExists(username, conn)) {
                    showAlert(Alert.AlertType.ERROR, "Error", "This user already exists!");
                    success = false;
                } else if (password.length() < 8) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Weak password. Must be at least 8 characters long.");
                    success = false;
                } else if (!Pattern.compile("[a-z]").matcher(password).find() ||
                        !Pattern.compile("[A-Z]").matcher(password).find() ||
                        !Pattern.compile("[^a-zA-Z0-9]").matcher(password).find()) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Weak password. Must contain uppercase, lowercase, and a special character.");
                    success = false;
                } else if (!email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid email format.");
                    success = false;
                } else if (!validateEmail(email)) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid email format!");
                    success = false;
                } else if (!enteredCaptcha.equalsIgnoreCase(expectedCaptcha)) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Captcha is incorrect. Please try again.");
                    success = false;
                }

                if (success) {
                    User user = new User(username, password, nickname, email, securityQuestion, securityAnswer);
                    user.save(conn);
                    LoginMenuController.currentUser = User.fetchByUsername(conn, username);
                    LoginMenuController.currentUser.updateLoggedInState(DatabaseUtil.getConnection(), true);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Account created successfully!");
                    redirectToMainMenu();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private String generateCaptcha() {
        String[] captchaCodes = captchaImageAnswers.keySet().toArray(new String[0]);
        currentCaptchaCode = captchaCodes[new Random().nextInt(captchaCodes.length)];
        return currentCaptchaCode;
    }

    private void displayCaptcha(String captchaCode) {
        captchaBox.getChildren().clear();

        Image captchaImage = loadCaptchaImage(captchaCode);

        if (captchaImage != null) {
            // Display captcha image
            ImageView captchaImageView = new ImageView(captchaImage);
            captchaImageView.setFitWidth(150);
            captchaImageView.setFitHeight(50);
            HBox.setMargin(captchaImageView, new Insets(10, 0, 10, 0));

            captchaBox.getChildren().addAll(captchaImageView, captchaInputField);
        } else {
            System.err.println("Failed to load CAPTCHA image.");
        }
    }

    private Image loadCaptchaImage(String captchaCode) {
        // Get image path based on captcha code
        File file = new File("C:\\Users\\LEGION\\IdeaProjects\\ProjectOOP\\src\\main\\resources\\captcha\\" + captchaCode);
        if (file.exists()) {
            return new Image(file.toURI().toString());
        } else {
            System.err.println("Image file does not exist: " + file.getPath());
            // Return a placeholder image or handle missing image scenario
            return null; // Or use a placeholder image
        }
    }


    private boolean validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void setPaneBackground() {
        Image backgroundImage = new Image(getClass().getResource("/images/mainMenu.jpg").toExternalForm());
        BackgroundImage bImage = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        Background background = new Background(bImage);
        rootPane.setBackground(background);
    }

    private String generateRandomPassword() {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_-+=<>?";
        int passwordLength = 12;
        StringBuilder password = new StringBuilder(passwordLength);
        Random random = new Random();

        for (int i = 0; i < passwordLength; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }

        return password.toString();
    }
}
