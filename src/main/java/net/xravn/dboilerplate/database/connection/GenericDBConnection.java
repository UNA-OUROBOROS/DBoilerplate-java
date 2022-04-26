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
        synchronized (lock) {
            if (!isConnected()) {
                try {
                    connection = java.sql.DriverManager.getConnection(connectionString);
                } catch (Exception e) {
                    System.err.println("Could not connect to database: " + e.getMessage());
                    return null;
                }
            }
            return connection;
        }
    }

    @Override
    public boolean isConnected() {
        synchronized (lock) {
            try {
                return connection == null ? false : !connection.isClosed();
            } catch (Exception e) {
                return false;
            }
        }
    }

    @Override
    public void closeConnection() {
        synchronized (lock) {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ex) {
                    System.err.printf("Could not close SQLite connection: %s\n",
                            ex.getLocalizedMessage());
                }
                connection = null;
            }
        }
    }

    private Connection connection = null;
    private String connectionString = null;
    private final Object lock = new Object();

}
