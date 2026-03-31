# 📦 College Lost & Found System

A Java-based command-line application that helps college students report lost items, log found items, search the database, and mark items as claimed — all stored persistently using SQLite.

---

## 🧩 Problem Statement

On a busy college campus, students frequently lose belongings — water bottles, ID cards, earphones, books, and more. There is often no centralized, accessible way to report or search for lost items. This project solves that by providing a simple, persistent digital Lost & Found system anyone can run locally.

---

## ✨ Features

| Feature | Description |
|---|---|
| Report Lost Item | Log details of something you've lost |
| Report Found Item | Log something you found on campus |
| View All Open Items | See every unresolved lost/found entry |
| Search by Keyword | Filter items by name, description, or location |
| Mark as Claimed | Close an entry once the item is returned |
| SQLite Persistence | All data saved to a local `.db` file |

---

## 🏗️ Project Structure

```
LostAndFound/
├── src/
│   ├── Main.java                  ← Entry point
│   ├── model/
│   │   └── Item.java              ← Data model
│   ├── db/
│   │   └── DatabaseManager.java   ← SQLite/JDBC layer
│   ├── service/
│   │   └── ItemService.java       ← Business logic
│   └── ui/
│       └── ConsoleUI.java         ← Console interface
├── lib/
│   └── sqlite-jdbc-3.x.x.jar     ← SQLite JDBC driver
├── lostfound.db                   ← Auto-created on first run
└── README.md
```

---

## ⚙️ Prerequisites

- Java JDK 17 or higher
- [SQLite JDBC Driver](https://github.com/xerial/sqlite-jdbc/releases) (`.jar` file)

---

## 🚀 Setup & Run

### Step 1 — Clone the repository
```bash
git clone https://github.com/YOUR_USERNAME/college-lost-and-found.git
cd college-lost-and-found
```

### Step 2 — Download SQLite JDBC driver
Download `sqlite-jdbc-3.x.x.jar` from [xerial/sqlite-jdbc releases](https://github.com/xerial/sqlite-jdbc/releases) and place it in the `lib/` folder.

### Step 3 — Compile
```bash
javac -cp "lib/sqlite-jdbc-3.x.x.jar" -d out src/Main.java src/model/Item.java src/db/DatabaseManager.java src/service/ItemService.java src/ui/ConsoleUI.java
```

### Step 4 — Run
```bash
java -cp "out:lib/sqlite-jdbc-3.x.x.jar" Main
```

> **Windows users:** Replace `:` with `;` in the classpath:
> ```bash
> java -cp "out;lib/sqlite-jdbc-3.x.x.jar" Main
> ```

---

## 🖥️ Usage

On launch, you'll see:

```
╔══════════════════════════════════════════════╗
║     COLLEGE LOST & FOUND SYSTEM              ║
╚══════════════════════════════════════════════╝

┌─────────────────────────────┐
│         MAIN MENU           │
├─────────────────────────────┤
│  1. Report Lost Item        │
│  2. Report Found Item       │
│  3. View All Open Items     │
│  4. Search Items            │
│  5. Mark Item as Claimed    │
│  6. Exit                    │
└─────────────────────────────┘
```

**Example — Reporting a lost item:**
```
Enter choice: 1

========== REPORT LOST ITEM ==========
  Item name      : Water Bottle
  Description    : Blue Nalgene with stickers
  Last seen at   : Library 2nd floor
  Date           : 2024-11-20
  Your contact   : 9876543210

  ✔ Lost item reported successfully! Your entry ID is: #3
```

**Example — Searching:**
```
Enter choice: 4
  Enter keyword  : bottle

  Found 1 result(s) for "bottle":

--------------------------------------------------
  ID       : 3
  Type     : LOST
  Item     : Water Bottle
  Desc     : Blue Nalgene with stickers
  Location : Library 2nd floor
  Date     : 2024-11-20
  Contact  : 9876543210
  Status   : OPEN
--------------------------------------------------
```

---

## 🗄️ Database

The app automatically creates `lostfound.db` in the project root on first run. The `items` table schema:

```sql
CREATE TABLE items (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    type        TEXT NOT NULL,       -- 'LOST' or 'FOUND'
    item_name   TEXT NOT NULL,
    description TEXT,
    location    TEXT,
    date        TEXT,
    contact     TEXT,
    status      TEXT NOT NULL        -- 'OPEN' or 'CLAIMED'
);
```

---

## 🧠 Concepts Demonstrated

- Object-Oriented Programming (classes, enums, encapsulation)
- Layered architecture (Model → DB → Service → UI)
- JDBC and SQLite integration
- Prepared statements (SQL injection prevention)
- Java Collections (`List`, `ArrayList`)
- Exception handling
- Java Text Blocks (multi-line strings)

---

## 👤 Author

**Tushar Kant Gupta**  
VIT Bhopal University | 24BAI10464
GitHub: https://github.com/Tushar-KG
