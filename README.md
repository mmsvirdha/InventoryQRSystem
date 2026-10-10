# 📦 Inventory QR System

A desktop **Inventory Management System** built with **Java Swing** and **MySQL**, featuring **QR-code based login**, role-based permissions, and webcam QR scanning.

> Developed by **Virdha** — NetBeans Java Project

---

## ✨ Features

- 🔐 **Secure Login** – username/password login against the MySQL database
- 📷 **QR Code Login** – scan a user's QR code with a webcam to log in
- 🔑 **Secret Code Login** – alternative QR-user login using username + secret code
- 🧾 **QR Code Generation** – generate a QR for any user; the PNG is saved and stored in the database
- 👥 **User Management** – add, edit and manage users and their details
- 🛡️ **Role & Permission System** – per-user permissions:
  - Generate QR · Scan QR · Manage Users · Manage Products
  - View Scan History · View User Logs · Access Settings
- 📦 **Product / Inventory Management**
- 🕓 **Scan History & User Logs**
- 🎨 **Modern UI** – animated login background, fade transitions between screens

---

## 🛠️ Tech Stack

| Component | Technology |
|-----------|-----------|
| Language | Java (JDK 17 or later recommended) |
| UI | Java Swing (NetBeans GUI Builder) |
| Database | MySQL 8.x |
| QR Generate / Read | ZXing (`core` + `javase`) |
| Webcam | webcam-capture (sarxos) |
| JSON | org.json |
| DB Driver | MySQL Connector/J |
| IDE | Apache NetBeans |

---

## 📁 Project Structure

```
InventoryQRSystem/
├── src/
│   ├── login/          LoginForm, QRScannerForm, GenerateQRDialog, AnimatedBackgroundPanel
│   ├── dashboard/      Dashboard and inventory screens
│   ├── utils/          DBConnection, TransitionHelper, centerFrame
│   └── InventoryImages/  Icons and GIFs used by the UI
├── dist/
│   ├── InventoryQRSystem.jar   Ready-to-run application
│   └── lib/                    All required .jar libraries
├── QR Code Generation/         Generated QR images are saved here
├── database/
│   └── inventory_db.sql        Database export 
└── README.md
```

---

## 🚀 Setup Guide

### 1. Prerequisites

- JDK 17+ installed
- Apache NetBeans (recommended) 
- MySQL Server 8.x + MySQL Workbench
- A **webcam** (needed only for QR scanning)

### 2. Import the database

1. Open **MySQL Workbench** and connect to your local server.
2. Go to **Server → Data Import**.
3. Choose **Import from Self-Contained File** and select `database/inventory_db.sql`.
4. Under *Default Target Schema* click **New…** and create `inventory_db`, then click **Start Import**.

Or run from the command line:

```bash
mysql -u root -p -e "CREATE DATABASE inventory_db;"
mysql -u root -p inventory_db < database/inventory_db.sql
```

### 3. Configure the database connection

Open `src/utils/DBConnection.java` and set your own MySQL details:

```java
String url  = "jdbc:mysql://localhost:3306/inventory_db";
String user = "root";
String pass = "YOUR_MYSQL_PASSWORD";
```

### 4. Run the application

Open the `dist` folder and run:

```bash
java -jar InventoryQRSystem.jar
```

(or double-click `InventoryQRSystem.jar`). All required libraries are already inside `dist/lib`, so **keep the `lib` folder next to the JAR**.

**Running from NetBeans instead:** open the project, right-click it → **Clean and Build**, then press **F5 / Run**. If NetBeans shows broken-library icons, right-click the project → **Resolve Reference Problems** and select the JARs in `dist/lib`.

---

## 🔑 Demo Login (for testing)

### Username & Password login

| Field | Value |
|-------|-------|
| Username | `demo` |
| Password | `demo123` |
| Role | Admin (all permissions enabled) |

### QR "Secret Code" login

| Field | Value |
|-------|-------|
| Username | `demo` |
| Secret Code | `DEMO-1234` |

> If the demo users are missing, run this in MySQL Workbench:
>
> ```sql
> USE inventory_db;
>
> INSERT INTO users (username, password, role, full_name, email, contact_no, status,
>  can_generate_qr, can_scan_qr, can_manage_users, can_manage_products,
>  can_view_scan_history, can_view_user_logs, can_access_settings)
> VALUES ('demo', 'demo123', 'Admin', 'Demo User', 'demo@example.com', '0771234567', 'Active',
>  1, 1, 1, 1, 1, 1, 1);
>
> INSERT INTO qr_users (username, password, fullname, role, secret)
> VALUES ('demo', 'demo123', 'Demo User', 'Admin', 'DEMO-1234');
> ```

---

## 🧪 How to Test the Whole System

1. **Normal login** – enter `demo` / `demo123` → the Dashboard opens.
2. **Generate a QR code** – open the *Generate QR* form, fill in username, password, full name, secret code and role, then click **Generate**. The QR PNG is saved in the `QR Code Generation` folder inside the project and also stored in the database.
3. **QR scan login** – on the login screen click the QR/scan button. Hold the generated QR image (on your phone or printed) in front of the webcam. On success the Dashboard opens with the scanned user's role.
4. **Secret code login** – click the QR-load button, enter the username, then the secret code.
5. **Explore the Dashboard** – products, scan history, user management, user logs and settings.
6. **Test permissions** – create a user with some permissions set to `0` and log in as them to see restricted access.

---

## 🗄️ Database Overview

| Table | Purpose |
|-------|---------|
| `users` | Login accounts, roles and permission flags |
| `qr_users` | QR users: username, secret code, role, stored QR image |
| *(other tables)* | Products, scan history, user logs, etc. — see `database/inventory_db.sql` |

**`users` columns:** `user_id`, `username`, `password`, `role`, `full_name`, `email`, `contact_no`, `status`, `can_generate_qr`, `can_scan_qr`, `can_manage_users`, `can_manage_products`, `can_view_scan_history`, `can_view_user_logs`, `can_access_settings`

---

## 🧯 Troubleshooting

| Problem | Solution |
|---------|----------|
| `Database error occurred` | Check MySQL is running and the credentials in `DBConnection.java` are correct. |
| `No database selected` in Workbench | Run `USE inventory_db;` first. |
| Webcam does not open | Close other apps using the camera; make sure the `lib` folder is next to the JAR. |
| QR not detected | Increase screen brightness, hold the code steady about 15–25 cm from the camera. |
| Images/icons not showing | Make sure the `InventoryImages` folder is inside `src` and is included in the build. |
| `NullPointerException` on `getResource` | The image path is wrong or the image is missing from the build. |
| `ClassNotFoundException` for MySQL | Make sure `dist/lib` is next to `InventoryQRSystem.jar` and contains the MySQL Connector/J JAR. |

---

## ⚠️ Notes

- This is an **academic/demo project**. Passwords are stored as plain text for simplicity; a production system should hash passwords (e.g. BCrypt).
- Demo accounts should be removed before any real-world use.

---

## 👩‍💻 Author

**Virdha**
Inventory QR System — Java Swing + MySQL

---

## 📄 License

This project is for educational purposes.