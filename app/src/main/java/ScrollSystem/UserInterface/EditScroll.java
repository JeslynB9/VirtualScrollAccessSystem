package ScrollSystem.UserInterface;

import java.util.*;

import ScrollSystem.FileHandlers.FileUpload;
import ScrollSystem.FileHandlers.LoginDatabase;
import ScrollSystem.FileHandlers.ScrollDatabase;
import ScrollSystem.FileHandlers.UserScroll;
import ScrollSystem.Users.User;
import processing.core.PApplet;
import processing.core.PImage;

public class EditScroll {
    PApplet parent;
    PImage uploadImg;
    public boolean isEditScrollScreenVisible = false;
    private int scrollIdToUpdate;
    boolean titleSelected = false;
    String titleText = ""; // To store the typed title
    float shadowOffset = 8;
    FileUpload fileUpload;

    boolean isUploaded = false;

    public EditScroll(PApplet parent) {
        this.parent = parent;

        uploadImg = parent.loadImage("src/main/resources/upload.png");
        uploadImg.resize(1920 / 10, 1080 / 10);
    }

    public void drawEditScrolls() {
        if (!isEditScrollScreenVisible) return;

        // Background Overlay
        parent.fill(0, 0, 0, 150);
        parent.rect(0, 0, parent.width * 2, parent.height);

        // Shadow properties
        parent.fill(0, 0, 0, 50);
        parent.noStroke();
        parent.rect(parent.width / 2 - 200 - shadowOffset, parent.height / 2 - 225 - shadowOffset, 400 + 2 * shadowOffset, 450 + 2 * shadowOffset, 15);

        // White Register Box
        parent.fill(255, 249, 254);
        parent.stroke(200);
        parent.rect(parent.width / 2 - 200, parent.height / 2 - 225, 400, 450, 10);

        // Title
        parent.fill(0);
        parent.textSize(24);
        parent.text("Upload Scroll", 410, 85);

        // Title Field
        if (titleSelected) {
            parent.fill(216, 202, 220);
        } else {
            parent.noFill();
        }
        parent.stroke(84, 84, 84);
        parent.rect(parent.width / 2 - 120, parent.height / 2 - 170, 240, 40, 5);
        parent.textSize(16);
        parent.fill(84, 84, 84);

        if (titleText.isEmpty()) {
            parent.text("Title", 370, 125); // Placeholder text when title is empty
        } else {
            parent.text(titleText, parent.width / 2 - 115, 125); // Display typed title
        }

        parent.noFill();
        parent.rect(parent.width / 2 - 120, parent.height / 2 - 110, 240, 260, 5);
        parent.image(uploadImg, parent.width / 2 - 95, parent.height / 2 - 70);
        parent.fill(0, 0, 0);
        parent.textSize(18);
        parent.text("Upload Scroll", parent.width / 2 - 55, parent.height / 2 + 60);

        // Browse Files Button
        boolean isHoverFiles = isMouseOverButton(parent.width / 2 - 60, parent.height / 2 + 80, 120, 30);
        if (isHoverFiles) {
            parent.fill(174, 37, 222, 200);
        } else {
            parent.fill(174, 37, 222);
        }
        parent.noStroke();
        parent.rect(parent.width / 2 - 60, parent.height / 2 + 80, 120, 30, 10);
        parent.fill(255);
        parent.textSize(16);
        parent.text("Browse Files", parent.width / 2 - 48, parent.height / 2 + 100);

        // Upload Button
        boolean isHover = isMouseOverButton(560, 440, 100, 40);
        if (isHover) {
            parent.fill(174, 37, 222, 200);
        } else {
            parent.fill(174, 37, 222);
        }
        parent.noStroke();
        parent.rect(560, 440, 100, 40, 10);
        parent.fill(255);
        parent.textSize(16);
        parent.text("Upload", 585, 465);

        // Cancel Button
        boolean isHoverCancel = isMouseOverButton(300, 440, 100, 40);
        if (isHoverCancel) {
            parent.fill(174, 37, 222, 200);
        } else {
            parent.fill(174, 37, 222);
        }
        parent.noStroke();
        parent.rect(300, 440, 100, 40, 10);
        parent.fill(255);
        parent.text("Cancel", 325, 465);
    }

    public void showUpdateConfirmation(int scrollId) {
        System.out.println("was here");
        scrollIdToUpdate = scrollId;
        isEditScrollScreenVisible = true;
    }


    private boolean isMouseOverButton(int x, int y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    public void mousePressed() {
        if (isMouseOverButton(parent.width / 2 - 120, parent.height / 2 - 170, 240, 40)) {
            titleSelected = true;
        } else {
            titleSelected = false; // Deselect title field when clicking elsewhere
        }

        if (isMouseOverButton(300, 440, 100, 40)) { // Cancel Button
            isEditScrollScreenVisible = false;
        }

        if (isMouseOverButton(560, 440, 100, 40)) { // Upload Button
//            if (fileUpload.uploadFile() == null) {
//                System.out.println("Did not choose file");
//                return;}

            //get old scroll info
            ScrollDatabase scrollDatabase = new ScrollDatabase("src/main/java/ScrollSystem/Databases/database.db");
            Map<String, String> oldScrollData = scrollDatabase.getRowById(scrollIdToUpdate);
            if (oldScrollData == null) {
                System.out.println("Scroll ID does not exist.");
                return;
            }

            String oldTitle = oldScrollData.get("name");
            String oldFilePath = oldScrollData.get("filePath");
            String author = oldScrollData.get("author");


            //archive old file
            if (fileUpload.deleteRowById(scrollIdToUpdate)) {
                System.out.println("Old file moved to archive.");
            } else {
                System.out.println("Failed to move old file to archive.");
            }

            //upload new file 
            String pathToUploadedFile = fileUpload.uploadFile();
            if (pathToUploadedFile == null) {
                System.out.println("pathToUploadedFile is null");
                return;
            }
            
            //edit database
            scrollDatabase.editRow(scrollIdToUpdate, titleText, author, oldScrollData.get("publishDate"), pathToUploadedFile);
            
            isUploaded = true;
        }

        if (isMouseOverButton(parent.width / 2 - 60, parent.height / 2 + 80, 120, 30)) { // Browse Files
            fileUpload = new FileUpload(); // Reinitialize upload file class (upload new file)
            fileUpload.browseFile();
        }
    }

    public void keyPressed() {
        if (titleSelected) {
            if (parent.key == PApplet.BACKSPACE) {
                if (titleText.length() > 0) {
                    titleText = titleText.substring(0, titleText.length() - 1);
                }
            } else if (parent.key == PApplet.ENTER || parent.key == PApplet.RETURN) {
                // Do nothing or trigger some event if necessary
            } else if (parent.key != PApplet.CODED) {
                titleText += parent.key; // Append typed characters to the title
            }
        }
    }
}
