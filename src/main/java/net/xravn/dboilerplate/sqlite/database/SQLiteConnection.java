package net.xravn.dboilerplate.sqlite.database;

import java.sql.Connection;

import net.xravn.dboilerplate.database.GenericDBConnection;

public class SQLiteConnection extends GenericDBConnection {

    @Override
    public void setCredentials(String database, String user, String password) {
        this.database = database;
    }

    @Override
    public Connection getConnection() throws Exception {
        synchronized (lock) {
            if (connection.isClosed()) {
                connection = generateConnection("org.sqlite.JDBC", "jdbc:sqlite:" + database);
            }
            return connection;
        }
    }

    @Override
    public boolean isConnected() {
        return connection == null;
    }

    @Override
    public void closeConnection() {
        synchronized (lock) {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ex) {
                    System.err.printf("No se pudo cerrar la conexión con la base de datos: %s\n",
                            ex.getLocalizedMessage());
                    // close the connection anyway
                    connection = null;
                }
            }
        }
    }

    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String CONNECTION_STRING = "jdbc:sqlite:%s";
    // by default a memory database is created
    private String database = ":memory:";
    private Connection connection = null;
    private final Object lock = new Object();

}
