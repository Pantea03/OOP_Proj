module ProjectOOP {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;

    // Open the package for reflective access by javafx.fxml
    opens org.example.menuController to javafx.fxml;

    // Export the necessary packages
    exports org.example;
    exports org.example.menuController to javafx.fxml;
}
