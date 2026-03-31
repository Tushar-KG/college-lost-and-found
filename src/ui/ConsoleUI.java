package ui;

import model.Item;
import service.ItemService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Handles all console input and output.
 * Calls ItemService for actual logic - never talks to DB directly.
 */
public class ConsoleUI {

    private final ItemService service;
    private final Scanner scanner;

    public ConsoleUI(ItemService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    // --- Main application loop ---
    public void start() {
        printBanner();
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> reportLost();
                case "2" -> reportFound();
                case "3" -> viewAllOpen();
                case "4" -> searchItems();
                case "5" -> claimItem();
                case "6" -> {
                    System.out.println("\n  Goodbye! Stay safe on campus. 👋\n");
                    running = false;
                }
                default -> System.out.println("\n  [!] Invalid option. Please enter 1–6.\n");
            }
        }
    }

    // --- Option 1: Report a lost item ---
    private void reportLost() {
        System.out.println("\n========== REPORT LOST ITEM ==========");
        String name   = prompt("  Item name      : ");
        String desc   = prompt("  Description    : ");
        String loc    = prompt("  Last seen at   : ");
        String date   = promptDate();
        String contact = prompt("  Your contact   : ");

        Item item = service.reportLostItem(name, desc, loc, date, contact);
        if (item != null) {
            System.out.println("\n  ✔ Lost item reported successfully! Your entry ID is: #" + item.getId());
            System.out.println("  Keep this ID — you'll need it to mark the item as claimed.");
        }
    }

    // --- Option 2: Report a found item ---
    private void reportFound() {
        System.out.println("\n========== REPORT FOUND ITEM ==========");
        String name    = prompt("  Item name      : ");
        String desc    = prompt("  Description    : ");
        String loc     = prompt("  Found at       : ");
        String date    = promptDate();
        String contact = prompt("  Your contact   : ");

        Item item = service.reportFoundItem(name, desc, loc, date, contact);
        if (item != null) {
            System.out.println("\n  ✔ Found item reported successfully! Entry ID: #" + item.getId());
            System.out.println("  Thank you for helping your campus community!");
        }
    }

    // --- Option 3: View all open items ---
    private void viewAllOpen() {
        System.out.println("\n========== ALL OPEN ITEMS ==========");
        List<Item> items = service.getAllOpenItems();

        if (items.isEmpty()) {
            System.out.println("  No open items found.");
        } else {
            System.out.println("  Found " + items.size() + " open item(s):\n");
            items.forEach(System.out::println);
        }
        System.out.println();
    }

    // --- Option 4: Search items by keyword ---
    private void searchItems() {
        System.out.println("\n========== SEARCH ITEMS ==========");
        String keyword = prompt("  Enter keyword  : ");

        List<Item> results = service.searchItems(keyword);

        if (results.isEmpty()) {
            System.out.println("  No items matched \"" + keyword + "\".");
        } else {
            System.out.println("  Found " + results.size() + " result(s) for \"" + keyword + "\":\n");
            results.forEach(System.out::println);
        }
        System.out.println();
    }

    // --- Option 5: Mark an item as claimed ---
    private void claimItem() {
        System.out.println("\n========== MARK ITEM AS CLAIMED ==========");
        String input = prompt("  Enter item ID  : ");

        try {
            int id = Integer.parseInt(input.trim());
            boolean success = service.claimItem(id);
            if (success) {
                System.out.println("\n  ✔ Item #" + id + " has been marked as CLAIMED.");
            } else {
                System.out.println("\n  Could not mark item as claimed. Check the ID and try again.");
            }
        } catch (NumberFormatException e) {
            System.out.println("\n  [!] Invalid ID. Please enter a number.");
        }
        System.out.println();
    }

    // --- Utility: print the welcome banner ---
    private void printBanner() {
        System.out.println("\n================================================");
        System.out.println("       COLLEGE LOST AND FOUND SYSTEM");
        System.out.println("   Helping students reunite with their things");
        System.out.println("================================================\n");
    }

    // --- Utility: print the main menu ---
    private void printMenu() {
        System.out.println("---------------------------");
        System.out.println("         MAIN MENU         ");
        System.out.println("---------------------------");
        System.out.println("  1. Report Lost Item");
        System.out.println("  2. Report Found Item");
        System.out.println("  3. View All Open Items");
        System.out.println("  4. Search Items");
        System.out.println("  5. Mark Item as Claimed");
        System.out.println("  6. Exit");
        System.out.println("---------------------------");
        System.out.print("Enter choice: ");
    }

    // --- Utility: prompt user for a non-blank string ---
    private String prompt(String message) {
        String input;
        do {
            System.out.print(message);
            input = scanner.nextLine().trim();
            if (input.isBlank()) {
                System.out.println("  [!] This field cannot be empty.");
            }
        } while (input.isBlank());
        return input;
    }

    // --- Utility: prompt for date, default to today if blank ---
    private String promptDate() {
        System.out.print("  Date (YYYY-MM-DD, or press Enter for today): ");
        String input = scanner.nextLine().trim();
        return input.isBlank() ? LocalDate.now().toString() : input;
    }
}
