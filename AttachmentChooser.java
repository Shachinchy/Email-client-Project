package org.example;
import javax.swing.*;
import java.io.File;

public class AttachmentChooser {
    public static File[] chooseAttachments() {
        // Leverage JFileChooser to create a dialog that allows users to navigate their filesystem and select one or more files to attach to an email.
        JFileChooser fileChooser = new JFileChooser();
        // Configure the file chooser to allow multi-selection, enhancing user experience by enabling the attachment of several files in one go.
        fileChooser.setMultiSelectionEnabled(true);
        // The method checks the user's choice returned by showOpenDialog().
        // and retrieves the selected files using getSelectedFiles().
        int option = fileChooser.showOpenDialog(null);  // null is passed, indicating that the dialog should be displayed without a parent component. This centers the dialog on the screen.
        if (option == JFileChooser.APPROVE_OPTION) {    // APPROVE_OPTION indicates that the user made a positive choice in the file chooser dialog ("Open" or "OK" button)
            return fileChooser.getSelectedFiles();
        }
        return new File[] {}; // Return an empty array if no selection
    }
}