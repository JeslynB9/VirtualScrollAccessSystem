package ScrollSystem.UserInterface;

import java.util.*;

import ScrollSystem.FileHandlers.LoginDatabase;
import ScrollSystem.FileHandlers.ScrollDatabase;
import ScrollSystem.FileHandlers.UserScroll;
import ScrollSystem.Users.User;
import processing.core.PApplet;
import processing.core.PImage;

public class UserProfile {
    PApplet parent;
    PImage scrollsImg;
    PImage filterImg, filterImgHover;
    PImage downloadImg, deleteImg;
    ViewScrollsUsers viewScrollsUsers;
    UserScroll database;
    ScrollDatabase scrollDatabase;
    LoginDatabase loginDatabase;
    public EditUserScreen editUserScreen;
    DeleteScreen deleteScreen;
    public EditScroll editScroll;
    public UploadScroll uploadScroll;
    String username;
    static int width = 1920 / 2;
    static int height = 1080 / 2;
    float rectW = width - 100;
    float rectH = (float) (height/3)*2;
    float cornerRadius = 10;
    float rectX;
    float rectY, rectY1;
    public boolean isUserProfileVisible = false;

    // Draw the shadow all around (slightly larger than the rectangle)
    float shadowOffset = 8;

    private int currentPage = 0;
    private static final int SCROLLS_PER_PAGE = 4;
    List<HashMap<String, Object>> userScrolls;
    float rectHeight = 40;

    // Constructor receives the PApplet instance
    public UserProfile(PApplet parent, ViewScrollsUsers viewScrollsUsers, DeleteScreen deleteScreen, EditScroll editScroll) {
        this.parent = parent;
        this.viewScrollsUsers = viewScrollsUsers;
        this.deleteScreen = deleteScreen;
        this.editScroll = editScroll;

        uploadScroll = new UploadScroll(parent, this);
        editUserScreen = new EditUserScreen(parent, this, viewScrollsUsers.getUserObj());
        // deleteScreen = new DeleteScreen(parent);

        // Calculate the rectangle's top-left corner based on the center
        rectX = (float) width / 2 - rectW / 2;
        rectY = (float) height / 2 - rectH / 2;

        scrollsImg = parent.loadImage("src/main/resources/scrolls.png");
        scrollsImg.resize(1920 / 40, 1080 / 40);

        filterImg = parent.loadImage("src/main/resources/filter.png");
        filterImg.resize(1920 / 20, 1080 / 20);

        filterImgHover = parent.loadImage("src/main/resources/filter_hover.png");  
        filterImgHover.resize(1920 / 20, 1080 / 20);

        downloadImg = parent.loadImage("src/main/resources/download.png");
        downloadImg.resize(1920 / 30, 1080 / 30);

        deleteImg = parent.loadImage("src/main/resources/delete.png");
        deleteImg.resize(1920 / 60, 1080 / 33);

        this.database = new UserScroll("src/main/java/ScrollSystem/Databases/database.db");
        this.scrollDatabase = new ScrollDatabase("src/main/java/ScrollSystem/Databases/database.db");
        this.loginDatabase = new LoginDatabase("src/main/java/ScrollSystem/Databases/database.db");
        this.userScrolls = database.searchScrollsByUserId(loginDatabase.getUserIdByUsername(username));

    }

