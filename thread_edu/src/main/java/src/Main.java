package src;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        IntStream.range(0, 10).forEach(n -> executor.execute(() -> {
            try {
                TimeUnit.MICROSECONDS.sleep(300);
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName);
                Thread.currentThread().interrupt();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
    }
}
