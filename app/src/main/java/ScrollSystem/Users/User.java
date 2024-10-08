package ScrollSystem.Users;
import ScrollSystem.FileHandlers.LoginDatabase;
import ScrollSystem.FileHandlers.ScrollDatabase;

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

    // Add more methods as needed for user functionality
}