package net.xravn.dboilerplate.database.connection;

import java.sql.Connection;
import java.util.Map;

public interface DBConnection {
    public void setParameters(Map<String, Object> parameters);

    public Map<String, Object> getParameters();

    public Connection getConnection() throws Exception;
}
