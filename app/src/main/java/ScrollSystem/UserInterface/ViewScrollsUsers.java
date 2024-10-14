package ScrollSystem.UserInterface;

import ScrollSystem.FileHandlers.ScrollDatabase;
import ScrollSystem.Users.User;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.List;
import java.util.Map;

public class ViewScrollsUsers {
    PApplet parent;
    PImage scrollsImg;
    PImage filterImg, filterImgHover;
    PImage downloadImg;
    String username;
    public FilterScreen filterScreen;
    public PreviewScreen previewScreen;
    public UserProfile userProfile;
    LoginScreen loginScreen;
    ScrollDatabase scrollDb;
    List<Map<String, String>> scrolls;
    private User currentUser;
    static int width = 1920 / 2;
    static int height = 1080 / 2;
    float rectW = width - 100;
    float rectH = (float) (height/3)*2;
    float cornerRadius = 10;
    float rectX;
    float rectY;
    float rectY1;
    float rectHeight = 40;

    // Draw the shadow all around (slightly larger than the rectangle)
    float shadowOffset = 8;

    String title;
    String author;
    String uploadDate;
    String lastUpdate;
    String scrollId;


    // Constructor receives the PApplet instance
    public ViewScrollsUsers(PApplet parent, LoginScreen loginScreen) {
        this.parent = parent;
        this.loginScreen = loginScreen;

        updateCurrentUser(loginScreen.getEnteredUsername());

        filterScreen = new FilterScreen(parent, this);
        previewScreen = new PreviewScreen(parent, this);
        userProfile = new UserProfile(parent, this);

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

        scrollDb = new ScrollDatabase("src/main/java/ScrollSystem/Databases/database.db");
        scrolls = scrollDb.getAllScrolls();
    }

    public void drawScrollsUsers() {

        parent.redraw();
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
        parent.image(scrollsImg, 100, 110);
        parent.text("Scrolls", 145, 127);

        parent.image(filterImg,(rectW/14)*13, 95);

        // User details
        username = loginScreen.getEnteredUsername();
        if (isMouseOverButton((int) rectX, 30, (int)parent.textWidth(username),10)) {
            parent.fill(174,37,222);
        } else {
            parent.fill(253, 249, 255);
        }
        parent.text(username, rectX, 40);

        parent.fill(253, 249, 255);
        parent.text("User", rectX, 60);


        // --------------------------- SCROLLS ---------------------------

        drawScrolls();

    }

    public void drawScrolls() {

        // parent.noLoop();
        parent.fill(92, 86, 93);
        parent.text("Title", rectX + 50, rectY + 95);
        parent.text("Author", rectX + 210, rectY + 95);
        parent.text("Upload Date", rectX + 370, rectY + 95);
        parent.text("Last Updated", rectX + 600, rectY + 95);
        rectY1 = rectY;

        for (Map<String, String> scroll : scrolls) {
            title = scroll.get("name");
            author = scroll.get("author");
            uploadDate = scroll.get("publishDate");
            lastUpdate = scroll.get("lastUpdate");
            scrollId = scroll.get("ID");

            // Draw box for scroll information
            parent.stroke(92, 86, 93);
            parent.strokeWeight(2);
            parent.noFill();

            // Title Field
            parent.rect(rectX + 40, rectY1 + 100, 160, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(title, rectX + 50, rectY1 + 125);

            // Author Field
            parent.noFill();
            parent.rect(rectX + 200, rectY1 + 100, 160, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(author, rectX + 210, rectY1 + 125);

            // Upload Date Field
            parent.noFill();
            parent.rect(rectX + 360, rectY1 + 100, 230, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(uploadDate, rectX + 370, rectY1 + 125);

            // Last Update Field
            parent.noFill();
            parent.rect(rectX + 590, rectY1 + 100, 230, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(lastUpdate, rectX + 600, rectY1 + 125);

            // Download Field
            if (isMouseOverButton((int) rectX + 768, (int) rectY1 + 103, downloadImg.width, downloadImg.height)) {
                parent.fill(216, 202, 220, 200);
           } else {
                parent.noFill();
            }
            parent.rect(rectX + 780, rectY1 + 100, 40, 40);
            parent.image(downloadImg, rectX + 768, rectY1 + 103);

            //Draw the filter image
            //Draw the filter image
            if (isMouseOverButton((float) ((rectW / 14.0) * 13.4), 105, filterImg.width - 50, filterImg.height - 20)) {
                parent.image(filterImgHover, (rectW / 14) * 13, 95);  
            } else {
                parent.image(filterImg, (rectW / 14) * 13, 95);  
            }
            
            // Update Y position for the next scroll
            rectY1 += rectHeight + 20; // Move down for the next box (adjust spacing as needed)

        }
    }

    private boolean isMouseOverButton(int x, int y, int w, int h) {
        parent.redraw();
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    private boolean isMouseOverButton(float x, int y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    // Method to handle mouse presses
    public void mousePressed() {
        if (isMouseOverButton((int) (rectW / 14) * 13, 95, filterImg.width, filterImg.height)) {
            System.out.println("Filter Selected");
            parent.redraw();
            filterScreen.isFilterScreenVisible = true;
            filterScreen.mousePressed();

        }

        // Check which scroll's download button is clicked
        for (int i = 0; i < scrolls.size(); i++) {
            float downloadX = rectX + 780;
            float downloadY = rectY + 100 + (i * (rectHeight + 20));

            if (isMouseOverButton((int) downloadX, (int) downloadY, downloadImg.width, downloadImg.height)) {
                Map<String, String> selectedScroll = scrolls.get(i); // Get the selected scroll details
                String scrollId = selectedScroll.get("ID");
                String title = selectedScroll.get("name");
                String author = selectedScroll.get("author");
                String uploadDate = selectedScroll.get("publishDate");

                System.out.println("Download Selected for scroll: " + title);

                previewScreen.setScrollDetails(scrollId, title, author, uploadDate);
                previewScreen.isPreviewScreenVisible = true; // Show the preview screen
                parent.redraw();
                previewScreen.mousePressed();
                parent.redraw();
            }
        }

        if (username != null && isMouseOverButton((int) rectX, 30, (int) parent.textWidth(username), 10)) {
            System.out.println("User Profile Selected");
            userProfile.isUserProfileVisible = true;
            parent.redraw();
            loginScreen.isViewScrollsUserVisible = false;
            userProfile.mousePressed();
        }
    }

    // Method to update the current user
    public void updateCurrentUser(String username) {
        if (currentUser == null) {
            currentUser = new User();
        } else {
            currentUser.setUsername(username);
        }
    }

    // Assuming this method is called when a scroll is selected
    public void onScrollSelected(String scrollId, String title, String author, String uploadDate) {
        // Assuming you have an instance of PreviewScreen named previewScreen
        previewScreen.setScrollDetails(scrollId, title, author, uploadDate);
        previewScreen.isPreviewScreenVisible = true; // Show the preview screen
        parent.redraw(); // Redraw the parent to reflect changes
    }

}
