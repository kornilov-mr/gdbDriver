package gdbDriver.Configer;

import gdbDriver.CallBacks.ElementaryCallbacksInterface;
import gdbDriver.CallBacks.IntegratedCallBackInterface;
import gdbDriver.CallBacks.SimpleCallBackInterface;
import gdbDriver.Output.OutputConfig;

import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ErrorCatcher {

    private final Vector<SimpleCallBackInterface> simpleCallbacks = new Vector<>();
    private final Vector<IntegratedCallBackInterface> integratedCallbacks = new Vector<>();

    private final Vector<ElementaryCallbacksInterface> elementaryCallbacks = new Vector<>();


    public void addCallback(IntegratedCallBackInterface callback) {
        integratedCallbacks.add(callback);
    }

    public void addCallback(SimpleCallBackInterface callback) {
        simpleCallbacks.add(callback);
    }

    public void addCallback(ElementaryCallbacksInterface callback) {
        elementaryCallbacks.add(callback);
    }


    protected String createCatchCommand() {
        return "catch catch";
    }

    public void executeCallbacks(OutputConfig outputConfig, ConcurrentLinkedQueue<String> userCommandQueue) {
        for (ElementaryCallbacksInterface callback : elementaryCallbacks) {
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
