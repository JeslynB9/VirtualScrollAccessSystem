package ScrollSystem.FileHandlers;

import java.sql.*;
import java.util.*;
import java.security.*;
import java.nio.charset.StandardCharsets;

/**
 * Database Structure: 
 *      username   : String  | Primary Key 
 *      pass       : String  | title of scroll
 *      full name  : String
 *      email      : String 
 *      phone no.  : String 
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
                + "username VARCHAR(50) PRIMARY KEY, "
                + "pass VARCHAR(255), "
                + "fullName VARCHAR(100), "
                + "email VARCHAR(100), "
                + "phoneNo VARCHAR(10)" 
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
    public boolean addUser(String username, String pass, String fullName, String email, String phoneNo) {
        if (!isValidPhoneNumber(phoneNo)) {
            System.out.println("Invalid phone number: must be exactly 10 digits");
            return false;
        } else if (usernameExists(username)) {
            System.out.println("Fail to add user: username already exists");
            return false;
        }


        String insertSQL = "INSERT INTO Users (username, pass, fullName, email, phoneNo) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {

            pstmt.setString(1, username);
            pstmt.setString(2, hashPassword(pass)); //hash pass 
            pstmt.setString(3, fullName);
            pstmt.setString(4, email);
            pstmt.setString(5, phoneNo); 

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Edits an existing user in the database 
     * @params:     
     *      username : String 
     *      pass     : String 
     *      fullName : String 
     *      email    : String 
     *      phoneNo  : String 
     * @ret: 
     *      true if successfully edited, else false
     */
    public boolean editUser(String username, String pass, String fullName, String email, String phoneNo) {
        if (!isValidPhoneNumber(phoneNo)) {
            System.out.println("Invalid phone number: must be exactly 10 digits");
            return false;
        } 

        String updateSQL = "UPDATE Users SET pass = ?, fullName = ?, email = ?, phoneNo = ? WHERE username = ?";

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {

            pstmt.setString(1, pass);
            pstmt.setString(2, fullName);
            pstmt.setString(3, email);
            pstmt.setString(4, phoneNo);
            pstmt.setString(5, username);

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
                    String value = rs.getString(i);
                    userData.put(columnName, value);
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
     * check phone number is exactly 10 digits
     * @param 
     *      phoneNo the phone number to validate
     * @return 
     *      true if valid, false otherwise
     */
    private boolean isValidPhoneNumber(String phoneNo) {
        return phoneNo.matches("\\d{10}");
    }
}
