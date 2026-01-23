# Troubleshooting Guide

## Build & Dependency Issues

### ✅ FIXED: NoSuchMethodError with JUnit (October 2025)

**Error:**
```
java.lang.NoSuchMethodError: 'void org.junit.platform.commons.util.CollectionUtils.forEachInReverseOrder(java.util.List, java.util.function.Consumer)'
```

**Root Cause:**
- Outdated Maven Surefire plugin (2.22.1)
- JUnit version mismatch between dependencies

**Fix Applied:**
```xml
<!-- Updated in pom.xml -->
<plugin>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.2.5</version> <!-- Was 2.22.1 -->
</plugin>

<dependency>
    <groupId>org.junit.vintage</groupId>
    <artifactId>junit-vintage-engine</artifactId>
    <version>5.11.2</version> <!-- Was 5.9.2 -->
</dependency>
```

**Result:** ✅ Build now successful with aligned JUnit dependencies

---

## Healenium Issues

### Backend Won't Start

**Symptoms:**
```bash
./start-healenium.sh
# Error: Docker is not running
```

**Solutions:**

1. **Check Docker is installed:**
   ```bash
   docker --version
   ```

2. **Start Docker Desktop:**
   - Open Docker Desktop application
   - Wait for it to fully start
   - Run `./start-healenium.sh` again

3. **Check if services are running:**
   ```bash
   docker-compose ps
   ```

### Port Already in Use

**Symptoms:**
```
Error: port 7878 already allocated
```

**Solutions:**

1. **Find what's using the port:**
   ```bash
   lsof -i :7878
   lsof -i :5432
   ```

2. **Kill the process:**
   ```bash
   kill -9 <PID>
   ```

3. **Or change port in docker-compose.yml:**
   ```yaml
   healenium-backend:
     ports:
       - "7879:7878"  # Change 7878 to 7879
   ```

### Tests Not Healing

**Checklist:**

- [ ] Is `heal-enabled = true` in `healenium.properties`?
  ```bash
  cat src/test/resources/healenium.properties | grep heal-enabled
  ```

- [ ] Is Healenium enabled in `serenity.conf`?
  ```bash
  cat src/test/resources/serenity.conf | grep HealeniumDriverManager
  ```

- [ ] Did you run `mvn clean install`?
  ```bash
  mvn clean install
  ```

- [ ] Is backend running?
  ```bash
  docker-compose ps
  # Should show 3 services running
  ```

**Fix:**
```bash
# Full reset
./stop-healenium.sh
mvn clean install
./start-healenium.sh
mvn clean verify
```

### Maven Warnings

**Warning:**
```
WARNING: io.cucumber:messages/maven-metadata.xml failed to transfer
```

**Explanation:**
- This is a warning about deprecated JCenter repository
- Does NOT affect build or tests
- Can be safely ignored

**Optional Fix:** (If it bothers you)
Remove the JCenter repository from `pom.xml`:
```xml
<!-- Can be removed -->
<repository>
    <id>central</id>
    <name>bintray</name>
    <url>http://jcenter.bintray.com</url>
</repository>
```

---

## Test Execution Issues

### Tests Not Running

**Issue:** `mvn clean verify` shows "Tests run: 0"

**Possible Causes:**

1. **Wrong test pattern:**
   - Check your test class names match the patterns in `pom.xml`
   - Patterns: `*Test.java`, `Test*.java`, `*Tests.java`, `*TestCase.java`

2. **Tests in wrong directory:**
   - Tests should be in: `src/test/java/`
   - Features should be in: `src/test/resources/features/`

### Compilation Errors

**Issue:** Tests won't compile

**Solutions:**

1. **Clean and rebuild:**
   ```bash
   mvn clean compile test-compile
   ```

2. **Check Java version:**
   ```bash
   java -version  # Should be Java 21
   ```

3. **Update dependencies:**
   ```bash
   mvn clean install -U
   ```

---

## Serenity BDD Issues

### Reports Not Generated

**Issue:** No Serenity report after test run

**Solution:**
```bash
# Generate reports manually
mvn serenity:aggregate

# Check report
open target/site/serenity/index.html
```

