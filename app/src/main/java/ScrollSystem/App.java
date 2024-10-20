package ScrollSystem;

import ScrollSystem.FileHandlers.*;
import ScrollSystem.UserInterface.*;
import ScrollSystem.Users.User;
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
    UserProfile userProfile;
    UploadScroll uploadScroll;
    PreviewScreen previewScreen;
    ParsingScreen parsingScreen;
    AdminProfile adminProfile;
    ViewUsersDetails viewUsersDetails;
    DeleteScreen deleteScreen;

    ScrollDatabase scrollDatabase;

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
        // Set the name of the window
        surface.setTitle("Virtual Scroll Access System");
        // load the logo
        logo = loadImage("src/main/resources/logo.png");
        logo.resize(1920/10, 1080/10);

        // Initialize the ScrollDatabase
        scrollDatabase = new ScrollDatabase(DATABASE_PATH);

        deleteScreen = new DeleteScreen(this);  
        loginScreen = new LoginScreen(this, scrollDatabase, uploadScroll, deleteScreen);
        viewScrollsGuest = new ViewScrollsGuest(this);
        viewScrollsUsers = new ViewScrollsUsers(this, loginScreen, uploadScroll, deleteScreen);
        viewScrollsAdmin = new ViewScrollsAdmin(this, loginScreen, scrollDatabase);
        filterScreen = new FilterScreen(this, viewScrollsGuest);
        previewScreen = new PreviewScreen(this, viewScrollsUsers);
        // userProfile = new UserProfile(this, viewScrollsUsers);
        uploadScroll = new UploadScroll(this, userProfile);
        parsingScreen = new ParsingScreen(this, previewScreen);
        adminProfile = new AdminProfile(this, viewScrollsAdmin);
        viewUsersDetails = new ViewUsersDetails(this, viewScrollsAdmin, adminProfile);
        userProfile = new UserProfile(this, viewScrollsUsers, deleteScreen); 
        
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

        if (loginScreen.registerScreen.isRegisterScreenVisible) {
            loginScreen.registerScreen.drawRegister();
        }

        if (loginScreen.isLoginScreenVisible) {
            loginScreen.drawLogin();
        } else if (loginScreen.isViewScrollsUserVisible) {
            viewScrollsUsers.drawScrollsUsers();
        } else if (loginScreen.isViewScrollsAdminVisible) {
            viewScrollsAdmin.drawScrollsAdmin();
        } else if (loginScreen.isViewScrollsGuestVisible) {
            viewScrollsGuest.drawScrollsGuest();
        }

        if (viewScrollsGuest.filterScreen.isFilterScreenVisible) {
            viewScrollsGuest.filterScreen.drawFilter();
        }else if (viewScrollsUsers.filterScreen.isFilterScreenVisible) {
            viewScrollsUsers.filterScreen.drawFilter();
        } else if (viewScrollsAdmin.filterScreen.isFilterScreenVisible) {
            viewScrollsAdmin.filterScreen.drawFilter();
        }

        if (viewScrollsUsers.previewScreen.isPreviewScreenVisible) {
            viewScrollsUsers.previewScreen.drawPreview();
        } else if (viewScrollsAdmin.previewScreen.isPreviewScreenVisible) {
            viewScrollsAdmin.previewScreen.drawPreview();
        }

        if (viewScrollsUsers.previewScreen.parsingScreen.isParsingScreenVisible) {
            viewScrollsUsers.previewScreen.parsingScreen.drawParsing();
        } else if (viewScrollsAdmin.previewScreen.parsingScreen.isParsingScreenVisible) {
            viewScrollsAdmin.previewScreen.parsingScreen.drawParsing();
        }

        if (viewScrollsAdmin.statsScreen.isStatsScreenVisible) {
            viewScrollsAdmin.statsScreen.drawStats();
        }

        if (viewScrollsUsers.userProfile.isUserProfileVisible) {
            viewScrollsUsers.userProfile.drawUserProfile();
        } else if (viewScrollsAdmin.adminProfile.isAdminProfileVisible) {
            viewScrollsAdmin.adminProfile.drawUserProfile();
        }

        if (viewScrollsAdmin.adminProfile.viewUsersDetails.isViewUsersDetailsVisible) {
            viewScrollsAdmin.adminProfile.viewUsersDetails.drawViewUserDetails();
        }

        if (viewScrollsAdmin.adminProfile.addUserScreen.isAddUserScreenVisible) {
            viewScrollsAdmin.adminProfile.addUserScreen.drawAddUser();
        }

        if (viewScrollsUsers.userProfile.editUserScreen.isEditProfileScreenVisible) {
            viewScrollsUsers.userProfile.editUserScreen.drawEditProfile();
        }

        if (viewScrollsUsers.userProfile.uploadScroll.isUploadScreenVisible) {
            viewScrollsUsers.userProfile.uploadScroll.drawUploadScroll();
        }

        if (deleteScreen.isDeleteScreenVisible) {
            deleteScreen.drawDelete();  
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

        if (loginScreen.isViewScrollsGuestVisible) {
            viewScrollsGuest.mousePressed();
        }

        if (viewScrollsGuest.filterScreen.isFilterScreenVisible) {
            viewScrollsGuest.filterScreen.mousePressed();
        } else if (viewScrollsUsers.filterScreen.isFilterScreenVisible) {
            viewScrollsUsers.filterScreen.mousePressed();
        } else if (viewScrollsAdmin.filterScreen.isFilterScreenVisible) {
            viewScrollsAdmin.filterScreen.mousePressed();
        }

        if (loginScreen.isViewScrollsUserVisible) {
            viewScrollsUsers.mousePressed();
        }

        if (loginScreen.isViewScrollsAdminVisible) {
            viewScrollsAdmin.mousePressed();
        }

        if (viewScrollsUsers.previewScreen.isPreviewScreenVisible) {
            viewScrollsUsers.previewScreen.mousePressed();
        }

        if (viewScrollsAdmin.previewScreen.isPreviewScreenVisible) {
            viewScrollsAdmin.previewScreen.mousePressed();
        }

        if (viewScrollsAdmin.statsScreen.isStatsScreenVisible) {
            viewScrollsAdmin.statsScreen.mousePressed();
        }

        if (viewScrollsAdmin.adminProfile.isAdminProfileVisible) {
            viewScrollsAdmin.adminProfile.mousePressed();
        }

        if (viewScrollsAdmin.adminProfile.addUserScreen.isAddUserScreenVisible) {
            viewScrollsAdmin.adminProfile.addUserScreen.mousePressed();
        }

        if (viewScrollsAdmin.adminProfile.viewUsersDetails.isViewUsersDetailsVisible) {
            viewScrollsAdmin.adminProfile.viewUsersDetails.mousePressed();
        }

        if (viewScrollsUsers.userProfile.isUserProfileVisible) {
            viewScrollsUsers.userProfile.mousePressed();
        }

        if (viewScrollsUsers.userProfile.uploadScroll.isUploadScreenVisible) {
            viewScrollsUsers.userProfile.uploadScroll.mousePressed();
        }

        if (viewScrollsUsers.userProfile.editUserScreen.isEditProfileScreenVisible) {
            viewScrollsUsers.userProfile.editUserScreen.mousePressed();
        }

        if (viewScrollsUsers.previewScreen.parsingScreen.isParsingScreenVisible) {
            viewScrollsUsers.previewScreen.parsingScreen.mousePressed();
        }

        if (deleteScreen.isDeleteScreenVisible) {
            deleteScreen.mousePressed();  
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

        if (viewScrollsUsers.userProfile.editUserScreen.isEditProfileScreenVisible) {
            viewScrollsUsers.userProfile.editUserScreen.keyPressed();
        }

        if (viewScrollsAdmin.adminProfile.addUserScreen.isAddUserScreenVisible) {
            viewScrollsAdmin.adminProfile.addUserScreen.keyPressed();
        }

        if (viewScrollsUsers.previewScreen.parsingScreen.isParsingScreenVisible) {
            viewScrollsUsers.previewScreen.parsingScreen.keyPressed();
        }

        if (viewScrollsUsers.userProfile.uploadScroll.isUploadScreenVisible) {
            viewScrollsUsers.userProfile.uploadScroll.keyPressed();
        }

    }

    public void mouseWheel(processing.event.MouseEvent event) {
        if (viewScrollsUsers.previewScreen.isPreviewScreenVisible) {
            viewScrollsUsers.previewScreen.mouseWheel(event);
        }

        if (viewScrollsAdmin.previewScreen.isPreviewScreenVisible) {
            viewScrollsAdmin.previewScreen.mouseWheel(event);
        }
    }

    public static void main(String[] args) {
        final String DATABASE_PATH = "src/main/java/ScrollSystem/Databases/database.db";

        //initialise database tables
        ScrollDatabase scrollDatabase = new ScrollDatabase(DATABASE_PATH);
        LoginDatabase loginDatabase = new LoginDatabase(DATABASE_PATH);
        UserScroll userScrollDatabase = new UserScroll(DATABASE_PATH);

        scrollDatabase.addRow(1, "Scroll of Wisdom", "rebo", "2024-01-01", "src/main/java/ScrollSystem/Scrolls/sample.bin");
        scrollDatabase.addRow(2, "Scroll of Law", "rebo", "2024-01-01", "src/main/java/ScrollSystem/Scrolls/numbered.bin");
        scrollDatabase.addRow(3, "croissant", "tebo", "2024-01-01", "src/main/java/ScrollSystem/Scrolls/croissant.bin");
        scrollDatabase.addRow(4, "bagel", "tebo", "2024-01-01", "src/main/java/ScrollSystem/Scrolls/bagel.bin");
        scrollDatabase.addRow(5, "pancakes", "tebo", "2024-01-01", "src/main/java/ScrollSystem/Scrolls/pancakes.bin");

        loginDatabase.addUser("tebo", "rawr", "te bo", "tebo@chillipeppers.com", "0412345678", false);
        loginDatabase.addUser("admin", "admin", "ad min", "admin@dinonuggets.com", "0487654321", true);

        userScrollDatabase.uploadScroll(1, 3);
        userScrollDatabase.uploadScroll(1, 4);
        userScrollDatabase.uploadScroll(1,5);

        PApplet.main("ScrollSystem.App");
    }

}
