package ScrollSystem.UserInterface;

import ScrollSystem.FileHandlers.FilterScroll;
import processing.core.PApplet;

public class ParsingScreen {
    PApplet parent;
    PreviewScreen previewScreen;
    public boolean isParsingScreenVisible = false;
    float shadowOffset = 8;



    public ParsingScreen(PApplet parent, PreviewScreen previewScreen) {
        this.parent = parent;
        this.previewScreen = previewScreen;
    }

    public void drawParsing() {
        if (!isParsingScreenVisible) return;

        // Background Overlay
        parent.fill(0, 0, 0, 150);
        parent.rect(0, 0, parent.width*2, parent.height);

        // Shadow properties
        parent.fill(0, 0, 0, 50);
        parent.noStroke();
        parent.rect(parent.width / 2 - 300 - shadowOffset, parent.height / 2 - 125 - shadowOffset, 600 + 2 * shadowOffset, 250 + 2 * shadowOffset, 15);

        // White Register Box
        parent.fill(255,249,254);
        parent.stroke(200);
        parent.rect(parent.width / 2 - 300, parent.height / 2 - 125, 600, 250, 10);

        // Title
        parent.fill(0);
        parent.textSize(24);
        parent.text("Parse Scroll", 410, 180);

        // Scroll Preview Rectangle
        parent.noFill();
        parent.stroke(92,86,93);
        parent.rect(200, 200, 555, 60, 10);

        // Line Details
        parent.rect(200, 280, 200, 40, 10);
        parent.fill(92,86,93);
        parent.textSize(16);
        parent.text("Line Number", 210, 305);

        parent.noFill();

        // Previous Button
        boolean isHoverPrevious = isMouseOverButton(440, 280, 100, 40);
        if (isHoverPrevious) {
            parent.fill(174,37,222, 200);
        } else {
            parent.fill(174,37,222);
        }
        parent.noStroke();
        parent.rect(440, 280, 100, 40, 10);
        parent.fill(255);
        parent.text("Previous", 458, 305);

        // Next Button
        boolean isHoverNext = isMouseOverButton(580, 280, 100, 40);
        if (isHoverNext) {
            parent.fill(174,37,222, 200);
        } else {
            parent.fill(174,37,222);
        }
        parent.noStroke();
        parent.rect(580, 280, 100, 40, 10);
        parent.fill(255);
        parent.text("Next", 613, 305);

        // Close Button
        boolean isHoverClose = isMouseOverButton(655, 340, 100, 40);
        if (isHoverClose) {
            parent.fill(174,37,222, 200);
        } else {
            parent.fill(174,37,222);
        }
        parent.noStroke();
        parent.rect(655, 340, 100, 40, 10);
        parent.fill(255);
        parent.text("Close", 683, 365);
    }

    private boolean isMouseOverButton(int x, int y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    public void mousePressed() {
        if (isMouseOverButton(655, 340, 100, 40)) {
            isParsingScreenVisible = false;
        }
    }


}
