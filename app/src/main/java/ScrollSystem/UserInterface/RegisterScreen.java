package ScrollSystem.UserInterface;

import processing.core.PApplet;

public class RegisterScreen {
    PApplet parent;
    public boolean isRegisterScreenVisible = false;
    boolean phoneNumberSelected = false;
    boolean emailSelected = false;
    boolean fullNameSelected = false;
    boolean usernameSelected = false;
    boolean passwordSelected = false;
    float shadowOffset = 8;
    LoginScreen loginScreen;
    String enteredEmployeeId = "";
    String enteredUsername = "";
    String enteredPassword = "";


    public RegisterScreen(PApplet parent, LoginScreen loginScreen) {
        this.parent = parent;
        this.loginScreen = loginScreen;
    }

    public void drawRegister() {
        if (!isRegisterScreenVisible) return;

        // Background Overlay
        parent.fill(92,86,93);
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
        parent.text("Register", 430, 120);

        // Phone Number Field
        if (phoneNumberSelected) {
            parent.fill(216,202,220);
        } else {
            parent.noFill();
        }
        parent.stroke(84, 84, 84);
        parent.rect(parent.width / 2 - 120, parent.height / 2 - 120, 240, 40, 5);

        parent.fill(84, 84, 84);
        parent.textSize(16);
        parent.text("Phone Number", 370, 175);

        // Email Field
        if (emailSelected) {
            parent.fill(216,202,220);
        } else {
            parent.noFill();
        }

        parent.stroke(84, 84, 84);
        parent.rect(parent.width / 2 - 120, parent.height / 2 - 70, 240, 40, 5);

        parent.fill(84, 84, 84);
        parent.textSize(16);
        parent.text("Email", 370, 225);

        // Full Name Field
        if (fullNameSelected) {
            parent.fill(216,202,220);
        } else {
            parent.noFill();
        }

        parent.stroke(84, 84, 84);
        parent.rect(parent.width / 2 - 120, parent.height / 2 - 20, 240, 40, 5);

        parent.fill(84, 84, 84);
        parent.textSize(16);
        parent.text("Full Name", 370, 275);

        // Username Field
        if (usernameSelected) {
            parent.fill(216,202,220);
        } else {
            parent.noFill();
        }
        parent.stroke(84, 84, 84);
        parent.rect(parent.width / 2 - 120, parent.height / 2 + 30, 240, 40, 5);
        parent.fill(84, 84, 84);
        parent.textSize(16);
        parent.text("Username", 370, 325);

        // Password Field
        if (passwordSelected) {
            parent.fill(216,202,220);
        } else {
            parent.noFill();
        }
        parent.stroke(84, 84, 84);
        parent.rect(parent.width / 2 - 120, parent.height / 2 + 80, 240, 40, 5);
        parent.fill(84, 84, 84);
        parent.textSize(16);
        parent.text("Password", 370, 375);


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
        parent.text("Register", 577, 435);

        boolean isHovering = isMouseOverButton(300, 425, (int)parent.textWidth("Have An Account?"), 10);
        if (isHovering) {
            parent.fill(174,37,222);
        } else {
            parent.fill(0);
        }

        parent.text("Have An Account?", 300, 435);

    }

    private boolean isMouseOverButton(int x, int y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    public void mousePressed() {
        if (isMouseOverButton(300, 425, (int)parent.textWidth("Have An Account?"), 10)) {
            isRegisterScreenVisible = false;
            loginScreen.isLoginScreenVisible = true;
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
}
