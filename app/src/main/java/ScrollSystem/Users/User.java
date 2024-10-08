package ScrollSystem.Users;
import ScrollSystem.FileHandlers.*;


import java.util.List;
import java.util.Map;
/*
 * Uses the FileHandlers to perform tasks (NOTE: frontend will not directly use the file handlers classes, i.e. frontend will use functions from here)
 */

public class User {
    protected String username;
    protected LoginDatabase loginDatabase;
    protected ScrollDatabase scrollDatabase;

    public User() {
        this.loginDatabase = new LoginDatabase("src/main/java/ScrollSystem/Databases/loginDatabase.db");
        this.scrollDatabase = new ScrollDatabase("src/main/java/ScrollSystem/Databases/scrollDatabase.db");
        loginDatabase.printAllUsers();
//        scrollDatabase.printAll();
    }

    public boolean login(String username, String password) {
        if (loginDatabase.checkCredentials(username, password)) {
            this.username = username;
            return true;
        }
        return false;
    }

    public boolean register(String username, String password, String fullName, String email, String phoneNo, Boolean admin) {
        return loginDatabase.addUser(username, password, fullName, email, phoneNo, admin);
    }

    public Map<String, String> getUserInfo() {
        return loginDatabase.getUserInfo(username);
    }

    public void updateUserInfo(String fullName, String email, String phoneNo) {
        loginDatabase.editUser(username, null, fullName, email, phoneNo);
    }

    public Map<String, String> getScrollById(int id) {
        return scrollDatabase.getRowById(id);
    }

    public List<Map<String, String>> viewAllScrolls() {
        return scrollDatabase.getAllScrolls();
    }

    public List<Map<String, String>> searchScrollsByName(String name) {
        return scrollDatabase.getRowByName(name);
    }

    public List<Map<String, String>> searchScrollsByAuthor(String author) {
        return scrollDatabase.getRowsByAuthor(author);
    }

    public List<Map<String, String>> searchScrollsByUploadDate(String uploadDate) {
        return scrollDatabase.getRowsByPublishDate(uploadDate);
    }

    public List<Map<String, String>> searchScrollsBetweenDates(String startDate, String endDate) {
        return scrollDatabase.getRowsBetweenPublishDate(startDate, endDate);
    }


    public boolean downloadScroll(int scrollId) {
        if (username == null) {
            System.out.println("User not logged in");
            return false;
        }

        // Get the file name of the scroll
        String fileName = scrollDatabase.getFileById(scrollId);
        if (fileName == null) {
            System.out.println("Scroll not found");
            return false;
        }

        // Use FileDownload to handle the actual file download
        FileDownload fileDownload = new FileDownload();
        boolean downloadSuccess = false;

        try {
            fileDownload.downloadFile(fileName);
            downloadSuccess = true;
        } catch (Exception e) {
            System.err.println("Error during download: " + e.getMessage());
            downloadSuccess = false;
        }

        if (downloadSuccess) {
            // Update the download count for the scroll
            scrollDatabase.updateNumDownloads(scrollId);
        }

        return downloadSuccess;
    }




























}