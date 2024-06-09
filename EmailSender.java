package org.example;
import java.io.File;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {
    public static void sendEmailWithAttachment(String to, String subject, String body, File[] attachments) {
        try {
            // Retrieve credentials from EmailSessionManager to accept usernames and passwords dynamically
            String username = EmailSessionManager.getUsername();
            String password = EmailSessionManager.getPassword();

            // Set up properties to access Gmail's STMP server
            Properties prop = new Properties();
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", "587");
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.starttls.enable", "true"); // Enable TLS

            Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            try {
                // Create an email message with specified sender, recipient, subject, and body
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                message.setSubject(subject);

                // Add functionality to attach one or more files to the email.
                // Each file selected for attachment is added as a separate MimeBodyPart,
                // and all parts (text and attachments) are combined into a Multipart object.
                Multipart multipart = new MimeMultipart();

                // Add text as a MimeBodyPart object
                MimeBodyPart textPart = new MimeBodyPart();
                textPart.setText(body);
                multipart.addBodyPart(textPart);

                // Add each attachment as a MimeBodyPart object
                for (File file : attachments) {
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    attachmentPart.attachFile(file);
                    multipart.addBodyPart(attachmentPart);
                }

                message.setContent(multipart);
                Transport.send(message);
                System.out.println("Email sent successfully with attachments.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}