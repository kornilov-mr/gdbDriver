package gdbDriver.Commands.userCommands;

import gdbDriver.StreamHandlers.CommandExecutor;
import gdbDriver.StreamHandlers.OutputStream.State;

public interface UserCommandInterface {
    public void execute(CommandExecutor commandExecutor, State state);
}
