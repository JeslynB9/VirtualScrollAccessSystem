package ScrollSystem.FileHandlers;

import java.io.*;
import java.nio.file.*;
import javax.swing.JFileChooser;

//https://www.geeksforgeeks.org/java-swing-jfilechooser/
//https://www.youtube.com/watch?v=A6sA9KItwpY

public class FileUpload {

    private File selectedFile;
    private ScrollDatabase scrollDatabase = new ScrollDatabase("src/main/java/ScrollSystem/Databases/database.db");

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
        if (fileExistsScrolls()) {
            System.out.println("File with the same name already exists.");
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
     * Deletes an existing scroll in the database and moves the file to the archive.
     *
     * @param id: int
     */
    public boolean deleteRowById(int id) {
        System.out.println("DELETING ROW ----------------------------------------------");
        if (!scrollDatabase.idExists(id)) {
            System.out.println("Fail to delete: ID does not exist");
            return false;
        }

        String filePath = scrollDatabase.getFileById(id);
        if (filePath != null) {
            try {
                File fileToMove = new File(filePath);
                File archiveFolder = new File("src/main/java/ScrollSystem/Scrolls/Archive");

                if (!archiveFolder.exists()) {
                    archiveFolder.mkdirs();
                }

                String newFileName = getUniqueFileName(archiveFolder, fileToMove.getName());
                File archiveFile = new File(archiveFolder + "/" + newFileName);

                Files.move(fileToMove.toPath(), archiveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File moved to archive: " + archiveFile.getAbsolutePath());
                return true;

            } catch (IOException e) {
                System.err.println("Failed to move file to archive: " + e.getMessage());
                return false;
            }
        }
        return false;
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

    /**
     * Checks if a file with the same name already exists in the Scrolls folder
     * @ret
     *      true if file exists, otherwise false
     */
    public boolean fileExistsScrolls() {
        if (selectedFile == null) {
            System.err.println("File is null");
            return false;
        }

        File destinationFile = new File("src/main/java/ScrollSystem/Scrolls/" + selectedFile.getName());
        return destinationFile.exists();
    }

    /**
     * Generates a unique file name- appends a number if file already exists
     * @params: 
     *      directory: File 
     *      originalName : String 
     * @ret
     *      new file name
     */
    public String getUniqueFileName(File directory, String originalName) {
        String baseName = originalName;
        String extension = "";
    
        int dotIndex = originalName.lastIndexOf(".");
        if (dotIndex != -1) {
            baseName = originalName.substring(0, dotIndex);
            extension = originalName.substring(dotIndex);
        }
    
        String newFileName = originalName;
        int counter = 1;
    
        while (new File(directory, newFileName).exists()) {
            newFileName = baseName + "(" + counter + ")" + extension;
            counter++;
        }
    
        return newFileName;
    }
}
