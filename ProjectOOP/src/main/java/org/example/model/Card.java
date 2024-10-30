package org.example.model;

import java.util.ArrayList;
import org.example.menuController.MainMenuController;
public class Card {
    int cardId;
    String name;
    int cost;
    int upgradeCost;
    int upgradeLevel;
//    int playerDamage;
//    int cardDefenseAttack;
    int duration;
    //**************
    int upgradedDefenseAttack;
    String character;
    boolean extra=false, deactivate=false , copiedCard =false;
    int upgradedLevel;


    public Card(String name, int cost, int upgradeCost, int upgradeLevel, int playerDamage, int cardDefenseAttack, int duration) {
        this.name = name;
        this.cost = cost;
        this.upgradeCost = upgradeCost;
        this.upgradeLevel = upgradeLevel;
//        this.playerDamage = playerDamage;
//        this.cardDefenseAttack = cardDefenseAttack;
        this.duration = duration;
        this.upgradedLevel = 0;
    }

    public Card() {
    }
    public Card(Card card) {
        this.name = card.name;
        this.duration =card.duration;
        this.upgradeLevel = card.upgradeLevel;
        this.upgradeCost =card.upgradeCost;
        this.character = card.character;
        this.cost = card.cost;
        this.playerDamage = card.playerDamage;
        this.cardDefenseAttack = card.cardDefenseAttack;
        fillDamagePerCell();
        fillBrokenCells();

    }
//    public Card(Card card, int playerDamage, int cardDefenseAttack) {
//        this.name = card.name;
//        this.duration =card.duration;
//        this.upgradeLevel = card.upgradeLevel;
//        this.upgradeCost =card.upgradeCost;
//        this.character = card.character;
//        this.cost = card.cost;
//        this.playerDamage = playerDamage;
//        this.cardDefenseAttack = cardDefenseAttack;
//        fillDamagePerCell();
//        fillBrokenCells();
//    }

    // Getters and Setters
    public int getCardId() { return cardId; }
    public String getName() { return name; }
    public int getCost() { return cost; }
    public int getUpgradeCost() { return upgradeCost; }
    public int getUpgradeLevel() { return upgradeLevel; }
//    public int getPlayerDamage() { return playerDamage; }
//    public int getCardDefenseAttack() { return cardDefenseAttack; }
    public int getDuration() { return duration; }

    public int getUpgradedDefenseAttack() {
        return upgradedDefenseAttack;
    }
    public String getCharacter() {
        return character;
    }
    public void setCharacter(String character) {
        this.character = character;
    }
    public void setCost(int cost) {this.cost = cost;}

    public void setDeactivate(Boolean deactivate) {
        this.deactivate = deactivate;
    }

    public void setCopiedCard(Boolean copiedCard) {
        this.copiedCard = copiedCard;
    }
    public void setName(String name) { this.name = name; }
    public void setDuration(int d) { this.duration = d; }

    public void setUpgradeCost(int upgradeCost) {
        this.upgradeCost = upgradeCost;
    }

    public void setUpgradeLevel(int upgradeLevel) {
        this.upgradeLevel = upgradeLevel;
    }

    // ****************************************************
    int playerDamage;
    int cardDefenseAttack;
    int numberOfBrokenCells=0;
    private ArrayList<Boolean> brokenCells = new ArrayList<>();
    ArrayList<Integer> damagePerCell = new ArrayList<>();
    Card(ArrayList<String> cmd){
        this.name = cmd.get(0);
        this.cardDefenseAttack = Integer.parseInt(cmd.get(1));
        this.duration = Integer.parseInt(cmd.get(2));
        this.playerDamage = Integer.parseInt(cmd.get(3));
        this.upgradedLevel = Integer.parseInt(cmd.get(4));
        this.upgradeCost = Integer.parseInt(cmd.get(5));
        fillDamagePerCell();
        fillBrokenCells();
    }

    public int getPlayerDamage(String character) {
        if(this.character.equals(character)){
            return playerDamage+5*this.duration;
        }
        else{ //"-1" for normal mode
            return playerDamage;
        }

    }
    public String getType() {
        if (execution != null) {
            return "Spell";
        }
        else {
            return "Attack/Defense";
        }
    }

    public int getCardDefenseAttack(){
        return this.cardDefenseAttack;
    }

    public void printDetails() {
        System.out.println("Card name:\t"+ this.name+"\tDuration:\t"+this.duration+
                "\tDefense/Attack:\t"+this.cardDefenseAttack+"\tPlayer damage:\t"+this.playerDamage+
                "\tCharacter\t"+this.character+"\tExecution:\t"+this.execution);
    }

    protected void lowerDamage() {
        this.playerDamage-=5;
    }
    protected void lowerPower(){
        this.cardDefenseAttack-=5;
    }

    public void setPlayerDamage(int playerDamage) {
        this.playerDamage = playerDamage;
        fillDamagePerCell();
    }

    public void setCardDefenseAttack(int cardDefenseAttack) {
        this.cardDefenseAttack = cardDefenseAttack;
    }

    private void fillDamagePerCell() {
        this.damagePerCell.clear();
        for(int i=0; i< this.duration; i++){
            this.damagePerCell.add(this.playerDamage/this.duration);
        }
    }
    public void fillBrokenCells(){
        for(int i=0; i< this.duration; i++){
            this.brokenCells.add(false);
        }
    }

    public int getNumberOfBrokenCells() {
        return numberOfBrokenCells;
    }

    public void setBrokenCell(int number_of_broken_Cells, int cellIndex) {
        this.numberOfBrokenCells = number_of_broken_Cells;
        this.brokenCells.set(cellIndex, true);

    }

    public boolean isCellBroken(int index){
        return this.brokenCells.get(index);
    }

    public int getDamagePerCell(String character, int index) {
        if(this.character.equals(character)){
            return damagePerCell.get(index)+5;
        }
        else{  //"-1" for normal mode
            return damagePerCell.get(index);
        }
    }

    public void setDamagePerCelL(int value, int index) {
        this.damagePerCell.set(index,value);
    }

    protected void setExtra(boolean b) {
        this.extra = b;
    }

    public Boolean getExtra() {
        return extra;
    }

    public int getPlayerDamage() {
        return playerDamage;
    }
    // *************************************

    public String execution;

    String type;
//    public void Activate(int playerOrder, User player, Hand hand, Board board, Round round){}


}


