package gdbDriver.StreamHandlers.ErrorStream;

import gdbDriver.Output.OutputConfig;
import gdbDriver.StreamHandlers.StreamReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class ErrorStreamWriter extends Thread {
    private final StreamReader streamReader;
    private final OutputConfig outputConfig;

    public ErrorStreamWriter(InputStream io, OutputConfig outputConfig) {
        this.streamReader = new StreamReader(new InputStreamReader(io));
        this.outputConfig = outputConfig;
    }

    public void run() {
        //Reading error input loop

        try {
            while (true) {
                Thread.sleep(100);
                String newLine = streamReader.readNextLine();
                //Ignoring error for undefined command, what we use to skip to new (gdb ) symbol
                if (Objects.equals("Undefined command: \"skipToNewCommand\".  Try \"help\".", newLine)) {
                    continue;
                }
                if (!Objects.equals(newLine, "")) {
                    outputConfig.writeError(newLine);
                }

            }
        } catch (InterruptedException e) {
        }
    }
}
