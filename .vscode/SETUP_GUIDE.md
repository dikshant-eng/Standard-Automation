# Cursor IDE Setup for Cucumber/Gherkin

## Required Extensions

To enable Cucumber step definition recognition in Cursor IDE, you need to install the following extension:

### 1. Install Cucumber (Gherkin) Full Support
- Extension ID: `alexkrechik.cucumberautocomplete`
- Open Command Palette (Cmd+Shift+P on Mac, Ctrl+Shift+P on Windows/Linux)
- Type: `Extensions: Install Extensions`
- Search for: **"Cucumber (Gherkin) Full Support"**
- Click Install

## Configuration Applied

The following configuration has been added to `.vscode/settings.json`:

- **Step Definitions Path**: Points to `src/test/java/com/SerenityBDD/steps/**/*.java`
- **Feature Files Path**: Points to `src/test/resources/features/**/*.feature`
- **Auto-completion**: Enabled for Gherkin keywords and step definitions

## After Installation

1. **Reload Cursor IDE**: 
   - Command Palette → "Developer: Reload Window" (or just restart Cursor)

2. **Verify Setup**:
   - Open any `.feature` file
   - Hover over a step (e.g., "Given I launch the browser and open the login page")
   - You should see a tooltip or be able to Cmd+Click to navigate to the step definition

3. **If Steps Still Not Recognized**:
   - Open Command Palette (Cmd+Shift+P / Ctrl+Shift+P)
   - Type: `Cucumber: Refresh autocomplete`
   - Or reload the window

## Alternative Extensions (Optional)

If the above doesn't work, you can also try:
- **Cucumber** by Cucumber (Extension ID: `cucumberopen.cucumber-official`)
- This is the official Cucumber extension but may require additional configuration

## Troubleshooting

### Issue: Steps still showing as undefined
**Solution**: 
1. Ensure Java extensions are installed (vscode-java-pack)
2. Wait for Java Language Server to fully index the project
3. Check the Output panel → "Cucumber" for any errors
4. Verify that Maven/Gradle has successfully built the project

### Issue: Navigation to step definitions not working
**Solution**:
1. Check that the glue path in settings.json matches your actual step definitions location
2. Make sure step definition files are using correct Cucumber annotations (@Given, @When, @Then)
3. Rebuild the project

## Running Tests

To run tests from Cursor IDE:
1. Open the test file: `src/test/java/com/SerenityBDD/testsuites/SanitySuiteTest.java`
2. Click the "Run" or "Debug" button that appears above the class
3. Or use Command Palette → "Java: Run Tests"

## Notes

- IntelliJ IDEA has better native support for Cucumber, which is why it works better there
- Cursor IDE (based on VS Code) requires extensions for Cucumber support
- The configuration is project-specific and stored in `.vscode/settings.json`


