# Railway Deployment Guide for ChroniCare Backend

## ✅ Pre-Deployment Checklist

All backend fixes have been applied and tested:
- [x] Fixed Medications NullPointerException and structure (schedule object)
- [x] Added GET /api/v1/alerts endpoint
- [x] Added exception handling to Medical Records endpoints
- [x] Fixed authorization with proper ROLE_ prefix
- [x] Server tested locally on port 11083
- [x] Configuration files created for Railway

## 📋 Deployment Steps

### 1. Create Railway Project

1. Go to [Railway.app](https://railway.app)
2. Sign in with GitHub
3. Click **"New Project"**
4. Select **"Deploy from GitHub repo"**
5. Choose the `ChroniCaree-Backend` repository
6. Select branch: `feature/frontend-backend-integration`

### 2. Add MySQL Database

1. In your Railway project dashboard, click **"+ New"**
2. Select **"Database"** → **"Add MySQL"**
3. Railway will automatically create a MySQL instance
4. Note the connection details (they'll be available as environment variables)

### 3. Configure Environment Variables

Go to your backend service → **Variables** tab and add:

#### Database Configuration (Auto-populated by Railway MySQL)
```
MYSQLHOST=<auto-populated>
MYSQLPORT=<auto-populated>
MYSQLDATABASE=railway
MYSQLUSER=<auto-populated>
MYSQLPASSWORD=<auto-populated>
```

#### Spring DataSource (Required)
```
SPRING_DATASOURCE_URL=jdbc:mysql://${MYSQLHOST}:${MYSQLPORT}/${MYSQLDATABASE}?useSSL=true&serverTimezone=UTC
SPRING_DATASOURCE_USERNAME=${MYSQLUSER}
SPRING_DATASOURCE_PASSWORD=${MYSQLPASSWORD}
```

#### JPA Configuration
```
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=false
SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.MySQL8Dialect
```

#### JWT Configuration (⚠️ CHANGE THESE!)
```
JWT_SECRET=<generate-a-secure-256-bit-key-here>
JWT_EXPIRATION_DAYS=7
```

**Generate a secure JWT secret:**
```bash
# Run this command to generate a secure key
openssl rand -base64 64
```

#### Server Configuration
```
PORT=8080
```

#### CORS Configuration
```
CORS_ALLOWED_ORIGINS=https://your-frontend-domain.com,http://localhost:4200
```

### 4. Configure Build Settings

Railway will automatically detect the Maven project. Verify in **Settings** tab:

- **Build Command**: `mvn clean install -DskipTests`
- **Start Command**: `java -Dserver.port=$PORT -jar target/chronicaree-backend-0.0.1-SNAPSHOT.jar`

These are already configured in `railway.json`.

### 5. Deploy

1. Click **"Deploy"** in Railway dashboard
2. Railway will:
   - Clone your repository
   - Run `mvn clean install -DskipTests`
   - Start the application with the configured start command
3. Monitor the deployment logs for any errors

### 6. Verify Deployment

Once deployed, Railway will provide a URL like: `https://chronicaree-backend-production.up.railway.app`

**Test the endpoints:**

```bash
# Health check (if Actuator is enabled)
curl https://your-app.railway.app/actuator/health

# Test alerts endpoint
curl https://your-app.railway.app/api/v1/alerts

# Test with authentication
curl -H "Authorization: Bearer YOUR_JWT_TOKEN" \
     https://your-app.railway.app/api/v1/records/patient/1
```

## 🔧 Configuration Files Created

### 1. `railway.json`
- Defines build and deployment configuration
- Specifies Maven build command
- Sets start command with dynamic port

### 2. `Procfile`
- Alternative deployment configuration
- Railway uses this if `railway.json` is not present

### 3. `.env.railway.example`
- Template for environment variables
- Copy these to Railway's Variables tab

### 4. `application.properties` (Updated)
- Now supports environment variables
- Database connection: `${SPRING_DATASOURCE_URL:localhost}`
- Port: `${PORT:11083}`
- JWT secret: `${JWT_SECRET:default}`

## 🔐 Security Best Practices

### ⚠️ CRITICAL: Change These Before Production

1. **JWT Secret**: Generate a new 256-bit secure key
2. **Database Password**: Use Railway's auto-generated password (don't use "1234")
3. **CORS Origins**: Restrict to your actual frontend domains
4. **Logging**: Set `spring.jpa.show-sql=false` in production

### Recommended Changes

```properties
# In Railway environment variables
SPRING_JPA_SHOW_SQL=false
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_COM_CHRONICARE=INFO
LOGGING_LEVEL_ORG_HIBERNATE=WARN
```

## 🚨 Common Issues & Solutions

### Issue 1: Build Fails
**Solution**: Check Maven logs in Railway dashboard. Common causes:
- Java version mismatch (ensure Railway uses Java 25)
- Missing dependencies in `pom.xml`
- Add to `railway.json`: `"nixpacksVersion": "1.21.0"`

### Issue 2: Database Connection Fails
**Solution**: 
- Verify MySQL service is running in Railway
- Check environment variables are correctly set
- Ensure `SPRING_DATASOURCE_URL` uses Railway's MySQL variables

### Issue 3: Port Binding Error
**Solution**: 
- Railway automatically assigns `$PORT` environment variable
- Ensure `application.properties` has: `server.port=${PORT:11083}`
- Don't hardcode port 11083 for production

### Issue 4: CORS Errors
**Solution**:
- Add frontend domain to `CORS_ALLOWED_ORIGINS`
- Update CORS configuration in Spring Security config
- Example: `https://your-frontend.vercel.app,https://your-frontend.railway.app`

### Issue 5: 403 Authorization Errors
**Solution**: Already fixed! ✅
- `UserDetailsImpl` now adds "ROLE_" prefix
- JWT tokens include correct roles
- Verify JWT is not expired

## 📊 Post-Deployment Monitoring

### Check Application Logs
```bash
# In Railway dashboard, go to your service → Deployments → View Logs
```

### Key things to monitor:
- Application startup time
- Database connection status
- Any exception stack traces
- API endpoint response times

### Verify Database Tables
Railway MySQL should have all tables created by Hibernate:
```sql
SHOW TABLES;
-- Should see: alerts, appointments, attachments, diagnosis, doctors, 
-- medical_records, medications, medication_schedules, messages, patients, etc.
```

## 🌐 Frontend Integration

Update your Angular frontend to use the Railway URL:

```typescript
// environment.prod.ts
export const environment = {
  production: true,
  apiUrl: 'https://chronicaree-backend-production.up.railway.app/api/v1'
};
```

## 📝 Next Steps After Deployment

1. **Test all endpoints** with Postman or curl
2. **Monitor logs** for any runtime errors
3. **Set up custom domain** (optional) in Railway settings
4. **Enable HTTPS** (Railway provides this automatically)
5. **Set up CI/CD** (Railway auto-deploys on git push)
6. **Configure alerts** in Railway for deployment failures

## 🔄 Continuous Deployment

Railway automatically redeploys when you push to the configured branch:

```bash
git add .
git commit -m "Fix: Updated medication schedule structure"
git push origin feature/frontend-backend-integration
```

Railway will automatically:
1. Detect the push
2. Run the build command
3. Deploy the new version
4. Keep the old version running until the new one is healthy

## 📞 Support Resources

- **Railway Docs**: https://docs.railway.app
- **Railway Discord**: https://discord.gg/railway
- **Spring Boot on Railway**: https://docs.railway.app/guides/spring-boot

---

## ✅ Deployment Checklist

Before going live, verify:

- [ ] All environment variables configured in Railway
- [ ] JWT secret changed from default value
- [ ] CORS origins restricted to actual frontend domains
- [ ] MySQL database connected and accessible
- [ ] `spring.jpa.show-sql=false` for production
- [ ] Logging level set to INFO/WARN (not DEBUG)
- [ ] All endpoints return correct JSON structures
- [ ] Test with actual JWT tokens
- [ ] Frontend updated with production API URL
- [ ] Health check endpoint responding
- [ ] Database tables created successfully

## 🎉 Deployment Complete!

Your ChroniCare backend is now running on Railway with:
- ✅ Fixed medication schedule structure
- ✅ GET /alerts endpoint added
- ✅ Exception handling in medical records
- ✅ Proper role-based authorization
- ✅ Environment-based configuration
- ✅ Automatic deployments on git push

---

**Last Updated**: January 2025  
**Version**: 1.0.0  
**Branch**: feature/frontend-backend-integration
