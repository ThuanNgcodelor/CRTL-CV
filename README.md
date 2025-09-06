# MICROSERVICE SHOPEE - Hệ thống E-commerce Backend

## Tổng quan dự án

Microservice Shopee là một hệ thống e-commerce backend được xây dựng theo kiến trúc microservice, sử dụng Spring Boot và các công nghệ hiện đại để cung cấp các dịch vụ phân tán, có khả năng mở rộng cao.

## Kiến trúc hệ thống

### Microservices Architecture
- **API Gateway** - Điểm vào duy nhất cho tất cả requests
- **Service Discovery** - Eureka Server cho service registration
- **Config Server** - Centralized configuration management
- **Authentication Service** - JWT-based authentication
- **User Service** - Quản lý người dùng và profile
- **Stock Service** - Quản lý sản phẩm và kho
- **Order Service** - Xử lý đơn hàng
- **File Storage Service** - Quản lý file upload
- **Job Service** - Quản lý công việc/tuyển dụng
- **Notification Service** - Gửi thông báo

## Công nghệ sử dụng

### Backend Framework
- **Spring Boot 3.x** - Main framework
- **Spring Cloud Gateway** - API Gateway
- **Spring Cloud Netflix Eureka** - Service Discovery
- **Spring Security** - Authentication & Authorization
- **Spring Data JPA** - Database ORM
- **Spring WebFlux** - Reactive programming

### Database & Caching
- **MySQL 8.0** - Primary database
- **Redis** - Caching và session storage
- **JPA/Hibernate** - ORM mapping

### Message Queue & Event Streaming
- **Apache Kafka** - Message broker
- **Zookeeper** - Kafka coordination
- **Kafka UI** - Management interface

### Infrastructure
- **Docker & Docker Compose** - Containerization
- **Maven** - Build tool
- **JWT** - Token-based authentication

## Cấu trúc dự án

```
microservice-shopee/
├── gateway/                 # API Gateway (Port: 8080)
├── eureka-server/          # Service Discovery (Port: 8761)
├── config-server/          # Configuration Server (Port: 8888)
├── auth-service/           # Authentication Service (Port: 8001)
├── user-service/           # User Management Service (Port: 8002)
├── stock-service/          # Product & Inventory Service (Port: 8004)
├── order-service/          # Order Processing Service (Port: 8003)
├── file-storage/           # File Management Service (Port: 8005)
├── job-service/            # Job Management Service (Port: 8006)
├── notification-service/   # Notification Service (Port: 8007)
├── config/                 # Shared configuration
├── docker-compose.yml      # Docker orchestration
└── README.md
```

## Cài đặt và chạy

### Yêu cầu hệ thống
- **Java 17+** - JDK để chạy Spring Boot
- **Maven 3.6+** - Build tool
- **Docker & Docker Compose** - Containerization
- **MySQL 8.0** - Database
- **Redis** - Caching
- **Git** - Version control

### Cách 1: Chạy với Docker Compose (Khuyến nghị)

#### Bước 1: Cài đặt Docker
```bash
# Windows
# Tải Docker Desktop từ https://www.docker.com/products/docker-desktop

# Linux Ubuntu
sudo apt update
sudo apt install docker.io docker-compose
sudo systemctl start docker
sudo systemctl enable docker

# macOS
# Tải Docker Desktop từ https://www.docker.com/products/docker-desktop
```

#### Bước 2: Clone và chạy dự án
```bash
# Clone repository
git clone <repository-url>
cd microservice-shopee

# Chạy tất cả services
docker-compose up -d

# Kiểm tra logs
docker-compose logs -f

# Dừng services
docker-compose down
```

#### Bước 3: Kiểm tra services
```bash
# Kiểm tra containers đang chạy
docker-compose ps

# Kiểm tra logs của service cụ thể
docker-compose logs -f auth-service
docker-compose logs -f user-service
```

### Cách 2: Chạy từng service riêng lẻ

#### Bước 1: Chạy Infrastructure Services
```bash
# Chạy MySQL, Redis, Kafka, Zookeeper
docker-compose up -d mysql redis kafka zookeeper kafka-ui

# Kiểm tra services
docker ps
```

