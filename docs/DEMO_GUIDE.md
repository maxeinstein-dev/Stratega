# Stratega Back-End Demo Guide

This guide helps reviewers test the Stratega API without needing to run the front-end locally.

## Live Services

- Front-end target domain: `https://stratega.maxsueleinstein.dev`
- Back-end base URL: `https://stratega-back.maxsueleinstein.dev`
- Current Render back-end URL: `https://stratega.onrender.com`
- Swagger UI: `/swagger-ui/index.html`
- API docs JSON: `/v3/api-docs`

The Swagger UI is protected with HTTP Basic Auth:

- Username: `swagger`
- Password: use the `SWAGGER_PASSWORD` value configured in Render.

Application endpoints use JWT Bearer authentication. Create a user, log in, and use the returned token in Swagger's `Authorize` modal.

## Demo Flow

### 1. Open the API

Open the Swagger UI:

```text
https://stratega-back.maxsueleinstein.dev/swagger-ui/index.html
```

If the custom domain is not active yet, use:

```text
https://stratega.onrender.com/swagger-ui/index.html
```

Log in with the Swagger credentials described above.

### 2. Confirm Service Health

Call:

```http
GET /
GET /health
```

Expected result:

- `/` returns the API name, status, and Swagger path.
- `/health` returns `status: ok`.

### 3. Register a Demo User

Call:

```http
POST /api/auth/register
```

Example body:

```json
{
  "name": "Demo Reviewer",
  "email": "demo.reviewer@example.com",
  "password": "DemoPass123!"
}
```

If the email already exists, change it to another address such as `demo.reviewer+2@example.com`.

### 4. Log In

Call:

```http
POST /api/auth/login
```

Example body:

```json
{
  "email": "demo.reviewer@example.com",
  "password": "DemoPass123!"
}
```

Copy the returned token. In Swagger, click `Authorize` and enter:

```text
Bearer <token>
```

### 5. Create a Wallet

Call:

```http
POST /api/wallets
```

Example body:

```json
{
  "name": "Main Wallet",
  "initialBalance": 2500.00,
  "userId": null,
  "currency": "BRL",
  "allowNegativeBalance": false
}
```

Copy the returned wallet `id`.

### 6. Create a Category

Call:

```http
POST /api/categories
```

Example body:

```json
{
  "name": "Freelance",
  "type": "INCOME",
  "userId": null
}
```

Copy the returned category `id`.

### 7. Create a Transaction

Call:

```http
POST /api/transactions
```

Example body:

```json
{
  "description": "International client payment",
  "amount": 1200.00,
  "date": "2026-09-11T12:00:00",
  "type": "INCOME",
  "walletId": "<wallet-id>",
  "categoryId": "<category-id>",
  "installments": null,
  "recurringMonths": null
}
```

Then call:

```http
GET /api/transactions
GET /api/wallets
GET /api/dashboard/summary
```

These calls demonstrate the authenticated financial workflow: account creation, wallet management, categorization, transactions, and dashboard summary.

## Custom Domain Setup

Target domains:

- `stratega.maxsueleinstein.dev` for the Cloudflare Pages front-end.
- `stratega-back.maxsueleinstein.dev` for the Render back-end.

Recommended DNS records:

| Hostname | Type | Target | Proxy |
| --- | --- | --- | --- |
| `stratega` | `CNAME` | the Stratega Front Cloudflare Pages domain, for example `stratega-front.pages.dev` | Proxied or automatic via Pages |
| `stratega-back` | `CNAME` | `stratega.onrender.com` | DNS only while Render verifies the custom domain |

Before creating the DNS record for the back-end, add `stratega-back.maxsueleinstein.dev` as a custom domain in the Render service. After DNS propagates, click `Verify` in Render.

For the front-end, add `stratega.maxsueleinstein.dev` in the Cloudflare Pages project's Custom domains section. If `maxsueleinstein.dev` is already managed by Cloudflare, Cloudflare can create the CNAME automatically.

## Render Environment Checklist

The back-end service should include:

```env
PORT=10000
SPRING_PROFILES_ACTIVE=prod
DB_HOST=<internal Render Postgres host>
DB_PORT=5432
DB_NAME=<database name>
DB_USER=<database user>
DB_PASSWORD=<database password>
JWT_SECRET=<strong generated secret>
JWT_EXPIRATION=86400000
SWAGGER_PASSWORD=<private Swagger password>
CORS_ALLOWED_ORIGIN=https://stratega.maxsueleinstein.dev
```

Use Render Postgres internal connection details for the database variables. Do not use a fixed IP address.
