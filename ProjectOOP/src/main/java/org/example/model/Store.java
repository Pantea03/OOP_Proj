package org.example.model;


import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.example.menuController.LoginMenuController;
import org.example.menuController.MainMenuController;
public class Store {
    // Display all cards available in the game that the player does not own
    public static List<Card> getAvailableCards(Connection conn, User user) throws SQLException {
        String sql = "SELECT * FROM cards WHERE name NOT IN (SELECT card_name FROM user_cards WHERE user_id = ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, user.getUserId());
            try (ResultSet rs = stmt.executeQuery()) {
                List<Card> availableCards = new ArrayList<>();
                while (rs.next()) {
                    Card card = new Card();
                    card.cardId = rs.getInt("card_id");
                    card.name = rs.getString("name");
                    card.cost = rs.getInt("cost");
                    card.upgradeCost = rs.getInt("upgrade_cost");
                    card.upgradeLevel = rs.getInt("upgrade_level");
                    card.duration = rs.getInt("duration");
                    card.character = rs.getString("character");
                    card.playerDamage = rs.getInt("player_damage");
                    card.cardDefenseAttack = rs.getInt("card_defense_attack");
                    card.upgradedLevel = rs.getInt("upgraded_level") + 1; // Start from 1
                    card.execution = rs.getString("execution");
                    availableCards.add(card);

                }
                return availableCards;
            }
        }
    }
    public static List<Card> getAllCards(Connection conn) throws SQLException {
        String sql = "SELECT * FROM cards";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<Card> availableCards = new ArrayList<>();
            while (rs.next()) {
                Card card = new Card();
                card.cardId = (rs.getInt("card_id"));
                card.setName(rs.getString("name"));
                card.setCost(rs.getInt("cost"));
                card.setUpgradeCost(rs.getInt("upgrade_cost"));
                card.setUpgradeLevel(rs.getInt("upgrade_level"));
                card.setDuration(rs.getInt("duration"));
                card.setCharacter(rs.getString("character"));
                card.setPlayerDamage(rs.getInt("player_damage"));
                card.setCardDefenseAttack(rs.getInt("card_defense_attack"));
                card.upgradedLevel = (rs.getInt("upgraded_level") + 1); // Start from 1
                card.execution = (rs.getString("execution"));
                availableCards.add(card);
            }
            return availableCards;
        }
    }


    // Buy a new card by name
    public static boolean buyCardByName(Connection conn, String cardName) throws SQLException {
        String cardQuery = "SELECT * FROM cards WHERE name = ?";
        String playerQuery = "SELECT coins FROM user WHERE user_id = ?";
        String insertCard = "INSERT INTO user_cards (user_id, card_name, cost, upgrade_cost, upgrade_level, player_damage, card_defence_attack, duration, execution, `character`, upgraded_level) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String updateCoins = "UPDATE user SET coins = coins - ? WHERE user_id = ?";

        try (PreparedStatement cardStmt = conn.prepareStatement(cardQuery);
             PreparedStatement playerStmt = conn.prepareStatement(playerQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertCard);
             PreparedStatement updateStmt = conn.prepareStatement(updateCoins)) {

            // Get card details
            cardStmt.setString(1, cardName);
            ResultSet cardRs = cardStmt.executeQuery();
            if (cardRs.next()) {
                int cardPrice = cardRs.getInt("cost");

                // Get player's coins
                playerStmt.setInt(1, LoginMenuController.currentUser.getUserId());
                ResultSet playerRs = playerStmt.executeQuery();
                if (playerRs.next()) {
                    int playerCoins = playerRs.getInt("coins");

                    // Check if player has enough coins to buy the card
                    if (playerCoins >= cardPrice) {
                        // Insert into user_cards
                        insertStmt.setInt(1, LoginMenuController.currentUser.getUserId());
                        insertStmt.setString(2, cardName);
                        insertStmt.setInt(3, cardPrice);
                        insertStmt.setInt(4, cardRs.getInt("upgrade_cost"));
                        insertStmt.setInt(5, cardRs.getInt("upgrade_level"));
                        insertStmt.setInt(6, cardRs.getInt("player_damage"));
                        insertStmt.setInt(7, cardRs.getInt("card_defense_attack"));
                        insertStmt.setInt(8, cardRs.getInt("duration"));
                        insertStmt.setString(9, cardRs.getString("execution"));
                        insertStmt.setString(10, cardRs.getString("character"));
                        insertStmt.setInt(11, cardRs.getInt("upgraded_level"));
                        insertStmt.executeUpdate();

                        // Update player's coins
                        updateStmt.setInt(1, cardPrice);
                        updateStmt.setInt(2, LoginMenuController.currentUser.getUserId());
                        updateStmt.executeUpdate();

                        LoginMenuController.currentUser.updateFromDatabase(conn);
                        return true; // Purchase successful
                    } else {
                        System.out.println("Insufficient coins to buy the card.");
                    }
                }
            }
        }
        return false; // Purchase unsuccessful
    }



    public static List<Card> getUserCards(Connection conn) throws SQLException {
        // SQL query to retrieve user cards
        String sql = "SELECT uc.user_card_id, uc.card_name, uc.upgraded_level, c.cost, c.upgrade_cost, c.upgrade_level, c.name, c.player_damage, c.card_defense_attack, c.duration, c.execution, c.character " +
                "FROM user_cards uc JOIN cards c ON uc.card_name = c.name " +
                "WHERE uc.user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int userId = LoginMenuController.currentUser.getUserId();
            stmt.setInt(1, userId);  // Set the user ID

            try (ResultSet rs = stmt.executeQuery()) {
                List<Card> userCards = new ArrayList<>();
                while (rs.next()) {
                    int currentUpgradedLevel = rs.getInt("upgraded_level");
                    int initialUpgradeCost = rs.getInt("upgrade_cost");
                    double multiplier = 1.0 + (0.25 * currentUpgradedLevel);
                    int nextUpgradeCost = (int) (initialUpgradeCost * multiplier);

                        // Create a NormalCard object and set its attributes
                    Card card = new Card();
                    card.cardId = rs.getInt("user_card_id");
                    card.name = rs.getString("card_name");
                    card.cost = rs.getInt("cost");
                    card.upgradeCost = nextUpgradeCost; // Cost for next upgrade
                    card.upgradeLevel = rs.getInt("upgrade_level");
                    card.duration = rs.getInt("duration");
                    card.character = rs.getString("character");
                    card.playerDamage = rs.getInt("player_damage");
                    card.cardDefenseAttack = rs.getInt("card_defense_attack");
                    card.upgradedLevel = currentUpgradedLevel + 1; // Current level of the card
                    card.execution = rs.getString("execution");
                    userCards.add(card);

                }
                LoginMenuController.currentUser.updateFromDatabase(conn);
                return userCards;
            }
        }
    }




    // Method to calculate absolute value
    public static int abser(int a) {
        if (a > 0) {
            return a;
        } else {
            return 0;
        }
    }

    // Method to upgrade a card by name for the current user
    public static boolean upgradeCard(Connection conn, String cardName) throws SQLException {
        String userCardQuery = "SELECT * FROM user_cards WHERE user_id = ? AND card_name = ?";
        String updateCard = "UPDATE user_cards SET upgrade_level = ?, " +
                "upgraded_level = upgraded_level + 1, " +
                "card_defence_attack = ?, " +
                "player_damage = ? " +
                "WHERE user_id = ? AND card_name = ?";
        String updateCoins = "UPDATE user SET coins = coins - ? WHERE user_id = ?";

        try (PreparedStatement userCardStmt = conn.prepareStatement(userCardQuery);
             PreparedStatement updateCardStmt = conn.prepareStatement(updateCard);
             PreparedStatement updateCoinsStmt = conn.prepareStatement(updateCoins)) {

            // Fetch user card information by user_id and card_name
            userCardStmt.setInt(1, LoginMenuController.currentUser.getUserId());
            userCardStmt.setString(2, cardName);
            ResultSet userCardRs = userCardStmt.executeQuery();

            if (userCardRs.next()) {
                int currentUpgradedLevel = userCardRs.getInt("upgraded_level");
                int currentCardDefenseAttack = userCardRs.getInt("card_defence_attack");
                int currentPlayerDamage = userCardRs.getInt("player_damage");
                int initialUpgradeCost = userCardRs.getInt("upgrade_cost");
                int upgradeLevelRequired = userCardRs.getInt("upgrade_level");

                // Fetch player information
                int playerCoins = LoginMenuController.currentUser.getCoin();
                int playerLevel = LoginMenuController.currentUser.getLevel();

                // Adjust upgrade level required to start from 1
                if (upgradeLevelRequired >= 1) {
                    upgradeLevelRequired--; // Adjust upgrade level requirement
                }


                // Calculate the current upgrade cost
                int currentUpgradeCost = (int) (initialUpgradeCost * Math.pow(1.25, currentUpgradedLevel));

                // Check if player can afford the upgrade and meets level requirements
                if (playerCoins >= currentUpgradeCost && playerLevel >= upgradeLevelRequired) {
                    // Calculate new values for card_defense_attack and player_damage
                    int newCardDefenseAttack = 100 - abser(90 - currentCardDefenseAttack);
                    int newPlayerDamage = 50 - abser(40 - currentPlayerDamage);

                    // Calculate new upgrade level
                    double upgradeFactor = 0.01 * currentUpgradedLevel * currentUpgradedLevel;
                    int newUpgradeLevel = (int) Math.ceil(upgradeFactor);

                    // Update the card's upgrade level, upgraded level, card_defense_attack, and player_damage
                    updateCardStmt.setInt(1, newUpgradeLevel);
                    updateCardStmt.setInt(2, newCardDefenseAttack);
                    updateCardStmt.setInt(3, newPlayerDamage);
                    updateCardStmt.setInt(4, LoginMenuController.currentUser.getUserId());
                    updateCardStmt.setString(5, cardName);
                    updateCardStmt.executeUpdate();

                    // Deduct the coins
                    updateCoinsStmt.setInt(1, currentUpgradeCost);
                    updateCoinsStmt.setInt(2, LoginMenuController.currentUser.getUserId());
                    updateCoinsStmt.executeUpdate();
                    LoginMenuController.currentUser.updateFromDatabase(conn);
                    return true; // Upgrade successful
                } else if (playerLevel < upgradeLevelRequired) {
                    System.out.println("Insufficient levels to upgrade the card.");
                } else {
                    System.out.println("Insufficient coins to upgrade the card.");
                }
            }
        }
        return false; // Upgrade unsuccessful
    }




    // Method to add predefined cards to the database
    public static void getPredefinedCards(Connection conn) throws SQLException {
        // SQL query to insert a card into the cards table
        String insertCardQuery = "INSERT INTO cards (name, cost, upgrade_cost, upgrade_level, player_damage, card_defense_attack, duration, execution, `character`, upgraded_level) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        ArrayList<Integer> defenseAttacks = new ArrayList<>();
        ArrayList<Integer> playerDamages = new ArrayList<>();
        ArrayList<Integer> durations = new ArrayList<>();
        ArrayList<String> characters = new ArrayList<>();
        for(int i =1;i<21;i++){
            defenseAttacks.add(10+((i*2)%91));
            durations.add((i%5) +1);
            playerDamages.add(player_damage_randomizer(i%5+1));
            characters.add(getCharacter(i%4));
        }
        try (PreparedStatement insertCardStmt = conn.prepareStatement(insertCardQuery)) {
            // Define the cards with their respective attributes
            String a = String.valueOf(Integer.MAX_VALUE);
            String[][] predefinedCards = {
                    {"Shield", "300", "100", "5", "0", a, "1", "Breaks any card it encounters", "KIM"},
                    {"Heal", "200", "50", "1", "0", "0", "1", "Adds HP to the player", "Healer"},
                    {"Buffer", "200", "50", "1", "0", "0", "1", "Enhances a random played card", "Enhancer"},
                    {"Move Ditch", "200", "50", "1", "0", "0", "1", "Changes the position of destroyed blocks randomly", "Changer"},
                    {"Repair", "200", "50", "1", "0", "0", "1", "Repairs the pits", "Repairer"},
                    {"Round Reducer", "200", "50", "1", "0", "0", "5", "Reduces one round", "Reducer"},
                    {"Card Thief", "300", "50", "1", "0", "0", "1", "Moves a random opponent card to the player", "Remover"},
                    {"Weakener", "200", "50", "1", "0", "0", "1", "Weakens two random opponent cards", "Weakener"},
                    {"Copy Card", "200", "50", "1", "0", "0", "1", "Copies another player's card", "Copier"},
                    {"Invisibility", "500", "50", "1", "0", "0", "1", "Grants temporary invisibility", "Invisible"}
            };

            // Insert predefined cards into the cards table
            for (String[] card : predefinedCards) {
                insertCard(insertCardStmt, card, 0);
            }

            // Unique names for dynamically generated cards
            String[] dynamicCardNames = {
                    "Blazing Arrow", "Thunder Strike", "Shadow Assassin", "Crystal Barrier",
                    "Mystic Healer", "Storm Bringer", "Earth Shaker", "Phoenix Flame",
                    "Ice Guardian", "Spirit Walker", "Dragon Slayer", "Wind Dancer",
                    "Arcane Sorcerer", "Vortex Weaver", "Lunar Priestess", "Solar Knight",
                    "Chaos Warden", "Harmony Seeker", "Frostbite", "Inferno Blade"
            };

            // Generate and insert additional cards with attributes from the ArrayLists
            for (int i = 0; i < 20; i++) {
                String[] dynamicCard = {
                        dynamicCardNames[i], // unique name
                        String.valueOf(30 + (i * 50)), // Example cost
                        "500", // Example initial upgrade cost
                        "2", // Example upgrade level required
                        String.valueOf(playerDamages.get(i)), // player damage
                        String.valueOf(defenseAttacks.get(i)), // card defense attack
                        String.valueOf(durations.get(i)), // duration
                        null, // Example execution
                        characters.get(i) // character
                };
                insertCard(insertCardStmt, dynamicCard, 0);
            }
        }
    }
    private static int player_damage_randomizer(int duration) {
        // 10-50
    Random rand = new Random();
    int low = 10 / duration;
    int high = 50 / duration;
    int randomNum = rand.nextInt((high - low) + 1) + low;
    return randomNum * duration;
    }
    private static String getCharacter(int i) {
        return switch (i) {
            case 0 -> "KIM";
            case 1 -> "KAI";
            case 2 -> "PAN";
            default -> "PAR";
        };
    }
    // Helper method to insert a card into the database
    private static void insertCard(PreparedStatement stmt, String[] card, int upgradedLevel) throws SQLException {
        int initialUpgradeCost = Integer.parseInt(card[2]); // initial upgrade cost

        // Calculate the upgrade cost based on the initial upgrade cost and upgraded level
        double multiplier = 1.0 + (0.25 * upgradedLevel);
        int upgradeCost = (int) (initialUpgradeCost * multiplier);

        stmt.setString(1, card[0]); // name
        stmt.setInt(2, Integer.parseInt(card[1])); // cost
        stmt.setInt(3, upgradeCost); // calculated upgrade cost
        stmt.setInt(4, Integer.parseInt(card[3])); // upgrade level
        stmt.setInt(5, Integer.parseInt(card[4])); // player_damage
        stmt.setInt(6, Integer.parseInt(card[5])); // card_defense_attack
        stmt.setInt(7, Integer.parseInt(card[6])); // duration
        stmt.setString(8, card[7]); // execution
        stmt.setString(9, card[8]); // character
        stmt.setInt(10, upgradedLevel); // upgraded_level

        stmt.executeUpdate();
    }

    // Method to check if predefined cards exist in the database
    public static boolean arePredefinedCardsExist(Connection conn) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM cards WHERE upgraded_level = 0";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                int count = rs.getInt("count");
                return count > 0;
            }
        }
        return false;
    }

}
