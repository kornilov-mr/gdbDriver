package gdbDriver;

import gdbDriver.Configer.DebuggerConfig;
import gdbDriver.Output.OutputConfig;
import gdbDriver.StreamHandlers.ErrorStreamWritter;
import gdbDriver.StreamHandlers.SystemInListener;
import gdbDriver.StreamHandlers.OutputStreamHandler;
import gdbDriver.StreamHandlers.ThreadManager;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

import static java.lang.Thread.sleep;


public class Driver {
    //Configs what user Creates
    private final DebuggerConfig debuggerConfig;
    private final OutputConfig outputConfig;

    //Object for user's input
    private final Queue<String> UserCommandQueue = new LinkedList<>();

    private File sourceFile;
    private File executableFile;
    private String directory;

    //Object to stop all Threads after receiving exit command
    public ThreadManager threadManager;

    private static final boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
    //Args for process builder, which variates to system
    private static final String[] arg = new String[3];

    static {
        if (isWindows) {
            //windows
            arg[0] = "cmd.exe";
            arg[1] = "/c";
        } else {
            //no support for linux
            arg[0] = "cmd.exe";
            arg[1] = "/c";
        }
    }

    public Driver(DebuggerConfig debuggerConfig, OutputConfig outputConfig) {
        this.debuggerConfig = debuggerConfig;
        this.outputConfig = outputConfig;
    }

    public void load(File sourceFile) {
        this.sourceFile = sourceFile;
        this.directory = String.valueOf(sourceFile.getParentFile());
    }

    public void run() {

        compileCode();

        Process process = startProcess();

        OutputStream outputStream = process.getOutputStream();

        InputStream inputStream = process.getInputStream();
        InputStream errorStream = process.getErrorStream();

        //Starting thread, what is responsible for dealing with error stream
        ErrorStreamWritter errorStreamWritter = new ErrorStreamWritter(errorStream);
        errorStreamWritter.start();

        //Starting thread, what is responsible for reading input in System.in
        SystemInListener systemInListener = new SystemInListener(UserCommandQueue);
        systemInListener.start();

        threadManager = new ThreadManager(errorStreamWritter, systemInListener, process);

        //Starting thread, what is responsible for general output from gdb and preforms all code visualization
        //and applying other output configs
        OutputStreamHandler outputStreamHandler = new OutputStreamHandler(
                new InputStreamReader(inputStream),
                outputStream,
                debuggerConfig,
                outputConfig,
                directory,
                UserCommandQueue,
                threadManager);

        outputStreamHandler.start();
    }

    private void compileCode() {
        ProcessBuilder builder = new ProcessBuilder();
        builder.directory(sourceFile.getParentFile());

        arg[2] = createCompileExecuteString();
        builder.command(arg);
        try {
            builder.start();
        } catch (IOException e) {
            System.out.println("A problem occurred during code compilation");
            throw new RuntimeException(e);
        }
    }

    private Process startProcess() {
        File commandFile = debuggerConfig.createPreRunCommandFile();


        String command = createGDBExecuteString(commandFile);

        ProcessBuilder builder = new ProcessBuilder();
        builder.directory(this.executableFile.getParentFile());
        arg[2] = command;
        builder.command(arg);

        try {
            return builder.start();
        } catch (IOException e) {
            System.out.println("A problem occurred during starting up the gdb \n" +
                    "with command:" + command);
            throw new RuntimeException(e);
        }
    }

    private String createGDBExecuteString(File CommandFile) {
        return debuggerConfig.createTerminalCommand() + " " + executableFile.getAbsolutePath() + " --command=" + CommandFile.getAbsolutePath() + " -q";
    }

    private String createCompileExecuteString() {
        String[] temp = sourceFile.getName().split("\\.");
        String extension = "." + temp[temp.length - 1];
        this.executableFile = new File(sourceFile.getAbsolutePath().replace(extension, ".exe"));
        return "g++ -g -std=c++14 " + sourceFile.getName() + " -o " + sourceFile.getName().replace(extension, "");

    }

}

