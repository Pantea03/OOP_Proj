//package org.example.model;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Random;
//import java.util.Scanner;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import org.example.menuController.MainMenuController;
//public class Hand {
//
//
//    private ArrayList<Card> player1Deck = new ArrayList<Card>(), player2Deck = new ArrayList<Card>(),
//            player1DeckAll = new ArrayList<Card>(), player2DeckAll = new ArrayList<Card>();
//    Pattern pattern1 = Pattern.compile("select card number (\\d+) player (\\d+)");
//    Pattern pattern2 = Pattern.compile("placing card number (\\d+) in block (\\d+)");
//    Pattern pattern3 = Pattern.compile("use card number (\\d+)");
//    boolean matcher1Bool = false, matcher2Bool = false;
//    Random random = new Random();
//    User player, opponent;
//    Hand(){
//        this.matcher2Bool=false;
//    }
//    private Card lastCard;
//    private  int lastIndex;
//
//    public Card getLastCard() {
//        return lastCard;
//    }
//
//    public int getLastIndex() {
//        return lastIndex;
//    }
//
//
//    public void execute(int playerOrder, Scanner scanner, User player1, User player2, Board board, Round round) {
//        Matcher matcher1, matcher2, matcher3;
//        String str;
//        player = getPlayer(playerOrder, player1, player2);
//        opponent = getPlayer(3-playerOrder, player1,player2);
//        Card chosenCard = null;
//        int n = 1;
//        //matcher2Bool=false;
//        do {
//            str = scanner.nextLine().trim().toLowerCase().replaceAll("\\s+", " ");
//            matcher1 = pattern1.matcher(str);
//            matcher2 = pattern2.matcher(str);
//            matcher3 = pattern3.matcher(str);
//            if (matcher1.find()) {
//                showCardDetails(matcher1, player,playerOrder);
//            } else if (matcher2.find()) {
//                chosenCard = player.getHand().get(Integer.parseInt(matcher2.group(1))-1);
//                if(chosenCard.execution == null || chosenCard.execution.contains("Breaks")|| chosenCard.execution.contains("HP")){
//                    matcher2Bool = placeCard(matcher2,player,playerOrder, board ,chosenCard , round);
//                }
//                else{
//                    System.out.println("You can't place a spell card on the board");
//                }
//            } else if (matcher3.find()) {
//                n = Integer.parseInt(matcher3.group(1));
//                chosenCard = player.getHand().get(n - 1);
//                useMagicalCard(matcher3,playerOrder, board, chosenCard, round);
//                matcher2Bool = true;
//            }
//            else {
//                System.out.println("WRONG COMMAND!");
//            }
//        }
//        while (!matcher2Bool);
//        this.lastCard = chosenCard;
//        this.lastIndex = (n - 1);
//        player.setHands_played(player.getHands_played() + 1);
//
//    }
//
//    private boolean placeCard(Matcher matcher2, User player, int playerOrder, Board board, Card chosenCard, Round round) {
//        //placing card number <> in block <>
//
//        ArrayList<Card> roundDeck = new ArrayList<Card>(player.getDeck());
//        int n = Integer.parseInt(matcher2.group(1));
//        int index = Integer.parseInt(matcher2.group(2)) - 1;
//        int rand;
//        Card[] rowPlayer = getRow(board, playerOrder);
//        Card[] rowOpponent = getRow(board, 3 - playerOrder);
//        //check if is same type as middle card
//        if (player.getHand().size() == 5) {
//            if (player.getHand().get(n - 1).getCharacter().equals(player.getHand().get(2).getCharacter())) {
//                rand = random.nextInt(100) + 1;
//                getProbability(player, rand, n);
//            }
//        }
//        if (canPlaceCardHere(chosenCard, index, rowPlayer, board, playerOrder)) {
//            roundDeck.remove(chosenCard);
//            rowPlayer[index] = new Card(chosenCard);
//            for (int l = 0; l < chosenCard.getDuration(); l++) {
//                rowPlayer[index + l] = rowPlayer[index];
//                cellBreak(rowPlayer, rowOpponent, index, l);
//                brokeCardCompletely(playerOrder, rowPlayer, rowOpponent, index, l, player);
//            }
//            removeCardFromDeck(player,chosenCard);
//            return true;
//        } else {
//            System.out.println("You cant place card here!");
//            return false;
//        }
//    }
//
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
//
//    private void showCardDetails(Matcher matcher1, User player, int playerOrder) {
//        int n = Integer.parseInt(matcher1.group(1));
//        if (playerOrder == Integer.parseInt(matcher1.group(2))) {
//            if (player.getHand().size() >= n) {
//                player.getHand().get(n - 1).printDetails();
//                matcher1Bool = true;
//            } else {
//                System.out.println("out of bound hand!");
//            }
//
//        } else {
//            System.out.println("Not your turn!");
//        }
//    }
//
//    private boolean canPlaceCardHere(Card chosenCard, int index, Card[] row1, Board board, int playerOrder) {
//        //deal with it for magical cards. only shield and heal take up space.
//        int duration = chosenCard.getDuration();
//        int ditch = 0;
//        if (playerOrder == 1) {
//            ditch = board.getRow1DitchIndex();
//        } else {
//            ditch = board.getRow2DitchIndex();
//        }
//        for (int i = 0; i < duration; i++) {
//            if (row1[i + index] != null || ditch == (i + index)) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private void getProbability(User player, int rand, int n) {
//        double p;
//        if (player.getHand().get(n - 1).getCharacter().equals("KIM")) {
//            p = 0.45;
//        } else if (player.getHand().get(n - 1).getCharacter().equals("KAI")) {
//            p = 0.4;
//        } else if (player.getHand().get(n - 1).getCharacter().equals("PAN")) {
//            p = 0.55;
//        } else {
//            p = 0.5;
//        }
//        if (rand <= (p * 100)) {
//            (player.getHand().get(2)).setPlayerDamage((player.getHand().get(2)).getPlayerDamage("-1") * 2);
//        }
//    }
//
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
//
//    public Card[] getRow(Board board, int playerOrder) {
//        if (playerOrder == 1) {
//            return board.getRow1();
//        } else {
//            return board.getRow2();
//        }
//    }
//
//    public ArrayList<Card> getPlayerDeck(int playerOrder) {
//        if (playerOrder == 1) {
//            return player1Deck;
//        } else {
//            return player2Deck;
//        }
//    }
//
//    public ArrayList<Card> getPlayerDeckAll(int playerOrder) {
//        if (playerOrder == 1) {
//            return player1DeckAll;
//        } else {
//            return player2DeckAll;
//        }
//    }
//
//
//    public void addToDeck(int player, Card card) {
//        if (player == 1) {
//            player1Deck.add(card);
//            player1DeckAll.add(card);
//        } else {
//            player2Deck.add(card);
//            player2DeckAll.add(card);
//        }
//    }
//    //use the method below when a card is placed on the row
//
////if card used was Copied by CopyCard, delete it from deck.
//
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
//
//    public User getPlayer(int playerOrder, User player1, User player2) {
//        if (playerOrder == 1) {
//            return player1;
//        } else {
//            return player2;
//        }
//    }
//
//    private void useMagicalCard(Matcher matcher3, int playerOrder, Board board, Card chosenCard, Round round) {
//        boolean b= true;
//        if (chosenCard.getName().equals("Card Thief")) {
//            ActivateCardThief(playerOrder);
//        }
//        else if(chosenCard.getName().equals("Buffer")){
//            ActivateBuffer(playerOrder,board);
//
//        }
//        else if(chosenCard.getName().equals("Move Ditch")){
//            ActivateMoveDitch(playerOrder,board);
//
//        }
//        else if(chosenCard.getName().equals("Repair")){
//            ActivateRepair(playerOrder,board);
//
//        }
//        else if(chosenCard.getName().equals("Round Reducer")){
//            ActivateRoundReducer(playerOrder,board);
//
//        }
//        else if(chosenCard.getName().equals("Weakener")){
//            ActivateDebilitater(playerOrder,board);
//
//        }
//        else if(chosenCard.getName().equals("Copy Card")){
//            ActivateCopyCard(playerOrder,board, new Scanner(System.in), player, opponent);
//
//        }
//        else if(chosenCard.getName().equals("Invisibility")){
//            ActivateInvisiblity(playerOrder,board);
//
//        }
//        else{
//            b = false;
//        }
//        if(b){
//            removeCardFromDeck(player,chosenCard);
//        }
//    }
//
//    private void ActivateInvisiblity(int playerOrder, Board board) {
//        board.invisible(3-playerOrder);
//    }
//
//    private void ActivateCopyCard(int playerOrder, Board board, Scanner scanner, User player1, User player2) {
//        System.out.println("Please enter a card you want to copy:");
//        int cardNumber = Integer.parseInt(scanner.nextLine());
//        this.addToDeck(playerOrder, getPlayer(playerOrder, player1, player2).getHand().get(cardNumber-1));
//        System.out.println("Player "+playerOrder + " copied card "+
//                getPlayer(playerOrder, player1, player2).getHand().get(cardNumber-1) + " and now has 6 cards!"
//        );
//    }
//
//    private void ActivateDebilitater(int playerOrder, Board board) {
//        ArrayList<Card> otherPlayerCards = this.getPlayerDeck(3-playerOrder);
//        if(otherPlayerCards.size()<2){
//            System.out.println("You used this card too late!");
//        }
//        else{
//            Random rand = new Random();
//            int random_damage = rand.nextInt(otherPlayerCards.size());
//            int random_power = rand.nextInt(otherPlayerCards.size());
//            if(random_damage==random_power){
//                random_power = (random_power+1)% otherPlayerCards.size();
//            }
//// **********************************************************************
//            (otherPlayerCards.get(random_damage)).lowerDamage();
//            (otherPlayerCards.get(random_power)).lowerPower();
//        }
//    }
//
//
//    private void ActivateRoundReducer(int playerOrder, Board board) {
//        opponent.setHands_played(opponent.getHands_played()+1);
//    }
//
//    private void ActivateRepair(int playerOrder, Board board) {
//        if(playerOrder == 1){
//            board.setRow1DitchIndex(-1);
//        }
//        else{
//            board.setRow2DitchIndex(-1);
//        }
//    }
//
//    private void ActivateMoveDitch(int playerOrder, Board board) {
//        int k=0,j=0;
//        ArrayList<Integer> emptyCellsRow1 = new ArrayList<Integer>();
//        ArrayList<Integer> emptyCellsRow2 = new ArrayList<Integer>();
//        for (int i=0; i<21 ; i++){
//            if(board.getRow1()[i] == null && board.getRow1DitchIndex()!= i){
//                k++;
//                emptyCellsRow1.add(i);
//            }
//            if(board.getRow2()[i] == null && board.getRow2DitchIndex()!= i){
//                j++;
//                emptyCellsRow2.add(i);
//            }
//        }
//
//        Random rand = new Random();
//        board.setRow1DitchIndex(emptyCellsRow1.get(rand.nextInt(k)));
//        board.setRow2DitchIndex(emptyCellsRow2.get(rand.nextInt(j)));
//    }
//
//
//    private void ActivateCardThief(int playerOrder) {
//        ArrayList<Card> otherPlayerCards = opponent.getHand(),
//                playerCards = player.getHand();
//        if(otherPlayerCards.isEmpty()){
//            System.out.println("You used this card too late!");
//        }
//        else{
//            Random rand = new Random();
//            int random = 0;
//            random = rand.nextInt(otherPlayerCards.size());
//            System.out.println("Player "+playerOrder + " stole card "+otherPlayerCards.get(random).getName()+
//                    " from player "+ (3-playerOrder)+" !");
//            otherPlayerCards.get(random).extra = true;
//            playerCards.add(otherPlayerCards.get(random));
//            otherPlayerCards.remove(otherPlayerCards.get(random));
//        }
//    }
//
//
//    private void ActivateBuffer(int playerOrder, Board board){
//        Random rand = new Random();
//        if(board.getPlayedCards(playerOrder).isEmpty()){
//            System.out.println("There is no card to buff!");
//        }else {
//            int ran = rand.nextInt(board.getPlayedCards(playerOrder).size());
//            if (playerOrder == 1) {
//                int index = indexOf(board.getRow1(), board.getPlayedCards(playerOrder).get(ran));
//                (board.getRow1()[index]).setPlayerDamage((board.getRow1()[index]).getPlayerDamage("-1") * 2);
//                System.out.println("Card " + board.getRow1()[index].getName()
//                        + " is buffed and player damage value is doubled!");
//
//            }
//            if (playerOrder == 2) {
//                int index = indexOf(board.getRow2(), board.getPlayedCards(playerOrder).get(ran));
//                (board.getRow2()[index]).setPlayerDamage((board.getRow2()[index]).getPlayerDamage("-1") * 2);
//                System.out.println("Card " + board.getRow2()[index].getName()
//                        + " is buffed and player damage value is doubled!");
//
//            }
//        }
//    }
//
//    public static <T> int indexOf(T[] arr, T val) {
//        return Arrays.asList(arr).indexOf(val);
//    }
//
//}
