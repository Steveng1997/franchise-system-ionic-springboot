# 🚀 Franchise Management System - Backend (Reactive API)

This project is a high-performance RESTful API designed to manage franchises, branches, and product inventory. It was built using a **Reactive Programming** paradigm to ensure high availability, non-blocking I/O, and superior scalability.

---

## 🛠️ Tech Stack

* **Runtime:** Java 17 (OpenJDK)
* **Framework:** Spring Boot 3.4+ (Spring WebFlux)
* **Paradigm:** Reactive Programming with Project Reactor
* **Persistence:** MongoDB (Reactive Driver)
* **Infrastructure:** Terraform (IaC) & Docker (Multi-stage Builds)
* **Tooling:** Lombok, Maven

---

## 🏗️ Architecture Design

The project strictly follows **Clean Architecture** principles to ensure a decoupled, testable, and maintainable codebase:

* **Domain Layer:** Contains core business entities (`Franchise`, `Branch`, `Product`) and repository interfaces.
* **Application Layer:** Implements business logic through **Reactive Services** using non-blocking operators.
* **Infrastructure Layer:** Manages REST Controllers, MongoDB persistence, and Infrastructure as Code definitions.

---

## 📸 Technical Evidence

Visual evidence of the implementation, including build processes and API testing, is available in the `docs/screenshots/` directory:

1.  **Autonomous Build (Multi-stage):** Optimized `Dockerfile` ensuring a lightweight and secure runtime.
    * *See: `docs/screenshots/build_process.png`*
2.  **Service Orchestration:** Ecosystem deployment using **Docker Compose** with full network isolation.
    * *See: `docs/screenshots/docker_status.png`*
3.  **API Integration Testing:** End-to-end validation of reactive flows through REST requests.
    * *See: `docs/screenshots/api_test.png.png`*

---

## 🧪 Testing

Code quality is verified through **Unit Tests** focusing on core business logic.
To run the test suite:
```bash
./mvnw test
