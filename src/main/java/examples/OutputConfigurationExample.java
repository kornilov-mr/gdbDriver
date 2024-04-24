package examples;

import gdbDriver.Configer.BreakPoint;
import gdbDriver.Configer.DebuggerConfig;
import gdbDriver.Driver;
import gdbDriver.Output.OutputConfig;

import java.io.File;

public class OutputConfigurationExample {
    public static void main(String[] args) {
        File sourceFile= new File("src/main/java/examples/cppFiles/OutputConfigurationExample.cpp");

        DebuggerConfig debuggerConfig = new DebuggerConfig("gdb");

        BreakPoint testBreakPoint = new BreakPoint("OutputConfigurationExample.cpp",7);
        testBreakPoint.addCallback((OutputConfig outputConfig) -> {
            outputConfig.writeLine("Main Thread hit the user's breakpoint");
        });

        debuggerConfig.addBreakPoint(testBreakPoint);

        OutputConfig outputConfig = new OutputConfig(true,3,true);
        Driver driver = new Driver(debuggerConfig,outputConfig);

        driver.load(sourceFile);
        driver.run();
    }
}
