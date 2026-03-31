package service;

import db.DatabaseManager;
import model.Item;
import model.Item.Type;

import java.sql.SQLException;
import java.util.List;

/**
 * Business logic layer.
 * ConsoleUI talks to ItemService, which talks to DatabaseManager.
 * This separation keeps UI and DB code clean and independent.
 */
public class ItemService {

    private final DatabaseManager db;

    public ItemService(DatabaseManager db) {
        this.db = db;
    }

    // --- Report a new lost item ---
    public Item reportLostItem(String name, String description,
                               String location, String date, String contact) {
        Item item = new Item(Type.LOST, name, description, location, date, contact);
        try {
            int id = db.insertItem(item);
            item.setId(id);
            return item;
        } catch (SQLException e) {
            System.err.println("[Error] Could not save lost item: " + e.getMessage());
            return null;
        }
    }

    // --- Report a new found item ---
    public Item reportFoundItem(String name, String description,
                                String location, String date, String contact) {
        Item item = new Item(Type.FOUND, name, description, location, date, contact);
        try {
            int id = db.insertItem(item);
            item.setId(id);
            return item;
        } catch (SQLException e) {
            System.err.println("[Error] Could not save found item: " + e.getMessage());
            return null;
        }
    }

    // --- Get all currently open (unclaimed) items ---
    public List<Item> getAllOpenItems() {
        try {
            return db.getAllOpenItems();
        } catch (SQLException e) {
            System.err.println("[Error] Could not fetch items: " + e.getMessage());
            return List.of();
        }
    }

    // --- Search items by keyword ---
    public List<Item> searchItems(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            System.out.println("[Warning] Please enter a valid search keyword.");
            return List.of();
        }
        try {
            return db.searchItems(keyword);
        } catch (SQLException e) {
            System.err.println("[Error] Search failed: " + e.getMessage());
            return List.of();
        }
    }

    // --- Mark an item as claimed ---
    // Returns true on success, false if ID not found or already claimed
    public boolean claimItem(int id) {
        try {
            Item item = db.getItemById(id);
            if (item == null) {
                System.out.println("[Error] No item found with ID: " + id);
                return false;
            }
            if (item.getStatus() == Item.Status.CLAIMED) {
                System.out.println("[Info] Item #" + id + " is already marked as CLAIMED.");
                return false;
            }
            return db.markAsClaimed(id);
        } catch (SQLException e) {
            System.err.println("[Error] Could not update item: " + e.getMessage());
            return false;
        }
    }
}
