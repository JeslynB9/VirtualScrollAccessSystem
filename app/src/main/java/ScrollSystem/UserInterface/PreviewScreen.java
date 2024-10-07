package ScrollSystem.UserInterface;

import processing.core.PApplet;

public class PreviewScreen {
    PApplet parent;
    public boolean isPreviewScreenVisible = false;
    float shadowOffset = 8;
    ViewScrollsUsers viewScrollsUsers;


    public PreviewScreen(PApplet parent, ViewScrollsUsers viewScrollsUsers) {
        this.parent = parent;
        this.viewScrollsUsers = viewScrollsUsers;
    }

    public void drawPreview() {
        if (!isPreviewScreenVisible) return;

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
        parent.text("Preview Scroll", 400, 85);

        // Scroll Preview Rectangle
        parent.noFill();
        parent.stroke(92,86,93);
        parent.rect(200, 100, 330, 370, 10);

        // Scroll Details
        parent.rect(560, 100, 200, 40, 10);
        parent.fill(92,86,93);
        parent.textSize(16);
        parent.text("Scroll ID:", 570, 125);

        parent.noFill();
        parent.rect(560, 160, 200, 40, 10);
        parent.fill(92,86,93);
        parent.text("Title:", 570, 185);

        parent.noFill();
        parent.rect(560, 220, 200, 40, 10);
        parent.fill(92,86,93);
        parent.text("Author:", 570, 245);

        parent.noFill();
        parent.rect(560, 280, 200, 40, 10);
        parent.fill(92,86,93);
        parent.text("Upload Date:", 570, 305);

        parent.noFill();

        // Download Button
        boolean isHoverDownload = isMouseOverButton(610, 340, 100, 40);
        if (isHoverDownload) {
            parent.fill(174,37,222, 200);
        } else {
            parent.fill(174,37,222);
        }
        parent.noStroke();
        parent.rect(610, 340, 100, 40, 10);
        parent.fill(255);
        parent.text("Download", 622, 365);

        // Cancel Button
        boolean isHoverCancel = isMouseOverButton(610, 400, 100, 40);
        if (isHoverCancel) {
            parent.fill(174,37,222, 200);
        } else {
            parent.fill(174,37,222);
        }
        parent.noStroke();
        parent.rect(610, 400, 100, 40, 10);
        parent.fill(255);
        parent.text("Cancel", 635, 425);
    }

    private boolean isMouseOverButton(int x, int y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    public void mousePressed() {
        if (isMouseOverButton(610, 400, 100, 40)) {
            isPreviewScreenVisible = false;
        }
    }


}
