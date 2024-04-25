package gdbDriver.Output.OutputInformation;

public abstract class OutputInformation {
    private final String line;

    OutputInformation(String line) {
        this.line = line;
    }

    public String getLine() {
        return line;
    }
}