#### Bước 2: Chạy Eureka Server
```bash
cd eureka-server
mvn clean install
mvn spring-boot:run

# Kiểm tra: http://localhost:8761
```

#### Bước 3: Chạy Config Server
```bash
cd config-server
mvn clean install
mvn spring-boot:run

# Kiểm tra: http://localhost:8888
```

#### Bước 4: Chạy các Microservices
```bash
# Terminal 1 - Auth Service
cd auth-service
mvn clean install
mvn spring-boot:run

# Terminal 2 - User Service
cd user-service
mvn clean install
mvn spring-boot:run

# Terminal 3 - Stock Service
cd stock-service
mvn clean install
mvn spring-boot:run

# Terminal 4 - Order Service
cd order-service
mvn clean install
mvn spring-boot:run

# Terminal 5 - File Storage Service
cd file-storage
mvn clean install
mvn spring-boot:run

# Terminal 6 - Job Service
cd job-service
mvn clean install
mvn spring-boot:run

# Terminal 7 - Notification Service
cd notification-service
mvn clean install
mvn spring-boot:run

# Terminal 8 - Gateway
cd gateway
mvn clean install
mvn spring-boot:run
```

### Cách 3: Chạy với IDE (IntelliJ IDEA/Eclipse)

#### Bước 1: Import dự án
1. Mở IDE
2. Import từ Maven project
3. Chọn thư mục `microservice-shopee`

#### Bước 2: Cấu hình Database
1. Tạo database `microservice` trong MySQL
2. Cập nhật connection string trong `application.properties`

#### Bước 3: Chạy services theo thứ tự
1. **Eureka Server** (eureka-server)
2. **Config Server** (config-server)
3. **Auth Service** (auth-service)
4. **User Service** (user-service)
5. **Stock Service** (stock-service)
6. **Order Service** (order-service)
7. **File Storage Service** (file-storage)
8. **Job Service** (job-service)
9. **Notification Service** (notification-service)
10. **Gateway** (gateway)

## Các Service và API Endpoints

### 1. API Gateway (Port: 8080)
**Chức năng**: Điểm vào duy nhất, routing requests đến các service

**Routes**:
```
/v1/auth/** → auth-service (Port: 8001)
/v1/user/** → user-service (Port: 8002)
/v1/stock/** → stock-service (Port: 8004)
/v1/order/** → order-service (Port: 8003)
/v1/file-storage/** → file-storage (Port: 8005)
/v1/job-service/** → job-service (Port: 8006)
/v1/notification/** → notification-service (Port: 8007)
```

**Features**:
- JWT Authentication filter
- Load balancing
- Request/Response logging
- CORS handling
- Rate limiting

### 2. Authentication Service (Port: 8001)
**Chức năng**: Xử lý đăng nhập, đăng ký, quản lý JWT tokens, OTP

**Endpoints**:
```http
POST /v1/auth/login
POST /v1/auth/register  
POST /v1/auth/login/google
POST /v1/auth/forgotPassword
POST /v1/auth/verifyOtp
POST /v1/auth/updatePassword
```

**Cấu hình**:
- Gmail SMTP cho gửi OTP
- Google OAuth integration
- Redis cho lưu trữ OTP
- JWT token generation

**JSON Request/Response Examples**:

**Login Request**:
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Login Response**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "user123",
    "email": "user@example.com",
    "username": "username",
    "roles": ["ROLE_USER"]
  }
}
```

**Register Request**:
```json
{
  "email": "newuser@example.com",
  "password": "password123",
  "username": "newuser",
  "fullName": "Nguyễn Văn A",
  "phone": "0123456789"
}
```

**Forgot Password Request**:
```json
{
  "email": "user@example.com"
}
```

**Verify OTP Request**:
```json
{
  "email": "user@example.com",
  "otp": "123456"
}
```

### 3. User Service (Port: 8002)
**Chức năng**: Quản lý thông tin người dùng, địa chỉ, giỏ hàng

**Endpoints**:
```http
# User Management
GET /v1/user/information
GET /v1/user/cart
POST /v1/user/save
GET /v1/user/getAll
GET /v1/user/getUserById/{id}
GET /v1/user/getUserByEmail/{email}
GET /v1/user/getUserByUsername/{username}
PUT /v1/user/update
DELETE /v1/user/delete/{id}

