package ScrollSystem.FileHandlers;

import static org.junit.Assert.*;
import org.junit.*;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.util.*;

public class FilterScrollTest {
    private FilterScroll filterScroll;
    private final String BIN_PATH = "src/test/java/ScrollSystem/resources/test.bin";

    @Before
    public void setUp() {
        //intitialise  
        filterScroll = new FilterScroll(BIN_PATH);
        MockitoAnnotations.openMocks(this);
    }

    @Test //file does not exist
    public void testInvalidInitialisation() {
        String invalidPath = "invalid_path_to_file.bin";
        FilterScroll invalidFilterScroll = new FilterScroll(invalidPath);
        assertEquals(0, invalidFilterScroll.countLines());
    }

    @Test //exception 
    public void testInitialise2() {
        FilterScroll filterScroll = mock(FilterScroll.class);
        doThrow(new RuntimeException("Exception")).when(filterScroll).getLine(anyInt());
        assertNull(filterScroll.previousLine());
    }


    @Test //general case 
    public void testGetLine1() {
        assertEquals("line 1", filterScroll.getLine(1));
        assertEquals("line 5", filterScroll.getLine(5));
    }

    @Test 
    public void testGetLine2() {
        //exceeding file line number 
        assertNull(filterScroll.getLine(100));

        //negative number 
        assertNull(filterScroll.getLine(-1));
    }

    @Test //general case 
    public void testNextLine1() {
        assertEquals("line 1", filterScroll.getLine(1));
        assertEquals("line 2", filterScroll.nextLine());
        assertEquals("line 3", filterScroll.nextLine());
        assertEquals("line 4", filterScroll.nextLine());
        assertEquals("line 5", filterScroll.nextLine());
    }

    @Test //EOF
    public void testNextLine2() {
        assertEquals("line 5", filterScroll.getLine(5));
        assertNull(filterScroll.nextLine()); //line does not exist 
    }

    @Test //general case
    public void testPreviousLine1() {
        assertEquals("line 1", filterScroll.getLine(1));
        assertEquals("line 2", filterScroll.nextLine());
        assertEquals("line 1", filterScroll.previousLine()); 
    }

    @Test //non existent
    public void testPreviousLine2() {
        //previous from first line 
        assertEquals("line 1", filterScroll.getLine(1));
        assertNull(filterScroll.previousLine()); 
        assertNull(filterScroll.previousLine());
    }

    @Test //general case 
    public void testGetAllLines() {
        String allLines = filterScroll.getAllLines();
        assertNotNull(allLines);
        assertTrue(allLines.contains("line 1"));
        assertTrue(allLines.contains("line 2"));
        assertTrue(allLines.contains("line 3"));
        assertTrue(allLines.contains("line 4"));
        assertTrue(allLines.contains("line 5"));
        assertFalse(allLines.contains("rah"));
    }

    @Test //general case
    public void testGetLinesBetweenValid() {
        String linesBetween = filterScroll.getLinesBetween(1, 3);
        assertNotNull(linesBetween);
        assertTrue(linesBetween.contains("line 1"));
        assertTrue(linesBetween.contains("line 2"));
        assertTrue(linesBetween.contains("line 3"));
    }

    @Test // invalid parameters
    public void testGetLinesBetweenInvalid() {
        assertNull(filterScroll.getLinesBetween(-1, 3)); 
        assertNull(filterScroll.getLinesBetween(-1, -3)); 
        assertNull(filterScroll.getLinesBetween(1, -3)); 
        assertNull(filterScroll.getLinesBetween(1, 100));
        assertNull(filterScroll.getLinesBetween(100, 101));
        assertNull(filterScroll.getLinesBetween(3, 1)); 
    }

    @Test 
    public void testCountLines() {
        assertEquals(5, filterScroll.countLines()); 
    }

    @After
    public void end() {
        filterScroll.close();
    }
}
