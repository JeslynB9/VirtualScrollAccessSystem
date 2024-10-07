package ScrollSystem.UserInterface;

import processing.core.PApplet;

public class FilterScreen {
    PApplet parent;
    public boolean isFilterScreenVisible = false;
    boolean scrollIdSelected = false;
    boolean uploaderIdSelected = false;
    boolean titleSelected = false;
    boolean uploadDateSelected = false;
    float shadowOffset = 8;
    ViewScrollsGuest viewScrollsGuest;
    ViewScrollsUsers viewScrollsUsers;
    ViewScrollsAdmin viewScrollsAdmin;

    String enteredEmployeeId = "";
    String enteredUsername = "";
    String enteredPassword = "";


    public FilterScreen(PApplet parent, ViewScrollsGuest viewScrollsGuest) {
        this.parent = parent;
        this.viewScrollsGuest = viewScrollsGuest;
    }

    public FilterScreen(PApplet parent, ViewScrollsUsers viewScrollsUsers) {
        this.parent = parent;
        this.viewScrollsUsers = viewScrollsUsers;
    }

    public FilterScreen(PApplet parent, ViewScrollsAdmin viewScrollsAdmin) {
        this.parent = parent;
        this.viewScrollsAdmin = viewScrollsAdmin;
    }

    public void drawFilter() {
        if (!isFilterScreenVisible) return;

        // Background Overlay
        parent.fill(0, 0, 0, 150);
        parent.rect(0, 0, parent.width*2, parent.height);

        // Shadow properties
        parent.fill(0, 0, 0, 50);
        parent.noStroke();
        parent.rect(parent.width / 2 - 200 - shadowOffset, parent.height / 2 - 200 - shadowOffset, 400 + 2 * shadowOffset, 350 + 2 * shadowOffset, 15);

        // White Register Box
        parent.fill(255,249,254);
        parent.stroke(200);
        parent.rect(parent.width / 2 - 200, parent.height / 2 - 200, 400, 350, 10);

        // Title
        parent.fill(0);
        parent.textSize(24);
        parent.text("Filter", 450, 120);

        // Scroll ID Field
        if (scrollIdSelected) {
            parent.fill(216,202,220);
        } else {
            parent.noFill();
        }
        parent.stroke(84, 84, 84);
        parent.rect(parent.width / 2 - 120, parent.height / 2 - 120, 240, 40, 5);

        parent.fill(84, 84, 84);
        parent.textSize(16);
        parent.text("Scroll ID", 370, 175);

        // Uploader ID Field
        if (uploaderIdSelected) {
            parent.fill(216,202,220);
        } else {
            parent.noFill();
        }

        parent.stroke(84, 84, 84);
        parent.rect(parent.width / 2 - 120, parent.height / 2 - 70, 240, 40, 5);

        parent.fill(84, 84, 84);
        parent.textSize(16);
        parent.text("Uploader ID", 370, 225);

        // Title Field
        if (titleSelected) {
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
        if (uploadDateSelected) {
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
        boolean isHoverFilter = isMouseOverButton(560, 370, 100, 40);
        if (isHoverFilter) {
            parent.fill(174,37,222, 200);
        } else {
            parent.fill(174,37,222);
        }
        parent.noStroke();
        parent.rect(560, 370, 100, 40, 10);
        parent.fill(255);
        parent.textSize(16);
        parent.text("Filter", 590, 395);

        // Close Button
        boolean isHoverClose = isMouseOverButton(300, 370, 100, 40);
        if (isHoverClose) {
            parent.fill(174,37,222, 200);
        } else {
            parent.fill(174,37,222);
        }
        parent.noStroke();
        parent.rect(300, 370, 100, 40, 10);
        parent.fill(255);
        parent.textSize(16);
        parent.text("Close", 330, 395);
    }

    private boolean isMouseOverButton(int x, int y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    public void mousePressed() {
        if (isMouseOverButton(300, 370, 100, 40)) {
            System.out.println("Close filter selected");
            isFilterScreenVisible = false;
        }

        if (isMouseOverButton(parent.width / 2 - 120, parent.height / 2 - 120, 240, 40)) {
            scrollIdSelected = true;
            uploaderIdSelected = false;
            titleSelected = false;
            uploadDateSelected = false;
        }

        if (isMouseOverButton(parent.width / 2 - 120, parent.height / 2 - 70, 240, 40)) {
            scrollIdSelected = false;
            uploaderIdSelected = true;
            titleSelected = false;
            uploadDateSelected = false;
        }

        if (isMouseOverButton(parent.width / 2 - 120, parent.height / 2 - 10, 240, 40)) {
            scrollIdSelected = false;
            uploaderIdSelected = false;
            titleSelected = true;
            uploadDateSelected = false;
        }

        if (isMouseOverButton(parent.width / 2 - 120, parent.height / 2 + 30, 240, 40)) {
            scrollIdSelected = false;
            uploaderIdSelected = false;
            titleSelected = false;
            uploadDateSelected = true;
        }
    }
}
