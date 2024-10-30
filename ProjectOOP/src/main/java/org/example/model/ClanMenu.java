//package org.example.model;
//
//import java.sql.SQLException;
//import java.util.Scanner;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import org.example.menuController.MainMenuController;
//
//public class ClanMenu {
//    Matcher matcher1, matcher2, matcher3, matcher4;
//    Pattern pattern3 = Pattern.compile("create a clan (\\w+)", Pattern.CASE_INSENSITIVE);
//    Pattern pattern4 = Pattern.compile("join (\\d+)", Pattern.CASE_INSENSITIVE);
//    Pattern pattern5 = Pattern.compile("play (\\d+)", Pattern.CASE_INSENSITIVE);
//
//    //QUESTION
//    //can someone log out before clanwar is over?
//
//    Clan currentUserClan;
//    User player;
////    ClanDatabase cdb = new ClanDatabase();
//    ClanWar clanWar;
//    public ClanMenu() {
//        clanWar = new ClanWar();
//    }
//
//    public void execute(Scanner scanner, User player) throws SQLException {
//        this.player = player;
//        this.currentUserClan = player.getMyClan();
//        System.out.println("Options:\n 1)My clan \n 2)Join Clan \n 3)Create Clan");
//        String input;
//        player.setMyClan(ClanDatabase.getClanByPlayerUsername(DatabaseUtil.getConnection(), player.getUsername()));
//
//        while (true) {
//            input = scanner.nextLine();
//            input = input.trim().toLowerCase().replace("  ", "");
//            matcher3 = pattern3.matcher(input);
//            if (input.equals("my clan")) {
//                myClan(scanner);
//            } else if (input.equals("join clan")) {
//                joinClan(scanner);
//            } else if (matcher3.find()) {
//                createClan(matcher3.group(1));
//            } else if (input.equals("back")) {
//                break;
//            } else {
//                System.out.println("Wrong Command!");
//            }
//        }
//
//
//    }
//
//    private void joinClan(Scanner scanner) throws SQLException {
//        String input;
//        do {
//            input = scanner.nextLine();
//            matcher4 = pattern4.matcher(input);
//            if (input.replace("  ", "").equalsIgnoreCase("all clans")) {
//                for (Clan clan : ClanDatabase.getAllClans(DatabaseUtil.getConnection())) {
//                    System.out.println("Clan Name: " + clan.getName() + " Clan Code: " + clan.getCode());
//                }
//            } else if (matcher4.find()) {
//                break;
//            } else {
//                System.out.println("Invalid command!");
//            }
//        }
//        while (!matcher4.find());
//
//        if (matcher4.find()) {
//            for (Clan clan : ClanDatabase.getAllClans(DatabaseUtil.getConnection())) {
//                if (clan.getCode().equals(matcher4.group(1)))
//                    ClanDatabase.addMemberToClan(DatabaseUtil.getConnection(), clan.getName(), player);
//            }
//        }
//
//    }
//
//    private void createClan(String name) throws SQLException {
//        if (player.getCoin() > 100 && !ClanDatabase.doesClanExist(DatabaseUtil.getConnection(), name)) {
//            player.setCoin(player.getCoin() - 100);
//            Clan clan = new Clan(name, player);
//            ClanDatabase.addClan(DatabaseUtil.getConnection(), clan);
//            player.setMyClan(clan);
//        }
//    }
//
//    private void myClan(Scanner scanner) throws SQLException {
//        for (Clan clan : ClanDatabase.getAllClans(DatabaseUtil.getConnection())) {
//            if (clan.getName().equals(this.player.getMyClan().getName())) {
//                player.setMyClan(clan);
//                System.out.println("Clan Name:\t" + clan.getName() + "\tCode:\t" + clan.getCode() +
//                        "\tNumber of members:\t" + clan.getMembers().size() + "\tWins:\t" + clan.getWins() +
//                        "\tLosses:\t" + clan.getLosses() + "\tTies:\t" + clan.getTies() + "\tPoints:\t" + clan.getPoints());
//                break;
//            }
//        }
//        String input = "";
//        boolean foundClan = false;
//        while (!input.equals("back")) {
//
//            input = scanner.nextLine();
//            matcher2 = pattern5.matcher(input);
//            if (input.trim().replace("  ", "").equalsIgnoreCase("play clanWar")) {
//                if (ClanWar.ongoing) {
//                    if(!clanWar.isMember(player)){
//                        System.out.println("You're not a member of the opposing clans!");
//                    }
//                    else if (ClanWar.didParticipate(player)) {
//                        System.out.println("You've already participated!");
//                    } else {
//                        clanWar.setCurrentUser(player);
//                        clanWar.execute();
//                        //how to get this clan war???
//                    }
//                } else {
//                    System.out.println("No clan war has been started yet!");
//                }
//            } else if (matcher2.find()) {
//                if (player.getUsername().equals(player.getMyClan().getBoss().getUsername())){
//                    for(Clan clan: ClanDatabase.getAllClans(DatabaseUtil.getConnection())){
//                        if(clan.getCode().equals(matcher2.group(1))){
//                            foundClan = true;
//                            if(ClanWar.ongoing){
//                                System.out.println("There is already a clan war going on!");
//                            }
//                            else{
//                                clanWar.setBoss(player);
//                                clanWar.setOpponentClan(clan);
//                                clanWar.setScanner(scanner);
//                            }
//
//                        }
//                    }
//                }
//                else{
//                    System.out.println("You're not the boss and can't initiate a clan war.");
//                }
//            }
//        }
//        if (!ClanWar.didParticipate(player)) {
//            System.out.println("There is a clan war going on and you haven't played yet.");
//        }
//
//
//    }
//
//
//}
//
//
