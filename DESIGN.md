# Simple CRM - Design Document

## 1. Entitas & Relasi

### Customer
- **id**: Long (Primary Key)
- **nama**: String
- **tipe**: Enum (PERORANGAN, PERUSAHAAN)
- **status**: Enum (SEED, NURTURE, LEAD, ACTIVE, LOYAL)
- **alamat**: Address (One-to-One)
- **contactPersons**: List<ContactPerson> (One-to-Many)

### Address
- **id**: Long (Primary Key)
- **jalan**: String
- **kota**: String
- **propinsi**: String
- **negara**: String

### ContactPerson
- **id**: Long (Primary Key)
- **customerId**: Long (Foreign Key ke Customer)
- **nama**: String
- **nomorTelepon**: String
- **email**: String

## 2. Relasi Antar Entitas
- Satu Customer memiliki satu Address.
- Satu Customer dapat memiliki banyak ContactPerson.

## 3. Fitur Utama
- CRUD data customer.
- CRUD data contact person (per customer, bisa lebih dari satu).
- Pencarian customer (by name, kota, propinsi, negara).
- Tabel data customer dengan sorting per kolom.
- Pagination (maksimal 50 baris per halaman).

## 4. Layered Architecture

### 4.1. Presentation Layer (UI)
- Java Swing
- Komponen: Main window, form input customer, tabel data, panel pencarian, dsb.
- Hanya berinteraksi dengan Service Layer.

### 4.2. Service Layer (Business Logic)
- Kelas: CustomerService, ContactPersonService, dsb.
- Validasi data, pagination, sorting, dsb.
- Berinteraksi dengan DAO Layer.

### 4.3. Data Access Layer (DAO/Repository)
- Kelas: CustomerDAO, AddressDAO, ContactPersonDAO, dsb.
- CRUD dan query ke database H2 (JDBC).

### 4.4. Model/Entity Layer
- Kelas: Customer, Address, ContactPerson, dsb.
- Berisi field dan getter/setter.

## 5. Database
- Menggunakan H2 in-memory database.
- Tabel: CUSTOMER, ADDRESS, CONTACT_PERSON.
- Relasi foreign key sesuai model di atas.

## 6. Catatan Teknis
- Pagination: SQL LIMIT & OFFSET.
- Sorting: SQL ORDER BY sesuai kolom yang dipilih user.
- Swing JTable: gunakan setAutoCreateRowSorter(true) untuk sorting kolom.
- Validasi data di Service Layer.

---

**Dokumen ini menjadi acuan pengembangan aplikasi Simple CRM.**

**Dalam pembangunan aplikasi, pastikan untuk menyediakan unit test dan integration test antar layer.**
