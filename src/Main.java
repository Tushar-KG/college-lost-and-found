import db.DatabaseManager;
import service.ItemService;
import ui.ConsoleUI;

import java.sql.SQLException;

/**
 * Entry point for the College Lost & Found System.
 * Wires together DatabaseManager → ItemService → ConsoleUI and starts the app.
 */
public class Main {

    public static void main(String[] args) {

        // 1. Set up the database
        DatabaseManager db = new DatabaseManager();
        try {
            db.connect();
        } catch (SQLException e) {
            System.err.println("[Fatal] Could not connect to database: " + e.getMessage());
            System.err.println("Make sure sqlite-jdbc.jar is in your classpath.");
            return;
        }

        // 2. Wire up the service and UI layers
        ItemService service = new ItemService(db);
        ConsoleUI ui = new ConsoleUI(service);

        // 3. Add a shutdown hook so the DB closes cleanly even on Ctrl+C
        Runtime.getRuntime().addShutdownHook(new Thread(db::close));

        // 4. Start the application
        ui.start();

        // 5. Close DB on normal exit
        db.close();
    }
}
