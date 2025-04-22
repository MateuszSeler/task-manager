# Task Manager App

## 📌 Project Overview  

Task Manager is a **web application** designed to help teams organize and manage their projects efficiently. The application provides a **structured approach to task management**, ensuring that team members have clearly defined roles and responsibilities within each project. At its core, the system allows users to **create projects, assign tasks, track progress, and collaborate through comments and file attachments**. Access control is a key feature, ensuring that only authorized team members can view or modify project data. The application distinguishes between **administrative roles** (system-wide) and **project-specific roles**, enabling flexible permission management.  

A built-in **notification system** informs users about important updates, such as new tasks, status changes, or comments on tasks they are assigned to. The default implementation uses email notifications via Gmail, but the system is designed to be easily extended to other communication platforms

Task Manager also includes **cloud-based file storage** integration, with Dropbox as the default provider. This allows team members to upload, retrieve, and manage attachments related to specific tasks. The file storage module is built in a way that makes it possible to add new providers or switch to an internal storage system if necessary.  

The backend is implemented using **Spring Boot**, with authentication managed via **JWT and Spring Security**. Data persistence is handled through **MySQL**, with schema versioning managed by **Liquibase**. The application is fully containerized using **Docker**, and testing is automated with **JUnit, Testcontainers, and MailHog**.  

### 🚀 Features
✔️ **User authentication & authorization** (JWT, Spring Security)   
✔️ **Project Management** – Create projects accessible only to team members.   
✔️ **Task & Project management** (CRUD operations, labels, attachments, comments)  
✔️**Attachments** – Upload and manage file attachments.  
✔️ **Event-based notification system**  

---

## 🔹 Technologies & Requirements  
- **Java 17**  
- **Spring Boot**  
- **Maven** – Build system  
- **MySQL 8** – Database  
- **Docker** – Includes `Dockerfile` and `docker-compose.yml`  
- **Spring Security + JWT** – User authentication & authorization  
- **JUnit, Testcontainers, Awaitility, Mockito** – Unit and integration testing (running in Docker and MailHog)  
- **Liquibase** – Database versioning and migration  
- **Log4j2** – Centralized logging and error tracking  
- **MapStruct** – Automatic DTO ↔ Entity mapping  
- **Lombok** – Reduces boilerplate code (e.g., builders, getters/setters)  

---

## 📂 Project Structure  

The project follows a **layered architecture** to ensure maintainability, scalability, and clean separation of concerns. Below is the complete project structure along with its key components.

- **`config/`** – Contains configuration files, including security, object mapping, and external integrations (e.g., Dropbox).  
- **`controller/`** – REST controllers responsible for handling HTTP requests.  
- **`dto/`** – Data Transfer Objects, ensuring a clear separation between API responses and domain models.  
- **`exception/`** – Custom exception handling to manage application errors gracefully.  
- **`mapper/`** – Converts entities to DTOs and vice versa.  
- **`model/`** – JPA entities representing the database structure.  
- **`repository/`** – Spring Data JPA repositories for database access.  
- **`security/`** – Security and authentication layer, including JWT handling and user authentication.  
- **`service/`** – Business logic layer, handling application functionalities such as tasks, projects, and notifications.  
- **`validator/`** – Custom validation classes, e.g., for email validation.  

---

## 🔐 User Authentication & Authorization (JWT, Spring Security)  

The application implements **two levels of authorization**:  

1. **Global role-based access control (RBAC) using Spring Security**  
2. **Project-specific role management** handled by the `MemberService`  

### 🛡️ Role-Based Access Control (RBAC)  

Each user has one of the following global roles:  

- **`ADMIN`** – Can manage users and edit the global set of labels.  
- **`USER`** – Assigned automatically upon registration. Users can create and manage their own projects.  

### 📌 Project-Level Permissions  

Each project has a **list of members** with different levels of access:  

- **`Manager`** (automatically assigned to the project creator)  
  - Can add/remove **members** and **managers** 
  - Can create, assign, and manage **tasks** 
  - Can add **labels** 
  - Can update project settings  

- **`Member`** (added by a manager)  
  - Can **comment** on tasks  
  - Can **complete** assigned tasks
  - Can attachted and download **files** 
  - Can **comment** task and edit if they are the authors

### 🔍 Role Verification  

Project roles are managed by the **`MemberService`**, which ensures that only authorized users can perform specific actions within a project.  

The authentication mechanism uses:  
✔ **JWT tokens** for stateless authentication  
✔ **Spring Security** for endpoint protection  

