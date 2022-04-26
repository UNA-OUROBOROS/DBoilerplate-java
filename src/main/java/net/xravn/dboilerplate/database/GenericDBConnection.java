package net.xravn.dboilerplate.database;

import java.sql.Connection;

public abstract class GenericDBConnection implements DBConnection {

    protected Connection generateConnection(String driver, String connectionString) throws Exception {
        try {
            Class.forName(driver);
            return java.sql.DriverManager.getConnection(connectionString);
        }
        // check for driver not found
        catch (ClassNotFoundException e) {
            System.err.println("Could not find the driver: " + driver);
        }
        return null;
    }

}
