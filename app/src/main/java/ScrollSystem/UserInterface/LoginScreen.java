package ScrollSystem.UserInterface;

import ScrollSystem.FileHandlers.ScrollDatabase;
import ScrollSystem.UserInterface.RegisterScreen;
import ScrollSystem.Users.User;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.Map;

public class LoginScreen {
    PApplet parent;
    ViewScrollsGuest viewScrollsGuest;
    ViewScrollsUsers viewScrollsUsers;
    ViewScrollsAdmin viewScrollsAdmin;
    boolean loginFailed = false;
    private String loginErrorMessage = "";
    public boolean isLoginScreenVisible = true;
    public boolean isViewScrollsGuestVisible = false;
    public boolean isViewScrollsUserVisible = false;
    public boolean isViewScrollsAdminVisible = false;
    boolean usernameSelected = false;
    boolean passwordSelected = false;
    public RegisterScreen registerScreen;
    float shadowOffset = 8;
    User user; 

    String enteredUsername = "";
    String enteredPassword = "";

    public LoginScreen(PApplet parent, ScrollDatabase scrollDatabase, UploadScroll uploadScroll, DeleteScreen deleteScreen, EditScroll editScroll) {

        this.parent = parent;
        this.user = new User();
        registerScreen = new RegisterScreen(parent, this);
        viewScrollsGuest = new ViewScrollsGuest(parent);
        viewScrollsUsers = new ViewScrollsUsers(parent, this, uploadScroll, deleteScreen, editScroll);
        viewScrollsAdmin = new ViewScrollsAdmin(parent, this, scrollDatabase);
        System.out.println("Register initialized");
    }

    public void drawLogin() {

        if (!isLoginScreenVisible) return;


        // Background Overlay
        parent.fill(92,86,93);
        parent.rect(0, 0, parent.width*2, parent.height);

        // Shadow properties
        parent.fill(0, 0, 0, 50);
        parent.noStroke();
        parent.rect(parent.width / 2 - 200 - shadowOffset, parent.height / 2 - 150 - shadowOffset, 400 + 2 * shadowOffset, 300 + 2 * shadowOffset, 15);


        // White Login Box
        parent.fill(255,249,254);
        parent.stroke(200);
        parent.rect(parent.width / 2 - 200, parent.height / 2 - 150, 400, 300, 10);

        // Title
        parent.fill(0);
        parent.textSize(24);
        parent.text("Login", 450, 165);

        // Username Field
        if (usernameSelected) {
            parent.fill(216,202,220);
        } else {
            parent.noFill();
        }
        parent.stroke(84, 84, 84);
        parent.rect(parent.width / 2 - 120, parent.height / 2 - 75, 240, 40, 5);
        if (enteredUsername.isEmpty()) {
            parent.fill(84, 84, 84);
            parent.textSize(16);
            parent.text("Username", parent.width / 2 - 110, parent.height / 2 - 50);
        }
        parent.textSize(16);
        parent.fill(0);
        parent.text(enteredUsername, parent.width / 2 - 110, parent.height / 2 - 50);

        // Password Field
        if (passwordSelected) {
            parent.fill(216,202,220);
        } else {
            parent.noFill();
        }
        parent.stroke(84, 84, 84);
        parent.rect(parent.width / 2 - 120, parent.height / 2 - 15, 240, 40, 5);
        if (enteredPassword.isEmpty()) {
            parent.fill(84, 84, 84);
            parent.textSize(16);
            parent.text("Password", 370, parent.height / 2 + 10);

        }
        parent.textSize(16);
        parent.fill(0);

        String hiddenPassword = "*".repeat(enteredPassword.length());
        parent.text(hiddenPassword, parent.width / 2 - 110, parent.height / 2 + 10);

        // Login Button
        boolean isHover = isMouseOverButton(430, 330, 100, 40);
        if (isHover) {
            parent.fill(174,37,222,200);
        } else {
            parent.fill(174,37,222);
        }
        parent.noStroke();
        parent.rect(430, 330, 100, 40, 10);
        parent.fill(255);
        parent.textSize(16);
        parent.text("Login", 460, 355);

        boolean isHoveringNew = isMouseOverButton(parent.width / 2 - 120, 310, (int)parent.textWidth("New User?"), 10);
        if (isHoveringNew) {
            parent.fill(174,37,222);
        } else {
            parent.fill(0);
        }

        parent.text("New User?", parent.width / 2 - 120, 320);

        boolean isHoveringGuest = isMouseOverButton(parent.width / 2 - 120, 385, (int)parent.textWidth("Continue as Guest?"), 10);
        if (isHoveringGuest) {
            parent.fill(174,37,222);
        } else {
            parent.fill(0);
        }

        parent.text("Continue as Guest?", parent.width / 2 - 120, 395);

        if (loginFailed) {
            parent.fill(255, 0, 0); // Red color for error message
            parent.textSize(16);
            parent.text(loginErrorMessage, parent.width / 2 - 175, 185); // Position it appropriately
        }
    }

