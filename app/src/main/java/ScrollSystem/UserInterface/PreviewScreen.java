package ScrollSystem.UserInterface;

import ScrollSystem.FileHandlers.FilterScroll;
import ScrollSystem.Users.User;
import processing.core.PApplet;

import java.io.*;
import java.nio.file.*;

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
    private static final int MAX_PREVIEW_SIZE = 1024 * 1024; // 1MB max preview size

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

        // Background Overlay
        parent.fill(0, 0, 0, 150);
        parent.rect(0, 0, parent.width * 2, parent.height);

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
        parent.textSize(14);

        // Set the position for the text and specify the width for wrapping
        int textX = 200;
        int textY = 100; // Fixed Y position for the text
        int textWidth = 330; // Width of the text area
        int textHeight = 370; // Height of the text area
        parent.rect(textX, textY, textWidth, textHeight, 10); // Draw the rectangle

        // Handle file preview
        String previewContent = getFilePreview(filePath);

        // Draw wrapped text with scrolling
        parent.fill(0); // Set fill for the text
        parent.textAlign(PApplet.LEFT, PApplet.TOP); // Align text to the left-top corner

        // Clipping
        parent.pushStyle();
        parent.clip(textX, textY, textWidth, textHeight);

        // Calculate the visible area and draw text
        drawWrappedText(previewContent, textX + 5, textY + 5, textWidth - 10, textHeight - 10);

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

    private String getFilePreview(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return "File not found: " + filePath;
        }

        try {
            byte[] fileBytes = Files.readAllBytes(file.toPath());

            if (isTextFile(fileBytes)) {
                return new String(fileBytes, 0, Math.min(fileBytes.length, MAX_PREVIEW_SIZE));
            } else {
                return getHexDump(fileBytes, MAX_PREVIEW_SIZE);
            }
        } catch (IOException e) {
            return "Error reading file: " + e.getMessage();
        }
    }

    private boolean isTextFile(byte[] bytes) {
        int nullCount = 0;
        for (int i = 0; i < Math.min(bytes.length, 1000); i++) {
            if (bytes[i] == 0) {
                nullCount++;
            }
        }
        return nullCount < 5; // Assume it's text if less than 5 null bytes in the first 1000 bytes
    }

    private String getHexDump(byte[] bytes, int maxBytes) {
        StringBuilder sb = new StringBuilder();
        sb.append("Binary file content (first " + Math.min(bytes.length, maxBytes) + " bytes):\n\n");
        for (int i = 0; i < Math.min(bytes.length, maxBytes); i++) {
            sb.append(String.format("%02X ", bytes[i]));
            if ((i + 1) % 16 == 0) sb.append("\n");
        }
        return sb.toString();
    }

    private void drawWrappedText(String text, float x, float y, float maxWidth, float maxHeight) {
        String[] lines = text.split("\n");
        lineHeight = parent.textAscent() + parent.textDescent();

        int visibleLines = (int) (maxHeight / lineHeight);
        int startLine = (int) scrollOffset;
        int endLine = Math.min(startLine + visibleLines, lines.length);

        parent.pushStyle();
        parent.clip((int) x, (int) y, (int) maxWidth, (int) maxHeight);

        float currentY = y;
        for (int i = startLine; i < endLine; i++) {
            parent.text(lines[i], x, currentY);
            currentY += lineHeight;
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
                    downloadMessage = "Downloaded to: " + downloadPath;
                } else {
                    System.out.println("Failed to download scroll");
                    downloadMessage = "Failed to download scroll";
                }
                parent.redraw();
            } else {
                System.out.println("Unable to download: User or ScrollId is null");
                downloadMessage = "Unable to download: User or ScrollId is null";
                parent.redraw();
            }
        }
    }

    public void mouseWheel(processing.event.MouseEvent event) {
        if (lines == null) {
            System.out.println("Warning: 'lines' is null, cannot scroll.");
            return;
        }
        int scrollAmount = event.getCount();
        scrollOffset -= scrollAmount * scrollStep;
        scrollOffset = PApplet.constrain(scrollOffset, 0, Math.max(0, lines.split("\n").length - maxVisibleLines));
        parent.redraw();
    }

    public String getFilePath() {
        return filePath;
    }

    public void resetScroll() {
        this.scrollOffset = 0;
    }
}