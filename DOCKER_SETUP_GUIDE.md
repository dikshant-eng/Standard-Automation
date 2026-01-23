# Docker Setup Guide for Healenium

## 📋 Overview

Healenium requires **3 Docker containers** to run:

| Service | Purpose | Port | Image |
|---------|---------|------|-------|
| PostgreSQL | Stores healing data | 5432 | postgres:14-alpine |
| Healenium Backend | Core healing engine + Web UI | 7878 | healenium/hlm-backend:3.5.1 |
| Selector Imitator | ML-powered element matching | 8000 | healenium/hlm-selector-imitator:1.3 |

---

## 🔧 Docker Configuration Details

### **1. PostgreSQL Database**

```yaml
postgres-db:
  image: postgres:14-alpine
  container_name: healenium-postgres
  ports:
    - "5432:5432"
  volumes:
    - ./db-data:/var/lib/postgresql/data
  environment:
    - POSTGRES_DB=healenium
    - POSTGRES_USER=healenium_user
    - POSTGRES_PASSWORD=YourStrongPassword
```

**Configuration Options:**

| Setting | Default Value | Can Change? | Purpose |
|---------|--------------|-------------|---------|
| `POSTGRES_DB` | healenium | ⚠️ No | Database name (must match backend) |
| `POSTGRES_USER` | healenium_user | ⚠️ No | Username (must match backend) |
| `POSTGRES_PASSWORD` | YourStrongPassword | ✅ Yes | Database password |
| Port | 5432 | ✅ Yes | PostgreSQL port |
| Volume | ./db-data | ✅ Yes | Data persistence location |

**To Change Password:**

Edit `docker-compose.yml`:
```yaml
environment:
  - POSTGRES_PASSWORD=MySecurePassword123  # Change here

# Also update in healenium-backend:
healenium-backend:
  environment:
    - SPRING_POSTGRES_PASSWORD=MySecurePassword123  # And here
```

---

### **2. Healenium Backend**

```yaml
healenium-backend:
  image: healenium/hlm-backend:3.5.1
  container_name: healenium-backend
  ports:
    - "7878:7878"
  environment:
    - SPRING_POSTGRES_DB=healenium
    - SPRING_POSTGRES_USER=healenium_user
    - SPRING_POSTGRES_PASSWORD=YourStrongPassword
    - SPRING_POSTGRES_URL=jdbc:postgresql://postgres-db:5432/healenium?currentSchema=healenium
  depends_on:
    - postgres-db
  volumes:
    - ./screenshots:/screenshots
```

**Configuration Options:**

| Setting | Default Value | Can Change? | Purpose |
|---------|--------------|-------------|---------|
| Image version | 3.5.1 | ⚠️ No* | Must match Java client version |
| Port | 7878 | ✅ Yes | API and Web UI port |
| Database URL | postgres-db:5432 | ⚠️ No | Internal Docker network |
| Screenshots volume | ./screenshots | ✅ Yes | Screenshot storage location |

*Only change if you also update the Java dependency version in pom.xml

**To Change Port:**

```yaml
ports:
  - "8080:7878"  # Access via localhost:8080 instead

# Also update healenium.properties:
serverPort = 8080
```

---

### **3. Selector Imitator**

```yaml
healenium-selector-imitator:
  image: healenium/hlm-selector-imitator:1.3
  container_name: healenium-selector-imitator
  ports:
    - "8000:8000"
```

**Configuration Options:**

| Setting | Default Value | Can Change? | Purpose |
|---------|--------------|-------------|---------|
| Image version | 1.3 | ⚠️ Rarely | ML algorithm version |
| Port | 8000 | ✅ Yes | Service port |

**To Change Port:**

```yaml
ports:
  - "8001:8000"  # Different external port
```

---

## 🚀 Setup Instructions

### **Prerequisites**

#### **1. Install Docker Desktop**

**macOS:**
```bash
# Download and install from:
# https://www.docker.com/products/docker-desktop

# Or via Homebrew:
brew install --cask docker
```

Then:
1. Open Docker Desktop app
2. Wait for it to start (whale icon in menu bar)
3. Verify:
   ```bash
   docker --version
   docker-compose --version
   ```

#### **2. System Requirements**

- **Disk Space**: ~500MB for images + data
- **RAM**: At least 4GB available
- **Ports**: 5432, 7878, 8000 must be free

