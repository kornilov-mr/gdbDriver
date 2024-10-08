package gdbDriver.Core;

import gdbDriver.Commands.userCommands.UserCommandQueue;
import gdbDriver.Configer.DebuggerConfig;
import gdbDriver.Output.OutputConfig;
import gdbDriver.StreamHandlers.ErrorStream.ErrorStreamWriter;
import gdbDriver.StreamHandlers.InputStream.SystemInListener;
import gdbDriver.StreamHandlers.OutputStream.OutputStreamHandler;
import gdbDriver.StreamHandlers.ThreadManager;

import java.io.*;
import java.util.ArrayList;


public class Driver {
    //Configs what user Creates
    private final DebuggerConfig debuggerConfig;
    private final OutputConfig outputConfig;

    //Object for user's input
    private final UserCommandQueue userCommandQueue = new UserCommandQueue();

    private File sourceFile;
    private File executableFile;
    private String directory;

    //Object to stop all Threads after receiving exit command
    private ThreadManager threadManager;

    public Driver(DebuggerConfig debuggerConfig, OutputConfig outputConfig) {
        this.debuggerConfig = debuggerConfig;
        this.outputConfig = outputConfig;
    }

    public void load(File sourceFile) {
        this.sourceFile = sourceFile;
        this.directory = String.valueOf(sourceFile.getParentFile());
    }

    public void run() {
        if (sourceFile == null) {
            System.out.println("source file isn't loaded");
            return;
        }
        CodeCompiler codeCompiler = new CodeCompiler("-std=c++14");
        this.executableFile = codeCompiler.compileCode(sourceFile);

        Process process = startProcess();

        OutputStream outputStream = process.getOutputStream();

        InputStream inputStream = process.getInputStream();
        InputStream errorStream = process.getErrorStream();

        //Starting thread, what is responsible for dealing with error stream
        ErrorStreamWriter errorStreamWriter = new ErrorStreamWriter(errorStream, outputConfig);
        errorStreamWriter.start();

        //Starting thread, what is responsible for reading input in System.in
        SystemInListener systemInListener = new SystemInListener(userCommandQueue);
        systemInListener.start();

        threadManager = new ThreadManager(errorStreamWriter, systemInListener, process);

        //Starting thread, what is responsible for general output from gdb and preforms all code visualization
        //and applying other output configs
        OutputStreamHandler outputStreamHandler = new OutputStreamHandler(
                new InputStreamReader(inputStream),
                outputStream,
                debuggerConfig,
                outputConfig,
                directory,
                userCommandQueue,
                threadManager);

        outputStreamHandler.start();
    }

    private Process startProcess() {
        File commandFile = debuggerConfig.createPreRunCommandFile();

        ProcessBuilder builder = new ProcessBuilder();
        builder.directory(this.executableFile.getParentFile());

        ArrayList<String> args = createGDBExecuteArgs(commandFile);
        builder.command(args);

        try {
            return builder.start();
        } catch (IOException e) {
            System.out.println("A problem occurred during starting up the gdb");
            throw new RuntimeException(e);
        }
    }

    private ArrayList<String> createGDBExecuteArgs(File CommandFile) {
        ArrayList<String> args = new ArrayList<>();
        args.add(debuggerConfig.createTerminalCommand());
        args.add(executableFile.getAbsolutePath());
        args.add("--command="+CommandFile.getAbsolutePath());
        args.add("-q");
        return args;
    }

    public boolean isAlive(){
        return threadManager.isAlive();
    }
}

