package ScrollSystem.UserInterface;

import ScrollSystem.FileHandlers.FileUpload;
import ScrollSystem.FileHandlers.LoginDatabase;
import ScrollSystem.FileHandlers.ScrollDatabase;
import ScrollSystem.FileHandlers.UserScroll;
import processing.core.PApplet;

public class DeleteScreen {
    PApplet parent;
    public boolean isDeleteScreenVisible = false;
    private int scrollIdToDelete;  

    UserScroll userScrolldatabase;
    ScrollDatabase scrollDatabase;
    LoginDatabase loginDatabase;

    float shadowOffset = 8;

    public DeleteScreen(PApplet parent) {
        this.parent = parent;

        this.userScrolldatabase = new UserScroll("src/main/java/ScrollSystem/Databases/database.db");
        this.scrollDatabase = new ScrollDatabase("src/main/java/ScrollSystem/Databases/database.db");
        this.loginDatabase = new LoginDatabase("src/main/java/ScrollSystem/Databases/database.db");
    }

    public void drawDelete() {

        // parent.clear();
        if (isDeleteScreenVisible) {
            // Background Overlay
            parent.fill(0,0,0,200);
            parent.rect(0, 0, parent.width*2, parent.height);

            // Shadow properties
            parent.fill(0, 0, 0, 50);
            parent.noStroke();
            parent.rect(parent.width / 2 - 200 - shadowOffset, parent.height / 2 - 150 - shadowOffset, 400 + 2 * shadowOffset, 300 + 2 * shadowOffset, 15);


            // White Login Box
            parent.fill(255,249,254);
            parent.stroke(200);
            parent.rect(parent.width / 2 - 200, parent.height / 2 - 150, 400, 300, 10);

            // Title
            parent.fill(0);
            parent.textSize(24);
            parent.text("Delete Scroll", 415, 165);
            parent.text("Do you want to", 400, 225);
            parent.text("delete this", 425, 265);
            parent.text("scroll?", 450, 305);

            // Yes Button
            boolean isHoverYes = isMouseOverButton(550, 370, 120, 40);
            if (isHoverYes) {
                parent.fill(174,37,222,200);
            } else {
                parent.fill(174,37,222);
            }
            parent.noStroke();
            parent.rect(550, 370, 100, 40, 10);
            parent.fill(255);
            parent.textSize(16);
            parent.text("Yes", 585, 395);

            // No Button
            boolean isHoverNo = isMouseOverButton(310, 370, 120, 40);
            if (isHoverNo) {
                parent.fill(174,37,222,200);
            } else {
                parent.fill(174,37,222);
            }
            parent.noStroke();
            parent.rect(310, 370, 100, 40, 10);
            parent.fill(255);
            parent.textSize(16);
            parent.text("No", 350, 395);
        }
    }

    public void mousePressed() {
        if (isDeleteScreenVisible) {
            // Yes Button 
            if (isMouseOverButton(550, 370, 100, 40)) {
                System.out.println("Confirmed deletion of scroll ID: " + scrollIdToDelete);
                
                //archive file 
                FileUpload fileUpload = new FileUpload();
                fileUpload.deleteRowById(scrollIdToDelete);

                //delete from UserScroll 
                userScrolldatabase.removeScroll(scrollIdToDelete);

                //delete from scroll database 
                scrollDatabase.deleteRowById(scrollIdToDelete);

                isDeleteScreenVisible = false;
                refreshView();
            }

            // No Button 
            if (isMouseOverButton(310, 370, 100, 40)) {
                System.out.println("Cancelled deletion.");
                isDeleteScreenVisible = false; 
                refreshView();
            }
        }
    }

    public void showDeleteConfirmation(int scrollId) {
        System.out.println("was here");
        scrollIdToDelete = scrollId;
        isDeleteScreenVisible = true;  
    }

    private boolean isMouseOverButton(int x, int y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }
    
    public void refreshView() {
        parent.clear();  
        parent.redraw();  
    }
}