**Check if ports are free:**
```bash
lsof -i :5432
lsof -i :7878
lsof -i :8000

# If any show output, those ports are in use
```

---

### **Quick Start**

#### **Method 1: Using Helper Script** ⭐ Recommended

```bash
cd /Users/vivekkhurana/Work/Korecent/KorecentGems

# Start services
./start-healenium.sh

# Verify all services are running
./verify-healenium.sh

# Stop services when done
./stop-healenium.sh
```

#### **Method 2: Manual Docker Commands**

```bash
cd /Users/vivekkhurana/Work/Korecent/KorecentGems

# Pull images (first time only)
docker-compose pull

# Start services in background
docker-compose up -d

# Check status
docker-compose ps

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

---

## ✅ Verification Steps

### **1. Check Service Status**

```bash
# Quick check
./verify-healenium.sh

# Or manually
docker-compose ps
```

**Expected Output:**
```
NAME                           STATUS          PORTS
healenium-postgres             Up              0.0.0.0:5432->5432/tcp
healenium-backend              Up              0.0.0.0:7878->7878/tcp
healenium-selector-imitator    Up              0.0.0.0:8000->8000/tcp
```

### **2. Test Web UI**

```bash
# Open in browser
open http://localhost:7878/healenium/report

# Or use curl
curl http://localhost:7878/actuator/health
```

**Expected Response:**
```json
{"status":"UP"}
```

### **3. Test Database Connection**

```bash
# Connect to PostgreSQL
docker exec -it healenium-postgres psql -U healenium_user -d healenium

# Inside psql:
\dt healenium.*  # List tables
\q               # Quit
```

### **4. Check Logs**

```bash
# All services
docker-compose logs

# Specific service
docker-compose logs healenium-backend

# Follow logs in real-time
docker-compose logs -f healenium-backend
```

---

## 🗂️ Data Management

### **Persistent Data Locations**

```
KorecentGems/
├── db-data/              # PostgreSQL database files
│   ├── pg_data/          # Don't modify manually
│   └── ...
└── screenshots/          # Healing screenshots
    ├── 2025-10-09/       # Organized by date
    └── ...
```

### **Backup Data**

```bash
# Backup database
docker exec healenium-postgres pg_dump -U healenium_user healenium > healenium_backup.sql

# Backup screenshots
tar -czf screenshots_backup.tar.gz screenshots/
```

### **Restore Data**

```bash
# Restore database
cat healenium_backup.sql | docker exec -i healenium-postgres psql -U healenium_user -d healenium

# Restore screenshots
tar -xzf screenshots_backup.tar.gz
```

### **Clean All Data (Fresh Start)**

```bash
# WARNING: This deletes all healing history!
docker-compose down -v
rm -rf db-data/
rm -rf screenshots/
docker-compose up -d
```

---

## 🔧 Common Configurations

### **1. Use Custom Ports**

If ports 5432, 7878, or 8000 are already in use:

```yaml
# docker-compose.yml
services:
  postgres-db:
    ports:
      - "5433:5432"  # Use 5433 externally

  healenium-backend:
    ports:
      - "7879:7878"  # Use 7879 externally
    environment:
      - SPRING_POSTGRES_URL=jdbc:postgresql://postgres-db:5432/healenium?currentSchema=healenium
      # Note: Internal port stays 5432

  healenium-selector-imitator:
    ports:
      - "8001:8000"  # Use 8001 externally
```

Then update `healenium.properties`:
```properties
serverPort = 7879  # Match your new port
```

### **2. Increase Resource Limits**

For better performance with large test suites:

```yaml
healenium-backend:
  deploy:
    resources:
      limits:
        memory: 2G
        cpus: '2'
```

### **3. Enable Debug Logging**

```yaml
healenium-backend:
  environment:
    - LOGGING_LEVEL_ROOT=DEBUG
    - LOGGING_LEVEL_COM_EPAM_HEALENIUM=DEBUG
```

### **4. Custom Database Configuration**

```yaml
postgres-db:
  environment:
    - POSTGRES_DB=my_healenium_db
    - POSTGRES_USER=my_user
    - POSTGRES_PASSWORD=MySecurePass123
  
