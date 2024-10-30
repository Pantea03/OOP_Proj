//package org.example.model;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//import org.example.menuController.MainMenuController;
//
//public class AdminMenu {
//    public static void execute(Scanner scanner) throws SQLException {
//        String input;
//        while (true) {
//            input = scanner.nextLine(); // Wait for user input
//            if (input.matches("add card")) {
//                ArrayList<String> info = new ArrayList<>();
//                Connection conn = DatabaseUtil.getConnection();
//
//                do {
//                    System.out.println("Enter a valid card name:");
//                    input = scanner.nextLine();
//                } while (!isValid(input, 1, conn));
//                info.add(input.trim());
//
//                do {
//                    System.out.println("Enter a valid card defense/attack:");
//                    input = scanner.nextLine();
//                } while (!isValid(input, 2, conn));
//                info.add(input.trim());
//
//                do {
//                    System.out.println("Enter a valid card duration:");
//                    input = scanner.nextLine();
//                } while (!isValid(input, 3, conn));
//                info.add(input.trim());
//
//                do {
//                    System.out.println("Enter a valid card player damage:");
//                    input = scanner.nextLine();
//                } while (!isValid(input, 4, conn));
//                info.add(input.trim());
//
//                do {
//                    System.out.println("Enter a valid card upgrade level:");
//                    input = scanner.nextLine();
//                } while (!isValid(input, 5, conn));
//                info.add(input.trim());
//
//                System.out.println("Enter a valid card upgrade cost:");
//                input = scanner.nextLine();
//                info.add(input.trim());
//
//                System.out.println("Enter a valid card cost:");
//                input = scanner.nextLine();
//                info.add(input.trim());
//
//                System.out.println("Enter a valid character:");
//                input = scanner.nextLine();
//                info.add(input.trim());
//
//                addNormalCard(conn, info);
//            } else if (input.matches("edit card")) {
//                editCard(scanner, DatabaseUtil.getConnection());
//            } else if (input.matches("delete card")) {
//                deleteCard(scanner, DatabaseUtil.getConnection());
//            } else if (input.matches("back")) {
//                InputProcessor.currentMenu.name = "main";
//                break;
//            } else if (input.matches("view players")) {
//                viewPlayers(DatabaseUtil.getConnection());
//            }
//            else {
//                System.out.println("Invalid command. Please enter a valid command.");
//            }
//        }
//    }
//
//
//    public static boolean isValid(String cmd, int type, Connection conn) throws SQLException {
//        switch (type) {
//            case 1: // name
//                cmd = cmd.trim();
//                if (isRepetitive(cmd, conn)) {
//                    System.out.println("Error: The card name is already taken. Please choose a different name.");
//                    return false;
//                } else {
//                    return true;
//                }
//            case 2: // card defense attack
//                try {
//                    int value = Integer.parseInt(cmd);
//                    if (value > 9 && value < 101) {
//                        return true;
//                    } else {
//                        System.out.println("Error: Card defense/attack must be between 10 and 100.");
//                        return false;
//                    }
//                } catch (NumberFormatException e) {
//                    System.out.println("Error: Card defense/attack must be a valid number.");
//                    return false;
//                }
//            case 3: // duration
//                try {
//                    int value = Integer.parseInt(cmd);
//                    if (value > 0 && value < 6) {
//                        return true;
//                    } else {
//                        System.out.println("Error: Duration must be between 1 and 5.");
//                        return false;
//                    }
//                } catch (NumberFormatException e) {
//                    System.out.println("Error: Duration must be a valid number.");
//                    return false;
//                }
//            case 4: // player damage
//                try {
//                    int value = Integer.parseInt(cmd);
//                    if (value > 9 && value < 51) {
//                        return true;
//                    } else {
//                        System.out.println("Error: Player damage must be between 10 and 50.");
//                        return false;
//                    }
//                } catch (NumberFormatException e) {
//                    System.out.println("Error: Player damage must be a valid number.");
//                    return false;
//                }
//            case 5: // upgrade level
//                try {
//                    int value = Integer.parseInt(cmd);
//                    if (value > 0) {
//                        return true;
//                    } else {
//                        System.out.println("Error: Upgrade level must be greater than 0.");
//                        return false;
//                    }
//                } catch (NumberFormatException e) {
//                    System.out.println("Error: Upgrade level must be a valid number.");
//                    return false;
//                }
//            case 6: // general case
//                return true;
//            default:
//                return false;
//        }
//    }
//
//    public static boolean isRepetitive(String name, Connection conn) throws SQLException {
//        String query = "SELECT COUNT(*) FROM cards WHERE name = ?";
//        try (PreparedStatement stmt = conn.prepareStatement(query)) {
//            stmt.setString(1, name);
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (rs.next() && rs.getInt(1) > 0) {
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        }
//    }
//
//    public static void addNormalCard(Connection conn, ArrayList<String> cardInfo) throws SQLException {
//        // SQL query to insert a normal card into the cards table
//        String insertCardQuery = "INSERT INTO cards (name, cost, upgrade_cost, upgrade_level, player_damage, card_defense_attack, duration, execution, `character`, upgraded_level) " +
//                "VALUES (?, ?, ?, ?, ?, ?, ?, NULL, ?, 0)";
//
//        try (PreparedStatement insertCardStmt = conn.prepareStatement(insertCardQuery)) {
//            // Set the parameters for the prepared statement using the provided cardInfo
//            insertCardStmt.setString(1, cardInfo.get(0)); // name
//            insertCardStmt.setInt(2, Integer.parseInt(cardInfo.get(1))); // cost
//            insertCardStmt.setInt(3, Integer.parseInt(cardInfo.get(2))); // upgrade_cost
//            insertCardStmt.setInt(4, Integer.parseInt(cardInfo.get(3))); // upgrade_level
//            insertCardStmt.setInt(5, Integer.parseInt(cardInfo.get(4))); // player_damage
//            insertCardStmt.setInt(6, Integer.parseInt(cardInfo.get(5))); // card_defense_attack
//            insertCardStmt.setInt(7, Integer.parseInt(cardInfo.get(6))); // duration
//            insertCardStmt.setString(8, cardInfo.get(7)); // character
//
//            // Execute the insert statement
//            insertCardStmt.executeUpdate();
//
//            System.out.println("Normal card added successfully!");
//        } catch (SQLException e) {
//            System.err.println("Error while adding normal card: " + e.getMessage());
//            throw e; // Re-throw the exception for further handling if necessary
//        }
//    }
//
//    public static List<Card> getAllNormalCards(Connection conn) throws SQLException {
//        String sql = "SELECT * FROM cards WHERE execution IS NULL";
//        try (PreparedStatement stmt = conn.prepareStatement(sql);
//             ResultSet rs = stmt.executeQuery()) {
//            List<Card> allCards = new ArrayList<>();
//            while (rs.next()) {
//                Card card = new Card();
//                card.cardId = rs.getInt("card_id");
//                card.name = rs.getString("name");
//                card.cost = rs.getInt("cost");
//                card.upgradeCost = rs.getInt("upgrade_cost");
//                card.upgradeLevel = rs.getInt("upgrade_level");
//                card.playerDamage = rs.getInt("player_damage");
//                card.cardDefenseAttack = rs.getInt("card_defense_attack");
//                card.duration = rs.getInt("duration");
//                card.character = rs.getString("character");
//                card.upgradedLevel = rs.getInt("upgraded_level");
//                allCards.add(card);
//            }
//            return allCards;
//        }
//    }
//
//
//    public static void editCard(Scanner scanner, Connection conn) throws SQLException {
//        List<Card> allCards = getAllNormalCards(conn);
//
//        // Display all cards
//        System.out.println("All available cards:");
//        for (int i = 0; i < allCards.size(); i++) {
//            System.out.println((i + 1) + ". " + allCards.get(i).getName());
//        }
//
//        System.out.println("Enter the number of the card you want to edit, or 'back' to return:");
//        String input = scanner.nextLine().trim();
//        if (input.equalsIgnoreCase("back")) {
//            return;
//        }
//
//        int cardIndex;
//        try {
//            cardIndex = Integer.parseInt(input) - 1;
//            if (cardIndex < 0 || cardIndex >= allCards.size()) {
//                System.out.println("Invalid card number. Returning to previous menu.");
//                return;
//            }
//        } catch (NumberFormatException e) {
//            System.out.println("Invalid input. Returning to previous menu.");
//            return;
//        }
//
//        Card selectedCard = allCards.get(cardIndex);
//        System.out.println("Selected card: " + selectedCard.getName());
//
//        boolean editing = true;
//        while (editing) {
//            System.out.println("Current card details:");
//            System.out.println("1. Name: " + selectedCard.getName());
//            System.out.println("2. Card defense/attack: " + selectedCard.getCardDefenseAttack());
//            System.out.println("3. Duration: " + selectedCard.getDuration());
//            System.out.println("4. Player damage: " + selectedCard.getPlayerDamage());
//            System.out.println("5. Upgrade level: " + selectedCard.getUpgradeLevel());
//            System.out.println("6. Upgrade cost: " + selectedCard.getUpgradeCost());
//            System.out.println("7. Cost: " + selectedCard.getCost());
//            System.out.println("8. Character: " + selectedCard.getCharacter());
//            System.out.println("Enter the number of the field you want to edit, or 'back' to return:");
//
//            input = scanner.nextLine().trim();
//            if (input.equalsIgnoreCase("back")) {
//                return;
//            }
//
//            int fieldNumber;
//            try {
//                fieldNumber = Integer.parseInt(input);
//            } catch (NumberFormatException e) {
//                System.out.println("Invalid input. Returning to previous menu.");
//                return;
//            }
//
//            System.out.println("Enter the new value:");
//            String newValue = scanner.nextLine().trim();
//            boolean valid = false;
//
//            switch (fieldNumber) {
//                case 1: // Name
//                    if (isValid(newValue, 1, conn)) {
//                        selectedCard.setName(newValue);
//                        valid = true;
//                    }
//                    break;
//                case 2: // Card defense/attack
//                    if (isValid(newValue, 2, conn)) {
//                        selectedCard.setCardDefenseAttack(Integer.parseInt(newValue));
//                        valid = true;
//                    }
//                    break;
//                case 3: // Duration
//                    if (isValid(newValue, 3, conn)) {
//                        selectedCard.setDuration(Integer.parseInt(newValue));
//                        valid = true;
//                    }
//                    break;
//                case 4: // Player damage
//                    if (isValid(newValue, 4, conn)) {
//                        selectedCard.setPlayerDamage(Integer.parseInt(newValue));
//                        valid = true;
//                    }
//                    break;
//                case 5: // Upgrade level
//                    if (isValid(newValue, 5, conn)) {
//                        selectedCard.setUpgradeLevel(Integer.parseInt(newValue));
//                        valid = true;
//                    }
//                    break;
//                case 6: // Upgrade cost
//                    selectedCard.setUpgradeCost(Integer.parseInt(newValue));
//                    valid = true;
//                    break;
//                case 7: // Cost
//                    selectedCard.setCost(Integer.parseInt(newValue));
//                    valid = true;
//                    break;
//                case 8: // Character
//                    selectedCard.setCharacter(newValue);
//                    valid = true;
//                    break;
//                default:
//                    System.out.println("Invalid field number. Returning to previous menu.");
//                    return;
//            }
//
//            if (valid) {
//                System.out.println("Field updated successfully. Do you want to continue editing? (y/n)");
//                input = scanner.nextLine().trim();
//                if (!input.equalsIgnoreCase("y")) {
//                    editing = false;
//                }
//            } else {
//                System.out.println("Invalid input. Please try again.");
//            }
//        }
//
//        System.out.println("Are you sure you want to save the changes? (y/n)");
//        input = scanner.nextLine().trim();
//        if (input.equalsIgnoreCase("y")) {
//            updateCardInDatabase(conn, selectedCard);
//            System.out.println("Card successfully edited.");
//        } else {
//            System.out.println("Changes discarded. Returning to previous menu.");
//        }
//    }
//
//    public static void updateCardInDatabase(Connection conn, Card card) throws SQLException {
//        String updateQuery = "UPDATE cards SET name = ?, cost = ?, upgrade_cost = ?, upgrade_level = ?, player_damage = ?, card_defense_attack = ?, duration = ?, `character` = ? WHERE card_id = ?";
//        try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
//            stmt.setString(1, card.getName());
//            stmt.setInt(2, card.getCost());
//            stmt.setInt(3, card.getUpgradeCost());
//            stmt.setInt(4, card.getUpgradeLevel());
//            stmt.setInt(5, card.getPlayerDamage());
//            stmt.setInt(6, card.getCardDefenseAttack());
//            stmt.setInt(7, card.getDuration());
//            stmt.setString(8, card.getCharacter());
//            stmt.setInt(9, card.getCardId());
//            stmt.executeUpdate();
//        }
//    }
//
//    // Deleting a selected card
//    public static void deleteCard(Scanner scanner, Connection conn) throws SQLException {
//        List<Card> allCards = getAllNormalCards(conn);
//
//        // Display all cards
//        System.out.println("All available cards:");
//        for (int i = 0; i < allCards.size(); i++) {
//            System.out.println((i + 1) + ". " + allCards.get(i).getName());
//        }
//
//        System.out.println("Enter the number of the card you want to delete, or 'back' to return:");
//        String input = scanner.nextLine().trim();
//        if (input.equalsIgnoreCase("back")) {
//            return;
//        }
//
//        int cardIndex;
//        try {
//            cardIndex = Integer.parseInt(input) - 1;
//            if (cardIndex < 0 || cardIndex >= allCards.size()) {
//                System.out.println("Input Invalid. Returning to previous menu.");
//                return;
//            }
//        } catch (NumberFormatException e) {
//            System.out.println("Input Invalid. Returning to previous menu.");
//            return;
//        }
//
//        Card selectedCard = allCards.get(cardIndex);
//        System.out.println("Are you sure you want to delete this card? (y/n)");
//        input = scanner.nextLine().trim();
//        if (input.equalsIgnoreCase("y")) {
//            deleteCardFromDatabase(conn, selectedCard);
//            System.out.println("Card successfully deleted.");
//        } else {
//            System.out.println("Deletion canceled. Returning to previous menu.");
//        }
//    }
//
//    // Deleting the card from the database
//    public static void deleteCardFromDatabase(Connection conn, Card card) throws SQLException {
//        String deleteQuery = "DELETE FROM cards WHERE card_id = ?";
//        try (PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
//            stmt.setInt(1, card.getCardId());
//            stmt.executeUpdate();
//        }
//
//    }
//    // Function to view all players and their details
//    public static void viewPlayers(Connection conn) throws SQLException {
//        String query = "SELECT username, level, coins FROM user";
//        try (PreparedStatement stmt = conn.prepareStatement(query)) {
//            ResultSet rs = stmt.executeQuery();
//            System.out.println("Players:");
//            System.out.printf("%-20s %-10s %-10s\n", "Username", "Level", "Coins");
//            while (rs.next()) {
//                String username = rs.getString("username");
//                int level = rs.getInt("level");
//                int coins = rs.getInt("coins");
//                System.out.printf("%-20s %-10d %-10d\n", username, level, coins);
//            }
//        }
//    }
//}
