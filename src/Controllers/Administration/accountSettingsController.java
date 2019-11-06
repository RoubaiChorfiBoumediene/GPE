package Controllers;

import Entities.Main;
import Tools.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Ashraf on 01/05/2017.
 */
public class accountSettingsController {
    @FXML private Button changePasswordButton;
    @FXML private Button accountManagementButton;


    @FXML public void OnChangePasswordClick(ActionEvent actionEvent){
        try {
            //setting the new scene
            Main.setRoot(FXMLLoader.load(getClass().getResource("/UI/Layouts/Administration/changePassword.fxml")));
            Scene scene = new Scene(Main.getRoot());
            Stage stage =  Main.getStage();
            Main.setStage(stage);
            //show the new scene when changePassword button is clicked
            stage = (Stage) changePasswordButton.getScene().getWindow();
            stage.setTitle("Changement de mot de passe");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML public void OnAccountManagementClick(ActionEvent actionEvent){
        try {
            //setting the new scene
            Main.setRoot(FXMLLoader.load(getClass().getResource("/UI/Layouts/Administration/accountManagement.fxml")));
            Scene scene = new Scene(Main.getRoot());
            Stage stage = Main.getStage();

            Main.setStage(stage);
            //show the new scene when accountManagement button is clicked
            stage = (Stage) accountManagementButton.getScene().getWindow();
            
            stage.setTitle("Gestion des comptes");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void initialize(){
        Session session = new Session();
        if(!session.getType().equals("Admin")){
            accountManagementButton.setDisable(true);
        }
    }
}
