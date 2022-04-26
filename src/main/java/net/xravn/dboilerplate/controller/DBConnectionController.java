package net.xravn.dboilerplate.controller;

import java.sql.Connection;

import net.xravn.dboilerplate.database.connection.DBConnection;

public class DBConnectionController {
    private DBConnectionController() {
    }

    public void setDBConnection(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public DBConnection getDBConnection() {
        return dbConnection;
    }

    public Connection getConnection() {
        if (dbConnection == null) {
            return null;
        }
        try {
            ConfigurationController config = ConfigurationController.getInstance();
            String database;
            String user;
            String password;
            if (!testMode) {
                database = config.getDatabaseSchema();
                user = config.getDatabaseUser();
                password = config.getDatabasePassword();
            } else {
                database = config.getTestDatabaseSchema();
                user = config.getTestDatabaseUser();
                password = config.getTestDatabasePassword();
            }
            dbConnection.setCredentials(database, user, password);
            return dbConnection.getConnection();
        } catch (Exception ex) {
            System.err.println("Error creating database connection: " + ex);
            return null;
        }
    }

    public boolean isConnected() {
        Connection connection = getConnection();
        if (connection != null) {
            return true;
        } else {
            return false;
        }
    }

    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

    private boolean testMode = false;
    private DBConnection dbConnection = null;

    private static DBConnectionController instance = null;
    private static final Object lock = new Object();

    public static DBConnectionController getInstance() {
        synchronized (lock) {
            if (instance == null) {
                instance = new DBConnectionController();
            }
        }
        return instance;
    }
}
