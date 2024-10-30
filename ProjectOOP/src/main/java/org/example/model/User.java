package org.example.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.example.menuController.MainMenuController;
public class User {
    private int userId;
    private String username;
    private String password;
    private String nickname;
    private String securityQuestion;
    private String securityAnswer;
    private String email;
    private boolean loggedIn;

    // *************************************


    // Constructor
    public User(String username, String password, String nickname, String email, String securityQuestion, String securityAnswer) throws SQLException {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
    }
    public User() {
    }

    // Getters for each field
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    //     Save user to the database
    public void save(Connection conn) throws SQLException {
        String sql = "INSERT INTO USER (username, password, nickname, email, security_question, security_answer) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, this.username);
            stmt.setString(2, this.password);
            stmt.setString(3, this.nickname);
            stmt.setString(4, this.email);
            stmt.setString(5, this.securityQuestion);
            stmt.setString(6, this.securityAnswer);
            stmt.executeUpdate();
        }
    }

    public void updateLoggedInState(Connection conn, boolean loggedIn) throws SQLException {
        String sql = "UPDATE USER SET logged_in = ? WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, loggedIn);
            stmt.setInt(2, this.userId);
            stmt.executeUpdate();
            this.loggedIn = loggedIn;
        }
    }

    public boolean isLoggedIn(Connection conn) throws SQLException {
        String sql = "SELECT logged_in FROM USER WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, this.userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("logged_in");
                }
            }
        }
        return false;
    }

    // Static method to fetch model.User by username
    public static User fetchByUsername(Connection conn, String username) throws SQLException {
        String sql = "SELECT * FROM USER WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.userId = rs.getInt("user_id");
                    user.username = rs.getString("username");
                    user.loggedIn = rs.getBoolean("logged_in");
                    user.password = rs.getString("password");
                    user.nickname = rs.getString("nickname");
                    user.email = rs.getString("email");
                    user.securityQuestion = rs.getString("security_question");
                    user.securityAnswer = rs.getString("security_answer");
                    return user;
                }
            }
        }
        return null;
    }



    //////////////////////////////////////////
    ////////////////////

    private int HP,XP,Level, Coin, damage, hands_played=0, playerPoints=0 ,maxXp=100, maxHP=100;
    private String Character;
//    private Clan myClan;
//    private ArrayList<Card> ownedCards = new ArrayList<Card>(), Deck = new ArrayList<Card>(), Hand = new ArrayList<Card>();

    public void chooseDeck(){

    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }
    public int getXP() {
        return XP;
    }

    public void setXP(int XP) {
        this.XP = HP;
    }

//    public int getOrder(Game game) {
//        if(game.player2.equals(this)){
//            return 2;
//        }
//        else{
//            return 1;
//        }
//    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }

    public int getCoin() {
        return Coin;
    }

    public void setCoin(int coin) {
        Coin = coin;
    }

    //    public void levelUp(){
