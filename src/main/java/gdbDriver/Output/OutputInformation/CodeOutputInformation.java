package gdbDriver.Output.OutputInformation;

public class CodeOutputInformation extends OutputInformation {

    private final String location;
    private final int row;
    public CodeOutputInformation(String line, String location, int row) {
        super(line);
        this.location = location;
        this.row = row;
    }

    public String getLocation() {
        return location;
    }

    public int getRow() {
        return row;
    }
}
