        
## 1. GET /v1/order/getAll

### Request
```
GET /v1/order/getAll?status=PENDING
```

### Response
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "userId": "user123",
    "totalPrice": 150.50,
    "orderStatus": "PENDING",
    "notes": "Giao hàng vào buổi chiều",
    "shippingAddress": "123 Đường ABC, Quận 1, TP.HCM",
    "paymentMethod": "CASH",
    "creationTimestamp": "2024-01-15T10:30:00",
    "updateTimestamp": "2024-01-15T10:30:00",
    "orderItems": [
      {
        "id": "item001",
        "orderId": "550e8400-e29b-41d4-a716-446655440000",
        "productId": "prod123",
        "quantity": 2,
        "unitPrice": 75.25,
        "totalPrice": 150.50,
        "creationTimestamp": "2024-01-15T10:30:00",
        "updateTimestamp": "2024-01-15T10:30:00"
      }
    ]
  }
]
```

## 2. GET /v1/order/search

### Request
```
GET /v1/order/search?userId=user123&status=COMPLETED&startDate=2024-01-01&endDate=2024-12-31
```
### Response
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "userId": "user123",
    "totalPrice": 299.99,
    "orderStatus": "COMPLETED",
    "notes": "Đã giao hàng thành công",
    "shippingAddress": "456 Đường XYZ, Quận 2, TP.HCM",
    "paymentMethod": "CREDIT_CARD",
    "creationTimestamp": "2024-01-10T14:20:00",
    "updateTimestamp": "2024-01-12T16:45:00",
    "orderItems": [
      {
        "id": "item002",
        "orderId": "550e8400-e29b-41d4-a716-446655440001",
        "productId": "prod456",
        "quantity": 1,
        "unitPrice": 199.99,
        "totalPrice": 199.99,
        "creationTimestamp": "2024-01-10T14:20:00",
        "updateTimestamp": "2024-01-10T14:20:00"
      },
      {
        "id": "item003",
        "orderId": "550e8400-e29b-41d4-a716-446655440001",
        "productId": "prod789",
        "quantity": 1,
        "unitPrice": 100.00,
        "totalPrice": 100.00,
        "creationTimestamp": "2024-01-10T14:20:00",
        "updateTimestamp": "2024-01-10T14:20:00"
      }
    ]
  }
]
```

## 3. GET /v1/order/getOrderById/{id}

### Request
```
GET /v1/order/getOrderById/550e8400-e29b-41d4-a716-446655440000
```

### Response
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "userId": "user123",
  "totalPrice": 150.50,
  "orderStatus": "PENDING",
  "notes": "Giao hàng vào buổi chiều",
  "shippingAddress": "123 Đường ABC, Quận 1, TP.HCM",
  "paymentMethod": "CASH",
  "creationTimestamp": "2024-01-15T10:30:00",
  "updateTimestamp": "2024-01-15T10:30:00",
  "orderItems": [
    {
      "id": "item001",
      "orderId": "550e8400-e29b-41d4-a716-446655440000",
      "productId": "prod123",
      "quantity": 2,
      "unitPrice": 75.25,
      "totalPrice": 150.50,
      "creationTimestamp": "2024-01-15T10:30:00",
      "updateTimestamp": "2024-01-15T10:30:00"
    }
  ]
}
```

## 4. POST /v1/order/create-direct

### Request
```json
POST /v1/order/create-direct
Content-Type: application/json

{
  "orderItems": [
    {
      "productId": "prod123",
      "quantity": 2,
      "unitPrice": 75.25
    },
    {
      "productId": "prod456",
      "quantity": 1,
      "unitPrice": 100.00
    }
  ],
  "notes": "Giao hàng nhanh",
  "shippingAddress": "789 Đường DEF, Quận 3, TP.HCM",
  "paymentMethod": "BANK_TRANSFER"
}
```

### Response
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440002",
  "userId": "user123",
  "totalPrice": 250.50,
  "orderStatus": "PENDING",
  "notes": "Giao hàng nhanh",
  "shippingAddress": "789 Đường DEF, Quận 3, TP.HCM",
  "paymentMethod": "BANK_TRANSFER",
  "creationTimestamp": "2024-01-15T15:45:00",
  "updateTimestamp": "2024-01-15T15:45:00",
  "orderItems": [
    {
      "id": "item004",
      "orderId": "550e8400-e29b-41d4-a716-446655440002",
      "productId": "prod123",
      "quantity": 2,
      "unitPrice": 75.25,
      "totalPrice": 150.50,
      "creationTimestamp": "2024-01-15T15:45:00",
      "updateTimestamp": "2024-01-15T15:45:00"
    },
    {
      "id": "item005",
      "orderId": "550e8400-e29b-41d4-a716-446655440002",
      "productId": "prod456",
      "quantity": 1,
      "unitPrice": 100.00,
      "totalPrice": 100.00,
      "creationTimestamp": "2024-01-15T15:45:00",
      "updateTimestamp": "2024-01-15T15:45:00"
    }
  ]
}
```

