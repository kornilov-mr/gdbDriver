package gdbDriver.Commands.userCommands;

import gdbDriver.StreamHandlers.CommandExecutor;
import gdbDriver.StreamHandlers.OutputStream.State;

public abstract class UserCommandClass implements UserCommandInterface {
    protected final String command;
    protected final CommandExecutor commandExecutor;
    protected final State state;

    protected UserCommandClass(String command, CommandExecutor commandExecutor, State state) {
        this.command = command;
        this.commandExecutor = commandExecutor;
        this.state = state;
    }


}
