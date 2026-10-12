# Spring Contact Management API

RESTful API untuk manajemen kontak dan alamat, dibangun dengan Spring Boot 4.1.1, Spring Security + JWT, MySQL, JPA, dan SpringDoc OpenAPI (Swagger UI).

## Features

- Authentication & Authorization (JWT Access Token, Refresh Token di HttpOnly Cookie)
- Role-based access control (USER, ADMIN, SUPER_ADMIN) dengan Role Hierarchy
- CRUD Contact (user hanya bisa akses kontak miliknya sendiri)
- CRUD Address (nested di bawah contact)
- Pagination & Search (name/email/phone) untuk Contact
- Soft Delete dengan `@SQLRestriction`
- Global Exception Handling
- Rate Limiting untuk endpoint auth
- API Documentation (Swagger UI / OpenAPI)

## Tech Stack

- Java 21
- Spring Boot 4.1.1
- Spring Security 6.x
- Spring Data JPA
- Hibernate
- MySQL 8
- JJWT 0.13.0
- Lombok
- SpringDoc OpenAPI 3.1.0
- Maven

## Prerequisites

- JDK 21+
- Maven 3.9+
- Docker & Docker Compose (untuk menjalankan MySQL)

## Getting Started

### 1. Clone repository

```bash
git clone <repo-url>
cd spring-contact-management
```

### 2. Jalankan MySQL dengan Docker Compose

```bash
docker compose up -d
```

Service MySQL akan berjalan di `localhost:3306` dengan konfigurasi:

```yaml
MYSQL_DATABASE: contact_management
MYSQL_USER: myuser
MYSQL_PASSWORD: secret
MYSQL_ROOT_PASSWORD: verysecret
```

### 3. Konfigurasi Environment (opsional)

Bisa mengatur via environment variable atau `application.yaml`:

| Variable | Default | Deskripsi |
|---|---|---|
| `JWT_ACCESS_SECRET` | `wdgFBVS9P2joCLJ3eNmknbg3IXlkZBpMOgdpriotWH0=` | Base64/secret untuk signing JWT Access Token |
| `COOKIE_SECURE` | `true` | Secure flag untuk refresh token cookie |
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://localhost:3306/contact_management` | JDBC URL |
| `SPRING_DATASOURCE_USERNAME` | `myuser` | DB Username |
| `SPRING_DATASOURCE_PASSWORD` | `verysecret` | DB Password |

### 4. Build & Run

```bash
# Build project
./mvnw clean compile

# Jalankan aplikasi
./mvnw spring-boot:run
```

Aplikasi akan berjalan di `http://localhost:8080`.

### 5. API Documentation (Swagger UI)

Swagger UI tersedia di:

- `http://localhost:8080/swagger-ui.html`
- `http://localhost:8080/swagger-ui/index.html`

OpenAPI JSON: `http://localhost:8080/api-docs`

Untuk mengakses endpoint yang terproteksi, klik `Authorize` → masukkan `Bearer <accessToken>`.

## Database Schema

Tabel utama:
- `t_user` (user)
- `t_roles`, `t_permissions`, `t_user_roles`, `t_role_permissions`
- `t_refresh_token`
- `t_contacts` (kontak, milik user)
- `t_addresses` (alamat, milik contact)

Menggunakan soft delete (`deleted_at`, `deleted_by`) dengan `@SQLRestriction("deleted_at IS NULL")`.

## API Endpoints

Dokumentasi detail per module tersedia di folder [`docs/`](./docs):

- [User & Auth API](./docs/user.md)
- [Contact API](./docs/contact.md)
- [Address API](./docs/address.md)

Base URL: `/api/v1`

Format response umum (sukses):

```json
{
  "status": 200,
  "message": "Success",
  "data": {}
}
```

Format response error:

```json
{
  "timestamp": "2026-01-01T00:00:00",
  "status": 400,
  "message": "Bad Request",
  "error": {}
}
```

## Authentication Flow

1. `POST /api/v1/auth/register` → mendapatkan `accessToken` + `refresh_token` (cookie HttpOnly)
2. `POST /api/v1/auth/login` → login, set refresh token cookie
3. `POST /api/v1/auth/refresh-token` (dengan cookie refresh_token) → mendapatkan access token baru
4. Gunakan `Authorization: Bearer <accessToken>` untuk semua request terproteksi
5. `POST /api/v1/auth/logout` / `logout-all-devices` → revoke token & hapus cookie

## Role & Permission

Role hierarchy: `SUPER_ADMIN > ADMIN > USER`

- Update/Delete Contact: `ADMIN`, `SUPER_ADMIN`
- Update/Delete Address: `ADMIN`, `SUPER_ADMIN`
- Read/Create Contact & Address: `USER` (dan role di atasnya)
- User Management: sesuai definisi di User API

## Running Tests

```bash
./mvnw test
```

## License

MIT License
