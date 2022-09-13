package wright.firstproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import wright.firstproject.Controllers.Inventory;

import java.io.IOException;
/** Main class used to run the application
 RUNTIME ERROR the application would add the seed data every time the main screen opened. This was fixed by making an add seed data method in the
 inventory class and calling it in the start method so that it is only called once.
 FUTURE ENHANCEMENT would be to connect this to some kind of database to save data even when the application is not running*/
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