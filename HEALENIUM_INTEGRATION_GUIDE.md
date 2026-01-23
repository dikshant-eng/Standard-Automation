# Healenium Integration Guide for KorecentGems

## What is Healenium?

Healenium is a self-healing test automation library that automatically fixes broken Selenium tests when web elements change. It uses machine learning algorithms to find the best matching element when the original locator fails.

### Key Benefits:
- **Automatic Locator Healing**: Fixes broken locators in runtime
- **Reduced Maintenance**: Minimizes time spent updating test scripts
- **CI/CD Stability**: Prevents test failures due to minor UI changes
- **Smart Reporting**: Shows which locators were healed with screenshots
- **IntelliJ Plugin**: Update test code with healed locators in one click

---

## Installation & Setup

### Prerequisites
- Java 21
- Maven
- Docker (for Healenium backend services)

### Step 1: Install Dependencies

Dependencies have already been added to `pom.xml`. Run:

```bash
mvn clean install
```

### Step 2: Start Healenium Backend (Optional but Recommended)

Healenium can work in two modes:
1. **With Backend** (Recommended) - Full reporting and locator management
2. **Without Backend** - Basic healing without central reporting

#### Option A: With Backend (Recommended)

Start the Healenium backend services using Docker:

```bash
docker-compose up -d
```

This will start:
- PostgreSQL database (port 5432)
- Healenium backend (port 7878)
- Selector Imitator (port 8000)

Verify services are running:
```bash
docker-compose ps
```

#### Option B: Without Backend

If you prefer not to use Docker, Healenium will work in file-based mode. Update `src/test/resources/healenium.properties`:

```properties
heal-enabled = true
recovery-tries = 1
score-cap = 0.5
# Comment out or set to empty for file-based mode
serverHost = 
serverPort = 
```

---

## Configuration

### Healenium Properties

Edit `src/test/resources/healenium.properties`:

```properties
# Enable/disable self-healing
heal-enabled = true

# How many times to retry healing
recovery-tries = 1

# Minimum similarity score (0.0 to 1.0) - lower means more lenient
score-cap = 0.5

# Backend server (if using Docker)
serverHost = localhost
serverPort = 7878

# Report path
reportPath = target/healenium

# Enable screenshots on failure
screenshotOnFail = true

# Enable healing for elements inside iframes
healingIframe = true
```

### Serenity Configuration

To enable Healenium in Serenity BDD:

1. Open `src/test/resources/serenity.conf`
2. Uncomment the Healenium configuration lines at the bottom:

```hocon
# BEFORE (default Serenity)
environment = "chrome"

# AFTER (with Healenium)
environment = "chrome"
webdriver.driver = provided
webdriver.provided.type = com.SerenityBDD.config.HealeniumDriverManager
```

---

## Usage

### Running Tests with Healenium

Once configured, run your tests normally:

```bash
# Run all tests
mvn clean verify

# Run specific test suite
mvn clean verify -Dcucumber.filter.tags="@Sanity"

# Run with different browser
mvn clean verify -Denvironment=edge
```

### How It Works

1. **Normal Execution**: Test runs normally with original locators
2. **Element Not Found**: When a locator fails (NoSuchElementException)
3. **Self-Healing**: Healenium analyzes the DOM and finds the best matching element
4. **Test Continues**: Test proceeds with the healed locator
5. **Reporting**: Healed locators are logged with screenshots

### Example Scenario

**Original Code:**
```java
public static By SUBMIT_BUTTON = By.id("submit-btn");
```

**What Happens:**
1. Developer changes HTML: `id="submit-btn"` → `id="submit-button-new"`
2. Without Healenium: Test fails with NoSuchElementException ❌
3. With Healenium: 
   - Detects failure
   - Finds `id="submit-button-new"` (best match)
   - Test continues successfully ✅
   - Logs the change in report

---

## Viewing Healenium Reports

### Web UI (With Backend)

Access the Healenium report dashboard:

```
http://localhost:7878/healenium/report
```

Features:
- View all healed elements
- See screenshots before/after
- Compare old vs new locators
- Track healing success rate

### File-Based Reports (Without Backend)

Reports are saved to: `target/healenium/`

---

## IntelliJ IDEA Plugin (Optional)

The Healenium IntelliJ plugin allows you to update test code with healed locators automatically.

### Installation:
1. Open IntelliJ IDEA
2. Go to: Settings → Plugins
3. Search for "Healenium"
4. Install and restart

