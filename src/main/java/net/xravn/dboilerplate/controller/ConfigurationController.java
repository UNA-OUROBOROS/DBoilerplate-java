package net.xravn.dboilerplate.controller;

import java.io.File;

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

    public Integer getWebServerPort() {
        return toml.getLong("web_server.port").intValue();
    }

    public String getDatabaseHost() {
        return toml.getString("database.host");
    }

    public Integer getDatabasePort() {
        return toml.getLong("database.port").intValue();
    }

    public String getDatabaseSchema() {
        return toml.getString("database.schema");
    }

    public String getDatabaseUser() {
        return toml.getString("database.credentials.user");
    }

    public String getDatabasePassword() {
        return toml.getString("database.credentials.password");
    }

    public String getTestDatabaseHost() {
        return toml.getString("database.host");
    }

    public Integer getTestDatabasePort() {
        return toml.getLong("database.port").intValue();
    }

    public String getTestDatabaseSchema() {
        return toml.getString("database.schema");
    }

    public String getTestDatabaseUser() {
        return toml.getString("unit-tests.database.credentials.user");
    }

    public String getTestDatabasePassword() {
        return toml.getString("unit-tests.database.credentials.password");
    }

    public Boolean ommitDBTests() {
        return toml.getBoolean("unit-tests.ommit_db_tests");
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