# Address Management
POST /v1/user/address/save
GET /v1/user/address/getAllAddresses
GET /v1/user/address/getAddressById/{id}
PUT /v1/user/address/update
PUT /v1/user/address/setDefaultAddress/{id}
DELETE /v1/user/address/deleteAddressById/{id}
```

**JSON Examples**:

**User Information Response**:
```json
{
  "id": "user123",
  "email": "user@example.com",
  "username": "username",
  "fullName": "Nguyễn Văn A",
  "phone": "0123456789",
  "addresses": [
    {
      "id": "addr1",
      "fullName": "Nguyễn Văn A",
      "phone": "0123456789",
      "address": "123 Đường ABC, Quận 1, TP.HCM",
      "city": "TP.HCM",
      "district": "Quận 1",
      "ward": "Phường Bến Nghé",
      "isDefault": true
    }
  ]
}
```

**Address Create Request**:
```json
{
  "fullName": "Nguyễn Văn A",
  "phone": "0123456789",
  "address": "123 Đường ABC, Quận 1, TP.HCM",
  "city": "TP.HCM",
  "district": "Quận 1",
  "ward": "Phường Bến Nghé",
  "isDefault": false
}
```

**Cart Response**:
```json
{
  "id": "cart123",
  "userId": "user123",
  "items": [
    {
      "id": "item1",
      "productId": "prod1",
      "productName": "iPhone 15 Pro",
      "price": 29990000,
      "quantity": 2,
      "subtotal": 59980000,
      "productImage": "img1"
    }
  ],
  "totalAmount": 59980000,
  "totalItems": 2
}
```

### 4. Stock Service (Port: 8004)
**Chức năng**: Quản lý sản phẩm, danh mục, kho hàng, giỏ hàng

**Endpoints**:
```http
# Product Management
GET /v1/stock/product/list?pageNo=1
GET /v1/stock/product/getProductById/{id}
GET /v1/stock/product/search?keyword=iphone&pageNo=1
POST /v1/stock/product/create
PUT /v1/stock/product/update
DELETE /v1/stock/product/deleteProductById/{id}
POST /v1/stock/product/decreaseStock

# Category Management  
GET /v1/stock/category/list
POST /v1/stock/category/create
PUT /v1/stock/category/update/{id}
DELETE /v1/stock/category/delete/{id}

