# 🚀 ThrottleX – Distributed API Gateway & Rate Limiter

ThrottleX is a **distributed, stateless API Gateway** built using **Java, Spring Boot, and Redis**, designed to securely route requests, enforce **rate limiting**, and protect backend services from traffic spikes and abuse.  
It is optimized for **high concurrency**, **horizontal scalability**, and **fault-tolerant API traffic management**.

---

## 📌 Overview

Modern applications often face unpredictable traffic patterns, API abuse, and scalability challenges.  
ThrottleX solves this by acting as a **centralized API Gateway** that:

- Routes incoming requests securely
- Applies **Redis-backed Token Bucket rate limiting**
- Handles traffic spikes gracefully
- Scales horizontally without session affinity

---

## ✨ Core Features

### 🔐 Stateless API Gateway
- Centralized request routing using Spring Boot REST APIs
- Fully stateless architecture enables horizontal scaling
- No server-side session dependency

### 🚦 Redis-Based Rate Limiting
- Implements **Token Bucket algorithm**
- Redis acts as a centralized, low-latency counter store
- Ensures consistent throttling across multiple instances

### ⚡ High-Concurrency Handling
- Optimized request flow for thousands of concurrent requests
- Minimal latency overhead using in-memory Redis operations

### 🧩 Fault Tolerance & Scalability
- Graceful degradation during Redis or network latency
- Designed to scale behind load balancers (Nginx / Cloud LB)
- Safe throttling logic prevents cascading failures

---

## 🛠️ Tech Stack

### Backend
- Java
- Spring Boot
- Spring Web (REST APIs)

### Caching & Rate Limiting
- Redis
- Token Bucket Algorithm

### Frontend (Demo / Client)
- HTML
- CSS
- JavaScript

### Architecture
- Stateless
- Distributed
- Horizontally Scalable

---

## 🧠 System Architecture

```text
Client
  |
  v
[ Load Balancer ]
  |
  v
+----------------------------+
|   ThrottleX API Gateway    |
|   - Request Routing        |
|   - Rate Limiting          |
+------------+---------------+
             |
             v
       Redis (Token Bucket)
             |
             v
[ Backend Services / APIs ]
```
### Architecture Highlights
- Every request passes through ThrottleX before reaching backend services
- Redis maintains token counts per client or API key
- Stateless gateway instances allow seamless horizontal scaling
- Centralized rate limiting ensures fairness and API protection

---

## 🚦 Rate Limiting Strategy

### Token Bucket Algorithm
- Each client is assigned a bucket with a fixed capacity
- Tokens are refilled at a defined rate
- Each request consumes one token
- Requests are rejected when tokens are exhausted

### Why Token Bucket?
- Allows controlled burst traffic
- Provides smoother throttling compared to fixed-window approaches
- Improves API consumer experience

---

## 🔌 Request Flow

1. Client sends request to ThrottleX
2. Gateway validates and inspects the request
3. Redis token bucket is checked
4. If token is available → request is forwarded
5. If token is not available → `429 Too Many Requests` is returned

---

## 🧪 Performance & Load Testing

- Simulated high-concurrency traffic scenarios
- Verified consistent rate limiting under peak load
- Optimized request flow to reduce gateway latency
- Ensured reliability during sudden traffic spikes

---

## 📁 Project Structure

```text
ThrottleX/
├── src/
│   └── main/
│       ├── java/
│       │   ├── config/
│       │   │   ├── RedisConfig.java          # Redis connection & cache configuration
│       │   │   └── SecurityConfig.java       # Gateway security configuration
│       │   ├── controller/
│       │   │   └── GatewayController.java    # Central request routing logic
│       │   ├── service/
│       │   │   └── RateLimiterService.java   # Token Bucket rate limiting logic
│       │   ├── util/
│       │   │   └── TokenBucket.java          # Rate limiting implementation
│       │   └── ThrottleXApplication.java     # Spring Boot application entry point
│       │
│       └── resources/
│           └── application.yml               # Application configuration
│
└── pom.xml                                   # Maven dependencies & build configuration
```
---

## 🎯 Use Cases

- API Gateway for microservices
- Rate limiting for public APIs
- Protecting backend services from abuse
- Traffic shaping for SaaS platforms
- Distributed systems and system-design learning

---

## 🔐 Security Considerations

- Stateless request validation
- Centralized throttling using Redis
- Easily extensible for:
  - API key validation
  - JWT-based authentication
  - IP-based rate limiting

---

## 🚀 Scalability Strategy

- Stateless gateway instances
- Shared Redis for global rate-limit state
- Compatible with:
  - Docker
  - Kubernetes
  - Cloud-based load balancers

---

## 📝 Notes

- Built as a **system-design–focused project**
- Demonstrates real-world API Gateway patterns
- Easily extendable with:
  - Metrics and monitoring
  - Distributed tracing
  - Circuit breakers
  - Request analytics

---

## 🤝 Contributing

This project is intended for educational and portfolio purposes.  
For production use, consider adding:

- Prometheus & Grafana monitoring
- Centralized logging
- Request tracing
- API analytics dashboard
- Advanced security policies

---

## ⭐ Support

If you find this project useful, please consider giving it a ⭐ on GitHub.

---

## 📄 License

This project is open-source and available under the **MIT License**.

