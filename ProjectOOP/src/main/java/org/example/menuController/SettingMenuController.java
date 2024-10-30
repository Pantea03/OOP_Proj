package org.example.menuController;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import org.example.view.SettingsUtil;
import org.example.view.View;
import org.example.view.ViewSwitcher;

public class SettingMenuController {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private CheckBox musicCheckbox;
    @FXML
    private CheckBox soundEffectsCheckbox;
    @FXML
    private ComboBox<String> themeComboBox;
    @FXML
    private ComboBox<String> backgroundComboBox;
    @FXML
    private Slider volumeSlider; // Reference to the volume slider

    static boolean isMusicEnabled = true; // Initial state for music
    static boolean areSoundEffectsEnabled = true; // Initial state for sound effects
    static String selectedBackground = "Background 1"; // Default background

    @FXML
    public void initialize() {
        initializeSettings();

        // Play default music on startup
        if (isMusicEnabled) {
            SettingsUtil.playMusic(SettingsUtil.MUSIC_FILE_1, volumeSlider.getValue());
        }

        // Listener for volume slider changes
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (isMusicEnabled) {
                SettingsUtil.setVolume(newValue.doubleValue());
            }
        });
    }

    private void initializeSettings() {
        // Example: Load user settings from preferences or default values
        musicCheckbox.setSelected(isMusicEnabled);
        soundEffectsCheckbox.setSelected(areSoundEffectsEnabled);

        // Populate theme options
        themeComboBox.getItems().addAll("Theme 1", "Theme 2", "Theme 3");
        themeComboBox.setValue("Theme 1"); // Default theme selection

        // Set theme change listener
        themeComboBox.setOnAction(event -> {
            String selectedTheme = themeComboBox.getValue();
            switch (selectedTheme) {
                case "Theme 1":
                    SettingsUtil.playMusic(SettingsUtil.MUSIC_FILE_1, volumeSlider.getValue());
                    break;
                case "Theme 2":
                    SettingsUtil.playMusic(SettingsUtil.MUSIC_FILE_2, volumeSlider.getValue());
                    break;
                case "Theme 3":
                    SettingsUtil.playMusic(SettingsUtil.MUSIC_FILE_3, volumeSlider.getValue());
                    break;
                default:
                    break;
            }
        });

        // Populate background options
        backgroundComboBox.getItems().addAll("Background 1", "Background 2", "Background 3");
        backgroundComboBox.setValue(selectedBackground); // Default background selection

        // Set background change listener
        backgroundComboBox.setOnAction(event -> {
            selectedBackground = backgroundComboBox.getValue();
        });
    }

    @FXML
    public void applySettings() {
        // Implement logic to apply settings (e.g., save to preferences)
        isMusicEnabled = musicCheckbox.isSelected();
        areSoundEffectsEnabled = soundEffectsCheckbox.isSelected();

        // Save selected background
        SettingsUtil.setBackground(selectedBackground);

        // Example: Save settings to preferences or database
        // For demonstration, just show an alert
        showAlert("Settings Applied", "Settings applied successfully!");

        // Play or stop music based on user settings
        if (isMusicEnabled) {
            String selectedTheme = themeComboBox.getValue();
            switch (selectedTheme) {
                case "Theme 1":
                    SettingsUtil.playMusic(SettingsUtil.MUSIC_FILE_1, volumeSlider.getValue());
                    break;
                case "Theme 2":
                    SettingsUtil.playMusic(SettingsUtil.MUSIC_FILE_2, volumeSlider.getValue());
                    break;
                case "Theme 3":
                    SettingsUtil.playMusic(SettingsUtil.MUSIC_FILE_3, volumeSlider.getValue());
                    break;
                default:
                    break;
            }
        } else {
            SettingsUtil.stopMusic();
        }
    }

    @FXML
    public void cancelSettings() {
        // Implement logic to cancel settings and go back to main menu
        ViewSwitcher.switchTo(View.MAIN_MENU); // Example: Switch back to main menu
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
