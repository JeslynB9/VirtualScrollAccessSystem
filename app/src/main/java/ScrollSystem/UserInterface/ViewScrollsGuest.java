package ScrollSystem.UserInterface;

import processing.core.PApplet;
import processing.core.PImage;

public class ViewScrollsGuest {
    PApplet parent;
    PImage scrollImg;
    PImage filterImg;
    public FilterScreen filterScreen;
    static int width = 1920 / 2;
    static int height = 1080 / 2;
    float rectW = width - 100;
    float rectH = (float) (height/3)*2;
    float cornerRadius = 10;
    float rectX;
    float rectY;

    // Canvas center
    int centerX = width / 2;
    int centerY = height / 2;

    // Shadow offset
    float shadowOffsetX = 10;
    float shadowOffsetY = 10;

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
    }

    public void drawScrollsGuest() {

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
        parent.rect(rectX + 360, rectY + 80, 230, 40);
        parent.fill(92,86,93);
        parent.text("Upload Date:", rectX + 370, rectY + 105);

        // Last Update Field
        parent.noFill();
        parent.rect(rectX + 590, rectY + 80, 230, 40);
        parent.fill(92,86,93);
        parent.text("Last Update:", rectX + 600, rectY + 105);

        // Title Field
        parent.noFill();
        parent.rect(rectX + 40, rectY + 140, 160, 40);
        parent.fill(92,86,93);
        parent.text("[Title]", rectX + 50, rectY + 165);

        // Author Field
        parent.noFill();
        parent.rect(rectX + 200, rectY + 140, 160, 40);
        parent.fill(92,86,93);
        parent.text("[Author]", rectX + 210, rectY + 165);

        // Upload Date Field
        parent.noFill();
        parent.rect(rectX + 360, rectY + 140, 230, 40);
        parent.fill(92,86,93);
        parent.text("Upload Date:", rectX + 370, rectY + 165);

        // Last Update Field
        parent.noFill();
        parent.rect(rectX + 590, rectY + 140, 230, 40);
        parent.fill(92,86,93);
        parent.text("Last Update:", rectX + 600, rectY + 165);

        parent.noStroke();

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
    }
}
