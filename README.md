# Play-CRUDJava  

![Programming Language](https://img.shields.io/badge/Java-red?style=flat&logo=openjdk&logoColor=white)  
![Framework](https://img.shields.io/badge/Play!%20Framework-green?style=flat&logo=play&logoColor=white) ![Framework](https://img.shields.io/badge/Bootstrap-purple?style=flat&logo=bootstrap&logoColor=white)  
![Database](https://img.shields.io/badge/PostgreSQL-blue?logo=postgresql&logoColor=white)  
![Platform: Web](https://img.shields.io/badge/Platform-Web-blue?logo=google-chrome)
![Last Commit](https://img.shields.io/github/last-commit/ander1code/play-crudjava?color=yellow&logo=github) ![Size](https://img.shields.io/github/repo-size/ander1code/play-crudjava?color=blue&logo=files) ![License](https://img.shields.io/github/license/ander1code/play-crudjava?color=black&logo=open-source-initiative)

---

## 1. Description
**Play-CRUDJava** is a prototype CRUD application built with the **Play Framework (Java)** and **PostgreSQL**. It demonstrates how to create a basic system for managing authors and their associated books, while implementing custom validation and secure login functionality.

## 2. Features
- **Author and Book Management:** Each author can have one or more registered books.
- **Login System:** Includes secure login functionality with usernames and passwords encrypted using **SHA-512**.
- **Custom Validation:** Validation logic built independently; the standard Play Framework validation was not used.
- **Custom Login Implementation:** The login system is fully customized, diverging from the default Play Framework login system.

## 3. Tools Used
- **IDE:** IntelliJ IDEA Community Edition 2017.3.2 x64
- **Java Version:** JAVA 8 (jdk1.8.0_131)
- **Database:** PostgreSQL 9.6
- **Front-End Framework:** Bootstrap 3.3.7
- **Database Management Tool:** SQL Manager Lite for MySQL 5.6.4 (build 50082)

## 4. Additional Information

### 4.1. Features Not Implemented
- **Pagination:** The lists of authors and books do not include pagination.

### 4.2. Database Setup Steps
1. **Create a Database:** Use **PostgreSQL** to create a new database for the project.
2. **Configure Connection:** Update the `db` session in the `application.conf` file with your database connection details.
3. **Generate Table Script:** Access the application at `127.0.0.1:9000` to trigger automatic generation of the table creation script.
4. **Add Default User:** Execute the provided SQL file (`user.sql`) to create a default user in the database.
