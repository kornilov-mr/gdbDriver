package gdbDriver.Output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public class OutputConfig {
    //True if we want to show local variables values in code
    private boolean infoLocal = true;
    //Range of line of code what are being shown
    private int adjacentRowShow = 2;
    //True if we want the runner to output in system out
    private boolean writeInSystemOut = true;
    //Stream in log file if logFile is set
    private PrintStream writeInLogFile;

    public void setLogFile(File logFile) {
        if(logFile.exists()){
            logFile.delete();
        }
        try {
            logFile.createNewFile();
        } catch (IOException e) {
            System.out.println("A problem occurred while creating log file");
            throw new RuntimeException(e);
        }
        try {
            this.writeInLogFile = new PrintStream(logFile);
        } catch (FileNotFoundException e) {
            System.out.println("A problem occurred while creating PrintStream for log File");
            throw new RuntimeException(e);
        }
    }

    public boolean isInfoLocal() {
        return infoLocal;
    }

    public int getAdjacentRowShow() {
        return adjacentRowShow;
    }


    public OutputConfig(boolean infoLocal, int adjacentRowShow) {
        this.infoLocal = infoLocal;
        this.adjacentRowShow = adjacentRowShow;
    }

    public OutputConfig(boolean infoLocal, int adjacentRowShow, boolean writeInSystemOut) {
        this.infoLocal = infoLocal;
        this.adjacentRowShow = adjacentRowShow;
        this.writeInSystemOut = writeInSystemOut;
    }
    public OutputConfig( boolean writeInSystemOut) {
        this.writeInSystemOut = writeInSystemOut;
    }

    public OutputConfig() {
    }

    public void writeLine(String line) {
        //write in system out or/and in log file
        if (writeInSystemOut) {
            System.out.println(line);
        }
        if (writeInLogFile != null) {
            writeInLogFile.println(line);
        }
    }
}
