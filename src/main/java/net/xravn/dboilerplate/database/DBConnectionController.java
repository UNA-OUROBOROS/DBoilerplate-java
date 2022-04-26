package net.xravn.dboilerplate.database;

import java.sql.Connection;

import net.xravn.dboilerplate.configuration.ConfigurationManager;
import net.xravn.dboilerplate.database.connection.DBConnection;

public class DBConnectionController {
    private DBConnectionController() {
    }

    public Connection getConnection() {
        try {
            ConfigurationManager config = ConfigurationManager.getInstance();
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
            DBConnection instance = DBConnection.getInstance();
            instance.setTestMode(testMode);
            return instance.getConnection(database, user, password);
        } catch (Exception ex) {
            System.err.printf("No se pudo conectar con la base de datos: %s\n", ex.getLocalizedMessage());
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

    private static DBConnectionController instance = null;

    public static DBConnectionController getInstance() {
        if (instance == null) {
            instance = new DBConnectionController();
        }
        return instance;
    }
}
