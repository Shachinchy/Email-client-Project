package org.example;
import javax.mail.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EmailReceiver {
    // Implement static fields for storing email credentials
    private static String username = "";
    private static String password = "";

    // Add a method to dynamically set these credentials
    // so that they can be called with the user's input from the GUI after login.
    public static void setCredentials(String user, String pass) {
        username = user;
        password = pass;
    }

    public static Message[] receiveEmail() throws MessagingException {
        // Set up properties to access Gmail's IMAP server
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", "imap.gmail.com");
        properties.put("mail.imaps.port", "993");
        properties.put("mail.imaps.ssl.enable", "true");

        List<Message> messagesList = new ArrayList<>();

        // Utilize the Session object to obtain a Store object configured for IMAP
        Session emailSession = Session.getInstance(properties);
        Store store = emailSession.getStore("imaps");
        store.connect("imap.gmail.com", username, password);

        // Open the "INBOX" folder from the store in read-only mode,
        Folder emailFolder = store.getFolder("INBOX");
        emailFolder.open(Folder.READ_ONLY);

        Message[] messages = emailFolder.getMessages();
        for (Message message : messages) {
            messagesList.add(message);
        }

        emailFolder.close(false);
        store.close();

        return messagesList.toArray(new Message[0]);
    }
}