package gdbDriver.Commands.userCommands;

import gdbDriver.StreamHandlers.CommandExecutor;
import gdbDriver.StreamHandlers.OutputStream.State;

import java.util.concurrent.ConcurrentLinkedQueue;

public class UserCommandQueue {
    private final ConcurrentLinkedQueue<UserCommandInterface> userCommands= new ConcurrentLinkedQueue<>();
    private CommandExecutor commandExecutor;

    //Object, which contains variable for code visualization
    private State state;

    public void setCommandExecutor(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    public void setState(State state) {
        this.state = state;
    }


    public void addCommand(String command){
        UserCommandInterface userCommand = UserCommandFactory.CreateUserCommand(command,commandExecutor,state);
        userCommands.add(userCommand);
    }
    public void executeNextCommand(){
        if(!userCommands.isEmpty()){
            userCommands.poll().execute();
        }
    }

}
