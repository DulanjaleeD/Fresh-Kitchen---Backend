# Fresh Kitchen - Backend API

A Spring Boot-based REST API for a modern food ordering and management system. The backend provides comprehensive APIs for user authentication, menu management, cart operations, order processing, and payment handling.

## Tech Stack

- **Java 17+** - Programming language
- **Spring Boot 3.x** - Web framework
- **Spring Security** - Authentication & authorization
- **Spring Data JPA** - ORM and database access
- **Maven** - Build and dependency management
- **MySQL/PostgreSQL** - Database (configurable)
- **JWT** - Token-based authentication
- **Lombok** - Code generation and simplification



## Prerequisites

Before running the backend, ensure you have the following installed:

- **Java Development Kit (JDK) 17+**
  - Verify: `java -version`
- **Maven 3.8+**
  - Verify: `mvn -version`
- **MySQL or PostgreSQL**
  - Verify: Database service is running
- **Git** - For version control

##  Installation & Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd backend
```

### 2. Configure Database Connection

Edit `src/main/resources/application.properties`:

```properties
# MySQL Example
spring.datasource.url=jdbc:mysql://localhost:3306/fresh_kitchen
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Server Configuration
server.port=8080

# JWT Configuration
jwt.secret=your_secret_key_here
jwt.expiration=86400000
```

### 3. Create Database

```bash
CREATE DATABASE fresh_kitchen;
```

##  Building the Project

### Using Maven Wrapper (Recommended)

```bash
# On Windows
mvnw.cmd clean package

# On macOS/Linux
./mvnw clean package
```

### Using Maven (If installed globally)

```bash
mvn clean package
```

### Using Maven

```bash
mvn spring-boot:run
```

The API will be available at: `http://localhost:8080`

##  Project Structure

```
src/main/java/com/dulanjali/kitchen/
├── FreshKitchenApplication.java      # Main application entry point
├── controller/                         # REST API endpoints
│   ├── CartController.java
│   ├── CategoryController.java
│   ├── FoodController.java
│   ├── OrderController.java
│   ├── PaymentController.java
│   ├── UserController.java
│   └── secured/                       # Protected endpoints
├── service/                            # Business logic
│   ├── impl/                          # Service implementations
│   └── secured/                       # Protected services
├── dao/                                # Data access objects
├── entities/                           # JPA entities/models
├── dto/                                # Data Transfer Objects
│   └── secured/
├── securityConfig/                    # Spring Security configuration
├── corsConfig/                        # CORS configuration
├── exception/                         # Custom exceptions & handlers
├── enums/                             # Enum definitions
└── util/                              # Utility classes

src/main/resources/
└── application.properties              # Configuration file
```

##  API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `POST /api/auth/logout` - User logout

### Users
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user profile
- `GET /api/users/profile` - Get current user profile (secured)

### Menu & Categories
- `GET /api/categories` - List all food categories
- `GET /api/foods` - List all foods
- `GET /api/foods/category/{categoryId}` - Foods by category
- `GET /api/foods/{id}` - Get food details

### Cart Management
- `GET /api/cart` - Get user's cart (secured)
- `POST /api/cart/items` - Add item to cart (secured)
- `PUT /api/cart/items/{itemId}` - Update cart item (secured)
- `DELETE /api/cart/items/{itemId}` - Remove item from cart (secured)
- `DELETE /api/cart/clear` - Clear cart (secured)

### Orders
- `POST /api/orders` - Create order (secured)
- `GET /api/orders` - Get user's orders (secured)
- `GET /api/orders/{id}` - Get order details (secured)
- `PUT /api/orders/{id}` - Update order status (secured)

### Payments
- `POST /api/payments` - Process payment (secured)
- `GET /api/payments/{id}` - Get payment details (secured)
- `GET /api/payments/order/{orderId}` - Get payment by order (secured)

##  Configuration

### Environment Variables

You can override `application.properties` with environment variables:

```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://db-host:3306/fresh_kitchen
export SPRING_DATASOURCE_USERNAME=db_user
export SPRING_DATASOURCE_PASSWORD=db_pass
export SERVER_PORT=9000
```

### CORS Configuration

CORS is configured in `corsConfig/CORSConfig.java`. Update allowed origins as needed:

```java
configuration.setAllowedOrigins(Arrays.asList(
    "http://localhost:3000",
    "http://localhost:5173",
    "https://yourdomain.com"
));
```

### Security Configuration

JWT-based authentication is configured in `securityConfig/`. Update JWT secrets in `application.properties`:

```properties
jwt.secret=your_secure_secret_key_min_32_chars
jwt.expiration=86400000  # 24 hours in milliseconds
```


Test files are located in: `src/test/java/com/dulanjali/kitchen/`

##  Security Features

- **JWT Authentication** - Stateless token-based authentication
- **Spring Security** - Method-level and request-level authorization
- **Password Encryption** - BCrypt for secure password storage
- **CORS Protection** - Configured for allowed origins
- **Input Validation** - DTOs with validation annotations
