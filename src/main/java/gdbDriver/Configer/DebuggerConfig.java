package gdbDriver.Configer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DebuggerConfig {
    protected String DebuggerToolPath;
    public Map<String, BreakPoint> BreakPoints = new HashMap<>();
    public ErrorCatcher errorCatcher = new ErrorCatcher();

    public void addBreakPoint(BreakPoint breakPoint) {
        BreakPoints.put(breakPoint.fileName + ":" + breakPoint.row, breakPoint);
    }

    public void setCatcher(ErrorCatcher errorCatcher) {
        this.errorCatcher = errorCatcher;
    }

    public DebuggerConfig() {
        this.DebuggerToolPath = "gdb";
    }

    public DebuggerConfig(String DebuggerToolPath) {
        this.DebuggerToolPath = DebuggerToolPath;
    }

    public String createTerminalCommand() {
        return DebuggerToolPath;
    }

    public File createPreRunCommandFile() {

        File newCommandFile = new File("PreRunCommand.txt");
        try {

            newCommandFile.createNewFile();
            FileWriter myWriter = new FileWriter(newCommandFile.getAbsoluteFile());
            for (BreakPoint breakPoint : BreakPoints.values()) {
                myWriter.write(breakPoint.createBreakCommand() + "\n");
            }
            if (errorCatcher != null) {
                myWriter.write(errorCatcher.createCatchCommand() + "\n");
            }
            myWriter.write("run");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("A problem occurred while creating pre run commands");
            throw new RuntimeException(e);
        }
        return newCommandFile;
    }
}
