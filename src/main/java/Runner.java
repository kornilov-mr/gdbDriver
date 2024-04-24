import gdbDriver.Configer.BreakPoint;
import gdbDriver.Configer.Catcher;
import gdbDriver.Configer.DebuggerConfig;
import gdbDriver.Output.OutputConfig;
import gdbDriver.Driver;

import java.io.*;
import java.util.Queue;

public class Runner {
    public static void main(String[] args){

        File SourceFile= new File("C://test/main.cpp");
        File outputFile= new File("C://test/log.txt");

        DebuggerConfig debuggerConfig = new DebuggerConfig("gdb");


        BreakPoint testBreakPoint = new BreakPoint("main.cpp",6);

        testBreakPoint.addCallback((OutputConfig outputConfig, Queue<String> userCommandQueue) ->{
            userCommandQueue.add("info locals"+"\n");
        });

        testBreakPoint.addCallback((OutputConfig outputConfig) ->{
            outputConfig.writeLine("test");
        });

        debuggerConfig.addBreakPoint(testBreakPoint);

        Catcher catcher = new Catcher();

        catcher.addCallback((OutputConfig outputConfig, Queue<String> userCommandQueue) ->{
            outputConfig.writeLine("Catch");
            userCommandQueue.add("info locals"+"\n");
        });

        debuggerConfig.setCatcher(catcher);

        OutputConfig outputConfig = new OutputConfig();

        outputConfig.setLogFile(outputFile);

        Driver driver = new Driver(debuggerConfig,outputConfig);

        driver.load(SourceFile);
        driver.run();

    }
}