## 5. PUT /v1/order/update/{id}

### Request
```json
PUT /v1/order/update/550e8400-e29b-41d4-a716-446655440000
Content-Type: application/json

{
  "orderStatus": "PROCESSING",
  "notes": "Đang xử lý đơn hàng",
  "shippingAddress": "123 Đường ABC, Quận 1, TP.HCM",
  "paymentMethod": "CASH"
}
```

### Response
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "userId": "user123",
  "totalPrice": 150.50,
  "orderStatus": "PROCESSING",
  "notes": "Đang xử lý đơn hàng",
  "shippingAddress": "123 Đường ABC, Quận 1, TP.HCM",
  "paymentMethod": "CASH",
  "creationTimestamp": "2024-01-15T10:30:00",
  "updateTimestamp": "2024-01-15T16:20:00",
  "orderItems": [
    {
      "id": "item001",
      "orderId": "550e8400-e29b-41d4-a716-446655440000",
      "productId": "prod123",
      "quantity": 2,
      "unitPrice": 75.25,
      "totalPrice": 150.50,
      "creationTimestamp": "2024-01-15T10:30:00",
      "updateTimestamp": "2024-01-15T10:30:00"
    }
  ]
}
```

## 6. PUT /v1/order/update-status/{id}

### Request
```
PUT /v1/order/update-status/550e8400-e29b-41d4-a716-446655440000?status=SHIPPED
```

### Response
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "userId": "user123",
  "totalPrice": 150.50,
  "orderStatus": "SHIPPED",
  "notes": "Đang xử lý đơn hàng",
  "shippingAddress": "123 Đường ABC, Quận 1, TP.HCM",
  "paymentMethod": "CASH",
  "creationTimestamp": "2024-01-15T10:30:00",
  "updateTimestamp": "2024-01-15T17:30:00",
  "orderItems": [
    {
      "id": "item001",
      "orderId": "550e8400-e29b-41d4-a716-446655440000",
      "productId": "prod123",
      "quantity": 2,
      "unitPrice": 75.25,
      "totalPrice": 150.50,
      "creationTimestamp": "2024-01-15T10:30:00",
      "updateTimestamp": "2024-01-15T10:30:00"
    }
  ]
}
```

## 7. DELETE /v1/order/delete/{id}

### Request
```
DELETE /v1/order/delete/550e8400-e29b-41d4-a716-446655440000
```

### Response
```json
"Order deleted successfully"
```

## 8. GET /v1/order/statistics

### Request
```
GET /v1/order/statistics?startDate=2024-01-01&endDate=2024-12-31
```

### Response
```json
{
  "totalOrders": 150,
  "totalRevenue": 45000.50,
  "averageOrderValue": 300.00,
  "ordersByStatus": {
    "PENDING": 25,
    "PROCESSING": 30,
    "SHIPPED": 45,
    "DELIVERED": 40,
    "CANCELLED": 10
  },
  "ordersByMonth": {
    "1": 12,
    "2": 15,
    "3": 18,
    "4": 22,
    "5": 20,
    "6": 25,
    "7": 28,
    "8": 30,
    "9": 25,
    "10": 22,
    "11": 18,
    "12": 15
  },
  "periodStart": "2024-01-01T00:00:00",
  "periodEnd": "2024-12-31T23:59:59"
}
```

## 9. GET /v1/order/getOrderByUserId

### Request
```
GET /v1/order/getOrderByUserId
Authorization: Bearer <jwt_token>
```

### Response
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "userId": "user123",
    "totalPrice": 150.50,
    "orderStatus": "PENDING",
    "notes": "Giao hàng vào buổi chiều",
    "shippingAddress": "123 Đường ABC, Quận 1, TP.HCM",
    "paymentMethod": "CASH",
    "creationTimestamp": "2024-01-15T10:30:00",
    "updateTimestamp": "2024-01-15T10:30:00",
    "orderItems": [
      {
        "id": "item001",
        "orderId": "550e8400-e29b-41d4-a716-446655440000",
        "productId": "prod123",
        "quantity": 2,
        "unitPrice": 75.25,
        "totalPrice": 150.50,
        "creationTimestamp": "2024-01-15T10:30:00",
        "updateTimestamp": "2024-01-15T10:30:00"
      }
    ]
  }
]
```

## Error Responses

### 400 Bad Request
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/v1/order/create-direct"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Order not found for ID: invalid-id",
  "path": "/v1/order/getOrderById/invalid-id"
}
```

### 500 Internal Server Error
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred",
  "path": "/v1/order/getAll"
}
```

## Order Status Values
- `PENDING` - Chờ xử lý
- `PROCESSING` - Đang xử lý
- `SHIPPED` - Đã giao cho shipper
- `DELIVERED` - Đã giao hàng
- `CANCELLED` - Đã hủy

## Payment Method Values
- `CASH` - Thanh toán tiền mặt
- `CREDIT_CARD` - Thẻ tín dụng
- `BANK_TRANSFER` - Chuyển khoản ngân hàng
- `E_WALLET` - Ví điện tử
