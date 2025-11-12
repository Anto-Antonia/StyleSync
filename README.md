# StyleSync

StyleSync is a full-stack fashion commerce backend build using Spring Boot. It supports secure user authentication,
cart management, order placement, favorite list and admin-level product control.
Designed for scalability, clarity and seamless integration with frontend clients.

# Features

 - **JWT Authentication** - Secure login and registration role-based access (``ADMIN``, ``USER``);
 - **Cart System** - Add/remove items, auto-clear on order placement;
 - **Order Placement** - Pulls items from cart, stores shipping info, tracks status, clears cart, updates the stock of the items;
 - **Order history** - Admin can view any user's orders;
 - **Favorite product** - Users can add any items to their favorite list;
 - **Role management** - Assign and manage roles;
 - **Global exception handling** - Clear error responses for validation, missing data and/or empty carts.

# Tech Stack

| Backend    | Java, Spring Boot       |
|------------|-------------------------|
| Security   | Spring Security, JWT    |
| Database   | MySQL                   |
| ORM        | Hibernate/JPA           |
| Validation | Jakarta Bean Validation |
| Build Tool | Maven                   |

# Setup

1. Clone repo
2. Configure DB in ``application.properties``
3. Run ``mvn spring-boot:run``
4. Test endpoints with Postman

### Default Admin
Email: admin@stylesync.dev  
Password: Admin@123

This account is created automatically on first run.  
Please change the password after login.

## Database configuration
Update `application.properties`:
 - spring.datasource.url=jdbc:mysql://localhost:3306/stylesync
 - spring.datasource.username=root
 - spring.datasource.password=root
 - spring.jpa.hibernate.ddl-auto=create (Use ``create`` for first run to generate tables, then switch to ``update`` for development changes. **For production**, set to ``none``.)

# Installation

``git clone https://github.com/Anto-Antonia/StyleSync.git`` <br>
``cd stylesync`` <br>
``mvn clean install``

# Authentication
``POST  /auth/register`` - Register new User <br>
``POST  /auth/signIn`` - Signing in and receiving JWT

# Cart Endpoints
``GET /api/cart`` - View current cart <br>
``POST /api/cart/{productId}`` - Add product to cart (this endpoint uses **@RequestParam** for quantity, therefor the end result will look like this ``/api/cart/2?quantity=1``) <br>
``DELETE /api/cart/removeitem/{productId}`` - Remove product from cart

# Order Endpoints
``POST /api/orders/place_Order`` - Place order (pulls items from cart) <br>
``GET /api/orders/order/{orderId}`` - Admin-only: view user's order

# Admin Endpoints
``POST /api/product`` - Add a product <br>
``PATCH /api/product/products/{productId}`` - Update product <br>
``DELETE /api/product/{productId}`` - Delete product <br>
``GET /api/users/roles`` - View roles <br>
``POST /api/users/role/addRole`` - Add role <br>
``PATCH /api/orders/{orderId}/status`` - Update status order

# Additional Notes
 - All endpoints have been tested via Postman
 - Cart auto-clears after order placement
 - The status of the order is being manually changed
 - Empty cart returns ``200 OK`` with message instead of error
 - Enum values stored as strings (``CREDIT_CARD``, ``PENDING``, etc.)
 - Update DB username/password in application.properties to match your local setup