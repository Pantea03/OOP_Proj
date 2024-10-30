//package org.example.model;
//
//import java.sql.SQLException;
//import java.util.Random;
//import java.util.Scanner;
//import org.example.menuController.MainMenuController;
//public class DoublePlayer extends Game {
//
//    Scanner scanner = new Scanner(System.in);
//    private User Winner, Loser;
//
//    public DoublePlayer(User player1, User player2) {
//        super(player1, player2);
//    }
//
//    public void execute() throws SQLException {
//        //we won't handle error here because we use a button in graphic
//
//        getCharacters();
//
//        Board board = new Board(this.player1, this.player2);
//        handCards(0, player1, player2);
//        Random random = new Random();
//        int playerOrder = random.nextInt(2) + 1;
//        System.out.println("Player " + playerOrder + " gets to go first!\n");
//
//
//        boolean DEAD = false;
//        while (!DEAD) {
//            System.out.println("\n***START ROUND***");
//            board.printBoard(1);
//            Round round = new Round(player1, player2, board, playerOrder, this);
//            round.execute();
//            System.out.println("***ROUND OVER***\n");
//            DEAD = round.isDEAD();
//        }
//        givePoints();
//        // save *******************************
//        String res1;
//        String res2;
//        if (giveWinner().equals(player1.getUsername())) {
//            res1 = "Win";
//            res2 = "Lose";
//        }
//        else {
//            res1 = "Lose";
//            res2 = "Win";
//        }
//        GameHistory.save(DatabaseUtil.getConnection(), player1.getUserId(), player2.getUsername(), player2.getLevel(), res1, giveRewardsPenalties1());
//        GameHistory.save(DatabaseUtil.getConnection(), player2.getUserId(), player1.getUsername(), player1.getLevel(), res2, giveRewardsPenalties2());
//
//    }
//    private String giveRewardsPenalties1() {
//        //give point, xp, level, coin
//        //refer to database handler.
//        if (player1.getHP() == 0) {
//            return "XP += "+ (int)this.player1.getPlayerPoints();
//        } else {
//            return "XP += "+ (int) ((double) (this.player1.getPlayerPoints() / 100) * 0.5 * this.player1.getMaxXp()) +
//                    "Coins += " + this.player1.getPlayerPoints() * 2 + 100 * this.player1.getLevel();
//        }
//
//    }
//    private String giveRewardsPenalties2() {
//        //give point, xp, level, coin
//        //refer to database handler.
//        if (player2.getHP() == 0) {
//            return "XP += "+ (int)this.player2.getPlayerPoints();
//        } else {
//            return "XP += "+ (int) ((double) (this.player2.getPlayerPoints() / 100) * 0.5 * this.player2.getMaxXp()) +
//                    "Coins += " + this.player2.getPlayerPoints() * 2 + 100 * this.player2.getLevel();
//        }
//
//    }
//
//    private void getCharacters() {
//        System.out.println("Player1 please select your character: \n Your options are: KIM, PAN, PAR, KAI");
//        String str = scanner.nextLine();
//        str = str.trim();
//        player1.setCharacter(str);
//        System.out.println("Player2 please select your character: \n Your options are: KIM, PAN, PAR, KAI");
//        str = scanner.nextLine();
//        str = str.trim();
//        player2.setCharacter(str);
//    }
//
//    private void givePoints() throws SQLException {
//        //give point, xp, level, coin
//        //refer to database handler.
//        if (player1.getHP() == 0) {
//            this.Winner = player2;
//            this.Loser = player1;
//        } else {
//            this.Winner = player1;
//            this.Loser = player2;
//        }
//        this.Loser.setXP((int) (this.Loser.getXP() + this.Loser.getPlayerPoints()));
//        this.Winner.setXP(this.Winner.getXP() + (int) ((double) (this.Winner.getPlayerPoints() / 100) * 0.5 * this.Winner.getMaxXp()));
//        this.Winner.setCoin(this.Winner.getCoin() + this.Winner.getPlayerPoints() * 2 + 100 * this.Winner.getLevel());
//        Winner.leveledUp();
//        Loser.leveledUp();
//        printStats(Winner, Loser);
//
//        player1.setPlayerPoints(abser(player1.getDamage() - player2.getDamage()));
//        player2.setPlayerPoints(abser(player2.getDamage() - player1.getDamage()));
//        player1.setDamage(0);
//        player2.setDamage(0);
//
//        player1.updateUserInfo(DatabaseUtil.getConnection());
//        player2.updateUserInfo(DatabaseUtil.getConnection());
//    }
//
//    private String giveWinner() {
//        //give point, xp, level, coin
//        //refer to database handler.
//        if (player1.getHP() == 0) {
//            return player2.getUsername();
//        } else {
//            return player1.getUsername();
//        }
//    }
//
//    private String giveRewardsPenalties() {
//        //give point, xp, level, coin
//        //refer to database handler.
//        if (player1.getHP() == 0) {
//            this.Winner = player2;
//            this.Loser = player1;
//            return "XP += "+ (int)this.Loser.getPlayerPoints();
//        } else {
//            this.Winner = player1;
//            this.Loser = player2;
//            return "XP += "+ (int) ((double) (this.Winner.getPlayerPoints() / 100) * 0.5 * this.Winner.getMaxXp()) +
//                    "Coins += " + this.Winner.getPlayerPoints() * 2 + 100 * this.Winner.getLevel();
//        }
//
//    }
//
//
//    private int abser(int i) {
//        if (i > 0) {
//            return i;
//        } else {
//            return 0;
//        }
//    }
//
//    public void printStats(User winner, User loser) {
//        System.out.println("Stats:");
//        System.out.println("Winner:\t" + winner.getUsername() + "\tLevel:\t" + winner.getLevel() + "\tCoins:\t" + winner.getCoin() + "\tXP:\t" + winner.getXP());
//        System.out.println("Loser:\t" + loser.getUsername() + "\tLevel:\t" + loser.getLevel() + "\tCoins:\t" + loser.getCoin() + "\tXP:\t" + loser.getXP());
//    }
//
//    public User getWinner(){
//        return this.Winner;
//    }
//}
