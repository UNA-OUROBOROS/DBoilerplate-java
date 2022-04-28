package net.xravn.dboilerplate.cli;

import java.util.concurrent.Callable;


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

		if (testDBconnection) {
			System.out.println("Testing the database connection");
			// GenericDBConnection dbConnection = new GenericDBConnection();
		}

		return 0;
	}

}
