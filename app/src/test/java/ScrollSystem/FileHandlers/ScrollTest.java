package ScrollSystem.FileHandlers;

import static org.junit.Assert.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import org.junit.*;
import java.io.*;
import java.util.*;

public class ScrollTest {
    private ScrollDatabase database;
    private final String DATABASE_PATH = "src/test/java/ScrollSystem/resources/scrollDatabase.db";

    @Before
    public void setUp() {
        //delete existing file first 
        File databaseFile = new File(DATABASE_PATH);
        if (databaseFile.exists()) {
            boolean deleted = databaseFile.delete();
            assertTrue("Test Setup Fail: cant delete existing database file", deleted);
        }

        //intitialise database 
        database = new ScrollDatabase(DATABASE_PATH);
        database.initialiseDatabase();
    }

    @Test //test if file exists
    public void testInitialiseDatabase() {
        assertTrue(new File(DATABASE_PATH).exists());
    }

    @Test //general case adding valid rows 
    public void testAddRow1() {
        boolean result = database.addRow(1, "Scroll of Wisdom", "Author A", "2024-01-01 00:00", "scroll_wisdom.pdf");
        assertTrue(result);
        
        Map<String, String> row = database.getRowById(1);
        assertEquals("Scroll of Wisdom", row.get("name"));
        assertEquals("Author A", row.get("author"));
        assertEquals("scroll_wisdom.pdf", row.get("filePath"));
    }

    @Test //adding a row with an existing id 
    public void testAddRow2() {
        database.addRow(1, "Scroll of Wisdom", "Author A", "2024-01-01 00:00",  "scroll_wisdom.pdf");
        boolean result = database.addRow(1, "Scroll of Wisdom", "Author A", "2024-01-01 00:00", "scroll_wisdom.pdf");
        assertFalse(result); 
    }

    @Test //general case editing a row
    public void testEditRow1() {
        database.addRow(1, "Scroll of Wisdom", "Author A", "2024-01-01 00:00", "scroll_wisdom.pdf");        
        database.editRow(1, "Scroll of Brains", "Author A", "2024-01-01 00:00", "scroll_wisdom.pdf");
        
        Map<String, String> row = database.getRowById(1);
        assertEquals("Scroll of Brains", row.get("name"));
        assertEquals("Author A", row.get("author"));
        assertNull(database.getRowById(99));
    }

    @Test //editing a non existent row 
    public void testEditRow2() {
        boolean result = database.editRow(99, "Nonexistent Scroll", "Unknown Author", "2024-01-01 00:00", "scroll_wisdom.pdf");
        assertFalse(result);
    }

    @Test //general case deleting row 
    public void testDeleteRow1() {
        database.addRow(1, "Scroll of Wisdom", "Author A", "2024-01-01 00:00", "scroll_wisdom.pdf");        
        boolean result = database.deleteRowById(1);
        assertTrue(result);
        
        assertNull(database.getRowById(1));
    }

    @Test //deleting a row with non existent id  
    public void testDeleteRow2() {
        boolean result = database.deleteRowById(99);
        assertFalse(result); 
    }

    @Test //general case getting row by id 
    public void testGetRowById1() {
        database.addRow(1, "Scroll of Wisdom", "Author A", "2024-01-01 00:00", "scroll_wisdom.pdf");        
        Map<String, String> row = database.getRowById(1);
        assertNotNull(row);
        assertEquals("Scroll of Wisdom", row.get("name"));
    }

    @Test //getting a row with non existent id 
    public void testGetRowById2() {
        assertNull(database.getRowById(99)); 
    }

    @Test //general case getting rows by name 
    public void testGetRowByName1() {
        database.addRow(1, "Scroll of Wisdom", "Author A", "2024-01-01 00:00", "scroll_wisdom.pdf");        
        List<Map<String, String>> rows = database.getRowByName("Scroll of Wisdom");
        assertEquals(1, rows.size());
        assertEquals("Author A", rows.get(0).get("author"));
    }

