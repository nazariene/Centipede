package ru.nazariene.threads;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Small demo of why we use volatile
 * While volatile + synchronized will solve the synchronization issues, it is very expensive and slow. Alternative - use Atomics
 */
public class AtomicsExample {

    private static int counterUnsafe = 0;

    private static AtomicInteger counterAtomic = new AtomicInteger(0);

    private static AtomicInteger counterAtomicCompare = new AtomicInteger(0);

    public static void incrementUnsafe() {
        counterUnsafe++;
    }

    public static void incrementSync() {
        counterAtomic.incrementAndGet();
    }

    public static void incrementCounterAtomicCompare() {
        counterAtomicCompare.getAndIncrement();
    }

    public static class CountRunnable implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                incrementUnsafe();
            }

            for (int i = 0; i < 10000; i++) {
                incrementSync();
            }

            for (int i = 0; i < 10000; i++) {
                incrementCounterAtomicCompare();
            }
            System.out.println(Thread.currentThread().getName() + " done");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread threadA = new Thread(new CountRunnable(), "Thread A");
        Thread threadB = new Thread(new CountRunnable(), "Thread B");

        threadA.start();
        threadB.start();

        threadA.join();
        threadB.join();

        System.out.println(counterUnsafe);
        System.out.println(counterAtomic);
        System.out.println(counterAtomicCompare);
    }
}
