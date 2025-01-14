package ScrollSystem.UserInterface;

import ScrollSystem.FileHandlers.FilterScroll;
import ScrollSystem.Users.User;
import processing.core.PApplet;

public class PreviewScreen {
    PApplet parent;
    public boolean isPreviewScreenVisible = false;
    float shadowOffset = 8;
    ViewScrollsUsers viewScrollsUsers;
    ViewScrollsAdmin viewScrollsAdmin;
    public ParsingScreen parsingScreen;
    FilterScroll filterScroll;
    String lines;
    private String scrollId;
    private String title;
    private String author;
    private String uploadDate;
    private String filePath;
    private User user;
    private String downloadMessage = "";
    // Scrolling variables
    private float scrollOffset = 0; // Track the scroll position
    private float scrollStep = 5; // Amount to scroll with each wheel event
    private final int maxVisibleLines = 10000; // Maximum lines visible
    private float lineHeight; // Height of each line of text

    public PreviewScreen(PApplet parent, ViewScrollsUsers viewScrollsUsers) {
        this.parent = parent;
        this.viewScrollsUsers = viewScrollsUsers;
        this.user = viewScrollsUsers.getUserObj();
        parsingScreen = new ParsingScreen(parent, this);
        lineHeight = parent.textAscent() + parent.textDescent(); // Calculate line height
    }

    public PreviewScreen(PApplet parent, ViewScrollsAdmin viewScrollsAdmin) {
        this.parent = parent;
        this.user = viewScrollsAdmin.getUserObj();
        parsingScreen = new ParsingScreen(parent, this);
        lineHeight = parent.textAscent() + parent.textDescent(); // Calculate line height
    }

    public void setScrollDetails(String scrollId, String title, String author, String uploadDate, String filePath) {
        this.scrollId = scrollId;
        this.title = title;
        this.author = author;
        this.uploadDate = uploadDate;
        this.filePath = filePath;
    }

    public void drawPreview() {

        if (!isPreviewScreenVisible) return;

        if (!parsingScreen.isParsingScreenVisible) {
            // Background Overlay
            parent.fill(0, 0, 0, 150);
            parent.rect(0, 0, parent.width * 2, parent.height);
        }

        // Shadow properties
        parent.fill(0, 0, 0, 50);
        parent.noStroke();
        parent.rect(parent.width / 2 - 300 - shadowOffset, parent.height / 2 - 225 - shadowOffset, 600 + 2 * shadowOffset, 450 + 2 * shadowOffset, 15);

        // White Register Box
        parent.fill(255, 249, 254);
        parent.stroke(200);
        parent.rect(parent.width / 2 - 300, parent.height / 2 - 225, 600, 450, 10);

        // Title
        parent.fill(0);
        parent.textSize(24);
        parent.text("Preview Scroll", 400, 85);

        // Scroll Preview Rectangle
        parent.noFill();
        filterScroll = new FilterScroll(filePath);
        lines = filterScroll.getAllLines();
        parent.textSize(14);

        // Set the position for the text and specify the width for wrapping
        int textX = 200;
        int textY = 100; // Fixed Y position for the text
        int textWidth = 330; // Width of the text area
        int textHeight = 370; // Height of the text area
        parent.rect(textX, textY, textWidth, textHeight, 10); // Draw the rectangle

        // Draw wrapped text with scrolling
        parent.fill(0); // Set fill for the text
        parent.textAlign(PApplet.LEFT, PApplet.TOP); // Align text to the left-top corner

        // Clipping
        parent.pushStyle();
        parent.clip(textX, textY, textWidth, textHeight);

        // Calculate the visible area and draw text
        drawWrappedText(lines, textX + 5, textY + 5, textWidth - 10, textHeight - 10); // Adjusted Y position

        parent.noClip();
        parent.popStyle();

        // Scroll Details
        parent.textAlign(PApplet.CORNER);
        parent.noFill();
        parent.rect(560, 100, 200, 40, 10);
        parent.fill(92, 86, 93);
        parent.textSize(16);
        parent.text("Scroll ID: " + scrollId, 570, 125);

        parent.noFill();
        parent.rect(560, 150, 200, 40, 10);
        parent.fill(92, 86, 93);
        parent.text("Title: " + title, 570, 175);

        parent.noFill();
        parent.rect(560, 210, 200, 40, 10);
        parent.fill(92, 86, 93);
        parent.text("Author: " + author, 570, 235);

        parent.noFill();
        parent.rect(560, 270, 200, 40, 10);
        parent.fill(92, 86, 93);
        parent.text("Uploaded: " + uploadDate, 570, 295);

        parent.noFill();

        // Download Button
        boolean isHoverDownload = isMouseOverButton(610, 340, 100, 40);
        if (isHoverDownload) {
            parent.fill(174, 37, 222, 200);
        } else {
            parent.fill(174, 37, 222);
        }
        parent.noStroke();
        parent.rect(610, 330, 100, 40, 10);
        parent.fill(255);
        parent.text("Download", 622, 355);

        // Display download message
        if (!downloadMessage.isEmpty()) {
            parent.fill(0);
            parent.textSize(12);
            parent.text(downloadMessage, 200, 487);
        }

        parent.textSize(16);

        // Parse Button
        boolean isHoverParsing = isMouseOverButton(610, 390, 100, 40);
        if (isHoverParsing) {
            parent.fill(174, 37, 222, 200);
        } else {
            parent.fill(174, 37, 222);
        }
        parent.noStroke();
        parent.rect(610, 380, 100, 40, 10);
        parent.fill(255);
        parent.text("Line View", 625, 405);

        // Cancel Button
        boolean isHoverCancel = isMouseOverButton(610, 440, 100, 40);
        if (isHoverCancel) {
            parent.fill(174, 37, 222, 200);
        } else {
            parent.fill(174, 37, 222);
        }
        parent.noStroke();
        parent.rect(610, 430, 100, 40, 10);
        parent.fill(255);
        parent.text("Cancel", 635, 455);
    }


