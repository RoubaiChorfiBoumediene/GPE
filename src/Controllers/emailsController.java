package Controllers;

import Tools.Email.CheckingMails;
import Tools.Email.EmailDisplay;
import Tools.Session;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Collections;

/**
 * Created by Ashraf on 04/06/2017.
 */
public class emailsController {
    @FXML PasswordField emailPasswordPF;
    @FXML Button emailLoginButton;
    @FXML Button emailRefresheButton;
    @FXML TableView emailContentTable;
    @FXML Label incorrectPasswordLable;
    Session session = new Session();
    ObservableList<EmailDisplay> emailsList;

    //to connect and retrieve emails from email account
    public void onConnexionButtonClick(){
        if(areYouOnline()){ //check for internet connection
            Platform.runLater(() -> { //launch interface platfrom to let the application do another task in parallel
                String emailAddress = session.getEmail(); //getting the email of the current user
                String emailPassword = emailPasswordPF.getText(); //getting the password of the email
                try {
                    emailsList = FXCollections.observableArrayList();
                    //check for email authentication and retrieve emails
                    CheckingMails.check(emailAddress,emailPassword,emailsList);
                    //setting the display of the emails
                    emailPasswordPF.setDisable(true);
                    emailLoginButton.setDisable(true);
                    emailRefresheButton.setDisable(false);
                    emailContentTable.setDisable(false);
                    incorrectPasswordLable.setText("");
                    //sort order of the emails from newest to the oldest
                    Collections.reverse(emailsList);
                    //add the list of emails and show it in the table
                    emailContentTable.setItems(emailsList);
                } catch (MessagingException e) {
                    //if username/password aren't correct
                    if(e.toString().contains("[AUTH] Username and password not accepted")){
                        incorrectPasswordLable.setText("Mot de passe incorrect");
                        incorrectPasswordLable.setStyle("-fx-font-weight: bold;-fx-text-fill: #ff484b");
                    } else { //if the email account doesn't Allowing less secure apps to access your account
                        securityError();
                        incorrectPasswordLable.setText("");
                    }
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            //waiting message
            incorrectPasswordLable.setText("Soyez patient s'il vous plaît...");
            incorrectPasswordLable.setStyle("-fx-font-weight: bold;-fx-text-fill: #1663ab");
        //incase there is no internet connexion
        }else new Alert(javafx.scene.control.Alert.AlertType.ERROR,
                "Problème de connexion Internet.\n" +
                        "S'il vous plaît vérifiez vos paramètres Internet").showAndWait();
    }


    public void onRefresheButtonClick(){
        String emailAddress = session.getEmail();
        String emailPassword = emailPasswordPF.getText();
        try {
            //getting the new list of emails
            CheckingMails.check(emailAddress,emailPassword,emailsList);
            //sort order from newest to the oldest
            Collections.reverse(emailsList);
            //adding them into the table to show them
            emailContentTable.setItems(emailsList);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //table preparing method
    private void setTable(){
        //create column , setting it's relation with the Emaildispaly class and setting it's width display
        TableColumn<EmailDisplay,Integer> emailNumberColumn = new TableColumn<>("Numéro d'email");
        emailNumberColumn.setCellValueFactory
                (new PropertyValueFactory<EmailDisplay, Integer>("number"));
        emailNumberColumn.setPrefWidth(100);

        //create column , setting it's relation with the Emaildispaly class and setting it's width display
        TableColumn<EmailDisplay,String> emailFromColumn = new TableColumn<>("De");
        emailFromColumn.setCellValueFactory
                (new PropertyValueFactory<EmailDisplay, String>("from"));
        emailFromColumn.setPrefWidth(135);

        //create column , setting it's relation with the Emaildispaly class and setting it's width display
        TableColumn<EmailDisplay,String> emailSubjectColumn = new TableColumn<>("Objet");
        emailSubjectColumn.setCellValueFactory
                (new PropertyValueFactory<EmailDisplay, String>("subject"));
        emailSubjectColumn.setPrefWidth(200);

        //create column , setting it's relation with the Emaildispaly class and setting it's width display
        TableColumn<EmailDisplay,String> emailDateColumn = new TableColumn<>("Date");
        emailDateColumn.setCellValueFactory
                (new PropertyValueFactory<EmailDisplay, String>("sentDate"));
        emailDateColumn.setPrefWidth(138);

        //add the column to the table
        emailContentTable.getColumns().addAll(emailNumberColumn,emailFromColumn,emailDateColumn,emailSubjectColumn);
        //disabling it until connexion is established
        emailContentTable.setDisable(true);
    }


    //error message ,in case not allowing less secure apps to access your account
    private void securityError(){
        Alert alert = new Alert(Alert.AlertType.ERROR,
                "");
        alert.setHeaderText("Échec de connexion");

        FlowPane fp = new FlowPane();
        Label lbl = new Label("Vous devez autoriser des applications moins sécurisées à accéder à votre compte.\n" +
                "Contactez notre support ou visitez le site ci-dessous pour plus d'informations sur la façon de le faire: ");
        Hyperlink link = new Hyperlink("https://support.google.com/accounts/answer/6010255");
        fp.getChildren().addAll( lbl, link);

        link.setOnAction( (evt) -> {
        } );

        alert.getDialogPane().contentProperty().set( fp );

        alert.showAndWait();
    }

    //to check if there is an internet connexion
    public static boolean areYouOnline() {
        Socket sock = new Socket(); //create a socket
        InetSocketAddress addr = new InetSocketAddress("google.com",80); //set pinging address
        try {
            sock.connect(addr,3000); //pinging
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            try {sock.close();}
            catch (IOException e) {}
        }
    }
   @FXML private void initialize(){
        setTable(); //setting the table from the beginning
        emailRefresheButton.setDisable(true); //disabling the refreshe button before establishing the connection
    }
}
