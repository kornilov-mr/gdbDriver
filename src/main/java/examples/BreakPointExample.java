package examples;


import gdbDriver.Configer.BreakPoint;
import gdbDriver.Configer.Catcher;
import gdbDriver.Configer.DebuggerConfig;
import gdbDriver.Driver;
import gdbDriver.Output.OutputConfig;

import java.io.File;


public class BreakPointExample {
    public static void main(String[] args) {
        File sourceFile= new File("src/main/java/examples/cppFiles/BreakPointExample.cpp");

        DebuggerConfig debuggerConfig = new DebuggerConfig("gdb");

        BreakPoint testBreakPoint = new BreakPoint("BreakPointExample.cpp",6);
        testBreakPoint.addCallback((OutputConfig outputConfig) -> {
            System.out.println("Main Thread hit the user's breakpoint");
        });

        debuggerConfig.addBreakPoint(testBreakPoint);

        Driver driver = new Driver(debuggerConfig,new OutputConfig());

        driver.load(sourceFile);
        driver.run();
    }
}
