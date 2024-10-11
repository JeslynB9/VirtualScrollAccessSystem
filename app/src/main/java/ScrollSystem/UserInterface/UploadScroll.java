package ScrollSystem.UserInterface;

import ScrollSystem.Users.User;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.Map;

public class UploadScroll {
    PApplet parent;
    UserProfile userProfile;
    PImage uploadImg;
    public boolean isUploadScreenVisible = false;
    boolean titleSelected = false;
    float shadowOffset = 8;

    public UploadScroll(PApplet parent, UserProfile userProfile) {
        this.parent = parent;
        this.userProfile = userProfile;

        uploadImg = parent.loadImage("src/main/resources/upload.png");
        uploadImg.resize(1920 / 10, 1080 / 10);
    }

    public void drawUploadScroll() {
        if (!isUploadScreenVisible) return;

        // Background Overlay
        parent.fill(0, 0, 0, 150);
        parent.rect(0, 0, parent.width*2, parent.height);

        // Shadow properties
        parent.fill(0, 0, 0, 50);
        parent.noStroke();
        parent.rect(parent.width / 2 - 200 - shadowOffset, parent.height / 2 - 225 - shadowOffset, 400 + 2 * shadowOffset, 450 + 2 * shadowOffset, 15);

        // White Register Box
        parent.fill(255,249,254);
        parent.stroke(200);
        parent.rect(parent.width / 2 - 200, parent.height / 2 - 225, 400, 450, 10);

        // Title
        parent.fill(0);
        parent.textSize(24);
        parent.text("Upload Scroll", 410, 85);

        // Title Field
        if (titleSelected) {
            parent.fill(216,202,220);
        } else {
            parent.noFill();
        }
        parent.stroke(84, 84, 84);
        parent.rect(parent.width / 2 - 120, parent.height / 2 - 170, 240, 40, 5);
        parent.textSize(16);
        parent.fill(84, 84, 84);
        parent.text("Title", 370, 125);

        parent.noFill();
        parent.rect(parent.width / 2 - 120, parent.height / 2 - 110, 240, 260, 5);
        parent.image(uploadImg, parent.width / 2 - 95, parent.height / 2 - 70);
        parent.fill(0, 0, 0);
        parent.textSize(18);
        parent.text("Upload Scroll", parent.width / 2 - 55, parent.height / 2 + 60);

        // Browse Files Button
        boolean isHoverFiles = isMouseOverButton(parent.width / 2 - 60, parent.height / 2 + 80, 120, 30);
        if (isHoverFiles) {
            parent.fill(174,37,222, 200);
        } else {
            parent.fill(174,37,222);
        }
        parent.noStroke();
        parent.rect(parent.width / 2 - 60, parent.height / 2 + 80, 120, 30, 10);
        parent.fill(255);
        parent.textSize(16);
        parent.text("Browse Files", parent.width / 2 - 48, parent.height / 2 + 100);



        // Upload Button
        boolean isHover = isMouseOverButton(560, 440, 100, 40);
        if (isHover) {
            parent.fill(174,37,222, 200);
        } else {
            parent.fill(174,37,222);
        }
        parent.noStroke();
        parent.rect(560, 440, 100, 40, 10);
        parent.fill(255);
        parent.textSize(16);
        parent.text("Upload", 585, 465);

        // Cancel Button
        boolean isHoverCancel = isMouseOverButton(300, 440, 100, 40);
        if (isHoverCancel) {
            parent.fill(174,37,222, 200);
        } else {
            parent.fill(174,37,222);
        }
        parent.noStroke();
        parent.rect(300, 440, 100, 40, 10);
        parent.fill(255);
        parent.text("Cancel", 325, 465);

    }

    private boolean isMouseOverButton(int x, int y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    public void mousePressed() {
        if (isMouseOverButton(parent.width / 2 - 120, parent.height / 2 - 120, 240, 40)) {
            titleSelected = true;
        }

        if (isMouseOverButton(300, 440, 100, 40)) {
            isUploadScreenVisible = false;
        }
    }

}
