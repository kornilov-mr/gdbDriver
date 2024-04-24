package gdbDriver.CallBacks;

import gdbDriver.Output.OutputConfig;

import java.util.Queue;

public interface IntegratedCallBackInterface {
    public void run(OutputConfig outputConfig, Queue<String> userCommandQueue);
}
