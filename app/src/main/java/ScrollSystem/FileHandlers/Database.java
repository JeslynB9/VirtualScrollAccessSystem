package ScrollSystem.FileHandlers;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Database Structure: 
 *      id         : integer | Primary Key 
 *      name       : String 
 *      author     : String 
 *      publishDate: Date
 *      lastUpdate : Date
 */
public class Database {
    private final String url;

    public Database(String filePath) {
        this.url = "jdbc:sqlite:" + filePath;  // SQLite URL
        initialiseDatabase();
    }

    /*
     * Create and return new connection to SQLite database using URL
     */
    Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

    /*
     * Initialise a database table if it doesnt exist
     */
    public void initialiseDatabase() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Scrolls ("
                + "ID INTEGER PRIMARY KEY, "
                + "name VARCHAR(30), "
                + "author VARCHAR(30), "
                + "publishDate datetime, "
                + "lastUpdate datetime"
                + ");";

        //try to open connection to the SQLite database
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.execute(createTableSQL); //create table if it doesnt exists
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a scroll to the database 
     * @params:     
     *      id: int 
     *      name : String 
     *      author: String 
     *      publishDate: String 
     *      lastUpdate: String 
     * @ret: 
     *      true if sucessfully added, else false
     */
    public boolean addRow(int id, String name, String author, String publishDate, String lastUpdate) {
        if (idExists(id)) {
            System.out.println("Fail to add: ID already exists");
            return false;
        }

        String insertSQL = "INSERT INTO Scrolls (ID, name, author, publishDate, lastUpdate) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, author);
            pstmt.setString(4, publishDate);
            pstmt.setString(5, lastUpdate);
            
            return pstmt.executeUpdate() > 0; //row was added
        } catch (SQLException e) {
            e.printStackTrace();
            return false; //row was not added
        }
    }


    /**
     * Modifies an existing scroll (row) in the database 
     * @params:     
     *      id: int 
     *      name : String 
     *      author: String 
     *      publishDate: String 
     *      lastUpdate: String 
     * @ret: 
     *      true if sucessfully edited, else false
     */
    public boolean editRow(int id, String name, String author, String publishDate, String lastUpdate) {
        if (!idExists(id)) {
            System.out.println("Fail to modify: ID does not exist");
            return false;
        } 
        
        String updateSQL = "UPDATE Scrolls SET name = ?, author = ?, publishDate = ?, lastUpdate = ? WHERE ID = ?";
    
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, author);
            pstmt.setString(3, publishDate);
            pstmt.setString(4, lastUpdate);
            pstmt.setInt(5, id);
            
