package ru.nazariene.threads;

/**
 * Small demo of why we use volatile
 * Volatile - guarantees visibility
 * synchronized - guarantees visibility AND atomicity
 *
 * To add atomicity for volatile - use Atomics.
 */
public class VolatileExample {

    /**
     * This variable will use Thread local cache value. I.e. it might (and most probably will) get missed writes
     * Basically, thread will not sync its CPU cache with RAM each read/write - therefore, outdated values.
     * Worst case - each thread will work with this variable locally, without syncing with each other
     */
    private static int counterUnsafe = 0;
    /**
     * This will NOT get missed writes and reads. Thread CPU cache will be updated before reads/writes
     * Basically we have visibility AND atomicity guarantees here.
     */
    private static int counterSynchronized = 0;

    /**
     * This might get missed writes - because while volatile provides visibility guarantees
     * (i.e. Thread local cache will be synced with main memory before reads/writes)
     * it does NOT guarantee atomicity. And ++ is composite operation.
     * So, while thread will receive actually value for counter - other thread might not see the full update in time.
     * But generally spealing this value will be higher in the end than counterUnsafe :)
     */
    private static volatile int counterVolatile = 0;

    public static void incrementUnsafe() {
        counterUnsafe++;
    }

    public static synchronized void incrementSync() {
        counterSynchronized++;
    }

    public static void incrementVolatile() {
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


    private static Runnable runnable = () -> {
        int seed = (int) (Math.random() * (1000));
        System.out.println("Thread " + Thread.currentThread().getName() + " seed is " + seed);
        counterVolatile = seed;
    };


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
