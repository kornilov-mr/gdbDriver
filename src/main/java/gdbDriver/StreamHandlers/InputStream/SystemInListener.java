package gdbDriver.StreamHandlers.InputStream;

import gdbDriver.Commands.userCommands.UserCommandQueue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SystemInListener extends Thread {

    private final UserCommandQueue userCommandQueue;

    public SystemInListener(UserCommandQueue userCommandQueue) {
        this.userCommandQueue = userCommandQueue;
    }

    @Override
    public void run() {
        //reading user input loop
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                if (bufferedReader.ready()) {
                    String command = bufferedReader.readLine();
                    userCommandQueue.addCommand(command);
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
