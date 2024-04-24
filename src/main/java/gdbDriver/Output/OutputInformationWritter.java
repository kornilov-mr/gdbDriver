package gdbDriver.Output;

import gdbDriver.Output.OutputInformation.CodeOutputInformation;
import gdbDriver.Output.OutputInformation.GeneralOutputInformation;
import gdbDriver.Output.OutputInformation.OutputInformation;
import gdbDriver.StreamHandlers.CommandExecutor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Vector;

public class OutputInformationWritter {
    private final OutputConfig outputConfig;
    private final CommandExecutor commandExecutor;
    private final String directory;
    private CodeOutputInformation codeOutputInformation=null;

    public OutputInformationWritter(OutputConfig outputConfig, CommandExecutor commandExecutor, String directory) {
        this.outputConfig = outputConfig;
        this.commandExecutor = commandExecutor;
        this.directory = directory;
    }
    public void writePreviousCodeWithShift(int rowShift){
        if(codeOutputInformation!=null){
            write(codeOutputInformation,rowShift,true);
        }
    }
    //For testing
    public void writeInfo(OutputInformation outputInformation, int rowShift){
        //Casts outputInformation into fitting class
        if(outputInformation instanceof CodeOutputInformation){
            write((CodeOutputInformation) outputInformation, rowShift,false);
        }else if(outputInformation instanceof  GeneralOutputInformation){
            write((GeneralOutputInformation) outputInformation);
        }
    }
    public void writeInfo(OutputInformation outputInformation){
        //Casts outputInformation into fitting class
        if(outputInformation instanceof CodeOutputInformation){
            write((CodeOutputInformation) outputInformation, 0, false);
        }else if(outputInformation instanceof  GeneralOutputInformation){
            write((GeneralOutputInformation) outputInformation);
        }
    }
    private void write(GeneralOutputInformation generalOutputInformation){
        if(!Objects.equals(generalOutputInformation.line,"")){
            outputConfig.writeLine(generalOutputInformation.line);
        }
    }
    private void write(CodeOutputInformation codeOutputInformation, int rowShift, boolean skipGDBSymbol) {
        //Saving last code line for further shift if there is a need for that
        this.codeOutputInformation=codeOutputInformation;
        //Reading file from location to get Adjacent lines
        Vector<StringBuilder> codeLines = getAdjacentLine(codeOutputInformation.location,
                codeOutputInformation.row,
                rowShift);
        //Adding local variables to output
        if (outputConfig.isInfoLocal()) {
            //going to new sdb command so "(gdb )" won't be shown in main output
            if(!skipGDBSymbol){
                commandExecutor.skipToNextGDB();
            }
            //Getting values of local variable form gdb
            Vector<String> infoLocals = commandExecutor.getLocalInfo();
            //Joining local variable to the left of code lines
            codeLines = appendInfoLocal(codeLines, infoLocals);
        }
        writeCodeLines(codeLines);
    }

    private void writeCodeLines(Vector<StringBuilder> codeLines) {
        for (StringBuilder sb : codeLines) {
            outputConfig.writeLine(sb.toString());
        }
    }

