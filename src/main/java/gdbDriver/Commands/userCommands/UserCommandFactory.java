package gdbDriver.Commands.userCommands;

import gdbDriver.Commands.Commands;
import gdbDriver.Commands.userCommands.UserCommandImplementation.*;

public class UserCommandFactory {
    public static UserCommandInterface CreateUserCommand(String commandString){
        UserCommandInterface command = null;
        if(Commands.upCommands.contains(commandString)) {
            command = new LineUpShiftCommand(commandString);
        }else if(Commands.downCommands.contains(commandString)){
            command = new LineDownShiftCommand(commandString);
        }else if(Commands.resetCommands.contains(commandString)){
            command = new LineResetCommand(commandString);
        }else if(Commands.exitCommands.contains(commandString)) {
            command = new ExitCommand(commandString);
        }else{
            command = new GdbCommand(commandString);
        }
        return command;
    }
}
