//package org.example.model;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import org.example.menuController.MainMenuController;
//public class SignUPMenu extends Menu {
//    static ArrayList<User> users = new ArrayList<>();
//    String name = "Signup";
//    public SignUPMenu(String name) {
//        super(name);
//    }
//    public static void createUser(Connection conn, String username, String password, String passwordConfirmation, String email, String nickname,
//                                  int questionNumber, String answer) throws SQLException {
//        String question = "";
//        if (questionNumber == 1){
//            question = "What is your father’s name ?";
//        }
//        else if (questionNumber == 2){
//            question = "What is your favourite color ?";
//        }
//        else if (questionNumber == 3){
//            question = "What was the name of your first pet?";
//        }
//        User user = new User(username, password, nickname, email, question, answer);
//        user.save(conn);
//        SignUPMenu.users.add(user);
//    }
//
//}
