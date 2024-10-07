package ScrollSystem.Users;

import ScrollSystem.Users.User;
import ScrollSystem.Users.Admin;
import ScrollSystem.Users.Guest;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TerminalTest {
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser;

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- Virtual Scroll Access System ---");
            System.out.println("1. Register");
            System.out.println("2. Login as User");
            System.out.println("3. Login as Admin");
            System.out.println("4. Continue as Guest");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    register();
                    break;
                case 2:
                    loginAsUser();
                    break;
                case 3:
                    loginAsAdmin();
                    break;
                case 4:
                    continueAsGuest();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }


    private static void register() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter full name: ");
        String fullName = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter phone number: ");
        String phoneNo = scanner.nextLine();

        User user = new User();
        if (user.register(username, password, fullName, email, phoneNo)) {
            System.out.println("Registration successful! You can now login.");
        } else {
            System.out.println("Registration failed. Please try again.");
        }
    }
    private static void loginAsUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = new User();
        if (user.login(username, password)) {
            System.out.println("Login successful!");
            currentUser = user;
            userMenu();
        } else {
            System.out.println("Login failed. Please try again.");
        }
    }

    private static void loginAsAdmin() {
        System.out.print("Enter admin username: ");
        String username = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();

        Admin admin = new Admin();
        if (admin.login(username, password)) {
            System.out.println("Admin login successful!");
            currentUser = admin;
            adminMenu();
        } else {
            System.out.println("Admin login failed. Please try again.");
        }
    }

    private static void continueAsGuest() {
        currentUser = new Guest();
        guestMenu();
    }

    private static void userMenu() {
        while (true) {
            System.out.println("\n--- User Menu ---");
            System.out.println("1. View User Info");
            System.out.println("2. Update User Info");
            System.out.println("3. View All Scrolls");
            System.out.println("4. View Scroll by ID");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewUserInfo();
                    break;
                case 2:
                    updateUserInfo();
                    break;
                case 3:
                    viewAllScrolls();
                    break;
                case 4:
                    viewScrollById();
                    break;
                case 5:
                    currentUser = null;
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void adminMenu() {
        Admin admin = (Admin) currentUser;
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. View All Users");
            System.out.println("2. Add User");
            System.out.println("3. Delete User");
            System.out.println("4. View All Scrolls");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewAllUsers(admin);
                    break;
                case 2:
                    addUser(admin);
                    break;
                case 3:
                    deleteUser(admin);
                    break;
                case 4:
                    viewAllScrolls();
                    break;
                case 5:
                    currentUser = null;
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void guestMenu() {
        while (true) {
            System.out.println("\n--- Guest Menu ---");
            System.out.println("1. View All Scrolls");
            System.out.println("2. Return to Main Menu");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewAllScrolls();
                    break;
                case 2:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void viewUserInfo() {
        Map<String, String> userInfo = currentUser.getUserInfo();
        System.out.println("\nUser Information:");
        for (Map.Entry<String, String> entry : userInfo.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private static void updateUserInfo() {
        System.out.print("Enter new full name: ");
        String fullName = scanner.nextLine();
        System.out.print("Enter new email: ");
        String email = scanner.nextLine();
        System.out.print("Enter new phone number: ");
        String phoneNo = scanner.nextLine();

        currentUser.updateUserInfo(fullName, email, phoneNo);
        System.out.println("User information updated successfully.");
    }

    private static void viewAllScrolls() {
//        List<Map<String, String>> scrolls = ((Guest) currentUser).viewAllScrolls();
//        System.out.println("\nAll Scrolls:");
//        for (Map<String, String> scroll : scrolls) {
//            System.out.println("ID: " + scroll.get("ID") + ", Name: " + scroll.get("name") + ", Author: " + scroll.get("author"));
//        }
    }

    private static void viewScrollById() {
        System.out.print("Enter scroll ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Map<String, String> scroll = currentUser.getScrollById(id);
        if (scroll != null) {
            System.out.println("\nScroll Information:");
            for (Map.Entry<String, String> entry : scroll.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        } else {
            System.out.println("Scroll not found.");
        }
    }

    private static void viewAllUsers(Admin admin) {
        List<Map<String, String>> users = admin.getAllUsers();
        System.out.println("\nAll Users:");
        for (Map<String, String> user : users) {
            System.out.println("Username: " + user.get("username") + ", Full Name: " + user.get("fullName"));
        }
    }

    private static void addUser(Admin admin) {
        System.out.print("Enter new username: ");
        String username = scanner.nextLine();
        System.out.print("Enter new password: ");
        String password = scanner.nextLine();
        System.out.print("Enter full name: ");
        String fullName = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter phone number: ");
        String phoneNo = scanner.nextLine();

        if (admin.addUser(username, password, fullName, email, phoneNo)) {
            System.out.println("User added successfully.");
        } else {
            System.out.println("Failed to add user.");
        }
    }

    private static void deleteUser(Admin admin) {
        System.out.print("Enter username to delete: ");
        String username = scanner.nextLine();

        if (admin.deleteUser(username)) {
            System.out.println("User deleted successfully.");
        } else {
            System.out.println("Failed to delete user.");
        }
    }
}