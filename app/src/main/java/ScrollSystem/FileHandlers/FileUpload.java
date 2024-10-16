package ScrollSystem.FileHandlers;

import java.io.*;
import java.nio.file.*;
import javax.swing.JFileChooser;

//https://www.geeksforgeeks.org/java-swing-jfilechooser/
//https://www.youtube.com/watch?v=A6sA9KItwpY

public class FileUpload {

    private File selectedFile;

    /**
     * Opens a file chooser to open the file 
     * @ret: 
     *      a File, else null 
     */
    public File browseFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            return selectedFile;
        }
        return null;
    }

    /**
     * Copies the file from the users computer to the destination folder (Scrolls)
     * @ret:
     *      path to the file uploaded, else null 
     */
    public String uploadFile() {
        if (selectedFile == null) {
            System.err.println("No file selected");
            return null;
        }

        File destinationFolder = new File("src/main/java/ScrollSystem/Scrolls/");

        //check folder exists
        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs();  
        }

        //concat stirng 
        String filePath = "src/main/java/ScrollSystem/Scrolls/" + selectedFile.getName();
        
        //set destination path
        File destinationFile = new File(filePath);

        try {
            //copy file from user computer to destination folder
            Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File uploaded successfully to: " + filePath);
            return filePath;
        } catch (IOException e) {
            System.err.println("Failed to upload file: " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets the name of the file
     * @ret
     *      name of file else null
     */
    public String getFileName() {
        if (selectedFile != null) {
            return selectedFile.getName();
        } else {
            System.out.println("No file selected");
            return null;
        }
    }

    /**
     * Gets absolute path of file from the users computer 
     * @ret: 
     *      path to file in user computer, else null
     */
    public String getUserFilePath() {
        if (selectedFile != null) {
            return selectedFile.getAbsolutePath();
        } else {
            System.out.println("No file selected");
            return null;
        }
    }
}