//        this.Coin+=100*this.Level;
//        this.Level+=1;
//    }
    public void leveledUp(){
        if(this.XP >= this.maxXp){
            this.Coin+=100*this.Level;
            this.maxHP += 10*this.Level;
            this.Level+=1;
            this.maxXp = this.Level*50*(this.Level+1);
        }
    }

    public String getCharacter() {
        return Character;
    }

    public void setCharacter(String character) {
        Character = character;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getHands_played() {
        return hands_played;
    }

    public void setHands_played(int hands_played) {
        this.hands_played = hands_played;
    }

    //    public ArrayList<Card> getDeck() {
//        return Deck;
//    }
//
//    public void setDeck(ArrayList<Card> deck) {
//        Deck = deck;
//    }
//
//    public ArrayList<Card> getHand() {
//        return Hand;
//    }
//
//    public void setHand(ArrayList<Card> hand) {
//        Hand = hand;
//    }
//
//    public ArrayList<Card> getOwnedCards() {
//        return ownedCards;
//    }
//
//    public void setOwnedCards(ArrayList<Card> ownedCards) {
//        this.ownedCards = ownedCards;
//    }
//
//    public void printHand() {
//        for(Card card: this.Hand){
//            System.out.println("Name:" + card.getName() + ",\tType:"+ card.getType() +",\tDuration:" +
//                    card.getDuration()+",\t Player damage:"+ (card).getPlayerDamage(this.getCharacter())+
//                    ",\tAttack/defense point:"+ (card).cardDefenseAttack+"||");
//
//        }
//        System.out.println();
//    }
//
////    private String isMagical(Card card) {
////        if(card.){
////            return "Spell card";
////        }
////        else{
////            return "Attack/defense card";
////        }
////    }
//
//
//    public int getPlayerPoints() {
//        return playerPoints;
//    }
//
//    public void setPlayerPoints(int playerPoints) {
//        this.playerPoints = playerPoints;
//    }
//
//    public double getMaxXp() {
//        return this.maxXp;
//    }
//
//    public void reset() {
//        this.HP = maxHP;
//        this.damage = 0 ;
//        this.hands_played =0;
//        this.playerPoints =0;
//        this.Character = null;
//        this.Deck.clear();
//        this.Hand.clear();
//    }
//
//    public Clan getMyClan() {
//        return myClan;
//    }
//
//    public void setMyClan(Clan myClan) {
//        this.myClan = myClan;
//    }
//
//
//    public void setPlayer() {
//
//    }
//
//
//    public void giftPack(Connection connection) throws SQLException {
//        this.Level = 1;
//        this.HP = 100;
//        this.maxHP =100;
//        this.XP=0;
//        this.Coin = 1000;
//        ArrayList<Card> allCards = (ArrayList<Card>) Store.getAvailableCards(connection, this);
//        // Create a deep copy of the ArrayList
//        ArrayList<Integer> indexer = new ArrayList<>();
//        for(int i=0; i< allCards.size() ; i++){
//            indexer.add(i);
//        }
//        Collections.shuffle(indexer);
//        ArrayList<Card> starterCards = new ArrayList<>();
//        for(int i: indexer.subList(0,20)){
//            starterCards.add(allCards.get(i));
//        }
//        this.setOwnedCards(starterCards);
//        this.updateUserInfo(connection);
//        // Insert starter cards into user_cards table
//        String insertQuery = "INSERT INTO user_cards (user_id, card_name, cost, upgrade_cost, upgrade_level, player_damage, card_defence_attack, duration, execution, `character`, upgraded_level) " +
//                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//        try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
//            for (Card card : starterCards) {
//                stmt.setInt(1, this.userId); // Assuming userId is set somewhere in your class
//                stmt.setString(2, card.getName());
//                stmt.setInt(3, card.getCost());
//                stmt.setInt(4, card.getUpgradeCost());
//                stmt.setInt(5, card.getUpgradeLevel());
//                stmt.setInt(6, (card).getPlayerDamage());
//                stmt.setInt(7, (card).getCardDefenseAttack());
//                stmt.setInt(8, card.getDuration());
//                stmt.setString(9, null); // Assuming card.getExecution() returns a String
//                stmt.setString(10, card.getCharacter()); // Assuming card.getCharacter() returns a String
//                stmt.setInt(11, card.upgradedLevel);
//                stmt.setString(9, (card).execution);
//                stmt.executeUpdate();
//
//            }
//            System.out.println("Starter cards added to user_cards table successfully.");
//        } catch (SQLException e) {
//            System.err.println("Error inserting starter cards: " + e.getMessage());
//            throw e; // Re-throw exception to handle it further up the call stack if needed
//        }
//
//    }
    // Method to update user information in the database
    public void updateUserInfo(Connection conn) throws SQLException {
        String sql = "UPDATE USER " +
                "SET username = ?, password = ?, nickname = ?, security_question = ?, " +
                "security_answer = ?, email = ?, level = ?, max_hp = ?, current_xp = ?, " +
                "coins = ?, logged_in = ? " +
                "WHERE user_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, this.username);
            stmt.setString(2, this.password);
            stmt.setString(3, this.nickname);
            stmt.setString(4, this.securityQuestion);
            stmt.setString(5, this.securityAnswer);
            stmt.setString(6, this.email);
            stmt.setInt(7, this.Level);
            stmt.setInt(8, this.maxHP);
            stmt.setInt(9, this.XP);
            stmt.setInt(10, this.Coin);
            stmt.setBoolean(11, this.loggedIn);
            stmt.setInt(12, this.userId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("User information updated successfully.");
            } else {
                System.out.println("User information update failed. No rows affected.");
            }
        }
    }
    public static boolean userExists(String username, Connection conn) throws SQLException {
        String query = "SELECT COUNT(*) FROM user WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public static boolean passwordMatchesUsername(String username, String password, Connection conn) throws SQLException {
        String query = "SELECT password FROM user WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    return storedPassword.equals(password);
                }
            }
        }
        return false;
    }

