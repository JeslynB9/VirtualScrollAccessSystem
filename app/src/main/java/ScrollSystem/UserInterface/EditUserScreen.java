package ScrollSystem.UserInterface;

import ScrollSystem.FileHandlers.LoginDatabase;
import ScrollSystem.Users.User;
import processing.core.PApplet;

import java.util.Map;

public class EditUserScreen {
    PApplet parent;
    public boolean isEditProfileScreenVisible = false;
    boolean phoneNumberSelected = false;
    boolean emailSelected = false;
    boolean fullNameSelected = false;
    boolean usernameSelected = false;
    boolean passwordSelected = false;
    float shadowOffset = 8;
    UserProfile userProfile;
    LoginDatabase loginDatabase;
    String enteredEmployeeId = "";
    String enteredPhoneNumber = "";
    String enteredEmail = "";
    String enteredFullName = "";
    String enteredUsername = "";
    String enteredPassword = "";

    public EditUserScreen(PApplet parent, UserProfile userProfile) {
        this.parent = parent;
        this.userProfile = userProfile;

        this.loginDatabase = new LoginDatabase("src/main/java/ScrollSystem/Databases/database.db");
        loadUserData(userProfile.viewScrollsUsers.loginScreen.getEnteredUsername());
    }

    public void loadUserData(String username) {
        Map<String, String> userData = loginDatabase.getUserInfo(username);
        
        if (userData != null) {
            enteredPhoneNumber = userData.get("phoneNo");
            enteredEmail = userData.get("email");
            enteredFullName = userData.get("fullName");
            enteredUsername = userData.get("username");
            enteredPassword = ""; //for security 
        } else {
            System.out.println("User not found.");
        }
    }

    public void drawEditProfile() {
        if (!isEditProfileScreenVisible) return;

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
        parent.text("Update Profile", 400, 120);

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

        // Update Button
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
        parent.text("Update", 583, 435);

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

    }

    public void update() {
        System.out.println("Updating...");

        // Create a User object
        User user = new User();

        // Update the user
        boolean res = user.updateUserInfo(enteredUsername, enteredPassword, enteredFullName, enteredEmail, enteredPhoneNumber);
        if (res) {
            System.out.println("User updated successfully!");
        } else {
            System.out.println("Failed to update user");
        }
    }

    private boolean isMouseOverButton(int x, int y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    public void mousePressed() {
        if (isMouseOverButton(300, 425, (int)parent.textWidth("Have An Account?"), 10)) {
            isEditProfileScreenVisible = false;

            enteredPhoneNumber = "";
            enteredEmail = "";
            enteredFullName = "";
            enteredUsername = "";
            enteredPassword = "";

            userProfile.isUserProfileVisible = true;

        }

        if (isMouseOverButton(560, 410, 100, 40)) {
            update();
            isEditProfileScreenVisible = false;
        }

        if (isMouseOverButton(parent.width / 2 - 120, parent.height / 2 - 120, 240, 40)) {
            parent.redraw();
            phoneNumberSelected = true;
            emailSelected = false;
            fullNameSelected = false;
            usernameSelected = false;
            passwordSelected = false;
        }

        if (isMouseOverButton(parent.width / 2 - 120, parent.height / 2 - 70, 240, 40)) {
            parent.redraw();
            phoneNumberSelected = false;
            emailSelected = true;
            fullNameSelected = false;
            usernameSelected = false;
            passwordSelected = false;
        }

        if (isMouseOverButton(parent.width / 2 - 120, parent.height / 2 - 10, 240, 40)) {
            parent.redraw();
            phoneNumberSelected = false;
            emailSelected = false;
            fullNameSelected = true;
            usernameSelected = false;
            passwordSelected = false;
        }

        if (isMouseOverButton(parent.width / 2 - 120, parent.height / 2 + 30, 240, 40)) {
            parent.redraw();
            phoneNumberSelected = false;
            emailSelected = false;
            fullNameSelected = false;
            usernameSelected = true;
            passwordSelected = false;
        }

        if (isMouseOverButton(parent.width / 2 - 120, parent.height / 2 + 80, 240, 40)) {
            parent.redraw();
            phoneNumberSelected = false;
            emailSelected = false;
            fullNameSelected = false;
            usernameSelected = false;
            passwordSelected = true;
        }
    }

    public void keyPressed() {
        handleKeyInput();

        parent.redraw();
        if (parent.key == PApplet.ENTER || parent.key == PApplet.RETURN) {
            try {
                int id = Integer.valueOf(enteredUsername);
//                if (adminLogin.checkLogin(id, enteredPassword)) {
//                    System.out.println("Login successful");
//                    parent2.isAdminLoggedIn = true;
//                    parent2.userID = Integer.valueOf(enteredUsername);
                userProfile.isUserProfileVisible = false;

                enteredPhoneNumber = "";
                enteredEmail = "";
                enteredFullName = "";
                enteredUsername = "";
                enteredPassword = "";

                // Trigger whatever happens after login (e.g., show another screen)
//                } else {
//                    System.out.println("Login failed. Invalid username or password.");
            } catch (NumberFormatException e) {
                System.out.println("Entered ID is not an integer");
            }
        }
    }


    public void handleKeyInput() {
        parent.redraw();
        char key = parent.key;
        if (phoneNumberSelected) {
            if ((Character.isDigit(key) || key == '+') && enteredPhoneNumber.length() < 20) {
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
