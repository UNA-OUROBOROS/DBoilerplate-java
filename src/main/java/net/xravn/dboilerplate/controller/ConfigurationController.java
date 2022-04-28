package net.xravn.dboilerplate.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.moandjiezana.toml.Toml;

import net.xravn.dboilerplate.util.ResourceManager;

public class ConfigurationController {
    private ConfigurationController() {
        if (!configExists()) {
            System.out.println("Configuration file not found. Creating default configuration.");
            initDefaultConfig();
        }
        try {
            toml = new Toml().read(new File(getConfigFilePath()));
            System.out.println("Configuration file loaded in:" + getConfigFilePath());
        } catch (Exception ex) {
            System.err.println("Error loading configuration file: " + ex);
            toml = null;
        }
    }

    /**
     * check if server config file exists
     * 
     * @return true if exists
     */
    public static boolean configExists() {
        File file = new File(getConfigFilePath());
        return file.isFile();
    }

    public static String getConfigFilePath() {
        // try to load the enviroment variable
        String path = System.getenv("APP_CONFIG_FILE_PATH");
        return path == null ? configFile : path;
    }

    /**
     * create the default server configuration file
     */
    private void initDefaultConfig() {
        try {
            ResourceManager.ExportResource(configResourceFile, getConfigFilePath());
        } catch (Exception e) {
            System.err.println("Error creating default configuration file: " + e);
        }
    }

    public Boolean ommitDBTests() {
        return toml.getBoolean("unit-tests.ommit_db_tests");
    }

    public String getJDBCDriver() {
        return toml.getString("database.java.jdbc_driver");
    }

    public String getTestJDBCDriver() {
        return toml.getString("unit-tests.database.java.jdbc_driver");
    }

    public String getJDBCConnectionString() {
        return toml.getString("database.java.connection_string");
    }

    public String getTestJDBCConnectionString() {
        return toml.getString("unit-tests.database.java.connection_string");
    }

    public Map<String, Object> getDatabaseParameters() {
        return getFormatedMap(toml.getTable("database.parameters").toMap());
    }

    public Map<String, Object> getTestDatabaseParameters() {
        return getFormatedMap(toml.getTable("unit-tests.database.parameters").toMap());
    }

    /**
     * returns the formated map of a given templated toml with enviroment variables
     * example:
     * [database.parameters]
     * dbname = "${dbname}"
     * dbuser = "${dbuser}"
     * dbpass = "${dbpass}"
     * returns:
     * [database.parameters]
     * dbname = "dbname"
     * dbuser = "dbuser"
     * dbpass = "dbpass"
     * 
     * @param toml toml to generate the map from
     * @return the parsed map
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getFormatedMap(Map<String, Object> toml) {
        Map<String, Object> formatedMap = new HashMap<>();
        // iterate over the map recursively
        for (Map.Entry<String, Object> entry : toml.entrySet()) {
            if (entry.getValue() instanceof Map) {
                // do a checked cast to Map<String, Object>
                Map<String, Object> subMap = (Map<String, Object>) entry.getValue();
                formatedMap.put(entry.getKey(), getFormatedMap(subMap));
            } else if (entry.getValue() instanceof String) {
                // if the input is a string check if begins with ${
                // and ends with }
                if (((String) entry.getValue()).startsWith("${") && ((String) entry.getValue()).endsWith("}")) {
                    // if so, get the enviroment variable
                    String envVar = ((String) entry.getValue()).substring(2, ((String) entry.getValue()).length() - 1);
                    // and replace it with the enviroment variable value
                    formatedMap.put(entry.getKey(), System.getenv(envVar));
                } else {
                    // if not, just add the string
                    formatedMap.put(entry.getKey(), entry.getValue());
                }
            } else {
                formatedMap.put(entry.getKey(), entry.getValue());
            }
        }
        return formatedMap;
    }

    private Toml toml;
    private static String configFile = "DBoilerplate.toml";
    private static String configResourceFile = "/configuration/DBoilerplate.toml";

    private static ConfigurationController instance = null;
    private static final Object lock = new Object();

    public static ConfigurationController getInstance() {
        synchronized (lock) {
            if (instance == null) {
                instance = new ConfigurationController();
            }
        }
        return instance;
    }
}
