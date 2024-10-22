package ScrollSystem.FileHandlers;

import java.io.*;
import java.nio.file.*;
import javax.swing.JFileChooser;

public class FileDownload {
    private static String DEFAULT_DOWNLOAD_DIR = System.getProperty("user.home") + File.separator + "Downloads" + File.separator + "VSAS_Downloads";
    private static final String SOURCE_FOLDER = "src/main/java/ScrollSystem/Scrolls/";
    ScrollDatabase scrollDb = new ScrollDatabase("src/main/java/ScrollSystem/Databases/database.db");
    private String downloadDirectory;

    public FileDownload() {
        this.downloadDirectory = DEFAULT_DOWNLOAD_DIR;
    }
    public FileDownload(String downloadDirectory) {
        this.downloadDirectory = downloadDirectory;
    }

    public void setDownloadDirectory(String downloadDirectory) {
        this.DEFAULT_DOWNLOAD_DIR = downloadDirectory;  // Allow setting the directory to a new path
    }

    public String downloadFile(String fileName) {
        File sourceFile;

        // Remove any duplicate paths
        fileName = fileName.replace(SOURCE_FOLDER + SOURCE_FOLDER, SOURCE_FOLDER);
        System.out.println("FILE: " + fileName);
        // scrollDb.updateNumDownloads(Integer.parseInt(scrollDb.getRowByName(fileName).get(0).get("ID")));

        // Check if fileName is a full path or just a filename
        if (fileName.contains(SOURCE_FOLDER)) {
            // If it's a full path, use it directly
            sourceFile = new File(fileName);
        } else if(new File(fileName).isAbsolute()) {
            sourceFile = new File(fileName);
        } else {
            // If it's just a filename, construct the full path
            sourceFile = new File(SOURCE_FOLDER, fileName);
        }

        // Check if file exists
        if (!sourceFile.exists()) {
            System.err.println("Fail to download: File not found at " + sourceFile.getAbsolutePath());
            return null;
        }

        // Create default download directory if it doesn't exist
        File downloadDir = new File(DEFAULT_DOWNLOAD_DIR);
        if (!downloadDir.exists()) {
            downloadDir.mkdirs();
        }

        File destinationFile = new File(downloadDir, sourceFile.getName());

        try {
            // Copy file from source to destination
            Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File downloaded successfully to: " + destinationFile.getAbsolutePath());
            return destinationFile.getAbsolutePath();
        } catch (IOException e) {
            System.err.println("Failed to download file: " + e.getMessage());
            return null;
        }
    }
}