            return pstmt.executeUpdate() > 0;
        } 
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes an existing scroll in the database
     * @param 
     *      id: int 
     */
    public boolean deleteRowById(int id) {
        String deleteSQL = "DELETE FROM Scrolls WHERE ID = ?";
        if (!idExists(id)) {
            System.out.println("Fail to delete: ID does not exist");
            return false;
        }
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } 
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets the rows with given id 
     * @param 
     *      id: int 
     * @return
     *      map of scrolls with the given id 
     */
    public Map<String, String> getRowById(int id) {
        String selectSQL = "SELECT * FROM Scrolls WHERE ID = ?";
        Map<String, String> rowData = new HashMap<>();
    
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
    
            if (rs.next()) {
                ResultSetMetaData metaData = rs.getMetaData(); //use metadata to get column name
                int columnCount = metaData.getColumnCount();
                
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String value = rs.getString(i);
                    rowData.put(columnName, value);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowData; 
    }
    
    
    /**
     * Gets the rows with given name 
     * @params: 
     *      name: String 
     * @ret: 
     *      map of scrolls with name (title)
     */
    public List<Map<String, String>> getRowByName(String name) {
        String selectSQL = "SELECT * FROM Scrolls WHERE name LIKE ?";
        List<Map<String, String>> rowDataList = new ArrayList<>();
    
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            
            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();
    
            while (rs.next()) {
                Map<String, String> rowData = new HashMap<>();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String value = rs.getString(i);
                    rowData.put(columnName, value);
                }
                rowDataList.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowDataList; 
    }
    
    
    /**
     * Gets the rows with the given author
     * @param 
     *      author: String 
     * @return
     *      map of scrolls with author 
     */
    public List<Map<String, String>> getRowsByAuthor(String author) {
        String selectSQL = "SELECT * FROM Scrolls WHERE author LIKE ?";
        List<Map<String, String>> rowDataList = new ArrayList<>();
    
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            
            pstmt.setString(1, "%" + author + "%");
            ResultSet rs = pstmt.executeQuery();
    
            while (rs.next()) {
                Map<String, String> rowData = new HashMap<>();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String value = rs.getString(i);
                    rowData.put(columnName, value);
                }
                rowDataList.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowDataList; 
    }
    
    /**
     * Gets the rows with the lastUpdate date 
     * @param 
     *      lastUpdate: String 
     * @return
     *      map of scrolls with lastUpdate 
     */
    public List<Map<String, String>> getRowsByLastUpdate(String lastUpdate) {
        String selectSQL = "SELECT * FROM Scrolls WHERE lastUpdate = ?";
        List<Map<String, String>> rows = new ArrayList<>();
    
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            
            pstmt.setString(1, lastUpdate);
            ResultSet rs = pstmt.executeQuery();
    
            while (rs.next()) {
                Map<String, String> rowData = new HashMap<>();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String value = rs.getString(i);
                    rowData.put(columnName, value);
                }
                rows.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows; 
    }

    /**
     * Gets the rows with the publishDate date 
     * @param 
     *      publishDate: String 
     * @return
     *      map of scrolls with publishDate 
     */
    public List<Map<String, String>> getRowsByPublishDate(String publishDate) {
        String selectSQL = "SELECT * FROM Scrolls WHERE publishDate = ?";
        List<Map<String, String>> rows = new ArrayList<>();
    
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            
            pstmt.setString(1, publishDate);
            ResultSet rs = pstmt.executeQuery();
    
            while (rs.next()) {
                Map<String, String> rowData = new HashMap<>();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String value = rs.getString(i);
                    rowData.put(columnName, value);
                }
                rows.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows; 
    }
    
    /**
     * Gets rows between two publish dates 
     * @params:
     *      startDate: String 
     *      endDate: String
     * @ret: 
     *      map of rows between two publish dates 
     */
    public List<Map<String, String>> getRowsBetweenPublishDate(String startDate, String endDate) {
        String selectSQL = "SELECT * FROM Scrolls WHERE publishDate BETWEEN ? AND ?";
        List<Map<String, String>> rows = new ArrayList<>();
    
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            
            pstmt.setString(1, startDate);
            pstmt.setString(2, endDate);
            ResultSet rs = pstmt.executeQuery();
    
            while (rs.next()) {
                Map<String, String> rowData = new HashMap<>();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String value = rs.getString(i);
                    rowData.put(columnName, value);
                }
                rows.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows; 
    }

    /**
     * Converts provided parameters into datetime format 
     * @params: 
     *      day, month, year, hour, min: int 
     * @ret:    
     *      converted datetime
     */
    public String convertToDatetime(int day, int month, int year, int hour, int min) {
        if (year <= 0 || month <= 0 || month > 12 || hour < 0 || hour >= 24 || min < 0 || min >= 60) {
            System.out.println("Error: Invalid Parameters in convertToDatetime");
            return null;
        }

        int maxDaysInMonth = LocalDateTime.of(year, month, 1, 0, 0).getMonth().length(LocalDateTime.of(year, month, 1, 0, 0).toLocalDate().isLeapYear());
        if (day <= 0 || day > maxDaysInMonth) {
            System.out.println("Error: Invalid day");
            return null;
        }

        LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, min);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    
    /**
     * Checks if an id (primary key) already exists in the database
     * @params: 
     *      id: int
     * @ret: 
     *      true if exists, else false 
     */
    private boolean idExists(int id) {
        String selectSQL = "SELECT COUNT(*) FROM Scrolls WHERE ID = ?";
        
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
    
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; //id exists
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }
}