# Cart Management
POST /v1/stock/cart/item/add
PUT /v1/stock/cart/item/update/{id}
DELETE /v1/stock/cart/item/delete/{id}
```

**Features**:
- Product search và pagination
- Stock management
- Category management
- Cart operations
- Kafka integration cho notifications

**JSON Examples**:

**Product List Response**:
```json
{
  "content": [
    {
      "id": "prod1",
      "name": "iPhone 15 Pro",
      "description": "Latest iPhone model with A17 Pro chip",
      "price": 29990000,
      "discountPrice": 27990000,
      "category": {
        "id": "cat1",
        "name": "Điện thoại",
        "description": "Smartphones and mobile devices"
      },
      "images": [
        {
          "id": "img1",
          "url": "/file-storage/get/img1",
          "isMain": true
        }
      ],
      "stock": 50,
      "sku": "IPH15PRO-256GB",
      "brand": "Apple",
      "specifications": {
        "screen": "6.1 inch Super Retina XDR",
        "camera": "48MP Main Camera",
        "storage": "256GB",
        "color": "Titanium"
      },
      "createdAt": "2024-01-15T10:30:00Z",
      "updatedAt": "2024-01-15T10:30:00Z"
    }
  ],
  "totalElements": 100,
  "totalPages": 10,
  "size": 10,
  "number": 0,
  "first": true,
  "last": false
}
```

**Add to Cart Request**:
```json
{
  "productId": "prod1",
  "quantity": 2,
  "userId": "user123"
}
```

**Decrease Stock Request**:
```json
{
  "productId": "prod1",
  "quantity": 5
}
```

### 5. Order Service (Port: 8003)
**Chức năng**: Xử lý đơn hàng, thanh toán, vận chuyển

**Endpoints**:
```http
POST /v1/order/create
GET /v1/order/getOrderByUserId
GET /v1/order/getOrderById/{id}
```

**Features**:
- Kafka integration cho async order processing
- Order status tracking
- Integration với Stock Service
- User order history

**JSON Examples**:

**Create Order Response**:
```json
{
  "message": "Order has been sent to Kafka."
}
```

**Order Response**:
```json
{
  "id": "order123",
  "orderNumber": "ORD-2024-001",
  "userId": "user123",
  "status": "PENDING",
  "items": [
    {
      "id": "item1",
      "productId": "prod1",
      "productName": "iPhone 15 Pro",
      "quantity": 2,
      "price": 29990000,
      "subtotal": 59980000
    }
  ],
  "shippingAddress": {
    "fullName": "Nguyễn Văn A",
    "phone": "0123456789",
    "address": "123 Đường ABC, Quận 1, TP.HCM"
  },
  "paymentMethod": "COD",
  "totalAmount": 59980000,
  "shippingFee": 30000,
  "finalAmount": 60010000,
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T10:30:00Z"
}
```

### 6. File Storage Service (Port: 8005)
**Chức năng**: Quản lý upload, download, lưu trữ file

**Endpoints**:
```http
POST /v1/file-storage/upload
GET /v1/file-storage/get/{fileId}
GET /v1/file-storage/download/{fileId}
DELETE /v1/file-storage/delete/{fileId}
```

**Features**:
- File upload với MultipartFile
- Image processing
- File metadata storage
- Multiple file type support

**JSON Examples**:

**Upload Request**:
```http
POST /v1/file-storage/upload
Content-Type: multipart/form-data

image: [file]
```

**Upload Response**:
```json
{
  "id": "file123",
  "originalName": "product-image.jpg",
  "fileName": "file123_product-image.jpg",
  "fileSize": 1024000,
  "contentType": "image/jpeg",
  "url": "/file-storage/get/file123",
  "uploadedAt": "2024-01-15T10:30:00Z"
}
```

### 7. Job Service (Port: 8006)
**Chức năng**: Quản lý công việc, tuyển dụng

**Endpoints**:
```http
POST /v1/job/job/create
GET /v1/job/job/getAll
GET /v1/job/job/getById/{id}
PUT /v1/job/job/update
DELETE /v1/job/job/delete/{id}
POST /v1/job/job/getJobsThatFitYourNeeds/{needs}

# Job Categories
GET /v1/job/category/list
POST /v1/job/category/create
PUT /v1/job/category/update/{id}
DELETE /v1/job/category/delete/{id}

# Job Offers
POST /v1/job/offer/create
GET /v1/job/offer/getAll
PUT /v1/job/offer/update/{id}
DELETE /v1/job/offer/delete/{id}
```

**Features**:
- Job posting và management
- Category-based job organization
- Job search functionality
- File upload cho job descriptions
- Admin-only job creation

### 8. Notification Service (Port: 8007)
**Chức năng**: Gửi thông báo email, SMS, push notifications

**Endpoints**:
```http
POST /v1/notification/send-email
POST /v1/notification/send-sms
POST /v1/notification/send-push
GET /v1/notification/history
```

**Features**:
- Email notifications
- SMS integration
- Push notification support
- Notification history tracking
- Template-based messaging

## Database Schema

### MySQL Database: `microservice`

**Tables**:
- `users` - Thông tin người dùng
- `addresses` - Địa chỉ giao hàng
- `categories` - Danh mục sản phẩm
- `products` - Sản phẩm
- `product_images` - Hình ảnh sản phẩm
- `carts` - Giỏ hàng
- `cart_items` - Chi tiết giỏ hàng
- `orders` - Đơn hàng
- `order_items` - Chi tiết đơn hàng
- `files` - Thông tin file
- `jobs` - Công việc
- `job_categories` - Danh mục công việc
- `job_offers` - Đơn ứng tuyển
- `notifications` - Thông báo

## Cấu hình chi tiết

### Application Properties

#### Auth Service (Port: 8001)
```properties
server.port=8001
spring.application.name=auth-service
spring.config.import=configserver:http://localhost:8888/

