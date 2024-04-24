package gdbDriver.Output.OutputInformation;

public class CodeOutputInformation extends OutputInformation {

    public final String location;
    public final int row;
    public CodeOutputInformation(String line, String location, int row) {
        super(line);
        this.location = location;
        this.row = row;
    }

}
