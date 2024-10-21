package ScrollSystem.UserInterface;

import ScrollSystem.FileHandlers.ScrollDatabase;
import ScrollSystem.Users.User;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.List;
import java.util.Map;

import com.mysql.cj.x.protobuf.MysqlxCrud.Delete;

public class ViewScrollsUsers {
    PApplet parent;
    PImage scrollsImg;
    PImage filterImg, filterImgHover;
    PImage downloadImg;
    String username;
    public FilterScreen filterScreen;
    public PreviewScreen previewScreen;
    public UserProfile userProfile;

    UploadScroll uploadScroll;

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
    
    private int currentPage = 0;
    private final int SCROLLS_PER_PAGE = 4;

    // Constructor receives the PApplet instance
    public ViewScrollsUsers(PApplet parent, LoginScreen loginScreen, UploadScroll uploadScroll, DeleteScreen deleteScreen, EditScroll editScroll) {
        this.parent = parent;
        this.loginScreen = loginScreen;

        this.uploadScroll = uploadScroll;

        updateCurrentUser(loginScreen.getEnteredUsername());

        filterScreen = new FilterScreen(parent, this);
        previewScreen = new PreviewScreen(parent, this);
        userProfile = new UserProfile(parent, this, deleteScreen, editScroll);

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

//        if (filterScreen.isFilterScreenVisible) {
//            System.out.println("good");
//        } else {
//            scrolls = scrollDb.getAllScrolls();
//            System.out.println("bad");
//        }
        if (uploadScroll != null && uploadScroll.isUploaded) {
            scrolls = scrollDb.getAllScrolls();
        }

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

        //Draw the filter image
        if (isMouseOverButton((float) ((rectW / 14.0) * 13.4), 105, filterImg.width - 50, filterImg.height - 20)) {
            parent.fill(216, 202, 220, 200);
        } else {
            parent.noFill();
        }
        parent.rect(rectX + 780, rectY1 + 100, 40, 40);
        parent.image(filterImg, (rectW / 14) * 13, 95);

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

        scrolls = scrollDb.getAllScrolls();
        int start = currentPage * SCROLLS_PER_PAGE;
        int end = Math.min(start + SCROLLS_PER_PAGE, scrolls.size());

        for (int i = start; i < end; i++) {
            Map<String, String> scroll = scrolls.get(i);
            String title = scroll.get("name");
            String author = scroll.get("author");
            String uploadDate = scroll.get("publishDate");
            String lastUpdate = scroll.get("lastUpdate");
    
            parent.stroke(92, 86, 93);
            parent.strokeWeight(2);
            parent.noFill();
    
            // Title 
            parent.rect(rectX + 40, rectY1 + 100, 160, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(title, rectX + 50, rectY1 + 125);
    
            // Author 
            parent.noFill();
            parent.rect(rectX + 200, rectY1 + 100, 160, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(author, rectX + 210, rectY1 + 125);
    
            // Upload Date 
            parent.noFill();
            parent.rect(rectX + 360, rectY1 + 100, 230, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(uploadDate, rectX + 370, rectY1 + 125);
    
            // Last Update 
            parent.noFill();
            parent.rect(rectX + 590, rectY1 + 100, 230, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(lastUpdate, rectX + 600, rectY1 + 125);

            // Download Field
            if (isMouseOverButton((int) rectX + 780, (int) rectY1 + 103, 40, 40)) {
                parent.fill(216, 202, 220, 200);
            } else  {
                parent.noFill();
            }
            parent.rect(rectX + 780, rectY1 + 100, 40, 40);
            parent.image(downloadImg, rectX + 768, rectY1 + 103);

            rectY1 += rectHeight + 20; 
        }

        if (currentPage > 0) {
            parent.noStroke();
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

        if ((currentPage + 1) * SCROLLS_PER_PAGE < scrolls.size()) {

            parent.noStroke();
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
    }

    private boolean isMouseOverButton(int x, int y, int w, int h) {
        parent.redraw();
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    private boolean isMouseOverButton(float x, float y, int w, int h) {
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

        // Next button
        if (isMouseOverButton(rectX + rectW - 90, rectY + rectH - 35, 40, 30)) {
            if ((currentPage + 1) * SCROLLS_PER_PAGE < scrolls.size()) {
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

        // Check which scroll's download button is clicked
        for (int i = 0; i < scrolls.size()-1; i++) {
            float downloadX = rectX + 780;
            float downloadY = rectY + 100 + (i * (rectHeight + 20));

            if (isMouseOverButton((int) downloadX, (int) downloadY, downloadImg.width, downloadImg.height)) {
                Map<String, String> selectedScroll = scrolls.get(i); // Get the selected scroll details
                String scrollId = selectedScroll.get("ID");
                String title = selectedScroll.get("name");
                String author = selectedScroll.get("author");
                String uploadDate = selectedScroll.get("publishDate");
                String filePath = selectedScroll.get("filePath");

                System.out.println("Download Selected for scroll: " + title);
                scrollDb.updateNumViews(Integer.parseInt(scrollId));

                previewScreen.setScrollDetails(scrollId, title, author, uploadDate, filePath);
                previewScreen.resetScroll();
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

    public User getUserObj() {
        return loginScreen.getUserObj();
    }

    public void updateScrolls(List<Map<String, String>> filteredScrolls) {
        this.scrolls = filteredScrolls;
        refreshView();
    }

    // Refresh the view with the updated list of scrolls
    private void refreshView() {
        parent.clear(); // Clear the existing content
        drawScrollsUsers();  // Draw the updated scrolls list
        parent.redraw(); // Redraw
    }
}
