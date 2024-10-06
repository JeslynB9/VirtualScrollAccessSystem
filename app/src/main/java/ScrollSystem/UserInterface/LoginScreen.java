package ScrollSystem.UserInterface;

import ScrollSystem.UserInterface.RegisterScreen;
import processing.core.PApplet;
import processing.core.PImage;

public class LoginScreen {
    PApplet parent;
    ViewScrollsGuest viewScrollsGuest;
    public boolean isUserGuest = false;
    public boolean isLoginScreenVisible = true;
    boolean usernameSelected = false;
    boolean passwordSelected = false;
    public RegisterScreen registerScreen;
    float shadowOffset = 8;

    // Correct credentials
    String correctUsername = "user";
    String correctPassword = "123";

    String enteredUsername = "";
    String enteredPassword = "";

    public LoginScreen(PApplet parent) {

        this.parent = parent;
        registerScreen = new RegisterScreen(parent, this);
        viewScrollsGuest = new ViewScrollsGuest(parent);
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
        parent.text("Login", 450, 175);

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
    }

    public void checkLogin() {
        System.out.println("Checking login...");

        // Check if the entered username and password match the correct ones
        if (enteredUsername.equals(correctUsername) && enteredPassword.equals(correctPassword)) {
            System.out.println("Login successful!");
            isLoginScreenVisible = false;
            // You can trigger further actions here, like switching to another screen
        } else {
            System.out.println("Login failed. Incorrect username or password.");
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
            isUserGuest = true;
        }
    }

    public void keyPressed() {
        handleKeyInput();

        if (parent.key == PApplet.ENTER || parent.key == PApplet.RETURN) {
            try {
                checkLogin();
                isLoginScreenVisible = false;
                    // Trigger whatever happens after login (e.g., show another screen)
//                } else {
//                    System.out.println("Login failed. Invalid username or password.");
            } catch (NumberFormatException e) {
                System.out.println("Entered ID is not an integer");
            }
        }
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

}
