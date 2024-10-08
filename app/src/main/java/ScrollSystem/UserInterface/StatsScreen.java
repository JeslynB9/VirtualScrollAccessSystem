package ScrollSystem.UserInterface;

import processing.core.PApplet;

public class StatsScreen {
    PApplet parent;
    public boolean isStatsScreenVisible = false;
    float shadowOffset = 8;
    ViewScrollsAdmin viewScrollsAdmin;

    public StatsScreen(PApplet parent, ViewScrollsAdmin viewScrollsAdmin) {
        this.parent = parent;
        this.viewScrollsAdmin = viewScrollsAdmin;
    }

    public void drawStats() {
        if (!isStatsScreenVisible) return;

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
        parent.text("[Scroll Title] Scroll", 400, 85);

        // Scroll Preview Rectangle
        parent.noFill();
        parent.stroke(92,86,93);
        parent.rect(200, 100, 330, 370, 10);

        // Scroll Details
        parent.rect(560, 100, 200, 40, 10);
        parent.fill(92,86,93);
        parent.textSize(16);
        parent.text("Downloads:", 570, 125);

        parent.noFill();
        parent.rect(560, 160, 200, 40, 10);
        parent.fill(92,86,93);
        parent.text("Uploads:", 570, 185);

        parent.noFill();
        parent.rect(560, 220, 200, 40, 10);
        parent.fill(92,86,93);
        parent.text("Views:", 570, 245);

        parent.noFill();

        // Close Button
        boolean isHoverDownload = isMouseOverButton(610, 280, 100, 40);
        if (isHoverDownload) {
            parent.fill(174,37,222, 200);
        } else {
            parent.fill(174,37,222);
        }
        parent.noStroke();
        parent.rect(610, 280, 100, 40, 10);
        parent.fill(255);
        parent.text("Close", 640, 305);
    }

    private boolean isMouseOverButton(int x, int y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    public void mousePressed() {
        if (isMouseOverButton(610, 280, 100, 40)) {
            isStatsScreenVisible = false;
        }
    }


}
