package net.xravn.dboilerplate.database;

import java.sql.Connection;

public interface DBConnection {
    public void setCredentials(String database, String user, String password);

    public Connection getConnection() throws Exception;

    public void closeConnection();

    public boolean isConnected();

}
