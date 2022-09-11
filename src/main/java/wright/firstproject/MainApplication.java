package wright.firstproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import wright.firstproject.Controllers.Inventory;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/wright/firstproject/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        scene.getStylesheets().add(MainApplication.class.getResource("bootstrap3.css").toExternalForm());
        //Adding seed data
        Inventory.addSeedData();
    }

    public static void main(String[] args) {
        launch();
    }
}