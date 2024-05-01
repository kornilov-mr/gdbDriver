package gdbDriver.Commands.userCommands.UserCommandImplementation;

import gdbDriver.Commands.userCommands.UserCommandClass;
import gdbDriver.StreamHandlers.CommandExecutor;
import gdbDriver.StreamHandlers.OutputStream.State;

public class LineResetCommand extends UserCommandClass {
    public LineResetCommand(String command) {
        super(command);
    }

    @Override
    public void execute(CommandExecutor commandExecutor, State state) {
        state.rowShift=0;
        state.showLineWithUpdatedState();
    }
}
