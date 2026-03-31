# Project Report
## College Lost & Found System
### BYOP — Bring Your Own Project Submission

---

**Student Name:** [Your Name]  
**Roll Number:** [Your Roll Number]  
**Course:** Programming in Java  
**Institution:** [Your College Name]  
**Submission Date:** [Date]  
**GitHub Repository:** [Your GitHub Link]

---

## 1. Problem Statement

Every day on a college campus, students lose personal belongings — water bottles, ID cards, earphones, chargers, notebooks, keys, and more. In most colleges, there is no organized system to handle this. A lost item is either left where it was found, handed to a security guard with no tracking, or simply never reunited with its owner.

This creates a real, everyday frustration for students. The problem is not the absence of goodwill — many students who find items genuinely want to return them — but the absence of a reliable, accessible platform to connect finders with losers.

This project addresses that gap with a simple, practical, and persistent digital Lost & Found system.

---

## 2. Why This Problem Matters

- **Frequency:** Lost items are a near-daily occurrence in any institution with hundreds of students.
- **Impact:** Losing an ID card, a key, or a textbook can disrupt a student's entire day or week.
- **Solvability:** This is a well-scoped problem that a small software tool can meaningfully improve.
- **Community value:** A working system benefits every student on campus, not just a few.

The solution does not require internet access, a server, or an account — just a shared machine running the program, making it realistic to deploy even in low-resource environments.

---

## 3. Proposed Solution

A Java-based command-line application that allows any student to:

1. Report an item they have **lost** (with name, description, location, date, and contact)
2. Report an item they have **found**
3. **Browse** all currently open (unresolved) entries
4. **Search** the database by keyword
5. **Mark** an item as claimed once it has been returned

All data is stored persistently in a local SQLite database using JDBC, so records survive across sessions.

---

## 4. System Design

### Architecture

The project follows a layered, three-tier architecture:

```
ConsoleUI  →  ItemService  →  DatabaseManager  →  SQLite (.db file)
```

| Layer | Class | Responsibility |
|---|---|---|
| Model | `Item.java` | Data structure for a lost/found entry |
| Database | `DatabaseManager.java` | JDBC connection, SQL queries |
| Service | `ItemService.java` | Business logic, validation |
| UI | `ConsoleUI.java` | User interaction, console I/O |
| Entry Point | `Main.java` | Wiring and startup |

### Why This Architecture?

Separating concerns into distinct layers makes the code:
- **Easier to maintain** — changing the database doesn't touch the UI
- **Easier to test** — each layer can be tested in isolation
- **More readable** — each class has one clear job

### Data Model

Each `Item` has:
- A unique auto-incremented ID
- Type: `LOST` or `FOUND` (Java enum)
- Name, description, location, date, contact
- Status: `OPEN` or `CLAIMED` (Java enum)

### Database

SQLite was chosen because:
- It requires no server or installation
- The entire database is a single `.db` file
- It is widely used in real applications (Android, browsers, etc.)
- JDBC makes it easy to integrate with Java

---

## 5. Key Implementation Decisions

### Decision 1: Enums for Type and Status
Instead of storing raw strings like `"lost"` or `"open"`, I used Java enums (`Item.Type`, `Item.Status`). This prevents invalid values from ever entering the system and makes the code self-documenting.

### Decision 2: Prepared Statements for All Queries
All database queries use `PreparedStatement` instead of string concatenation. This protects against SQL injection — a critical security practice even in small projects.

### Decision 3: Service Layer Between UI and DB
Rather than calling database methods directly from the UI, all calls go through `ItemService`. This means the UI never needs to know about SQL, and the database layer never needs to know about console formatting.

### Decision 4: Shutdown Hook for Clean DB Closure
A `Runtime.getRuntime().addShutdownHook()` ensures the database connection is properly closed even if the user presses Ctrl+C, preventing potential data corruption.

### Decision 5: Text Blocks for Readability
Java 15+ text blocks (`"""..."""`) were used for multi-line SQL and console strings, making code significantly more readable.

---

## 6. Features Implemented

| Feature | Status |
|---|---|
| Report lost item | ✅ Complete |
| Report found item | ✅ Complete |
| View all open items | ✅ Complete |
| Search by keyword (name/desc/location) | ✅ Complete |
| Mark item as claimed | ✅ Complete |
| Persistent SQLite storage | ✅ Complete |
| Input validation (no blank fields) | ✅ Complete |
| Graceful DB shutdown on exit | ✅ Complete |

---

## 7. Challenges Faced

### Challenge 1: Setting Up JDBC with SQLite
Integrating an external `.jar` driver into the classpath was initially confusing. I learned how Java's classpath works and how to compile and run programs that depend on external libraries.

**Resolution:** Placed the SQLite JDBC driver in a `lib/` folder and explicitly included it in both compile and run commands.

### Challenge 2: Handling the ResultSet Correctly
The JDBC `ResultSet` must be read while the connection is open, and it is forward-only. Early on, I tried to close the statement before reading the results, which caused an exception.

**Resolution:** Moved result processing into a helper method (`mapResultSet`) that reads all rows into a `List<Item>` before the statement closes.

### Challenge 3: Layered Architecture Design
Initially, I was unsure how to cleanly separate the UI, logic, and database layers without over-engineering. 

**Resolution:** I used the principle of "each class should have one reason to change" — `ConsoleUI` changes only if the interface changes; `DatabaseManager` changes only if the database schema changes; `ItemService` changes only if business rules change.

---

## 8. What I Learned

- **JDBC and SQLite integration:** How to connect Java applications to a relational database, execute queries with prepared statements, and handle result sets.
- **Layered architecture:** How to structure a real application across model, data, service, and UI layers — and why this matters.
- **Java enums:** Using enums for type safety instead of raw string comparisons.
- **Exception handling in a layered system:** How to catch `SQLException` at the service layer so the UI always receives clean results.
- **Version control discipline:** Committing code incrementally with meaningful commit messages rather than uploading everything at once.
- **README writing:** How to document a project clearly enough that a stranger could clone and run it without help.

---

## 9. Future Improvements

If more time were available, the following enhancements would make the system more complete:

1. **Admin login** — Separate view for wardens/staff to manage all entries
2. **Filter by type** — View only LOST or only FOUND items
3. **Email/SMS notification** — Alert a reporter when a match is found
4. **Date range filter** — Search items reported in a specific period
5. **Swing GUI** — Replace the console with a simple graphical interface
6. **Export to CSV** — Let admins download a report of all entries

---

## 10. Conclusion

This project successfully demonstrates how core Java concepts — object-oriented design, enums, collections, exception handling, and JDBC — can be applied to solve a real, everyday problem faced by students on a college campus.

The result is a working, persistent, and well-structured application that is immediately useful and can realistically be deployed at any institution. More importantly, the development process reinforced the value of clean architecture, separation of concerns, and thoughtful design decisions — skills that extend well beyond this particular project.

---

## References

- Oracle Java Documentation: https://docs.oracle.com/en/java/
- SQLite JDBC Driver (xerial): https://github.com/xerial/sqlite-jdbc
- SQLite Official Documentation: https://www.sqlite.org/docs.html
- Effective Java, Joshua Bloch — for design patterns and best practices
