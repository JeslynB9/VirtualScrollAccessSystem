package ScrollSystem.Users;

import static org.junit.Assert.*;

import ScrollSystem.FileHandlers.*;

import org.junit.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class UserTest {
    private User user;
    private final String TEST_DB_PATH = "src/test/java/ScrollSystem/resources/testDatabase.db";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    @Before
    public void setUp() {
        user = new User();
        System.setOut(new PrintStream(outContent));
        // Delete existing file first
        File databaseFile = new File(TEST_DB_PATH);
        if (databaseFile.exists()) {
            boolean deleted = databaseFile.delete();
            assertTrue("Test Setup Fail: can't delete existing database file", deleted);
        }

        // Initialize user with test database
        user = new User() {
            {
                loginDatabase = new LoginDatabase(TEST_DB_PATH);
                scrollDatabase = new ScrollDatabase(TEST_DB_PATH);
                UserScroll = new UserScroll(TEST_DB_PATH);
            }
        };
    }


    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testRegisterAndLogin() {
        assertTrue(user.register("testuser", "password", "Test User", "test@example.com", "1234567890", false));
        assertTrue(user.login("testuser", "password"));
        assertFalse(user.login("testuser", "wrongpassword"));
    }

    @Test
    public void testGetUserInfo() {
        user.register("infouser", "password", "Info User", "info@example.com", "0987654321", false);
        user.login("infouser", "password");
        Map<String, String> userInfo = user.getUserInfo();
        assertEquals("infouser", userInfo.get("username"));
        assertEquals("Info User", userInfo.get("fullName"));
        assertEquals("info@example.com", userInfo.get("email"));
        assertEquals("0987654321", userInfo.get("phoneNo"));
    }

    @Test
    public void testUpdateUserInfo() {
        user.register("updateuser", "password", "Update User", "update@example.com", "1122334455", false);
        user.login("updateuser", "password");
        assertTrue(user.updateUserInfo("updateuser", "newusername", "newpassword", "New Name", "new@example.com", "5544332211"));
        assertFalse(user.login("updateuser", "password"));
        assertTrue(user.login("newusername", "newpassword"));
    }

    @Test
    public void testUploadAndGetScroll() {
        user.register("scrolluser", "password", "Scroll User", "scroll@example.com", "9876543210", false);
        user.login("scrolluser", "password");
        assertTrue(user.uploadScroll(1, "Test Scroll", "Test Author", "2023-06-15", "path/to/testscroll.txt"));
        Map<String, String> scroll = user.getScrollById(1);
        assertNotNull(scroll);
        assertEquals("Test Scroll", scroll.get("name"));
        assertEquals("Test Author", scroll.get("author"));
    }

    @Test
    public void testViewAllScrolls() {
        user.register("allscrollsuser", "password", "All Scrolls User", "allscrolls@example.com", "1234567890", false);
        user.login("allscrollsuser", "password");
        user.uploadScroll(1, "Scroll One", "Author One", "2023-06-15", "path/to/scroll1.txt");
        user.uploadScroll(2, "Scroll Two", "Author Two", "2023-06-16", "path/to/scroll2.txt");
        List<Map<String, String>> allScrolls = user.viewAllScrolls();
        assertEquals(2, allScrolls.size());
    }

    @Test
    public void testSearchScrollsByName() {
        user.register("searchuser", "password", "Search User", "search@example.com", "9876543210", false);
        user.login("searchuser", "password");
        user.uploadScroll(1, "Ancient Scroll", "Old Author", "2023-06-15", "path/to/ancient.txt");
        user.uploadScroll(2, "Modern Scroll", "New Author", "2023-06-16", "path/to/modern.txt");
        List<Map<String, String>> searchResults = user.searchScrollsByName("Ancient");
        assertEquals(1, searchResults.size());
        assertEquals("Ancient Scroll", searchResults.get(0).get("name"));
    }


    @Test
    public void testDownloadScroll() {
        // Get the project root directory by resolving relative to test class location
        Path projectRoot = Paths.get("")
                .toAbsolutePath()
                .normalize();

        // Create the test resources path relative to project root
        Path testResourcePath = projectRoot
                .resolve("src")
                .resolve("test")
                .resolve("java")
                .resolve("ScrollSystem")
                .resolve("resources")
                .resolve("test.bin");

        // Only create directories if they don't exist, don't modify test.bin
        try {
            Files.createDirectories(testResourcePath.getParent());
            // Remove the file creation part since we want to use existing test.bin
        } catch (IOException e) {
            fail("Could not create directories: " + e.getMessage());
        }

        // Verify test.bin exists before proceeding
        if (!Files.exists(testResourcePath)) {
            fail("test.bin must exist at: " + testResourcePath);
        }

        boolean registered = user.register("downloaduser", "password", "Download User",
                "download@example.com", "1234567890", false);
        boolean loggedIn = user.login("downloaduser", "password");
        boolean uploaded = user.uploadScroll(1, "Downloadable Scroll", "Download Author",
                "2023-06-15", testResourcePath.toString());
        Map<String, String> scroll = user.getScrollById(1);
        String downloadedPath = user.downloadScroll(1);

        assertNotNull("Downloaded path should not be null", downloadedPath);
        assertTrue("Registration should succeed", registered);
        assertTrue("Login should succeed", loggedIn);
        assertTrue("Upload should succeed", uploaded);
        assertTrue("Downloaded file should exist", new File(downloadedPath).exists());

        // Only clean up the downloaded file, leave test.bin in place
        try {
            Files.deleteIfExists(Paths.get(downloadedPath));
        } catch (IOException e) {
            System.err.println("Warning: Could not clean up downloaded file: " + e.getMessage());
        }
    }

    @Test
    public void testDownloadScrollWhenNotLoggedIn() {
        // First, log in and upload a scroll
        user.register("testuser", "password", "Test User", "test@example.com", "1234567890", false);
        user.login("testuser", "password");
        user.uploadScroll(1, "Test Scroll", "Test Author", "2023-06-15", "path/to/test/file.txt");

        // Now log out
        user.logout();

        // Attempt to download the scroll
        String downloadedPath = user.downloadScroll(1);

        // Assert that the download failed due to not being logged in
        assertNull("Download should fail when user is not logged in", downloadedPath);
    }

    @Test
    public void testDownloadNonExistentScroll() {
        // Register and log in a user
        user.register("testuser", "password", "Test User", "test@example.com", "1234567890", false);
        user.login("testuser", "password");

        // Redirect System.out to capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Attempt to download a non-existent scroll (assuming 999 is not a valid scroll ID)
        String downloadedPath = user.downloadScroll(999);

        // Restore original System.out
        System.setOut(originalOut);

        // Assert that the download failed due to scroll not being found
        assertNull("Download should fail for non-existent scroll", downloadedPath);

        // Check the console output
        String consoleOutput = outContent.toString().trim();
        assertEquals("Console output should indicate scroll not found",
                "Scroll not found", consoleOutput);
    }

    @Test
    public void testFailedScrollDownload() {
        // Register and log in a user
        user.register("testuser", "password", "Test User", "test@example.com", "1234567890", false);
        user.login("testuser", "password");

        // Upload a scroll with a non-existent file path
        String nonExistentFilePath = "non_existent_file.txt";
        boolean uploaded = user.uploadScroll(1, "Test Scroll", "Test Author", "2023-06-15", nonExistentFilePath);
        System.out.println("Scroll uploaded: " + uploaded);

        // Redirect System.out to capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Attempt to download the scroll
        String downloadedPath = user.downloadScroll(1);

        // Restore original System.out
        System.setOut(originalOut);

        // Assert that the download failed
        assertNull("Download should fail when file doesn't exist", downloadedPath);

        // Check the console output
        String consoleOutput = outContent.toString().trim();
        assertTrue("Console output should indicate failed download",
                consoleOutput.contains("Failed to download scroll"));

        // Print captured output for debugging
        System.out.println("Captured output: " + consoleOutput);
    }

    @Test
    public void testSearchScrollsByAuthor() {
        user.register("authoruser", "password", "Author User", "author@example.com", "1234567890", false);
        user.login("authoruser", "password");
        user.uploadScroll(1, "Scroll One", "Author A", "2023-06-15", "path/to/scroll1.txt");
        user.uploadScroll(2, "Scroll Two", "Author B", "2023-06-16", "path/to/scroll2.txt");
        user.uploadScroll(3, "Scroll Three", "Author A", "2023-06-17", "path/to/scroll3.txt");

        List<Map<String, String>> searchResults = user.searchScrollsByAuthor("Author A");
        assertEquals(2, searchResults.size());
        assertTrue(searchResults.stream().allMatch(scroll -> "Author A".equals(scroll.get("author"))));
    }


    @Test
    public void testSearchScrollsByUploadDate() {
        user.register("dateuser", "password", "Date User", "date@example.com", "1234567890", false);
        user.login("dateuser", "password");
        user.uploadScroll(1, "Scroll One", "Author A", "2023-06-15", "path/to/scroll1.txt");
        user.uploadScroll(2, "Scroll Two", "Author B", "2023-06-16", "path/to/scroll2.txt");

        List<Map<String, String>> searchResults = user.searchScrollsByUploadDate("2023-06-15");
        assertEquals(1, searchResults.size());
        assertEquals("Scroll One", searchResults.get(0).get("name"));
    }


    @Test
    public void testSearchScrollsBetweenDates() {
        user.register("daterangeuser", "password", "Date Range User", "daterange@example.com", "1234567890", false);
        user.login("daterangeuser", "password");
        user.uploadScroll(1, "Scroll One", "Author A", "2023-06-15", "path/to/scroll1.txt");
        user.uploadScroll(2, "Scroll Two", "Author B", "2023-06-16", "path/to/scroll2.txt");
        user.uploadScroll(3, "Scroll Three", "Author C", "2023-06-17", "path/to/scroll3.txt");

        List<Map<String, String>> searchResults = user.searchScrollsBetweenDates("2023-06-15", "2023-06-16");
        assertEquals(2, searchResults.size());
    }



    @Test
    public void testUploadScrollComprehensive() {
        user.register("uploaduser", "password", "Upload User", "upload@example.com", "1234567890", false);
        user.login("uploaduser", "password");

        assertTrue(user.uploadScroll(1, "Test Scroll", "Test Author", "2023-06-15", "path/to/testscroll.txt"));

        // Verify the scroll was added correctly
        Map<String, String> scroll = user.getScrollById(1);
        assertNotNull(scroll);
        assertEquals("Test Scroll", scroll.get("name"));
        assertEquals("Test Author", scroll.get("author"));
        assertEquals("2023-06-15", scroll.get("publishDate"));
        assertEquals("path/to/testscroll.txt", scroll.get("filePath"));

        // Try to upload a scroll with the same ID (should fail)
        assertFalse(user.uploadScroll(1, "Duplicate Scroll", "Another Author", "2023-06-16", "path/to/duplicate.txt"));

        // Verify the original scroll is still there and wasn't overwritten
        scroll = user.getScrollById(1);
        assertEquals("Test Scroll", scroll.get("name"));
    }

    @Test
    public void testUsernameGetterAndSetter() {
        // Create a new User object
        User user = new User();

        // Test initial value (seems to be "infouser" based on the error message)
        assertEquals("Initial username should be 'infouser'", "infouser", user.getUsername());

        // Set a new username
        String testUsername = "testUser123";
        user.setUsername(testUsername);

        // Test if the getter returns the set username
        assertEquals("Username getter should return the set username", testUsername, user.getUsername());

        // Test setting a different username
        String newUsername = "newTestUser456";
        user.setUsername(newUsername);

        // Test if the getter returns the new username
        assertEquals("Username getter should return the new username", newUsername, user.getUsername());

        // Test setting username to null (if your implementation allows it)
        user.setUsername(null);
        assertNull("Username should be null after setting it to null", user.getUsername());
    }

    @Test
    public void testViewAllUsers() {
        // First, add some users to ensure there's data to print
        user.register("user1", "password1", "User One", "user1@example.com", "1234567890", false);
        user.register("user2", "password2", "User Two", "user2@example.com", "0987654321", false);

        // Call the method we're testing
        user.viewAllUsers();

        // Get the printed output
        String printedOutput = outContent.toString();

        // Assert that the output contains the expected information
        assertTrue("Output should contain user1", printedOutput.contains("user1"));
        assertTrue("Output should contain User One", printedOutput.contains("User One"));
        assertTrue("Output should contain user1@example.com", printedOutput.contains("user1@example.com"));
        assertTrue("Output should contain 1234567890", printedOutput.contains("1234567890"));

        assertTrue("Output should contain user2", printedOutput.contains("user2"));
        assertTrue("Output should contain User Two", printedOutput.contains("User Two"));
        assertTrue("Output should contain user2@example.com", printedOutput.contains("user2@example.com"));
        assertTrue("Output should contain 0987654321", printedOutput.contains("0987654321"));

        // You can add more specific assertions based on the exact format of your output
    }



    @Test
    public void testUpdateUserInfoWithNonExistentUser() {
        // Attempt to update a non-existent user
        String oldUsername = "nonexistentuser";
        String newUsername = "newusername";
        boolean result = user.updateUserInfo(oldUsername, newUsername, "newpassword", "New Name", "new@example.com", "1234567890");

        // Assert that the update failed
        assertFalse("Update should fail for non-existent user", result);

        // Get the printed output
        String printedOutput = outContent.toString().trim();

        // Assert that the correct error message was printed
        String expectedError = "Error: user with ID does not exist " + oldUsername + " | " + newUsername;
        assertTrue("Error message should be present for non-existent user",
                printedOutput.contains(expectedError));

        // Print the actual output for debugging
        System.out.println("Actual output:");
        System.out.println(printedOutput);
    }




    @Test
    public void testFailedUserRegistration() {
        // First, register a user successfully
        boolean success = user.register("existinguser", "password", "Existing User", "existing@example.com", "1234567890", false);
        assertTrue("First user should be registered successfully", success);

        // Clear the output stream
        outContent.reset();

        // Now try to register the same user again (which should fail)
        success = user.register("existinguser", "newpassword", "New User", "new@example.com", "0987654321", false);

        // Assert that the registration failed
        assertFalse("Registration should fail for duplicate username", success);

        // Get the printed output
        String printedOutput = outContent.toString().trim();

        // Assert that the correct error messages were printed
        assertTrue("Error message should contain username already exists message",
                printedOutput.contains("Fail to add user: username already exists"));
        assertTrue("Error message should contain user failed to register message",
                printedOutput.contains("User failed to register"));

        // Print the actual output for debugging
        System.out.println("Actual output:");
        System.out.println(printedOutput);
    }



    @Test
    public void testUploadScrollFailureWithMissingId() {
        Map<String, String> incompleteUserInfo = new HashMap<>();
        incompleteUserInfo.put("username", "testuser");
        // Note: we're not putting "id" in the map
        user.setUserInfoForTesting(incompleteUserInfo);

        boolean result = user.uploadScroll(1, "Test Scroll", "Test Author", "2023-06-15", "path/to/test/file.txt");

        assertFalse("Scroll upload should fail when userInfo doesn't contain id", result);

        String output = outContent.toString().trim();
        assertTrue("Output should contain user list",
                output.contains("Username             Password Hash                            Full Name                      Email                          Phone No        Admin"));
        assertTrue("Output should contain error message about failing to get user info",
                output.contains("Fail to get user info: username does not exist"));
    }



    @Test
    public void testUploadScrollFailureWithNullUserInfo() {
        user.setUserInfoForTesting(null);

        boolean result = user.uploadScroll(1, "Test Scroll", "Test Author", "2023-06-15", "path/to/test/file.txt");

        assertFalse("Scroll upload should fail when userInfo is null", result);

        String output = outContent.toString().trim();
        assertTrue("Output should contain user list",
                output.contains("Username             Password Hash                            Full Name                      Email                          Phone No        Admin"));
        assertTrue("Output should contain error message about failing to get user info",
                output.contains("Fail to get user info: username does not exist"));
        assertFalse("Output should not contain 'User info is null or doesn't contain id'",
                output.contains("User info is null or doesn't contain id"));
    }

//w3
}