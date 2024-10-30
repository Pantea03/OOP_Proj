//package org.example.model;
//
//import org.example.menuController.MainMenuController;
//import org.example.GameController;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.SQLOutput;
//import java.util.*;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class InputProcessor {
//    Scanner scanner = new Scanner(System.in);
//    boolean exit = false;
//    String input;
//    public static User currentUser;
//    public static Menu currentMenu;
//    int currentPage = 1;
//    private static Map<Character, String[]> asciiArtMap = new HashMap<>();
//    private static final int MAX_FAILED_ATTEMPTS = 3; // Maximum allowed failed attempts
//    private static final long LOCKOUT_BASE_TIME_MS = 5000; // Base lockout time in milliseconds (5 seconds)
//    private static ArrayList<GameHistory> historyList;
//    private int failedAttempts = 0;
//    private long lastFailedTime = 0; // Timestamp of the last failed attempt
//    private static Random random = new Random();
//    public static boolean isSecondUser = false;
//    public static User secondUser;
//    public static boolean doublePlayer = false;
//    public static boolean gamblePlayer = false;
//    public static boolean singlePlayer = false;
//    public static boolean clanPlayer = false;
//    Computer computer;
//    public void start() throws SQLException {
//        initializeAsciiArt();
//        // Check if predefined cards already exist
//        if (!Store.arePredefinedCardsExist(DatabaseUtil.getConnection())) {
//            Store.getPredefinedCards(DatabaseUtil.getConnection());
//        }
//        currentMenu = new Menu("main");
//        MainMenuController mainMenuController = new MainMenuController(scanner);
//        while (!exit) {
//            if (currentMenu.name.equals("main")) {
//                mainMenuController.entrance();
//            }
//            else {
//                input = scanner.nextLine().trim();
//                if (currentMenu.name.equals("profile")) {
//                    if (currentUser == null) {
//                        System.out.println("You have to log in first!");
//                    }
//                    else {
//                        if (input.matches("Show information")) {
//                            if (currentUser == null) {
//                                System.out.println("You have to log in first!");
//                                currentMenu.name = "main";
//                            } else if (!currentUser.isLoggedIn(DatabaseUtil.getConnection())) {
//                                System.out.println("You have to log in first!");
//                                currentMenu.name = "main";
//                            } else if (currentUser.isLoggedIn(DatabaseUtil.getConnection())) {
//                                Matcher matcher = getCommandMatcher(input, "Show information");
//                                matcher.find();
//                                processShowInfo(matcher);
//                            }
//                        } else if (input.matches("Profile change -u (?<username>.+)")) {
//                            Matcher matcher = getCommandMatcher(input, "Profile change -u (?<username>.+)");
//                            matcher.find();
//                            processChangeUsername(matcher);
//                        } else if (input.matches("Profile change -n (?<nickname>.+)")) {
//                            Matcher matcher = getCommandMatcher(input, "Profile change -n (?<nickname>.+)");
//                            matcher.find();
//                            processChangeNickname(matcher);
//                        } else if (input.matches("Profile change password -o (?<oldPassword>.+) -n (?<newPassword>.+)")) {
//                            Matcher matcher = getCommandMatcher(input, "Profile change password -o (?<oldPassword>.+) -n (?<newPassword>.+)");
//                            matcher.find();
//                            processChangePassword(matcher);
//                        } else if (input.matches("Profile change -e (?<email>.+)")) {
//                            Matcher matcher = getCommandMatcher(input, "Profile change -e (?<email>.+)");
//                            matcher.find();
//                            processChangeEmail(matcher);
//                        } else if (input.matches("back")) {
//                            currentMenu.name = "main";
//                        }
//                    }
//                } else if (currentMenu.name.equals("signIn")) {
//                    if (input.matches("user create\\s*-u\\s*(?<username>[^\\s]*)?\\s*-p\\s*(?<password>[^\\s]*)?\\s*(?<passwordConfirmation>[^\\s]*)?\\s*–email\\s*(?<email>[^\\s]*)?\\s*-n\\s*(?<nickname>[^\\s]*)?")) {
//                        Matcher matcher = getCommandMatcher(input, "user create\\s*-u\\s*(?<username>[^\\s]*)?\\s*-p\\s*(?<password>[^\\s]*)?\\s*(?<passwordConfirmation>[^\\s]*)?\\s*–email\\s*(?<email>[^\\s]*)?\\s*-n\\s*(?<nickname>[^\\s]*)?");
//                        if (matcher.find()) {
//                            String username = matcher.group("username");
//                            while (true) {
//                                if (username.matches("^[a-zA-Z0-9_]+$")) {
//                                    break;
//                                } else {
//                                    System.out.println("Invalid username. Username must only contain English letters, numbers, and underscores.");
//                                    username = matcher.group("username");
//                                    input = scanner.nextLine().trim();
//                                    matcher = getCommandMatcher(input, "user create\\s*-u\\s*(?<username>[^\\s]*)?\\s*-p\\s*(?<password>[^\\s]*)?\\s*(?<passwordConfirmation>[^\\s]*)?\\s*–email\\s*(?<email>[^\\s]*)?\\s*-n\\s*(?<nickname>[^\\s]*)?");
//                                    if (matcher.find()) {
//                                        username = matcher.group("username");
//                                    }
//                                }
//                            }
//                            processCreateUser(matcher);
//                        }
//                    }
//                    else if (input.matches("back")) {
//                        currentMenu.name = "main";
//                    }
//                } else if (currentMenu.name.equals("logIn")) {
//                    if (input.matches("user login -u (?<username>.+) -p (?<password>.+)")) {
//                        Matcher matcher = getCommandMatcher(input, "user login -u (?<username>.+) -p (?<password>.+)");
//                        if (matcher.find()) {
//                            processLogin(matcher);
//                        }
//                    } else if (input.matches("Forgot my password -u (?<username>.+)")) {
//                        Matcher matcher = getCommandMatcher(input, "Forgot my password -u (?<username>.+)");
//                        if (matcher.find()) {
//                            processForgotPassword(matcher);
//                        }
//                    } else if (input.matches("log out")) {
//                        currentUser.updateLoggedInState(DatabaseUtil.getConnection(), false);
//                        currentMenu.name = "main";
//                    } else if (input.matches("-login admin (?<pass>.+)")) {
//                        Matcher matcher = getCommandMatcher(input, "-login admin (?<pass>.+)");
//                        if (matcher.find()) {
//                            processAdminLogin(matcher);
//                        }
//                    }
//                    else if (input.matches("back")) {
//                        currentMenu.name = "main";
//                    }
//                } else if (currentMenu.name.equals("gameHistory")) {
//                    if (input.matches("Show game history")) {
//                        if (currentUser == null) {
//                            System.out.println("You have to log in first!");
//                            currentMenu.name = "main";
//                        }
//                        else {
//                            // **********************************************
//                            GameHistory.handleGameHistoryCommands(scanner, DatabaseUtil.getConnection(), currentUser.getUserId(), historyList, 20, currentPage);
//                        }
//                    }
//                    else if (input.matches("back")) {
//                        currentMenu.name = "main";
//                    }
//                } else if (currentMenu.name.equals("store")) {
//                    if (input.matches("Show the cards I don't have")) {
//                        if (currentUser == null) {
//                            System.out.println("You have to log in first!");
//                        }
//                        else {
//                            List<Card> storeCards = Store.getAvailableCards(DatabaseUtil.getConnection(), currentUser);
//                            for (Card storeCard : storeCards) {
//                                    System.out.println("Name: " + storeCard.name + "   Cost: " + storeCard.cost + "   Duration: " +
//                                            storeCard.duration + "   Upgrade Cost: " + storeCard.upgradeCost + "   Upgrade Level: " +
//                                            storeCard.upgradeLevel + "   Defense/Attack: " + (storeCard).cardDefenseAttack +
//                                            "   Player Damage: " + (storeCard).playerDamage +  "   Execution: " + (storeCard).execution);
//                                }
//
//                        }
//                    } else if (input.matches("Buy card -n (?<name>.+)")) {
//                        if (currentUser == null) {
//                            System.out.println("You have to log in first!");
//                        } else {
//                            Matcher matcher = getCommandMatcher(input, "Buy card -n (?<name>.+)");
//                            if (matcher.find()) { // Use matcher.find() to find the match
//                                String cardName = matcher.group("name");
//                                processBuyCard(cardName);
//                            } else {
//                                System.out.println("Invalid command format.");
//                            }
//                        }
//                    } else if (input.matches("Show the upgradable cards I have")) {
//                        if (currentUser == null) {
//                            System.out.println("You have to log in first!");
//                        }
//                        else {
//                            List<Card> userCards = Store.getUserCards(DatabaseUtil.getConnection());
//                            for (Card storeCard : userCards) {
//                                System.out.println("Name: " + storeCard.name + "   Cost: " + storeCard.cost + "   Duration: " +
//                                        storeCard.duration + "   Upgrade Cost: " + storeCard.upgradeCost + "   Upgrade Level: " +
//                                        storeCard.upgradeLevel + "   Defense/Attack: " + (storeCard).cardDefenseAttack +
//                                        "   Player Damage: " + (storeCard).playerDamage +  "   Execution: " + (storeCard).execution);
//                            }
//                        }
//                    }
//                    else if (input.matches("Upgrade card -n (?<name>.+)")) {
//                        if (currentUser == null) {
//                            System.out.println("You have to log in first!");
//                        } else {
//                            Matcher matcher = getCommandMatcher(input, "Upgrade card -n (?<name>.+)");
//                            if (matcher.find()) { // Use matcher.find() to find the match
//                                String cardName = matcher.group("name");
//                                processUpgradeCard(cardName);
//                            } else {
//                                System.out.println("Invalid command format.");
//                            }
//                        }
//                    }
//                    else if (input.matches("back")) {
//                        currentMenu.name = "main";
//                    }
//                } else if (currentMenu.name.equals("game")) {
//                    if (currentUser == null) {
//                        System.out.println("You have to log in first!");
//                        currentMenu.name = "main";
//                    }
//                    else {
//                        // ************************
//                        if (input.matches("start game")) {
//                            if (secondUser != null) {
//                                if (doublePlayer) {
//                                    GameController.execute(currentUser, secondUser);
//                                    doublePlayer = false;
//                                }
//                                else if (gamblePlayer) {
//                                    GameController.execute(currentUser, secondUser);
//                                    gamblePlayer = false;
//                                }
//                            }
//                            else if (singlePlayer) {
//                                GameController.executeSingle(currentUser);
//                                singlePlayer = false;
//                            }
//                            else if (clanPlayer) {
//                                GameController.executeClan(scanner, currentUser);
//                                clanPlayer = false;
//                            }
//                            else {
//                                GameController.start(scanner, currentUser.getUsername(), DatabaseUtil.getConnection());
//                            }
//                        }
//                        if (input.matches("back")) {
//                            currentMenu.name = "main";
//                        }
//                    }
//                }
//            }
//        }
//    }
//    private Matcher getCommandMatcher(String input, String regex) {
//        Pattern pattern = Pattern.compile(regex);
//        return pattern.matcher(input);
//    }
//    private void processShowInfo(Matcher matcher) throws SQLException {
//        String username = currentUser.getUsername();
//        User user = ProfileMenu.showInformation(DatabaseUtil.getConnection(), username);
//        if (user != null){
//            System.out.println("Username: " + user.getUsername());
//            System.out.println("Nickname: " + user.getNickname());
//            System.out.println("Email: " + user.getEmail());
//            System.out.println("Password: " + user.getPassword());
//            System.out.println("Security Question: " + user.getSecurityQuestion());
//            System.out.println("Security Answer: " + user.getSecurityAnswer());
//        }
//        else {
//            System.out.println("model.User not found.");
//        }
//    }
//    private void processChangeUsername(Matcher matcher) throws SQLException {
//        String newUsername = matcher.group("username");
//        if (ProfileMenu.canChangeUsername(DatabaseUtil.getConnection(), currentUser.getUsername(), newUsername) == 1) {
//            ProfileMenu.changeUsername(DatabaseUtil.getConnection(), currentUser, newUsername);
////            currentUser.updateLoggedInState(model.DatabaseUtil.getConnection(), true);
//            System.out.println("Username updated successfully!");
//        } else if (ProfileMenu.canChangeUsername(DatabaseUtil.getConnection(), currentUser.getUsername(), newUsername) == 0) {
//            System.out.println("No change in the username!");
//        } else {
//            System.out.println("User not found.");
//        }
//    }
//
//    private void processChangeNickname(Matcher matcher) throws SQLException {
//        String nickname = matcher.group("nickname");
//        if (ProfileMenu.canChangeNickname(DatabaseUtil.getConnection(), currentUser.getUsername(), nickname) == 1){
//            ProfileMenu.changeNickname(DatabaseUtil.getConnection(), currentUser.getUsername(), nickname);
//            System.out.println("Nickname updated successfully!");
//        }
//        else if (ProfileMenu.canChangeNickname(DatabaseUtil.getConnection(), currentUser.getUsername(), nickname) == 0){
//            System.out.println("No change in the nickname!");
//        }
//        else {
//            System.out.println("User not found.");
//        }
//    }
//
//    private void processChangePassword(Matcher matcher) throws SQLException {
//        String oldPassword = matcher.group("oldPassword");
//        String newPassword = matcher.group("newPassword");
//        int n = ProfileMenu.changePassword(DatabaseUtil.getConnection(), currentUser.getUsername(), oldPassword, newPassword);
//        if (n == 1){
//            System.out.println("Current password is incorrect!");
//        }
//        else if (n == 2){
//            System.out.println("Please enter a new password!");
//        }
//        else if (n == 31){
//            System.out.println("Weak password. Must be at least 8 characters long.");
//        }
//        else if (n == 32){
//            System.out.println("Weak password. Must contain uppercase, lowercase, and a special character.");
//        }
//        else if (n == 4){
//            System.out.println("CAPTCHA verification failed. Please try again.");
//        }
//        else if (n == 5){
//            System.out.println("Please enter your new password again.");
//            String passConfirm = scanner.nextLine();
//            if (!passConfirm.equals(newPassword)){
//                System.out.println("Password is not confirmed!");
//            }
//            else {
//                // CAPTCHA verification
//                boolean captchaVerified = false;
//                while (!captchaVerified) {
//                    captchaVerified = verifyCaptcha();
//                    if (!captchaVerified) {
//                        System.out.println("CAPTCHA verification failed. Please try again.");
//                    }
//                }
//                System.out.println("Password updated successfully.");
//            }
//        }
//        else if (n == 6){
//            System.out.println("Password update failed.");
//        }
//        else {
//            System.out.println("User not found.");
//        }
//    }
//
//    private void processChangeEmail(Matcher matcher) throws SQLException {
//        String email = matcher.group("email");
//        if (ProfileMenu.changeEmail(DatabaseUtil.getConnection(), currentUser.getUsername(), email) == 1){
//            System.out.println("Email updated successfully!");
//        }
//        else if (ProfileMenu.changeEmail(DatabaseUtil.getConnection(), currentUser.getUsername(), email) == 0){
//            System.out.println("No change in the email!");
//        }
//        else {
//            System.out.println("User not found!");
//        }
//    }
//
//    private void processCreateUser(Matcher matcher) throws SQLException {
//        boolean success = true;
//        String username = matcher.group("username");
//        String password = matcher.group("password");
//        String passwordConfirmation = matcher.group("passwordConfirmation");
//        String email = matcher.group("email");
//        String nickname = matcher.group("nickname");
//
//        if (username.isEmpty()) {
//            System.out.println("Username field is empty!");
//            success = false;
//        } else if (password.isEmpty()) {
//            System.out.println("Password field is empty!");
//            success = false;
//        } else if (passwordConfirmation.isEmpty()) {
//            if (password.equals("random")) {
//                password = generateRandomPassword();
//                System.out.println("Your random password: " + password);
//                System.out.println("Please enter your password:");
//                String my_password = scanner.nextLine();
//                if (!password.equals(my_password)) {
//                    System.out.println("Wrong password!");
//                    success = false;
//                }
//            } else {
//                System.out.println("Password confirmation field is empty!");
//                success = false;
//            }
//        } else if (email.isEmpty()) {
//            System.out.println("Email field is empty!");
//            success = false;
//        } else if (nickname.isEmpty()) {
//            System.out.println("Nickname field is empty!");
//            success = false;
//        } else if (Menu.userAlreadyExists(username, DatabaseUtil.getConnection())) {
//            System.out.println("This user already exists!");
//            success = false;
//        } else if (password.length() < 8) {
//            System.out.println("Weak password. Must be at least 8 characters long.");
//            success = false;
//        } else if (!password.equals(passwordConfirmation)) {
//            System.out.println("Password is not confirmed!");
//            success = false;
//        }
//        else if (!Pattern.compile("[a-z]").matcher(password).find() ||
//                !Pattern.compile("[A-Z]").matcher(password).find() ||
//                !Pattern.compile("[^a-zA-Z0-9]").matcher(password).find()) {
//            System.out.println("Weak password. Must contain uppercase, lowercase, and a special character.");
//            success = false;
//        } else if (!email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
//            System.out.println("Invalid email format.");
//            success = false;
//        }
//        if (success) {
//            System.out.println("User created successfully. Please choose a security question :\n" +
//                    "• 1-What is your father’s name ?\n" +
//                    "• 2-What is your favourite color ?\n" +
//                    "• 3-What was the name of your first pet?");
//            String questionPick = scanner.nextLine();
//            if (questionPick.matches("question pick -q (?<questionNumber>\\d+) -a (?<answer>.+) -c (?<answerConfirm>.+)")) {
//                Matcher matcher1 = getCommandMatcher(questionPick, "question pick -q (?<questionNumber>\\d+) -a (?<answer>.+) -c (?<answerConfirm>.+)");
//                if (matcher1.find()) {
//                    int questionNumber = Integer.parseInt(matcher1.group("questionNumber"));
//                    String answer = matcher1.group("answer");
//                    String answerConfirm = matcher1.group("answerConfirm");
//                    if (!answerConfirm.equals(answer)) {
//                        System.out.println("Answer is not confirmed!");
//                    } else {
//                        // CAPTCHA verification
//                        boolean captchaVerified = false;
//                        while (!captchaVerified) {
//                            captchaVerified = verifyCaptcha();
//                            if (!captchaVerified) {
//                                System.out.println("CAPTCHA verification failed. Please try again.");
//                            }
//                        }
//                        System.out.println("User created successfully!");
//                        SignUPMenu.createUser(DatabaseUtil.getConnection(), username, password, passwordConfirmation, email, nickname, questionNumber, answer);
//                        User signedInUser = User.fetchByUsername(DatabaseUtil.getConnection(), username);
//                        signedInUser.giftPack(DatabaseUtil.getConnection());
//                    }
//                }
//            }
//        }
//    }
//    private void processLogin(Matcher matcher) throws SQLException {
//        String username = matcher.group("username");
//        String password = matcher.group("password");
//        if (!MainGameMenu.userAlreadyExists(username, DatabaseUtil.getConnection())){
//            System.out.println("Username doesn't exist!");
//        }
//        else if (!MainGameMenu.passwordMatchesUsername(username, password, DatabaseUtil.getConnection())){
//            System.out.println("Password and Username don’t match!");
//            handleLockout(username);
//        }
//        else if (MainGameMenu.isAUserLoggedIn(username, DatabaseUtil.getConnection()) && !doublePlayer && !gamblePlayer){
//            System.out.println("Another user is logged in!");
//        }
//        else {
//            if (isSecondUser){
//                secondUser = User.fetchByUsername(DatabaseUtil.getConnection(), username);
//                System.out.println("User logged in successfully!");
//                currentUser.updateFromDatabase(DatabaseUtil.getConnection());
//                isSecondUser = false;
//                currentMenu.setName("game");
//            }
//            else {
//                currentUser = User.fetchByUsername(DatabaseUtil.getConnection(), username);
//                currentUser.updateLoggedInState(DatabaseUtil.getConnection(), true);
//                System.out.println("User logged in successfully!");
//                currentUser.updateFromDatabase(DatabaseUtil.getConnection());
//            }
//        }
//    }
//    private void processForgotPassword(Matcher matcher) throws SQLException {
//        String username = matcher.group("username");
//        System.out.println("Please answer this question: ");
//        System.out.println(User.fetchByUsername(DatabaseUtil.getConnection(), username));
//        String answer = scanner.nextLine();
//        if (answer.equals(User.fetchByUsername(DatabaseUtil.getConnection(), username).getSecurityAnswer())){
//            System.out.println("Choose a new Password!");
//            String newPassword = scanner.nextLine();
//            if (ProfileMenu.changePassword(DatabaseUtil.getConnection(), username, newPassword) == 1){
//                System.out.println("Password updated successfully.");
//            }
//            else if (ProfileMenu.changePassword(DatabaseUtil.getConnection(), username, newPassword) == 0){
//                System.out.println("Password update failed.");
//            }
//            else {
//                System.out.println("User not found.");
//            }
//        }
//    }
//    private void processBuyCard(String cardName) {
//        try (Connection conn = DatabaseUtil.getConnection()) {
//            // Handle the purchase logic
//            if (Store.buyCardByName(conn, cardName)) {
//                System.out.println("Purchase was made successfully!");
//            } else {
//                System.out.println("Purchase was not successful!");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("An error occurred while processing the purchase.");
//        }
//    }
//    private void processUpgradeCard(String cardName) {
//        try (Connection conn = DatabaseUtil.getConnection()) {
//            // Handle the purchase logic
//            if (Store.upgradeCard(conn, cardName)) {
//                System.out.println("Card was upgraded successfully!");
//            } else {
//                System.out.println("Upgrade was not successful!");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("An error occurred while processing the upgrade.");
//        }
//    }
//    private void processAdminLogin(Matcher matcher) throws SQLException {
//        String pass = matcher.group("pass");
//        if (pass.equals("adminPass")) {
//            System.out.println("Admin logged in successfully");
//            AdminMenu.execute(scanner);
//        }
//        else {
//            System.out.println("Wrong password for admin");
//        }
//    }
//    private void handleLockout(String username) {
//        // Calculate lockout time based on failed attempts
//        failedAttempts++;
//        long currentTime = System.currentTimeMillis();
//        long lockoutTime = LOCKOUT_BASE_TIME_MS * (failedAttempts + 1);
//
//        // Calculate time left until next attempt can be made
//        long elapsedSinceLastFailed = currentTime - lastFailedTime;
//        long waitTime = lockoutTime - elapsedSinceLastFailed;
//
//        if (waitTime > 0) {
//            // model.User is locked out
//            System.out.println("Try again in " + (waitTime / 1000) + " seconds");
//        } else {
//            // Reset failed attempts if lockout time has passed
//            resetFailedAttempts();
//        }
//
//        lastFailedTime = currentTime;
//    }
//    private void resetFailedAttempts() {
//        failedAttempts = 0;
//        lastFailedTime = 0;
//    }
//    private static String generateRandomPassword() {
//        int length = 12;
//        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
//        StringBuilder password = new StringBuilder(length);
//        for (int i = 0; i < length; i++) {
//            password.append(characters.charAt(random.nextInt(characters.length())));
//        }
//        return password.toString();
//    }
//
//    private static void initializeAsciiArt() {
//        asciiArtMap.put('0', new String[]{
//                " .d8888b. ",
//                "d88P  Y88b",
//                "888    888",
//                "888    888",
//                "888    888",
//                "888    888",
//                "Y88b  d88P",
//                " 'Y8888P' "
//        });
//        asciiArtMap.put('1', new String[]{
//                "  .d888   ",
//                " d88P     ",
//                "  888     ",
//                "  888     ",
//                "  888     ",
//                "  888     ",
//                "  888     ",
//                "  888     "
//        });
//        asciiArtMap.put('2', new String[]{
//                " .d8888b. ",
//                "d88P  Y88b",
//                "      .d88",
//                "    .d88P ",
//                "  .d88P   ",
//                " .d88P    ",
//                "d88P  .d88",
//                "8888888P' "
//        });
//        asciiArtMap.put('3', new String[]{
//                " .d8888b. ",
//                "d88P  Y88b",
//                "     .d88P",
//                "   .d88P  ",
//                "    8888' ",
//                "     Y8b. ",
//                "Y88b  d88P",
//                " 'Y8888P' "
//        });
//        asciiArtMap.put('4', new String[]{
//                "    d8888 ",
//                "   d8P888 ",
//                "  d8P 888 ",
//                " d8P  888 ",
//                "d88   888 ",
//                "8888888888",
//                "      888 ",
//                "      888 "
//        });
//        asciiArtMap.put('5', new String[]{
//                "8888888888",
//                "888       ",
//                "888       ",
//                "8888888b. ",
//                "     'Y88b",
//                "       888",
//                "Y88b  d88P",
//                " 'Y8888P' "
//        });
//        asciiArtMap.put('6', new String[]{
//                " .d8888b. ",
//                "d88P  Y88b",
//                "888       ",
//                "888d888b. ",
//                "888P 'Y88b",
//                "888    888",
//                "Y88b  d88P",
//                " 'Y8888P' "
//        });
//        asciiArtMap.put('7', new String[]{
//                "8888888888",
//                "      d88P",
//                "     d88P ",
//                "    d88P  ",
//                "   d88P   ",
//                "  d88P    ",
//                " d88P     ",
//                "d88P      "
//        });
//        asciiArtMap.put('8', new String[]{
//                " .d8888b. ",
//                "d88P  Y88b",
//                "Y88b. d88P",
//                " 'Y8888P' ",
//                " .d88P88b.",
//                "888  8888",
//                "Y88b  d88P",
//                " 'Y8888P' "
//        });
//        asciiArtMap.put('9', new String[]{
//                " .d8888b. ",
//                "d88P  Y88b",
//                "888    888",
//                "Y88b. d888",
//                " 'Y888P888",
//                "     .d88P",
//                "    8888P ",
//                "    888P  "
//        });
//    }
//
//    private boolean verifyCaptcha() {
//        String captcha = generateCaptcha();
//        displayCaptcha(captcha);
//        System.out.print("Enter the CAPTCHA: ");
//        String inputCaptcha = scanner.nextLine();
//        return verifyCaptcha(inputCaptcha, captcha);
//    }
//
//    private static String generateCaptcha() {
//        int captchaLength = random.nextInt(3) + 5;
//        String captchaCharacters = "0123456789";
//        StringBuilder captcha = new StringBuilder();
//        for (int i = 0; i < captchaLength; i++) {
//            captcha.append(captchaCharacters.charAt(random.nextInt(captchaCharacters.length())));
//        }
//        return captcha.toString();
//    }
//
//    private void displayCaptcha(String captcha) {
//        for (char ch : captcha.toCharArray()) {
//            if (asciiArtMap.containsKey(ch)) {
//                String[] asciiArt = asciiArtMap.get(ch);
//                for (String line : asciiArt) {
//                    for (char c : line.toCharArray()) {
//                        if (random.nextBoolean()) {
//                            // Introduce some noise
//                            if (random.nextDouble() < 0.1) {
//                                System.out.print("*"); // 10% chance for noise
//                            } else if (random.nextDouble() < 0.2) {
//                                System.out.print("!"); // 20% chance for noise
//                            } else {
//                                System.out.print(random.nextBoolean() ? Character.toLowerCase(c) : Character.toUpperCase(c));
//                            }
//                        } else {
//                            System.out.print(c);
//                        }
//                    }
//                    System.out.println();
//                }
//            } else {
//                System.out.println(ch); // If no ASCII art, print the character directly
//            }
//            System.out.println(); // Add some spacing between characters
//        }
//    }
//
//    private static boolean verifyCaptcha(String inputCaptcha, String generatedCaptcha) {
//        return inputCaptcha.equalsIgnoreCase(generatedCaptcha);
//    }
//
//
//
//    private boolean verifyEquation() {
//        int num1 = random.nextInt(10);
//        int num2 = 1 + random.nextInt(9);
//        String[] operators = {"PLUS", "MINUS", "TIMES", "DIVIDED BY"};
//        String operator1 = operators[random.nextInt(operators.length)];
//
//        String equation = num1 + " " + operator1 + " " + num2;
//        double result = calculateEquation(num1, operator1, num2);
//
//        System.out.println("Solve the following equation: " + equation);
//        System.out.print("Enter the result: ");
//        double userResult = Double.parseDouble(scanner.nextLine());
//
//        if (Math.abs(userResult - result) > 0.0001) {
//            System.out.println("Incorrect result.");
//            return false;
//        }
//
//        return true;
//    }
//
//    private static double calculateEquation(int num1, String operator, int num2) {
//        double result = 0;
//        switch (operator) {
//            case "PLUS":
//                result = num1 + num2;
//                break;
//            case "MINUS":
//                result = num1 - num2;
//                break;
//            case "TIMES":
//                result = num1 * num2;
//                break;
//            case "DIVIDED BY":
//                result = (double) num1 / num2;
//                break;
//        }
//        return result;
//    }
//
//}
//
