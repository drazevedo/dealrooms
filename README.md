# 🧱 DealRoom – Project Architecture

This document describes the architecture and folder organization of the **DealRoom** project, built in Java and following **Clean Architecture** and **Domain-Driven Design (DDD)** principles.

---

## 🗂 Project Structure

```
src/main/java/com/bridge/dealroom/
│
├── application          → Use cases / Services / DTOs
│   ├── service           → Orchestrates business use cases
│   └── dto               → Data Transfer Objects (input/output)
│
├── domain               → Core business rules (rich domain entities)
│   ├── model             → Entities: DealRoom, Folder, Document, Permission, User
│   └── repository        → Repository interfaces (persistence contracts)
│
├── infrastructure        → Technical implementations and external integrations
│   └── config            → Configuration classes for Security, Swagger, Mongo
│
└── interfaces            → Interface layer (REST API)
    └── controller        → REST controllers and public endpoints
```

---

## 🧩 Architecture Overview

The project follows a layered and decoupled architecture, inspired by **Clean Architecture** and **DDD** principles:

| Layer | Responsibility |
|--------|----------------|
| **domain** | Contains the **core business logic** and domain entities, fully independent from frameworks. |
| **application** | Coordinates **use cases** and handles DTOs, calling domain logic as needed. |
| **infrastructure** | Implements **technical details** such as database access, storage, security, and configuration. |
| **interfaces** | Exposes the application to the external world through **REST APIs**. |

---

## 🔄 Execution Flow

1. A **Controller (interfaces)** receives an HTTP request.  
2. The **Service (application)** handles the use case, coordinating domain logic and data mapping.  
3. The **Domain** layer enforces business rules and uses **repository interfaces** for persistence.  
4. The **Infrastructure** layer implements these repositories using **MongoDB** or other technologies.  
5. The result is mapped back to a **DTO** and returned through the controller.

---

## ⚙️ Technologies and Principles

- **Java / Spring Boot**
- **MongoDB** for data persistence
- **Swagger / OpenAPI** for API documentation
- **Spring Security** for authentication and authorization
- **Clean Architecture**
- **Domain-Driven Design (DDD)**
- **Dependency Injection (IoC)** via Spring

---

## 🧠 Architectural Benefits

- **High cohesion and low coupling**
- **Testability**: business rules can be tested independently
- **Framework independence**
- **Ease of evolution** (e.g., changing the database or API layer)
- **Clear separation between business logic and technical implementation**

---

## 📹 Loom Video explaining the API

https://www.loom.com/share/1a223b264d8e464fb1eada4853bb06f9?sid=6acb8f6f-d922-4454-8b57-23f7d12d068d


_This architecture ensures that the business logic remains at the core of the system, while frameworks and external dependencies stay at the periphery, making the project modular, testable, and maintainable._
