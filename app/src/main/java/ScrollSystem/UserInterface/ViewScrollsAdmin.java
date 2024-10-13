package ScrollSystem.UserInterface;

import ScrollSystem.FileHandlers.ScrollDatabase;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.List;
import java.util.Map;

public class ViewScrollsAdmin {
    PApplet parent;
    PImage scrollsImg;
    PImage filterImg;
    PImage downloadImg;
    PImage statsImg;
    public FilterScreen filterScreen;
    public PreviewScreen previewScreen;
    public StatsScreen statsScreen;
    public LoginScreen loginScreen;
    ScrollDatabase scrollDb;
    List<Map<String, String>> scrolls;
    static int width = 1920 / 2;
    static int height = 1080 / 2;
    float rectW = width - 100;
    float rectH = (float) (height/3)*2;
    float cornerRadius = 10;
    float rectX;
    float rectY;
    float rectHeight = 40;

//    // Canvas center
//    int centerX = width / 2;
//    int centerY = height / 2;
//
//    // Shadow offset
//    float shadowOffsetX = 10;
//    float shadowOffsetY = 10;

    // Draw the shadow all around (slightly larger than the rectangle)
    float shadowOffset = 8;

    // Constructor receives the PApplet instance
    public ViewScrollsAdmin(PApplet parent, LoginScreen loginScreen) {
        this.parent = parent;
        this.loginScreen = loginScreen;

        filterScreen = new FilterScreen(parent, this);
        previewScreen = new PreviewScreen(parent, this);
        statsScreen = new StatsScreen(parent, this);

        // Calculate the rectangle's top-left corner based on the center
        rectX = (float) width / 2 - rectW / 2;
        rectY = (float) height / 2 - rectH / 2;

        scrollsImg = parent.loadImage("src/main/resources/scrolls.png");
        scrollsImg.resize(1920 / 40, 1080 / 40);

        filterImg = parent.loadImage("src/main/resources/filter.png");
        filterImg.resize(1920 / 20, 1080 / 20);

        downloadImg = parent.loadImage("src/main/resources/download.png");
        downloadImg.resize(1920 / 30, 1080 / 30);

        statsImg = parent.loadImage("src/main/resources/stats.png");
        statsImg.resize(1920 / 30, 1080 / 30);

        scrollDb = new ScrollDatabase("src/main/java/ScrollSystem/Databases/database.db");
        scrolls = scrollDb.getAllScrolls();
    }

    public void drawScrollsAdmin() {

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
        parent.fill(253,249,255);
        String username = loginScreen.getEnteredUsername();
        parent.text(username, rectX, 40);
        parent.text("Admin", rectX, 60);


        // --------------------------- SCROLLS ---------------------------
        drawScrolls();
//        parent.stroke(92,86,93);
//        parent.strokeWeight(2);
//        parent.noFill();
//
//        // Title Field
//        parent.rect(rectX + 40, rectY + 80, 160, 40);
//        parent.fill(92,86,93);
//        parent.text("[Title]", rectX + 50, rectY + 105);
//
//        // Author Field
//        parent.noFill();
//        parent.rect(rectX + 200, rectY + 80, 160, 40);
//        parent.fill(92,86,93);
//        parent.text("[Author]", rectX + 210, rectY + 105);
//
//        // Upload Date Field
//        parent.noFill();
//        parent.rect(rectX + 360, rectY + 80, 190, 40);
//        parent.fill(92,86,93);
//        parent.text("Upload Date:", rectX + 370, rectY + 105);
//
//        // Last Update Field
//        parent.noFill();
//        parent.rect(rectX + 550, rectY + 80, 190, 40);
//        parent.fill(92,86,93);
//        parent.text("Last Update:", rectX + 560, rectY + 105);
//
//        // Download Field
//        if (isMouseOverButton((int) rectX + 740, (int) rectY + 83, 40, 40)) {
//            parent.fill(216,202,220, 200);
//        } else  {
//            parent.noFill();
//        }
//        parent.rect(rectX + 740, rectY + 80, 40, 40);
//        parent.image(downloadImg,rectX + 728, rectY + 83);
//
//        // Stats Field
//        if (isMouseOverButton((int) rectX + 780, (int) rectY + 83, 40, 40)) {
//            parent.fill(216,202,220, 200);
//        } else  {
//            parent.noFill();
//        }
//        parent.rect(rectX + 780, rectY + 80, 40, 40);
//        parent.image(statsImg,rectX + 768, rectY + 83);
//
//        parent.noStroke();

    }

    public void drawScrolls() {

        parent.noLoop();
        parent.fill(92, 86, 93);
        parent.text("Title", rectX + 50, rectY + 95);
        parent.text("Author", rectX + 210, rectY + 95);
        parent.text("Upload Date", rectX + 370, rectY + 95);
        parent.text("Last Updated", rectX + 560, rectY + 95);

        for (Map<String, String> scroll : scrolls) {
            String title = scroll.get("name"); // Adjust the key name according to your database schema
            String author = scroll.get("author");
            String uploadDate = scroll.get("publishDate");
            String lastUpdate = scroll.get("lastUpdate");

            // Draw box for scroll information
            parent.stroke(92, 86, 93);
            parent.strokeWeight(2);
            parent.noFill();

            // Title Field
            parent.rect(rectX + 40, rectY + 100, 160, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(title, rectX + 50, rectY + 125);

            // Author Field
            parent.noFill();
            parent.rect(rectX + 200, rectY + 100, 160, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(author, rectX + 210, rectY + 125);

            // Upload Date Field
            parent.noFill();
            parent.rect(rectX + 360, rectY + 100, 190, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(uploadDate, rectX + 370, rectY + 125);

            // Last Update Field
            parent.noFill();
            parent.rect(rectX + 550, rectY + 100, 190, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(lastUpdate, rectX + 560, rectY + 125);

            // Download Field
            if (isMouseOverButton((int) rectX + 740, (int) rectY + 103, downloadImg.width, downloadImg.height)) {
                parent.fill(216, 202, 220, 200);
            } else {
                parent.noFill();
            }
            parent.rect(rectX + 740, rectY + 100, 40, 40);
            parent.image(downloadImg, rectX + 728, rectY + 103);

            // Stats Field
            if (isMouseOverButton((int) rectX + 780, (int) rectY + 103, 40, 40)) {
                parent.fill(216,202,220, 200);
            } else  {
                parent.noFill();
            }
            parent.rect(rectX + 780, rectY + 100, 40, 40);
            parent.image(statsImg,rectX + 768, rectY + 103);

            parent.noStroke();

            // Update Y position for the next scroll
            rectY += rectHeight + 20; // Move down for the next box (adjust spacing as needed)

        }
    }

    private boolean isMouseOverButton(int x, int y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    // Method to handle mouse presses
    public void mousePressed() {
        if (isMouseOverButton((int)(rectW/14)*13, 95, filterImg.width, filterImg.height)) {
            System.out.println("Filter Selected");
            filterScreen.isFilterScreenVisible = true;
            filterScreen.mousePressed();

        }

        if (isMouseOverButton((int) rectX + 740, (int) rectY + 83, 40, 40)) {
            System.out.println("Preview Selected");
            previewScreen.isPreviewScreenVisible = true;
            previewScreen.mousePressed();
        }

        if (isMouseOverButton((int) rectX + 780, (int) rectY + 83, 40, 40)) {
            System.out.println("Stats Selected");
            statsScreen.isStatsScreenVisible = true;
            statsScreen.mousePressed();
        }
    }
}
