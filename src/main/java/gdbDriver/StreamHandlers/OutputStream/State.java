package gdbDriver.StreamHandlers.OutputStream;

import gdbDriver.Output.OutputInformationWritter;
import gdbDriver.StreamHandlers.ThreadManager;

public class State {
    public int rowShift=0;
    private final ThreadManager threadManager;
    private final OutputInformationWritter outputInformationWritter;

    public State(ThreadManager threadManager, OutputInformationWritter outputInformationWritter) {
        this.threadManager = threadManager;
        this.outputInformationWritter = outputInformationWritter;
    }

    public void killThreads(){
        threadManager.stopAllThreads();
        Thread.currentThread().interrupt();
    }
    public void showLineWithUpdatedState(){
        outputInformationWritter.writePreviousCodeWithShift(this.rowShift);
    }

}
