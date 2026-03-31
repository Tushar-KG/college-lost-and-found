package db;

import model.Item;
import model.Item.Status;
import model.Item.Type;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all SQLite database operations.
 * Uses JDBC to connect to a local SQLite file: lostfound.db
 */
public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:lostfound.db";
    private Connection connection;

    // --- Connect and initialize the database ---
    public void connect() throws SQLException {
    try {
        Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
        throw new SQLException("SQLite JDBC driver not found: " + e.getMessage());
    }
    connection = DriverManager.getConnection(DB_URL);
        System.out.println("[DB] Connected to SQLite database.");
        createTableIfNotExists();
    }

    // --- Create the items table if it doesn't already exist ---
    private void createTableIfNotExists() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS items (
                id          INTEGER PRIMARY KEY AUTOINCREMENT,
                type        TEXT NOT NULL,
                item_name   TEXT NOT NULL,
                description TEXT,
                location    TEXT,
                date        TEXT,
                contact     TEXT,
                status      TEXT NOT NULL DEFAULT 'OPEN'
            );
        """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    // --- Insert a new item, return the generated ID ---
    public int insertItem(Item item) throws SQLException {
        String sql = """
            INSERT INTO items (type, item_name, description, location, date, contact, status)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, item.getType().name());
            pstmt.setString(2, item.getItemName());
            pstmt.setString(3, item.getDescription());
            pstmt.setString(4, item.getLocation());
            pstmt.setString(5, item.getDate());
            pstmt.setString(6, item.getContactInfo());
            pstmt.setString(7, item.getStatus().name());
            pstmt.executeUpdate();

            ResultSet keys = pstmt.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        }
        return -1;
    }

    // --- Get all items with status = OPEN ---
    public List<Item> getAllOpenItems() throws SQLException {
        String sql = "SELECT * FROM items WHERE status = 'OPEN' ORDER BY id DESC";
        return executeQuery(sql);
    }

    // --- Search items by keyword (matches item_name or description) ---
    public List<Item> searchItems(String keyword) throws SQLException {
        String sql = """
            SELECT * FROM items
            WHERE (LOWER(item_name) LIKE ? OR LOWER(description) LIKE ? OR LOWER(location) LIKE ?)
            ORDER BY id DESC
        """;
        String pattern = "%" + keyword.toLowerCase() + "%";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);
            pstmt.setString(3, pattern);
            ResultSet rs = pstmt.executeQuery();
            return mapResultSet(rs);
        }
    }

    // --- Mark an item as CLAIMED by its ID ---
    public boolean markAsClaimed(int id) throws SQLException {
        String sql = "UPDATE items SET status = 'CLAIMED' WHERE id = ? AND status = 'OPEN'";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0; // returns true if a row was updated
        }
    }

    // --- Get a single item by ID ---
    public Item getItemById(int id) throws SQLException {
        String sql = "SELECT * FROM items WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            List<Item> results = mapResultSet(rs);
            return results.isEmpty() ? null : results.get(0);
        }
    }

    // --- Helper: run a no-param query and return list of Items ---
    private List<Item> executeQuery(String sql) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            return mapResultSet(rs);
        }
    }

    // --- Helper: map a ResultSet to a List<Item> ---
    private List<Item> mapResultSet(ResultSet rs) throws SQLException {
        List<Item> items = new ArrayList<>();
        while (rs.next()) {
            items.add(new Item(
                rs.getInt("id"),
                Type.valueOf(rs.getString("type")),
                rs.getString("item_name"),
                rs.getString("description"),
                rs.getString("location"),
                rs.getString("date"),
                rs.getString("contact"),
                Status.valueOf(rs.getString("status"))
            ));
        }
        return items;
    }

    // --- Close the connection cleanly ---
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DB] Connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("[DB] Error closing connection: " + e.getMessage());
        }
    }
}
