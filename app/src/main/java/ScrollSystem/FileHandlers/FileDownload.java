package ScrollSystem.FileHandlers;

import java.io.*;
import java.nio.file.*;
import javax.swing.JFileChooser;

//https://www.geeksforgeeks.org/java-swing-jfilechooser/
//https://www.youtube.com/watch?v=A6sA9KItwpY

public class FileDownload {

    public void downloadFile(String fileName) {
        File sourceFolder = new File("src/main/java/ScrollSystem/Scrolls/");
        File sourceFile = new File(sourceFolder, fileName);

        //check file exists
        if (!sourceFile.exists()) {
            System.err.println("Fail to download: File not found at " + sourceFile.getAbsolutePath());
            return;
        }

        //open file chooser dialog - user chooses where to save file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(fileName));
        int result = fileChooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File destinationFile = fileChooser.getSelectedFile();

            try {
                //copy file from Scrolls folder to chosen destination
                Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File downloaded successfully to: " + destinationFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Failed to download file: " + e.getMessage());
            }
        }
    }
}
