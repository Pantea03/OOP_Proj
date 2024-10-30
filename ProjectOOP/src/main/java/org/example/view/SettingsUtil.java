package org.example.view;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class SettingsUtil {
    public static final String MUSIC_FILE_1 = "/music/lalaland1.mp3"; // Replace with your music file paths
    public static final String MUSIC_FILE_2 = "/music/lalaland2.mp3";
    public static final String MUSIC_FILE_3 = "/music/lalaland3.mp3";
    private static MediaPlayer mediaPlayer;
    private static String currentMusicFile; // Track current music file

    public static void playMusic(String musicFile, double volume) {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING && currentMusicFile.equals(musicFile)) {
            return; // Music is already playing for the same file, no need to start again
        }

        stopMusic(); // Stop any currently playing music

        Media media = new Media(SettingsUtil.class.getResource(musicFile).toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Set indefinite repeat
        mediaPlayer.setVolume(volume); // Set initial volume
        mediaPlayer.play();
        currentMusicFile = musicFile; // Update current music file
    }

    public static void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose(); // Dispose mediaPlayer to release resources
            mediaPlayer = null; // Reset mediaPlayer instance
            currentMusicFile = null; // Reset current music file
        }
    }

    public static void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }
    // Background image file paths
    private static String selectedBackground = "/images/main.jpg"; // Default background
    public static final String BACKGROUND_FILE_1 = "/images/main.jpg";
    public static final String BACKGROUND_FILE_2 = "/images/main2.jpg";
    public static final String BACKGROUND_FILE_3 = "/images/main3.jpg";


    public static void setBackground(String background) {
        selectedBackground = background;
    }

    public static String getSelectedBackground() {
        return selectedBackground;
    }

    public static void applyBackgroundImage(AnchorPane pane) {
        String backgroundFile;
        switch (selectedBackground) {
            case "Background 2":
                backgroundFile = BACKGROUND_FILE_2;
                break;
            case "Background 3":
                backgroundFile = BACKGROUND_FILE_3;
                break;
            case "Background 1":
            default:
                backgroundFile = BACKGROUND_FILE_1;
                break;
        }
        try {
            Image backgroundImage = new Image(SettingsUtil.class.getResourceAsStream(backgroundFile));
            BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, false, true);
            BackgroundImage bImage = new BackgroundImage(backgroundImage,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, backgroundSize);
            Background background = new Background(bImage);
            pane.setBackground(background);
        } catch (IllegalArgumentException e) {
            System.err.println("Error loading background image: " + e.getMessage());
            e.printStackTrace();
        }
    }



}
