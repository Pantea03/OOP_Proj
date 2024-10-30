package org.example.menuController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import org.example.model.DatabaseUtil;
import org.example.model.User;
import org.example.view.SettingsUtil;
import org.example.view.View;
import org.example.view.ViewSwitcher;

import java.sql.SQLException;

public class MainMenuController {
    @FXML
    private ImageView avatar;
    @FXML
    private Label username;

    @FXML
    private AnchorPane rootPane;


    @FXML
    public void initialize() {
        setPaneBackground();
//        SettingsUtil.setBackground(SettingsUtil.getSelectedBackground()); // Ensure selectedBackground is initialized
//        SettingsUtil.applyBackgroundImage(rootPane);
        if (SettingMenuController.isMusicEnabled)
            SettingsUtil.playMusic(SettingsUtil.MUSIC_FILE_1, 0.5);
        currentUser = getCurrentUser();
    }
    @FXML
    private void setPaneBackground() {
        String backgroundFile;
        switch (SettingsUtil.getSelectedBackground()) {
            case SettingsUtil.BACKGROUND_FILE_2:
                backgroundFile = SettingsUtil.BACKGROUND_FILE_2;
                break;
            case SettingsUtil.BACKGROUND_FILE_3:
                backgroundFile = SettingsUtil.BACKGROUND_FILE_3;
                break;
            case SettingsUtil.BACKGROUND_FILE_1:
            default:
                backgroundFile = SettingsUtil.BACKGROUND_FILE_1;
                break;
        }

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

//    @FXML
//    private void setPaneBackground() {
//        Image backgroundImage = new Image(getClass().getResource("/images/main.jpg").toExternalForm());
//        BackgroundImage bImage = new BackgroundImage(backgroundImage,
//                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
//                BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
//        Background background = new Background(bImage);
//        rootPane.setBackground(background);
//    }


    @FXML
    public void gotoProfileMenu(MouseEvent mouseEvent) {
        ViewSwitcher.switchTo(View.PROFILE_MENU);
    }

    @FXML
    public void exitGame() throws SQLException {
        LoginMenuController.currentUser.updateLoggedInState(DatabaseUtil.getConnection(), false);
        System.exit(0);
    }

    @FXML
    public void gotoGameHistoryMenu() {

    }
    @FXML
    public void gotoSettings() {
        ViewSwitcher.switchTo(View.SETTING);
    }

    @FXML
    public void startNewGame(MouseEvent mouseEvent) {
        ViewSwitcher.switchTo(View.GAME_MENU);
    }
    private User currentUser;

    @FXML
    private void gotoStore() {
        if (currentUser != null) {
            StoreController.currentUser = currentUser; // Set the current user before switching to the Store view
            ViewSwitcher.switchTo(View.STORE);
        } else {
            System.err.println("Error: currentUser is null.");
        }
    }

    private User getCurrentUser() {
        // Implement your logic to retrieve the current user
        return new User(/* initialize user */);
    }

}
