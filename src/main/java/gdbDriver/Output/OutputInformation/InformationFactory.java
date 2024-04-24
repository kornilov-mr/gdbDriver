package gdbDriver.Output.OutputInformation;

public class InformationFactory {
    public static OutputInformation createOutputInformation(String line, String location){
        //Checking if line is code line (only code lines contain row)
        int row = getRowIfCan(line);
        OutputInformation outputInformation = null;

        if(row!=-1){
            outputInformation = new CodeOutputInformation(line,location,row);
        }else{
            outputInformation = new GeneralOutputInformation(line);
        }
        return outputInformation;
    }
    private static int getRowIfCan(String line) {
        if (!line.contains("\t")) {
            return -1;
        }
        String presInt = line.split("\t")[0];
        if (presInt.matches("[0-9]+")) {
            return Integer.parseInt(presInt);
        } else {
            return -1;
        }
    }
}
