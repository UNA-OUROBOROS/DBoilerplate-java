package net.xravn.dboilerplate.database.connection;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public abstract class GenericJDBConnection implements DBConnection {

    public GenericJDBConnection(String driver) throws ClassNotFoundException {
        Class.forName(driver);
    }

    public GenericJDBConnection(String driver, String connectionString) throws ClassNotFoundException {
        this(driver);
        this.connectionString = connectionString;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public String setConnectionString(String connectionString) {
        return this.connectionString = connectionString;
    }

    /**
     * returns the formated connection string for the database
     * ex: if the connection string is:
     * jdbc:mysql://{user}:{password}@{host}:{port}/{schema}
     * the returned string will be (asuming user=user, password=password,
     * host=localhost, port=3306, schema=DBoilerplate):
     * jdbc:mysql://user:password@localhost:3306/DBoilerplate
     * and for sqlite:
     * jdbc:sqlite:{file}
     * will be:
     * jdbc:sqlite:{file}
     * 
     * @return the formated connection string
     */
    public String getFormatedConnectionString() {
        StringBuilder sb = new StringBuilder();
        // go to the entire original connection string
        for (int i = 0; i < connectionString.length(); i++) {
            // get the next {
            int startIndex = connectionString.indexOf("{", i);
            if (startIndex == -1) {
                // no more { found, append the rest of the string
                sb.append(connectionString.substring(i));
                break;
            } else {
                // append the string before the {
                sb.append(connectionString.substring(i, startIndex));
                // get the next }
                int endIndex = connectionString.indexOf("}", startIndex);
                // if not found return format exception
                if (endIndex == -1) {
                    throw new IllegalArgumentException("Invalid connection string: " + connectionString);
                }
                // get the key
                String key = connectionString.substring(startIndex + 1, endIndex);
                // get the value
                Object value = parameters.get(key);
                // if not found return IllegalArgumentException
                if (value == null) {
                    throw new IllegalArgumentException("Invalid connection string: " + connectionString);
                }
                // append the value
                sb.append(value);
                // update the index
                i = endIndex;
            }
        }

        return sb.toString();
    }

    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public void setParameters(
            Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    @Override
    public Connection getConnection() {
        try {
            return java.sql.DriverManager.getConnection(getFormatedConnectionString());
        } catch (Exception e) {
            System.err.println("Could not connect to database: " + e.getMessage());
        }
        return null;
    }

    private String connectionString = null;
    private Map<String, Object> parameters = new HashMap<>();

}