    @Test //general case getting rows by author full name 
    public void testGetRowsByAuthor1() {
        database.addRow(1, "Scroll of Wisdom", "Author A", "2024-01-01 00:00",  "scroll_wisdom.pdf");        
        database.addRow(2, "Another Scroll", "Author A", "2024-01-02 00:00", "another_scroll_wisdom.pdf");
        
        List<Map<String, String>> rows = database.getRowsByAuthor("Author A");
        assertEquals(2, rows.size());
    }

    @Test //general case getting rows by author partial name 
    public void testGetRowsByAuthor2() {
        database.addRow(1, "Scroll of Wisdom", "Author A", "2024-01-01 00:00", "scroll_wisdom.pdf");        
        database.addRow(2, "Another Scroll", "Author B", "2024-01-02 00:00", "another_scroll_wisdom.pdf");
        
        List<Map<String, String>> rows = database.getRowsByAuthor("Author");
        assertEquals(2, rows.size());
    }

    @Test //general case for getting rows by lastUpdateDate
    public void testGetRowsByLastUpdate() {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        database.addRow(1, "Scroll of Wisdom", "Author A", "2023-12-31 23:59", "scroll_wisdom.pdf");        
        
        List<Map<String, String>> rows = database.getRowsByLastUpdate(currentTime);
        assertFalse(rows.isEmpty());
    }

    
    @Test //general case for getting rows by publishDate
    public void testGetRowsByPublishDate() {
        String publishDate = "2024-01-01 00:00";
        database.addRow(1, "Scroll of Wisdom", "Author A", "2024-01-01 00:00", "scroll_wisdom.pdf");
        List<Map<String, String>> rows = database.getRowsByPublishDate(publishDate);
        assertEquals(1, rows.size()); 
        assertEquals("Scroll of Wisdom", rows.get(0).get("name"));
    }

    
    @Test //general case getting rows between two publish dates
    public void testGetRowsBetweenPublishDate() {
        database.addRow(1, "Scroll of Wisdom", "Author A", "2024-01-01 00:00", "scroll_wisdom.pdf");        
        database.addRow(2, "Another Scroll", "Author A", "2024-01-02 00:00", "another_scroll_wisdom.pdf");
        database.addRow(3, "Third Scroll", "Author B", "2024-01-03 00:00", "third_scroll_wisdom.pdf");

        List<Map<String, String>> rows = database.getRowsBetweenPublishDate("2024-01-01 00:00", "2024-01-02 00:00");
        assertEquals(2, rows.size()); 
        assertTrue(rows.stream().anyMatch(row -> row.get("name").equals("Scroll of Wisdom")));
        assertTrue(rows.stream().anyMatch(row -> row.get("name").equals("Another Scroll")));
    }

    @Test //general case getting the number of downloads 
    public void testGetNumDownloads1() {
        database.addRow(1, "Scroll of Wisdom", "Author A", "2024-01-01 00:00", "scroll_wisdom.pdf");
        database.updateNumDownloads(1);
        int downloads = database.getNumDownloads(1);
        assertEquals(1, downloads);
    }

    @Test //getting num downloads with non-existent id
    public void testGetNumDownloads2() {
        int downloads = database.getNumDownloads(99);
        assertEquals(-1, downloads);
    }

    @Test //general case updating the number of downloads
    public void testUpdateNumDownloads1() {
        database.addRow(1, "Scroll of Wisdom", "Author A", "2024-01-01 00:00", "scroll_wisdom.pdf");
        boolean result = database.updateNumDownloads(1);
        assertTrue(result);
        assertEquals(1, database.getNumDownloads(1));
    }

    @Test //update downloads with non-existent ID
    public void testUpdateNumDownloads2() {
        boolean result = database.updateNumDownloads(99);
        assertFalse(result);
    }

    @Test //general case getting number of uploads
    public void testGetNumUploads1() {
        database.addRow(2, "Scroll of Knowledge", "Author B", "2024-01-01 00:00", "scroll_knowledge.pdf");
        database.updateNumUploads(2);
        int uploads = database.getNumUploads(2);
        assertEquals(1, uploads);
    }

