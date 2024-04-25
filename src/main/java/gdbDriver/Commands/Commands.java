package gdbDriver.Commands;

import java.util.Arrays;
import java.util.Vector;

public class Commands {

    public static Vector<String> upCommands = new Vector<>(Arrays.asList(
            "up\n", "u\n", "Up\n", "U\n"
    ));
    public static Vector<String> downCommands = new Vector<>(Arrays.asList(
            "down\n", "d\n", "Down\n", "D\n"
    ));
    public static Vector<String> resetCommands = new Vector<>(Arrays.asList(
            "line reset\n", "reset\n"
    ));
    public static Vector<String> allCommands = new Vector<>();

    static {
        allCommands.addAll(upCommands);
        allCommands.addAll(downCommands);
        allCommands.addAll(resetCommands);
    }

}
