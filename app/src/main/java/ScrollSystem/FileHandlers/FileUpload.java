package ScrollSystem.FileHandlers;

import java.io.*;
import java.nio.file.*;
import javax.swing.JFileChooser;

//https://www.geeksforgeeks.org/java-swing-jfilechooser/
//https://www.youtube.com/watch?v=A6sA9KItwpY

public class FileUpload {

    public String uploadFile() {
        //create a file chooser dialog
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            if (!isBinaryFile(selectedFile)) {
                System.err.println("Error: selected file is not a binary file.");
                return null;
            }

            File destinationFolder = new File("src/main/java/ScrollSystem/Scrolls/");

            //check folder exists
            if (!destinationFolder.exists()) {
                destinationFolder.mkdirs();  
            }

            //set destination path
            File destinationFile = new File(destinationFolder, selectedFile.getName());

            try {
                //copy file from user computer to destination folder
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

    private boolean isBinaryFile(File file) {
        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            for (byte b : fileContent) {
                if (b < 0x20 && b != 0x09 && b != 0x0A && b != 0x0D) {
                    return true; 
                }
            }
            return false;
        } catch (IOException e) {
            System.err.println("Error: fail to check if file is binary: " + e.getMessage());
            return false; 
        }
    }
}
