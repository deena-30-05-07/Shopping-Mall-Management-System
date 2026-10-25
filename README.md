# Shopping Mall Management System

A web application to manage the products, stock, billing and sales transactions of a shopping mall. Built as a mini project for CS25C07 - Object Oriented Programming, SRM TRP Engineering College.

## Features

- **Login with two roles**: Admin and Cashier, with encrypted (BCrypt) passwords
- **Product management**: add, view and delete products (Admin)
- **Stock management**: restock products, with automatic stock reduction on every sale
- **Billing counter**: select quantities, stock check, 18% GST and a printable bill
- **Sales history**: every bill is saved and can be reopened later
- **Dashboard (Admin)**: total bills, revenue, GST collected, average bill, best-selling item and low-stock alerts (5 units or fewer)

## Technologies Used

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot (Spring Web, Spring Data JPA, Spring Security) |
| Front end | HTML, Thymeleaf, Bootstrap 5 |
| Database | MySQL |
| Build tool | Maven |

## Roles and Demo Logins

| Role | Username | Password | Access |
|---|---|---|---|
| Admin | `admin` | `admin123` | Everything: products, stock, billing, sales, dashboard |
| Cashier | `cashier` | `cashier123` | Billing, bills, sales history, and viewing products |

These are demo accounts for a mini project. Change them in `SecurityConfig.java` before any real use.

## How to Run

**Requirements:** JDK 21, MySQL Server, and an IDE such as IntelliJ IDEA (optional).

1. Clone the repository:
   ```
   git clone https://github.com/deena-30-05-07/Shopping-Mall-Management-System.git
   cd Shopping-Mall-Management-System
   ```
2. Create the database in MySQL:
   ```sql
   CREATE DATABASE mallapp_db;
   ```
3. Open `src/main/resources/application.properties` and put your own MySQL password:
   ```
   spring.datasource.password=YOUR_PASSWORD_HERE
   ```
4. Start the application, either by running `MallappApplication` in your IDE or from the terminal:
   ```
   ./mvnw spring-boot:run
   ```
   On Windows PowerShell use `.\mvnw spring-boot:run`.
5. Open **http://localhost:8080** in your browser and log in.

The tables (`product`, `sale`, `sale_item`) are created automatically on the first run.

## Pages

| Address | Page | Access |
|---|---|---|
| `/login` | Login | Everyone |
| `/products` | Product list, add, restock, delete | Admin and Cashier (changes: Admin only) |
| `/billing` | Billing counter | Admin and Cashier |
| `/bill/{id}` | Printable bill | Admin and Cashier |
| `/sales` | Sales history | Admin and Cashier |
| `/reports` | Dashboard | Admin only |

## Database Design

- **product**: id, name, category, price, stock
- **sale**: id, customer_name, date_time, subtotal, gst, total
- **sale_item**: id, sale_id (foreign key to sale), product_id, product_name, price, qty, line_total

One sale contains many sale items. The product name and price are copied into each sale item, so old bills never change.

## Project Structure

```
src/main/java/com/mall/mallapp/
  MallappApplication.java
  config/        SecurityConfig
  controller/    ProductController, BillingController, ReportController, LoginController
  model/         Product, Sale, SaleItem
  repository/    ProductRepository, SaleRepository
src/main/resources/
  application.properties
  templates/     login, products, billing, bill, sales, reports (.html)
```

## Screenshots
Login Page:
<img width="1362" height="605" alt="image" src="https://github.com/user-attachments/assets/d02f8c13-c4a6-4170-89c0-569bbf283b1e" />
Admin Dashboard :
<img width="1355" height="585" alt="image" src="https://github.com/user-attachments/assets/38f638e0-a0e8-4abf-8938-f09a831ec136" />
Cashier Dashboard :
<img width="1366" height="599" alt="image" src="https://github.com/user-attachments/assets/76df64e0-b845-4057-9b44-0d5882884bc8" />


## Future Enhancements

- Store users in the database with a registration screen
- Product editing, search and category filters
- Different GST rates and discount coupons
- Export bills and reports to PDF or Excel
- Sales charts and barcode scanning

## Author

Deenadhayalan S | B.E. CSE(AIML), SRM TRP Engineering College
