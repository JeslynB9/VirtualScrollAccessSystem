package ScrollSystem.FileHandlers;

import java.io.*;
import java.nio.file.*;
import javax.swing.JFileChooser;

public class FileUpload {

    
    public String uploadFile() {
        // Create a file chooser dialog to let the user select a file
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // Define the destination folder
            File destinationFolder = new File("src/main/java/ScrollSystem/Scrolls/");

            // Ensure the folder exists
            if (!destinationFolder.exists()) {
                destinationFolder.mkdirs();  // Create the folder if it doesn't exist
            }

            // Create the destination path
            File destinationFile = new File(destinationFolder, selectedFile.getName());

            try {
                // Copy the file from user's computer to the destination folder
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File uploaded successfully to: " + destinationFile.getAbsolutePath());
                return destinationFile.getAbsolutePath();
            } catch (IOException e) {
                System.err.println("Failed to upload file: " + e.getMessage());
                return null;
            }
        }
        return null;
    }
}