    private void drawWrappedText(String text, float x, float y, float maxWidth, float maxHeight) {
        String[] linesArray = text.split("\n");
        lineHeight = parent.textAscent() + parent.textDescent();

        // Calculate maximum visible area
        int visibleLines = (int) (maxHeight / lineHeight);

        int startLine = (int)scrollOffset;
        int endLine = Math.min(startLine + visibleLines, linesArray.length);

        // Clipping
        parent.pushStyle();
        parent.clip((int) x, (int) y, (int) maxWidth, (int) maxHeight);

        float currentY = y;
        for (int i = startLine; i < endLine; i++) {
            parent.text(linesArray[i], x, currentY);
            currentY += lineHeight; // Increment Y position for each line
        }
        parent.popStyle();
    }

    private boolean isMouseOverButton(int x, int y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    public void mousePressed() {
        if (isMouseOverButton(610, 440, 100, 40)) {
            isPreviewScreenVisible = false;
            parent.redraw();
        }

        if (isMouseOverButton(610, 390, 100, 40)) {
            System.out.println("Parsing screen active");
            parsingScreen.isParsingScreenVisible = true;
            parent.redraw();
            parsingScreen.mousePressed();
        }

        if (isMouseOverButton(610, 340, 100, 40)) {
            System.out.println("Download button pressed");
            if (scrollId != null && user != null) {
                int id = Integer.parseInt(scrollId);
                String downloadPath = user.downloadScroll(id);
                if (downloadPath != null) {
                    System.out.println("Scroll downloaded successfully to: " + downloadPath);
                } else {
                    System.out.println("Failed to download scroll");
                }
            } else {
                System.out.println("Unable to download: User or ScrollId is null");
            }
        }
    }

    public void mouseWheel(processing.event.MouseEvent event) {
        String[] linesArray = lines.split("\n");
        int totalLines = linesArray.length;
        int scrollAmount = event.getCount();
        scrollOffset -= scrollAmount * scrollStep;
        int visibleLines = (int) (370 / lineHeight);
        scrollOffset = PApplet.constrain(scrollOffset, 0, Math.max(0, totalLines - visibleLines));
        parent.redraw();  
    }

    public String getFilePath() {
        return filePath;
    }

    public void resetScroll() {
        this.scrollOffset = 0;
    }


}