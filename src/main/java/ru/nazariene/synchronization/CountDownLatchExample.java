package ru.nazariene.synchronization;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchExample {

    private static CountDownLatch LATCH = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException {
        simpleLatchDemo();
        closedLatchDemo();
    }

    private static void simpleLatchDemo() throws InterruptedException {
        Runnable runnable = () -> {
            System.out.println("Runnable " + Thread.currentThread().getName());
            try {
                LATCH.await();
            } catch (InterruptedException e) {
                System.out.println("Got interruped " + Thread.currentThread().getName());
            }
            System.out.println("Released Runnable " + Thread.currentThread().getName());
        };

        new Thread(runnable, "ONE").start();
        Thread.sleep(2000);
        System.out.println(LATCH.getCount());
        LATCH.countDown();
        System.out.println(LATCH.getCount());
    }

    private static void closedLatchDemo() {
        LATCH = new CountDownLatch(1);
        LATCH.countDown();
        Runnable runnable = () -> {
            System.out.println("Runnable " + Thread.currentThread().getName());
            try {
                //Nothing happens, LATCH is closed
                LATCH.await();
            } catch (InterruptedException e) {
                System.out.println("Got interruped " + Thread.currentThread().getName());
            }
            System.out.println("Released Runnable " + Thread.currentThread().getName());
        };

        new Thread(runnable, "ONE").start();
    }
}
