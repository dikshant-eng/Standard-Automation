# Healenium Quick Start Guide

## What is Healenium?
Self-healing test automation that automatically fixes broken Selenium locators when UI elements change.

---

## 5-Minute Setup

### 1. Install Dependencies
```bash
mvn clean install
```

### 2. Start Healenium Backend (Optional)
```bash
docker-compose up -d
```

### 3. Enable Healenium

Edit `src/test/resources/serenity.conf` and add at the bottom:

```hocon
# Enable Healenium
webdriver.driver = provided
webdriver.provided.type = com.SerenityBDD.config.HealeniumDriverManager
```

### 4. Run Tests
```bash
mvn clean verify
```

### 5. View Reports
- **With Backend**: http://localhost:7878/healenium/report
- **Without Backend**: Check `target/healenium/` folder

---

## Example: How It Works

### Before (Test Fails):
```html
<!-- Old HTML -->
<button id="submit-btn">Submit</button>
```
```java
// Your test
By SUBMIT_BUTTON = By.id("submit-btn");
driver.findElement(SUBMIT_BUTTON).click(); // ❌ Fails after UI change
```

### After (Healenium Fixes It):
```html
<!-- New HTML (developer changed it) -->
<button id="submit-button-new">Submit</button>
```
```java
// Same test - no changes needed!
By SUBMIT_BUTTON = By.id("submit-btn");
driver.findElement(SUBMIT_BUTTON).click(); // ✅ Healenium finds the new locator automatically!
```

**Healenium automatically:**
1. Detects the original locator failed
2. Finds the new `submit-button-new` element
3. Continues test execution
4. Logs the change in reports

---

## Configuration Options

### Strict Healing (Recommended for Production)
```properties
# src/test/resources/healenium.properties
score-cap = 0.7  # Only heal if 70%+ similar
recovery-tries = 1
```

### Lenient Healing (Good for Exploratory Testing)
```properties
score-cap = 0.4  # More forgiving
recovery-tries = 2
```

---

## Common Commands

```bash
# Run tests with Healenium
mvn clean verify

# Start backend
docker-compose up -d

# Stop backend
docker-compose down

# View backend logs
docker-compose logs -f healenium-backend

# Run specific test
mvn clean verify -Dcucumber.filter.tags="@Sanity"
```

---

## Disable Healenium

### Temporarily:
```properties
# healenium.properties
heal-enabled = false
```

### Permanently:
Comment out in `serenity.conf`:
```hocon
# webdriver.driver = provided
# webdriver.provided.type = com.SerenityBDD.config.HealeniumDriverManager
```

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| Backend won't start | Ensure Docker is running: `docker ps` |
| Tests still failing | Lower `score-cap` to 0.4 in `healenium.properties` |
| No healing happening | Verify `heal-enabled = true` |
| Port already in use | Stop conflicting services or change ports in `docker-compose.yml` |

---

## Next Steps

1. ✅ Read full guide: [HEALENIUM_INTEGRATION_GUIDE.md](./HEALENIUM_INTEGRATION_GUIDE.md)
2. ✅ Install IntelliJ plugin to update code with healed locators
3. ✅ Review healing reports after each test run
4. ✅ Update page objects with validated healed locators

---

## Resources

- **Official Site**: https://healenium.io/
- **Documentation**: https://healenium.io/docs
- **GitHub**: https://github.com/healenium/healenium-web
- **Video Tutorial**: https://healenium.io/video

---

**Ready to go! Run `mvn clean verify` and watch Healenium heal your tests automatically! 🚀**

