package org.example.menuController;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import org.example.view.View;
import org.example.view.ViewSwitcher;

public class GameMenuController {
    @FXML
    private AnchorPane rootPane;

    @FXML
    public void initialize() {
        setPaneBackground();
    }

    @FXML
    private void setPaneBackground() {
        String backgroundFile = "/images/main.jpg"; // Path to your background image

        try {
            Image backgroundImage = new Image(getClass().getResource(backgroundFile).toExternalForm());
            BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, false, true);
            BackgroundImage bImage = new BackgroundImage(backgroundImage,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, backgroundSize);
            Background background = new Background(bImage);
            rootPane.setBackground(background);
        } catch (IllegalArgumentException e) {
            System.err.println("Error loading background image: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void gotoTwoPlayerMode(MouseEvent mouseEvent) {
        LoginMenuController.gotoTwoPlayerMode();
        ViewSwitcher.switchTo(View.LOGIN_MENU);
    }

    @FXML
    public void gotoBettingMode(MouseEvent mouseEvent) {
        ViewSwitcher.switchTo(View.BETTING_MODE);
    }

    @FXML
    public void gotoSinglePlayerMode(MouseEvent mouseEvent) {
        ViewSwitcher.switchTo(View.SINGLE_PLAYER_MODE);
    }

    @FXML
    public void goBack(MouseEvent mouseEvent) {
        ViewSwitcher.switchTo(View.MAIN_MENU);
    }
}
