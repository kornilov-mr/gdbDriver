package IntegratedTests.OutputWritter;

import gdbDriver.Configer.BreakPoint;
import gdbDriver.Configer.DebuggerConfig;
import gdbDriver.Output.OutputConfig;
import gdbDriver.Output.OutputInformation.CodeOutputInformation;
import gdbDriver.Output.OutputInformation.InformationFactory;
import gdbDriver.Output.OutputInformation.OutputInformation;
import gdbDriver.Output.OutputInformationWritter;
import gdbDriver.StreamHandlers.CommandExecutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Queue;
import java.util.Vector;

public class OutputWritterTest {
    private Vector<String> readLogFile(File logFile){
        Vector<String> strings= new Vector<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(logFile)));
            while(bufferedReader.ready()){
                String line = bufferedReader.readLine();
                strings.add(line);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return strings;
    }
    @Test
    public void codeVisualizationTest() {
        File logFile = new File("src/test/java/IntegratedTests/OutputWritter/codeVisualizationTestLog.txt");


        OutputConfig outputConfig = new OutputConfig(false, 2,false);
        outputConfig.setLogFile(logFile);
        CommandExecutor commandExecutor = new CommandExecutor(new InputStreamReader(System.in), System.out);
        File directory = new File("src/main/java/examples/cppFiles");


        OutputInformationWritter OIW = new OutputInformationWritter(outputConfig,
                commandExecutor,
                directory.getAbsolutePath());

        String line = "6\tfor(int i=0;i<3;i++){";
        String location = "BreakPointExample.cpp:6";
        OutputInformation outputInformation = InformationFactory.createOutputInformation(line, location);
        CodeOutputInformation COI = (CodeOutputInformation) outputInformation;

        OIW.writeInfo(COI);

        Vector<String> outputStrings = readLogFile(logFile);
        Vector<String> stringsNeeded= new Vector<>(Arrays.asList(
                " 4|",
                " 5|    int arr[3];",
                ">6|    for(int i=0;i<3;i++){",
                " 7|        cin>>arr[i];",
                " 8|    }"

        ));
        Assertions.assertEquals(outputStrings,stringsNeeded);

    }

    @Test
    public void codeVisualizationWithInfoLocalsTest() {
        File logFile = new File("src/test/java/IntegratedTests/OutputWritter/codeVisualizationWithInfoLocalsTestLog");

        String testCommands = "\n"+"i = 0\n" + "arr = {0,19}\n"+"(gdb) ";
        InputStream is = new ByteArrayInputStream(testCommands.getBytes());
        System.setIn(is);

        OutputConfig outputConfig = new OutputConfig(true, 2,false);
        outputConfig.setLogFile(logFile);
        CommandExecutor commandExecutor = new CommandExecutor(new InputStreamReader(System.in), System.out);
        File directory = new File("src/main/java/examples/cppFiles");


        OutputInformationWritter OIW = new OutputInformationWritter(outputConfig,
                commandExecutor,
                directory.getAbsolutePath());

        String line = "10\tcout<<arr[i]<<endl;";
        String location = "BreakPointExample.cpp:10";
        OutputInformation outputInformation = InformationFactory.createOutputInformation(line, location);
        CodeOutputInformation COI = (CodeOutputInformation) outputInformation;

        OIW.writeInfo(COI);

        Vector<String> outputStrings = readLogFile(logFile);
        Vector<String> stringsNeeded= new Vector<>(Arrays.asList(
                " 8 |    }                                 ---------------",
                " 9 |    for(int i=0;i<3;i++){             |i = 0|",
                ">10|        cout<<arr[i]<<endl;           |arr = {0,19}|",
                " 11|    }                                 ---------------",
                " 12|}"

        ));
        Assertions.assertEquals(outputStrings,stringsNeeded);

    }



    @Test
    public void codeVisualizationShiftTest() {
        File logFile = new File("src/test/java/IntegratedTests/OutputWritter/codeVisualizationShiftTestLog.txt");

        OutputConfig outputConfig = new OutputConfig(false, 2,false);
        outputConfig.setLogFile(logFile);
        CommandExecutor commandExecutor = new CommandExecutor(new InputStreamReader(System.in), System.out);
        File directory = new File("src/main/java/examples/cppFiles");


        OutputInformationWritter OIW = new OutputInformationWritter(outputConfig,
                commandExecutor,
                directory.getAbsolutePath());

        String line = "10\tcout<<arr[i]<<endl;";
        String location = "BreakPointExample.cpp:10";
        OutputInformation outputInformation = InformationFactory.createOutputInformation(line, location);
        CodeOutputInformation COI = (CodeOutputInformation) outputInformation;

        OIW.writeInfo(COI, -4);

        Vector<String> outputStrings = readLogFile(logFile);
        Vector<String> stringsNeeded= new Vector<>(Arrays.asList(
                " 4|",
                " 5|    int arr[3];",
                " 6|    for(int i=0;i<3;i++){",
                " 7|        cin>>arr[i];",
                " 8|    }",
                "##################################",
                ">10|        cout<<arr[i]<<endl;"

        ));
        Assertions.assertEquals(outputStrings,stringsNeeded);
    }
}
