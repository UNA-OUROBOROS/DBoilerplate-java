package net.xravn.dboilerplate;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.xravn.dboilerplate.controller.ConfigurationController;
import net.xravn.dboilerplate.controller.DBConnectionController;
import net.xravn.dboilerplate.database.connection.GenericJDBConnection;

public class DatabaseConnectionTest {

    @BeforeAll
    public static void setUp() throws ClassNotFoundException {
        DBConnectionController dbConnectionController = DBConnectionController.getInstance();
        ConfigurationController configurationController = ConfigurationController.getInstance();
        dbConnectionController.setTestMode(true);
        String driver = configurationController.getTestJDBCDriver();
        String connectionString = configurationController.getTestJDBCConnectionString();
        GenericJDBConnection dbConnection;
        String driverFile = configurationController.getTestJDBCDriverFile();
        String driverPath = configurationController.getTestJDBCDriverPath();
        if (driverFile != null) {
            dbConnection = new GenericJDBConnection(
                driver,
                connectionString,
                driverFile,
                false);
        } else if (driverPath != null) {
            dbConnection = new GenericJDBConnection(
                driver,
                connectionString,
                driverPath,
                true);
        }
        else{
            dbConnection = new GenericJDBConnection(driver, connectionString);
        }
        dbConnectionController.setDBConnection(dbConnection);
    }

    @Test
    void testConnect() {
        ConfigurationController configurationManager = ConfigurationController.getInstance();
        if (configurationManager.ommitDBTests()) {
            System.err.println("Skipping database connection tests");
        } else {
            DBConnectionController dbConnectionController = DBConnectionController.getInstance();
            assertTrue(dbConnectionController.isConnected());
        }
    }
}
