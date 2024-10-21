package ScrollSystem.UserInterface;

import ScrollSystem.FileHandlers.LoginDatabase;
import ScrollSystem.FileHandlers.ScrollDatabase;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.List;
import java.util.Map;

public class AdminProfile {
    PApplet parent;
    PImage viewImg;
    PImage exitImg;
    ViewScrollsAdmin viewScrollsAdmin;
    public EditUserScreen editUserScreen;
    public AddUserScreen addUserScreen;
    public ViewUsersDetails viewUsersDetails;

    ScrollDatabase scrollDatabase;

    String username;
    String usernameDeleted;
    LoginDatabase loginDatabase;
    List<Map<String, String>> users;
    static int width = 1920 / 2;
    static int height = 1080 / 2;
    float rectW = width - 100;
    float rectH = (float) (height/3)*2;
    float cornerRadius = 10;
    float rectX;
    float rectY;
    float rectY1;
    float rectHeight = 40;
    public boolean isAdminProfileVisible = false;
    boolean isDeleteActive = false;
    String name;
    String fullName;
    String phone;
    String email;

    private int currentPage = 0;
    private static final int USERS_PER_PAGE = 4;

    // Draw the shadow all around (slightly larger than the rectangle)
    float shadowOffset = 8;

    // Constructor receives the PApplet instance
    public AdminProfile(PApplet parent, ViewScrollsAdmin viewScrollsAdmin) {
        this.parent = parent;
        this.viewScrollsAdmin = viewScrollsAdmin;
        this.viewUsersDetails = new ViewUsersDetails(parent, viewScrollsAdmin, this);
        addUserScreen = new AddUserScreen(parent, this);
        editUserScreen = new EditUserScreen(parent, this, viewScrollsAdmin.getUserObj());

        // Calculate the rectangle's top-left corner based on the center
        rectX = (float) width / 2 - rectW / 2;
        rectY = (float) height / 2 - rectH / 2;

        viewImg = parent.loadImage("src/main/resources/view.png");
        viewImg.resize(1920 / 40, 1080 / 40);

        exitImg = parent.loadImage("src/main/resources/exit.png");
        exitImg.resize(1920 / 30, 1080 / 30);

        loginDatabase = new LoginDatabase("src/main/java/ScrollSystem/Databases/database.db");
        users = loginDatabase.getAllUsers();
    }

    public void drawUserProfile() {

        // Set text size using the PApplet instance
        parent.stroke(84, 84, 84);
        parent.textSize(12);
        parent.fill(0);

        // Shadow properties
        parent.fill(0, 0, 0, 50);
        parent.noStroke();
        parent.rect(rectX - shadowOffset, rectY - shadowOffset, rectW + 2 * shadowOffset, rectH + 2 * shadowOffset,
                cornerRadius + 5);

        // Main rectangle properties
        parent.fill(253,249,255);
        parent.noStroke();

        // Draw the main rounded rectangle
        parent.rect(rectX, rectY, rectW, rectH, cornerRadius);

        // Long rectangle header
        parent.noStroke();
        parent.fill(216,202,220);
        parent.rect(rectX, rectY + 30, rectW, 30);
        parent.rect(rectX, rectY, rectW, 60, cornerRadius);

        parent.noStroke();
        parent.fill(255, 249, 254);
        parent.rect(rectX, rectY, rectW/4, 60, cornerRadius);
        parent.rect(rectX, rectY+30, rectW/4, 30);

        // Converter text and image
        parent.fill(174,37,222);
        parent.textSize(16);
        parent.text("Users", 135, 127);


        // User details
        parent.fill(253,249,255);
        // username = viewScrollsUsers.loginScreen.getEnteredUsername();
        username = viewScrollsAdmin.getUserObj().getUsername();
        parent.text(username, rectX, 40);
        parent.text("User", rectX, 60);



        // Edit Profile Button
        boolean isHoverEdit = isMouseOverButton((int) rectX + username.length() + 40, 25, 100, 40);
        if (isHoverEdit) {
            parent.fill(174,37,222,200);
        } else {
            parent.fill(174,37,222);
        }
        parent.noStroke();
        parent.rect(rectX + username.length() + 60, 25, 100, 40, 10);
        parent.fill(255);
        parent.textSize(16);
        parent.text("Edit Profile", rectX + username.length() + 68, 50);

        // Homepage Button
        boolean isHoverHome = isMouseOverButton((int) rectX + username.length() + 40 + 140, 25, 100, 40);
        if (isHoverHome) {
            parent.fill(174,37,222,200);
        } else {
            parent.fill(174,37,222);
        }
        parent.noStroke();
        parent.rect(rectX + username.length() + 40 + 140, 25, 100, 40, 10);
        parent.fill(255);
        parent.textSize(16);
        parent.text("Homepage", rectX + username.length() + 48 + 140, 50);


        // --------------------------- USERS ---------------------------
        drawUsers();

        parent.noStroke();

        // Add User Button
        boolean isHoverAddUser = isMouseOverButton(730, 100, 120, 40);
        if (isHoverAddUser) {
            parent.fill(174,37,222,200);
        } else {
            parent.fill(174,37,222);
        }
        parent.noStroke();
        parent.rect(730, 100, 120, 40, 10);
        parent.fill(255);
        parent.textSize(16);
        parent.text("Add User", 755, 125);

        if (isDeleteActive) {
            drawDelete();
        }
    }

