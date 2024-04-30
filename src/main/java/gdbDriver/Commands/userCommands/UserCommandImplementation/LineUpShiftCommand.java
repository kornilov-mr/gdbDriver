package gdbDriver.Commands.userCommands.UserCommandImplementation;

import gdbDriver.Commands.userCommands.UserCommandClass;
import gdbDriver.StreamHandlers.CommandExecutor;
import gdbDriver.StreamHandlers.OutputStream.State;

public class LineUpShiftCommand extends UserCommandClass {
    public LineUpShiftCommand(String command, CommandExecutor commandExecutor, State state) {
        super(command, commandExecutor, state);
    }

    @Override
    public void execute() {
        state.rowShift-=1;
        state.showLineWithUpdatedState();
    }
}
