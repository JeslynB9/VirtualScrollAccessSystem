package ScrollSystem.Users;

/*
 * Uses the FileHandlers to perform tasks (NOTE: frontend will not directly use the file handlers classes, i.e. frontend will use functions from here)
 */


import java.util.List;
import java.util.Map;

public class Admin extends User {
    public Admin() {
        super();
    }

    @Override
    public boolean login(String username, String password) {
        if (super.login(username, password)) {
            // Check if the user is an admin
            Map<String, String> userInfo = getUserInfo();
            return userInfo != null && "admin".equals(userInfo.get("userType"));
        }
        return false;
    }

    public List<Map<String, String>> getAllUsers() {
        return loginDatabase.getAllUsers();
    }

    public boolean addUser(String username, String password, String fullName, String email, String phoneNo, boolean admin) {
        return loginDatabase.addUser(username, password, fullName, email, phoneNo, admin);
    }

    public boolean deleteUser(String username) {
        return loginDatabase.deleteUserByUsername(username);
    }

    // Add more admin-specific methods as needed
}