    public void draw() {
        if (isLoginScreenVisible) {
            drawLogin();  // Draw login screen if it's visible
        } else if (isViewScrollsGuestVisible) {
//            viewScrollsGuest.drawScrollsGuest();  // Draw ViewScrollsGuest screen
        }
    }

    public void checkLogin() {
        System.out.println("Checking login...");

        // Create a User object
        user = new User();

        // Check if the entered username and password match the correct ones
        if (user.login(enteredUsername, enteredPassword)) {
            // Retrieve user info, including the 'admin' status
            Map<String, String> userInfo = user.getUserInfo();
            boolean isAdmin = userInfo != null && Boolean.parseBoolean(userInfo.get("admin"));  // Assuming 'admin' is stored as a boolean in the database

            if (isAdmin) {
                System.out.println("Admin login successful!");
                user.setUsername(enteredUsername);
                isLoginScreenVisible = false;
                isViewScrollsAdminVisible = true;
            } else {
                System.out.println("User login successful! User: " + enteredUsername);
                user.setUsername(enteredUsername);
                isLoginScreenVisible = false;
                isViewScrollsUserVisible = true;
            }

        } else {
            System.out.println("Login failed. Incorrect username or password.");
            loginFailed = true;
            loginErrorMessage = "Login failed. Incorrect username or password.";

            usernameSelected = false;
            passwordSelected = false;
            // Reset username and password fields
            enteredUsername = "";
            enteredPassword = "";
        }

    }


    private boolean isMouseOverButton(int x, int y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    public void mousePressed() {
        System.out.println("Mouse Pressed at: " + parent.mouseX + ", " + parent.mouseY);
        if (isMouseOverButton(parent.width / 2 - 120, 310, (int)parent.textWidth("New User?"), 10)) {
            System.out.println("Switching to Register screen");
            registerScreen.isRegisterScreenVisible = true;
            isLoginScreenVisible = false;

            enteredUsername = "";  // reset username and password fields
            enteredPassword = "";

            registerScreen.mousePressed();
        }


        if (isMouseOverButton(parent.width / 2 - 120, parent.height / 2 - 75, 240, 40)) {
            usernameSelected = true;
            passwordSelected = false;
        }

        if (isMouseOverButton(parent.width / 2 - 120, parent.height / 2 - 10, 240, 40)) {
            usernameSelected = false;
            passwordSelected = true;
        }

        if (isMouseOverButton(parent.width / 2 - 120, 385, (int)parent.textWidth("Continue as Guest?"), 10)) {
            System.out.println("Continuing as Guest");
            registerScreen.isRegisterScreenVisible = false;
            isLoginScreenVisible = false;
            isViewScrollsGuestVisible = true;
        }

        if (isMouseOverButton(430, 330, 100, 40)) {
            checkLogin();
        }
    }

    public void keyPressed() {
        handleKeyInput();
//
//        if (parent.key == PApplet.ENTER || parent.key == PApplet.RETURN) {
//            try {
//                checkLogin();
//                isLoginScreenVisible = false;
//                isViewScrollsUserVisible = true;
//                    // Trigger whatever happens after login (e.g., show another screen)
////                } else {
////                    System.out.println("Login failed. Invalid username or password.");
//            } catch (NumberFormatException e) {
//                System.out.println("Entered ID is not an integer");
//            }
//        }
    }

    public void handleKeyInput() {
        char key = parent.key;
        if (usernameSelected) {
            if (Character.isLetterOrDigit(key) || key == '_') {
                enteredUsername += key;
            }
            if (key == PApplet.BACKSPACE && enteredUsername.length() > 0) {
                enteredUsername = enteredUsername.substring(0, enteredUsername.length() - 1);
            }
        } else if (passwordSelected) {
            if (Character.isLetterOrDigit(key) || key == '_') {
                enteredPassword += key;
            }
            if (key == PApplet.BACKSPACE && enteredPassword.length() > 0) {
                enteredPassword = enteredPassword.substring(0, enteredPassword.length() - 1);
            }
        }
    }

    public String getEnteredUsername() {
        return enteredUsername;
    }

    public User getUserObj() {
        return user;
    }
}
