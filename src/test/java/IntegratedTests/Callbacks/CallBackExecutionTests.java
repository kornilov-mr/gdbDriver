package IntegratedTests.Callbacks;

import gdbDriver.Commands.userCommands.UserCommandQueue;
import gdbDriver.Configer.BreakPoint;
import gdbDriver.Configer.ErrorCatcher;
import gdbDriver.Configer.DebuggerConfig;
import gdbDriver.Core.Driver;
import gdbDriver.Output.OutputConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CallBackExecutionTests {
    private boolean CheckIfLogFileContains(File logFile,String neededString){
        boolean neededStringFound= false;

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(logFile)));
            while(bufferedReader.ready()){
                String line = bufferedReader.readLine();
                if(Objects.equals(line, neededString)){
                    neededStringFound=true;
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return neededStringFound;
    }
    private void createFileIfDoesNotExist(File sourceFile){
        if(!sourceFile.exists()){
            try {
                sourceFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Test
    public void basicBreakPointExecutionTest() throws InterruptedException {

        String testCommands= "3\n"+"3\n"+"3\n";
        InputStream is = new ByteArrayInputStream( testCommands.getBytes() );
        System.setIn(is);

        File sourceFile= new File("src/main/java/examples/cppFiles/BreakPointExample.cpp");
        createFileIfDoesNotExist(sourceFile);
        File logFile= new File("src/test/java/integratedTests/Callbacks/BreakPointExecutionTestLog.txt");

        DebuggerConfig debuggerConfig = new DebuggerConfig("gdb");

        BreakPoint testBreakPoint = new BreakPoint("BreakPointExample.cpp",9);
        testBreakPoint.addCallback((OutputConfig outputConfig, UserCommandQueue userCommandQueue) -> {
            outputConfig.writeLine("BreakPoint was executed");
            userCommandQueue.addCommand("exit");
        });

        debuggerConfig.addBreakPoint(testBreakPoint);

        OutputConfig outputConfig = new OutputConfig(false);
        outputConfig.setLogFile(logFile);

        Driver driver = new Driver(debuggerConfig,outputConfig);

        driver.load(sourceFile);
        driver.run();
        while(true){
            Thread.sleep(100);
            if(!driver.isAlive()){
                break;
            }
        }
        boolean neededStringFound= CheckIfLogFileContains(logFile, "BreakPoint was executed");
        Assertions.assertTrue(neededStringFound);
    }
    @Test
    public void CatherExecutionTest() throws InterruptedException {

        String testCommands= "3\n"+"3\n"+"3\n";
        InputStream is = new ByteArrayInputStream( testCommands.getBytes() );
        System.setIn(is);

        File sourceFile= new File("src/main/java/examples/cppFiles/CatcherExample.cpp");
        createFileIfDoesNotExist(sourceFile);
        File logFile= new File("src/test/java/integratedTests/Callbacks/CatcherExecutionTestLog.txt");

        DebuggerConfig debuggerConfig = new DebuggerConfig("gdb");

        ErrorCatcher catcher = new ErrorCatcher();
        catcher.addCallback((OutputConfig outputConfig,UserCommandQueue userCommandQueue) ->{
            outputConfig.writeLine("Cather was executed");
            userCommandQueue.addCommand("exit");
        });
        debuggerConfig.setCatcher(catcher);

        OutputConfig outputConfig = new OutputConfig(false);
        outputConfig.setLogFile(logFile);

        Driver driver = new Driver(debuggerConfig,outputConfig);

        driver.load(sourceFile);
        driver.run();
        while(true){
            Thread.sleep(100);
            if(!driver.isAlive()){
                break;
            }
        }
        boolean neededStringFound= CheckIfLogFileContains(logFile, "Cather was executed");
        Assertions.assertTrue(neededStringFound);
    }
    @Test
    public void IntegratedCallbackExecutionTest() throws InterruptedException {

        String testCommands= "3\n"+"3\n"+"69\n";
        InputStream is = new ByteArrayInputStream( testCommands.getBytes() );
        System.setIn(is);

        File sourceFile= new File("src/main/java/examples/cppFiles/IntegratedCallbacksExample.cpp");
        createFileIfDoesNotExist(sourceFile);
        File logFile= new File("src/test/java/integratedTests/Callbacks/IntegratedCallbackExecutionTestLog.txt");


        DebuggerConfig debuggerConfig = new DebuggerConfig("gdb");

        BreakPoint breakPoint = new BreakPoint("IntegratedCallbacksExample.cpp", 11);
        breakPoint.addCallback((OutputConfig outputConfig, UserCommandQueue userCommandQueue) -> {
            outputConfig.writeLine("Variable was successfully changed");
            userCommandQueue.addCommand("set variable g=2");
            userCommandQueue.addCommand("continue");
        });
        debuggerConfig.addBreakPoint(breakPoint);

        BreakPoint breakPoint2 = new BreakPoint("IntegratedCallbacksExample.cpp", 16);
        breakPoint2.addCallback((OutputConfig outputConfig, UserCommandQueue userCommandQueue) -> {
            userCommandQueue.addCommand("exit");
        });
        debuggerConfig.addBreakPoint(breakPoint2);


        OutputConfig outputConfig = new OutputConfig(true);
        outputConfig.setLogFile(logFile);

        Driver driver = new Driver(debuggerConfig,outputConfig);

        driver.load(sourceFile);
        driver.run();
        while(true){
            Thread.sleep(100);
            if(!driver.isAlive()){
                break;
            }
        }
        boolean neededStringFound= CheckIfLogFileContains(logFile, "69");
        Assertions.assertTrue(neededStringFound);
    }
}
