package gdbDriver.StreamHandlers.OutputStream;

import gdbDriver.Commands.userCommands.UserCommandQueue;
import gdbDriver.Configer.DebuggerConfig;
import gdbDriver.Output.OutputConfig;
import gdbDriver.Output.OutputInformation.InformationFactory;
import gdbDriver.Output.OutputInformation.OutputInformation;
import gdbDriver.Output.OutputInformationWritter;
import gdbDriver.StreamHandlers.CommandExecutor;
import gdbDriver.StreamHandlers.ThreadManager;

import java.io.*;

public class OutputStreamHandler extends Thread {

    //Configs what user Creates
    private final DebuggerConfig debuggerConfig;
    private final OutputConfig outputConfig;

    //Object for Sending command into gdb and reading the results of them
    private final CommandExecutor commandExecutor;

    //Queue for user's commands
    protected UserCommandQueue userCommandQueue;

    //Object for Writing all line from gdb and preforming code visualization and other output configs
    private final OutputInformationWritter outputInformationWritter;

    //Object to stop all Threads after receiving exit command
    private final ThreadManager threadManager;

    //Object, which contains variable for code visualization
    private gdbDriver.StreamHandlers.OutputStream.State state;

    public OutputStreamHandler(InputStreamReader inputStreamReader, OutputStream outputStream,
                               DebuggerConfig debuggerConfig, OutputConfig outputConfig,
                               String directory,
                               UserCommandQueue userCommandQueue,
                               ThreadManager threadManager) {


        this.debuggerConfig = debuggerConfig;
        this.userCommandQueue = userCommandQueue;

        this.commandExecutor = new CommandExecutor(outputStream, inputStreamReader);

        this.outputInformationWritter = new OutputInformationWritter(outputConfig,commandExecutor,directory);

        this.outputConfig = outputConfig;

        this.threadManager=threadManager;

        this.state = new gdbDriver.StreamHandlers.OutputStream.State(threadManager, outputInformationWritter);
    }

    public void run() {
        String location = null;
        try {
            while (true) {
                Thread.sleep(10);
                String newLine = commandExecutor.readNextLine();

                //Getting location where gdb hits breakpoint or catches an exception
                String temp = tryToGetLocation(newLine);
                location = temp != null ? temp : location;

                //Creating the Object which will be shown on output,
                // can be CodeOutputInformation (for line, which contains code)
                // or GeneralOutputInformation (for all other Strings)
                OutputInformation outputInformation = InformationFactory.createOutputInformation(newLine, location);

                //Printing information
                outputInformationWritter.writeInfo(outputInformation);

                //Executing next commands in userCommandQueue
                userCommandQueue.executeNextCommand(commandExecutor, state);
            }
        } catch (InterruptedException e) {

        }
    }

    private String tryToGetLocation(String newLine) {
        //Getting location if gdb hit exception
        if(newLine.contains("exception")){
            //Executing all callbacks, which have this location
            debuggerConfig.errorCatcher.executeCallbacks(outputConfig,userCommandQueue);
            String line = commandExecutor.readNextLine();
            return getLocationFromBreakPoint(line);
        }
        //Getting location if gdb hit a breakpoint
        if (newLine.contains("Breakpoint") && newLine.contains("hit")) {
            String location = getLocationFromBreakPoint(newLine);
            //Executing all callbacks, which have this location
            ExecuteCallbacksForBreakPoint(location);
            return location;
        }else{
            return null;
        }
    }

    private String getLocationFromBreakPoint(String line) {
        String[] data = line.split(" ");
        return data[data.length - 1];
    }

    private void ExecuteCallbacksForBreakPoint(String location) {
        this.debuggerConfig.BreakPoints.get(location).executeCallbacks(outputConfig,userCommandQueue);
    }


}