    public void drawUserProfile() {
        // parent.clear();
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
        parent.text("Uploaded Scrolls", 95, 127);


        // User details
        parent.fill(253,249,255);
        username = viewScrollsUsers.getUserObj().getUsername();
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
        parent.rect(rectX + username.length() + 40, 25, 100, 40, 10);
        parent.fill(255);
        parent.textSize(16);
        parent.text("Edit Profile", rectX + username.length() + 48, 50);


        // Homepage Button
        boolean isHoverHome = isMouseOverButton((int) rectX + username.length() + 40 + 120, 25, 100, 40);
        if (isHoverHome) {
            parent.fill(174,37,222,200);
        } else {
            parent.fill(174,37,222);
        }
        parent.noStroke();
        parent.rect(rectX + username.length() + 40 + 120, 25, 100, 40, 10);
        parent.fill(255);
        parent.textSize(16);
        parent.text("Homepage", rectX + username.length() + 48 + 120, 50);



        // --------------------------- SCROLLS ---------------------------
        parent.fill(92, 86, 93);
        parent.text("Title", rectX + 50, rectY + 95);
        parent.text("Author", rectX + 210, rectY + 95);
        parent.text("Upload Date", rectX + 370, rectY + 95);
        parent.text("Last Updated", rectX + 530, rectY + 95);
        rectY1 = rectY;


        userScrolls = database.searchScrollsByUserId(loginDatabase.getUserIdByUsername(username));

        if (userScrolls.isEmpty()) {
            parent.fill(92, 86, 93);
            parent.text("No uploaded scrolls found", rectX + 50, rectY + 150);
            return;
        }
        
        rectY1 = (float) height / 2 - rectH / 2 + 30;

        int start = currentPage * SCROLLS_PER_PAGE;
        int end = Math.min(start + SCROLLS_PER_PAGE, userScrolls.size());

        for (int i = start; i < end; i++) {
            parent.textSize(16);
            HashMap<String, Object> scrollData = userScrolls.get(i);

            String scrollName = (String) scrollData.get("scrollName");
            String scrollAuthor = (String) scrollData.get("username");
            int scrollId = (int) scrollData.get("scrollId");

            Map<String, String> scrollDetails = scrollDatabase.getRowById(scrollId);

            String uploadDate = scrollDetails.get("publishDate");
            String lastUpdate = scrollDetails.get("lastUpdate");

            // float rowYPosition = rectY + 105 + 60 + (i - start) * 60;
            // parent.text(scrollName, rectX + 50, rectY + 105 + 60 + (i - start) * 60);
            // parent.text(scrollAuthor, rectX + 210, rectY + 105 + 60 + (i - start) * 60);
            // parent.text(uploadDate, rectX + 370, rectY + 105 + 60 + (i - start) * 60);
            // parent.text(lastUpdate, rectX + 580, rectY + 105 + 60 + (i - start) * 60);

            parent.stroke(92, 86, 93);
            parent.strokeWeight(2);

            // Title
            parent.noFill();
            parent.rect(rectX + 40, rectY1 + 70, 160, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(scrollName, rectX + 50, rectY1 + 95);

            // Author
            parent.noFill();
            parent.rect(rectX + 200, rectY1 + 70, 160, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(scrollAuthor, rectX + 210, rectY1 + 95);

            // Upload Date
            parent.noFill();
            parent.rect(rectX + 360, rectY1 + 70, 160, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(uploadDate, rectX + 370, rectY1 + 95);

            // Last Update
            parent.noFill();
            parent.rect(rectX + 520, rectY1 + 70, 170, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(lastUpdate, rectX + 530, rectY1 + 95);

            // Update Scroll
            if (isMouseOverButton(rectX + 690, rectY1 + 70, 90, 40)) {
                parent.fill(216, 202, 220, 200);
            } else  {
                parent.noFill();
            }
            parent.rect(rectX + 690, rectY1 + 70, 90, rectHeight);
            parent.fill(174,37,222);
            parent.text("Update?", rectX + 700, rectY1 + 95);

            // Delete Field
            if (isMouseOverButton((int) rectX + 784, (int) rectY1 + 70, 40, 40)) {
                parent.fill(216, 202, 220, 200);
            } else  {
                parent.noFill();
            }
            parent.rect(rectX + 780, rectY1 + 70, 40, 40);
            parent.image(deleteImg, rectX + 784, rectY1 + 76);

            rectY1 += rectHeight + 20;
        }


        if (currentPage > 0) {
            if (isMouseOverButton(rectX + 50, rectY + rectH - 35, 40, 30)) {
                parent.fill(200, 50, 250);
            } else {
                parent.fill(174, 37, 222);
            }
            parent.rect(rectX + 50, rectY + rectH - 35, 40, 30, 5);
            parent.fill(255);
            parent.textSize(35);
            parent.text("<", rectX + 55, rectY + rectH - 10);
        }

        if ((currentPage + 1) * SCROLLS_PER_PAGE < userScrolls.size()) {
            if (isMouseOverButton(rectX + rectW - 90, rectY + rectH - 35, 40, 30)) {
                parent.fill(200, 50, 250);
            } else {
                parent.fill(174, 37, 222);
            }
            parent.rect(rectX + rectW - 90, rectY + rectH - 35, 40, 30, 5);
            parent.fill(255);
            parent.textSize(35);
            parent.text(">", rectX + rectW - 83, rectY + rectH - 10);
        }

        //Draw the filter image
        if (isMouseOverButton((float) ((rectW / 14.0) * 13.4), 105, filterImg.width - 50, filterImg.height - 20)) {
            parent.image(filterImgHover, (rectW / 14) * 13, 95);
        } else {
            parent.image(filterImg, (rectW / 14) * 13, 95);
        }

        parent.noStroke();

        // Upload Button
        boolean isHoverUpload = isMouseOverButton(700, 100, 120, 40);
        if (isHoverUpload) {
            parent.fill(174,37,222,200);
        } else {
            parent.fill(174,37,222);
        }
        parent.noStroke();
        parent.rect(700, 100, 120, 40, 10);
        parent.fill(255);
        parent.textSize(16);
        parent.text("Upload Scroll", 710, 125);

    }

    private boolean isMouseOverButton(int x, int y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    private boolean isMouseOverButton(float x, int y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    private boolean isMouseOverButton(float x, float y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    // Method to handle mouse presses
    public void mousePressed() {
        if (isMouseOverButton(700, 100, 120, 40)) {
            System.out.println("Upload Scroll Selected");
            uploadScroll.isUploadScreenVisible = true;
            uploadScroll.mousePressed();
            uploadScroll.isUploaded = false;
        }

        if (isMouseOverButton((int) rectX + viewScrollsUsers.loginScreen.getEnteredUsername().length() + 40, 25, 100, 40)) {
            System.out.println("Edit Profile Selected");
            editUserScreen.isEditProfileScreenVisible = true;
            parent.redraw();
            editUserScreen.mousePressed();
        }

        if (isMouseOverButton((int) rectX + viewScrollsUsers.loginScreen.getEnteredUsername().length() + 40 + 120, 25, 100, 40)) {
            System.out.println("Home Page Selected");
            isUserProfileVisible = false;
            viewScrollsUsers.loginScreen.isViewScrollsUserVisible = true;

            viewScrollsUsers.updateScrolls(viewScrollsUsers.scrollDb.getAllScrolls());
            parent.redraw();
            viewScrollsUsers.mousePressed();
        }

        // Next button
        if (isMouseOverButton(rectX + rectW - 90, rectY + rectH - 35, 40, 30)) {
            if ((currentPage + 1) * SCROLLS_PER_PAGE < userScrolls.size()) {
                currentPage++;
                refreshView();
            }
        }

        // Previous button
        if (isMouseOverButton(rectX + 50, rectY + rectH - 35, 40, 30)) {
            if (currentPage > 0) {
                currentPage--;
                refreshView();
            }
        }

        // Delete button 
        rectY1 = (float) height / 2 - rectH / 2 + 30;  
        int start = currentPage * SCROLLS_PER_PAGE;
        int end = Math.min(start + SCROLLS_PER_PAGE, userScrolls.size());

        for (int i = start; i < end; i++) {
            if (isMouseOverButton((int) rectX + 784, (int) rectY1 + 70, deleteImg.width, deleteImg.height)) {
                System.out.println("Delete Scroll : " + userScrolls.get(i).get("scrollName"));
                
                // scrollDatabase.deleteRowById((int) userScrolls.get(i).get("scrollId"));
                deleteScreen.showDeleteConfirmation((int) userScrolls.get(i).get("scrollId")); 

                userScrolls = database.searchScrollsByUserId(loginDatabase.getUserIdByUsername(username));
                refreshView();  
                return;
            }

            if (isMouseOverButton(rectX + 690, rectY1 + 70, 90, 40)) {
                System.out.println("Update Scroll : " + userScrolls.get(i).get("scrollName"));

                // scrollDatabase.deleteRowById((int) userScrolls.get(i).get("scrollId"));
                editScroll.showUpdateConfirmation((int) userScrolls.get(i).get("scrollId"));

                userScrolls = database.searchScrollsByUserId(loginDatabase.getUserIdByUsername(username));
                refreshView();
                return;
            }

            rectY1 += rectHeight + 20;
        }

    }

    // Refresh the view with the updated list of scrolls
    private void refreshView() {
        parent.clear(); // Clear the existing content
        drawUserProfile();  // Draw the updated scrolls list
        parent.redraw(); // Redraw
    }

    public String getUsername() {
        return username;
    }
}
