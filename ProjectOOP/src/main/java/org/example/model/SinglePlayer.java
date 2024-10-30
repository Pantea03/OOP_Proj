//package org.example.model;
//
//import java.sql.SQLException;
//import java.util.Random;
//import java.util.Scanner;
//import org.example.menuController.MainMenuController;
//public class SinglePlayer extends Game{
//    private final int TOTAL_STAGES = 4;
//    private int currentStage = 1;
//    private User Winner, Loser;
//    private User computer;
//    private boolean isBossFight = false;
//
//    public SinglePlayer(User player1, Computer computer) {
//        super(player1, computer);
//        this.computer = computer;
//    }
//
//    public User getComputer() {
//        return computer;
//    }
//
//    private boolean playStage(Board board) throws SQLException {
//        // Set up and play a single stage
//        setUpStage();
//        return playRound(board);
//    }
//
//    private void setUpStage() {
//        // Set up the computer character randomly for each stage
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Player1 please select your character: \n Your options are: KIM, PAN, PAR, KAI");
//        String str = scanner.nextLine();
//        str = str.trim();
//        player1.setCharacter(str);
//
//        String[] characters = {"KIM", "PAN", "PAR", "KAI"};
//        Random random = new Random();
//        String computerCharacter = characters[random.nextInt(characters.length)];
//        computer.setCharacter(computerCharacter);
//    }
//
//    private boolean playRound(Board board) throws SQLException {
//        handCards(1, player1, computer);
//        board.printBoard(1);
//        System.out.println("User gets to go first!");
//        boolean DEAD = false;
//
//        while (!DEAD) {
//            RoundSingle round = new RoundSingle(this.player1, this.computer, board, 1, this);
//            round.execute();
//            DEAD = round.isDEAD();
//        }
//
//        givePoints();
//        player1.reset();
//        computer.reset();
//        return player1.getHP() > 0;
//    }
//
//    public void execute() throws SQLException {
//        Board board = new Board(player1, computer);
//        while (currentStage <= TOTAL_STAGES) {
//            System.out.println("Stage " + currentStage + " begins!");
//            if (playStage(board)) {
//                currentStage++;
//                if (currentStage > TOTAL_STAGES) {
//                    isBossFight = true;
//                    System.out.println("Boss fight begins!");
//                    //playBossFight();
//                }
//            } else {
//                System.out.println("You lost. Restarting from stage 1.");
//                currentStage = 1;
//            }
//        }
//        System.out.println("Game over.");
//    }
//
//
//    private void givePoints() {
//        if (player1.getHP() == 0) {
//            this.Winner = computer;
//            this.Loser = player1;
//        } else {
//            this.Winner = player1;
//            this.Loser = computer;
//        }
//        this.Loser.setXP((int) (this.Loser.getXP() + this.Loser.getPlayerPoints()));
//        this.Winner.setXP(this.Winner.getXP() + (int) ((double) (this.Winner.getPlayerPoints() / 100) * 0.5 * this.Winner.getMaxXp()));
//        this.Winner.setCoin(this.Winner.getCoin() + this.Winner.getPlayerPoints() * 2 + 100 * this.Winner.getLevel());
//        Winner.leveledUp();
//        Loser.leveledUp();
//        printStats(Winner, Loser);
//    }
//
//    public void printStats(User winner, User loser) {
//        System.out.println("Stats:");
//        System.out.println("Winner:\t" + winner.getUsername() + "\tLevel:\t" + winner.getLevel() + "\tCoins:\t" + winner.getCoin() + "\tXP:\t" + winner.getXP());
//        System.out.println("Loser:\t" + loser.getUsername() + "\tLevel:\t" + loser.getLevel() + "\tCoins:\t" + loser.getCoin() + "\tXP:\t" + loser.getXP());
//    }
//}
