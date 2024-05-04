package examples;

import gdbDriver.Configer.DebuggerConfig;
import gdbDriver.Configer.ErrorCatcher;
import gdbDriver.Core.Driver;
import gdbDriver.Output.OutputConfig;

import java.io.File;

// Simple Cather example to check basic Cather functionality

public class CatherExample {

    public static void main(String[] args) {
        File sourceFile= new File("src/main/java/examples/cppFiles/CatcherExample.cpp");

        DebuggerConfig debuggerConfig = new DebuggerConfig("gdb");

        ErrorCatcher catcher = new ErrorCatcher();
        debuggerConfig.setCatcher(catcher);

        Driver driver = new Driver(debuggerConfig,new OutputConfig());

        driver.load(sourceFile);
        driver.run();
    }
}
