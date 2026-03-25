# Render Deployment Guide

## Setup Instructions for Airbnb Backend on Render.com

### Prerequisites
- GitHub account with your repository pushed
- Render.com account (free tier available)

### Step 1: Connect GitHub Repository
1. Go to [Render.com](https://render.com)
2. Sign up or log in to your account
3. Click "New +" → "Web Service"
4. Connect your GitHub account and select the `AirBnb` repository
5. Choose the repository branch (main)

### Step 2: Configure Service
The `render.yaml` file in the repository contains the configuration. If using manual setup:

**Service Configuration:**
- **Name:** airbnb-backend
- **Environment:** Java
- **Region:** Oregon (or your preferred region)
- **Plan:** Free (Starter tier)

**Build Command:**
```bash
mvn clean install -DskipTests
```

**Start Command:**
```bash
java -jar target/AirBnb-0.0.1-SNAPSHOT.jar
```

### Step 3: Set Environment Variables
Configure these in Render dashboard → Environment:

```
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8080
SPRING_DATASOURCE_URL=postgresql://user:password@host:5432/airbnb_db
SPRING_DATASOURCE_USERNAME=<postgres_username>
SPRING_DATASOURCE_PASSWORD=<postgres_password>
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.PostgreSQL10Dialect
JWT_SECRET=<your_secret_key>
STRIPE_SECRET_KEY=<your_stripe_key>
STRIPE_WEBHOOK_SECRET=<your_webhook_secret>
FRONTEND_URL=<your_frontend_url>
```

### Step 4: Create PostgreSQL Database (Optional)
If using the embedded PostgreSQL in Render:
1. Click "New +" → "PostgreSQL"
2. Name it: `airbnb-postgres`
3. Region: Same as web service
4. Database name: `airbnb_db`
5. Link it to the web service

### Step 5: Deploy
1. Click "Create Web Service"
2. Render automatically deploys on every push to main branch
3. Monitor deployment in the "Logs" tab

### Step 6: Access Your API
Once deployed, your API will be available at:
```
https://<render-service-name>.onrender.com/api/v1/swagger-ui.html
```

### Monitoring & Logs
- CPU usage, memory, and request metrics available in Dashboard
- View deployment logs in Logs tab
- Free tier has auto-sleep after 15 minutes of inactivity

### Important Notes
- **Free Tier Limitations:**
  - Auto-spins down after 15 minutes of no traffic
  - 0.5 GB RAM
  - Shared CPU
  - Limited to 100GB/month data transfer
  
- **For Production:**
  - Upgrade to Starter or Pro plan
  - Add custom domain
  - Enable auto-scaling
  - Set up error monitoring

### Troubleshooting

**Long build times:**
- Render free tier may take 5-10 minutes to build Maven project
- Can reduce by excluding test compilation

**Out of memory errors:**
- Reduce heap size in start command:
```bash
java -Xmx256m -jar target/AirBnb-0.0.1-SNAPSHOT.jar
```

**Database connection issues:**
- Verify environment variables are correct
- Check PostgreSQL database is running
- Ensure IP allowlist settings if applicable

### Auto-Deploy on Push
Render automatically deploys whenever you push to the configured branch:
```bash
git add .
git commit -m "Update: description"
git push origin main
```

### Rollback to Previous Deployment
1. Go to Render Dashboard
2. Select your service
3. Click "Deploys" tab
4. Select previous deployment
5. Click "Deploy"

---

**Repository:** https://github.com/Subodh26oct/AirBnb.git
**Free Tier:** Yes - starts at $0/month
**Deployment Time:** ~5-10 minutes