//    public static User fetchByUsername(String username, Connection conn) throws SQLException {
//        String query = "SELECT * FROM user WHERE username = ?";
//        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
//            pstmt.setString(1, username);
//            try (ResultSet rs = pstmt.executeQuery()) {
//                if (rs.next()) {
//                    return new User(
//                            rs.getString("username"),
//                            rs.getString("password"),
//                            rs.getString("nickname"),
//                            rs.getString("security_question"),
//                            rs.getString("security_answer"),
//                            rs.getString("email")
//                    );
//                }
//            }
//        }
//        return null;
//    }

    public void updatePasswordInDatabase(Connection conn) throws SQLException {
        String query = "UPDATE user SET password = ? WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, this.password);
            pstmt.setString(2, this.username);
            pstmt.executeUpdate();
        }
    }

    // Method to update this User object from the database
    public void updateFromDatabase(Connection conn) throws SQLException {
        String selectQuery = "SELECT username, password, nickname, security_question, security_answer, " +
                "email, level, max_hp, current_xp, coins, logged_in " +
                "FROM USER WHERE user_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(selectQuery)) {
            stmt.setInt(1, this.userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                this.username = rs.getString("username");
                this.password = rs.getString("password");
                this.nickname = rs.getString("nickname");
                this.securityQuestion = rs.getString("security_question");
                this.securityAnswer = rs.getString("security_answer");
                this.email = rs.getString("email");
                this.Level = rs.getInt("level");
                this.maxHP = rs.getInt("max_hp");
                this.HP = rs.getInt("max_hp");
                this.XP = rs.getInt("current_xp");
                this.Coin = rs.getInt("coins");
                this.loggedIn = rs.getBoolean("logged_in");
            }
        }
    }
//    public List<Card> getUserCards(Connection conn) throws SQLException {
//        // SQL query to retrieve user cards
//        String sql = "SELECT uc.user_card_id, uc.card_name, uc.upgraded_level, c.cost, c.upgrade_cost, c.upgrade_level, c.name, c.player_damage, c.card_defense_attack, c.duration, c.execution, c.character " +
//                "FROM user_cards uc JOIN cards c ON uc.card_name = c.name " +
//                "WHERE uc.user_id = ?";
//        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//            int userId = this.getUserId();
//            stmt.setInt(1, userId);  // Set the user ID
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                List<Card> userCards = new ArrayList<>();
//                while (rs.next()) {
//                    int currentUpgradedLevel = rs.getInt("upgraded_level");
//                    int initialUpgradeCost = rs.getInt("upgrade_cost");
//                    double multiplier = 1.0 + (0.25 * currentUpgradedLevel);
//                    int nextUpgradeCost = (int) (initialUpgradeCost * multiplier);
//
//                    // Create a NormalCard object and set its attributes
//                    Card card = new Card();
//                    card.cardId = rs.getInt("user_card_id");
//                    card.name = rs.getString("card_name");
//                    card.cost = rs.getInt("cost");
//                    card.upgradeCost = nextUpgradeCost; // Cost for next upgrade
//                    card.upgradeLevel = rs.getInt("upgrade_level");
//                    card.duration = rs.getInt("duration");
//                    card.character = rs.getString("character");
//                    card.playerDamage = rs.getInt("player_damage");
//                    card.cardDefenseAttack = rs.getInt("card_defense_attack");
//                    card.upgradedLevel = currentUpgradedLevel + 1; // Current level of the card
//                    card.execution = rs.getString("execution");
//                    userCards.add(card);
//                }
//
//                this.updateFromDatabase(conn);
//                return userCards;
//            }
//        }
//    }


}
