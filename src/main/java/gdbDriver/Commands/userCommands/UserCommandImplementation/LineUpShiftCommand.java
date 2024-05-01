package gdbDriver.Commands.userCommands.UserCommandImplementation;

import gdbDriver.Commands.userCommands.UserCommandClass;
import gdbDriver.StreamHandlers.CommandExecutor;
import gdbDriver.StreamHandlers.OutputStream.State;

public class LineUpShiftCommand extends UserCommandClass {
    public LineUpShiftCommand(String command) {
        super(command);
    }

    @Override
    public void execute(CommandExecutor commandExecutor, State state) {
        state.rowShift-=1;
        state.showLineWithUpdatedState();
    }
}
