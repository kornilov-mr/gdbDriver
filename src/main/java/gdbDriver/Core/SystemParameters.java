package gdbDriver.Core;

import java.util.ArrayList;

public class SystemParameters {
    private static final boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
    public static  String ExecutableFileExtension;
    static {
        if(isWindows){
            ExecutableFileExtension=".exe";
        }else{
            ExecutableFileExtension="";
        }
    }
    public static ArrayList<String> getArgs(){
        ArrayList<String> args = new ArrayList<>();
        if (isWindows) {
            //windows
            args.add("cmd.exe");
            args.add("/c");
        } else {
            //linux
            args.add("/bin/bash");
            args.add("/c");
        }
        return args;
    }
}
