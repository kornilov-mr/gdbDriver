package gdbDriver.StreamHandlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Queue;

public class SystemInListener extends Thread {

    private final Queue<String> UserCommandQueue;

    public SystemInListener(Queue<String> UserCommandQueue) {
        this.UserCommandQueue = UserCommandQueue;
    }

    @Override
    public void run() {
        //reading user input loop
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                if (bufferedReader.ready()) {
                    String command = bufferedReader.readLine();
                    UserCommandQueue.add(command + "\n");
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                break;
            } catch (IOException e) {
                System.out.println("A problem occurred while reading the System.in");
                throw new RuntimeException(e);
            }

        }
    }
}