    @Test //getting num uploads with non-existent id
    public void testGetNumUploads2() {
        int uploads = database.getNumUploads(99);
        assertEquals(-1, uploads);
    }

    @Test //general case updating the number of uploads
    public void testUpdateNumUploads1() {
        database.addRow(2, "Scroll of Knowledge", "Author B", "2024-01-01 00:00", "scroll_knowledge.pdf");
        boolean result = database.updateNumUploads(2);
        assertTrue(result);
        assertEquals(1, database.getNumUploads(2));
    }

    @Test //updating uploads for a non-existent ID
    public void testUpdateNumUploads2() {
        boolean result = database.updateNumUploads(99);
        assertFalse(result);
    }

    @Test //general case getting number of views 
    public void testGetNumViews1() {
        database.addRow(3, "Scroll of Insights", "Author C", "2024-01-01 00:00", "scroll_insights.pdf");
        database.updateNumViews(3);
        int views = database.getNumViews(3);
        assertEquals(1, views);
    }

    @Test //getting num views with non-existent id
    public void testGetNumViews2() {
        int views = database.getNumViews(99);
        assertEquals(-1, views);
    }

    @Test //general case updating the number of views
    public void testUpdateNumViews1() {
        database.addRow(3, "Scroll of Insights", "Author C", "2024-01-01 00:00", "scroll_insights.pdf");
        boolean result = database.updateNumViews(3);
        assertTrue(result);
        assertEquals(1, database.getNumViews(3));
    }

    @Test //updating views for a non-existent ID
    public void testUpdateNumViews2() {
        boolean result = database.updateNumViews(99);
        assertFalse(result);
    }

    @Test //general case retrieving file path by ID
    public void testGetFileById1() {
        database.addRow(4, "Scroll of Secrets", "Author D", "2024-01-01 00:00", "scroll_secrets.pdf");
        String filePath = database.getFileById(4);
        assertEquals("scroll_secrets.pdf", filePath);
    }

    @Test //getting file with non-existent id
    public void testGetFileById2() {
        String filePath = database.getFileById(99);
        assertNull(filePath);
    }

    @Test //test convertion datetime
    public void testConvertToDatetime1() {
        String datetime = database.convertToDatetime(1, 1, 2024, 12, 0);
        assertEquals("2024-01-01", datetime);
    }

    @Test //testing convertion to datetime with invalid parameters 
    public void testConvertToDatetime2() {
        assertNull(database.convertToDatetime(0, 0, 0, 0, 0)); 
        assertNull(database.convertToDatetime(-1, -1, -1, -1, -1));
        assertNull(database.convertToDatetime(12, 6, 2024, -1, 0)); //invalid hour (negative)
        assertNull(database.convertToDatetime(12, 6, 2024, 12, -1)); // invalid minute (negative)
        assertNull(database.convertToDatetime(30, 2, 2024, 12, 0)); //invalid day for feb
        assertNotNull(database.convertToDatetime(29, 2, 2024, 12, 0)); //feb leap year
        assertNull(database.convertToDatetime(32, 1, 2024, 12, 0)); //invalid day for jan
        assertNull(database.convertToDatetime(31, 4, 2024, 12, 0)); // invalid day for april
        assertNull(database.convertToDatetime(0, 4, 2024, 12, 0)); //invalid day
        assertNull(database.convertToDatetime(12, 13, 2024, 12, 0)); // invalid month
        assertNull(database.convertToDatetime(12, 0, 2024, 12, 0)); //invalid month
        assertNull(database.convertToDatetime(12, 6, 2024, 25, 0)); //invalid hour
        assertNull(database.convertToDatetime(12, 6, 2024, 24, 0)); //invalid hour
        assertNull(database.convertToDatetime(12, 6, 0, 12, 60)); //invalid minute
        assertNull(database.convertToDatetime(12, 6, 2024, 12, 70)); // invalid minute
    }
}