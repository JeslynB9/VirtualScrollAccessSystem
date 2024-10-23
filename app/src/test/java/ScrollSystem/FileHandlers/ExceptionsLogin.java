package ScrollSystem.FileHandlers;

import org.junit.*;
import org.mockito.*;
import java.sql.*;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ExceptionsLogin {
    @Mock
    private Connection mockConnection;
    
    @Mock
    private PreparedStatement mockPreparedStatement;
    
    @Mock
    private ResultSet mockResultSet;

    private LoginDatabase loginDatabase;

    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);
        loginDatabase = Mockito.spy(new LoginDatabase("test.db"));

        doReturn(mockConnection).when(loginDatabase).getConnection();
    }

    @Test
    public void testAddUserSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        boolean result = loginDatabase.addUser("username", "pass", "fullName", "email", "0123456789", false);
        assertFalse(result);
    }

    @Test
    public void testEditUserSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        boolean result = loginDatabase.editUser(1, "username", "pass", "fullName", "email", "0123456789");
        assertFalse(result);
    }

    @Test
    public void testUpdateScrollsAuthorSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        boolean result = loginDatabase.updateScrollsAuthor("oldUsername", "newUsername");
        assertFalse(result);
    }

    @Test
    public void testDeleteUserByUsernameSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        boolean result = loginDatabase.deleteUserByUsername("username");
        assertFalse(result);
    }

    @Test
    public void testGetUserInfoSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        Map<String, String> result = loginDatabase.getUserInfo("username");
        assertNull(result);
    }

    @Test
    public void testGetAllUsersSQLException() throws SQLException {
        when(mockConnection.createStatement()).thenThrow(new SQLException("Database error"));

        List<Map<String, String>> result = loginDatabase.getAllUsers();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCheckCredentialsSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        boolean result = loginDatabase.checkCredentials("username", "password");
        assertFalse(result);
    }

    @Test
    public void testGetUserIdByUsernameSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        int result = loginDatabase.getUserIdByUsername("username");
        assertEquals(-1, result);
    }

    @Test
    public void testGetUserByIdSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        String result = loginDatabase.getUserById(1);
        assertNull(result);
    }
}
