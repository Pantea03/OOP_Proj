//package org.example.model;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Random;
//import java.util.Scanner;
//import org.example.menuController.MainMenuController;
//public class RoundSingle extends Round{
//    private User player1;
//    Board board;
//    Game game;
//    private int playerOrder;
//    Scanner scanner = new Scanner(System.in);
//    private boolean DEAD = false;
//    private final int HANDS_PER_ROUND = 1;
//    User computer;
//    private Card[] computerRow;
//    private static int lastIndex;
//    RoundSingle() {
//    }
//
//    RoundSingle(User player1, User computer, Board board, int playerOrder, Game game) {
//        this.player1 = player1;
//        this.computer = computer;
//        this.board = board;
//        this.playerOrder = playerOrder;
//        this.game = game;
//    }
//
//    public void execute() throws SQLException {
//
//        playerOrder =1;
//        Card lastCard = null;
//        int lastIndex =0;
//        while (player1.getHands_played() < 4 || computer.getHands_played() < 4) {
//            Hand hand = new Hand();
//            if (playerOrder == 1) {
//                hand.execute(playerOrder, scanner, player1, computer, board, this);
//                lastCard = hand.getLastCard();
//                lastIndex = hand.getLastIndex();
//
//            } else {
//                computerTurn(lastCard, lastIndex);
//            }
//            playerOrder = 3 - playerOrder;
//            board.printBoard(1);
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
//        game.handCards(1, player1, computer);
//        computer.setHands_played(0);
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
//                            (card2).getDamagePerCell(computer.getCharacter(), card2Index),
//                            player1.getHP(), computer.getHP(), player1, computer);
//                    card1Index += 1;
//                    card2Index += 1;
//                    if (card1Index == card1.getDuration()) {
//                        card1Index = 0;
//                    }
//                    if (card2Index == card2.getDuration()) {
//                        card2Index = 0;
//                    }
//                    dealWithSpellCard(card2,computer);
//                } else {
//                    DEAD = applyDamage((card1).getDamagePerCell(player1.getCharacter(), card1Index), 0,
//                            player1.getHP(), computer.getHP(), player1, computer);
//                    card1Index += 1;
//                    if (card1Index == card1.getDuration()) {
//                        card1Index = 0;
//                    }
//                }
//                dealWithSpellCard(card1,player1);
//                board.printBoard(0);
//            } else if (card2 != null) {
//                DEAD = applyDamage(0, (card2).getDamagePerCell(computer.getCharacter(), card2Index),
//                        player1.getHP(), computer.getHP(), player1, computer);
//                card2Index += 1;
//                if (card2Index == card2.getDuration()) {
//                    card2Index = 0;
//                }
//
//                dealWithSpellCard(card2,computer);
//                board.printBoard(0);
//            }
//
//            //this prints 21
//            //board.printBoard(true);
//        }
//        this.DEAD = (player1.getHP() == 0) || (computer.getHP() == 0);
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
//    private boolean applyDamage(int damage1, int damage2, int hp1, int hp2, User player1, User computer) {
//        if (damage1 > damage2) {
//            if (willSurvive(hp2, damage1 - damage2)) {
//                computer.setHP(computer.getHP() - damage1 + damage2);
//                computer.setDamage(computer.getDamage() + damage1 - damage2);
//                return false;
//            } else {
//                computer.setHP(0);
//                computer.setDamage(computer.getDamage() + damage1 - damage2);
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
//            return computer;
//        }
//    }
//
//    private void computerTurn(Card playerCard, int lastIndex) {
//
//        Card chosenCard = chooseCardForComputer(computer.getHand(), lastIndex);
//
//        playCard(chosenCard, lastIndex);
//        computer.setHands_played(computer.getHands_played() + 1);
//    }
//
//    public Card chooseCardForComputer(ArrayList<Card> computerHand,int index) {
//        System.out.println(index);
//        Card playerCard = board.getRow1()[index];
//        Card bestCard = null;
//
//        ArrayList<Card> usable = findUsableCards(computerHand, index);
//
//        if(playerCard == null){
//            if (!usable.isEmpty()) {
//                for (Card card : usable) {
//                    if(bestCard!= null){
//                        if( card.getCardDefenseAttack()< bestCard.getCardDefenseAttack()){
//                            bestCard = card;
//                        }
//                    }
//                    else{
//                        bestCard = card;
//                    }
//                }
//                return bestCard;
//            }
//        }
//        int pDA = playerCard.getCardDefenseAttack();
//        if (!usable.isEmpty()) {
//            for (Card card : usable) {
//                if(card.getCardDefenseAttack()>pDA){
//                    if(bestCard!= null){
//                        if( card.getCardDefenseAttack()< bestCard.getCardDefenseAttack()){
//                            bestCard = card;
//                        }
//                    }
//                    else{
//                        bestCard = card;
//                    }
//                }
//            }
//            return bestCard;
//        }
//        return chooseCardForComputer(computerHand,(index+1)%21);
//    }
//    private ArrayList<Card> findUsableCards(ArrayList<Card> computerHand, int index) {
//        ArrayList<Card> result = new ArrayList<>();
//        for (Card card : computerHand) {
//            if (canPlaceCardHere(card, index, computerRow, board, 2)) {
//                result.add(card);
//                //System.out.println(card.getName()+ " can be used");
//                //works fine
//            }
//        }
//        if (index != lastIndex - 1) {
//            if (result.isEmpty()) {
//                findUsableCards(computerHand, (index + 1) % 21);
//            } else {
//                return result;
//            }
//        } else {
//            return result;
//        }
//        return new ArrayList<>();
//    }
//
//    private void playCard(Card chosenCard, int index) {
//        if (chosenCard == null) {
//            throw new IllegalArgumentException("Chosen card cannot be null");
//        }
//        int card_duration = chosenCard.getDuration();
//
//
//        Card[] rowPlayer = board.getRow1();
//        Card[] rowComputer = board.getRow2();
//
//        rowComputer[index] = new Card(chosenCard);
//        //System.out.println(rowComputer[index].getName());
//        for (int l = 0; l < card_duration; l++) {
//            rowComputer[index + l] = rowComputer[index];
//            cellBreak(rowComputer, rowPlayer, index, l);
//            brokeCardCompletely(playerOrder, rowComputer, rowPlayer, index, l, computer);
//        }
//        removeCardFromDeck(computer,chosenCard);
//
//    }
//    private void removeCardFromDeck(User player, Card chosenCard) {
//        int rand = 0;
//        ArrayList<Card> roundDeck = new ArrayList<Card>(player.getDeck());
//        roundDeck.remove(chosenCard);
//        if (player.getHand().size()>5) {
//            player.getHand().remove(chosenCard);
//        } else {
//            rand = random.nextInt(roundDeck.size());
//            player.getHand().set(player.getHand().indexOf(chosenCard), roundDeck.get(rand));
//        }
//    }
//    Random random = new Random();
//    private void brokeCardCompletely(int playerOrder, Card[] playerRow, Card[] opponentRow, int index, int l, User player) {
//        if (opponentRow[index + l] != null && ((opponentRow[index + l]).getNumberOfBrokenCells() == opponentRow[index + l].getDuration())) {
//            System.out.println("You completely broke one of the opponent's cards!");
//            int r = random.nextInt(2);
//            player.setPlayerPoints(player.getPlayerPoints() + (opponentRow[index + l]).getNumberOfBrokenCells() * 15);
//            if (r == 0) {
//                player.setCoin(player.getCoin() + 20);
//            } else {
//                player.setXP(player.getXP() + 100);
//            }
//        }
//
//        else if (playerRow[index + l] != null && ((playerRow[index + l]).getNumberOfBrokenCells() == playerRow[index + l].getDuration())) {
//            System.out.println("You completely broke one of your own cards!");
//            int r = random.nextInt(2);
//            player.setPlayerPoints(player.getPlayerPoints() + (playerRow[index + l]).getNumberOfBrokenCells() * 15);
//            if (r == 0) {
//                player.setCoin(player.getCoin() + 20);
//            } else {
//                player.setXP(player.getXP() + 100);
//            }
//        }
//
//    }
//    private void cellBreak(Card[] rowPlayer, Card[] rowOponnent, int index, int l) {
//        if (rowOponnent[index + l] != null) {
//            if (rowOponnent[index + l].getCardDefenseAttack() < rowPlayer[index + l].getCardDefenseAttack()) {
//                rowOponnent[index + l].setDamagePerCelL(0, l);
//                rowOponnent[index + l].setBrokenCell(rowOponnent[index + l].getNumberOfBrokenCells() + 1, l);
//            } else if (rowOponnent[index + l].getCardDefenseAttack() == rowPlayer[index + l].getCardDefenseAttack()) {
//                rowPlayer[index + l].setDamagePerCelL(0, l);
//                rowOponnent[index + l].setDamagePerCelL(0, l);
//                rowPlayer[index + l].setBrokenCell(rowPlayer[index + l].getNumberOfBrokenCells() + 1, l);
//                rowOponnent[index + l].setBrokenCell(rowOponnent[index + l].getNumberOfBrokenCells() + 1, l);
//            } else {
//                rowPlayer[index + l].setDamagePerCelL(0, l);
//                rowPlayer[index + l].setBrokenCell(rowPlayer[index + l].getNumberOfBrokenCells() + 1, l);
//            }
//        }
//    }
//    private boolean canPlaceCardHere(Card chosenCard, int index, Card[] row1, Board board, int playerOrder) {
//        int duration = chosenCard.getDuration();
//        int ditch = (playerOrder == 1) ? board.getRow1DitchIndex() : board.getRow2DitchIndex();
//        if (row1 != null) {
//            int row1Length = row1.length;
//
//            for (int i = 0; i < duration; i++) {
//                int currentIndex = index + i;
//                if (currentIndex >= row1Length || currentIndex < 0 || row1[currentIndex] != null || ditch == currentIndex) {
//                    return false;
//                }
//            }
//            return true;
//        }
//        return true;
//    }
//
//}
