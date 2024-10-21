package ScrollSystem.UserInterface;

import ScrollSystem.FileHandlers.ScrollDatabase;
import ScrollSystem.FileHandlers.LoginDatabase;
import processing.core.PApplet;

import java.util.List;
import java.util.Map;

public class ViewUsersDetails {
    PApplet parent;
    String username;

    ViewScrollsAdmin viewScrollsAdmin;
    ScrollDatabase scrollDatabase;
    LoginDatabase loginDatabase;
    AdminProfile adminProfile;

    List<Map<String, String>> userScrolls;
    static int width = 1920 / 2;
    static int height = 1080 / 2;
    float rectW = width - 100;
    float rectH = (float) (height/3)*2;
    float cornerRadius = 10;
    float rectX;
    float rectY;
    float rectY1;
    float rectHeight = 40;
    public boolean isViewUsersDetailsVisible = false;

    private int currentPage = 0;
    private static final int SCROLLS_PER_PAGE = 4;

    // Draw the shadow all around (slightly larger than the rectangle)
    float shadowOffset = 8;

    public ViewUsersDetails(PApplet parent, ViewScrollsAdmin viewScrollsAdmin, AdminProfile adminProfile) {
        this.parent = parent;
        this.viewScrollsAdmin = viewScrollsAdmin;
        this.adminProfile = adminProfile;
        scrollDatabase = viewScrollsAdmin.scrollDb;

        // Calculate the rectangle's top-left corner based on the center
        rectX = (float) width / 2 - rectW / 2;
        rectY = (float) height / 2 - rectH / 2;
    }

    // Method to set the username and fetch corresponding scrolls
    public void setUsername(String username) {
        this.username = username;
        userScrolls = scrollDatabase.getRowsByAuthor(username); // Fetch user's scrolls

        System.out.println("Setting username in ViewUsersDetails: " + username);
        if (userScrolls == null || userScrolls.isEmpty()) {
            System.out.println("No scrolls found for user: " + username);
        } else {
            System.out.println("Scrolls loaded for user: " + username);
        }
    }

    public void drawViewUserDetails() {
        if (!isViewUsersDetailsVisible) return;

        this.username = username;
        userScrolls = scrollDatabase.getRowsByAuthor(username);

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

        // Title text
        parent.fill(174, 37, 222);
        parent.textSize(16);
        parent.text("User Scrolls", 135, 127);

        // Back Button
        boolean isHoverBack = isMouseOverButton(730, 100, 120, 40);
        if (isHoverBack) {
            parent.fill(174, 37, 222, 200);
        } else {
            parent.fill(174, 37, 222);
        }
        parent.noStroke();
        parent.rect(730, 100, 120, 40, 10);
        parent.fill(255);
        parent.textSize(16);
        parent.text("Back", 775, 125);

        // Draw scroll details
        drawScrolls();
    }

    public void drawScrolls() {
        // Header for the scroll details
        parent.fill(92, 86, 93);
        parent.text("Scroll ID", rectX + 50, rectY + 95);
        parent.text("Title", rectX + 210, rectY + 95);
        parent.text("Publish Date", rectX + 370, rectY + 95);
        parent.text("Last Update", rectX + 530, rectY + 95);
        rectY1 = rectY;

        int start = currentPage * SCROLLS_PER_PAGE;
        int end = Math.min(start + SCROLLS_PER_PAGE, userScrolls.size());

        for (int i = start; i < end; i++) {
            Map<String, String> scroll = userScrolls.get(i);
            String scrollID = scroll.get("ID");
            String title = scroll.get("name");
            String publishDate = scroll.get("publishDate");
            String lastUpdate = scroll.get("lastUpdate");

            // Draw box for scroll information
            parent.stroke(92, 86, 93);
            parent.strokeWeight(2);
            parent.noFill();

            // Scroll ID Field
            parent.rect(rectX + 40, rectY1 + 100, 160, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(scrollID, rectX + 50, rectY1 + 125);

            // Title Field
            parent.noFill();
            parent.rect(rectX + 200, rectY1 + 100, 160, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(title, rectX + 210, rectY1 + 125);

            // Publish Date Field
            parent.noFill();
            parent.rect(rectX + 360, rectY1 + 100, 160, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(publishDate, rectX + 370, rectY1 + 125);

            // Last Update Field
            parent.noFill();
            parent.rect(rectX + 520, rectY1 + 100, 220, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(lastUpdate, rectX + 530, rectY1 + 125);

            parent.noStroke();

            // Update Y position for the next scroll
            rectY1 += rectHeight + 20;
        }
    }

    private boolean isMouseOverButton(int x, int y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    public void mousePressed() {
        if (isMouseOverButton(730, 100, 120, 40)) {
            System.out.println("Back Selected");
            isViewUsersDetailsVisible = false;
            adminProfile.isAdminProfileVisible = true;
            parent.redraw();
        }
    }
}