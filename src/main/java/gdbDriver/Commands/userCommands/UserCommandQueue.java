package gdbDriver.Commands.userCommands;

import gdbDriver.StreamHandlers.CommandExecutor;
import gdbDriver.StreamHandlers.OutputStream.State;

import java.util.concurrent.ConcurrentLinkedQueue;

public class UserCommandQueue {
    private final ConcurrentLinkedQueue<UserCommandInterface> userCommands= new ConcurrentLinkedQueue<>();

    public void addCommand(String command){
        UserCommandInterface userCommand = UserCommandFactory.CreateUserCommand(command);
        userCommands.add(userCommand);
    }
    public void executeNextCommand(CommandExecutor commandExecutor,State state){
        if(!userCommands.isEmpty()){
            userCommands.poll().execute(commandExecutor,state);
        }
    }

}
