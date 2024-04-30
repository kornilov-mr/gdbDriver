package gdbDriver.Commands.userCommands;

import gdbDriver.Commands.Commands;
import gdbDriver.Commands.userCommands.UserCommandImplementation.*;
import gdbDriver.StreamHandlers.CommandExecutor;
import gdbDriver.StreamHandlers.OutputStream.State;

public class UserCommandFactory {
    public static UserCommandInterface CreateUserCommand(String commandString, CommandExecutor commandExecutor, State state){
        UserCommandInterface command = null;
        if(Commands.upCommands.contains(commandString)) {
            command = new LineUpShiftCommand(commandString,commandExecutor,state);
        }else if(Commands.downCommands.contains(commandString)){
            command = new LineDownShiftCommand(commandString,commandExecutor,state);
        }else if(Commands.resetCommands.contains(commandString)){
            command = new LineResetCommand(commandString,commandExecutor,state);
        }else if(Commands.exitCommands.contains(commandString)) {
            command = new ExitCommand(commandString,commandExecutor,state);
        }else{
            command = new GdbCommand(commandString,commandExecutor,state);
        }
        return command;
    }
}
