package org.example.menuController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.example.model.Card;
import org.example.model.DatabaseUtil;
import org.example.model.Store;
import org.example.model.User;
import org.example.view.View;
import org.example.view.ViewSwitcher;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class StoreController {
    @FXML
    private ListView<Card> availableCardsList;
    @FXML
    private ListView<Card> userCardsList;
    @FXML
    private Label cardDetailsLabel;
    @FXML
    private ImageView cardImageView;
    @FXML
    private Button buyButton;
    @FXML
    private Button upgradeButton;
    @FXML
    private AnchorPane rootPane;

    private ObservableList<Card> availableCards = FXCollections.observableArrayList();
    private ObservableList<Card> userCards = FXCollections.observableArrayList();

    static User currentUser; // Assuming you have a way to set the current user

    @FXML
    public void initialize() {
        // Initialize the currentUser before calling any method that uses it
        currentUser = LoginMenuController.currentUser; // Add your method to get the current user

        if (currentUser == null) {
            System.err.println("Error: currentUser is null.");
            return;
        }

        // Initialize list views
        availableCardsList.setItems(availableCards);
        userCardsList.setItems(userCards);

        // Set listeners
        availableCardsList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showCardDetails(newValue)
        );

        userCardsList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showCardDetails(newValue)
        );

        // Load cards
        loadAvailableCards();
        loadUserCards();

        // Set initial card details view
        showCardDetails(null);

        // Set background
        setPaneBackground();
    }

    private void loadAvailableCards() {
        try (Connection conn = DatabaseUtil.getConnection()) {
            List<Card> cards = Store.getAvailableCards(conn, currentUser);
            availableCards.addAll(cards);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection or query errors
        }
    }

    private void loadUserCards() {
        try (Connection conn = DatabaseUtil.getConnection()) {
            List<Card> cards = Store.getUserCards(conn);
            userCards.addAll(cards);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection or query errors
        }
    }

    private void showCardDetails(Card card) {
        if (card != null) {
            cardDetailsLabel.setText(card.toString()); // Customize this to display relevant card details
            // Load and display card image
            try {
                String imagePath = "/cards/" + card.getName() + ".jpg"; // Example path
                Image cardImage = new Image(getClass().getResourceAsStream(imagePath));
                cardImageView.setImage(cardImage);
            } catch (Exception e) {
                e.printStackTrace();
                // Handle image loading errors
            }
            // Enable/disable buttons based on conditions (e.g., user's coins, card ownership)
            updateButtonStates(card);
        } else {
            cardDetailsLabel.setText("");
            cardImageView.setImage(null);
            buyButton.setDisable(true);
            upgradeButton.setDisable(true);
        }
    }

    private void updateButtonStates(Card card) {
        if (card != null) {
            boolean canBuy = currentUser.getCoin() >= card.getCost();
            boolean canUpgrade = /* Implement logic to check if user can upgrade this card */ true;

            buyButton.setDisable(!canBuy);
            upgradeButton.setDisable(!canUpgrade);
        }
    }

    @FXML
    private void buyCard(MouseEvent event) {
        Card selectedCard = availableCardsList.getSelectionModel().getSelectedItem();
        if (selectedCard != null) {
            try (Connection conn = DatabaseUtil.getConnection()) {
                boolean success = Store.buyCardByName(conn, selectedCard.getName());
                if (success) {
                    // Update UI after successful purchase
                    availableCards.remove(selectedCard);
                    userCards.add(selectedCard);
                    showCardDetails(null); // Clear details after purchase
                } else {
                    // Handle unsuccessful purchase (e.g., insufficient coins)
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database connection or query errors
            }
        }
    }

    @FXML
    private void upgradeCard(MouseEvent event) {
        Card selectedCard = userCardsList.getSelectionModel().getSelectedItem();
        if (selectedCard != null) {
            try (Connection conn = DatabaseUtil.getConnection()) {
                boolean success = Store.upgradeCard(conn, selectedCard.getName());
                if (success) {
                    // Update UI after successful upgrade
                    showCardDetails(selectedCard); // Refresh card details after upgrade
                } else {
                    // Handle unsuccessful upgrade (e.g., insufficient coins or level)
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database connection or query errors
            }
        }
    }

    @FXML
    private void setPaneBackground() {
        // Implement your background setting logic here
        // Example:
        String backgroundFile = "/images/mainMenu.jpg";
        Image backgroundImage = new Image(getClass().getResourceAsStream(backgroundFile));
        BackgroundImage bImage = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        Background background = new Background(bImage);
        rootPane.setBackground(background);
    }

    @FXML
    private void handleBackButtonClick() {
        // Switch to the previous menu or main menu
        ViewSwitcher.switchTo(View.MAIN_MENU);
    }
}
