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

    String enteredScrollID = "";
    String enteredUploaderID = "";
    String enteredTitle = "";
    String enteredUploadDate = "";


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
        if (enteredScrollID.isEmpty()) {
            parent.fill(84, 84, 84);
            parent.textSize(16);
            parent.text("Scroll ID", 370, 175);
        }
        parent.textSize(16);
        parent.fill(0);
        parent.text(enteredScrollID, 370, 175);

        // Uploader ID Field
        if (uploaderIdSelected) {
            parent.fill(216,202,220);
        } else {
            parent.noFill();
        }
        parent.stroke(84, 84, 84);
        parent.rect(parent.width / 2 - 120, parent.height / 2 - 70, 240, 40, 5);
        if (enteredUploaderID.isEmpty()) {
            parent.fill(84, 84, 84);
            parent.textSize(16);
            parent.text("Uploader ID", 370, 225);
        }
        parent.textSize(16);
        parent.fill(0);
        parent.text(enteredUploaderID, 370, 225);

        // Title Field
        if (titleSelected) {
            parent.fill(216,202,220);
        } else {
            parent.noFill();
        }
        parent.stroke(84, 84, 84);
        float titleBoxX = parent.width / 2 - 120;
        float titleBoxY = parent.height / 2 - 20;
        float titleBoxWidth = 240;
        float titleBoxHeight = 40;
        parent.rect(titleBoxX, titleBoxY, titleBoxWidth, titleBoxHeight, 5);

        // Handle text overflow and scrolling
        float titleTextWidth = parent.textWidth(enteredTitle);
        float titleStartX = titleBoxX + 10;
        float textOffset = 0;

        if (titleTextWidth > titleBoxWidth - 20) {  // If text exceeds the input box width
            textOffset = titleTextWidth - (titleBoxWidth - 20);  // Calculate how much to scroll
        }

        // Clip to the title input box
        parent.clip(titleBoxX + 10, titleBoxY, titleBoxWidth, titleBoxHeight);

        parent.textSize(16);
        parent.fill(0);
        parent.text(enteredTitle, titleStartX - textOffset, 275);

        parent.noClip();

        if (enteredTitle.isEmpty()) {
            parent.fill(84, 84, 84);
            parent.textSize(16);
            parent.text("Title", 370, 275);
        }

        // Upload Date Field
        if (uploadDateSelected) {
            parent.fill(216,202,220);
        } else {
            parent.noFill();
        }
        parent.stroke(84, 84, 84);
        parent.rect(parent.width / 2 - 120, parent.height / 2 + 30, 240, 40, 5);
        if (enteredUploadDate.isEmpty()) {
            parent.fill(84, 84, 84);
            parent.textSize(16);
            parent.text("Upload Date (dd-mm-yyyy)", 370, 325);
        }
        parent.textSize(16);
        parent.fill(0);
        parent.text(enteredUploadDate, 370, 325);

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

    public void keyPressed() {
        handleKeyInput();

        if (parent.key == PApplet.ENTER || parent.key == PApplet.RETURN) {
            try {
//                int id = Integer.valueOf(enteredUsername);
//                if (adminLogin.checkLogin(id, enteredPassword)) {
//                    System.out.println("Login successful");
//                    parent2.isAdminLoggedIn = true;
//                    parent2.userID = Integer.valueOf(enteredUsername);
                isFilterScreenVisible = false;

                enteredScrollID = "";
                enteredUploaderID = "";
                enteredTitle = "";
                enteredUploadDate = "";

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
        if (scrollIdSelected) {
            if (Character.isDigit(key) && enteredScrollID.length() < 20) {
                enteredScrollID += key;
            } else if (key == PApplet.BACKSPACE && enteredScrollID.length() > 0) {
                enteredScrollID = enteredScrollID.substring(0, enteredScrollID.length() - 1);
            }
        } else if (uploaderIdSelected) {
            if (Character.isDigit(key) && enteredUploaderID.length() < 20) {
                enteredUploaderID += key;
            } else if (key == PApplet.BACKSPACE && enteredUploaderID.length() > 0) {
                enteredUploaderID = enteredUploaderID.substring(0, enteredUploaderID.length() - 1);
            }
        } else if (titleSelected) {
            if ((Character.isLetterOrDigit(key) || key == ' ') && enteredTitle.length() < 100) {
                enteredTitle += key;
            } else if (key == PApplet.BACKSPACE && enteredTitle.length() > 0) {
                enteredTitle = enteredTitle.substring(0, enteredTitle.length() - 1);
            }
        } else if (uploadDateSelected) {
            if ((Character.isDigit(key) || key == '-') && enteredUploadDate.length() < 10) {
                enteredUploadDate += key;
            } else if (key == PApplet.BACKSPACE && enteredUploadDate.length() > 0) {
                enteredUploadDate = enteredUploadDate.substring(0, enteredUploadDate.length() - 1);
            }
        }
    }
}
