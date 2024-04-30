package gdbDriver.Commands.userCommands.UserCommandImplementation;

import gdbDriver.Commands.userCommands.UserCommandClass;
import gdbDriver.StreamHandlers.CommandExecutor;
import gdbDriver.StreamHandlers.OutputStream.State;

public class LineResetCommand extends UserCommandClass {
    public LineResetCommand(String command, CommandExecutor commandExecutor, State state) {
        super(command, commandExecutor, state);
    }

    @Override
    public void execute() {
        state.rowShift=0;
        state.showLineWithUpdatedState();
    }
}
