package IntegratedTests.Callbacks;

import gdbDriver.Configer.BreakPoint;
import gdbDriver.Configer.Catcher;
import gdbDriver.Configer.DebuggerConfig;
import gdbDriver.Driver;
import gdbDriver.Output.OutputConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Objects;
import java.util.Queue;

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
    @Test
    public void basicBreakPointExecutionTest() throws InterruptedException {

        String testCommands= "3\n"+"3\n"+"3\n";
        InputStream is = new ByteArrayInputStream( testCommands.getBytes() );
        System.setIn(is);

        File sourceFile= new File("src/main/java/examples/cppFiles/BreakPointExample.cpp");
        File logFile= new File("src/test/java/integratedTests/BreakPointExecutionTestLog.txt");

        DebuggerConfig debuggerConfig = new DebuggerConfig("gdb");

        BreakPoint testBreakPoint = new BreakPoint("BreakPointExample.cpp",9);
        testBreakPoint.addCallback((OutputConfig outputConfig,Queue<String> userCommandQueue) -> {
            outputConfig.writeLine("BreakPoint was executed");
            userCommandQueue.add("exit"+"\n");
        });

        debuggerConfig.addBreakPoint(testBreakPoint);

        OutputConfig outputConfig = new OutputConfig(false);
        outputConfig.setLogFile(logFile);

        Driver driver = new Driver(debuggerConfig,outputConfig);

        driver.load(sourceFile);
        driver.run();
        while(true){
            Thread.sleep(100);
            if(!driver.threadManager.alive){
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
        File logFile= new File("src/test/java/integratedTests/CatcherExecutionTestLog.txt");

        DebuggerConfig debuggerConfig = new DebuggerConfig("gdb");

        Catcher catcher = new Catcher();
        catcher.addCallback((OutputConfig outputConfig,Queue<String> userCommandQueue) ->{
            outputConfig.writeLine("Cather was executed");
            userCommandQueue.add("exit"+"\n");
        });
        debuggerConfig.setCatcher(catcher);

        OutputConfig outputConfig = new OutputConfig(false);
        outputConfig.setLogFile(logFile);

        Driver driver = new Driver(debuggerConfig,outputConfig);

        driver.load(sourceFile);
        driver.run();
        while(true){
            Thread.sleep(100);
            if(!driver.threadManager.alive){
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
        File logFile= new File("src/test/java/integratedTests/IntegratedCallbackExecutionTestLog.txt");


        DebuggerConfig debuggerConfig = new DebuggerConfig("gdb");

        BreakPoint breakPoint = new BreakPoint("IntegratedCallbacksExample.cpp", 11);
        breakPoint.addCallback((OutputConfig outputConfig, Queue<String> userCommandQueue) -> {
            outputConfig.writeLine("Variable was successfully changed");
            userCommandQueue.add("set variable g=2" + "\n");
            userCommandQueue.add("continue\n");
        });
        debuggerConfig.addBreakPoint(breakPoint);

        BreakPoint breakPoint2 = new BreakPoint("IntegratedCallbacksExample.cpp", 16);
        breakPoint2.addCallback((OutputConfig outputConfig, Queue<String> userCommandQueue) -> {
            userCommandQueue.add("exit\n");
        });
        debuggerConfig.addBreakPoint(breakPoint2);


        OutputConfig outputConfig = new OutputConfig(false);
        outputConfig.setLogFile(logFile);

        Driver driver = new Driver(debuggerConfig,outputConfig);

        driver.load(sourceFile);
        driver.run();
        while(true){
            Thread.sleep(100);
            if(!driver.threadManager.alive){
                break;
            }
        }
        boolean neededStringFound= CheckIfLogFileContains(logFile, "69");
        Assertions.assertTrue(neededStringFound);
    }
}
