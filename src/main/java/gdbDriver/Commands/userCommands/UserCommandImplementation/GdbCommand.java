package gdbDriver.Commands.userCommands.UserCommandImplementation;

import gdbDriver.Commands.userCommands.UserCommandClass;
import gdbDriver.StreamHandlers.CommandExecutor;
import gdbDriver.StreamHandlers.OutputStream.State;

public class GdbCommand extends UserCommandClass {

    public GdbCommand(String command, CommandExecutor commandExecutor, State state) {
        super(command, commandExecutor, state);
    }

    @Override
    public void execute() {
        commandExecutor.executeCommand(command);
    }
}
