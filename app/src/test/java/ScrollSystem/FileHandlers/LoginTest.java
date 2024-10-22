package ScrollSystem.FileHandlers;

import static org.junit.Assert.*;
import org.junit.*;

import java.io.*;
import java.util.*;


public class LoginTest {
    private LoginDatabase database;
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
        database = new LoginDatabase(DATABASE_PATH);
        database.initialiseDatabase();
    }

    @Test //general case 
    public void testAddUser1() {
        assertTrue(database.addUser("tebo", "rawr", "te bo", "tebo@chillipeppers.com", "0412345678", false));
        Map<String, String> user = database.getUserInfo("tebo");
        assertNotNull(user);
        assertEquals("tebo", user.get("username"));
    }

    @Test //invalid parameters, existing user 
    public void testAddUser2() {
        //invalid phone 
        assertFalse(database.addUser("rebo", "meow", "re bo", "rebo@dinonuggets.com", "0202", false));
        
        //add existing user 
        assertTrue(database.addUser("tebo", "rawr", "te bo", "tebo@chillipeppers.com", "0412345678", false));
        assertFalse(database.addUser("tebo", "rawr", "te bo", "tebo@chillipeppers.com", "0412345678", false));
    }

    @Test //adding existing user
    public void testAddUser3() { 
        assertTrue(database.addUser("tebo", "rawr", "te bo", "tebo@chillipeppers.com", "0412345678", false));
        assertFalse(database.addUser("tebo", "password", "te bo", "tebo@dinonuggets.com", "0412345678", false));
    }

    @Test //general case 
    public void testEditUser1() {
        database.addUser("yebo", "yaps", "ye bo", "yebo@pandas.com", "0412345678", false);
        assertTrue(database.editUser(database.getUserIdByUsername("yebo"), "yebo", "yappers", "ye bo", "yebo@donuts.com", "0412345678"));

        Map<String, String> user = database.getUserInfo("yebo");
        assertEquals("ye bo", user.get("fullName"));
        assertEquals("yebo@donuts.com", user.get("email"));
        assertEquals("0412345678", user.get("phoneNo"));
    }

    @Test //invalid number 
    public void testEditUser2() { 
        database.addUser("yebo", "yaps", "ye bo", "yebo@pandas.com", "0412345678", false);
        assertFalse(database.editUser(database.getUserIdByUsername("yebo"), "yebo", "yappers", "ye bo", "yebo@donuts.com", "12"));
        assertFalse(database.editUser(database.getUserIdByUsername("yebo"), "yebo", "yappers", "ye bo", "yebo@donuts.com", "ghfkgfh"));
    }

    @Test //all invalid cases
    public void testUserRow3() {
        database.addUser("yebo", "yaps", "ye bo", "yebo@pandas.com", "0412345678", false);
        assertFalse(database.editUser(1, null, null, null, null, null));
        assertFalse(database.editUser(1, "", "", "", "", ""));
        assertFalse(database.editUser(1, null, "", null, "", ""));
        assertTrue(database.editUser(1, "yebo", null, null, null, null));
        assertTrue(database.editUser(1, null, "yappers", null, null, null));
        assertTrue(database.editUser(1, null, null, "ye boo", null, null));
        assertTrue(database.editUser(1, null, null, null, "yebo@donuts.com", null));
        assertTrue(database.editUser(1, null, null, null, null, "1234567890"));
    }

    @Test //general case 
    public void testGetUserByUsername1() {
        database.addUser("mebo", "meep", "me bo", "mebo@map.com", "0412345678", false);
        Map<String, String> user = database.getUserInfo("mebo");
        assertNotNull(user);
        assertEquals("mebo@map.com", user.get("email"));
    }

    @Test //user doesnt exist  
    public void testGetUserByUsername2() {
        assertNull(database.getUserInfo("mebo"));
    }

    @Test //general case 
    public void testGetAllUsers1() {
        database.addUser("pebo", "peble", "pe bo", "pebo@pebbles.com", "0412345678", false);
        database.addUser("gebo", "geko", "ge ko", "gebo@gebble.com", "0498765432", false);
        
        List<Map<String, String>> users = database.getAllUsers();
        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(user -> user.get("username").equals("pebo")));
        assertTrue(users.stream().anyMatch(user -> user.get("username").equals("gebo")));
    }

    @Test //general case
    public void testDeleteUser1() {
        database.addUser("debo", "dap", "de bo", "debo@deto.com", "0412345678", false);
        assertTrue(database.deleteUserByUsername("debo"));
        assertNull(database.getUserInfo("debo"));
    }

    @Test //user doesnt exist 
    public void testDeleteUser2() {
        assertFalse(database.deleteUserByUsername("nebo"));
    }

    @Test //general case 
    public void testCheckCredentials1() {
        database.addUser("debo", "dap", "de bo", "debo@deto.com", "0412345678", false);
        assertTrue(database.checkCredentials("debo", "dap"));
        assertFalse(database.checkCredentials("debo", "dappers"));
    }

    @Test //user doesnt exist 
    public void testCheckCredentials2() {
        assertFalse(database.checkCredentials("rebo", "dappers"));
    }
}
