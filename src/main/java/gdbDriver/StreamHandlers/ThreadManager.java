package gdbDriver.StreamHandlers;

public class ThreadManager {

    private final Thread errorOutput;
    private final Thread userInput;
    private final Process GDBprocess;
    private boolean alive = true;

    public ThreadManager(Thread errorOutput, Thread userInput, Process gdBprocess) {
        this.errorOutput = errorOutput;
        this.userInput = userInput;
        this.GDBprocess = gdBprocess;
    }
    public void stopAllThreads(){
        alive=false;
        errorOutput.interrupt();
        userInput.interrupt();
        GDBprocess.destroy();
    }
    public boolean isAlive(){
        return alive;
    }
}
