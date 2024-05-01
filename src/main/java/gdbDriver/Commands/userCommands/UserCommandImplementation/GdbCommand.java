package gdbDriver.Commands.userCommands.UserCommandImplementation;

import gdbDriver.Commands.userCommands.UserCommandClass;
import gdbDriver.StreamHandlers.CommandExecutor;
import gdbDriver.StreamHandlers.OutputStream.State;

public class GdbCommand extends UserCommandClass {

    public GdbCommand(String command) {
        super(command);
    }

    @Override
    public void execute(CommandExecutor commandExecutor, State state) {
        commandExecutor.executeCommand(command);
    }
}
