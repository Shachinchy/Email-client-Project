package org.example;
import javax.mail.MessagingException;
import javax.swing.*;
import java.awt.*;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EmailClientGUI extends JFrame {

    private JTextField usernameField = new JTextField(20);
    private JPasswordField passwordField = new JPasswordField(20);

    public EmailClientGUI() {
        setTitle("Java Email Client");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
        setVisible(true);

        // Add window listener to handle application close
        // This ensures when we exit the application, the email session is properly closed.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    if (EmailSessionManager.getInstance() != null) {
                        EmailSessionManager.getInstance().close(); // Close the email session
                    }
                } catch (MessagingException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


    // Initialization of UI components
    private void initUI() {
        // Utilized JList with a DefaultListModel to list email subjects,
        // making it scrollable by adding it to a JScrollPane.
        DefaultListModel<String> emailListModel = new DefaultListModel<>();
        JList<String> emailList = new JList<>(emailListModel);
        add(new JScrollPane(emailList), BorderLayout.WEST);
        // Added a JTextArea for displaying the content of the selected email,
        // also within a JScrollPane to enable scrolling.
        JTextArea emailContent = new JTextArea();
        emailContent.setEditable(false);
        add(new JScrollPane(emailContent), BorderLayout.CENTER);
        // Added a simple button for initiating the email composition process.
        JButton composeButton = new JButton("Compose");
        add(composeButton, BorderLayout.SOUTH);
        // Ensured the login dialog is invoked immediately after the UI initializes,
        // making the login process the first interaction the user has with the application.
        SwingUtilities.invokeLater(this::showLoginDialog);
    }

    // Implement a login dialog that prompts the user to provide their email credentials.
    private void showLoginDialog() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Email:"));
        panel.add(usernameField);
        panel.add(new JLabel("App Password:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Login",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try {
                // Initialize EmailSessionManager here
                EmailSessionManager.getInstance(username, password);
                refreshInbox(); // Refresh inbox to load emails
            } catch (MessagingException e) {
                JOptionPane.showMessageDialog(this,
                        "Failed to initialize email session: " + e.getMessage(),
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.out.println("Login cancelled.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmailClientGUI());
        // To launch the GUI on the Event Dispatch Thread (EDT)
        // It ensures that the GUI is initialized and displayed correctly,
        // even if other tasks are being performed by the application's main thread.
    }

    private void refreshInbox() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}