package ScrollSystem.UserInterface;

import ScrollSystem.Users.User;
import org.checkerframework.checker.units.qual.A;
import processing.core.PApplet;

import java.util.Map;

public class AddUserScreen {
    PApplet parent;
    public boolean isAddUserScreenVisible = false;
    boolean phoneNumberSelected = false;
    boolean emailSelected = false;
    boolean fullNameSelected = false;
    boolean usernameSelected = false;
    boolean passwordSelected = false;
    float shadowOffset = 8;
    AdminProfile adminProfile;
    String enteredEmployeeId = "";
    String enteredPhoneNumber = "";
    String enteredEmail = "";
    String enteredFullName = "";
    String enteredUsername = "";
    String enteredPassword = "";
    boolean addFailed = false;
    private String addErrorMessage = "";

    public AddUserScreen(PApplet parent, AdminProfile adminProfile) {
        this.parent = parent;
        this.adminProfile = adminProfile;
    }

    public void drawAddUser() {
        if (!isAddUserScreenVisible) return;

        // Background Overlay
        parent.fill(0, 0, 0, 200);
        parent.rect(0, 0, parent.width*2, parent.height);

        // Shadow properties
        parent.fill(0, 0, 0, 50);
        parent.noStroke();
        parent.rect(parent.width / 2 - 200 - shadowOffset, parent.height / 2 - 200 - shadowOffset, 400 + 2 * shadowOffset, 400 + 2 * shadowOffset, 15);

        // White Register Box
        parent.fill(255,249,254);
        parent.stroke(200);
        parent.rect(parent.width / 2 - 200, parent.height / 2 - 200, 400, 400, 10);

        // Title
        parent.fill(0);
        parent.textSize(24);
        parent.text("Add User", 430, 120);

        // Phone Number Field
        if (phoneNumberSelected) {
            parent.fill(216,202,220);
        } else {
            parent.noFill();
        }
        parent.stroke(84, 84, 84);
        parent.rect(parent.width / 2 - 120, parent.height / 2 - 120, 240, 40, 5);
        if (enteredPhoneNumber.isEmpty()) {
            parent.fill(84, 84, 84);
            parent.textSize(16);
            parent.text("Phone Number", 370, 175);
        }
        parent.textSize(16);
        parent.fill(0);
        parent.text(enteredPhoneNumber, 370, 175);

        // Email Field
        if (emailSelected) {
            parent.fill(216,202,220);
        } else {
            parent.noFill();
        }
        parent.stroke(84, 84, 84);
        float emailBoxX = parent.width / 2 - 120;
        float emailBoxY = parent.height / 2 - 70;
        float emailBoxWidth = 240;
        float emailBoxHeight = 40;
        parent.rect(emailBoxX, emailBoxY, emailBoxWidth, emailBoxHeight, 5);

        // Handle text overflow and scrolling
        float emailTextWidth = parent.textWidth(enteredEmail);
        float emailStartX = emailBoxX + 10;
        float textOffset = 0;

        if (emailTextWidth > emailBoxWidth - 20) {  // If text exceeds the input box width
            textOffset = emailTextWidth - (emailBoxWidth - 20);  // Calculate how much to scroll
        }

        // Clip to the email input box
        parent.clip(emailBoxX + 10, emailBoxY, emailBoxWidth, emailBoxHeight);

        parent.textSize(16);
        parent.fill(0);
        parent.text(enteredEmail, emailStartX - textOffset, 225);

        parent.noClip();

        if (enteredEmail.isEmpty()) {
            parent.fill(84, 84, 84);
            parent.textSize(16);
            parent.text("Email", 370, 225);
        }

        // Full Name Field
        if (fullNameSelected) {
            parent.fill(216,202,220);
        } else {
            parent.noFill();
        }

        parent.stroke(84, 84, 84);
        parent.rect(parent.width / 2 - 120, parent.height / 2 - 20, 240, 40, 5);
        if (enteredFullName.isEmpty()) {
            parent.fill(84, 84, 84);
            parent.textSize(16);
            parent.text("Full Name", 370, 275);
        }
        parent.textSize(16);
        parent.fill(0);
        parent.text(enteredFullName, 370, 275);

        // Username Field
        if (usernameSelected) {
            parent.fill(216,202,220);
        } else {
            parent.noFill();
        }
        parent.stroke(84, 84, 84);
        parent.rect(parent.width / 2 - 120, parent.height / 2 + 30, 240, 40, 5);
        if (enteredUsername.isEmpty()) {
            parent.fill(84, 84, 84);
            parent.textSize(16);
            parent.text("Username", 370, 325);
        }
        parent.textSize(16);
        parent.fill(0);
        parent.text(enteredUsername, 370, 325);

        // Password Field
        if (passwordSelected) {
            parent.fill(216,202,220);
        } else {
            parent.noFill();
        }
        parent.stroke(84, 84, 84);
        parent.rect(parent.width / 2 - 120, parent.height / 2 + 80, 240, 40, 5);
        if (enteredPassword.isEmpty()) {
            parent.fill(84, 84, 84);
            parent.textSize(16);
            parent.text("Password", 370, 375);

        }
        parent.textSize(16);
        parent.fill(0);

        String hiddenPassword = "*".repeat(enteredPassword.length());
        parent.text(hiddenPassword, 370, 375);

        // Register Button
        boolean isHover = isMouseOverButton(560, 410, 100, 40);
        if (isHover) {
            parent.fill(174,37,222, 200);
        } else {
            parent.fill(174,37,222);
        }
        parent.noStroke();
        parent.rect(560, 410, 100, 40, 10);
        parent.fill(255);
        parent.textSize(16);
        parent.text("Add User", 575, 435);

        // Close Button
        boolean isHoverClose = isMouseOverButton(300, 410, 100, 40);
        if (isHoverClose) {
            parent.fill(174,37,222, 200);
        } else {
            parent.fill(174,37,222);
        }
        parent.noStroke();
        parent.rect(300, 410, 100, 40, 10);
        parent.fill(255);
        parent.textSize(16);
        parent.text("Close", 330, 435);

        if (addFailed) {
            parent.fill(255, 0, 0);
            parent.textSize(16);
            parent.text(addErrorMessage, parent.width / 2 - 140, 140);
        }
    }

