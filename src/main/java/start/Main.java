package start;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException, InterruptedException {
        Parent root = FXMLLoader.load(Main.class.getResource("/login_home_page_form.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("Gestione Back-Office");
        root.requestFocus(); // rimuove il focus dal campo email, per comodità
    }
}