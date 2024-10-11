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

    protected UserScroll UserScroll;
    private final String DATABASE_PATH = "src/main/java/ScrollSystem/Databases/database.db";


    public User() {
        this.loginDatabase = new LoginDatabase(DATABASE_PATH);
        this.scrollDatabase = new ScrollDatabase(DATABASE_PATH);
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

    public void register(String username, String password, String fullName, String email, String phoneNo, Boolean admin) {
        loginDatabase.addUser(username, password, fullName, email, phoneNo, admin);
        System.out.println("User registered successfully.");

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



    public boolean uploadScroll(int id, String name, String author, String publishDate, String filePath) {
        boolean addedToDatabase = scrollDatabase.addRow(id, name, author, publishDate, filePath);
        if (addedToDatabase) {
            // Get the user's ID from the loginDatabase
            Map<String, String> userInfo = loginDatabase.getUserInfo(username);
            if (userInfo != null && userInfo.containsKey("id")) {
                int userId = Integer.parseInt(userInfo.get("id"));
                // Use the correct method from UserScroll
                return UserScroll.uploadScroll(userId, id);
            }
        }
        return false;
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