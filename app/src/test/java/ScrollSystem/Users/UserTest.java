package ScrollSystem.Users;

import ScrollSystem.FileHandlers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserTest {

    private User user;

    @Mock
    private LoginDatabase loginDatabase;

    @Mock
    private ScrollDatabase scrollDatabase;

    @Mock
    private UserScroll userScroll;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.loginDatabase = loginDatabase;
        user.scrollDatabase = scrollDatabase;
        user.UserScroll = userScroll;
    }

    @Test
    void testLogin_Successful() {
        when(loginDatabase.checkCredentials("testuser", "password")).thenReturn(true);
        assertTrue(user.login("testuser", "password"));
        assertEquals("testuser", User.username);
    }

    @Test
    void testLogin_Failed() {
        when(loginDatabase.checkCredentials("testuser", "wrongpassword")).thenReturn(false);
        assertFalse(user.login("testuser", "wrongpassword"));
    }

    @Test
    void testRegister_Successful() {
        when(loginDatabase.addUser("newuser", "password", "New User", "new@user.com", "1234567890", false)).thenReturn(true);
        assertTrue(user.register("newuser", "password", "New User", "new@user.com", "1234567890", false));
    }

    @Test
    void testRegister_Failed() {
        when(loginDatabase.addUser("existinguser", "password", "Existing User", "existing@user.com", "0987654321", false)).thenReturn(false);
        assertFalse(user.register("existinguser", "password", "Existing User", "existing@user.com", "0987654321", false));
    }

    @Test
    void testGetUserInfo() {
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("username", "testuser");
        userInfo.put("email", "test@user.com");
        when(loginDatabase.getUserInfo("testuser")).thenReturn(userInfo);
        User.username = "testuser";
        assertEquals(userInfo, user.getUserInfo());
    }

    @Test
    void testUpdateUserInfo_Successful() {
        when(loginDatabase.getUserIdByUsername("olduser")).thenReturn(1);
        when(loginDatabase.editUser(1, "newuser", "newpass", "New Name", "new@email.com", "1234567890")).thenReturn(true);
        assertTrue(user.updateUserInfo("olduser", "newuser", "newpass", "New Name", "new@email.com", "1234567890"));
    }

    @Test
    void testUpdateUserInfo_Failed() {
        when(loginDatabase.getUserIdByUsername("nonexistentuser")).thenReturn(-1);
        assertFalse(user.updateUserInfo("nonexistentuser", "newuser", "newpass", "New Name", "new@email.com", "1234567890"));
    }

    @Test
    void testGetScrollById() {
        Map<String, String> scrollInfo = new HashMap<>();
        scrollInfo.put("id", "1");
        scrollInfo.put("name", "Test Scroll");
        when(scrollDatabase.getRowById(1)).thenReturn(scrollInfo);
        assertEquals(scrollInfo, user.getScrollById(1));
    }

    @Test
    void testViewAllScrolls() {
        List<Map<String, String>> allScrolls = new ArrayList<>();
        allScrolls.add(Collections.singletonMap("name", "Scroll 1"));
        allScrolls.add(Collections.singletonMap("name", "Scroll 2"));
        when(scrollDatabase.getAllScrolls()).thenReturn(allScrolls);
        assertEquals(allScrolls, user.viewAllScrolls());
    }

    @Test
    void testSearchScrollsByName() {
        List<Map<String, String>> searchResults = new ArrayList<>();
        searchResults.add(Collections.singletonMap("name", "Test Scroll"));
        when(scrollDatabase.getRowByName("Test")).thenReturn(searchResults);
        assertEquals(searchResults, user.searchScrollsByName("Test"));
    }

    @Test
    void testUploadScroll_Successful() {
        when(scrollDatabase.addRow(1, "New Scroll", "Author", "2023-01-01", "path/to/file")).thenReturn(true);
        when(loginDatabase.getUserInfo("testuser")).thenReturn(Collections.singletonMap("id", "1"));
        when(userScroll.uploadScroll(1, 1)).thenReturn(true);
        User.username = "testuser";
        assertTrue(user.uploadScroll(1, "New Scroll", "Author", "2023-01-01", "path/to/file"));
    }

    @Test
    void testDownloadScroll_Successful() {
        User.username = "testuser";
        when(scrollDatabase.getFileById(1)).thenReturn("testfile.bin");
        when(new FileDownload().downloadFile("testfile.bin")).thenReturn("/path/to/downloaded/testfile.bin");
        assertEquals("/path/to/downloaded/testfile.bin", user.downloadScroll(1));
        verify(scrollDatabase).updateNumDownloads(1);
    }

    @Test
    void testDownloadScroll_Failed() {
        User.username = "testuser";
        when(scrollDatabase.getFileById(1)).thenReturn(null);
        assertNull(user.downloadScroll(1));
    }
}