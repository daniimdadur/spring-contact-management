# User API Spec

Base URL : `/api/v1`

Semua response sukses menggunakan format dari `Response<T>` :

```json
{
  "status" : 200,
  "message" : "Success",
  "data" : {}
}
```

Semua response error menggunakan format dari `ResponseError` :

```json
{
  "timestamp" : "2026-01-01T00:00:00",
  "status" : 400,
  "message" : "Bad Request",
  "error" : "detail error"
}
```

## Register User API

Endpoint : POST /api/v1/auth/register
Akses : Public

Request Body :

```json
{
  "firstName" : "Dani",
  "lastName" : "Imdadur",
  "username" : "imdadur",
  "password" : "rahasia"
}
```

Response Body Success (201 Created) :
Selain body di bawah, server juga mengirim header `Set-Cookie` berisi `refresh_token` (HttpOnly).

```json
{
  "status" : 201,
  "message" : "Created",
  "data" : {
    "accessToken" : "unique-access-token",
    "accessTokenExpiration" : 15,
    "refreshTokenExpiration" : 1440
  }
}
```

Response Body Error (409 Conflict) :

```json
{
  "timestamp" : "2026-01-01T00:00:00",
  "status" : 409,
  "message" : "Conflict",
  "error" : "Username already exists"
}
```

## Login User API

Endpoint : POST /api/v1/auth/login
Akses : Public

Request Body :

```json
{
  "username" : "imdadur",
  "password" : "rahasia"
}
```

Response Body Success (200 OK) :
Selain body di bawah, server juga mengirim header `Set-Cookie` berisi `refresh_token` (HttpOnly).

```json
{
  "status" : 200,
  "message" : "Success",
  "data" : {
    "accessToken" : "unique-access-token",
    "accessTokenExpiration" : 15,
    "refreshTokenExpiration" : 1440
  }
}
```

Response Body Error (401 Unauthorized) :

```json
{
  "timestamp" : "2026-01-01T00:00:00",
  "status" : 401,
  "message" : "Unauthorized",
  "error" : "Bad credentials"
}
```

## Login Logout API

Login sekaligus mencabut (revoke) seluruh refresh token yang lama, sehingga user hanya login pada satu device saja. Endpoint : POST /api/v1/auth/login-logout
Akses : Public

Request Body :

```json
{
  "username" : "imdadur",
  "password" : "rahasia"
}
```

Response Body Success (200 OK) :
Server juga mengirim header `Set-Cookie` berisi `refresh_token` (HttpOnly).

```json
{
  "status" : 200,
  "message" : "Success",
  "data" : {
    "accessToken" : "unique-access-token",
    "accessTokenExpiration" : 15,
    "refreshTokenExpiration" : 1440
  }
}
```

Response Body Error (401 Unauthorized) :

```json
{
  "timestamp" : "2026-01-01T00:00:00",
  "status" : 401,
  "message" : "Unauthorized",
  "error" : "Bad credentials"
}
```

## Refresh Token API

Menghasilkan access token baru menggunakan refresh token yang tersimpan di cookie. Endpoint : POST /api/v1/auth/refresh-token
Akses : Public

Cookies :
- refresh_token : refresh token (dikirim otomatis oleh browser)

Response Body Success (201 Created) :

```json
{
  "status" : 201,
  "message" : "Created",
  "data" : {
    "accessToken" : "new-access-token",
    "accessTokenExpiration" : 15
  }
}
```

Response Body Error (401 Unauthorized) :

```json
{
  "timestamp" : "2026-01-01T00:00:00",
  "status" : 401,
  "message" : "Unauthorized",
  "error" : "Invalid or expired refresh token"
}
```

## Logout User API

Mencabut refresh token device saat ini dan menghapus cookie `refresh_token`. Endpoint : POST /api/v1/auth/logout
Akses : Public

Cookies :
- refresh_token : refresh token (dikirim otomatis oleh browser)

Response Body Success (200 OK) :

```json
{
  "status" : 200,
  "message" : "Success",
  "data" : "Logout successful from this device"
}
```

Response Body Error (401 Unauthorized) :

```json
{
  "timestamp" : "2026-01-01T00:00:00",
  "status" : 401,
  "message" : "Unauthorized",
  "error" : "Invalid or expired refresh token"
}
```

## Logout All Devices API

Mencabut seluruh refresh token milik user dan menghapus cookie `refresh_token`. Endpoint : POST /api/v1/auth/logout-all-devices
Akses : Public

Cookies :
- refresh_token : refresh token (dikirim otomatis oleh browser)

Response Body Success (200 OK) :

```json
{
  "status" : 200,
  "message" : "Success",
  "data" : "Logout successful from all devices"
}
```

Response Body Error (401 Unauthorized) :

```json
{
  "timestamp" : "2026-01-01T00:00:00",
  "status" : 401,
  "message" : "Unauthorized",
  "error" : "Invalid or expired refresh token"
}
```

## Get Users API

Endpoint : GET /api/v1/users
Akses : Role USER

Headers :
- Authorization : Bearer {accessToken}

Query params :
- page : number of page, default 0
- size : size per page, default 20
- sort : field untuk sorting, default createdAt
- direction : arah sorting (ASC / DESC), default ASC

Response Body Success (200 OK) :

```json
{
  "status" : 200,
  "message" : "Success",
  "data" : {
    "content" : [
      {
        "id" : "randomUUID",
        "firstName" : "Dani",
        "lastName" : "Imdadur",
        "username" : "imdadur",
        "roles" : ["USER"]
      }
    ],
    "page" : 0,
    "size" : 20,
    "totalElements" : 1,
    "totalPages" : 1,
    "first" : true,
    "last" : true,
    "hasNext" : false,
    "hasPrevious" : false
  }
}
```

Response Body Error (401 Unauthorized) :

```json
{
  "timestamp" : "2026-01-01T00:00:00",
  "status" : 401,
  "message" : "Unauthorized",
  "error" : "Unauthorized"
}
```

## Update User Roles API

Endpoint : POST /api/v1/users/:userId/roles
Akses : Role SUPER_ADMIN

Headers :
- Authorization : Bearer {accessToken}

Request Body :

```json
{
  "roles" : ["ADMIN", "SUPER_ADMIN"]
}
```

Catatan : nilai `roles` yang valid adalah `USER`, `ADMIN`, `SUPER_ADMIN`.

Response Body Success (200 OK) :

```json
{
  "status" : 200,
  "message" : "Updated",
  "data" : "User roles updated successfully"
}
```

Response Body Error (404 Not Found) :

```json
{
  "timestamp" : "2026-01-01T00:00:00",
  "status" : 404,
  "message" : "Not Found",
  "error" : "User not found"
}
```

Response Body Error (400 Bad Request) :

```json
{
  "timestamp" : "2026-01-01T00:00:00",
  "status" : 400,
  "message" : "Bad Request",
  "error" : "One or more specified roles do not exist"
}
```

Response Body Error (403 Forbidden) :

```json
{
  "timestamp" : "2026-01-01T00:00:00",
  "status" : 403,
  "message" : "Forbidden",
  "error" : "Access Denied"
}
```
