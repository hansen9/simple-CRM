# Simple CRM Application Design

## 1. Overview
This document describes the design and architecture of the Simple CRM application.

## 2. Main Features
- Manage customer prospect data. Customers can be individuals or companies.
- Customer data includes: name, address (street, city/regency, province, country), contact person(s), status (Seed, Nurture, Lead, Active, Loyal).
- Browse customer data with search by name, city, province, or country. Customer list is displayed in a table. Clicking a column header sorts by that column. Maximum 50 rows per page; user must click next page for more.

## 3. Technology Stack
- Programming Language: Java 21
- Frontend: Java Swing
- Database: H2 in-memory database

## 4. Layered Architecture
- **Presentation Layer**: Java Swing UI for user interaction.
- **Service Layer**: Business logic, validation, and coordination between layers.
- **Repository/DAO Layer**: Data access logic (CRUD, search, pagination, sorting).
- **Model Layer**: Entity classes (Customer, Address, ContactPerson, etc.) with fields and getter/setter methods.

## 5. Database
- Uses H2 in-memory database.
- Tables: CUSTOMER, ADDRESS, CONTACT_PERSON.
- Foreign key relationships according to the model above.

## 6. Technical Notes
- Pagination: Use SQL LIMIT & OFFSET.
- Sorting: Use SQL ORDER BY according to the column selected by the user.
- Swing JTable: use setAutoCreateRowSorter(true) for column sorting.
- Data validation in the Service Layer.

---

**This document is the main reference for developing the Simple CRM application.**

**During development, ensure to provide unit tests and integration tests between layers.**
