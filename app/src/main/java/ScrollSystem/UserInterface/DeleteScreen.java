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

    public DeleteScreen(PApplet parent) {
        this.parent = parent;

        this.userScrolldatabase = new UserScroll("src/main/java/ScrollSystem/Databases/database.db");
        this.scrollDatabase = new ScrollDatabase("src/main/java/ScrollSystem/Databases/database.db");
        this.loginDatabase = new LoginDatabase("src/main/java/ScrollSystem/Databases/database.db");
    }

    public void drawDelete() {
        // parent.clear();
        if (isDeleteScreenVisible) {
            parent.fill(0, 0, 0, 200); 
            parent.rect(0, 0, parent.width, parent.height);  

            parent.fill(255);
            parent.stroke(84, 84, 84);
            parent.rect(parent.width / 2 - 200, parent.height / 2 - 100, 400, 200, 10); 

            // Text
            parent.fill(0);
            parent.textSize(20);
            parent.text("Do you want to delete this scroll?", (parent.width / 2) - 150, parent.height / 2 - 30);
            // Yes button
            parent.fill(174, 37, 222);
            parent.rect(parent.width / 2 - 100, parent.height / 2 + 20, 80, 40, 10);
            parent.fill(255);
            parent.text("Yes", (parent.width / 2) - 85, parent.height / 2 + 45);  
            // No button
            parent.fill(174, 37, 222);
            parent.rect(parent.width / 2 + 20, parent.height / 2 + 20, 80, 40, 10);
            parent.fill(255);
            parent.text("No", (parent.width / 2) + 35, parent.height / 2 + 45);  
        }
    }

    public void mousePressed() {
        if (isDeleteScreenVisible) {
            // Yes Button 
            if (isMouseOverButton(parent.width / 2 - 100, parent.height / 2 + 20, 80, 40)) {
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
            if (isMouseOverButton(parent.width / 2 + 20, parent.height / 2 + 20, 80, 40)) {
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
