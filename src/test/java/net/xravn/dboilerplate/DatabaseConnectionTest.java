package net.xravn.dboilerplate;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.xravn.dboilerplate.controller.ConfigurationController;
import net.xravn.dboilerplate.controller.DBConnectionController;
import net.xravn.dboilerplate.sqlite.connection.SQLiteConnection;

public class DatabaseConnectionTest {

    @BeforeAll
    public static void setUp() throws ClassNotFoundException {
        DBConnectionController dbConnectionController = DBConnectionController.getInstance();
        dbConnectionController.setTestMode(true);
        dbConnectionController.setDBConnection(new SQLiteConnection());

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
