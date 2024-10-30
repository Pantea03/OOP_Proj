//package org.example.model;
//
//import java.sql.SQLException;
//import java.util.Scanner;
//import org.example.menuController.MainMenuController;
//public class GambleMode extends Game {
//    public GambleMode(User player1, User player2) {
//        super(player1, player2);
//    }
//
//    Scanner scanner = new Scanner(System.in);
//
//    public void execute() throws SQLException {
//
//        System.out.println("How much are you going to bet?");
//        int bettingValue = 0;
//        while (true) {
//            try {
//                String str = scanner.nextLine();
//                bettingValue = Integer.parseInt(str.trim());
//                if (isValid(bettingValue)) {
//                    break;
//                } else {
//                    System.out.println("Invalid betting value. Try again.");
//                }
//            } catch (NumberFormatException e) {
//                System.out.println("Please enter a valid number.");
//            }
//        }
//
//        player1.setCoin(player1.getCoin() - bettingValue);
//        player2.setCoin(player2.getCoin() - bettingValue);
////        System.out.println("0: "+player2.getCoin());
//
//        DoublePlayer game = new DoublePlayer(player1,player2);
//        game.execute();
//        if(game.getWinner().equals(player1)){
//            System.out.println("Player 1 won the bet!");
//            player1.setCoin(player1.getCoin()+2*bettingValue);
////            System.out.println("1: "+player2.getCoin());
//        }
//        else if(game.getWinner().equals(player2)){
//            System.out.println("Player 2 won the bet!");
//            player2.setCoin(player2.getCoin()+2*bettingValue);
////            System.out.println("2:"+player2.getCoin());
//        }
//        else{
//            System.out.println("Tie!");
//            player1.setCoin(player1.getCoin()+bettingValue);
//            player1.setCoin(player1.getCoin()+bettingValue);
//            System.out.println("3: "+player2.getCoin());
//        }
//        player1.updateUserInfo(DatabaseUtil.getConnection());
//        player2.updateUserInfo(DatabaseUtil.getConnection());
//        String res1;
//        String res2;
//        if (game.getWinner().equals(player1)) {
//            res1 = "Win";
//            res2 = "Lose";
//        }
//        else {
//            res1 = "Lose";
//            res2 = "Win";
//        }
//        GameHistory.save(DatabaseUtil.getConnection(), player1.getUserId(), player2.getUsername(), player2.getLevel(), res1, giveRewardsPenalties1());
//        GameHistory.save(DatabaseUtil.getConnection(), player2.getUserId(), player1.getUsername(), player1.getLevel(), res2, giveRewardsPenalties2());
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
//
//
//    public boolean isValid(int value){
//        if(this.player1.getCoin()>value && this.player2.getCoin()>value){
//            return true;
//        }
//        else{
//            return false;
//        }
//    }
//}
