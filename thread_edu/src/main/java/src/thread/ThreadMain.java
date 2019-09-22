package src.thread;

import java.util.concurrent.TimeUnit;

public class ThreadMain {
    private static volatile boolean stopRequested;

    private static synchronized void requestStop() {
        stopRequested = true;
    }

    private static synchronized boolean stopRequested() {
        return stopRequested;
    }


    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            int i = 0;
            while (!stopRequested) {
                System.out.println(i++);
            }
        });

        thread.start();

        TimeUnit.SECONDS.sleep(1);
        stopRequested = true;
    }
}
