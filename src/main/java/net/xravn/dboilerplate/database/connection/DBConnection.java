package net.xravn.dboilerplate.database.connection;

import java.sql.Connection;

public interface DBConnection {
    public void setCredentials(String database, String user, String password);

    public Connection getConnection() throws Exception;
}
