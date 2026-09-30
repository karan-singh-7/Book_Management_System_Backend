# 📚 Book Management System

A backend-based **Book Management System** developed using **Java and Spring Boot**. The application provides REST APIs for managing books, users, authentication, and book borrowing operations.

## 🚀 Features

* User registration and login
* JWT-based authentication
* Role-based access control
* Add new books
* View all available books
* View book details
* Update book information
* Delete books
* Track book stock/availability
* Borrow books
* Return borrowed books
* Global exception handling
* Input validation
* RESTful APIs
* Pagination for book listing

## 🛠️ Technologies Used

* **Java**
* **Spring Boot**
* **Spring MVC**
* **Spring Data JPA**
* **Spring Security**
* **JWT**
* **Hibernate**
* **MySQL**
* **Maven**
* **Lombok**
* **Bean Validation**

## 🏗️ Project Structure

```text
src/main/java
└── com.library
    ├── controller
    ├── service
    ├── repository
    ├── entity
    ├── dto
    ├── security
    ├── exception
    └── config
```

## 🔐 Authentication

The application uses **Spring Security with JWT authentication**.

The general authentication flow is:

```text
Client
   ↓
Login API
   ↓
Username / Password Validation
   ↓
JWT Token Generated
   ↓
Client Sends JWT
   ↓
JWT Authentication Filter
   ↓
Spring Security
   ↓
Protected API
```

Protected APIs require a valid JWT token in the request header:

```text
Authorization: Bearer <JWT_TOKEN>
```

## 📖 Book Management

The system allows authorized users to manage books.

Example book information:

```json
{
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "totalStock": 5
}
```

The system maintains book availability based on the current stock.

## 👤 User Management

Users can:

* Register an account
* Login
* View available books
* Borrow books
* Return books

Administrators can perform management operations such as adding, updating, and deleting books.

## 📚 Borrowing System

When a user borrows a book:

```text
User
 ↓
Borrow Book
 ↓
Check Book Exists
 ↓
Check Stock Availability
 ↓
Reduce Available Stock
 ↓
Create Borrow Record
```

When a book is returned:

```text
Return Book
 ↓
Update Borrow Record
 ↓
Increase Available Stock
```

## 🌐 API Endpoints

### Authentication

| Method | Endpoint             | Description         |
| ------ | -------------------- | ------------------- |
| POST   | `/api/auth/register` | Register a new user |
| POST   | `/api/auth/login`    | Login user          |

### Books

| Method | Endpoint          | Description    |
| ------ | ----------------- | -------------- |
| POST   | `/api/books`      | Add a new book |
| GET    | `/api/books`      | Get all books  |
| GET    | `/api/books/{id}` | Get book by ID |
| PUT    | `/api/books/{id}` | Update book    |
| DELETE | `/api/books/{id}` | Delete book    |

### Borrowing

| Method | Endpoint                      | Description            |
| ------ | ----------------------------- | ---------------------- |
| POST   | `/api/borrowings/{bookId}`    | Borrow a book          |
| PUT    | `/api/borrowings/{id}/return` | Return a borrowed book |

> Note: Update the endpoint names above if your actual controller mappings are different.

## ⚙️ Configuration

Create your local `application.properties` file inside:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/librarydb
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update

jwt.secret=YOUR_SECRET_KEY
```

⚠️ **Do not commit passwords, database credentials, JWT secrets, API keys, or other sensitive information to GitHub.**

The actual `application.properties` file is excluded using `.gitignore`.

## ▶️ How to Run

### 1. Clone the repository

```bash
git clone <your-repository-url>
```

### 2. Open the project

Open the project in:

* Eclipse
* IntelliJ IDEA
* VS Code

### 3. Configure the database

Create a MySQL database:

```sql
CREATE DATABASE librarydb;
```

Update your local `application.properties`.

### 4. Build the project

Using Maven:

```bash
mvn clean install
```

### 5. Run the application

```bash
mvn spring-boot:run
```

Or run the main Spring Boot application class from your IDE.

## 🧪 API Testing

The APIs can be tested using:

* Postman
* Thunder Client
* Swagger/OpenAPI (if configured)

## 🔮 Future Enhancements

* Frontend using React
* Admin dashboard
* Search books by title/author
* Advanced filtering and sorting
* Book borrowing history
* Email notifications
* Due-date and fine calculation
* Docker deployment
* Cloud deployment

## 👨‍💻 Author

**Karan Singh**

Java Developer | Spring Boot | REST APIs | Microservices
