package gdbDriver.Configer;

import gdbDriver.CallBacks.IntegratedCallBackInterface;
import gdbDriver.CallBacks.SimpleCallBackInterface;
import gdbDriver.Output.OutputConfig;

import java.util.Queue;
import java.util.Vector;

public class Catcher {

    private final Vector<SimpleCallBackInterface> simpleCallbacks = new Vector<>();
    private final Vector<IntegratedCallBackInterface> integratedCallbacks = new Vector<>();
    public void addCallback(IntegratedCallBackInterface callback){
        integratedCallbacks.add(callback);
    }
    public void addCallback(SimpleCallBackInterface callback){
        simpleCallbacks.add(callback);
    }

    protected String createCatchCommand(){
        return "catch catch";
    }
    public void executeCallbacks(OutputConfig outputConfig, Queue<String> userCommandQueue){
        for(SimpleCallBackInterface callback : simpleCallbacks){
            callback.run(outputConfig);
        }
        for(IntegratedCallBackInterface callback : integratedCallbacks){
            callback.run(outputConfig,userCommandQueue);
        }
    }
}