### WebDriver Issues

**Issue:** "WebDriver not found" or browser doesn't start

**Solutions:**

1. **Check driver location:**
   ```bash
   ls src/test/resources/drivers/mac/
   # Should see chromedriver, msedgedriver
   ```

2. **Make drivers executable:**
   ```bash
   chmod +x src/test/resources/drivers/mac/*
   ```

3. **Check browser version matches driver:**
   - Chrome version should match chromedriver version
   - Update drivers if needed from:
     - Chrome: https://chromedriver.chromium.org/
     - Edge: https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/

---

## macOS-Specific Issues

### "chromedriver cannot be opened because the developer cannot be verified"

**Solution:**
```bash
# Allow chromedriver to run
xattr -d com.apple.quarantine src/test/resources/drivers/mac/chromedriver
xattr -d com.apple.quarantine src/test/resources/drivers/mac/msedgedriver

# Or via System Preferences:
# 1. System Preferences → Security & Privacy
# 2. Click "Allow Anyway" for blocked driver
```

### Permission Denied

**Issue:**
```
Permission denied: ./start-healenium.sh
```

**Solution:**
```bash
chmod +x start-healenium.sh
chmod +x stop-healenium.sh
```

---

## Docker Issues

### Docker Compose v1 vs v2

**Issue:** `docker-compose: command not found`

**Solution:**

If using Docker Compose v2:
```bash
# Use 'docker compose' (space instead of hyphen)
docker compose up -d
docker compose down
```

Or install v1 compatibility:
```bash
brew install docker-compose
```

### Database Connection Issues

**Issue:** Healenium can't connect to PostgreSQL

**Solutions:**

1. **Check PostgreSQL is running:**
   ```bash
   docker-compose logs postgres-db
   ```

2. **Restart services:**
   ```bash
   docker-compose down
   docker-compose up -d
   ```

3. **Reset database:**
   ```bash
   docker-compose down -v  # WARNING: Deletes all data
   docker-compose up -d
   ```

---

## Performance Issues

### Slow Test Execution

**Possible Solutions:**

1. **Increase timeouts in `serenity.conf`:**
   ```hocon
   webdriver {
     timeouts {
       implicit = 20000  # Increase from 15000
     }
   }
   ```

2. **Disable screenshots for faster execution:**
   ```hocon
   serenity.take.screenshots = DISABLED
   ```

3. **Run tests in parallel:**
   ```bash
   mvn clean verify -Dparallel=classes -DthreadCount=4
   ```

### Out of Memory Errors

**Solution:**

Increase Maven memory:
```bash
export MAVEN_OPTS="-Xmx2048m -XX:MaxPermSize=512m"
mvn clean verify
```

Or in `pom.xml`:
```xml
<plugin>
    <artifactId>maven-failsafe-plugin</artifactId>
    <configuration>
        <argLine>-Xmx2048m</argLine>
    </configuration>
</plugin>
```

---

## Getting Help

### Check Logs

1. **Maven logs:**
   ```bash
   mvn clean verify -X  # Full debug output
   ```

2. **Healenium logs:**
   ```bash
   docker-compose logs -f healenium-backend
   ```

3. **Surefire reports:**
   ```bash
   cat target/surefire-reports/*.txt
   ```

### Common Commands

```bash
# Clean everything
mvn clean
rm -rf target/
docker-compose down -v

# Fresh start
mvn clean install
./start-healenium.sh
mvn clean verify

# Check versions
mvn -version
java -version
docker --version
```

---

## Quick Reference

| Issue | Quick Fix |
|-------|-----------|
| Build fails | `mvn clean install` |
| Tests not healing | Check `healenium.properties` |
| Backend won't start | Start Docker Desktop |
| Port in use | `lsof -i :7878` then `kill -9 <PID>` |
| Driver issues | `chmod +x drivers/**` |
| Out of memory | `export MAVEN_OPTS="-Xmx2048m"` |

---

**For more help:**
- Healenium: https://healenium.io/docs
- Serenity BDD: https://serenity-bdd.github.io/
- Cucumber: https://cucumber.io/docs

