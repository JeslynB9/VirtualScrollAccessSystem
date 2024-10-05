package ScrollSystem.FileHandlers;

import java.io.*;
import java.nio.file.*;
import javax.swing.JFileChooser;

public class FileDownload {

    public void downloadFile(String fileName) {
        // Define the source folder (the Scrolls folder)
        File sourceFolder = new File("src/main/java/ScrollSystem/Scrolls/");
        File sourceFile = new File(sourceFolder, fileName);

        // Check if the file exists
        if (!sourceFile.exists()) {
            System.err.println("File not found: " + sourceFile.getAbsolutePath());
            return;
        }

        // Open a file chooser dialog to allow the user to choose where to save the file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(fileName));
        int result = fileChooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File destinationFile = fileChooser.getSelectedFile();

            try {
                // Copy the file from the Scrolls folder to the chosen destination
                Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File downloaded successfully to: " + destinationFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Failed to download file: " + e.getMessage());
            }
        }
    }
}
