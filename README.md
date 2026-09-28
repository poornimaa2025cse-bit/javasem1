# Smart Street Light Controller & Municipal Energy ERP

A Spring Boot backend implementing the full spec: master data, the Procure-to-Pay
and Order-to-Cash accounting cycles, telemetry-driven dimming control with
automated fault detection, zone budgeting, and financial reporting.

**Stack:** Java 17, Spring Boot 3.3.4, Spring Data JPA, MySQL (H2 available as a fallback), Maven.

---

## 1. Prerequisites

- JDK 17 or newer
- Maven 3.8+ (or use your IDE's built-in Maven support - IntelliJ/Eclipse/VS Code all work)
- Internet access the *first* time you build, so Maven can download the
  dependencies listed in `pom.xml` from Maven Central. After that first
  download they're cached locally and you can build offline.

> **Note on how this project was built:** it was written and reviewed file-by-file
> in a sandboxed environment that could not reach Maven Central, so it could not
> be compiled there as a final check. Every file was manually reviewed for
> correctness (imports, method signatures, entity relationships). Please run
> `mvn clean install` as your first step — if anything doesn't compile, tell me
> the exact error and I'll fix it immediately.

## 2. Database setup (MySQL) - do this once before first run

This project is configured to use **MySQL** as its primary database (see
`application.properties`), so that you can open the tables directly in
MySQL Workbench for screenshots in your project report.

1. Install MySQL Community Server + MySQL Workbench if you don't already
   have them (https://dev.mysql.com/downloads/).
2. Open MySQL Workbench (or a terminal) and connect to your local server.
3. Create the database:
   ```sql
   CREATE DATABASE smartlightingdb;
   ```
4. In `src/main/resources/application.properties`, update the username/password
   to match your own MySQL install:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=root   <-- change this to your real password
   ```
5. Run the app (see below). Hibernate (`ddl-auto=update`) will automatically
   create all tables the first time it starts - you do NOT need to write any
   CREATE TABLE statements yourself.

**Don't have MySQL installed / just want a quick test run?** Comment out the
MySQL block in `application.properties` and uncomment the H2 block right
below it - the app will run instantly with a zero-setup in-memory database.
Just remember to switch back to MySQL before taking your report screenshots.

## 3. How to run

```bash
cd smart-lighting-erp
mvn spring-boot:run
```

The app starts on **http://localhost:8080**. On startup, `DataSeeder` automatically
loads demo data (Chart of Accounts, Journals, 3 Contacts, 3 Products, 2 zones,
3 sample poles) — you can start testing endpoints immediately. Because MySQL
is a real persistent database (unlike H2), this data stays there even after
you stop and restart the app — the seeder only inserts it once (on top of
whatever's already there), so don't be surprised if you see duplicate demo
rows after several restarts. If that happens, just drop and recreate the
database (`DROP DATABASE smartlightingdb; CREATE DATABASE smartlightingdb;`)
for a clean slate before a demo.

## 4. Viewing your tables in MySQL (for your report screenshots)

**Option A — MySQL Workbench (GUI, easiest for screenshots):**
1. Open MySQL Workbench and connect to your local MySQL instance.
2. In the **Navigator** panel on the left, expand **Schemas → smartlightingdb → Tables**.
3. You'll see every table Hibernate created from the `@Entity` classes:
   `contacts`, `products`, `accounts`, `journals`, `journal_entries`,
   `journal_entry_lines`, `analytic_accounts`, `budgets`, `purchase_orders`,
   `vendor_bills`, `sales_orders`, `customer_invoices`, `payments`,
   `street_lights`, `telemetry_readings`, `fault_tickets`.
4. Right-click any table → **Select Rows - Limit 1000** to see its data,
   or double-click it and open the **Columns** tab to see its structure
   (exactly what Section 5.2 "Database Schema" in your report needs).
5. To see the whole schema visually as an ER diagram: **Database → Reverse
   Engineer...** → pick `smartlightingdb` → this auto-generates an ERD from
   your live tables, which you can screenshot for Section 5.1.

**Option B — Command line:**
```bash
mysql -u root -p
SHOW DATABASES;
USE smartlightingdb;
SHOW TABLES;
DESCRIBE contacts;          -- see column structure of one table
SELECT * FROM contacts;     -- see the actual seeded rows
```

If you run the app once (so the tables get created) and then look in
Workbench and see nothing, the most common causes are: the app failed to
start (check the console for a connection error), you're looking at the
wrong schema, or the username/password in `application.properties` doesn't
match your local MySQL install.

## 5. Project structure

```
src/main/java/com/citygrid/smartlighting/
 ├── entity/       16 JPA entities + 8 enums (the database model)
 ├── repository/   16 Spring Data JPA repositories
 ├── service/       13 services (business logic + double-entry posting)
 ├── controller/    12 REST controllers
 ├── dto/           7 request/response shape classes
 ├── exception/     custom exceptions + centralised error handling
 ├── config/        AccountCodes constants + DataSeeder (demo data loader)
 └── SmartLightingApplication.java
src/main/resources/
 └── application.properties
```

## 6. Full endpoint reference

### Master Data
| Method | Path | Purpose |
|---|---|---|
| POST/GET | `/api/contacts` | Vendors & customers |
| GET | `/api/contacts/{id}` | Single contact |
| POST/GET | `/api/products` | Goods & services |
| POST/GET | `/api/accounts` | Chart of Accounts |
| POST/GET | `/api/zones` | Analytic Accounts (lighting zones) |

### Purchase cycle (Procure-to-Pay)
| Method | Path | Purpose |
|---|---|---|
| POST/GET | `/api/purchase-orders` | Create/list POs |
| POST | `/api/vendor-bills/from-purchase-order/{poId}` | PO → draft Vendor Bill |
| POST | `/api/vendor-bills/{id}/post` | Post bill to ledger (Debit Expense/Asset, Credit Payable) |
| POST | `/api/vendor-bills/{id}/pay` | Pay via bank (Debit Payable, Credit Bank) |
| GET | `/api/vendor-bills` | List all bills |

### Sales cycle (Order-to-Cash)
| Method | Path | Purpose |
|---|---|---|
| POST/GET | `/api/sales-orders` | Create/list SOs |
| POST | `/api/customer-invoices/from-sales-order/{soId}` | SO → draft Invoice |
| POST | `/api/customer-invoices/{id}/post` | Post invoice (Debit Receivable, Credit Income) |
| POST | `/api/customer-invoices/{id}/receive-payment` | Register receipt (Debit Bank, Credit Receivable) |
| GET | `/api/customer-invoices` | List all invoices |

### Street lights & telemetry
| Method | Path | Purpose |
|---|---|---|
| POST/GET | `/api/street-lights` | Create/list poles |
| PUT | `/api/street-lights/{id}/dimming` | Adjust dimming % |
| POST | `/api/street-lights/{id}/telemetry` | Push a sensor reading (may auto-raise a fault ticket) |
| GET | `/api/street-lights/power-summary` | Zone-wise power rollup |
| GET | `/api/fault-tickets` / `/open` | List all / open fault tickets |
| POST | `/api/fault-tickets/{id}/resolve` | Close a ticket |

### Budget & Reports
| Method | Path | Purpose |
|---|---|---|
| POST/GET | `/api/budgets` | Create/list zone budgets |
| GET | `/api/budgets/variance-report` | Planned vs. actual per zone |
| GET | `/api/reports/balance-sheet` | Assets vs. Liabilities |
| GET | `/api/reports/profit-and-loss` | Income vs. Expenses |

## 7. Step-by-step demo walkthrough (curl)

First, find the seeded IDs (fresh restart = ids start at 1):

```bash
curl http://localhost:8080/api/contacts        # vendor id=1 (utility), id=2 (contractor); customer id=3
curl http://localhost:8080/api/products        # pole id=1, electricity id=2, repair id=3
curl http://localhost:8080/api/zones           # zone id=1 (Highway Zone 7), id=2 (Downtown Zone 3)
curl http://localhost:8080/api/accounts        # note the id of "Cash / Bank" - used as bankAccountId below
curl http://localhost:8080/api/street-lights   # pole id=1, 2, 3
```

### A. Purchase cycle — pay the electricity bill

```bash
# 1. Raise a PO to the utility for grid electricity, tagged to Zone 7
curl -X POST http://localhost:8080/api/purchase-orders \
  -H "Content-Type: application/json" \
  -d '{"vendor":{"id":1},"product":{"id":2},"analyticAccount":{"id":1},"quantity":5000,"unitPrice":8.0}'

# 2. Convert PO id=1 into a draft Vendor Bill
curl -X POST http://localhost:8080/api/vendor-bills/from-purchase-order/1

# 3. Post the bill (Debit Electricity Expense, Credit Accounts Payable)
curl -X POST http://localhost:8080/api/vendor-bills/1/post

# 4. Pay it via bank (use the Cash/Bank account id from step above, e.g. id=1)
curl -X POST http://localhost:8080/api/vendor-bills/1/pay \
  -H "Content-Type: application/json" \
  -d '{"amount":40000,"bankAccountId":1}'
```

### B. Sales cycle — bill the municipality

```bash
curl -X POST http://localhost:8080/api/sales-orders \
  -H "Content-Type: application/json" \
  -d '{"customer":{"id":3},"product":{"id":1},"analyticAccount":{"id":1},"quantity":1,"unitPrice":50000}'

curl -X POST http://localhost:8080/api/customer-invoices/from-sales-order/1
curl -X POST http://localhost:8080/api/customer-invoices/1/post
curl -X POST http://localhost:8080/api/customer-invoices/1/receive-payment \
  -H "Content-Type: application/json" \
  -d '{"amount":50000,"bankAccountId":1}'
```

### C. Trigger the automated fault-ticket rule

```bash
# Pole is "night active" but reporting zero power -> auto fault ticket
curl -X POST http://localhost:8080/api/street-lights/1/telemetry \
  -H "Content-Type: application/json" \
  -d '{"powerDrawWatts":0,"ambientLightLux":2.0,"nightActive":true}'

curl http://localhost:8080/api/fault-tickets/open
```

### D. Reports

```bash
curl http://localhost:8080/api/street-lights/power-summary
curl http://localhost:8080/api/reports/balance-sheet
curl http://localhost:8080/api/reports/profit-and-loss

# Budget variance (create a budget first, period format "yyyy-MM")
curl -X POST http://localhost:8080/api/budgets \
  -H "Content-Type: application/json" \
  -d '{"analyticAccount":{"id":1},"period":"2026-09","plannedAmount":100000}'
curl http://localhost:8080/api/budgets/variance-report
```

## 8. Design notes & assumptions (good to know for your viva)

- **Lombok was intentionally NOT used** — every getter/setter is written out
  explicitly so the code is easy to read and explain line-by-line, matching
  the accompanying "Spring Boot Complete Notes" document.
- **One shared "Accounts Payable" account** is used for both the electricity
  utility and the maintenance contractor, since the spec's Chart of Accounts
  only lists one liability account ("Power Utility Payables"). This is a
  simplification you can call out if asked — a larger system would usually
  split payables per vendor type.
- **Actual budget spend is computed live** from posted Vendor Bills rather
  than stored, so the variance report is always accurate even if bills are
  posted after a budget is created.
- **`ddl-auto=update`** lets Hibernate auto-create tables in MySQL from the
  `@Entity` classes — great for a student project; a production system would
  use Flyway/Liquibase migrations instead. This is why you never had to
  write a single `CREATE TABLE` statement yourself, but the tables still
  show up perfectly normally in MySQL Workbench, as if you had.
- Every double-entry transaction is validated with `JournalEntry.isBalanced()`
  before saving — this is the one rule that must never break in accounting
  software, and it's a great thing to point out live in your viva.
