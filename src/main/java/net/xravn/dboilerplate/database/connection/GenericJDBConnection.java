package net.xravn.dboilerplate.database.connection;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.xravn.dboilerplate.database.runtime.DriverLoader;

public class GenericJDBConnection implements DBConnection {
    

    public GenericJDBConnection(String driver) throws ClassNotFoundException {
        this(driver, "");
    }

    public GenericJDBConnection(String driver, String connectionString) throws ClassNotFoundException {
        this(driver, connectionString, null, false);
    }

    public GenericJDBConnection(String driver, String connectionString, String driverPath, boolean isPath)
            throws ClassNotFoundException {
        this.connectionString = connectionString;
        if (driverPath != null) {
            // load the driver at runtime
            try {
                Class<?> driverClass = DriverLoader.load(driver, driverPath, isPath);
                Driver driverInstance = (Driver) driverClass.getDeclaredConstructor().newInstance();
                DriverManager.registerDriver(driverInstance);
                System.out.println("Driver loaded: " + driverClass.getName());
                this.driver = driverInstance;
            } catch (Exception e) {
                throw new RuntimeException("Failed to load driver: " + driver, e);
            }
        } else {
            Class.forName(driver);
        }

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
                    throw new IllegalArgumentException("Invalid connection string[parameter {"+ key + "} not found]: " + connectionString);
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
            if(driver == null){
                return DriverManager.getConnection(getFormatedConnectionString(), "", "");
            }
            else{
                return driver.connect(getFormatedConnectionString(), new Properties());
            }
        } catch (Exception e) {
            System.err.println("Could not connect to database: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("Cause: " + e.getCause().getMessage());
            }
        }
        return null;
    }

    private String connectionString = null;
    private Map<String, Object> parameters = new HashMap<>();
    private Driver driver;

}
