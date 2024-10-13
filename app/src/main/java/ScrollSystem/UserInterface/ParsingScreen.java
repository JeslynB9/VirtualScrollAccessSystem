package ScrollSystem.UserInterface;

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
        parent.rect(parent.width / 2 - 300 - shadowOffset, parent.height / 2 - 225 - shadowOffset, 600 + 2 * shadowOffset, 450 + 2 * shadowOffset, 15);

        // White Register Box
        parent.fill(255,249,254);
        parent.stroke(200);
        parent.rect(parent.width / 2 - 300, parent.height / 2 - 225, 600, 450, 10);

        // Title
        parent.fill(0);
        parent.textSize(24);
        parent.text("Parse Scroll", 400, 85);

        // Scroll Preview Rectangle
        parent.noFill();
        parent.stroke(92,86,93);
        parent.rect(200, 100, 330, 370, 10);

        // Line Details
        parent.rect(560, 100, 200, 40, 10);
        parent.fill(92,86,93);
        parent.textSize(16);
        parent.text("Line Number:", 570, 125);

        parent.noFill();

    }

    private boolean isMouseOverButton(int x, int y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    public void mousePressed() {

    }


}
