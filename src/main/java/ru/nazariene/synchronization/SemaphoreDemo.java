package ru.nazariene.synchronization;

import java.util.concurrent.Semaphore;

public class SemaphoreDemo {

    private static final Semaphore SEMAPHORE = new Semaphore(2, true);

    private static Integer PERMITS = 1;

    private static final Runnable RUNNABLE = () -> {
        System.out.println(Thread.currentThread().getName() + " acquiring");
        try {
            SEMAPHORE.acquire(PERMITS);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " acquired!");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " releasing");
        for (int i = 0; i < PERMITS; i++) {
            SEMAPHORE.release();
        }
    };

    public static void main(String[] args) throws InterruptedException {
        semaphoreAcquire();
        System.out.println("------------------------");
        semaphoreAcquireMultiplePermits();
        System.out.println("------------------------");
        semaphoreAcquireInvalidPermits();
    }

    private static void semaphoreAcquire() throws InterruptedException {
        var thread1 = new Thread(RUNNABLE, "One");
        thread1.start();

        var thread2 = new Thread(RUNNABLE, "Two");
        thread2.start();

        var thread3 = new Thread(RUNNABLE, "Three");
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();
    }

    private static void semaphoreAcquireMultiplePermits() throws InterruptedException {
        PERMITS = 2;
        var thread1 = new Thread(RUNNABLE, "One");
        thread1.start();

        var thread2 = new Thread(RUNNABLE, "Two");
        thread2.start();

        var thread3 = new Thread(RUNNABLE, "Three");
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();
    }

    private static void semaphoreAcquireInvalidPermits() throws InterruptedException {
        PERMITS = 3;
        var thread = new Thread(RUNNABLE, "One");
        thread.start();
        Thread.sleep(10000);
        thread.interrupt();
    }

}
