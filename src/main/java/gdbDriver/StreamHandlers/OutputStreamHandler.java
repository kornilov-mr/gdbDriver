package gdbDriver.StreamHandlers;

import gdbDriver.Commands.Commands;
import gdbDriver.Configer.DebuggerConfig;
import gdbDriver.Output.OutputConfig;
import gdbDriver.Output.OutputInformation.InformationFactory;
import gdbDriver.Output.OutputInformation.OutputInformation;
import gdbDriver.Output.OutputInformationWritter;

import java.io.*;
import java.util.Objects;
import java.util.Queue;

public class OutputStreamHandler extends Thread {

    //Configs what user Creates
    private final DebuggerConfig debuggerConfig;
    private final OutputConfig outputConfig;

    //Object for Sending command into gdb and reading the results of them
    private final CommandExecutor commandExecutor;

    //Queue for user's commands
    protected Queue<String> UserCommandQueue;

    //Object for Writing all line from gdb and preforming code visualization and other output configs
    private final OutputInformationWritter outputInformationWritter;

    //Object to stop all Threads after receiving exit command
    private final ThreadManager threadManager;

    //Variable for counting rows to go up or down while code visualization
    private int rowShift=0;

    public OutputStreamHandler(InputStreamReader inputStreamReader, OutputStream outputStream,
                               DebuggerConfig debuggerConfig, OutputConfig outputConfig,
                               String directory,
                               Queue<String> UserCommandQueue,
                               ThreadManager threadManager) {


        this.debuggerConfig = debuggerConfig;
        this.UserCommandQueue = UserCommandQueue;

        this.commandExecutor = new CommandExecutor(outputStream, inputStreamReader);

        this.outputInformationWritter = new OutputInformationWritter(outputConfig,commandExecutor,directory);

        this.outputConfig = outputConfig;

        this.threadManager=threadManager;
    }

    public void run() {
        String location = null;
        while (true) {

            String newLine = commandExecutor.readNextLine();

            //Getting location where gdb hits breakpoint or catches an exception
            String temp = tryToGetLocation(newLine);
            location= temp!=null ? temp : location;

            //Creating the Object which will be shown on output,
            // can be CodeOutputInformation (for line, which contains code)
            // or GeneralOutputInformation (for all other Strings)
            OutputInformation outputInformation = InformationFactory.createOutputInformation(newLine,location);

            //Printing information
            outputInformationWritter.writeInfo(outputInformation);

            //Executing next commands in UserCommandQueue
            boolean stopThread = executeCommandLoop();

            //Checking if we hit exit command to stop all Threads
            if(stopThread){
                threadManager.stopAllThreads();
                break;
            }
        }
    }
    private boolean executeCommandLoop(){
        //Searching for every command, what isn't implemented in gdb, but affects code visualization
        while(Commands.allCommands.contains(UserCommandQueue.peek())){
            String nextCommands = UserCommandQueue.poll();
            if(Commands.upCommands.contains(nextCommands)) {
                rowShift-=1;
            }else if(Commands.downCommands.contains(nextCommands)){
                rowShift+=1;
            }else if(Commands.resetCommands.contains(nextCommands)){
                rowShift=0;
            }
            outputInformationWritter.writePreviousCodeWithShift(rowShift);
        }
        //Sending next command into gdb
        return commandExecutor.executeUserCommand(UserCommandQueue);
    }

    private String tryToGetLocation(String newLine) {
        //Getting location if gdb hit exception
        if(newLine.contains("exception")){
            //Executing all callbacks, which have this location
            debuggerConfig.catcher.executeCallbacks(outputConfig,UserCommandQueue);
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
        this.debuggerConfig.BreakPoints.get(location).executeCallbacks(outputConfig,UserCommandQueue);
    }


}