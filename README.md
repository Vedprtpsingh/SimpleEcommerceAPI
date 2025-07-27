# Simple E-commerce API

A simple e-commerce API built with Spring Boot that provides product listings, cart management, order creation, and user authentication.

## Features

- **Product Listings**: View available products
- **Cart Management**: Add, update, and remove items from your cart
- **Order Creation**: Create orders from your cart
- **User Authentication**: JWT-based authentication with customer and admin roles
- **Role-Based Access Control**: Different permissions for customers and admins

## API Endpoints

### Authentication

- `POST /api/auth/register`: Register a new user
  ```json
  {
    "email": "user@example.com",
    "password": "password123",
    "roles": ["CUSTOMER"]
  }
  ```

- `POST /api/auth/login`: Login and get JWT token
  ```json
  {
    "email": "user@example.com",
    "password": "password123"
  }
  ```

### Products

- `GET /api/products`: Get all products (paginated)
  - Query parameters: `page` (default: 0), `size` (default: 10)

- `GET /api/products/search`: Search products
  - Query parameters: `name` or `category`

- `POST /api/products`: Add a new product (Admin only)
  ```json
  {
    "name": "Product Name",
    "description": "Product Description",
    "category": "Category",
    "price": 19.99,
    "stock": 100
  }
  ```

- `PUT /api/products/{id}`: Update a product (Admin only)
- `DELETE /api/products/{id}`: Delete a product (Admin only)

### Cart

- `GET /api/cart`: Get current user's cart
- `POST /api/cart/add`: Add item to cart
  - Query parameters: `productId`, `quantity`
- `PUT /api/cart/update`: Update item quantity
  - Query parameters: `productId`, `quantity`
- `DELETE /api/cart/remove`: Remove item from cart
  - Query parameters: `productId`

### Orders

- `POST /api/orders/create`: Create an order from the current cart

## Web Interface

The application includes a simple web interface:

- `/`: Home page with product listings and cart
- `/login`: Login page
- `/register`: Registration page
- `/admin`: Admin dashboard (for admin users only)

## Default Users

- Admin: 
  - Email: `admin@example.com`
  - Password: `admin123`
- Customer:
  - Email: `customer@example.com`
  - Password: `customer123`

## Setup and Running

1. Make sure you have MySQL installed and running
2. Create a database named `simpleecommerceapidb`
3. Update `application.properties` with your database credentials if needed
4. Run the application using Maven:
   ```
   ./mvnw spring-boot:run
   ```
5. Access the web interface at `http://localhost:8080`

## Security

- JWT (JSON Web Tokens) are used for authentication
- Passwords are encrypted using BCrypt
- Role-based access control is implemented for API endpoints

## Code Structure

```
src/main/java/com/ved/SimpleEcommerceAPI/
├── config/
│   ├── DataInitializer.java       # Initializes default data
│   └── ThymeleafConfig.java       # Thymeleaf security configuration
├── controller/
│   ├── AdminController.java       # Admin dashboard controller
│   ├── AuthenticationController.java # Authentication endpoints
│   ├── CartController.java        # Cart management endpoints
│   ├── HomeController.java        # Main page controller
│   ├── OrderController.java       # Order creation endpoints
│   ├── ProductController.java     # Product management endpoints
│   └── WebController.java         # Web page controllers
├── model/
│   ├── Cart.java                  # Cart entity
│   ├── CartItem.java              # Cart item entity
│   ├── Order.java                 # Order entity
│   ├── Product.java               # Product entity
│   ├── Role.java                  # User role entity
│   └── User.java                  # User entity
├── repository/                    # JPA repositories
├── security/
│   ├── CustomUserDetailsService.java # User details service
│   ├── JwtAuthenticationFilter.java  # JWT filter
│   ├── JwtUtil.java               # JWT utilities
│   └── SecurityConfig.java        # Security configuration
├── service/
│   ├── CartService.java           # Cart business logic
│   ├── OrderService.java          # Order business logic
│   ├── ProductService.java        # Product business logic
│   └── UserService.java           # User business logic
└── SimpleEcommerceApiApplication.java # Main application class
```

## Technologies Used

- Spring Boot 3.5.3
- Spring Security with JWT
- Spring Data JPA
- Thymeleaf for server-side rendering
- MySQL database
- Bootstrap 5 for frontend styling
- JavaScript for client-side interactions