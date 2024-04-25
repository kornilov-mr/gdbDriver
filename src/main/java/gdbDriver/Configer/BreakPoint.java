package gdbDriver.Configer;

import gdbDriver.CallBacks.IntegratedCallBackInterface;
import gdbDriver.CallBacks.SimpleCallBackInterface;
import gdbDriver.Output.OutputConfig;

import java.util.Queue;
import java.util.Vector;

public class BreakPoint {

    protected int row;
    protected String fileName;
    private final Vector<Runnable> elementaryCallbacks = new Vector<>();
    private final Vector<SimpleCallBackInterface> simpleCallbacks = new Vector<>();
    private final Vector<IntegratedCallBackInterface> integratedCallbacks = new Vector<>();

    public BreakPoint(String fileName, int row) {
        this.row = row;
        this.fileName = fileName;
    }

    public void addCallback(Runnable callback) {
        elementaryCallbacks.add(callback);
    }

    public void addCallback(IntegratedCallBackInterface callback) {
        integratedCallbacks.add(callback);
    }

    public void addCallback(SimpleCallBackInterface callback) {
        simpleCallbacks.add(callback);
    }

    protected String createBreakCommand() {
        return "break " + row;
    }

    public void executeCallbacks(OutputConfig outputConfig, Queue<String> userCommandQueue) {
        for (Runnable callback : elementaryCallbacks) {
            callback.run();
        }
        for (SimpleCallBackInterface callback : simpleCallbacks) {
            callback.run(outputConfig);
        }
        for (IntegratedCallBackInterface callback : integratedCallbacks) {
            callback.run(outputConfig, userCommandQueue);
        }
    }
}
