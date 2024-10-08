package examples;

import gdbDriver.Commands.userCommands.UserCommandQueue;
import gdbDriver.Configer.BreakPoint;
import gdbDriver.Configer.DebuggerConfig;
import gdbDriver.Core.Driver;
import gdbDriver.Output.OutputConfig;

import java.io.File;

//Example for integrated callbacks, which allow to change variable on flight or just skip to the next breakpoint by
//sending command to gdb on flight
public class IntegratedCallbackExample {
    public static void main(String[] args) {
        File sourceFile = new File("src/main/java/examples/cppFiles/IntegratedCallbacksExample.cpp");

        DebuggerConfig debuggerConfig = new DebuggerConfig("gdb");

        BreakPoint breakPoint = new BreakPoint("IntegratedCallbacksExample.cpp", 11);
        breakPoint.addCallback((OutputConfig outputConfig, UserCommandQueue userCommandQueue) -> {
            outputConfig.writeLine("Variable was successfully changed");
            userCommandQueue.addCommand("set variable g=1");
            userCommandQueue.addCommand("continue");
        });
        debuggerConfig.addBreakPoint(breakPoint);

        Driver driver = new Driver(debuggerConfig, new OutputConfig());

        driver.load(sourceFile);
        driver.run();

    }
}
