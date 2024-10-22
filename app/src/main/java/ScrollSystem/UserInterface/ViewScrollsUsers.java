package ScrollSystem.UserInterface;

import ScrollSystem.FileHandlers.ScrollDatabase;
import ScrollSystem.Users.User;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewScrollsUsers {
    PApplet parent;
    PImage scrollsImg;
    PImage filterImg, filterImgHover;
    PImage downloadImg;
    String username;
    public FilterScreen filterScreen;
    public PreviewScreen previewScreen;
    public UserProfile userProfile;
    UploadScroll uploadScroll;
    LoginScreen loginScreen;
    ScrollDatabase scrollDb;
    List<Map<String, String>> scrolls;
    List<Map<String, String>> currentPageScrolls;
    private User currentUser;
    static int width = 1920 / 2;
    static int height = 1080 / 2;
    float rectW = width - 100;
    float rectH = (float) (height/3)*2;
    float cornerRadius = 10;
    float rectX;
    float rectY;
    float rectY1;
    float rectHeight = 40;
    float shadowOffset = 8;
    private int currentPage = 0;
    private final int SCROLLS_PER_PAGE = 4;

    public ViewScrollsUsers(PApplet parent, LoginScreen loginScreen, UploadScroll uploadScroll, DeleteScreen deleteScreen, EditScroll editScroll) {
        this.parent = parent;
        this.loginScreen = loginScreen;
        this.uploadScroll = uploadScroll;

        updateCurrentUser(loginScreen.getEnteredUsername());

        filterScreen = new FilterScreen(parent, this);
        previewScreen = new PreviewScreen(parent, this);
        userProfile = new UserProfile(parent, this, deleteScreen, editScroll);

        rectX = (float) width / 2 - rectW / 2;
        rectY = (float) height / 2 - rectH / 2;

        scrollsImg = parent.loadImage("src/main/resources/scrolls.png");
        scrollsImg.resize(1920 / 40, 1080 / 40);

        filterImg = parent.loadImage("src/main/resources/filter.png");
        filterImg.resize(1920 / 20, 1080 / 20);

        filterImgHover = parent.loadImage("src/main/resources/filter_hover.png");
        filterImgHover.resize(1920 / 20, 1080 / 20);

        downloadImg = parent.loadImage("src/main/resources/download.png");
        downloadImg.resize(1920 / 30, 1080 / 30);

        scrollDb = new ScrollDatabase("src/main/java/ScrollSystem/Databases/database.db");
        scrolls = new ArrayList<>();
        currentPageScrolls = new ArrayList<>();
        updateScrolls(scrollDb.getAllScrolls());
    }

    public void drawScrollsUsers() {
        if (uploadScroll != null && uploadScroll.isUploaded) {
            updateScrolls(scrollDb.getAllScrolls());
            uploadScroll.isUploaded = false;
        }

        parent.redraw();
        parent.stroke(84, 84, 84);
        parent.textSize(12);
        parent.fill(0);

        parent.fill(0, 0, 0, 50);
        parent.noStroke();
        parent.rect(rectX - shadowOffset, rectY - shadowOffset, rectW + 2 * shadowOffset, rectH + 2 * shadowOffset, cornerRadius + 5);

        parent.fill(253,249,255);
        parent.noStroke();
        parent.rect(rectX, rectY, rectW, rectH, cornerRadius);

        parent.noStroke();
        parent.fill(216,202,220);
        parent.rect(rectX, rectY + 30, rectW, 30);
        parent.rect(rectX, rectY, rectW, 60, cornerRadius);

        parent.noStroke();
        parent.fill(255, 249, 254);
        parent.rect(rectX, rectY, rectW/4, 60, cornerRadius);
        parent.rect(rectX, rectY+30, rectW/4, 30);

        parent.fill(174,37,222);
        parent.textSize(16);
        parent.image(scrollsImg, 100, 110);
        parent.text("Scrolls", 145, 127);

        parent.image(filterImg,(rectW/14)*13, 95);

        username = loginScreen.getEnteredUsername();
        if (isMouseOverButton((int) rectX, 30, (int)parent.textWidth(username),10)) {
            parent.fill(174,37,222);
        } else {
            parent.fill(253, 249, 255);
        }
        parent.text(username, rectX, 40);

        parent.fill(253, 249, 255);
        parent.text("User", rectX, 60);

        if (isMouseOverButton((float)((rectW / 14) * 13), 95, filterImg.width, filterImg.height)) {
            parent.image(filterImgHover, (rectW / 14) * 13, 95);
        } else {
            parent.image(filterImg, (rectW / 14) * 13, 95);
        }

        drawScrolls();
    }

    public void drawScrolls() {
        parent.fill(92, 86, 93);
        parent.text("Title", rectX + 50, rectY + 95);
        parent.text("Author", rectX + 210, rectY + 95);
        parent.text("Upload Date", rectX + 370, rectY + 95);
        parent.text("Last Updated", rectX + 600, rectY + 95);
        rectY1 = rectY;

        for (int i = 0; i < currentPageScrolls.size(); i++) {
            Map<String, String> scroll = currentPageScrolls.get(i);
            String title = scroll.get("name");
            String author = scroll.get("author");
            String uploadDate = scroll.get("publishDate");
            String lastUpdate = scroll.get("lastUpdate");

            parent.stroke(92, 86, 93);
            parent.strokeWeight(2);
            parent.noFill();

            parent.rect(rectX + 40, rectY1 + 100, 160, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(title, rectX + 50, rectY1 + 125);

            parent.noFill();
            parent.rect(rectX + 200, rectY1 + 100, 160, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(author, rectX + 210, rectY1 + 125);

            parent.noFill();
            parent.rect(rectX + 360, rectY1 + 100, 230, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(uploadDate, rectX + 370, rectY1 + 125);

            parent.noFill();
            parent.rect(rectX + 590, rectY1 + 100, 230, rectHeight);
            parent.fill(92, 86, 93);
            parent.text(lastUpdate, rectX + 600, rectY1 + 125);

            if (isMouseOverButton((int) rectX + 780, (int) rectY1 + 103, 40, 40)) {
                parent.fill(216, 202, 220, 200);
            } else  {
                parent.noFill();
            }
            parent.rect(rectX + 780, rectY1 + 100, 40, 40);
            parent.image(downloadImg, rectX + 768, rectY1 + 103);

            rectY1 += rectHeight + 20;
        }

        parent.noStroke();
        if (currentPage > 0) {
            if (isMouseOverButton(rectX + 50, rectY + rectH - 35, 40, 30)) {
                parent.fill(200, 50, 250);
            } else {
                parent.fill(174, 37, 222);
            }
            parent.rect(rectX + 50, rectY + rectH - 35, 40, 30, 5);
            parent.fill(255);
            parent.textSize(35);
            parent.text("<", rectX + 55, rectY + rectH - 10);
        }

        if ((currentPage + 1) * SCROLLS_PER_PAGE < scrolls.size()) {
            if (isMouseOverButton(rectX + rectW - 90, rectY + rectH - 35, 40, 30)) {
                parent.fill(200, 50, 250);
            } else {
                parent.fill(174, 37, 222);
            }
            parent.rect(rectX + rectW - 90, rectY + rectH - 35, 40, 30, 5);
            parent.fill(255);
            parent.textSize(35);
            parent.text(">", rectX + rectW - 83, rectY + rectH - 10);
        }
    }

    private boolean isMouseOverButton(int x, int y, int w, int h) {
        parent.redraw();
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    private boolean isMouseOverButton(float x, float y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    public void mousePressed() {
        if (isMouseOverButton((int) (rectW / 14) * 13, 95, filterImg.width, filterImg.height)) {
            System.out.println("Filter Selected");
            parent.redraw();
            filterScreen.isFilterScreenVisible = true;
            filterScreen.mousePressed();
        }

        if (isMouseOverButton(rectX + rectW - 90, rectY + rectH - 35, 40, 30)) {
            if ((currentPage + 1) * SCROLLS_PER_PAGE < scrolls.size()) {
                currentPage++;
                updateCurrentPageScrolls();
                refreshView();
            }
        }

        if (isMouseOverButton(rectX + 50, rectY + rectH - 35, 40, 30)) {
            if (currentPage > 0) {
                currentPage--;
                updateCurrentPageScrolls();
                refreshView();
            }
        }

        for (int i = 0; i < currentPageScrolls.size(); i++) {
            float downloadX = rectX + 780;
            float downloadY = rectY + 100 + (i * (rectHeight + 20));

            if (isMouseOverButton((int) downloadX, (int) downloadY, downloadImg.width, downloadImg.height)) {
                Map<String, String> selectedScroll = currentPageScrolls.get(i);
                String scrollId = selectedScroll.get("ID");
                String title = selectedScroll.get("name");
                String author = selectedScroll.get("author");
                String uploadDate = selectedScroll.get("publishDate");
                String filePath = selectedScroll.get("filePath");

                System.out.println("Download Selected for scroll: " + title);
                scrollDb.updateNumViews(Integer.parseInt(scrollId));

                previewScreen.setScrollDetails(scrollId, title, author, uploadDate, filePath);
                previewScreen.resetScroll();
                previewScreen.isPreviewScreenVisible = true;
                parent.redraw();
                previewScreen.mousePressed();
                parent.redraw();
            }
        }

        if (username != null && isMouseOverButton((int) rectX, 30, (int) parent.textWidth(username), 10)) {
            System.out.println("User Profile Selected");
            userProfile.isUserProfileVisible = true;
            parent.redraw();
            loginScreen.isViewScrollsUserVisible = false;
            userProfile.mousePressed();
        }
    }

    public void updateCurrentUser(String username) {
        if (currentUser == null) {
            currentUser = new User();
        }
        currentUser.setUsername(username);
    }

    public User getUserObj() {
        return loginScreen.getUserObj();
    }

    public void updateScrolls(List<Map<String, String>> filteredScrolls) {
        this.scrolls = filteredScrolls;
        this.currentPage = 0;
        updateCurrentPageScrolls();
        refreshView();
    }

    private void updateCurrentPageScrolls() {
        int start = currentPage * SCROLLS_PER_PAGE;
        int end = Math.min(start + SCROLLS_PER_PAGE, scrolls.size());
        currentPageScrolls = new ArrayList<>(scrolls.subList(start, end));
    }

    private void refreshView() {
        updateCurrentPageScrolls();
        parent.clear();
        drawScrollsUsers();
        parent.redraw();
    }
}