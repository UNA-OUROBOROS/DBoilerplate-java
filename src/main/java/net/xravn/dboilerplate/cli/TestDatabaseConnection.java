package net.xravn.dboilerplate.cli;

import java.util.concurrent.Callable;

import net.xravn.dboilerplate.controller.ConfigurationController;
import net.xravn.dboilerplate.controller.DBConnectionController;
import net.xravn.dboilerplate.database.connection.GenericJDBConnection;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "TestDatabaseConnection", mixinStandardHelpOptions = true, version = "0.0.1", //
		description = "Test the database connection")
public class TestDatabaseConnection implements Callable<Integer> {

	@Option(names = { "--unit-test-db", "-udb" }, description = "Test the unit-test database connection")
	private boolean testUnitDBconnection;

	@Option(names = { "--main-db", "-db" }, description = "Test the database connection")
	private boolean testDBconnection;

	@Override
	public Integer call() {

		DBConnectionController dbConnectionController = DBConnectionController.getInstance();
		ConfigurationController configurationController = ConfigurationController.getInstance();
		if (testDBconnection) {
			System.out.println("Testing the database connection");
			dbConnectionController.setTestMode(false);
			String driver = configurationController.getJDBCDriver();
			String connectionString = configurationController.getJDBCConnectionString();
			try {
				GenericJDBConnection dbConnection = new GenericJDBConnection(driver, connectionString);
				dbConnectionController.setDBConnection(dbConnection);
				if (dbConnectionController.isConnected()) {
					System.out.println("Database connection successful");
				} else {
					System.err.println("Database connection failed");
					return 1;
				}
			} catch (ClassNotFoundException e) {
				System.err.println("Could not load the driver: " + driver);
				return 1;
			}
		}
		if (testUnitDBconnection) {
			System.out.println("Testing the unit-test database connection");
			dbConnectionController.setTestMode(true);
			String driver = configurationController.getTestJDBCDriver();
			String connectionString = configurationController.getTestJDBCConnectionString();
			try {
				GenericJDBConnection dbConnection = new GenericJDBConnection(driver, connectionString);
				dbConnectionController.setDBConnection(dbConnection);
				if (dbConnectionController.isConnected()) {
					System.out.println("Unit-test database connection successful");
				} else {
					System.err.println("Unit-test database connection failed");
					return 1;
				}
			} catch (ClassNotFoundException e) {
				System.err.println("Could not load the driver: " + driver);
				return 1;
			}
		}

		return 0;
	}

}
