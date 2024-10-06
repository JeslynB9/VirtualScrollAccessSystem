package ScrollSystem.UserInterface;

import processing.core.PApplet;

public class FilterScreen {
    PApplet parent;
    public boolean isFilterScreenVisible = false;
    boolean phoneNumberSelected = false;
    boolean emailSelected = false;
    boolean fullNameSelected = false;
    boolean usernameSelected = false;
    boolean passwordSelected = false;
    float shadowOffset = 8;
    ViewScrollsGuest viewScrollsGuest;
    String enteredEmployeeId = "";
    String enteredUsername = "";
    String enteredPassword = "";


    public FilterScreen(PApplet parent, ViewScrollsGuest viewScrollsGuest) {
        this.parent = parent;
        this.viewScrollsGuest = viewScrollsGuest;
    }

    public void drawFilter() {
        if (!isFilterScreenVisible) return;

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
        parent.text("Filter", 430, 120);

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
        parent.text("Scroll ID", 370, 175);

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
        parent.text("Uploader ID", 370, 225);

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
        parent.text("Title", 370, 275);

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
        parent.text("Upload Date", 370, 325);

        // Filter Button
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
        parent.text("Filter", 577, 435);


    }

    private boolean isMouseOverButton(int x, int y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    public void mousePressed() {
    }
}
