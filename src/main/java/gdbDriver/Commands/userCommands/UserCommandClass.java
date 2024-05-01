package gdbDriver.Commands.userCommands;

import gdbDriver.StreamHandlers.CommandExecutor;

public abstract class UserCommandClass implements UserCommandInterface {
    protected final String command;

    protected UserCommandClass(String command) {
        this.command = command;
    }

    public void execute(CommandExecutor commandExecutor){
        commandExecutor.executeCommand(command+"\n");
    }
}
