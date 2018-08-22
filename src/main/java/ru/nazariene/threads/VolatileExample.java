package ru.nazariene.threads;

/**
 * Small demo of why we use volatile
 * While volatile + synchronized will solve the synchronization issues, it is very expensive and slow. Alternative - use Atomics
 */
public class VolatileExample {

    /**
     * This will have missed writes
     */
    private static int counterUnsafe = 0;
    /**
     * This MIGHT get missed writes, as it is not volatile (i.e. while synchronized, other threads may have outdated caches values)
     */
    private static int counterSynchronized = 0;
    /**
     * This will NOT get missed writes
     */
    private static volatile int counterVolatile = 0;

    public static void incrementUnsafe() {
        counterUnsafe++;
    }

    public static synchronized void incrementSync() {
        counterSynchronized++;
    }

    public static synchronized void incrementVolatile() {
        counterVolatile++;
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
                incrementVolatile();
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
        System.out.println(counterSynchronized);
        System.out.println(counterVolatile);
    }
}
