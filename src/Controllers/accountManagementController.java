package Controllers;

import Entities.Account;
import Tools.DB.DBHelper;
import Tools.Session;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ashraf on 02/05/2017.
 */
public class accountManagementController {


    @FXML private JFXTextField usernameAddTF;
    @FXML private JFXPasswordField passwordPF;
    @FXML private JFXPasswordField confirmPasswordPF;
    @FXML private JFXTextField emailTF;
    @FXML private JFXComboBox<String> accTypeCB;
    @FXML private JFXTextField searchTF;
    @FXML private TableView<Account> tableTV;
    @FXML private TableColumn<Account, String> usernameTC;
    @FXML private TableColumn<Account, String> accTypeTC;
    @FXML private TableColumn<Account, String> emailTC;

    private ObservableList<Account> accountsList = FXCollections.observableArrayList(); //list contains the accounts

    //on add_button click
    public void onAddClick() {
        String username = usernameAddTF.getText();
        String password = passwordPF.getText();
        String confirmPassword = confirmPasswordPF.getText();
        String email = emailTF.getText();
        String accType = accTypeCB.getValue();
        if (!emptyInput(username, password, confirmPassword, email)) { //verify the inputs if they empty or not
            if (validPassword(password, confirmPassword)) { //verify the password
                if (validEmail(email)) { //verify the email
                    if (validUsername(username)) { //verify the username
                        if (accType!=null && !accType.isEmpty()){
                            DBHelper dbHelper = new DBHelper();
                            if (dbHelper.addAccount(username, password, email, accType)) { //add account into the DB
                                new Alert(Alert.AlertType.INFORMATION, "Compte ajouté avec succès !").showAndWait();
                                usernameAddTF.setText("");
                                passwordPF.setText("");
                                confirmPasswordPF.setText("");
                                emailTF.setText("");
                                accTypeCB.setValue(accTypeCB.getItems().get(0));
                            } else new Alert(Alert.AlertType.ERROR, "Échec de l'ajout d'un nouveau compte").showAndWait();
                        } else new Alert(Alert.AlertType.ERROR, "il faut sélectionnez une valeur pour le type de compte").showAndWait();
                    } else new Alert(Alert.AlertType.ERROR, "Ce nom d'utilisateur est invalide ou existe déjà").showAndWait();
                }
            }
        }
    }

    public void onDeleteClick() {
        if (tableTV.getSelectionModel().getSelectedItem() != null) { //check if there is a selected account
            String username = tableTV.getSelectionModel().getSelectedItem().getUsername(); //get the username of the selected account
            Session session = new Session();
            if (!session.getUser().equals(username)) { //verify that the selected account isn't the account currently logged in with
                ButtonType yes = new ButtonType("Oui", ButtonBar.ButtonData.OK_DONE);
                ButtonType no = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
                Alert confrm = new Alert(Alert.AlertType.WARNING,
                            "Êtes-vous sûr de vouloir supprimer ce compte?" ,yes,no);
                confrm.setTitle("Confirmation");
                confrm.setHeaderText(null);
                Optional<ButtonType> answer = confrm.showAndWait();
                if (answer.isPresent() && answer.get() == yes){ //if the user confirmed the delete action
                    DBHelper dbHelper = new DBHelper();
                    if (dbHelper.deleteAccount(username)) { //delete the account
                        new Alert(Alert.AlertType.INFORMATION, "Le compte a été supprimé avec succès !").showAndWait();
                        setTable();
                    } else new Alert(Alert.AlertType.ERROR, "Échec de la suppression du compte").showAndWait();
                }
            } else new Alert(Alert.AlertType.ERROR, "Vous ne pouvez pas supprimer votre propre compte !").showAndWait();
        } else new Alert(Alert.AlertType.ERROR, "d'abord selectioner un compte").showAndWait();

    }

    public void onSearchClick(){
        String username = searchTF.getText();
        if(!username.isEmpty()){
            DBHelper dbHelper = new DBHelper();
            accountsList.clear();
            accountsList.setAll(dbHelper.getAccountsByUsername(username));
            tableTV.setItems(accountsList);
            searchTF.setText("");
        } else new Alert(javafx.scene.control.Alert.AlertType.ERROR, "Remplissez le champ de texte du nom d'utilisateur").showAndWait();
    }


    private boolean emptyInput(String username, String pass, String confirmPass, String email) {
        if (username.isEmpty() | pass.isEmpty() | confirmPass.isEmpty() | email.isEmpty()) {
            new Alert(Alert.AlertType.ERROR,
                    "il faut remplir tout les informations").showAndWait();
            return true;
        } else return false;
    }

    private boolean validPassword(String pass, String confirmPass) {
        if (pass.charAt(0) == ' ') {
            new Alert(Alert.AlertType.ERROR,
                    "mot de passe ne doit pas commencer avec un espace").showAndWait();
            return false;
        } else if (pass.length() < 8 | pass.length() > 20) {
            new Alert(Alert.AlertType.ERROR,
                    "mot de passe doit etre entre 8 et 20 character").showAndWait();
            return false;
        } else if (!pass.equals(confirmPass)) {
            new Alert(Alert.AlertType.ERROR,
                    "le nouveau mot de passe n'égal pas le confirmation du mot de passe").showAndWait();
            return false;
        } else return true;
    }

    private boolean validEmail(String email) {
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@esi-sba.dz");
        Matcher matcher = emailPattern.matcher(email);
        boolean result = matcher.find();
        if (!result) {
            new Alert(Alert.AlertType.ERROR,
                    "email non valide (exemple d'email valide: 'prenom.nom@esi-sba.dz')").showAndWait();
            return false;
        }
        DBHelper dbHelper = new DBHelper();
        if(dbHelper.getAccountByEmail(email) != null) {
            new Alert(Alert.AlertType.ERROR,
                    "email déja utilisé").showAndWait();
            return false;
        }
        return true;
    }

    private boolean validUsername(String username) {
        if ((username!=null) && (!username.isEmpty()) && username.charAt(0)!=' '){
            char [] tmp = username.toCharArray();//dicomposing the string into an char array
            //testing for each character if it's a letter or a space
            //if it is not a letter or space so the name isn't valid
            for (char c : tmp){
                if(c==' ') return false;
            }
            //if we reach here that means all characters are either a letter or numbers
            DBHelper dbHelper = new DBHelper();
            if (dbHelper.getAccountByUsername(username) != null) return false;
            return true;
        }
        return false;
    }

    @FXML private void setTable() {
        usernameTC.setCellValueFactory(new PropertyValueFactory<Account, String>("username"));
        accTypeTC.setCellValueFactory(new PropertyValueFactory<Account, String>("accType"));
        emailTC.setCellValueFactory(new PropertyValueFactory<Account, String>("email"));
        DBHelper dbHelper = new DBHelper();
        //to clear the previous search list
        accountsList.clear();
        tableTV.setItems(accountsList);
        //to set the actual present accounts in the database
        accountsList.addAll(dbHelper.getAccountList());
        tableTV.setItems(accountsList);
    }

    @FXML private void initialize() {
        accTypeCB.getItems().addAll("Gestionnaire", "Admin");
    }
}
