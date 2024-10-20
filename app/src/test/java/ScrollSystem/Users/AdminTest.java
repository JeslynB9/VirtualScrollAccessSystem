package ScrollSystem.Users;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import ScrollSystem.FileHandlers.LoginDatabase;

import java.util.*;

public class AdminTest {

    private Admin admin;
    private LoginDatabase mockLoginDatabase;

    @Before
    public void setUp() {
        admin = new Admin();
        mockLoginDatabase = mock(LoginDatabase.class);
        admin.loginDatabase = mockLoginDatabase;
    }

    @Test
    public void testLoginAsAdmin() {
        when(mockLoginDatabase.checkCredentials("adminUser", "adminPass")).thenReturn(true);

        Map<String, String> adminInfo = new HashMap<>();
        adminInfo.put("userType", "admin");
        when(mockLoginDatabase.getUserInfo("adminUser")).thenReturn(adminInfo);

        assertTrue(admin.login("adminUser", "adminPass"));
    }

    @Test
    public void testLoginAsNonAdmin() {
        when(mockLoginDatabase.checkCredentials("regularUser", "userPass")).thenReturn(true);

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("userType", "user");
        when(mockLoginDatabase.getUserInfo("regularUser")).thenReturn(userInfo);

        assertFalse(admin.login("regularUser", "userPass"));
    }

    @Test
    public void testLoginWithInvalidCredentials() {
        when(mockLoginDatabase.checkCredentials("invalidUser", "invalidPass")).thenReturn(false);

        assertFalse(admin.login("invalidUser", "invalidPass"));
    }

    @Test
    public void testGetAllUsers() {
        List<Map<String, String>> mockUsers = new ArrayList<>();
        mockUsers.add(Collections.singletonMap("username", "user1"));
        mockUsers.add(Collections.singletonMap("username", "user2"));

        when(mockLoginDatabase.getAllUsers()).thenReturn(mockUsers);

        List<Map<String, String>> result = admin.getAllUsers();
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).get("username"));
        assertEquals("user2", result.get(1).get("username"));
    }

    @Test
    public void testAddUser() {
        when(mockLoginDatabase.addUser("newUser", "password", "New User", "new@example.com", "1234567890", false))
                .thenReturn(true);

        assertTrue(admin.addUser("newUser", "password", "New User", "new@example.com", "1234567890", false));
    }

    @Test
    public void testAddUserFailure() {
        when(mockLoginDatabase.addUser("existingUser", "password", "Existing User", "existing@example.com", "0987654321", false))
                .thenReturn(false);

        assertFalse(admin.addUser("existingUser", "password", "Existing User", "existing@example.com", "0987654321", false));
    }

    @Test
    public void testDeleteUser() {
        when(mockLoginDatabase.deleteUserByUsername("userToDelete")).thenReturn(true);

        assertTrue(admin.deleteUser("userToDelete"));
    }

    @Test
    public void testDeleteUserFailure() {
        when(mockLoginDatabase.deleteUserByUsername("nonExistentUser")).thenReturn(false);

        assertFalse(admin.deleteUser("nonExistentUser"));
    }
}