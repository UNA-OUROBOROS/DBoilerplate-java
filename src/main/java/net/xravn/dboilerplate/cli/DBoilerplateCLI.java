package net.xravn.dboilerplate.cli;

import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "dboilerplate", mixinStandardHelpOptions = true, version = "0.0.1", //
        subcommands = { TestDatabaseConnection.class, CommandLine.HelpCommand.class }, //
        description = "DBoilerplate is a tool to reduce (D)atabase (B)oilerplate code.")
public class DBoilerplateCLI implements Callable<Void> {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new DBoilerplateCLI()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Void call() {
        System.out.println("DBoilerplate is a tool to reduce (D)atabase (B)oilerplate code.");
        return null;
    }
}
