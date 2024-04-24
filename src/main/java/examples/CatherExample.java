package examples;

import gdbDriver.Configer.BreakPoint;
import gdbDriver.Configer.Catcher;
import gdbDriver.Configer.DebuggerConfig;
import gdbDriver.Driver;
import gdbDriver.Output.OutputConfig;

import java.io.File;

public class CatherExample {

    public static void main(String[] args) {
        File sourceFile= new File("src/main/java/examples/cppFiles/CatcherExample.cpp");

        DebuggerConfig debuggerConfig = new DebuggerConfig("gdb");

        Catcher catcher = new Catcher();
        debuggerConfig.setCatcher(catcher);

        Driver driver = new Driver(debuggerConfig,new OutputConfig());

        driver.load(sourceFile);
        driver.run();
    }
}
