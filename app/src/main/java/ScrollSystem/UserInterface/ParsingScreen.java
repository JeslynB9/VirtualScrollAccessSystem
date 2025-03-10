package ScrollSystem.UserInterface;

import ScrollSystem.FileHandlers.FilterScroll;
import processing.core.PApplet;

public class ParsingScreen {
    PApplet parent;
    PreviewScreen previewScreen;
    public boolean isParsingScreenVisible = false;
    boolean isLineNumberSelected = false;
    float shadowOffset = 8;
    String lineInput = ""; // To store the user's input
    FilterScroll filterScroll;
    String lineToDisplay = "";
    int lineNumber;
    int currentLine;

    public ParsingScreen(PApplet parent, PreviewScreen previewScreen) {
        this.parent = parent;
        this.previewScreen = previewScreen;
    }

    public void drawParsing() {
        if (!isParsingScreenVisible) return;

        if (lineToDisplay == null) {
            lineToDisplay = "";
        }

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
        parent.fill(0);
        parent.text(lineToDisplay, parent.width / 2 - 275, parent.height / 2 - 50);

        // Title
        parent.fill(0);
        parent.textSize(24);
        parent.text("Parse Scroll", 410, 180);

        // Scroll Preview Rectangle
        parent.noFill();
        parent.stroke(92,86,93);
        parent.rect(200, 200, 555, 60, 10);

        // Line Details with user input
        if (isLineNumberSelected) {
            parent.fill(216,202,220);
        } else {
            parent.noFill();
        }
        parent.rect(200, 280, 200, 40, 10);
        parent.fill(92,86,93);
        parent.textSize(16);
        parent.text("Line Number:", 210, 305);
        parent.text(lineInput, parent.textWidth("Line Number:") + 220, 305); 

        parent.noFill();

        // Go Button
        boolean isHoverGo = isMouseOverButton(420, 280, 100, 40);
        if (isHoverGo) {
            parent.fill(174,37,222, 200);
        } else {
            parent.fill(174,37,222);
        }
        parent.noStroke();
        parent.rect(420, 280, 50, 40, 10);
        parent.fill(255);
        parent.text("Go", 433, 305);

        // Previous Button
        boolean isHoverPrevious = isMouseOverButton(510, 280, 100, 40);
        if (isHoverPrevious) {
            parent.fill(174,37,222, 200);
        } else {
            parent.fill(174,37,222);
        }
        parent.noStroke();
        parent.rect(510, 280, 100, 40, 10);
        parent.fill(255);
        parent.text("Previous", 527, 305);

        // Next Button
        boolean isHoverNext = isMouseOverButton(655, 280, 100, 40);
        if (isHoverNext) {
            parent.fill(174,37,222, 200);
        } else {
            parent.fill(174,37,222);
        }
        parent.noStroke();
        parent.rect(655, 280, 100, 40, 10);
        parent.fill(255);
        parent.text("Next", 687, 305);

        // Close Button
        boolean isHoverClose = isMouseOverButton(435, 340, 100, 40);
        if (isHoverClose) {
            parent.fill(174,37,222, 200);
        } else {
            parent.fill(174,37,222);
        }
        parent.noStroke();
        parent.rect(435, 340, 100, 40, 10);
        parent.fill(255);
        parent.text("Close", 463, 365);
    }

    private boolean isMouseOverButton(int x, int y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    public void mousePressed() {
        if (isMouseOverButton(435, 340, 100, 40)) {
            isParsingScreenVisible = false;
        }

        if (isMouseOverButton(200, 280, 200, 40)) {
            isLineNumberSelected = true;
        }

        lineToDisplay = "Please enter a line number and click 'Go' first.";

        if (isMouseOverButton(420, 280, 100, 40)) { // Go button
            if (lineInput.isEmpty()) {
                System.err.println("Please enter a line number");
                lineToDisplay = "ERROR: Please enter a line number";
                filterScroll = null;
                return;
            }

            try {
                lineNumber = Integer.parseInt(lineInput);
                filterScroll = new FilterScroll(previewScreen.getFilePath());

                // Check if the input line number is valid
                if (lineNumber < 1) {
                    lineToDisplay = "ERROR: Line number must be greater than 0";
                    filterScroll = null;
                } else if (lineNumber > filterScroll.countLines()) {

                    filterScroll = null;
                    lineToDisplay = "ERROR: Line number exceeds total lines";
                } else {
                    currentLine = lineNumber;
                    lineToDisplay = filterScroll.getLine(lineNumber);
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid input: Number Exception");
                lineToDisplay = "ERROR: Invalid input, please enter a valid number";
                filterScroll = null;
            }
        }

        if (filterScroll == null) {
            return;
        }

        if (isMouseOverButton(655, 280, 100, 40)) { //Next button
            if (filterScroll.hasNextLine()) {  // Check if there is a next line
                lineToDisplay = filterScroll.nextLine();
                currentLine++;
                lineInput = String.valueOf(currentLine);
            } else {
                lineToDisplay = "END OF FILE";  // End of file reached
            }
        }

        if (isMouseOverButton(510, 280, 100, 40)) { // Previous button
            if (filterScroll.hasPreviousLine()) {  // Check if there is a previous line
                lineToDisplay = filterScroll.previousLine();
                currentLine--;
                lineInput = String.valueOf(currentLine);
            } else {
                lineToDisplay = "NO PREVIOUS LINES";  // Beginning of file reached
            }
        }

    }

    // Handling integer input
    public void keyPressed() {
        if (parent.key >= '0' && parent.key <= '9') {
            lineInput += parent.key; // Add numeric keys to the input string
        } else if (parent.key == PApplet.BACKSPACE && lineInput.length() > 0) {
            lineInput = lineInput.substring(0, lineInput.length() - 1); // Handle backspace
        }
    }

}
