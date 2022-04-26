package net.xravn.dboilerplate.database.connection;

import java.sql.Connection;

public abstract class GenericDBConnection implements DBConnection {

    protected GenericDBConnection(String driver) throws ClassNotFoundException {
        Class.forName(driver);
    }

    protected GenericDBConnection(String driver, String connectionString) throws ClassNotFoundException {
        this(driver);
        this.connectionString = connectionString;
    }

    protected String getConnectionString() {
        return connectionString;
    }

    protected String setConnectionString(String connectionString) {
        return this.connectionString = connectionString;
    }

    @Override
    public Connection getConnection() {
        try {
            return java.sql.DriverManager.getConnection(connectionString);
        } catch (Exception e) {
            System.err.println("Could not connect to database: " + e.getMessage());
        }
        return null;
    }

    private String connectionString = null;

}
