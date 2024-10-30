package org.example.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.view.View;

import java.io.IOException;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ViewSwitcher {
    private static Stage stage;

    public static void switchTo(View view) {
        Parent root = null;
        try {
            // Load the FXML file for the given view
            root = FXMLLoader.load(Objects.requireNonNull(ViewSwitcher.class.getResource(view.getFile())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Adjust the stage size based on the view
        switch (view) {
            case MAIN_MENU, PROFILE_MENU -> {
                stage.setHeight(600);
                stage.setWidth(800);
            }
            case CAPTCHA_VERIFICATION, SETTING, GAME_MENU, STORE ->
            {
                stage.setHeight(500);
                stage.setWidth(500);
            }
            case  SIGNIN_MENU, LOGIN_MENU -> {
                stage.setHeight(700);
                stage.setWidth(700);
            }



            // Add other cases if needed
        }

        // Check if root is not null before creating the scene
        assert root != null;
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        ViewSwitcher.stage = stage;
    }
}
