package gdbDriver.Core;

import java.util.ArrayList;

public class SystemParameters {
    private static final boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
    public static ArrayList<String> getArgs(){
        ArrayList<String> args = new ArrayList<>();
        if (isWindows) {
            //windows
            args.add("cmd.exe");
            args.add("/c");
        } else {
            //no support for linux
            args.add("cmd.exe");
            args.add("/c");
        }
        return args;
    }
}
