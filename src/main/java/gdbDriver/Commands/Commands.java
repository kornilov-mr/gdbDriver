package gdbDriver.Commands;

import java.util.Arrays;
import java.util.Vector;

public class Commands {

    public static Vector<String> upCommands = new Vector<>(Arrays.asList(
            "up", "u", "Up", "U"
    ));
    public static Vector<String> downCommands = new Vector<>(Arrays.asList(
            "down", "d", "Down", "D"
    ));
    public static Vector<String> resetCommands = new Vector<>(Arrays.asList(
            "line reset", "reset"
    ));
    public static Vector<String> exitCommands = new Vector<>(Arrays.asList(
       "exit","Exit","ex","Ex"
    ));
    public static Vector<String> allUserCommands = new Vector<>();

    static {
        allUserCommands.addAll(upCommands);
        allUserCommands.addAll(downCommands);
        allUserCommands.addAll(resetCommands);
    }

}
