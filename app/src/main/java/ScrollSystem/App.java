package ScrollSystem;

import ScrollSystem.FileHandlers.*;
import ScrollSystem.UserInterface.*;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App extends PApplet {
    final String DATABASE_PATH = "src/main/java/ScrollSystem/Databases/database.db";

    LoginScreen loginScreen;
    ViewScrollsGuest viewScrollsGuest;
    ViewScrollsUsers viewScrollsUsers;
    ViewScrollsAdmin viewScrollsAdmin;
    FilterScreen filterScreen;

//    // Canvas center
//    int centerX = width/2;
//    int centerY = height/2;
//
//    // Rectangle properties
//    float rectW = width-100;
//    float rectH = height/2;
//    float cornerRadius = 10;
//
//    // Calculate position to center the rectangle
//    float rectX = centerX - rectW / 2;
//    float rectY = centerY - rectH / 2;
//
//    int finalTab = (int)(rectX+3*rectW/4);
//
//    // Shadow offset
//    float shadowOffsetX = 10;
//    float shadowOffsetY = 10;
//
//    // Draw the shadow all around (slightly larger than the rectangle)
//    float shadowOffset = 8;

    static int width = 1920/2;
    static int height = 1080/2;
    PImage logo;

    @Override
    public void setup() {
        // load the logo
        logo = loadImage("src/main/resources/logo.png");
        logo.resize(1920/10, 1080/10);

        loginScreen = new LoginScreen(this);
        viewScrollsGuest = new ViewScrollsGuest(this);
        viewScrollsUsers = new ViewScrollsUsers(this, loginScreen);
        viewScrollsAdmin = new ViewScrollsAdmin(this, loginScreen);
        filterScreen = new FilterScreen(this, viewScrollsGuest);
    }

    public void settings() {
        // setting the size of the app
        size(width, height);
    }

    @Override
    public void draw() {
        // drawing the background colours - light purple
        fill(253,249,255);
        rect(0, height / 2, width, height / 2);

        // drawing the background colours - dark purple
        fill(77,16,92);
        rect(0, 0, width, height / 2);

        // drawing the logo
        image(logo, (width/4) * 3 + 25, -10);
        
        if (loginScreen.isUserGuest) {
            viewScrollsGuest.drawScrollsGuest();
        }

        if (loginScreen.registerScreen.isRegisterScreenVisible) {
            loginScreen.registerScreen.drawRegister();
        }

        if (loginScreen.isLoginScreenVisible) {
            loginScreen.drawLogin();
        }

        if (viewScrollsGuest.filterScreen.isFilterScreenVisible) {
            viewScrollsGuest.filterScreen.drawFilter();
        }

        if (loginScreen.isViewScrollsUserVisible) {
           viewScrollsUsers.drawScrollsUsers();
        }

        if (loginScreen.isViewScrollsAdminVisible) {
            viewScrollsAdmin.drawScrollsAdmin();
        }

        if (viewScrollsUsers.filterScreen.isFilterScreenVisible) {
            viewScrollsUsers.filterScreen.drawFilter();
        }

        if (viewScrollsUsers.previewScreen.isPreviewScreenVisible) {
            viewScrollsUsers.previewScreen.drawPreview();
        }

        if (viewScrollsAdmin.filterScreen.isFilterScreenVisible) {
            viewScrollsAdmin.filterScreen.drawFilter();
        }

        if (viewScrollsAdmin.previewScreen.isPreviewScreenVisible) {
            viewScrollsAdmin.previewScreen.drawPreview();
        }

        if (viewScrollsAdmin.statsScreen.isStatsScreenVisible) {
            viewScrollsAdmin.statsScreen.drawStats();
        }

    }

    private boolean isMouseOverButton(int x, int y, int w, int h) {
        return (mouseX > x && mouseX < x + w &&
                mouseY > y && mouseY < y + h);
    }

    @Override
    public void mousePressed() {

        if (loginScreen.isLoginScreenVisible) {
            loginScreen.mousePressed();
        }

        if (loginScreen.registerScreen.isRegisterScreenVisible) {
            loginScreen.registerScreen.mousePressed();
        }

        if (loginScreen.isUserGuest) {
            viewScrollsGuest.mousePressed();
        }

        if (viewScrollsGuest.filterScreen.isFilterScreenVisible) {
            viewScrollsGuest.filterScreen.mousePressed();
        }

        if (loginScreen.isViewScrollsUserVisible) {
            viewScrollsUsers.mousePressed();
        }

        if (loginScreen.isViewScrollsAdminVisible) {
            viewScrollsAdmin.mousePressed();
        }

        if (viewScrollsUsers.filterScreen.isFilterScreenVisible) {
            viewScrollsUsers.filterScreen.mousePressed();
        }

        if (viewScrollsUsers.previewScreen.isPreviewScreenVisible) {
            viewScrollsUsers.previewScreen.mousePressed();
        }

        if (viewScrollsAdmin.filterScreen.isFilterScreenVisible) {
            viewScrollsAdmin.filterScreen.mousePressed();
        }

        if (viewScrollsAdmin.previewScreen.isPreviewScreenVisible) {
            viewScrollsAdmin.previewScreen.mousePressed();
        }

        if (viewScrollsAdmin.statsScreen.isStatsScreenVisible) {
            viewScrollsAdmin.statsScreen.mousePressed();
        }
    }

    @Override
    public void keyPressed() {
        if (loginScreen.isLoginScreenVisible) {
            loginScreen.keyPressed();
        }
        if (loginScreen.registerScreen.isRegisterScreenVisible) {
            loginScreen.registerScreen.keyPressed();
        }
        viewScrollsGuest.filterScreen.keyPressed();
        viewScrollsUsers.filterScreen.keyPressed();
        viewScrollsAdmin.filterScreen.keyPressed();
    }

    public static void main(String[] args) {
        final String DATABASE_PATH = "src/main/java/ScrollSystem/Databases/database.db";

        //initialise database tables
        ScrollDatabase scrollDatabase = new ScrollDatabase(DATABASE_PATH);
        LoginDatabase loginDatabase = new LoginDatabase(DATABASE_PATH);
        UserScroll userScrollDatabase = new UserScroll(DATABASE_PATH);

        scrollDatabase.addRow(1, "Scroll of Wisdom", "Author A", "2024-01-01 00:00", "scroll_wisdom.pdf"); 
        scrollDatabase.addRow(2, "Scroll of Law", "Author A", "2024-01-01 00:00", "scroll_wisdom.pdf");

        loginDatabase.addUser("tebo", "rawr", "te bo", "tebo@chillipeppers.com", "0412345678", false);
        loginDatabase.addUser("admin", "admin", "ad min", "admin@dinonuggets.com", "0487654321", true);
        
        PApplet.main("ScrollSystem.App");
    }
    
}
