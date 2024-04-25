package gdbDriver.CallBacks;

import gdbDriver.Output.OutputConfig;

import java.util.concurrent.ConcurrentLinkedQueue;

public interface IntegratedCallBackInterface {
    public void run(OutputConfig outputConfig, ConcurrentLinkedQueue<String> userCommandQueue);
}
