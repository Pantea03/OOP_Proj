package org.example.menuController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.example.view.View;
import org.example.view.ViewSwitcher;

public class CharacterSelectionController {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button kimButton;
    @FXML
    private Button panButton;
    @FXML
    private Button parButton;
    @FXML
    private Button kaiButton;
    @FXML
    private Text playerOneSelection;
    @FXML
    private Text playerTwoSelection;

    private boolean isPlayerOneTurn = true;

    @FXML
    public void initialize() {
        // Initialize any necessary data here

    }

    @FXML
    public void handleCharacterSelection(MouseEvent event) {
        Button selectedButton = (Button) event.getSource();
        String selectedCharacter = selectedButton.getText();

        if (isPlayerOneTurn) {
            playerOneSelection.setText("Player 1: " + selectedCharacter);
            isPlayerOneTurn = false;
        } else {
            playerTwoSelection.setText("Player 2: " + selectedCharacter);
            // Start the game with selected characters
            startGame();
        }
    }

    private void startGame() {
        // Logic to start the game with selected characters
        ViewSwitcher.switchTo(View.GAME_VIEW);
    }
}
