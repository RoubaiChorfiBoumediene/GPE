package Controllers;

import Entities.Account;
import Tools.DB.DBHelper;
import Tools.Session;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;


/**
 * Created by Ashraf on 30/04/2017.
 */
public class loginController {
    @FXML private JFXTextField usernameTF;
    @FXML private JFXPasswordField passwordF;
    @FXML private JFXButton loginButton;
   @FXML private Label errorMessage = new Label();
   @FXML private Hyperlink forgotPasswordLink;

   private DBHelper dbHelper = new DBHelper();
   int nbrTries = 3;
   int seconds = 29;
   Timeline timeline;
   Session session;



   public void handelLogin(ActionEvent actionEvent) throws IOException {
       String username = usernameTF.getText();
       String password = passwordF.getText();
       if(!emptyInput(username,password)){
           //checkng if the account exists
           if(dbHelper.checkLoginAccess(username,password)){ //if it exists
               Account account = dbHelper.getAccountByUsername(username);
               //creating a new session
               session = new Session();
               session.setPrefs(account.getUsername(),true,account.getAccType(),account.getEmail());
               //setting the scene
               Parent root = FXMLLoader.load(getClass().getResource("/UI/Layouts/emailsDisplay.fxml"));
               Scene scene = new Scene(root);
               //setting the stage
               Stage stage = new Stage();
               stage.setTitle("The project");
               stage.setScene(scene);
               stage.show();
               Stage stage1 = (Stage) this.loginButton.getScene().getWindow();
               stage1.close();
           } else {
               if(nbrTries!=0){
                   errorMessage.setText("Pseudo/Mot de passe incorrect..."+nbrTries+" essayes restant");
                   errorMessage.setStyle("-fx-font-weight: bold;-fx-text-fill: #ff484b");
                   nbrTries--;
               } else {
                   usernameTF.setDisable(true);
                   passwordF.setDisable(true);
                   loginButton.setDisable(true);
                   errorMessage.setText("You have to wait "+(seconds+1)+" seconds");
                   timeline = new Timeline(new KeyFrame(
                           Duration.millis(1000), e->decrement()));
                   timeline.setCycleCount(Animation.INDEFINITE);
                   timeline.play();
               }
           }
       }
   }

   public void onForgotPasswordClick(){
       try {
           Parent root = FXMLLoader.load(getClass().getResource("/UI/Layouts/Administration/forgotPassword.fxml"));
           Scene scene = new Scene(root);
           Stage stage = new Stage();
           stage.setTitle("récupération de mot de passe");
           stage.setScene(scene);
           stage.initModality(Modality.APPLICATION_MODAL);
           stage.initOwner(forgotPasswordLink.getScene().getWindow());
           stage.show();
       } catch (IOException e) {
           e.printStackTrace();
       }
   }

    private boolean emptyInput (String username,String password){
        if(username.isEmpty() & password.isEmpty()){
            new Alert(Alert.AlertType.ERROR,
                    "Pseudo et Mot de passe invalides").showAndWait();
            return true;
        }
        else if(username.isEmpty()){
            new Alert(Alert.AlertType.ERROR, "Nom d'utilisateur invalide").showAndWait();
            return true;
        }
        else if(password.isEmpty()){
            new Alert(Alert.AlertType.ERROR, "Mot de passe invalide").showAndWait();
            return true;
        } else return false;
    }
    private void decrement() {
        errorMessage.setText("Il faut attendre "+seconds+" secondes");
        seconds--;
        if (seconds<0){
            timeline.stop();
            usernameTF.setDisable(false);
            passwordF.setDisable(false);
            loginButton.setDisable(false);
            errorMessage.setText("");
            seconds=30;
            nbrTries= 5;
        }
    }

    @FXML private void initialize(){
       errorMessage.setText("");
    }
}
