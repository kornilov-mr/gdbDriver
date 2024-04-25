package examples;

import gdbDriver.Configer.BreakPoint;
import gdbDriver.Configer.DebuggerConfig;
import gdbDriver.Core.Driver;
import gdbDriver.Output.OutputConfig;

import java.io.File;
import java.util.Queue;

public class IntegratedCallbackExample {
    public static void main(String[] args) {
        File sourceFile = new File("src/main/java/examples/cppFiles/IntegratedCallbacksExample.cpp");

        DebuggerConfig debuggerConfig = new DebuggerConfig("gdb");

        BreakPoint breakPoint = new BreakPoint("IntegratedCallbacksExample.cpp", 11);
        breakPoint.addCallback((OutputConfig outputConfig, Queue<String> userCommandQueue) -> {
            outputConfig.writeLine("Variable was successfully changed");
            userCommandQueue.add("set variable g=1" + "\n");
            userCommandQueue.add("continue" + "\n");
        });
        debuggerConfig.addBreakPoint(breakPoint);

        Driver driver = new Driver(debuggerConfig, new OutputConfig());

        driver.load(sourceFile);
        driver.run();

    }
}
