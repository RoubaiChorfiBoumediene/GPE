package Tools.Email;

/**
 * Created by Ashraf on 04/06/2017.
 */
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

public class CheckingMails {

    public static void check(String user, String password,ObservableList<EmailDisplay> emailsList)
            throws MessagingException, IOException {

        //create properties field
            Properties properties = new Properties();

            properties.put("mail.pop3.host", "pop.gmail.com");
            properties.put("mail.pop3.port", "995");
            properties.put("mail.pop3.starttls.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties);

            //create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore("pop3s");

            store.connect("pop.gmail.com", user, password);

            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            emailsList.clear();
            for (int i = 0, n = messages.length; i < n; i++) {
                Message message = messages[i];
                EmailDisplay emailDisplay = new EmailDisplay();
                emailDisplay.setNumber(i+1);
                emailDisplay.setSubject(message.getSubject());
                emailDisplay.setFrom(message.getFrom()[0].toString());
                //reformatting the date style
                String date = message.getSentDate().toString().substring(0,11);
                date = date + message.getSentDate().toString().substring(message.getSentDate().toString().length()-4);
                date = date + message.getSentDate().toString().substring(10,19);
                emailDisplay.setSentDate(date);
                //adding the email to the emails list
                emailsList.add(emailDisplay);
            }
            //close the store and folder objects
            emailFolder.close(false);
            store.close();
    }

}