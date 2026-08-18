# 🚀 SpendWise Backend API (Node.js + Express + MongoDB)

Production-ready REST API backend for the **SpendWise** Personal Finance & Investment App. Built with Express, MongoDB Atlas / Mongoose, JWT authentication, and structured controllers.

---

## 📁 Project Structure

```
backend/
├── .env.example              # Environment variables template
├── Dockerfile                # Production Docker container image
├── docker-compose.yml        # Multi-container setup (Express + MongoDB)
├── package.json              # Dependencies and scripts
└── src/
    ├── server.js             # Main server entrypoint
    ├── config/
    │   └── db.js             # MongoDB Mongoose connection
    ├── models/               # MongoDB Mongoose Schemas
    │   ├── User.js           # User profile, KYC status, risk tier
    │   ├── Expense.js        # Expenses, categories, payment methods
    │   ├── Investment.js     # Stocks, Mutual Funds, Gold, SGBs, PnL
    │   ├── Goal.js           # Financial goals, target dates, SIPs
    │   └── Transaction.js    # Investment trades and ledger entries
    ├── middleware/
    │   ├── auth.js           # JWT Bearer Token verification
    │   └── errorHandler.js   # Centralized error handler
    ├── controllers/          # Business logic handlers
    │   ├── authController.js
    │   ├── dashboardController.js
    │   ├── expenseController.js
    │   ├── investmentController.js
    │   ├── goalController.js
    │   └── transactionController.js
    ├── routes/               # Express API route declarations
    │   ├── authRoutes.js
    │   ├── dashboardRoutes.js
    │   ├── expenseRoutes.js
    │   ├── investmentRoutes.js
    │   ├── goalRoutes.js
    │   └── transactionRoutes.js
    └── seed/
        └── seedData.js       # Pre-populates database with demo data
```

---

## ⚡ Quick Start

### 1. Prerequisites
- **Node.js** (v18+)
- **MongoDB** (Local instance or free [MongoDB Atlas Cluster](https://www.mongodb.com/cloud/atlas))

### 2. Installation & Setup
```bash
# Navigate to backend directory
cd backend

# Install dependencies
npm install

# Configure environment variables
cp .env.example .env
```

Edit `.env` with your configuration:
```ini
PORT=5000
NODE_ENV=development
MONGODB_URI=mongodb://localhost:27017/spendwise
# For Atlas: mongodb+srv://<username>:<password>@cluster0.xxx.mongodb.net/spendwise?retryWrites=true&w=majority
JWT_SECRET=spendwise_super_secret_jwt_key_2026
ALLOW_ANONYMOUS_DEV=true
```

### 3. Seed Demo Data (Optional)
Populate your database with default user Alex Riviera, stock holdings, expenses, and goals:
```bash
npm run seed
```

### 4. Run Server
```bash
# Development mode with hot-reloading
npm run dev

# Production mode
npm start
```
The server will start at `http://localhost:5000` with the health check at `http://localhost:5000/health`.

---

## 🐳 Docker Deployment (1-Command Run)

Run both MongoDB and the SpendWise Backend API together using Docker Compose:

```bash
docker-compose up -d --build
```
This automatically spins up:
- **MongoDB** on `mongodb://localhost:27017`
- **SpendWise API** on `http://localhost:5000`

---

## 🌐 Connecting from SpendWise Android App

- **Android Emulator**: Use `http://10.0.2.2:5000/api/` (10.0.2.2 routes to host machine `localhost`).
- **Physical Device over Wi-Fi**: Use `http://<YOUR_COMPUTER_LOCAL_IP>:5000/api/` (e.g. `http://192.168.1.15:5000/api/`).
- **Cloud Hosted (Render/Railway/GCP)**: Use your public HTTPS URL (e.g. `https://spendwise-api.onrender.com/api/`).

---

## 📡 REST API Reference

### 🔐 Authentication (`/api/auth`)
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `POST` | `/api/auth/register` | Register new user account | ❌ No |
| `POST` | `/api/auth/login` | Log in and receive JWT token | ❌ No |
| `GET` | `/api/auth/me` | Get current user profile & KYC details |  Yes (Bearer Token) |

#### Sample Login Request:
```json
POST /api/auth/login
{
  "email": "alex.riviera@spendwise.io",
  "password": "password123"
}
```

---

### 📊 Dashboard (`/api/dashboard`)
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `GET` | `/api/dashboard/summary` | Net worth, monthly savings, portfolio summary, and health score |  Yes |

---

### 💸 Expenses (`/api/expenses`)
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `GET` | `/api/expenses` | List all expenses with category breakdown |  Yes |
| `POST` | `/api/expenses` | Add a new expense |  Yes |
| `DELETE` | `/api/expenses/:id` | Remove an expense by ID |  Yes |

#### Sample Create Expense Request:
```json
POST /api/expenses
{
  "title": "Starbucks Coffee & Snacks",
  "category": "Dining & Food",
  "amount": 780.0,
  "formattedDate": "Today, 10:30 AM",
  "paymentMode": "UPI",
  "note": "Morning espresso"
}
```

---

### 📈 Investments (`/api/investments`)
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `GET` | `/api/investments` | Get portfolio holdings, asset allocation, and overall returns |  Yes |
| `POST` | `/api/investments` | Add new holding (Stock, Mutual Fund, Gold, etc.) |  Yes |
| `GET` | `/api/investments/:id` | Get asset details and transaction history |  Yes |

#### Sample Add Investment Request:
```json
POST /api/investments
{
  "symbol": "INFY",
  "name": "Infosys Ltd.",
  "type": "STOCK",
  "investedAmount": 32000.0,
  "currentValue": 37400.0,
  "holdingQty": 20,
  "avgPrice": 1600.0,
  "ltp": 1870.0
}
```

---

### 🎯 Financial Goals (`/api/goals`)
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `GET` | `/api/goals` | List all goals, target amounts, and SIP progress |  Yes |
| `POST` | `/api/goals` | Create a new financial goal |  Yes |
| `PATCH` | `/api/goals/:id/contribute` | Add money to an existing goal |  Yes |

---

## ☁️ Cloud Deployment Guide

### Option A: Deploy to Render (Recommended - Free & Easy)
1. Push your repository to **GitHub**.
2. Go to [Render.com](https://render.com) and click **New + Web Service**.
3. Select your repository and set the **Root Directory** to `backend`.
4. Set **Build Command** to `npm install` and **Start Command** to `npm start`.
5. Add Environment Variables:
   - `MONGODB_URI`: `<Your MongoDB Atlas Connection String>`
   - `JWT_SECRET`: `<A strong random key>`
   - `NODE_ENV`: `production`
6. Click **Deploy Web Service**.

---

### Option B: Deploy to Railway
1. Go to [Railway.app](https://railway.app) and create a **New Project**.
2. Add a **MongoDB** plugin (provisioned instantly).
3. Connect your GitHub repository and set the root directory to `/backend`.
4. Set `PORT=5000` and link `MONGODB_URI` from the MongoDB plugin.
5. Deploy.

---

### Option C: Deploy to Google Cloud Run
```bash
cd backend
gcloud builds submit --tag gcr.io/<YOUR_PROJECT_ID>/spendwise-backend
gcloud run deploy spendwise-backend \
  --image gcr.io/<YOUR_PROJECT_ID>/spendwise-backend \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --set-env-vars MONGODB_URI="<YOUR_ATLAS_URI>",JWT_SECRET="<SECRET>"
```
