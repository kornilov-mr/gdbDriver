package gdbDriver.StreamHandlers;

import gdbDriver.Output.OutputConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class ErrorStreamWriter extends Thread {
    private final InputStream io;
    private final OutputConfig outputConfig;

    public ErrorStreamWriter(InputStream io, OutputConfig outputConfig) {
        this.io = io;
        this.outputConfig = outputConfig;
    }

    public void run() {
        //Reading error input loop
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(io))) {
            while (true) {
                Thread.sleep(10);
                String newLine = bufferedReader.readLine();
                //Ignoring error for undefined command, what we use to skip to new (gdb ) symbol
                if (Objects.equals("Undefined command: \"skipToNewCommand\".  Try \"help\".", newLine)) {
                    continue;
                }
                if (Objects.equals(newLine, null)) {
                    break;
                }
                outputConfig.writeError(newLine);

            }
        } catch (IOException e) {
            System.out.println("A problem occurred while reading the ErrorStream");
            throw new RuntimeException(e);
        } catch (InterruptedException e) {

        }
    }
}
