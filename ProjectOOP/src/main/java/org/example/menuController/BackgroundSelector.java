package org.example.menuController;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class BackgroundSelector {

    public static void setBackground(AnchorPane root, String selectedBackground) {
        BackgroundImage backgroundImage = null;

        switch (selectedBackground) {
            case "Background 1":
                backgroundImage = new BackgroundImage(
                        new Image("/images/main.jpg", 800, 600, false, true),
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.DEFAULT,
                        BackgroundSize.DEFAULT
                );
                break;
            case "Background 2":
                backgroundImage = new BackgroundImage(
                        new Image("/images/main2.jpg", 800, 600, false, true),
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.DEFAULT,
                        BackgroundSize.DEFAULT
                );
                break;
            case "Background 3":
                backgroundImage = new BackgroundImage(
                        new Image("/images/main3.jpg", 800, 600, false, true),
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.DEFAULT,
                        BackgroundSize.DEFAULT
                );
                break;
        }

        if (backgroundImage != null) {
            root.setBackground(new Background(backgroundImage));
        } else {
            // Set a default background if none is selected or available
            root.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }
}
