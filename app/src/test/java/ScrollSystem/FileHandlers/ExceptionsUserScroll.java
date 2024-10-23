package ScrollSystem.FileHandlers;

import org.junit.*;
import org.mockito.*;
import java.sql.*;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ExceptionsUserScroll {
    @Mock
    Connection mockConnection;
    
    @Mock
    PreparedStatement mockPreparedStatement;
    
    @Mock
    ResultSet mockResultSet;

    private UserScroll userScroll;

    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);
        userScroll = Mockito.spy(new UserScroll("test.db"));

        doReturn(mockConnection).when(userScroll).getConnection();
    }

    @Test
    public void testUploadScrollSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        boolean result = userScroll.uploadScroll(1, 1);
        assertFalse(result);  
    }

    @Test
    public void testRemoveScrollSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        boolean result = userScroll.removeScroll(1);
        assertFalse(result); 
    }


    @Test
    public void testGetScrollIdByTitleSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        int result = userScroll.getScrollIdByTitle("NonExistentScroll");
        assertEquals(-1, result);
    }

    @Test
    public void testSearchScrollsByUsernameSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        List<HashMap<String, Object>> result = userScroll.searchScrollsByUsername("tebo");
        assertTrue(result.isEmpty()); 
    }

    @Test
    public void testSearchScrollsByUserIdSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        List<HashMap<String, Object>> result = userScroll.searchScrollsByUserId(1);
        assertTrue(result.isEmpty()); 
    }

    @Test
    public void testSearchScrollsByScrollIdSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        List<HashMap<String, Object>> result = userScroll.searchScrollsByScrollId(1);
        assertTrue(result.isEmpty());  
    }

    @Test
    public void testSearchScrollsByScrollNameSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        List<HashMap<String, Object>> result = userScroll.searchScrollsByScrollName("Scroll of Wisdom");
        assertTrue(result.isEmpty());  
    }
}
