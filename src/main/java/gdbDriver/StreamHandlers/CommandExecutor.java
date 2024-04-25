package gdbDriver.StreamHandlers;

import org.apache.commons.io.input.ReaderInputStream;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Queue;
import java.util.Vector;

import static java.lang.Thread.sleep;

public class CommandExecutor {
    private final OutputStream outputStream;
    private final InputStreamReader inputStreamReader;

    public CommandExecutor(OutputStream outputStream, InputStreamReader inputStreamReader) {
        this.outputStream = outputStream;
        this.inputStreamReader = inputStreamReader;
    }

    //For Testing
    public CommandExecutor(InputStreamReader in, PrintStream out) {
        this.outputStream = out;
        this.inputStreamReader = in;
    }

    public void skipToNextGDB() {
        try {
            sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            String newLine = readNextLine();
            if (Objects.equals(newLine, "(gdb) ")) {
                break;
            }
        }
    }

    public boolean executeUserCommand(Queue<String> UserCommandQueue) {
        if (UserCommandQueue.isEmpty()) {
            return false;
        }
        //Sending command into gdb
        PrintWriter printWriter = new PrintWriter(outputStream);
        String currCommand = UserCommandQueue.poll();
        printWriter.write(currCommand);
        printWriter.flush();
        //Checking if we hit exit to then close Threads
        return Objects.equals(currCommand, "exit\n");
    }

    public Vector<String> getLocalInfo() {
        //Sending command to gdb
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.write("info locals" + "\n");
        printWriter.flush();
        Vector<String> localInfo = new Vector<>();
        while (true) {
            //Reading output before we hit (gdb)
            String newLine = readNextLine();
            if (Objects.equals(newLine, "(gdb) ")) {
                break;
            }
            if (!Objects.equals(newLine, "")) {
                localInfo.add(newLine);
            }
        }

        printWriter.write("skipToNewCommand" + "\n");
        printWriter.flush();
        return localInfo;
    }

    public String readNextLine() {
        //Reading next line without stopping if a line isn't closed
        StringBuilder sb = new StringBuilder();
        try {
            while (true) {
                if (Objects.equals(sb.toString(), "(gdb) ")) {
                    return sb.toString();
                }
                if (!inputStreamReader.ready()) {
                    if (Objects.equals(sb.toString(), "")) {
                        return sb.toString();
                    }
                }
                int foo = inputStreamReader.read();
                char c = (char) foo;
                if (foo == 10 || foo == 13) {
                    return sb.toString();
                }
                sb.append(c);
            }
        } catch (IOException e) {
            System.out.println("A problem occurred while reading the inputStream");
            throw new RuntimeException(e);
        }
    }
}
