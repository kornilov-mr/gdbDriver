package gdbDriver.StreamHandlers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class StreamReader {

    private final InputStreamReader inputStreamReader;


    public StreamReader(InputStreamReader inputStreamReader) {
        this.inputStreamReader = inputStreamReader;
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
