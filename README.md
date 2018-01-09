# PlayCrud

## 1 - Description:
- Prototype of a CRUD developed with Play Framework (Java) and MySQL.

## 2 - Features:
- One author has one or more registered books.
- Login. Username and password encrypted for sha-512. 

## 3 - Tools used:
- IntelliJ IDEA Community Edition 2017.3.2 x64
- JAVA 8 (jdk1.8.0_131)
- MySQL 5
- Bootstrap 3.3.7
- SQL Manager Lite for MySQL 5.6.4 (build 50082)

## 4 - P.S:
- Without paginations in Lists. 
- Custom validation: 
	- Standard Play Framework validation was not used.
- Custom Login: 
	- Standard Play Framework login was not used.
- Database (Steps): 
	1- Create database in MySql.
	2- Configure connection with this database in session "db" on "application.conf" file.
	3- Access "127.0.0.1:9000" for automatic generation of table creation script. 
    	4- Run the attached *.sql file (user.sql) to create a user default.