healenium-backend:
  environment:
    - SPRING_POSTGRES_DB=my_healenium_db
    - SPRING_POSTGRES_USER=my_user
    - SPRING_POSTGRES_PASSWORD=MySecurePass123
    - SPRING_POSTGRES_URL=jdbc:postgresql://postgres-db:5432/my_healenium_db?currentSchema=healenium
```

---

## 🐛 Troubleshooting

### **Issue: Services Won't Start**

**Check 1: Docker running?**
```bash
docker ps
# If error: Start Docker Desktop
```

**Check 2: Ports available?**
```bash
lsof -i :5432
lsof -i :7878
lsof -i :8000

# Kill processes using these ports or change ports in docker-compose.yml
```

**Check 3: Disk space?**
```bash
df -h
# Need at least 1GB free
```

### **Issue: Backend Can't Connect to Database**

```bash
# Check if PostgreSQL is ready
docker-compose logs postgres-db | grep "ready to accept connections"

# Restart backend
docker-compose restart healenium-backend

# Check backend logs
docker-compose logs healenium-backend
```

### **Issue: Out of Memory**

```bash
# Check Docker resource settings in Docker Desktop:
# Settings → Resources → Increase Memory to 4GB+

# Or add memory limits in docker-compose.yml
```

### **Issue: Old Data Causing Issues**

```bash
# Clean restart
docker-compose down -v
docker volume prune
docker-compose up -d
```

---

## 📊 Monitoring

### **View Service Status**

```bash
# Service status
docker-compose ps

# Resource usage
docker stats healenium-backend healenium-postgres

# Disk usage
docker system df
```

### **View Logs by Time**

```bash
# Last 100 lines
docker-compose logs --tail=100 healenium-backend

# Since specific time
docker-compose logs --since="2025-10-09T10:00:00" healenium-backend

# Last 1 hour
docker-compose logs --since="1h" healenium-backend
```

---

## 🔄 Updating Services

### **Update to New Version**

```bash
# Update docker-compose.yml with new version
# healenium/hlm-backend:3.5.2  (example)

# Pull new images
docker-compose pull

# Restart with new images
docker-compose down
docker-compose up -d

# Verify
docker-compose ps
```

---

## 🛡️ Security Considerations

### **1. Change Default Password**

```yaml
environment:
  - POSTGRES_PASSWORD=Use_A_Strong_Password_Here_123!@#
```

### **2. Restrict Network Access**

If you don't need external database access:

```yaml
postgres-db:
  ports:
    # Remove this line to make DB internal-only
    # - "5432:5432"
```

### **3. Use Docker Secrets (Production)**

For production environments:

```yaml
services:
  postgres-db:
    environment:
      - POSTGRES_PASSWORD_FILE=/run/secrets/db_password
    secrets:
      - db_password

secrets:
  db_password:
    file: ./secrets/db_password.txt
```

---

## 📋 Command Reference

### **Lifecycle Commands**

```bash
# Start all services
docker-compose up -d

# Stop all services (keep data)
docker-compose down

# Stop and remove data
docker-compose down -v

# Restart specific service
docker-compose restart healenium-backend

# Rebuild and start
docker-compose up -d --build
```

### **Inspection Commands**

```bash
# View logs
docker-compose logs -f

# Check status
docker-compose ps

# Enter container shell
docker exec -it healenium-backend /bin/sh

# View resource usage
docker stats
```

### **Cleanup Commands**

```bash
# Remove stopped containers
docker-compose down

# Remove unused images
docker image prune

# Remove all unused data
docker system prune -a

# Remove volumes (WARNING: deletes data!)
docker volume prune
```

---

## ✅ Quick Checklist

Before running tests, ensure:

- [ ] Docker Desktop is running
- [ ] All 3 services are up: `docker-compose ps`
- [ ] Backend is healthy: `curl http://localhost:7878/actuator/health`
- [ ] Web UI is accessible: http://localhost:7878/healenium/report
- [ ] No port conflicts: `lsof -i :7878`
- [ ] Enough disk space: `df -h`

---

## 🎯 Summary

**Required Configuration:**
- ✅ `docker-compose.yml` is already configured
- ✅ Helper scripts are ready to use
- ✅ Default settings work out of the box

**To Start:**
```bash
./start-healenium.sh
```

**To Verify:**
```bash
./verify-healenium.sh
```

**To Stop:**
```bash
./stop-healenium.sh
```

**That's it!** The Docker setup is complete and ready to use. 🚀

