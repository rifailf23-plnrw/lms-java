package lms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/lms/view/ImportView.fxml"));

        if (loader.getLocation() == null) {
            throw new RuntimeException("Cannot find ImportView.fxml — check the path");
        }

        Scene scene = new Scene(loader.load(), 900, 620);
        primaryStage.setTitle("Java Library Viewer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