    public void register() {
        System.out.println("Registering...");

        // Validate inputs
        if (enteredUsername.isEmpty() || enteredPassword.isEmpty() || enteredFullName.isEmpty() || enteredEmail.isEmpty() || enteredPhoneNumber.isEmpty()) {
            System.out.println("All fields must be filled");
            addErrorMessage = "All fields must be filled.";
            addFailed = true;
            return;
        } else if (enteredPhoneNumber.length() != 10) {
            addErrorMessage = "Phone Number should have 10 digits.";
            addFailed = true;
            return;
        }
        isAddUserScreenVisible = false;

        // Create a User instance and register
        User user = new User();
        user.register(enteredUsername, enteredPassword, enteredFullName, enteredEmail, enteredPhoneNumber, false);

        enteredPhoneNumber = "";
        enteredEmail = "";
        enteredFullName = "";
        enteredUsername = "";
        enteredPassword = "";

        addErrorMessage = "";
    }

    private boolean isMouseOverButton(int x, int y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    public void mousePressed() {
        if (isMouseOverButton(300, 425, (int)parent.textWidth("Have An Account?"), 10)) {
            isAddUserScreenVisible = false;

            enteredPhoneNumber = "";
            enteredEmail = "";
            enteredFullName = "";
            enteredUsername = "";
            enteredPassword = "";

            adminProfile.isAdminProfileVisible = true;
        }

        if (isMouseOverButton(300, 410, 100, 40)) {
            isAddUserScreenVisible = false;
        }

        if (isMouseOverButton(560, 410, 100, 40)) {
            register();
            adminProfile.isAdminProfileVisible = true;
            parent.redraw();
        }

        if (isMouseOverButton(parent.width / 2 - 120, parent.height / 2 - 120, 240, 40)) {
            phoneNumberSelected = true;
            emailSelected = false;
            fullNameSelected = false;
            usernameSelected = false;
            passwordSelected = false;
        }

        if (isMouseOverButton(parent.width / 2 - 120, parent.height / 2 - 70, 240, 40)) {
            phoneNumberSelected = false;
            emailSelected = true;
            fullNameSelected = false;
            usernameSelected = false;
            passwordSelected = false;
        }

        if (isMouseOverButton(parent.width / 2 - 120, parent.height / 2 - 10, 240, 40)) {
            phoneNumberSelected = false;
            emailSelected = false;
            fullNameSelected = true;
            usernameSelected = false;
            passwordSelected = false;
        }

        if (isMouseOverButton(parent.width / 2 - 120, parent.height / 2 + 30, 240, 40)) {
            phoneNumberSelected = false;
            emailSelected = false;
            fullNameSelected = false;
            usernameSelected = true;
            passwordSelected = false;
        }

        if (isMouseOverButton(parent.width / 2 - 120, parent.height / 2 + 80, 240, 40)) {
            phoneNumberSelected = false;
            emailSelected = false;
            fullNameSelected = false;
            usernameSelected = false;
            passwordSelected = true;
        }
    }

    public void keyPressed() {
        handleKeyInput();

        if (parent.key == PApplet.ENTER || parent.key == PApplet.RETURN) {
            try {
                int id = Integer.valueOf(enteredUsername);
//                if (adminLogin.checkLogin(id, enteredPassword)) {
//                    System.out.println("Login successful");
//                    parent2.isAdminLoggedIn = true;
//                    parent2.userID = Integer.valueOf(enteredUsername);
                isAddUserScreenVisible = false;

                enteredPhoneNumber = "";
                enteredEmail = "";
                enteredFullName = "";
                enteredUsername = "";
                enteredPassword = "";

            } catch (NumberFormatException e) {
                System.out.println("Entered ID is not an integer");
            }
        }
    }

    public void handleKeyInput() {
        char key = parent.key;
        if (phoneNumberSelected) {
            if (Character.isDigit(key) && enteredPhoneNumber.length() < 10) {
                enteredPhoneNumber += key;
            } else if (key == PApplet.BACKSPACE && enteredPhoneNumber.length() > 0) {
                enteredPhoneNumber = enteredPhoneNumber.substring(0, enteredPhoneNumber.length() - 1);
            }
        } else if (emailSelected) {
            if ((Character.isLetterOrDigit(key) || key == '@' || key == '.' || key == '_') && enteredEmail.length() < 100) {
                enteredEmail += key;
            } else if (key == PApplet.BACKSPACE && enteredEmail.length() > 0) {
                enteredEmail = enteredEmail.substring(0, enteredEmail.length() - 1);
            }
        } else if (fullNameSelected) {
            if ((Character.isLetterOrDigit(key) || key == ' ') && enteredFullName.length() < 100) {
                enteredFullName += key;
            } else if (key == PApplet.BACKSPACE && enteredFullName.length() > 0) {
                enteredFullName = enteredFullName.substring(0, enteredFullName.length() - 1);
            }
        } else if (usernameSelected) {
            if ((Character.isLetterOrDigit(key) || key == '_' || key == '.') && enteredUsername.length() < 20) {
                enteredUsername += key;
            } else if (key == PApplet.BACKSPACE && enteredUsername.length() > 0) {
                enteredUsername = enteredUsername.substring(0, enteredUsername.length() - 1);
            }
        } else if (passwordSelected) {
            if ((Character.isLetterOrDigit(key) || key == '_') && enteredPassword.length() < 20) {
                enteredPassword += key;
            } else if (key == PApplet.BACKSPACE && enteredPassword.length() > 0) {
                enteredPassword = enteredPassword.substring(0, enteredPassword.length() - 1);
            }
        }
    }


}
