package net.xravn.dboilerplate.sqlite.connection;

import net.xravn.dboilerplate.database.connection.GenericDBConnection;

public class SQLiteConnection extends GenericDBConnection {

    public SQLiteConnection() throws ClassNotFoundException {
        super(DRIVER);
    }

    private String generateConnectionString() {
        return String.format(CONNECTION_STRING, database);
    }

    @Override
    public void setCredentials(String database, String user, String password) {
        this.database = database;
        super.setConnectionString(generateConnectionString());
    }

    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String CONNECTION_STRING = "jdbc:sqlite:%s";
    // by default a memory database is created
    private String database = ":memory:";

}
