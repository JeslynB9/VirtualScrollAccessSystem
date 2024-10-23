package ScrollSystem.FileHandlers;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/*
 * Database Structure
 *      userID: Integer
 *      scrollID: Integer 
 */
public class UserScroll {
    private final String url;

    public UserScroll(String filePath) {
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
        String createTableSQL = "CREATE TABLE IF NOT EXISTS UserScroll (" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "userId INTEGER NOT NULL, " +
                                "scrollId INTEGER NOT NULL, " +
                                "FOREIGN KEY (userId) REFERENCES Users(id), " +
                                "FOREIGN KEY (scrollId) REFERENCES Scrolls(id), " +
                                "UNIQUE (userId, scrollId))";
        
        try (Connection connection = getConnection();
            Statement stmt = connection.createStatement()) {

            stmt.execute(createTableSQL); 
        } catch (SQLException e) {
            e.printStackTrace();
        }                       
    }

    /** Whenever a user uploads a scroll, this function should be called 
     * @param 
     *      userId: int
     *      scrollId: int
     * @ret
     *      true if successfully updated to database, else false
     */
    public boolean uploadScroll(int userId, int scrollId) {
        if (rowExists(userId, scrollId)) {
            System.out.println("Error: userId " + userId + "and scrollId "+ scrollId +" already exist");
            return false;
        }
        if (!userExists(userId) || !scrollExists(scrollId)) {
            System.out.println("Error: userId or scrollId do not exist");
            return false;
        }

        String insertSQL = "INSERT INTO UserScroll (userId, scrollId) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, scrollId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;  
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /** Whenever a user deletes a scroll, this function should be called 
     * @param 
     *      scrollID: int
     * @ret
     *      true if successfully updated to database, else false
     */
    public boolean removeScroll(int scrollId) {
        if (!scrollExists(scrollId)) {
            System.out.println("Error: scrollId does not exist");
            return false;
        }
        String deleteSQL = "DELETE FROM UserScroll WHERE scrollId = ?";
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            
            pstmt.setInt(1, scrollId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;  
        } catch (SQLException e) {
            e.printStackTrace();
            return false; 
        }
    }

    /** Search scrolls by username
     * @params:     
     *      username: String
     * @ret: 
     *      Hashmap in the form userId, username, scrollId, scrollName
     */ 
    public List<HashMap<String, Object>> searchScrollsByUsername(String username) {
        String sql = "SELECT us.userId, l.username, us.scrollId, s.name " +
                     "FROM UserScroll us " +
                     "JOIN Users l ON us.userId = l.id " +
                     "JOIN Scrolls s ON us.scrollId = s.id " +
                     "WHERE l.username = ?";
        return executeSearch(sql, username);
    }

    /** Search scrolls by userId
     * @params:     
     *      userId: int
     * @ret: 
     *      Hashmap in the form userId, username, scrollId, scrollName
     */ 
    public List<HashMap<String, Object>> searchScrollsByUserId(int userId) {
        String sql = "SELECT us.userId, l.username, us.scrollId, s.name " +
                     "FROM UserScroll us " +
                     "JOIN Users l ON us.userId = l.id " +
                     "JOIN Scrolls s ON us.scrollId = s.id " +
                     "WHERE us.userId = ?";
        return executeSearch(sql, userId);
    }

    /** Search scrolls by scrollId
     * @params:     
     *      scrollId: int
     * @ret: 
     *      Hashmap in the form userId, username, scrollId, scrollName
     */ 
    public List<HashMap<String, Object>> searchScrollsByScrollId(int scrollId) {
        String sql = "SELECT us.userId, l.username, us.scrollId, s.name " +
                     "FROM UserScroll us " +
                     "JOIN Users l ON us.userId = l.id " +
                     "JOIN Scrolls s ON us.scrollId = s.id " +
                     "WHERE us.scrollId = ?";
        return executeSearch(sql, scrollId);
    }

    /** Search scrolls by scroll name
     * @params:     
     *      scrollId: int
     * @ret: 
     *      Hashmap in the form userId, username, scrollId, scrollName
     */ 
    public List<HashMap<String, Object>> searchScrollsByScrollName(String scrollName) {
        String sql = "SELECT us.userId, l.username, us.scrollId, s.name " +
                     "FROM UserScroll us " +
                     "JOIN Users l ON us.userId = l.id " +
                     "JOIN Scrolls s ON us.scrollId = s.id " +
                     "WHERE s.name LIKE ?";
        return executeSearch(sql, "%" + scrollName + "%"); 
    }

    /** executes the search 
     * @params:     
     *      sql: String | query
     *      param: Object | can be of any datatype 
     * @ret: 
     *      Hashmap in the form userId, username, scrollId, scrollName
     */ 
    private List<HashMap<String, Object>> executeSearch(String sql, Object param) {
        List<HashMap<String, Object>> results = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setObject(1, param);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("userId", rs.getInt("userId"));
                map.put("username", rs.getString("username"));
                map.put("scrollId", rs.getInt("scrollId"));
                map.put("scrollName", rs.getString("name"));
                results.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    /** Checks if a userId and scrollId pair already exists in the UserScroll table
     * @param 
     *      userId: int
     *      scrollId: int
     * @ret
     *      true if the pair exists, else false
     */
    public boolean rowExists(int userId, int scrollId) {
        String checkSQL = "SELECT COUNT(*) FROM UserScroll WHERE userId = ? AND scrollId = ?";
        try (Connection connection = getConnection();
            PreparedStatement pstmt = connection.prepareStatement(checkSQL)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, scrollId);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  
    }

    /** Checks if a userId exists in the Users table
     * @param 
     *      userId: int
     * @ret
     *      true if the userId exists, else false
     */
    private boolean userExists(int userId) {
        String checkSQL = "SELECT COUNT(*) FROM Users WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(checkSQL)) {
            
            pstmt.setInt(1, userId);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;  
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }

    /** Checks if a scrollId exists in the Scrolls table
     * @param 
     *      scrollId: int
     * @ret
     *      true if the scrollId exists, else false
     */
    private boolean scrollExists(int scrollId) {
        String checkSQL = "SELECT COUNT(*) FROM Scrolls WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(checkSQL)) {
            
            pstmt.setInt(1, scrollId);
            
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
     * Get the last inserted scrollId
     * @ret
     *      the last scrollid, else -1
     */
    // public int getLastScrollId() {
    //     String selectSQL = "SELECT MAX(scrollId) FROM UserScroll";
    //     try (Connection connection = getConnection();
    //         PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {

    //         ResultSet rs = pstmt.executeQuery();
    //         if (rs.next()) {
    //             return rs.getInt(1); 
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    //     return -1;  
    // }

    /**
     * Gets the scroll id with a given title
     * @params: 
     *      scrollName: String 
     * @ret: 
     *      id else -1
     */
    public int getScrollIdByTitle(String scrollName) {
        String selectSQL = "SELECT id FROM Scrolls WHERE name = ?";
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
    
            pstmt.setString(1, scrollName); 
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