# Gmail SMTP
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${MAIL_USERNAME:your-email@gmail.com}
spring.mail.password=${MAIL_PASSWORD:your-app-password}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Redis
spring.data.redis.host=localhost
spring.data.redis.port=6379

# Google OAuth
google.client-id=your-client-id
google.client-secret=your-client-secret
google.redirect-uri=http://localhost:5173/oauth2/callback
```

#### User Service (Port: 8002)
```properties
server.port=8002
spring.application.name=user-service
spring.config.import=configserver:http://localhost:8888/
```

#### Stock Service (Port: 8004)
```properties
spring.application.name=stock-service
server.port=8004

# Kafka Configuration
spring.kafka.producer.bootstrap-servers=localhost:9092
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
spring.kafka.topic.name=notificationTopic

spring.config.import=configserver:http://localhost:8888/
```

### Docker Compose Configuration
```yaml
version: "3.5"

services:
  mysql:
    container_name: mysql
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: microservice
      MYSQL_USER: sa
      MYSQL_PASSWORD: sa
    volumes:
      - mysql_data:/var/lib/mysql
    ports:
      - "3306:3306"
    command: --default-authentication-plugin=mysql_native_password
    networks:
      - microservice-network

  redis:
    container_name: redis
    image: redis:latest
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data
    networks:
      - microservice-network

  zookeeper:
    container_name: zookeeper
    image: "docker.io/bitnami/zookeeper:3"
    ports:
      - "2181:2181"
    volumes:
      - "zookeeper_data:/bitnami"
    environment:
      - ALLOW_ANONYMOUS_LOGIN=yes
    networks:
      - microservice-network

  kafka:
    container_name: kafka
    image: "docker.io/bitnami/kafka:2-debian-10"
    ports:
      - "9092:9092"
    expose:
      - "9093"
    volumes:
      - "kafka_data:/bitnami"
    environment:
      - KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper:2181
      - ALLOW_PLAINTEXT_LISTENER=yes
      - KAFKA_ADVERTISED_LISTENERS=INSIDE://kafka:9093,OUTSIDE://localhost:9092
      - KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=INSIDE:PLAINTEXT,OUTSIDE:PLAINTEXT
      - KAFKA_LISTENERS=INSIDE://0.0.0.0:9093,OUTSIDE://0.0.0.0:9092
      - KAFKA_INTER_BROKER_LISTENER_NAME=INSIDE
      - KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181
    depends_on:
      - zookeeper
    networks:
      - microservice-network

  kafka-ui:
    container_name: kafka-ui
    image: provectuslabs/kafka-ui
    ports:
      - "9090:8080"
    restart: always
    environment:
      - KAFKA_CLUSTERS_0_NAME=local
      - KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS=kafka:9093
      - KAFKA_CLUSTERS_0_ZOOKEEPER=zookeeper:2181
    networks:
      - microservice-network

volumes:
  zookeeper_data:
    driver: local
  kafka_data:
    driver: local
  mysql_data:
    driver: local
  redis_data:
    driver: local

networks:
  microservice-network:
    driver: bridge
```

## API Documentation

### Authentication Flow
1. **Register/Login** → Nhận JWT token
2. **Include token** trong header: `Authorization: Bearer <token>`
3. **Token validation** tại Gateway
4. **Route request** đến appropriate service

### Error Handling
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/v1/auth/login",
  "details": {
    "field": "email",
    "message": "Email is required"
  }
}
```

### Pagination
```json
{
  "content": [],
  "totalElements": 100,
  "totalPages": 10,
  "size": 10,
  "number": 0,
  "first": true,
  "last": false
}
```

## Security

### JWT Token Structure
```json
{
  "sub": "user123",
  "email": "user@example.com",
  "roles": ["ROLE_USER"],
  "iat": 1642248600,
  "exp": 1642335000
}
```

