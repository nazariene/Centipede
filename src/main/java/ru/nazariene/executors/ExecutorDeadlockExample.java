package ru.nazariene.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorDeadlockExample {
    public static void main(String[] args) throws InterruptedException {
        Object monitor = new Object();
        Runnable runnable = () -> {
            System.out.println("Runnable A executing");
            synchronized (monitor) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    System.out.println("Interrupted");
                }
            }

            System.out.println("Finished Runnable A!");
        };

        Runnable runnableB = () -> {
            System.out.println("Runnable B executing");
            synchronized (monitor) {
                monitor.notifyAll();
            }

            System.out.println("Finished Runnable B!");

        };
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(runnable);
        executorService.submit(runnableB);
        Thread.sleep(5000);
        System.out.println("Waited 5 seconds, enough for the demo. Stopping all");
        executorService.shutdownNow();
    }
}