    public void drawUsers() {

        users = loginDatabase.getAllUsers();
        // parent.noLoop();
        parent.fill(92, 86, 93);
        parent.text("Username", rectX + 50, rectY + 95);
        parent.text("Full Name", rectX + 210, rectY + 95);
        parent.text("Phone Number", rectX + 370, rectY + 95);
        parent.text("Email", rectX + 530, rectY + 95);
        rectY1 = rectY;

        int start = currentPage * USERS_PER_PAGE;
        int end = Math.min(start + USERS_PER_PAGE, users.size());

        for (int i = start; i < end; i++) {
            Map<String, String> user = users.get(i);
            name = user.get("username");
            fullName = user.get("fullName");
            phone = user.get("phoneNo");
            email = user.get("email");

            parent.stroke(92, 86, 93);
            parent.strokeWeight(2);
            parent.noFill();

            // Username Field
            parent.rect(rectX + 40, rectY1 + 100, 160, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(name, rectX + 50, rectY1 + 125);

            // Full Name Field
            parent.noFill();
            parent.rect(rectX + 200, rectY1 + 100, 160, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(fullName, rectX + 210, rectY1 + 125);

            // Phone Field
            parent.noFill();
            parent.rect(rectX + 360, rectY1 + 100, 160, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(phone, rectX + 370, rectY1 + 125);

            // Email Field
            parent.noFill();
            parent.rect(rectX + 520, rectY1 + 100, 220, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(email, rectX + 530, rectY1 + 125);

            // View Field
            if (isMouseOverButton((int) rectX + 740, (int) rectY1 + 103, 40, 40)) {
                parent.fill(216, 202, 220, 200);
            } else {
                parent.noFill();
            }
            parent.rect(rectX + 740, rectY1 + 100, 40, 40);
            parent.image(viewImg, rectX + 736, rectY1 + 107);

            // Delete Field
            if (isMouseOverButton((int) rectX + 780, (int) rectY1 + 103, 40, 40)) {
                parent.fill(216, 202, 220, 200);
            } else {
                parent.noFill();
            }
            parent.rect(rectX + 780, rectY1 + 100, 40, 40);
            parent.image(exitImg, rectX + 768, rectY1 + 103);

            parent.noStroke();

            rectY1 += rectHeight + 20;
        }

        // Previous button
        if (currentPage > 0) {
            if (isMouseOverButton((int) rectX + 50, (int) rectY + rectH - 35, 40, 30)) {
                parent.fill(200, 50, 250);
            } else {
                parent.fill(174, 37, 222);
            }
            parent.rect(rectX + 50, rectY + rectH - 35, 40, 30, 5);
            parent.fill(255);
            parent.textSize(35);
            parent.text("<", rectX + 55, rectY + rectH - 10);
        }

        // Next button
        if ((currentPage + 1) * USERS_PER_PAGE < users.size()) {
            if (isMouseOverButton((int) rectX + rectW - 90, (int) rectY + rectH - 35, 40, 30)) {
                parent.fill(200, 50,250); 
            } else { 
                parent.fill(174, 37, 222); 
            } 
            parent.rect(rectX + rectW - 90, rectY + rectH - 35, 40, 30, 5); 
            parent.fill(255); parent.textSize(35); 
            parent.text(">", rectX + rectW - 83, rectY + rectH - 10); 
        } 
    }


    public void drawDelete() {
        // Background Overlay
        parent.fill(0,0,0,200);
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
        parent.text("Delete User", 420, 165);
        parent.text("Do you want to", 400, 225);
        parent.text("delete this", 425, 265);
        parent.text("user?", 450, 305);

        // Yes Button
        boolean isHoverYes = isMouseOverButton(550, 370, 120, 40);
        if (isHoverYes) {
            parent.fill(174,37,222,200);
        } else {
            parent.fill(174,37,222);
        }
        parent.noStroke();
        parent.rect(550, 370, 100, 40, 10);
        parent.fill(255);
        parent.textSize(16);
        parent.text("Yes", 585, 395);

        // No Button
        boolean isHoverNo = isMouseOverButton(310, 370, 120, 40);
        if (isHoverNo) {
            parent.fill(174,37,222,200);
        } else {
            parent.fill(174,37,222);
        }
        parent.noStroke();
        parent.rect(310, 370, 100, 40, 10);
        parent.fill(255);
        parent.textSize(16);
        parent.text("No", 350, 395);

    }

    private boolean isMouseOverButton(int x, int y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    private boolean isMouseOverButton(int x, float y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    private boolean isMouseOverButton(float x, float y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    // Method to handle mouse presses
    public void mousePressed() {
        if (isMouseOverButton(730, 100, 120, 40)) {
            System.out.println("Add User Selected");
            addUserScreen.isAddUserScreenVisible = true;
            parent.redraw();
            addUserScreen.mousePressed();
        }

        if (isMouseOverButton((int) rectX + viewScrollsAdmin.loginScreen.getEnteredUsername().length() + 60, 25, 100, 40)) {
            System.out.println("Edit Profile Selected");
            editUserScreen.isEditProfileScreenVisible = true;
            parent.redraw();
            editUserScreen.mousePressed();
        }

        if (isMouseOverButton((int) rectX + viewScrollsAdmin.loginScreen.getEnteredUsername().length() + 40 + 140, 25, 100, 40)) {
            System.out.println("Home Page Selected");
            isAdminProfileVisible = false;
            viewScrollsAdmin.loginScreen.isViewScrollsAdminVisible = true;
            parent.redraw();
            viewScrollsAdmin.mousePressed();
        }

        // Iterate over users to detect View or Delete button clicks
        for (int i = 0; i < users.size(); i++) {
            float viewX = rectX + 740;
            float viewY = rectY + 100 + (i * (rectHeight + 20)); // Update viewY for each user dynamically

            // View button
            if (isMouseOverButton((int) viewX, (int) viewY, viewImg.width, viewImg.height)) {
                Map<String, String> selectedUser = users.get(i);
                String usernameToView = selectedUser.get("username");
                System.out.println("View Selected user: " + usernameToView);

                // Set the user to be viewed in ViewUsersDetails and display the screen
                viewUsersDetails.setUsername(usernameToView);
                viewUsersDetails.isViewUsersDetailsVisible = true;
                System.out.println("Setting isViewUsersDetailsVisible to true after selecting user.");

                isAdminProfileVisible = false;
                parent.redraw();
                return;
            }

            float exitX = rectX + 780;
            float exitY = rectY + 100 + (i * (rectHeight + 20));

            if (isMouseOverButton((int) exitX, (int) exitY, exitImg.width, exitImg.height)) {
                Map<String, String> selectedUser = users.get(i); // Get the selected scroll details
                usernameDeleted = selectedUser.get("username");
                System.out.println("Delete Selected user: " + usernameDeleted);
                isDeleteActive = true;

            }

            if (isDeleteActive) {
                if (isMouseOverButton(550, 370, 120, 40)) {
                    loginDatabase.deleteUserByUsername(usernameDeleted);
                    System.out.println("Deleted User: " + usernameDeleted);
                    users = loginDatabase.getAllUsers();
                    isDeleteActive = false;
                    parent.redraw();
                }

                if (isMouseOverButton(310, 370, 120, 40)) {
                    isDeleteActive = false;
                    parent.redraw();
                }
            }
        }

        if (isMouseOverButton((int) rectX + rectW - 90, (int) rectY + rectH - 35, 40, 30)) {
            if ((currentPage + 1) * USERS_PER_PAGE < users.size()) {
                currentPage++;
                parent.redraw();
            }
        }
    
        if (isMouseOverButton((int) rectX + 50, (int) rectY + rectH - 35, 40, 30)) {
            if (currentPage > 0) {
                currentPage--;
                parent.redraw();
            }
        }

    }
}

