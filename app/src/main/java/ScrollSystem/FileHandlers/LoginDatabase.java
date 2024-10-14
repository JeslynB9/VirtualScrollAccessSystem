package ScrollSystem.FileHandlers;

import java.sql.*;
import java.util.*;
import java.security.*;
import java.nio.charset.StandardCharsets;

/**
 * Database Structure: 
 *      id         : Integer | Primary Key 
 *      username   : String  | Unique 
 *      pass       : String  
 *      fullName   : String
 *      email      : String 
 *      phoneNo    : String 
 *      admin      : boolean
 */

public class LoginDatabase {
    private final String url;

    public LoginDatabase(String filePath) {
        this.url = "jdbc:sqlite:" + filePath; 
        initialiseDatabase();
    }

    /*
     * Create and return new connection to SQLite database
     */
    Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

    /*
     * Initialise a database table
     */
    public void initialiseDatabase() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username VARCHAR(50) UNIQUE, "
                + "pass VARCHAR(255), "
                + "fullName VARCHAR(100), "
                + "email VARCHAR(100), "
                + "phoneNo VARCHAR(10), " 
                + "admin BOOLEAN DEFAULT FALSE"
                + ");";
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.execute(createTableSQL); 
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a user to the database 
     * @params:     
     *      username : String 
     *      pass     : String 
     *      fullName : String 
     *      email    : String 
     *      phoneNo  : String  
     * @ret: 
     *      true if successfully added, else false
     */
    public boolean addUser(String username, String pass, String fullName, String email, String phoneNo, Boolean admin) {
        if (!isValidPhoneNumber(phoneNo)) {
            System.out.println("Invalid phone number: must be exactly 10 digits");
            return false;
        } else if (usernameExists(username)) {
            System.out.println("Fail to add user: username already exists");
            return false;
        }


        String insertSQL = "INSERT INTO Users (username, pass, fullName, email, phoneNo, admin) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {

            pstmt.setString(1, username);
            pstmt.setString(2, hashPassword(pass)); //hash pass 
            pstmt.setString(3, fullName);
            pstmt.setString(4, email);
            pstmt.setString(5, phoneNo);
            pstmt.setBoolean(6, admin);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Edits an existing user in the database 
     * @params:     
     *      id       : int 
     *      username : String 
     *      pass     : String 
     *      fullName : String 
     *      email    : String 
     *      phoneNo  : String
     *      admin : boolean
     * @ret: 
     *      true if successfully edited, else false
     */
    public boolean editUser(int id, String username, String pass, String fullName, String email, String phoneNo) {
        System.out.println("Updating user with id: " + id);
        //ensure at least one field is given 
        if ((username == null || username.isEmpty()) &&
            (pass == null || pass.isEmpty()) &&
            (fullName == null || fullName.isEmpty()) &&
            (email == null || email.isEmpty()) &&
            (phoneNo == null || phoneNo.isEmpty())) {
            
            System.out.println("At least one field must be provided for update.");
            return false;
        }
        
        if (phoneNo != null && !phoneNo.isEmpty() && !isValidPhoneNumber(phoneNo)) {
            System.out.println("Invalid phone number: must be exactly 10 digits");
            return false;
        } 

        if (pass != null && !pass.isEmpty()) {
            pass = hashPassword(pass);
        }

        String updateSQL = "UPDATE Users SET " +
                            "username = COALESCE(?, username), " +
                            "pass = COALESCE(?, pass), " +
                            "fullName = COALESCE(?, fullName), " +
                            "email = COALESCE(?, email), " +
                            "phoneNo = COALESCE(?, phoneNo) " +
                            "WHERE id = ?";        
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {

            pstmt.setString(1, (username != null && !username.isEmpty()) ? username : null); //keep original value if empty or null
            pstmt.setString(2, (pass != null && !pass.isEmpty()) ? pass : null); 
            pstmt.setString(3, (fullName != null && !fullName.isEmpty()) ? fullName : null);
            pstmt.setString(4, (email != null && !email.isEmpty()) ? email : null);
            pstmt.setString(5, (phoneNo != null && !phoneNo.isEmpty()) ? phoneNo : null);
            pstmt.setInt(6, id);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes an existing user in the database
     * @param:
     *      username: String 
     */
    public boolean deleteUserByUsername(String username) {
        if (!usernameExists(username)) {
            System.out.println("Fail to delete user: username does not exist");
            return false;
        }
        String deleteSQL = "DELETE FROM Users WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {

            pstmt.setString(1, username);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets a user by username 
     * @param: 
     *      username: String 
     * @ret
     *      map of user data with given username, else null 
     */
    public Map<String, String> getUserInfo(String username) {
        if (!usernameExists(username)) {
            System.out.println("Fail to get user info: username does not exist");
            return null;
        }
        String selectSQL = "SELECT * FROM Users WHERE username = ?";
        Map<String, String> userData = new HashMap<>();

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    if ("admin".equals(columnName)) {
                        boolean isAdmin = rs.getBoolean(i);
                        userData.put(columnName, String.valueOf(isAdmin));
                    } else {
                        String value = rs.getString(i);
                        userData.put(columnName, value);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userData; 
    }

    /**
     * Gets all users 
     * @ret:
     *      list of users 
     */
    public List<Map<String, String>> getAllUsers() {
        String selectSQL = "SELECT * FROM Users";
        List<Map<String, String>> userDataList = new ArrayList<>();
        
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {
            
            while (rs.next()) {
                Map<String, String> userData = new HashMap<>();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
    
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String value = rs.getString(i);
                    userData.put(columnName, value);
                }
                userDataList.add(userData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userDataList; 
    }
    /**
     * Checks if the username matches with the password
     * @params:     
     *      username: String 
     *      password: String 
     * @ret: 
     *      true if username and password mathc else false 
     */
    public boolean checkCredentials(String username, String password) {
        String selectSQL = "SELECT pass FROM Users WHERE username = ?";
        
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("pass");
                //hash input password and compare with stored hash
                return storedHash.equals(hashPassword(password));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }

    /**
     * Hashes the password 
     * @param 
     *      password: String 
     * @return
     *      hashed password 
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Check if a username exists in the database
     * @param 
     *      username : String 
     * @return 
     *      true if the username exists, else false
     */
    private boolean usernameExists(String username) {
        String selectSQL = "SELECT COUNT(*) FROM Users WHERE username = ?";
        
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
    
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
    
            if (rs.next()) {
                return rs.getInt(1) > 0; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }

    /**
     * Grant a given username admin functionalities 
     * @param
     *      username: String 
     * @return
     *      true if successfully set, else false 
     */
    public boolean setAdmin(String username) {
        String updateSQL = "UPDATE Users SET admin = TRUE WHERE username = ?";
    
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
    
            pstmt.setString(1, username);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if a given username is an admin 
     * @param 
     *      username: String 
     * @return
     *      true if is admin, else false 
     */
    public boolean isAdmin(String username) {
        String selectSQL = "SELECT admin FROM Users WHERE username = ?";

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getBoolean("admin");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * check phone number is exactly 10 digits
     * @param 
     *      phoneNo the phone number to validate
     * @return 
     *      true if valid, false otherwise
     */
    private boolean isValidPhoneNumber(String phoneNo) {
        return phoneNo.matches("\\d{10}");
    }

    /**
     * Prints all users in the database
     */
    public void printAllUsers() {
        List<Map<String, String>> allUsers = getAllUsers();

        if (allUsers.isEmpty()) {
            System.out.println("No users found in the database.");
        } else {
            System.out.printf("%-20s %-40s %-30s %-30s %-15s %-10s%n",
                    "Username", "Password Hash", "Full Name", "Email", "Phone No", "Admin");
            System.out.println("=".repeat(150)); // Print separator line

            for (Map<String, String> user : allUsers) {
                System.out.printf("%-20s %-40s %-30s %-30s %-15s %-10s%n",
                        user.get("username"),
                        user.get("pass"),
                        user.get("fullName"),
                        user.get("email"),
                        user.get("phoneNo"),
                        user.get("admin"));
            }
        }
    }

    /**
     * Gets the id of a user based on their username.
     * @param 
     *      username: String 
     * @return 
     *      The id of the user, else -1 
     */
    public int getUserIdByUsername(String username) {
        String selectSQL = "SELECT id FROM Users WHERE username = ?";
        try (Connection connection = getConnection();
            PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");  
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; 
    }

}