### Usage:
1. Run tests and note which locators were healed
2. In IntelliJ, use the Healenium plugin panel
3. Select healed locators
4. Click "Update Code"
5. Plugin automatically updates your page objects

---

## Best Practices

### 1. Healing Score Configuration
```properties
# Strict matching (recommended for production)
score-cap = 0.7

# Lenient matching (good for exploratory testing)
score-cap = 0.4
```

### 2. Review Healed Locators
- Don't blindly accept all healed locators
- Review the Healenium report regularly
- Update your page objects with validated healed locators
- Use the IntelliJ plugin to update code

### 3. CI/CD Integration
```bash
# Start Healenium before tests
docker-compose up -d

# Run tests
mvn clean verify

# Stop Healenium after tests
docker-compose down
```

### 4. Selective Healing
You can disable healing for specific tests by toggling in properties:
```properties
heal-enabled = false
```

### 5. Monitoring
- Check healing success rate
- If too many elements are being healed, investigate root causes
- Update locator strategies if needed (prefer stable attributes like `data-testid`)

---

## Troubleshooting

### Issue: Tests Still Fail Even with Healenium

**Possible Causes:**
1. Score too strict (`score-cap` too high)
2. Element structure changed completely
3. Element no longer exists on the page

**Solution:**
- Lower `score-cap` to 0.4 or 0.3
- Review the page structure changes
- Check Healenium logs in `target/healenium/`

### Issue: Backend Services Not Starting

**Solution:**
```bash
# Check Docker is running
docker --version

# Check if ports are available
lsof -i :7878
lsof -i :5432

# Restart services
docker-compose down
docker-compose up -d

# Check logs
docker-compose logs healenium-backend
```

### Issue: Cannot Connect to Backend

**Solution:**
Check `healenium.properties`:
```properties
serverHost = localhost
serverPort = 7878
```

If using Docker on remote machine, update `serverHost` accordingly.

### Issue: Healenium Not Healing Elements

**Checklist:**
- [ ] Is `heal-enabled = true` in healenium.properties?
- [ ] Is Healenium driver manager enabled in serenity.conf?
- [ ] Did you run `mvn clean install`?
- [ ] Are there any errors in console logs?

---

## Disabling Healenium

To temporarily disable Healenium:

### Option 1: Via Properties
```properties
# healenium.properties
heal-enabled = false
```

### Option 2: Via Serenity Config
```hocon
# serenity.conf
# Comment out these lines:
# webdriver.driver = provided
# webdriver.provided.type = com.SerenityBDD.config.HealeniumDriverManager
```

### Option 3: Via Environment Variable
```bash
mvn clean verify -Dheal.enabled=false
```

---

## Advanced Configuration

### Custom Healing Strategy

You can customize the healing strategy in `HealeniumDriverManager.java`:

```java
// Default strategy
WebDriver healeniumDriver = SelfHealingDriver.create(baseDriver);

// With custom config
Config config = Config.builder()
    .healEnabled(true)
    .serverHost("localhost")
    .serverPort(7878)
    .screenshotOnFail(true)
    .build();
WebDriver healeniumDriver = SelfHealingDriver.create(baseDriver, config);
```

### Parallel Execution

Healenium supports parallel test execution:

```bash
mvn clean verify -Dparallel=classes -DthreadCount=4
```

---

## Docker Management Commands

```bash
# Start services
docker-compose up -d

# Stop services
docker-compose down

# View logs
docker-compose logs -f healenium-backend

# Restart services
docker-compose restart

# Remove all data and restart fresh
docker-compose down -v
docker-compose up -d
```

---

## Resources

- **Healenium Official Site**: https://healenium.io/
- **GitHub**: https://github.com/healenium/healenium-web
- **Documentation**: https://healenium.io/docs
- **Video Tutorial**: https://healenium.io/video

---

## Support

For issues or questions:
1. Check the troubleshooting section above
2. Review Healenium documentation: https://healenium.io/docs
3. GitHub Issues: https://github.com/healenium/healenium-web/issues

---

## Summary

✅ **Added Healenium dependencies** to pom.xml  
✅ **Created configuration files** (healenium.properties, docker-compose.yml)  
✅ **Implemented custom driver manager** (HealeniumDriverManager.java)  
✅ **Updated Serenity configuration** with Healenium integration  

**Next Steps:**
1. Run `mvn clean install` to install dependencies
2. Start Healenium backend: `docker-compose up -d`
3. Enable Healenium in `serenity.conf`
4. Run your tests and see self-healing in action! 🚀

