package ScrollSystem.FileHandlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.*;
import java.io.*;
import java.util.*;

public class DatabaseTest {
    private Database database;
    private final String DATABASE_PATH = "src/test/java/ScrollSystem/resources/database.db";

    @Before
    public void setUp() {
        //delete existing file first 
        File databaseFile = new File(DATABASE_PATH);
        if (databaseFile.exists()) {
            boolean deleted = databaseFile.delete();
            assertTrue("Test Setup Fail: cant delete existing database file", deleted);
        }

        //intitialise database 
        database = new Database(DATABASE_PATH);
        database.initialiseDatabase();
    }

    @Test //test if file exists
    public void testInitialiseDatabase() {
        assertTrue(new File(DATABASE_PATH).exists());
    }

    @Test //general case adding valid rows 
    public void testAddRow1() {
        boolean result = database.addRow(1, "Scroll of Wisdom", "Author A", "2024-01-01 00:00:00", "2024-01-01 00:00:00");
        assertTrue(result);
        
        Map<String, String> row = database.getRowById(1);
        assertEquals("Scroll of Wisdom", row.get("name"));
        assertEquals("Author A", row.get("author"));
    }

    @Test //adding a row with an existing id 
    public void testAddRow2() {
        database.addRow(1, "Scroll of Wisdom", "Author A", "2024-01-01 00:00:00", "2024-01-01 00:00:00");
        boolean result = database.addRow(1, "Scroll of Knowledge", "Author B", "2024-01-02 00:00:00", "2024-01-02 00:00:00");
        assertFalse(result); 
    }

    @Test //general case editing a row
    public void testEditRow1() {
        database.addRow(1, "Scroll of Wisdom", "Author A", "2024-01-01 00:00:00", "2024-01-01 00:00:00");
        boolean result = database.editRow(1, "Scroll of Knowledge", "Author B", "2024-01-02 00:00:00", "2024-01-02 00:00:00");
        assertTrue(result);
        
        Map<String, String> row = database.getRowById(1);
        assertEquals("Scroll of Knowledge", row.get("name"));
        assertEquals("Author B", row.get("author"));
    }

    @Test //editing a non existent row 
    public void testEditRow2() {
        boolean result = database.editRow(99, "Nonexistent Scroll", "Unknown Author", "2024-01-01 00:00:00", "2024-01-01 00:00:00");
        assertFalse(result);
    }

    @Test //general case deleting row 
    public void testDeleteRow1() {
        database.addRow(1, "Scroll of Wisdom", "Author A", "2024-01-01 00:00:00", "2024-01-01 00:00:00");
        boolean result = database.deleteRowById(1);
        assertTrue(result);
        
        Map<String, String> row = database.getRowById(1);
        assertTrue(row.isEmpty());
    }

    @Test //deleting a row with non existent id  
    public void testDeleteRow2() {
        boolean result = database.deleteRowById(99);
        assertFalse(result); 
    }

    @Test //general case getting row by id 
    public void testGetRowById1() {
        database.addRow(1, "Scroll of Wisdom", "Author A", "2024-01-01 00:00:00", "2024-01-01 00:00:00");
        Map<String, String> row = database.getRowById(1);
        assertNotNull(row);
        assertEquals("Scroll of Wisdom", row.get("name"));
    }

    @Test //getting a row with non existent id 
    public void testGetRowById2() {
        Map<String, String> row = database.getRowById(99);
        assertTrue(row.isEmpty()); 
    }

    @Test //general case getting rows by name 
    public void testGetRowByName1() {
        database.addRow(1, "Scroll of Wisdom", "Author A", "2024-01-01 00:00:00", "2024-01-01 00:00:00");
        List<Map<String, String>> rows = database.getRowByName("Scroll of Wisdom");
        assertEquals(1, rows.size());
        assertEquals("Author A", rows.get(0).get("author"));
    }

    @Test //general case getting rows by author full name 
    public void testGetRowsByAuthor1() {
        database.addRow(1, "Scroll of Wisdom", "Author A", "2024-01-01 00:00:00", "2024-01-01 00:00:00");
        database.addRow(2, "Another Scroll", "Author A", "2024-01-02 00:00:00", "2024-01-02 00:00:00");
        
        List<Map<String, String>> rows = database.getRowsByAuthor("Author A");
        assertEquals(2, rows.size());
    }

