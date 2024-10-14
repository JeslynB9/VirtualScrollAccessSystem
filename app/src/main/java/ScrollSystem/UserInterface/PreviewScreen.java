package ScrollSystem.UserInterface;

import ScrollSystem.FileHandlers.FilterScroll;
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

    // Scrolling variables
    private float scrollOffset = 0; // Track the scroll position
    private float scrollStep = 20; // Amount to scroll with each wheel event
    private final int maxVisibleLines = 10; // Maximum lines visible
    private float lineHeight; // Height of each line of text

    public PreviewScreen(PApplet parent, ViewScrollsUsers viewScrollsUsers) {
        this.parent = parent;
        this.viewScrollsUsers = viewScrollsUsers;
        parsingScreen = new ParsingScreen(parent, this);
        lineHeight = parent.textAscent() + parent.textDescent(); // Calculate line height
    }

    public PreviewScreen(PApplet parent, ViewScrollsAdmin viewScrollsAdmin) {
        this.parent = parent;
        this.viewScrollsAdmin = viewScrollsAdmin;
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
        // System.out.println("LINE: " + lines);
        parent.textSize(12);

        // Set the position for the text and specify the width for wrapping
        int textX = 200;
        int textY = 100;
        int textWidth = 330; // Width of the text area
        int textHeight = 370;
        parent.rect(textX, textY, textWidth, textHeight, 10); // Draw the rectangle

        // Draw wrapped text with scrolling
        parent.fill(0); // Set fill for the text
        parent.textAlign(PApplet.LEFT, PApplet.TOP); // Align text to the left-top corner

        // Calculate the visible area and draw text
        drawWrappedText(lines, textX + 5, textY + 5 + scrollOffset, textWidth - 10, textHeight - 10);

        // Scroll Details
        parent.textAlign(PApplet.CORNER);
        parent.noFill();
        parent.rect(560, 100, 200, 40, 10);
        parent.fill(92, 86, 93);
        parent.textSize(16);
        parent.text("Scroll ID: " + scrollId, 570, 125);

        parent.noFill();
        parent.rect(560, 160, 200, 40, 10);
        parent.fill(92, 86, 93);
        parent.text("Title: " + title, 570, 185);

        parent.noFill();
        parent.rect(560, 220, 200, 40, 10);
        parent.fill(92, 86, 93);
        parent.text("Author: " + author, 570, 245);

        parent.noFill();
        parent.rect(560, 280, 200, 40, 10);
        parent.fill(92, 86, 93);
        parent.text("Uploaded: " + uploadDate, 570, 305);

        parent.noFill();

        // Download Button
        boolean isHoverDownload = isMouseOverButton(610, 340, 100, 40);
        if (isHoverDownload) {
            parent.fill(174, 37, 222, 200);
        } else {
            parent.fill(174, 37, 222);
        }
        parent.noStroke();
        parent.rect(610, 340, 100, 40, 10);
        parent.fill(255);
        parent.text("Download", 622, 365);

        // Cancel Button
        boolean isHoverCancel = isMouseOverButton(610, 440, 100, 40);
        if (isHoverCancel) {
            parent.fill(174, 37, 222, 200);
        } else {
            parent.fill(174, 37, 222);
        }
        parent.noStroke();
        parent.rect(610, 440, 100, 40, 10);
        parent.fill(255);
        parent.text("Cancel", 635, 465);

        // Parse Button
        boolean isHoverParsing = isMouseOverButton(610, 390, 100, 40);
        if (isHoverParsing) {
            parent.fill(174, 37, 222, 200);
        } else {
            parent.fill(174, 37, 222);
        }
        parent.noStroke();
        parent.rect(610, 390, 100, 40, 10);
        parent.fill(255);
        parent.text("View by Line", 612, 415);
    }

    private void drawWrappedText(String text, float x, float y, float maxWidth, float maxHeight) {
        String[] words = text.split("\n");
        float currentY = y;
        int totalLines = 0;

        for (String line : words) {
            String[] wordsInLine = line.split(" ");
            StringBuilder wrappedLine = new StringBuilder();
            for (String word : wordsInLine) {
                float lineWidth = parent.textWidth(wrappedLine.toString() + word + " ");
                if (lineWidth > maxWidth) {
                    parent.text(wrappedLine.toString(), x, currentY);
                    currentY += lineHeight; // Move down for the next line
                    totalLines++; // Count the drawn line
                    wrappedLine = new StringBuilder(word + " "); // Start new line with the current word
                } else {
                    wrappedLine.append(word).append(" ");
                }

                // Check if we exceed the max height
                if (totalLines >= maxVisibleLines) {
                    break; // Stop drawing if we exceed the height
                }
            }

            // Draw any remaining text in the wrapped line
            if (wrappedLine.length() > 0 && totalLines < maxVisibleLines) {
                parent.text(wrappedLine.toString(), x, currentY);
                currentY += lineHeight; // Move down for the next line
                totalLines++;
            }

            // Stop if we exceed the max height
            if (totalLines >= maxVisibleLines) {
                break; // Stop drawing if we exceed the height
            }
        }
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
    }

    // Add this method to handle mouse wheel scrolling
    public void mouseWheel(processing.event.MouseEvent event) {
        scrollOffset += event.getCount() * scrollStep; // Adjust scroll offset based on wheel movement
        // Constrain the offset to show only 10 lines
        scrollOffset = PApplet.constrain(scrollOffset, -(lines.split("\n").length - maxVisibleLines) * lineHeight, 0);
    }
}
