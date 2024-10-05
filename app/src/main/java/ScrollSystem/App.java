package ScrollSystem;

import ScrollSystem.FileHandlers.*;

import java.io.*;
import java.util.*;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        //initialise databases
        String scrollFilepath = "src/main/java/ScrollSystem/resources/FileHandlers/scrollDatabase.db";
        ScrollDatabase scrollDatabase = new ScrollDatabase(scrollFilepath);

        String loginFilepath = "src/main/java/ScrollSystem/resources/FileHandlers/loginDatabase.db";
        LoginDatabase loginDatabase = new LoginDatabase(loginFilepath);

        scrollDatabase.addRow(1, "Scroll of Wisdom", "Author A", "2024-01-01 00:00", "scroll_wisdom.pdf"); 
        scrollDatabase.addRow(2, "Scroll of Law", "Author A", "2024-01-01 00:00", "scroll_wisdom.pdf");        
    
        loginDatabase.addUser("tebo", "rawr", "te bo", "tebo@chillipeppers.com", "0412345678");
        loginDatabase.addUser("tebo", "password", "te bo", "tebo@dinonuggets.com", "0412345678");

    }

}
