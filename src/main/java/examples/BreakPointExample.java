package examples;


import gdbDriver.Configer.BreakPoint;
import gdbDriver.Configer.DebuggerConfig;
import gdbDriver.Core.Driver;
import gdbDriver.Output.OutputConfig;

import java.io.File;


public class BreakPointExample {
    public static void main(String[] args) {
        File sourceFile= new File("src/main/java/examples/cppFiles/BreakPointExample.cpp");

        DebuggerConfig debuggerConfig = new DebuggerConfig("gdb");

        BreakPoint testBreakPoint = new BreakPoint("BreakPointExample.cpp",6);
        testBreakPoint.addCallback(() -> {
            System.out.println("Main Thread hit the user's breakpoint");
        });

        debuggerConfig.addBreakPoint(testBreakPoint);

        Driver driver = new Driver(debuggerConfig,new OutputConfig());

        driver.load(sourceFile);
        driver.run();
    }
}
