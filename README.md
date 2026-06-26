# E-Commerce Backend System

## Project Overview

The **E-Commerce Backend System** is a production-style RESTful backend application developed using **Java Spring Boot**. It simulates the core functionalities of an online shopping platform by providing APIs for user management, product management, shopping cart operations, coupon management, checkout, inventory validation, and order processing.

The application follows a layered architecture consisting of **Controller, Service, Repository, DTO, and Entity** layers. It demonstrates enterprise development practices including transaction management, exception handling, logging, validation, pagination, Swagger documentation, and unit testing.

---

# Features

## User Management

* Register new users
* Retrieve user information
* Update user details
* Delete users
* Input validation using Jakarta Validation

---

## Product Management

* Add new products
* Update product details
* Delete products
* View product by ID
* View all products
* Pagination and sorting support
* Inventory management

---

## Shopping Cart

* Add product to cart
* Update cart item quantity
* Remove product from cart
* View user cart
* Automatic total price calculation
* Quantity validation
* Stock validation before adding/updating

---

## Coupon Management

* Create coupons
* Retrieve coupon by ID
* Retrieve coupon by coupon code
* Update coupon
* Delete coupon
* View all coupons with pagination
* Coupon activation/deactivation
* Coupon expiry validation

---

## Checkout Module

The checkout workflow performs multiple business validations:

* Validate user
* Validate cart
* Validate cart is not empty
* Validate available inventory
* Validate coupon
* Validate coupon expiry
* Validate coupon active status
* Apply coupon discount
* Simulate payment
* Reduce inventory after successful payment
* Create order
* Create order items
* Clear cart after successful checkout
* Generate order summary

All checkout operations are executed inside a **single transaction** using `@Transactional`.

---

# Project Architecture

The project follows a layered architecture.

```text
Controller
      │
      ▼
Service
      │
      ▼
Repository
      │
      ▼
Database
```

### Layers

### Controller

Handles HTTP requests and responses.

### Service

Contains business logic.

### Repository

Performs database operations using Spring Data JPA.

### DTO

Transfers request and response data.

### Entity

Represents database tables.

### Exception

Handles application-specific exceptions.

### Constants

Stores reusable messages and constants.

### Audit

Provides logging and auditing support.

---

# Technologies Used

| Technology      | Version |
| --------------- | ------- |
| Java            | 17      |
| Spring Boot     | 3.x     |
| Spring MVC      | ✔       |
| Spring Data JPA | ✔       |
| Spring Security | ✔       |
| MySQL           | 8.x     |
| Maven           | ✔       |
| Swagger OpenAPI | ✔       |
| JUnit 5         | ✔       |
| Mockito         | ✔       |
| Lombok          | ✔       |
| Hibernate       | ✔       |

---

# Database Tables

The application uses the following database tables:

* Users
* Products
* Cart
* Cart Items
* Orders
* Order Items
* Coupons

---

# REST APIs

## User APIs

| Method | Endpoint        |
| ------ | --------------- |
| POST   | /api/users      |
| GET    | /api/users/{id} |
| GET    | /api/users      |
| PUT    | /api/users/{id} |
| DELETE | /api/users/{id} |

---

## Product APIs

| Method | Endpoint           |
| ------ | ------------------ |
| POST   | /api/products      |
| GET    | /api/products/{id} |
| GET    | /api/products      |
| PUT    | /api/products/{id} |
| DELETE | /api/products/{id} |

---

## Cart APIs

| Method | Endpoint                          |
| ------ | --------------------------------- |
| POST   | /api/cart                         |
| GET    | /api/cart/{userId}                |
| PUT    | /api/cart/{cartItemId}/{quantity} |
| DELETE | /api/cart/{cartItemId}            |

---

## Coupon APIs

| Method | Endpoint                       |
| ------ | ------------------------------ |
| POST   | /api/coupons                   |
| GET    | /api/coupons                   |
| GET    | /api/coupons/{couponId}        |
| GET    | /api/coupons/code/{couponCode} |
| PUT    | /api/coupons/{couponId}        |
| DELETE | /api/coupons/{couponId}        |

---

## Checkout API

| Method | Endpoint      |
| ------ | ------------- |
| POST   | /api/checkout |

---

# Business Rules

* Product stock cannot become negative.
* Coupon must exist.
* Coupon must be active.
* Coupon must not be expired.
* Payment must be successful before creating an order.
* Cart cannot be empty.
* User must exist.
* Product stock is reduced only after successful payment.
* Cart is cleared after successful checkout.

---

# Unit Testing

The project includes unit tests using **JUnit 5** and **Mockito**.

### Tested Scenarios

* User not found
* Cart not found
* Empty cart
* Coupon not found
* Coupon inactive
* Coupon expired
* Payment failed
* Insufficient stock
* Successful checkout

All checkout business scenarios are covered through unit tests.

---

# API Documentation

Swagger UI is integrated for interactive API documentation.

```
http://localhost:8080/swagger-ui/index.html
```

---

# Installation & Setup

## Clone Repository

```bash
git clone https://github.com/YOUR_USERNAME/ECommerce-Backend.git
```

## Open Project

Import the project into Eclipse or IntelliJ IDEA.

---

## Configure Database

Create a MySQL database:

```sql
CREATE DATABASE ecommerce_db;
```

Update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=root
spring.datasource.password=your_password
```

---

## Run Application

```bash
mvn spring-boot:run
```

or run the main Spring Boot application class from your IDE.

---

# Project Structure

```text
src
 └── main
      ├── java
      │      ├── Controller
      │      ├── Service
      │      ├── ServiceImpl
      │      ├── Repository
      │      ├── DTO
      │      ├── Model
      │      ├── Exception
      │      ├── Config
      │      ├── Constants
      │      └── Audit
      │
      └── resources
             └── application.properties
```

---

# Future Enhancements

* JWT Authentication
* Role-Based Authorization
* Payment Gateway Integration
* Email Notifications
* Wishlist Module
* Product Categories
* Product Images
* Order History APIs
* Docker Deployment
* CI/CD Pipeline
* AWS Deployment

---

# Author

**Shaik Mohammad Ali**

Backend Developer | Java | Spring Boot | REST APIs | MySQL

---

# ⭐ Acknowledgements

This project was developed to demonstrate enterprise backend development concepts using Spring Boot, following clean architecture and industry-standard coding practices.