All API requests requiring authentication must include a **JWT token** in the `Authorization

---

## 📎 Attachments – Upload and Manage File Attachments  

The application provides **file attachment management** with an integrated cloud storage solution.  

### 📂 Storage Providers  

Attachments can be stored using different storage providers. By default, the system supports **Dropbox**, but it is designed to be **easily extended** with additional storage services or a custom solution.  

✔ **Dropbox Integration** (default)  
✔ **Expandable to other APIs or in-house storage systems**  

### 🏗️ File Storage Implementation  

- **`FileStorageProviderFactory`** dynamically provides the correct storage implementation.  
- Uses a **service map approach**, where services are registered using `@Component("<PROVIDER_NAME>")`.  
- Example: Dropbox storage is registered as `@Component("DROPBOX")`.  

### 🔐 Dropbox Authentication Flow   

Since Dropbox API requires **OAuth 2.0**, the application:  

1. Uses a **refresh token** to request a new **access token**.  
2. Stores the **encrypted access token** in the database.  
3. Automatically renews the token upon expiration.  

### 📌 Features  

✔ **Secure file upload & download**  
✔ **Storage provider abstraction for easy extension**  
✔ **Encrypted token storage for Dropbox authentication**  
✔ **Automatic token refresh on expiration**  

---

## 🔔 Event-Based Notification System  

The notification system ensures that users are informed about important project updates while maintaining **flexibility, scalability, and performance**.  

### 🏗️ Architecture  

✔ **Centralized event handling** – `ChangeManager` serves as the main communication hub between the application and notification services.  
✔ **Flexible event structure** – `ProjectEvent` follows the **Builder Pattern**, allowing easy modification and extension.  
✔ **Asynchronous processing** – Notifications are sent concurrently, improving efficiency.  
✔ **Expandable notification channels** – Designed to integrate additional services (e.g., Slack, WebSockets).  

### 🔄 **Notification System Flow**  

1️⃣ **`ChangeManager` acts as the central notification coordinator**, receiving and dispatching project-related events.  

2️⃣ **`ProjectEvent` is a dynamic event model** based on the **Builder Pattern**, allowing flexible event definitions. Any project-related event can be constructed dynamically without requiring multiple classes.  

3️⃣ **Extending `ProjectEvent` is straightforward** – just add a new field to the class and its constructor. Thanks to Lombok annotations, the builder pattern will automatically include it, simplifying modifications.  

4️⃣ **Custom event types can be added when necessary** – while `ProjectEvent` covers most scenarios, separate event classes (e.g., `UserRegistrationEvent`) can be introduced where needed.  

5️⃣ **Notifications are processed asynchronously** – The notification service utilizes **multi-threading** to handle multiple recipients in parallel. Messages are passed through a **`CompletableFuture` pipeline**, allowing easy integration of additional notification services.  
ows seamless integration with **other notification systems** such as WebSockets, push notifications, or third-party services like Slack and Microsoft Teams. 

### 📌 Components  

- **`ProjectEvent`** – Represents an event within the system (task updates, attachments, comments, etc.).  
- **`ChangeManager`** – Manages event processing and dispatches notifications.  
- **`NotificationService`** – Sends notifications through various channels.  
- **`EmailService`** – Handles email notifications (integrated with Gmail).  


### ✉️ Email Notifications Configuration
By default, notifications are sent via email using Gmail SMTP, but additional notification methods (e.g., Slack, WebSockets, SMS) can be implemented.

✔ Uses Spring Mail API   
✔ Supports email authentication & encryption   
✔ Easily extendable to other notification services   

#### 📌 Configuring Email Notifications
To enable email notifications, configure the following properties in application.properties:

```java
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-email-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000
```

---

## 🧪 Testing

The application includes **unit tests, integration tests, and database migration tests** using:  

✔ **Unit tests** – Verifying individual components such as services, mappers, and validators.  
✔ **Integration tests** – Testing interactions between different layers (e.g., repository + database, service + external APIs).  
✔ **Database migration tests** – Ensuring schema consistency with **Liquibase**.  
✔ **Email testing** – Simulating email notifications using **MailHog**.  
✔ **Asynchronous testing** – Verifying non-blocking operations with **Awaitility**.  

### 🛠 Tools & Libraries Used  

| 🔹 Tool / Library  | 🔹 Purpose |
|--------------------|--------------------------------|
| **JUnit 5**        | Core framework for unit and integration testing. |
| **Mockito**        | Mocking framework for dependency isolation in unit tests. |
| **Testcontainers** | Runs database and email tests in **Docker containers**. |
| **MailHog**        | Simulates an SMTP server for email notification testing. |
| **Liquibase**      | Manages database migrations and ensures schema consistency. |
| **Awaitility**     | Helps test **asynchronous operations** by waiting for expected conditions. |


### ⚙️ Test Configuration in application-test.properties
The integration tests use a temporary MySQL instance managed by Testcontainers, avoiding the need for a real database.

```java
spring.datasource.url=jdbc:tc:mysql:8:///testdb
spring.datasource.username=test
spring.datasource.password=test

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true

