package ru.nazariene.synchronization;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class BarrierExample {

    private static final String SEPARATOR = "----------------------------------";
    private static CyclicBarrier BARRIER = new CyclicBarrier(3, () -> System.out.println(SEPARATOR));

    public static void main(String[] args) throws InterruptedException {
        simpleBarrierDemo();
        brokenBarrierDemo();
    }

    private static void simpleBarrierDemo() {
        Runnable runnable = () -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Runnable " + Thread.currentThread().getName());
                try {
                    BARRIER.await();
                } catch (InterruptedException e) {
                    System.out.println("Got interruped " + Thread.currentThread().getName());
                } catch (BrokenBarrierException e) {
                    System.out.println("Broken Barrier " + Thread.currentThread().getName());
                }
            }
        };

        new Thread(runnable, "ONE").start();
        new Thread(runnable, "TWO").start();
        new Thread(runnable, "THREE").start();
    }

    private static void brokenBarrierDemo() throws InterruptedException {
        Runnable runnable = () -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Runnable " + Thread.currentThread().getName());
                try {
                    BARRIER.await();
                } catch (InterruptedException e) {
                    System.out.println("Got interruped " + Thread.currentThread().getName());
                } catch (BrokenBarrierException e) {
                    System.out.println("Broken Barrier " + Thread.currentThread().getName());
                }
            }
        };

        Thread thread1 = new Thread(runnable, "ONE");
        thread1.start();
        new Thread(runnable, "TWO").start();
        new Thread(runnable, "THREE").start();

        Thread.sleep(1000);

        BARRIER.reset();

    }

}