    private Vector<StringBuilder> getAdjacentLine(String location, int row, int rowShift) {

        //Getting file name
        Vector<StringBuilder> strings = new Vector<>();
        String[] data = location.split(":");
        String fileName = data[0];

        Path pathToFile = Paths.get(directory);
        pathToFile = pathToFile.resolve(fileName);


        int start_row = Math.max(row - outputConfig.getAdjacentRowShow()+rowShift, 1);
        int end_row = row + outputConfig.getAdjacentRowShow() + 1+rowShift;

        //String of code line, we are current at
        String currentLineString = null;

        //Calculation width of biggest number to create the same padding for every line
        int maxDigitCount = (int) Math.log10(end_row) + 1;

        //Reading strings
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(pathToFile.toFile().getAbsolutePath()))) {
            //Reading previous strings
            for (int i = 1; i < start_row; i++) {
                String line = bufferedReader.readLine();
                if(i==row){
                    currentLineString=line;
                }
            }

            //Reading lines between start and end_row
            for (int i = start_row; i < end_row; i++) {
                String line = bufferedReader.readLine();
                if (Objects.equals(line, null)) {
                    break;
                }
                //Creating line count padding
                StringBuilder sb = getLineCounts(i,row,maxDigitCount);
                sb.append(line);
                strings.add(sb);
            }

            //Reading rest line until we hit current line we are at
            for(int i=end_row;i<=row;i++){
                String line = bufferedReader.readLine();
                if(i==row){
                    currentLineString=line;
                }
            }

        } catch (IOException e) {
            System.out.println("A problem occurred while reading source File");
            throw new RuntimeException(e);
        }
        //If line we are at isn't in range from start_row to end_row
        if(currentLineString==null){
            return strings;
        }

        //Creating line count padding
        StringBuilder sbCode = getLineCounts(row,row,maxDigitCount);
        sbCode.append(currentLineString);
        StringBuilder sbDelimiterLine = new StringBuilder();
        //Creating delimiter to separate current line from other lines
        sbDelimiterLine.append("##################################");


        if(start_row>row){
            //add to the start if current line is above shown lines
            strings.insertElementAt(sbDelimiterLine,0);
            strings.insertElementAt(sbCode,0);
        }
        if(end_row<=row){
            //add to the end if current line is below shown lines
            strings.add(sbDelimiterLine);
            strings.add(sbCode);
        }
        return strings;

    }
    private StringBuilder getLineCounts(int i, int row, int maxDigitCount){
        StringBuilder stringBuilder = new StringBuilder();
        if (i == row) {
            stringBuilder.append(">");

        } else {
            stringBuilder.append(" ");
        }
        stringBuilder.append(i);
        appendStringBuilderToCertainLength(stringBuilder, maxDigitCount, ' ');
        stringBuilder.append("|");
        return  stringBuilder;
    }
    private Vector<StringBuilder> appendInfoLocal(Vector<StringBuilder> codeLines, Vector<String> infoLocals) {
        //Getting max width of code lines to apply padding
        int maxLineLength = 0;
        for (StringBuilder sb : codeLines) {
            maxLineLength = Math.max(maxLineLength, sb.length());
        }

        //Wrapping values of locals variables into box
        Vector<String> infolocalsWrapped = wrapInfolocals(infoLocals);

        //Adding values of locals variable to the right from code lines
        for (int i = 0; i < infolocalsWrapped.size(); i++) {
            if (codeLines.size() <= i) {
                codeLines.add(new StringBuilder());
            }
            StringBuilder sb = codeLines.get(i);
            appendStringBuilderToCertainLength(sb, maxLineLength + 10, ' ');
            sb.append(infolocalsWrapped.get(i));
        }
        return codeLines;
    }

    private Vector<String> wrapInfolocals(Vector<String> infoLocals) {
        Vector<String> infolocalsWrapped = new Vector<>();

        //Getting max width of variable lines to properly wrap them
        int maxLineLength = 0;
        for (String line : infoLocals) {
            maxLineLength = Math.max(maxLineLength, line.length());
        }

        //Adding box line from above
        StringBuilder stringBuilder = new StringBuilder();
        appendStringBuilderToCertainLength(stringBuilder, maxLineLength + 2, '-');
        infolocalsWrapped.add(stringBuilder.toString());

        for (String line : infoLocals) {
            stringBuilder = new StringBuilder();
            stringBuilder.append('|');
            stringBuilder.append(line);
            stringBuilder.append('|');
            infolocalsWrapped.add(stringBuilder.toString());
        }

        //Adding box line from beyond
        stringBuilder = new StringBuilder();
        appendStringBuilderToCertainLength(stringBuilder, maxLineLength + 2, '-');
        infolocalsWrapped.add(stringBuilder.toString());

        return infolocalsWrapped;
    }

    private static void appendStringBuilderToCertainLength(StringBuilder stringBuilder, int length, char toAppend) {
        while (stringBuilder.length() <= length) {
            stringBuilder.append(toAppend);
        }
    }

}
