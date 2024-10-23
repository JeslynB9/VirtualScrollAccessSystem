package ScrollSystem.FileHandlers;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.*;
import java.sql.*;

import java.io.*;
import java.util.*;

public class UserScrollTest {
    private UserScroll userScroll;
    private ScrollDatabase scrollDatabase;
    private LoginDatabase loginDatabase;
    private final String DATABASE_PATH = "src/test/java/ScrollSystem/resources/database.db";

    @Before
    public void setUp() {
        //if file exists delete
        File databaseFile = new File(DATABASE_PATH);
        if (databaseFile.exists()) {
            databaseFile.delete();
        }

        scrollDatabase = new ScrollDatabase(DATABASE_PATH);
        scrollDatabase.initialiseDatabase();

        loginDatabase = new LoginDatabase(DATABASE_PATH);
        loginDatabase.initialiseDatabase();

        loginDatabase.addUser("tebo", "rawr", "te bo", "tebo@chillipeppers.com", "0412345678", false);
        loginDatabase.addUser("rebo", "meow", "re bo", "rebo@dinonuggets.com", "0202", false);
        
        scrollDatabase.addRow(1, "Scroll of Wisdom", "Author A", "2024-01-01 00:00", "scroll_wisdom.pdf");
        scrollDatabase.addRow(2, "Scroll of Brains", "Author A", "2024-01-01 00:00", "scroll_brains.pdf");

        userScroll = new UserScroll(DATABASE_PATH);
        userScroll.initialiseDatabase();
    }

    Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + DATABASE_PATH);
    }

    @Test //general case
    public void testUploadScroll1() {
        int userId = 1; 
        int scrollId = 1; 
        assertFalse(userScroll.rowExists(userId, scrollId));
        boolean result = userScroll.uploadScroll(userId, scrollId);
        assertTrue(result);
        assertTrue(userScroll.rowExists(userId, scrollId));
    }

    @Test //uploading duplicate 
    public void testUploadScroll2() {
        int userId = 1;
        int scrollId = 1;
        userScroll.uploadScroll(userId, scrollId);
        boolean result = userScroll.uploadScroll(userId, scrollId);
        assertFalse(result);
    }

    @Test //non-existant 
    public void testUploadScroll3() {
        int nonExistentUserId = 999;
        int nonExistentScrollId = 999;
        assertFalse(userScroll.uploadScroll(nonExistentUserId, 101));
        assertFalse(userScroll.uploadScroll(1, nonExistentScrollId));
    }

    @Test //general case 
    public void testRemoveScroll1() {
        int userId = 1;
        int scrollId = 2;
                
        userScroll.uploadScroll(userId, scrollId);
        boolean result = userScroll.removeScroll(scrollId);
        assertTrue(result);
        assertFalse(userScroll.rowExists(userId, scrollId));
    }

    @Test //non-existent
    public void testRemoveScroll2() {
        int nonExistentScrollId = 999;
        boolean result = userScroll.removeScroll(nonExistentScrollId);
        assertFalse(result);
    }

    @Test
    public void testSearchScrollsByUsername() {
        int userId = 1; 
        int scrollId1 = 1;
        int scrollId2 = 2;

        userScroll.uploadScroll(userId, scrollId1);
        userScroll.uploadScroll(userId, scrollId2);

        List<HashMap<String, Object>> results = userScroll.searchScrollsByUsername("tebo");
        
        assertEquals(2, results.size());
        assertEquals("tebo", results.get(0).get("username"));
        assertEquals(1, results.get(0).get("scrollId"));
        assertEquals("tebo", results.get(1).get("username"));
        assertEquals(2, results.get(1).get("scrollId"));
    }

    @Test
    public void testSearchScrollsByUserId() {
        int userId = 1; 
        int scrollId1 = 1; 
        int scrollId2 = 2;

        userScroll.uploadScroll(userId, scrollId1);
        userScroll.uploadScroll(userId, scrollId2);

        List<HashMap<String, Object>> results = userScroll.searchScrollsByUserId(userId);
        
        assertEquals(2, results.size());
        assertEquals(userId, results.get(0).get("userId"));
        assertEquals("tebo", results.get(0).get("username"));
        assertEquals(scrollId1, results.get(0).get("scrollId"));
        assertEquals("Scroll of Wisdom", results.get(0).get("scrollName"));

        assertEquals(userId, results.get(1).get("userId"));
        assertEquals("tebo", results.get(1).get("username"));
        assertEquals(scrollId2, results.get(1).get("scrollId"));
        assertEquals("Scroll of Brains", results.get(1).get("scrollName"));
    }

    @Test
    public void testSearchScrollsByScrollId() {
        int userId = 1;
        int scrollId = 1; 

        userScroll.uploadScroll(userId, scrollId);

        List<HashMap<String, Object>> results = userScroll.searchScrollsByScrollId(scrollId);
        
        assertEquals(1, results.size());
        assertEquals(userId, results.get(0).get("userId"));
        assertEquals("tebo", results.get(0).get("username"));
        assertEquals(scrollId, results.get(0).get("scrollId"));
        assertEquals("Scroll of Wisdom", results.get(0).get("scrollName"));
    }

    @Test
    public void testSearchScrollsByScrollName() {
        int userId = 1; 
        int scrollId1 = 1;
        int scrollId2 = 2; 

        userScroll.uploadScroll(userId, scrollId1);
        userScroll.uploadScroll(userId, scrollId2);

        List<HashMap<String, Object>> results = userScroll.searchScrollsByScrollName("Scroll");
        
        assertEquals(2, results.size());
        assertEquals(userId, results.get(0).get("userId"));
        assertEquals("tebo", results.get(0).get("username"));
        assertEquals(scrollId1, results.get(0).get("scrollId"));
        assertEquals("Scroll of Wisdom", results.get(0).get("scrollName"));

        assertEquals(userId, results.get(1).get("userId"));
        assertEquals("tebo", results.get(1).get("username"));
        assertEquals(scrollId2, results.get(1).get("scrollId"));
        assertEquals("Scroll of Brains", results.get(1).get("scrollName"));
        
        List<HashMap<String, Object>> exactResults = userScroll.searchScrollsByScrollName("Scroll of Brains");
        assertEquals(1, exactResults.size());
        assertEquals("Scroll of Brains", exactResults.get(0).get("scrollName"));
    }


    @Test //general 
    public void testGetScrollIdByTitle1() {
        scrollDatabase.addRow(1, "Scroll of Wisdom", "Author A", "2024-01-01 00:00", "scroll_wisdom.pdf");
        scrollDatabase.addRow(2, "Scroll of Brains", "Author B", "2024-01-01 00:00", "scroll_brains.pdf");

        int scrollId = userScroll.getScrollIdByTitle("Scroll of Wisdom");
        assertEquals(1, scrollId);
    }

    @Test //title doesnt exist
    public void testGetScrollIdByTitle2() {
        int scrollId = userScroll.getScrollIdByTitle("Scroll of Shadows");
        assertEquals(-1, scrollId);
    }
}

