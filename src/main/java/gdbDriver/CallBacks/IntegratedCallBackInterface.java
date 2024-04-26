package gdbDriver.CallBacks;

import gdbDriver.Commands.userCommands.UserCommandQueue;
import gdbDriver.Output.OutputConfig;

import java.util.concurrent.ConcurrentLinkedQueue;

public interface IntegratedCallBackInterface {
    public void run(OutputConfig outputConfig, UserCommandQueue userCommandQueue);
}
