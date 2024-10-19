package ScrollSystem.UserInterface;

import java.util.List;
import java.util.Map;

import ScrollSystem.FileHandlers.ScrollDatabase;
import processing.core.PApplet;
import processing.core.PImage;

public class ViewScrollsGuest {
    PApplet parent;
    PImage scrollImg;
    PImage filterImg, filterImgHover;
    public FilterScreen filterScreen;
    ScrollDatabase scrollDb;
    List<Map<String, String>> scrolls;
    static int width = 1920 / 2;
    static int height = 1080 / 2;
    float rectW = width - 100;
    float rectH = (float) (height/3)*2;
    float cornerRadius = 10;
    float rectX;
    float rectY;
    float rectY1;
    float rectHeight = 40;

    private int currentPage = 0;
    private static final int SCROLLS_PER_PAGE = 4; 

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
    public ViewScrollsGuest(PApplet parent) {
        this.parent = parent;

        filterScreen = new FilterScreen(parent, this);

        // Calculate the rectangle's top-left corner based on the center
        rectX = (float) width / 2 - rectW / 2;
        rectY = (float) height / 2 - rectH / 2;

        scrollImg = parent.loadImage("src/main/resources/scrolls.png");
        scrollImg.resize(1920 / 40, 1080 / 40);

        filterImg = parent.loadImage("src/main/resources/filter.png");
        filterImg.resize(1920 / 20, 1080 / 20);

        filterImgHover = parent.loadImage("src/main/resources/filter_hover.png");  
        filterImgHover.resize(1920 / 20, 1080 / 20);

        scrollDb = new ScrollDatabase("src/main/java/ScrollSystem/Databases/database.db");
        scrolls = scrollDb.getAllScrolls();

        System.out.println(scrolls);
    }

    public void drawScrollsGuest() {

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
        parent.image(scrollImg, 100, 110);
        parent.text("Scrolls", 145, 127);

        parent.image(filterImg,(rectW/14)*13, 95);

        // User details
        parent.fill(253,249,255);
        parent.text("Guest User", rectX, 40);
        parent.text("Guest", rectX, 60);


        // --------------------------- SCROLLS ---------------------------

        parent.fill(92, 86, 93);
        parent.text("Title", rectX + 50, rectY + 95);
        parent.text("Author", rectX + 210, rectY + 95);
        parent.text("Upload Date", rectX + 370, rectY + 95);
        parent.text("Last Updated", rectX + 600, rectY + 95);
        rectY1 = rectY;

        int start = currentPage * SCROLLS_PER_PAGE;
        int end = Math.min(start + SCROLLS_PER_PAGE, scrolls.size());

        for (int i = start; i < end; i++) {
            Map<String, String> scroll = scrolls.get(i);
            String title = scroll.get("name");
            String author = scroll.get("author");
            String uploadDate = scroll.get("publishDate");
            String lastUpdate = scroll.get("lastUpdate");

            // Draw scroll details...
            parent.stroke(92, 86, 93);
            parent.strokeWeight(2);
            parent.noFill();

            parent.rect(rectX + 40, rectY1 + 100, 160, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(title, rectX + 50, rectY1 + 125);

            parent.noFill();
            parent.rect(rectX + 200, rectY1 + 100, 160, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(author, rectX + 210, rectY1 + 125);

            parent.noFill();
            parent.rect(rectX + 360, rectY1 + 100, 230, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(uploadDate, rectX + 370, rectY1 + 125);

            parent.noFill();
            parent.rect(rectX + 590, rectY1 + 100, 230, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(lastUpdate, rectX + 600, rectY1 + 125);

            rectY1 += rectHeight + 20;
        }

        //Draw the filter image
        if (isMouseOverButton((float) ((rectW / 14.0) * 13.4), 105, filterImg.width - 50, filterImg.height - 20)) {
            parent.image(filterImgHover, (rectW / 14) * 13, 95);
        } else {
            parent.image(filterImg, (rectW / 14) * 13, 95);
        }

        // Previous button
        if (currentPage > 0) {
            if (isMouseOverButton(rectX + 50, rectY + rectH - 35, 40, 30)) {
                parent.fill(200, 50, 250);
            } else {
                parent.fill(174,37,222);
            }
            parent.rect(rectX + 50, rectY + rectH - 35, 40, 30, 5);
            parent.fill(255);
            parent.textSize(35);
            parent.text("<", rectX + 60, rectY + rectH - 10); 
        }
        // Next button
        if ((currentPage + 1) * SCROLLS_PER_PAGE < scrolls.size()) {
            if (isMouseOverButton(rectX + rectW - 90, rectY + rectH - 35, 40, 30)) {
                parent.fill(200, 50, 250);
            } else {
                parent.fill(174,37,222);
            }
            parent.rect(rectX + rectW - 90, rectY + rectH - 35, 40, 30, 5);
            parent.fill(255);
            parent.textSize(35);
            parent.text(">", rectX + rectW - 80, rectY + rectH - 10); 
        }
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
        if (isMouseOverButton((int)(rectW/14)*13, 95, filterImg.width, filterImg.height)) {
            System.out.println("Filter Selected");
            filterScreen.isFilterScreenVisible = true;
            filterScreen.mousePressed();
        }

        //Next 
        if (isMouseOverButton(rectX + rectW - 90, rectY + rectH - 35, 40, 30)) {
            if ((currentPage + 1) * SCROLLS_PER_PAGE < scrolls.size()) {
                currentPage++; 
                refreshView(); 
            }
        }

        //Previous
        if (isMouseOverButton(rectX + 50, rectY + rectH - 35, 40, 30)) {
            if (currentPage > 0) {
                currentPage--; 
                refreshView(); 
            }
        }
    }

    public void updateScrolls(List<Map<String, String>> filteredScrolls) {
        this.scrolls = filteredScrolls;
        refreshView();
    }

    // Refresh the view with the updated list of scrolls
    private void refreshView() {
        parent.clear(); // Clear the existing content
        drawScrollsGuest();  // Draw the updated scrolls list
        parent.redraw(); // Redraw
    }

}