spring.liquibase.change-log=classpath:db.testchangelog/db.changelog-master.yaml

spring.mail.host=localhost
spring.mail.port=1025
spring.mail.username=test
spring.mail.password=test
```

### 🛠 MailHog Troubleshooting  

MailHog sometimes **gets stuck** or stops responding. If that happens, restart it using the following commands:  

```sh
docker stop mailhog
docker rm mailhog
docker run --name mailhog -p 1025:1025 -p 8025:8025 mailhog/mailhog
After running these commands, MailHog should be accessible again at:
🔗 Web UI: http://localhost:8025
```

---


## ⚠️ Exception Management & Logging  

The application includes a **centralized exception handling and logging system** to ensure that errors are properly captured, logged, and returned to the client in a structured format.  


### 🛠 Centralized Exception Handling  

All exceptions are handled **globally** in `CustomGlobalExceptionHandler`, so developers **do not need to log errors manually in individual services or controllers**.  

✔ **Consistent error responses** for API clients.  
✔ **Automatic logging of all exceptions**, including validation errors, authentication failures, and external API issues.  
✔ **Separation of concerns**, making it easy to extend exception handling without modifying business logic.  

This means that **whenever an exception occurs, it is automatically logged**—developers don’t need to write explicit logging statements for every error scenario.  

The following exceptions are handled centrally:  

| Exception Type                | HTTP Status | Description |
|--------------------------------|------------|-------------|
| `MethodArgumentNotValidException` | `400 Bad Request` | Validation errors in API requests. |
| `RegistrationException`         | `400 Bad Request` | User registration failure. |
| `AuthenticationException`       | `401 Unauthorized` | Failed authentication attempts. |
| `EntityNotFoundException`       | `404 Not Found` | Requested resource not found. |
| `DataProcessingException`       | `400 Bad Request` | General data processing failure. |
| `DropBoxProcessingException`    | `500 Internal Server Error` | Dropbox API processing failure. |

### 📜 Structured Logging  

The logging system captures key events and errors in separate logs to facilitate debugging and monitoring.  

| Log File | Purpose |
|----------|---------|
| `logs/errors.log` | Stores **all critical application errors**, managed centrally in `CustomGlobalExceptionHandler`. |
| `logs/user-registrations.log` | Logs **new user registration attempts**, both successful and failed. |
| `logs/app-events.log` | Captures **important application events** such as project creation, task assignments, and permission changes. |

By maintaining separate log files, the system allows for **better monitoring and faster issue resolution**.  


This approach keeps error management **consistent, centralized, and scalable**.   


---


## 🌐 API Endpoints  

Below is a summary of the main API endpoints.  

### 🛡️ **Authentication**  
| Method | Endpoint             | Description                | Auth Required |
|--------|----------------------|----------------------------|---------------|
| `POST` | `/authentication/registration` | Register a new user | ❌ No |
| `POST` | `/authentication/login` | Authenticate and get JWT token | ❌ No |

### 👤 **User Management**  
| Method  | Endpoint         | Description              | Auth Required |
|---------|-----------------|--------------------------|---------------|
| `GET`   | `/users/me`      | Get own user profile    | ✅ Yes (USER) |
| `PUT`   | `/users/me`      | Update own profile      | ✅ Yes (USER) |
| `GET`   | `/users/{userId}` | Get user by ID         | ✅ Yes (ADMIN) |
| `DELETE` | `/users/{userId}` | Delete user by ID     | ✅ Yes (ADMIN) |

### 🏗️ **Project Management**  
| Method | Endpoint             | Description                | Auth Required |
|--------|----------------------|----------------------------|---------------|
| `POST` | `/projects`          | Create a new project       | ✅ Yes (USER) |
| `GET`  | `/projects`          | Get all of own user's projects      | ✅ Yes (USER) |
| `GET`  | `/projects/{id}`     | Get a specific project     | ✅ Yes (Member) |
| `PUT`  | `/projects/{id}`     | Update project details     | ✅ Yes (Manager) |
| `DELETE` | `/projects/{id}`   | Delete a project           | ✅ Yes (Manager/Admin) |

### 📋 **Task Management**  
| Method | Endpoint             | Description                | Auth Required |
|--------|----------------------|----------------------------|---------------|
| `POST` | `/projects/{projectId}/tasks` | Create a new task | ✅ Yes (Manager) |
| `GET`  | `/projects/{projectId}/tasks` | Get all tasks in a project | ✅ Yes (Member) |
| `GET`  | `/projects/{projectId}/tasks/{taskId}` | Get a specific task | ✅ Yes (Member) |
| `PUT`  | `/projects/{projectId}/tasks/{taskId}` | Update a task | ✅ Yes (Manager) |
| `DELETE` | `/projects/{projectId}/tasks/{taskId}` | Delete a task | ✅ Yes (Manager) |

### 📁 **Attachments**  
| Method | Endpoint             | Description                | Auth Required |
|--------|----------------------|----------------------------|---------------|
| `POST` | `/projects/{projectId}/tasks/{taskId}/attachments/{apiName}` | Upload file | ✅ Yes (Member) |
| `GET`  | `/projects/{projectId}/tasks/{taskId}/attachments/{fileId}` | Download file | ✅ Yes (Member) |
| `DELETE` | `/projects/{projectId}/tasks/{taskId}/attachments/{fileId}` | Delete file | ✅ Yes (Manager) |

### 💬 **Comments**  
| Method | Endpoint             | Description                | Auth Required |
|--------|----------------------|----------------------------|---------------|
| `POST` | `/projects/{projectId}/tasks/{taskId}/comments` | Add comment | ✅ Yes (Member) |
| `GET`  | `/projects/{projectId}/tasks/{taskId}/comments` | Get task comments | ✅ Yes (Member) |
| `PUT`  | `/projects/{projectId}/tasks/{taskId}/comments/{commentId}` | Edit comment | ✅ Yes (Author) |
| `DELETE` | `/projects/{projectId}/tasks/{taskId}/comments/{commentId}` | Delete comment | ✅ Yes (Manager/Author) |

### 🏷️ **Labels**  
| Method | Endpoint             | Description                | Auth Required |
|--------|----------------------|----------------------------|---------------|
| `POST` | `/labels`            | Create a new label        | ✅ Yes (Admin) |
| `GET`  | `/labels`            | Get all labels            | ✅ Yes (Admin) |
| `PUT`  | `/labels/{labelId}`  | Update label              | ✅ Yes (Admin) |
| `DELETE` | `/labels/{labelId}` | Delete label             | ✅ Yes (Admin) |

### 👥 **Team & Members**  
| Method | Endpoint             | Description                | Auth Required |
|--------|----------------------|----------------------------|---------------|
| `POST` | `/projects/{projectId}/members/{userId}` | Add member to project | ✅ Yes (Manager) |
| `DELETE` | `/projects/{projectId}/members/{userId}` | Remove member | ✅ Yes (Manager) |
| `POST` | `/projects/{projectId}/members/{userId}/managers` | Promote user to manager | ✅ Yes (Manager) |
| `DELETE` | `/projects/{projectId}/members/{userId}/managers` | Revoke manager role | ✅ Yes (Manager) |


### 🛠 **Authentication via JWT tokens**
All API endpoints require authentication via JWT tokens in the `Authorization` header: Authorization: Bearer <JWT_TOKEN>

---

## 📜 Summary  

Task Manager is a **role-based project and task management system**. The system is modular and allows for future extensions, including additional notification channels, integrations with external services, and UI enhancements.  

### 🔮 Future Enhancements  

Planned improvements and possible extensions:  

1️⃣ **Task Dependency Chains** – Ability to link tasks into logical sequences, ensuring that one task cannot start until another is completed.  
2️⃣ **In-App Notifications** – A notification center within the application for tracking important updates without relying on emails.  
3️⃣ **Social Media & External Authentication** – Integration with **Google, Facebook**, and other OAuth providers for faster and easier login.  
4️⃣ **Project Timeline View** – A visual timeline that helps teams track project progress over time.  
5️⃣ **Calendar Integration** – Syncing tasks and deadlines with calendar applications like Google Calendar and Outlook.  

These features would further enhance **collaboration, usability, and efficiency**, making the system more adaptable to various team workflows.  

---

## 👤 Author & Contact  

**Author:** Mateusz Seler  
📧 **Email:** [mate.tasks.manager@gmail.com](mailto:mate.tasks.manager@gmail.com)  

For any questions, suggestions, or contributions, feel free to reach out! 🚀  