package Entities; /**
 * Created by Ashraf on 30/04/2017.
 */

import Controllers.loginController;
import Tools.DB.DBHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Parent root;
    public static Parent getRoot() {
        return root;
    }
    public static void setRoot(Parent root) {
        Main.root = root;
    }
    private static Stage stage;
    public static Stage getStage() {
        return stage;
    }
    public static void setStage(Stage stage) {
        Main.stage = stage;
    }


    public static void main(String[] args) {launch(args);}

    @Override
    public void start(Stage primaryStage) throws IOException {

        root = FXMLLoader.load(getClass().getResource("/UI/Layouts/login.fxml"));

        Scene scene = new Scene(root);

        primaryStage.setTitle("GPE");
        primaryStage.setScene(scene);
        primaryStage.show();


        stage= primaryStage;


    }
}
