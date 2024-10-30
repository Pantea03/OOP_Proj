package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.view.SettingsUtil;
import org.example.view.View;
import org.example.view.ViewSwitcher;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        ViewSwitcher.setStage(stage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/InitialPage.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Game");
        stage.show();
        SettingsUtil.playMusic(SettingsUtil.MUSIC_FILE_1, 0.5);
    }

}