### Security Headers
- `Authorization: Bearer <token>`
- `Content-Type: application/json`
- CORS configuration
- CSRF protection

## Monitoring & Logging

### Health Checks
- `/actuator/health` - Service health
- `/actuator/info` - Service information
- `/actuator/metrics` - Performance metrics

### Service URLs
- **Eureka Dashboard**: http://localhost:8761
- **Kafka UI**: http://localhost:9090
- **Gateway**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html

## Troubleshooting

### Common Issues

#### 1. Service Discovery Issues
```bash
# Kiểm tra Eureka server
curl http://localhost:8761/eureka/apps

# Kiểm tra service registration
docker-compose logs eureka-server
```

#### 2. Database Connection Issues
```bash
# Kiểm tra MySQL connection
docker-compose logs mysql

# Test connection
mysql -h localhost -P 3306 -u sa -p microservice
```

#### 3. JWT Token Issues
- Kiểm tra JWT secret key
- Verify token expiration
- Check token format

#### 4. Kafka Connection Issues
```bash
# Kiểm tra Kafka status
docker-compose logs kafka

# Test Kafka connection
docker exec -it kafka kafka-topics.sh --bootstrap-server localhost:9092 --list
```

#### 5. CORS Issues
- Kiểm tra Gateway CORS configuration
- Verify frontend URL trong CORS settings

### Debug Commands
```bash
# Kiểm tra tất cả containers
docker-compose ps

# Xem logs của service cụ thể
docker-compose logs -f auth-service

# Restart service
docker-compose restart auth-service

# Kiểm tra network
docker network ls
docker network inspect microservice-shopee_microservice-network
```

## Development Guidelines

### Code Structure
```
src/main/java/com/example/{service}/
├── controller/     # REST controllers
├── service/        # Business logic
├── repository/     # Data access
├── model/          # Entity models
├── dto/            # Data transfer objects
├── config/         # Configuration
├── util/           # Utilities
└── client/         # Feign clients
```

### Naming Conventions
- Controllers: `{Entity}Controller`
- Services: `{Entity}Service`
- Repositories: `{Entity}Repository`
- DTOs: `{Entity}Dto`
- Models: `{Entity}`

### API Testing
```bash
# Test authentication
curl -X POST http://localhost:8080/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password123"}'

# Test product list
curl -X GET http://localhost:8080/v1/stock/product/list?pageNo=1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Performance & Scalability

### Caching Strategy
- Redis cho session storage
- Product data caching
- User profile caching

### Database Optimization
- Connection pooling
- Query optimization
- Indexing strategy

### Load Balancing
- Eureka service discovery
- Gateway load balancing
- Horizontal scaling support

## Contributing

### Git Workflow
1. Create feature branch
2. Implement changes
3. Write tests
4. Submit pull request
5. Code review
6. Merge to main

### Code Standards
- Follow Spring Boot conventions
- Write comprehensive tests
- Document API endpoints
- Use meaningful commit messages
```

Tôi đã cập nhật file README cho backend với thông tin chi tiết hơn bao gồm:

### Cải tiến chính:

1. **Cài đặt chi tiết**:
   - 3 cách chạy dự án (Docker Compose, từng service riêng, IDE)
   - Hướng dẫn cài đặt Docker cho Windows/Linux/macOS
   - Thứ tự chạy services
   - Commands kiểm tra và debug

2. **Service endpoints chi tiết**:
   - Tất cả endpoints của từng service
   - Port numbers chính xác
   - Request/Response examples đầy đủ
   - Features và chức năng của từng service

3. **Cấu hình chi tiết**:
   - Application properties cho từng service
   - Docker Compose configuration đầy đủ
   - Database schema
   - Security configuration

4. **Troubleshooting**:
   - Common issues và cách fix
   - Debug commands
   - Health check URLs
   - Service monitoring

5. **Development guidelines**:
   - Code structure
   - Naming conventions
   - API testing commands
   - Performance considerations

File README này giờ đây cung cấp hướng dẫn đầy đủ để developer có thể hiểu và chạy được hệ thống microservice một cách dễ dàng.