    @Test //general case getting rows by author partial name 
    public void testGetRowsByAuthor2() {
        database.addRow(1, "Scroll of Wisdom", "Author A", "2024-01-01 00:00:00", "2024-01-01 00:00:00");
        database.addRow(2, "Another Scroll", "Author B", "2024-01-02 00:00:00", "2024-01-02 00:00:00");
        
        List<Map<String, String>> rows = database.getRowsByAuthor("Author");
        assertEquals(2, rows.size());
    }

    @Test //general case for getting rows by lastUpdateDate
    public void testGetRowsByLastUpdate() {
        String lastUpdate = "2024-01-01 00:00:00";
        database.addRow(1, "Scroll of Wisdom", "Author A", "2024-01-01 00:00:00", lastUpdate);
        database.addRow(2, "Another Scroll", "Author B", "2024-01-02 00:00:00", "2024-01-02 00:00:00");

        List<Map<String, String>> rows = database.getRowsByLastUpdate(lastUpdate);
        assertEquals(1, rows.size()); 
        assertEquals("Scroll of Wisdom", rows.get(0).get("name"));
    }

    
    @Test //general case for getting rows by publishDate
    public void testGetRowsByPublishDate() {
        String publishDate = "2024-01-01 00:00:00";
        database.addRow(1, "Scroll of Wisdom", "Author A", publishDate, "2024-01-01 00:00:00");
        database.addRow(2, "Another Scroll", "Author B", "2024-01-02 00:00:00", "2024-01-02 00:00:00");

        List<Map<String, String>> rows = database.getRowsByPublishDate(publishDate);
        assertEquals(1, rows.size()); 
        assertEquals("Scroll of Wisdom", rows.get(0).get("name"));
    }

    
    @Test //general case getting rows between two publish dates
    public void testGetRowsBetweenPublishDate() {
        database.addRow(1, "Scroll of Wisdom", "Author A", "2024-01-01 00:00:00", "2024-01-01 00:00:00");
        database.addRow(2, "Another Scroll", "Author A", "2024-01-02 00:00:00", "2024-01-02 00:00:00");
        database.addRow(3, "Third Scroll", "Author B", "2024-01-03 00:00:00", "2024-01-03 00:00:00");

        List<Map<String, String>> rows = database.getRowsBetweenPublishDate("2024-01-01 00:00:00", "2024-01-02 00:00:00");
        assertEquals(2, rows.size()); 
        assertTrue(rows.stream().anyMatch(row -> row.get("name").equals("Scroll of Wisdom")));
        assertTrue(rows.stream().anyMatch(row -> row.get("name").equals("Another Scroll")));
    }

    @Test //test convertion datetime
    public void testConvertToDatetime1() {
        String datetime = database.convertToDatetime(1, 1, 2024, 12, 0);
        assertEquals("2024-01-01 12:00:00", datetime);
    }

    @Test //testing convertion to datetime with invalid parameters 
    public void testConvertToDatetime2() {
        assertNull(database.convertToDatetime(0, 0, 0, 0, 0)); 
        assertNull(database.convertToDatetime(-1, -1, -1, -1, -1));
        assertNull(database.convertToDatetime(12, 6, 2024, -1, 0)); // invalid hour (negative)
        assertNull(database.convertToDatetime(12, 6, 2024, 12, -1)); // invalid minute (negative)
        assertNull(database.convertToDatetime(30, 2, 2024, 12, 0)); //invalid day for feb
        assertNotNull(database.convertToDatetime(29, 2, 2024, 12, 0)); //feb leap year
        assertNull(database.convertToDatetime(32, 1, 2024, 12, 0)); //invalid day for jan
        assertNull(database.convertToDatetime(31, 4, 2024, 12, 0)); //invalid day for april
        assertNull(database.convertToDatetime(0, 4, 2024, 12, 0)); //invalid day
        assertNull(database.convertToDatetime(12, 13, 2024, 12, 0)); //invalid month
        assertNull(database.convertToDatetime(12, 0, 2024, 12, 0)); //invalid month
        assertNull(database.convertToDatetime(12, 6, 2024, 25, 0)); //invalid hour
        assertNull(database.convertToDatetime(12, 6, 2024, 24, 0)); //invalid hour
        assertNull(database.convertToDatetime(12, 6, 0, 12, 60)); //invalid minute
        assertNull(database.convertToDatetime(12, 6, 2024, 12, 70)); //invalid minute
    }
}
