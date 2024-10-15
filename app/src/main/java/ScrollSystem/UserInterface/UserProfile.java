package ScrollSystem.UserInterface;

import processing.core.PApplet;
import processing.core.PImage;

public class UserProfile {
    PApplet parent;
    PImage scrollsImg;
    PImage filterImg, filterImgHover;
    PImage downloadImg;
    ViewScrollsUsers viewScrollsUsers;
    public EditUserScreen editUserScreen;
    public UploadScroll uploadScroll;
    String username;
    static int width = 1920 / 2;
    static int height = 1080 / 2;
    float rectW = width - 100;
    float rectH = (float) (height/3)*2;
    float cornerRadius = 10;
    float rectX;
    float rectY;
    public boolean isUserProfileVisible = false;

    // Draw the shadow all around (slightly larger than the rectangle)
    float shadowOffset = 8;

    // Constructor receives the PApplet instance
    public UserProfile(PApplet parent, ViewScrollsUsers viewScrollsUsers) {
        this.parent = parent;
        this.viewScrollsUsers = viewScrollsUsers;

        uploadScroll = new UploadScroll(parent, this);
        editUserScreen = new EditUserScreen(parent, this, viewScrollsUsers.getUserObj());

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
        parent.stroke(92,86,93);
        parent.strokeWeight(2);
        parent.noFill();

        // Title Field
        parent.rect(rectX + 40, rectY + 80, 160, 40);
        parent.fill(92,86,93);
        parent.text("[Title]", rectX + 50, rectY + 105);

        // Author Field
        parent.noFill();
        parent.rect(rectX + 200, rectY + 80, 160, 40);
        parent.fill(92,86,93);
        parent.text("[Author]", rectX + 210, rectY + 105);

        // Upload Date Field
        parent.noFill();
        parent.rect(rectX + 360, rectY + 80, 210, 40);
        parent.fill(92,86,93);
        parent.text("Upload Date:", rectX + 370, rectY + 105);

        // Last Update Field
        parent.noFill();
        parent.rect(rectX + 570, rectY + 80, 210, 40);
        parent.fill(92,86,93);
        parent.text("Last Update:", rectX + 580, rectY + 105);

        // Download Field
        if (isMouseOverButton((int) rectX + 768, (int) rectY + 83, downloadImg.width, downloadImg.height)) {
            parent.fill(216,202,220, 200);
        } else  {
            parent.noFill();
        }
        parent.rect(rectX + 780, rectY + 80, 40, 40);
        parent.image(downloadImg,rectX + 768, rectY + 83);

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
    // Method to handle mouse presses
    public void mousePressed() {
        if (isMouseOverButton(700, 100, 120, 40)) {
            System.out.println("Upload Scroll Selected");
            uploadScroll.isUploadScreenVisible = true;
            uploadScroll.mousePressed();
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
            parent.redraw();
            viewScrollsUsers.mousePressed();
        }

    }

    public String getUsername() {
        return username;
    }
}
