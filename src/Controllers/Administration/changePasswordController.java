package Controllers;

import Entities.Account;
import Tools.DB.DBHelper;
import Tools.Session;
import com.jfoenix.controls.JFXPasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

/**
 * Created by Ashraf on 01/05/2017.
 */
public class changePasswordController {
    @FXML private JFXPasswordField oldPasswordPF;
    @FXML private JFXPasswordField newPasswordPF;
    @FXML private JFXPasswordField confirmPasswordPF;

    public void onUpdateButtonClick(ActionEvent actionEvent){
        String oldPassword = oldPasswordPF.getText();
        String newPassword = newPasswordPF.getText();
        String confirmPassword = confirmPasswordPF.getText();
        if(!emptyInput(oldPassword,newPassword,confirmPassword)){
            DBHelper dbHelper = new DBHelper();
            Session session = new Session();
            Account account = new Account();
            account = dbHelper.getAccountByUsername(session.getUser());
            if(!account.getPassword().equals(dbHelper.passHash(oldPassword))){
                new Alert(Alert.AlertType.ERROR, "Mot de passe actuel incorrect").showAndWait();
            } else {
                if(validNewPassword(newPassword,confirmPassword)){
                    if(dbHelper.changeAccountPassword(session.getUser(),newPassword)){
                        oldPasswordPF.setText("");
                        newPasswordPF.setText("");
                        confirmPasswordPF.setText("");
                        new Alert(Alert.AlertType.INFORMATION, "Mot de passe mis à jour avec succès !").showAndWait();
                    }else new Alert(Alert.AlertType.ERROR, "Échec de mise à jour").showAndWait();
                }
            }
        }
    }

    private boolean emptyInput (String oldPass,String newPass,String confirmPass){
        if(oldPass.isEmpty() | newPass.isEmpty() | confirmPass.isEmpty()){
            new Alert(javafx.scene.control.Alert.AlertType.ERROR,
                    "il faut remplir tout les informations").showAndWait();
            return true;
        }
        else return false;
    }

    private boolean validNewPassword (String newPass,String confirmPass){
        if(newPass.charAt(0)==' ') {
            new Alert(javafx.scene.control.Alert.AlertType.ERROR,
                    "mot de passe ne doit pas commencer avec un espace").showAndWait();
            return false;
        } else if(newPass.length()<8 | newPass.length()>20){
            new Alert(javafx.scene.control.Alert.AlertType.ERROR,
                    "mot de passe doit etre entre 8 et 20 character").showAndWait();
            return false;
        } else if(!newPass.equals(confirmPass)){
            new Alert(javafx.scene.control.Alert.AlertType.ERROR,
                    "le nouveau mot de passe n'égal pas le confirmation du mot de passe").showAndWait();
            return false;
        } else return true;
    }
}
