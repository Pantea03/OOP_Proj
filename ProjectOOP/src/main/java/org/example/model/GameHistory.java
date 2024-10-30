//package org.example.model;
//import org.example.menuController.MainMenuController;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//
//public class GameHistory {
//    private int gameId;
//    private int userId;
//    private Timestamp gameDate;
//    private String opponent;
//    private int opponentLevel;
//    private String result;
//    private String rewardsPenalties;
//    private static String orderBy = "game_date";
//    private static boolean ascending = true;
//
//    // Constructor
//    public GameHistory(int userId, String opponent, int opponentLevel, String result, String rewardsPenalties) {
//        this.userId = userId;
//        this.opponent = opponent;
//        this.opponentLevel = opponentLevel;
//        this.result = result;
//        this.rewardsPenalties = rewardsPenalties;
//    }
//
//    // Getters and Setters
//    public int getGameId() { return gameId; }
//    public int getUserId() { return userId; }
//    public Timestamp getGameDate() { return gameDate; }
//    public String getOpponent() { return opponent; }
//    public int getOpponentLevel() { return opponentLevel; }
//    public String getResult() { return result; }
//    public String getRewardsPenalties() { return rewardsPenalties; }
//
//    // Save game history to the database at the end of each game *****************************
//    // *******************************************************
//    public static void save(Connection conn, int userId, String opponentUsername, int opponentLevel, String result, String rewardsPenalties) throws SQLException {
//        String sql = "INSERT INTO user_gamehistory (user_id, opponent_username, game_date, opponent_level, result, rewards_penalties) VALUES (?, ?, ?, ?, ?, ?)";
//        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setInt(1, userId);
//            stmt.setString(2, opponentUsername);
//            stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
//            stmt.setInt(4, opponentLevel);
//            stmt.setString(5, result);
//            stmt.setString(6, rewardsPenalties);
//            stmt.executeUpdate();
//        }
//    }
//
//    // Fetch game history by user ID
//    public static List<GameHistory> fetchByUserId(Connection conn, int userId, String orderBy, boolean ascending) throws SQLException {
//        String sql = "SELECT * FROM user_gamehistory WHERE user_id = ? ORDER BY " + orderBy + (ascending ? " ASC" : " DESC");
//        List<GameHistory> historyList = new ArrayList<>();
//        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setInt(1, userId);
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    GameHistory history = new GameHistory(
//                            rs.getInt("user_id"),
//                            rs.getString("opponent_username"),
//                            rs.getInt("opponent_level"),
//                            rs.getString("result"),
//                            rs.getString("rewards_penalties")
//                    );
//                    history.gameId = rs.getInt("game_id");
//                    history.gameDate = rs.getTimestamp("game_date");
//                    historyList.add(history);
//                }
//            }
//        }
//        return historyList;
//    }
//
//
//    // Display the game history with pagination
//    public static void displayGameHistory(List<GameHistory> historyList, int pageSize, int currentPage) {
//        int totalPages = (int) Math.ceil((double) historyList.size() / pageSize);
//        int start = (currentPage - 1) * pageSize;
//        int end = Math.min(start + pageSize, historyList.size());
//
//        System.out.println("Game History (Page " + currentPage + " of " + totalPages + "):");
//        System.out.printf("%-20s %-20s %-10s %-10s %-20s%n", "Date", "Opponent", "Level", "Result", "Rewards/Penalties");
//
//        for (int i = start; i < end; i++) {
//            GameHistory history = historyList.get(i);
//            System.out.printf("%-20s %-20s %-10d %-10s %-20s%n",
//                    history.getGameDate(), history.getOpponent(), history.getOpponentLevel(),
//                    history.getResult(), history.getRewardsPenalties());
//        }
//        System.out.println("\nCommands:");
//        System.out.println("1. back");
//        System.out.println("2. sort [date/result/opponent/level]");
//        System.out.println("3. order [asc/desc]");
//        System.out.println("4. next/previous/page [number]");
//    }
//
//
//    public static void handleGameHistoryCommands(Scanner scanner, Connection conn, int userId, List<GameHistory> historyList, int pageSize, int currentPage) {
//        String orderBy = "game_date"; // Default sort field
//        boolean ascending = true; // Default sort order
//
//        while (true) {
//            System.out.print("Enter command: ");
//            String command = scanner.nextLine();
//            switch (command) {
//                case "back":
//                    InputProcessor.currentMenu.name = "main";
//                    return;
//                case "sort date":
//                    orderBy = "game_date";
//                    break;
//                case "sort result":
//                    orderBy = "result";
//                    break;
//                case "sort opponent":
//                    orderBy = "opponent_username";
//                    break;
//                case "sort level":
//                    orderBy = "opponent_level";
//                    break;
//                case "order asc":
//                    ascending = true;
//                    break;
//                case "order desc":
//                    ascending = false;
//                    break;
//                case "next":
//                    if (currentPage < (historyList.size() + pageSize - 1) / pageSize) {
//                        currentPage++;
//                    }
//                    break;
//                case "previous":
//                    if (currentPage > 1) {
//                        currentPage--;
//                    }
//                    break;
//                default:
//                    if (command.startsWith("page ")) {
//                        try {
//                            int page = Integer.parseInt(command.split(" ")[1]);
//                            if (page > 0 && page <= (historyList.size() + pageSize - 1) / pageSize) {
//                                currentPage = page;
//                            }
//                        } catch (NumberFormatException e) {
//                            System.out.println("Invalid page number.");
//                        }
//                    }
//            }
//            try {
//                historyList = GameHistory.fetchByUserId(conn, userId, orderBy, ascending);
//                GameHistory.displayGameHistory(historyList, pageSize, currentPage);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//}
