package ScrollSystem;

import ScrollSystem.FileHandlers.*;
import ScrollSystem.UserInterface.LoginScreen;
import ScrollSystem.UserInterface.RegisterScreen;
import ScrollSystem.UserInterface.ViewScrollsGuest;
import processing.core.PApplet;
import processing.core.PImage;

public class App extends PApplet {
    LoginScreen loginScreen;
    ViewScrollsGuest viewScrollsGuest;

    // Canvas center
    int centerX = width/2;
    int centerY = height/2;

    // Rectangle properties
    float rectW = width-100;
    float rectH = height/2;
    float cornerRadius = 10;

    // Calculate position to center the rectangle
    float rectX = centerX - rectW / 2;
    float rectY = centerY - rectH / 2;

    int finalTab = (int)(rectX+3*rectW/4);

    // Shadow offset
    float shadowOffsetX = 10;
    float shadowOffsetY = 10;

    // Draw the shadow all around (slightly larger than the rectangle)
    float shadowOffset = 8;

    static int width = 1920/2;
    static int height = 1080/2;
    PImage logo;

    @Override
    public void setup() {
        // load the logo
        logo = loadImage("src/main/resources/logo.png");
        logo.resize(492/3, 187/3);

        loginScreen = new LoginScreen(this);
        viewScrollsGuest = new ViewScrollsGuest(this);
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
        image(logo, (width/4) * 3 + 25, 10);


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
    }

    @Override
    public void keyPressed() {
        loginScreen.keyPressed();
        loginScreen.registerScreen.keyPressed();
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

        PApplet.main("ScrollSystem.App");
    }

}
