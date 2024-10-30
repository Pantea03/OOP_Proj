package org.example.view;

public enum View {
    MAIN_MENU("/fxml/MainMenu.fxml"),
    PROFILE_MENU("/fxml/ProfileMenu.fxml"),
    CAPTCHA_VERIFICATION("/fxml/CaptchaVerification.fxml"),
    SETTING("/fxml/Setting.fxml"),
    SIGNIN_MENU("/fxml/SigninMenu.fxml"),
    LOGIN_MENU("/fxml/LoginMenu.fxml"),
    INITIAL_PAGE("/fxml/InitialPage.fxml"),
    TWO_PLAYER_MODE("/fxml/TwoPlayerMode.fxml"),
    BETTING_MODE("/fxml/BettingMode.fxml"),
    SINGLE_PLAYER_MODE("/fxml/SinglePlayerMode.fxml"),
    GAME_MENU("/fxml/GameMenu.fxml"),
    SECOND_PLAYER_LOGIN("/fxml/SecondPlayerLogin.fxml"),
    GAME_VIEW(""),
    STORE("/fxml/Store.fxml");
    private final String file;

    View(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }
}
