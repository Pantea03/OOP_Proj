package org.example.menuController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class InitialPageController {

    @FXML
    private Button loginButton;

    @FXML
    private Button signupButton;

    @FXML
    public void handleLoginButtonClick() {
        loadPage("/fxml/LoginMenu.fxml");
    }

    @FXML
    public void handleSignupButtonClick() {
        loadPage("/fxml/SigninMenu.fxml");
    }

    private void loadPage(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane pane = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
