# 🏎️ ThrottleX – Spring Boot Backend

**ThrottleX** is a Spring Boot backend project that provides **secure user authentication, user management, and RESTful APIs** for web or mobile applications. It uses **JWT-based authentication**, integrates with **MySQL**, and follows best practices for scalable backend development.

---

## 🚀 Features

- 🔐 **JWT-based Authentication** for secure login and sessions  
- 👥 **User Management** (Create, Read, Update, Delete)  
- 🗄️ **MySQL Database Integration** for persistent storage  
- ⚡ **Clean Spring Boot Architecture** with layered structure  
- ☁️ Ready for **cloud deployment** (AWS, Heroku, DigitalOcean)  

---

## 🛠️ Tech Stack

- **Backend:** Java 17, Spring Boot  
- **Database:** MySQL  
- **Authentication:** JWT (JSON Web Tokens)  
- **Build Tool:** Maven  
- **Dependencies:** Spring Security, Spring Data JPA, Lombok  

---

## 📝 Prerequisites

Before running the project, ensure you have:  

- Java JDK 17  
- Maven 3.x  
- MySQL Server  
- IDE (IntelliJ IDEA / Eclipse recommended)  
- Git  

---

## ⚡ Project Setup

### 1️⃣ Clone the repository

```bash
git clone https://github.com/<your-username>/ThrottleX.git
cd ThrottleX

2️⃣ Configure MySQL

Create a MySQL database:

CREATE DATABASE throttlex_db;


Update src/main/resources/application.properties with your MySQL credentials:

# MySQL Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/throttlex_db
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
spring.jpa.hibernate.ddl-auto=update

# Hibernate SQL Logging (optional)
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Server port
server.port=8080

# JWT Configuration
jwt.secret=YOUR_SECRET_KEY
jwt.expiration=3600000
