package org.example;
import javax.mail.Message;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

// We have a GUI, we may want to maintain this connection with Gmail until we choose to close the email client application.
// so, we need a class to manage email sessions for connecting to, fetching emails from, and disconnecting from an IMAP email server
public class EmailSessionManager {
    private Session emailSession;
    private Store store;
    private Folder emailFolder;
    private static EmailSessionManager instance;

    // Implement static fields for storing email credentials
    private static String currentUsername = "";
    private static String currentPassword = "";

    // Make the constructor private to enforce a singleton pattern
    // to ensure that only one instance of this class is created and used throughout the application.
    private EmailSessionManager(String username, String password) throws MessagingException {
        // Set up properties to access Gmail's IMAP server
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", "imap.gmail.com");
        properties.put("mail.imaps.port", "993");
        properties.put("mail.imaps.ssl.enable", "true");

        this.emailSession = Session.getInstance(properties, null);
        this.store = emailSession.getStore("imaps");
        this.store.connect(username, password);

        // Store the credentials upon successful connection
        currentUsername = username;
        currentPassword = password;
    }

    // Created a static getInstance method to provide a global access point to the singleton instance of EmailSessionManager
    public static EmailSessionManager getInstance(String username, String password) throws MessagingException {
        if (instance == null) {
            instance = new EmailSessionManager(username, password);
        }
        return instance;
    }

    public static EmailSessionManager getInstance() throws IllegalStateException {
        if (instance == null) {
            throw new IllegalStateException("EmailSessionManager is not initialized. Please login first.");
        }
        return instance;
    }

    // Add getter methods to return the username and password used to log the user in.
    // Method to retrieve the current username
    public static String getUsername() {
        return currentUsername;
    }

    // Method to retrieve the current password
    public static String getPassword() {
        return currentPassword;
    }

    // Created a receiveEmail method for fetching emails from the inbox.
    public Message[] receiveEmail() throws MessagingException {
        if (emailFolder == null || !emailFolder.isOpen()) {
            emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);
        }
        return emailFolder.getMessages();
    }

    // Created a close method to properly close the emailFolder and store.
    public void close() throws MessagingException {
        if (emailFolder != null) {
            emailFolder.close(false);
            emailFolder = null;
        }
        if (store != null) {
            store.close();
            store = null;
        }
        instance = null;
        // Clear the credentials upon closing the session
        currentUsername = "";
        currentPassword = "";
    }
}