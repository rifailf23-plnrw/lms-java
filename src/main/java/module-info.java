module lms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens lms to javafx.fxml;
    opens lms.controller to javafx.fxml;
    opens lms.model to javafx.fxml;
    opens lms.data to javafx.fxml;

    exports lms;
    exports lms.model;
    exports lms.data;
    exports lms.controller;
}