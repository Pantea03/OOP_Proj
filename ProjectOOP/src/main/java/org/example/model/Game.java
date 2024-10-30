//package org.example.model;
//
//import java.sql.SQLException;
//import java.util.*;
//import org.example.menuController.MainMenuController;
//public class Game {
//
//    private int turns=0;
//
//    User player1, player2 = new User();
//    Game(User player1, User player2){
//        this.player1 = player1;
//        this.player2 = player2;
//    }
//
//    Game(){}
//    public int getTurns() {
//        return turns;
//    }
//
//    public void setTurns(int turns) {
//        this.turns = turns;
//    }
//
//
//    protected void execute() throws SQLException {
//    }
//
//    protected void handCards(int b, User player1, User player2) throws SQLException {
//        Random rand = new Random();
//        Scanner scanner = new Scanner(System.in);
//        if (b == 0) {
//            List<Card> user1Cards = player1.getUserCards(DatabaseUtil.getConnection());
//            int j = 1;
//            System.out.println("Player1 cards:");
//            for (Card storeCard : user1Cards) {
//                    System.out.println(j + ".Name: " + storeCard.name + "   Cost: " + storeCard.cost + "   Duration: " +
//                            storeCard.duration + "   Upgrade Cost: " + storeCard.upgradeCost + "   Upgrade Level: " +
//                            storeCard.upgradeLevel + "   Defense/Attack: " + (storeCard).cardDefenseAttack +
//                            "   Player Damage: " + (storeCard).playerDamage+"   Execution: " + (storeCard).execution);
//                j ++;
//            }
//            List<Card> user2Cards = player2.getUserCards(DatabaseUtil.getConnection());
//            j = 1;
//            System.out.println("Player2 cards:");
//            for (Card storeCard : user2Cards) {
//                System.out.println(j + ".Name: " + storeCard.name + "   Cost: " + storeCard.cost + "   Duration: " +
//                        storeCard.duration + "   Upgrade Cost: " + storeCard.upgradeCost + "   Upgrade Level: " +
//                        storeCard.upgradeLevel + "   Defense/Attack: " + (storeCard).cardDefenseAttack +
//                        "   Player Damage: " + (storeCard).playerDamage+"   Execution: " + (storeCard).execution);
//                j ++;
//            }
//            int index=1;
//            System.out.println("Player1 :choose 20 cards to add to your deck.");
//            player1.getDeck().clear();
//            for (int i = 1; i <= 20; i++) {
//                index = Integer.parseInt(scanner.nextLine());
//                player1.getDeck().add(user1Cards.get(index - 1));
//            }
//            System.out.println("Player2 :choose 20 cards to add to your deck.");
//            player2.getDeck().clear();
//            for (int i = 1; i <= 20; i++) {
//                index = Integer.parseInt(scanner.nextLine());
//                player2.getDeck().add(user2Cards.get(index - 1));
//            }
//        }
//        else if (b == 1) {
//            List<Card> user1Cards = player1.getUserCards(DatabaseUtil.getConnection());
//            int j = 1;
//            System.out.println("Player1 cards:");
//            for (Card storeCard : user1Cards) {
//                System.out.println(j + ".Name: " + storeCard.name + "   Cost: " + storeCard.cost + "   Duration: " +
//                        storeCard.duration + "   Upgrade Cost: " + storeCard.upgradeCost + "   Upgrade Level: " +
//                        storeCard.upgradeLevel + "   Defense/Attack: " + (storeCard).cardDefenseAttack +
//                        "   Player Damage: " + (storeCard).playerDamage+"   Execution: " + (storeCard).execution);
//                j ++;
//            }
//            for (Card card: Store.getAllCards(DatabaseUtil.getConnection())){
//                if(card.execution == null){
//                    player2.getDeck().add(card);
//                }
//            }
////            player2.setDeck((ArrayList<Card>) Store.getAllCards(DatabaseUtil.getConnection()));
//            int index=1;
//            System.out.println("Player1 :choose 20 cards to add to your deck.");
//            player1.getDeck().clear();
//            for (int i = 1; i <= 20; i++) {
//                index = Integer.parseInt(scanner.nextLine());
//                player1.getDeck().add(user1Cards.get(index - 1));
//            }
//        }
//        ArrayList<Integer> indexer = new ArrayList<>();
//        for(int i=0; i< player1.getDeck().size(); i++){
//            indexer.add(i);
//        }
//
//        Collections.shuffle(indexer);
//        ArrayList<Card> player1Hand = new ArrayList<>();
//        for(int i: indexer.subList(0, 5)){
//            player1Hand.add(player1.getDeck().get(i));
//        }
////        player1Hand.add(player1.getDeck().get(4));
////        player1Hand.add(player1.getDeck().get(6));
//        player1.setHand(player1Hand);
//
//        indexer.clear();
//        for(int i=0; i< player2.getDeck().size(); i++){
//            indexer.add(i);
//        }
//        Collections.shuffle(indexer);
//        ArrayList<Card> player2Hand = new ArrayList<>();
//        for(int i: indexer.subList(0, 5)){
//            player2Hand.add(player2.getDeck().get(i));
//        }
////        player2Hand.add(player2.getDeck().get(0));
////        player2Hand.add(player2.getDeck().get(1));
//        player2.setHand(player2Hand);
//    }
//
//
//}
