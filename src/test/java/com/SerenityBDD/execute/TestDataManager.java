package com.SerenityBDD.execute;

import net.serenitybdd.core.Serenity;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggingEventBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TestDataManager {
    private static final LoggingEventBuilder LOGGER_INFO = LoggerFactory.getLogger(TestDataManager.class).atInfo();
    
    // Thread-safe static storage that persists across scenarios
    private static final ConcurrentHashMap<String, Object> GLOBAL_TEST_DATA = new ConcurrentHashMap<>();
    
    /**
     * Sets both Serenity session variable and global test data for cross-scenario data sharing
     * @param key The key for both session variable and test data
     * @param value The value to be stored
     */
    public static void setSessionAndTestData(String key, Object value) {
        // Set in Serenity session for current scenario
        try {
            Serenity.setSessionVariable(key).to(value);
        } catch (Exception e) {
            LOGGER_INFO.log("Warning: Could not set session variable: " + e.getMessage());
        }
        
        // Set in global test data (persists across scenarios)
        GLOBAL_TEST_DATA.put(key, value);
        
        LOGGER_INFO.log("Set Session Variable and Test Data: " + key + " to value: " + value);
        LOGGER_INFO.log("Current Global Test Data Keys: " + GLOBAL_TEST_DATA.keySet());
    }
    
    /**
     * Gets data from global test data storage (persists across scenarios)
     * @param key The key to retrieve
     * @return The stored value or null if not found
     */
    public static Object getTestData(String key) {
        Object value = GLOBAL_TEST_DATA.get(key);
        LOGGER_INFO.log("Retrieving Global Test Data for key: " + key + " - Found: " + (value != null ? "Yes, value: " + value : "No"));
        LOGGER_INFO.log("Current Global Test Data Keys: " + GLOBAL_TEST_DATA.keySet());
        return value;
    }
    
    /**
     * Sets only global test data (without session variable)
     * @param key The key for test data
     * @param value The value to be stored
     */
    public static void setTestData(String key, Object value) {
        GLOBAL_TEST_DATA.put(key, value);
        LOGGER_INFO.log("Set Global Test Data: " + key + " to value: " + value);
        LOGGER_INFO.log("Current Global Test Data Keys: " + GLOBAL_TEST_DATA.keySet());
    }
    
    /**
     * Clears all global test data
     */
    public static void clearTestData() {
        GLOBAL_TEST_DATA.clear();
        LOGGER_INFO.log("Cleared all Global Test Data");
    }
    
    /**
     * Clears specific global test data
     * @param key The key to clear
     */
    public static void clearTestData(String key) {
        GLOBAL_TEST_DATA.remove(key);
        LOGGER_INFO.log("Cleared Global Test Data for key: " + key);
    }
    
    /**
     * Checks if global test data exists for a key
     * @param key The key to check
     * @return true if exists, false otherwise
     */
    public static boolean hasTestData(String key) {
        boolean exists = GLOBAL_TEST_DATA.containsKey(key);
        LOGGER_INFO.log("Checking Global Test Data for key: " + key + " - Exists: " + exists);
        return exists;
    }
    
    /**
     * Gets all global test data keys
     * @return Set of all keys
     */
    public static Set<String> getAllKeys() {
        return GLOBAL_TEST_DATA.keySet();
    }
    
    /**
     * Gets all global test data as a map (for debugging)
     * @return Copy of all global test data
     */
    public static Map<String, Object> getAllTestData() {
        return new HashMap<>(GLOBAL_TEST_DATA);
    }
}
