# AWS S3 Presigned Upload Service

A production-grade Spring Boot microservice designed to securely handle file uploads using AWS S3 Presigned URLs. 

This service acts as an orchestrator: it securely authenticates upload requests, generates temporary presigned URLs, and records file metadata in a PostgreSQL database. Clients use the generated URL to upload binary files directly to an AWS S3 bucket, completely bypassing the backend server to ensure maximum scalability and zero network bottlenecking.

## 🚀 Features

*   **Direct-to-S3 Uploads:** Offloads heavy network traffic by allowing clients to upload directly to AWS S3 using short-lived Presigned URLs.
*   **User Management:** Basic user entity tracking to associate uploaded files with specific users.
*   **Metadata Tracking:** Stores file metadata (original filename, S3 key, content type) in a relational database for easy querying.
*   **Secure Credential Management:** 100% credential-free codebase. Relies on the AWS Default Credentials Provider Chain (environment variables or IAM roles).

## 🏗️ Architecture & Best Practices

This project was built adhering strictly to Tier-1 enterprise standards:

*   **Interface-Driven Design:** Business logic is decoupled using the Interface-Implementation pattern (e.g., `FileUploadService` and `FileUploadServiceImpl`) to facilitate easy unit testing and mocking.
*   **Domain-Driven Segregation:** Cloud infrastructure code (`AwsS3Service`) is entirely isolated from business logic and database transactions.
*   **Memory Leak Prevention:** The AWS `S3Presigner` is managed as a Spring Singleton bean with an explicit `@Bean(destroyMethod = "close")` directive to properly close HTTP connection pools on application shutdown.
*   **Fail-Fast Validation:** Controller-level DTO validation (`@Valid`) blocks malformed requests before they consume service-layer CPU cycles.
*   **Transactional Integrity:** Database operations are wrapped in `@Transactional` to ensure file metadata is never persisted if the AWS URL generation fails.

## 💻 Tech Stack

*   **Java 17+**
*   **Spring Boot 3.x** (Web, Data JPA, Validation)
*   **AWS SDK v2** (S3 & Presigner)
*   **PostgreSQL**
*   **MapStruct** (for clean DTO mapping)
*   **Lombok** (for boilerplate reduction)

## 🛠️ Local Setup & Configuration

### Prerequisites
*   Java 17+ installed.
*   PostgreSQL running locally.
*   An active AWS Account with an S3 Bucket.

### Environment Variables
For security, AWS credentials are not stored in `application.yaml`. You must configure the following environment variables in your IDE (e.g., IntelliJ Run Configuration) or system environment:

```env
AWS_ACCESS_KEY_ID=your_access_key
AWS_SECRET_ACCESS_KEY=your_secret_key
```

### Application Properties
Update your `src/main/resources/application.yaml` (or pass as env variables) with your specific AWS and database details:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/your_db
    username: your_username
    password: your_password

  cloud:
    aws:
      region:
        static: ap-south-1 # e.g., your AWS region

app:
  aws:
    s3:
      bucket: your-unique-bucket-name
```

## 📡 API Endpoints

### 1. Generate Presigned URL
**POST** `/api/v1/files/presigned-url`

**Request Body:**
```json
{
  "userId": 1,
  "fileName": "profile-picture.jpg",
  "contentType": "image/jpeg"
}
```

**Response (201 Created):**
```json
{
  "s3Key": "uploads/users/1/550e8400-e29b-41d4-a716-446655440000-profile-picture.jpg",
  "presignedUrl": "https://your-bucket.s3.amazonaws.com/uploads/...",
  "fileId": 12
}
```

### 2. Client-Side Upload (Direct to S3)
**PUT** `<presignedUrl_from_previous_response>`

*   **Headers:** `Content-Type: image/jpeg` (Must exactly match the content type provided in step 1).
*   **Body:** Raw binary file.
*   *Note: This request goes directly to AWS, bypassing this Spring Boot application entirely.*

## 🛣️ Future Enhancements
*   [ ] Implementation of AWS SQS Event Listeners to asynchronously update database status from `PENDING` to `UPLOADED` upon successful S3 upload.