package Controllers;

import Entities.Account;
import Tools.DB.DBHelper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.Properties;

/**
 * Created by Ashraf on 10/05/2017.
 */
public class forgotPasswordController {
    @FXML private JFXTextField emailTF;
    @FXML private JFXButton ResetButton;
    @FXML private ComboBox retriveMethodCB;
    @FXML Label invalidInfoLable;


    public void onResetButtonClick(){
        String text = emailTF.getText();//get the text from the text field
        if(text!=null && !text.isEmpty()){
            if(areYouOnline()){ //check if there is internet connection
                //waiting message
                invalidInfoLable.setText("Soyez patient s'il vous plaît...");
                invalidInfoLable.setStyle("-fx-font-weight: bold;-fx-text-fill: #1663ab");
                Platform.runLater(() ->{
                    String retriveMethod = retriveMethodCB.getSelectionModel().getSelectedItem().toString(); //get the selected method(username or email)
                    DBHelper dbHelper = new DBHelper();
                    Account account ;
                    if(retriveMethod.equals("Nom d'utilisateur")){//if the selected method is username
                        account = dbHelper.getAccountByUsername(text); //search for the account with that username
                        if(account != null) { // if there is a match in the result of the search
                            String newPass = passwordGenerator(12); //create new password
                            dbHelper.changeAccountPassword(account.getUsername(),newPass); //change the password
                            sendEmail(account.getEmail(),newPass);//send email with the new password
                            emailTF.clear(); //clear the textfield
                            if(!invalidInfoLable.getText().isEmpty()) invalidInfoLable.setText("");
                            new Alert(Alert.AlertType.INFORMATION,
                                    "un mot de passe a été envoyé vers votre email !").showAndWait();
                        } else  {
                            invalidInfoLable.setText("Le nom d'utilisateur entré ne correspond à aucun compte");
                            invalidInfoLable.setStyle("-fx-font-weight: bold;-fx-text-fill: #ff484b");
                        }
                    }else{ //if the selected method is email
                        account = dbHelper.getAccountByEmail(text); //search for the account with that email
                        if(account != null) { //if there is a match in the result of the search
                            String newPass = passwordGenerator(12); //create new password
                            dbHelper.changeAccountPassword(account.getUsername(),newPass); //change the password
                            sendEmail(account.getEmail(),newPass); //send email with the new password
                            emailTF.clear();
                            if(!invalidInfoLable.getText().isEmpty()) invalidInfoLable.setText("");
                            new Alert(Alert.AlertType.INFORMATION,
                                    "un mot de passe a été envoyé vers votre email !").showAndWait();
                        } else {
                            invalidInfoLable.setText("L'email entré ne correspond à aucun compte");
                            invalidInfoLable.setStyle("-fx-font-weight: bold;-fx-text-fill: #ff484b");
                        }
                    }
                        });
            } else new Alert(javafx.scene.control.Alert.AlertType.ERROR,
                    "Problème de connexion Internet.\n" +
                            "S'il vous plaît vérifiez vos paramètres Internet").showAndWait();
        }else new Alert(javafx.scene.control.Alert.AlertType.ERROR,
                "Remplissez le champ de texte du nom d'utilisateur/email").showAndWait();
    }

    private String passwordGenerator (int len){
        String passwordCharactersSet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"; //set of characters
        SecureRandom rnd = new SecureRandom(); //to get random string
        StringBuilder sb = new StringBuilder( len ); //creating a string builder of "len" characters
        for( int i = 0; i < len; i++ )  sb.append( passwordCharactersSet.charAt( rnd.nextInt(passwordCharactersSet.length()) ) );
            return sb.toString();

    }

    //checking internet connectivity function
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


    //sending email function
    public boolean sendEmail(String receiver, String msg){
        boolean emailSent = false; //boolean to check internet connection

        //checking internet connectivity
        if(areYouOnline()){

            //the application email authentication information
            final String username = "gpe.esi.sba@gmail.com";
            final String password = "esisba2017";

            //the email body text
            String body = "Bonjour, \n \n Voici le nouveau mot de passe de votre compte :"+msg;

            //setting the email connection properties
            Properties props = new Properties();
            props.put("mail.smtp.auth","true");
            props.put("mail.smtp.starttls.enable","true");
            props.put("mail.smtp.host","smtp.gmail.com");
            props.put("mail.smtp.port","587");

            //creating new session to log into application email
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator(){
                        protected PasswordAuthentication getPasswordAuthentication(){
                            return new PasswordAuthentication(username,password);
                        }
                    });

            try {
                //setting our message (email) characteristics
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("gpe.esi.sba@gmail.com")); //from
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(receiver)); //to
                message.setSubject("GPE: Récupération de mot de passe");
                message.setText(body);

                //sending the email
                Transport.send(message);

            } catch (MessagingException e) {
                e.printStackTrace();
            }
            emailSent = true;
        }
        return emailSent;
    }


    @FXML private void initialize() {
        retriveMethodCB.getItems().addAll("Nom d'utilisateur", "Tools/Email");
        retriveMethodCB.setValue(retriveMethodCB.getItems().get(0));
    }
}
