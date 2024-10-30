//package org.example.model;
//import org.example.menuController.MainMenuController;
//import java.sql.SQLException;
//import java.util.Scanner;
//
//import java.util.Arrays;
//
//public class Round {
//
//    private User player1, player2;
//    Board board;
//    Game game;
//    private int playerOrder;
//    Scanner scanner = new Scanner(System.in);
//    private boolean DEAD = false;
//    private final int HANDS_PER_ROUND = 1;
//    Round() {
//    }
//
//    Round(User player1, User player2, Board board, int playerOrder, Game game) {
//        this.player1 = player1;
//        this.player2 = player2;
//        this.board = board;
//        this.playerOrder = playerOrder;
//        this.game = game;
//    }
//
//    public void execute() throws SQLException {
//
//        while (player1.getHands_played() < HANDS_PER_ROUND || player2.getHands_played() < HANDS_PER_ROUND) {
//            System.out.println("It's player " + playerOrder + "'s turn!");
//            Hand hand = new Hand();
//            hand.execute(playerOrder, scanner, player1, player2, board, this);
//            playerOrder = 3 - playerOrder;
//            if(Board.INVISIBLE2 || Board.INVISIBLE1){
//                board.printBoard(2);
//            }
//            else {
//                board.printBoard(1);
//            }
//
//        }
//
//        System.out.println("##\t\t\t\t\t##\n##Let's see if the odds were in your favour!##\n##\t\t\t\t\t##");
//        finishSet(board);
//        resetItems();
//    }
//
//    private void resetItems() throws SQLException {
//        Arrays.fill(this.board.getRow1(), null);
//        Arrays.fill(this.board.getRow2(), null);
//        game.handCards(2, player1, player2);
//        player2.setHands_played(0);
//        player1.setHands_played(0);
//
//    }
//
//
//
//    private void finishSet(Board board) {
//        Card card1, card2;
//        boolean DEAD = false;
//        int card1Index = 0, card2Index = 0;
//        for (int i = 0; (i < board.BoardLength) && !DEAD; i++) {
//            card1 = board.getRow1()[i];
//            card2 = board.getRow2()[i];
//            if (card1 != null) {
//                if (card2 != null) {
//                    DEAD = applyDamage((card1).getDamagePerCell(player1.getCharacter(), card1Index),
//                            (card2).getDamagePerCell(player2.getCharacter(), card2Index),
//                            player1.getHP(), player2.getHP(), player1, player2);
//                    card1Index += 1;
//                    card2Index += 1;
//                    if (card1Index == card1.getDuration()) {
//                        card1Index = 0;
//                    }
//                    if (card2Index == card2.getDuration()) {
//                        card2Index = 0;
//                    }
//                    dealWithSpellCard(card2,player2);
//                } else {
//                    DEAD = applyDamage((card1).getDamagePerCell(player1.getCharacter(), card1Index), 0,
//                            player1.getHP(), player2.getHP(), player1, player2);
//                    card1Index += 1;
//                    if (card1Index == card1.getDuration()) {
//                        card1Index = 0;
//                    }
//                }
//                dealWithSpellCard(card1,player1);
//                board.printBoard(0);
//            } else if (card2 != null) {
//                DEAD = applyDamage(0, (card2).getDamagePerCell(player2.getCharacter(), card2Index),
//                        player1.getHP(), player2.getHP(), player1, player2);
//                card2Index += 1;
//                if (card2Index == card2.getDuration()) {
//                    card2Index = 0;
//                }
//
//                dealWithSpellCard(card2,player2);
//                board.printBoard(0);
//            }
//
//            //this prints 21
//            //board.printBoard(true);
//        }
//        this.DEAD = (player1.getHP() == 0) || (player2.getHP() == 0);
//    }
//    public void dealWithSpellCard(Card card, User player){
//        if (card.getName().equals("Heal")) {
//            ActivateHeal(player);
//        }
//    }
//
//    private void ActivateHeal(User player) {
//        if (player.getHP() >= 80) {
//            player.setHP(100);
//        } else {
//            player.setHP(player.getHP() + 20);
//        }
//        System.out.println("User" + playerOrder + " used card heal! \n New HP:" + player.getHP());
//    }
//
//
//    private boolean applyDamage(int damage1, int damage2, int hp1, int hp2, User player1, User player2) {
//        if (damage1 > damage2) {
//            if (willSurvive(hp2, damage1 - damage2)) {
//                player2.setHP(player2.getHP() - damage1 + damage2);
//                player2.setDamage(player2.getDamage() + damage1 - damage2);
//                return false;
//            } else {
//                player2.setHP(0);
//                player2.setDamage(player2.getDamage() + damage1 - damage2);
//                return true;
//            }
//        } else {
//            if (willSurvive(hp1, damage2 - damage1)) {
//                player1.setHP(player1.getHP() - damage2 + damage1);
//                player1.setDamage(player1.getDamage() + damage2 - damage1);
//                return false;
//            } else {
//                player1.setHP(0);
//                player1.setDamage(player1.getDamage() + damage2 - damage1);
//                return true;
//            }
//        }
//
//
//    }
//
//    private boolean willSurvive(int hp, int i) {
//        return hp > i;
//    }
//
//    public boolean isDEAD() {
//        return DEAD;
//    }
//
//    public User getPlayer(int playerOrder) {
//        if (playerOrder == 1) {
//            return player1;
//        } else {
//            return player2;
//        }
//    }
//
//}
