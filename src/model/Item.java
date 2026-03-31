package model;

import java.time.LocalDate;

/**
 * Represents a single Lost or Found item entry in the system.
 */
public class Item {

    // Enums for type-safe fields
    public enum Type { LOST, FOUND }
    public enum Status { OPEN, CLAIMED }

    private int id;
    private Type type;
    private String itemName;
    private String description;
    private String location;
    private String date;
    private String contactInfo;
    private Status status;

    // Constructor for creating a new item (no ID yet — DB assigns it)
    public Item(Type type, String itemName, String description,
                String location, String date, String contactInfo) {
        this.type = type;
        this.itemName = itemName;
        this.description = description;
        this.location = location;
        this.date = date;
        this.contactInfo = contactInfo;
        this.status = Status.OPEN; // All new items start as OPEN
    }

    // Constructor for loading an item from the database (ID already exists)
    public Item(int id, Type type, String itemName, String description,
                String location, String date, String contactInfo, Status status) {
        this(type, itemName, description, location, date, contactInfo);
        this.id = id;
        this.status = status;
    }

    // --- Getters ---
    public int getId()            { return id; }
    public Type getType()         { return type; }
    public String getItemName()   { return itemName; }
    public String getDescription(){ return description; }
    public String getLocation()   { return location; }
    public String getDate()       { return date; }
    public String getContactInfo(){ return contactInfo; }
    public Status getStatus()     { return status; }

    // --- Setters ---
    public void setId(int id)           { this.id = id; }
    public void setStatus(Status status){ this.status = status; }

    // Pretty-print for console display
    @Override
    public String toString() {
        return String.format(
            "\n--------------------------------------------------" +
            "\n  ID       : %d" +
            "\n  Type     : %s" +
            "\n  Item     : %s" +
            "\n  Desc     : %s" +
            "\n  Location : %s" +
            "\n  Date     : %s" +
            "\n  Contact  : %s" +
            "\n  Status   : %s" +
            "\n--------------------------------------------------",
            id, type, itemName, description, location, date, contactInfo, status
        );
    }
}
