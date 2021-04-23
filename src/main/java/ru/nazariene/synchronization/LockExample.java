package ru.nazariene.synchronization;

import java.util.concurrent.locks.ReentrantLock;

public class LockExample {

    private static final ReentrantLock LOCK = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        lockOnce();
        System.out.println("------------------");
        lockTwice();
    }

    private static void lockOnce() throws InterruptedException {
        Runnable runnable = () -> {
            System.out.println(Thread.currentThread().getName() + " locking");
            LOCK.lock();
            System.out.println(Thread.currentThread().getName() + " locked");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " unlock");
            LOCK.unlock();
        };

        var thread1 = new Thread(runnable, "One");
        thread1.start();
        var thread2 = new Thread(runnable, "Two");
        thread2.start();
        thread1.join();
        thread2.join();
    }

    /**
     * One can lock Lock multiple times. Be sure to unlock it same amount it times as well.
     * Otherwise you'll not be able to obtain the lock from another thread anymore
     * @throws InterruptedException
     */
    private static void lockTwice() throws InterruptedException {
        Runnable runnable = () -> {
            System.out.println(Thread.currentThread().getName() + " locking");
            try {
                LOCK.lockInterruptibly();
                LOCK.lockInterruptibly();
                System.out.println(Thread.currentThread().getName() + " locked");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " interrupted");
            }
            if (LOCK.isHeldByCurrentThread()) {
                LOCK.unlock();
                System.out.println(Thread.currentThread().getName() + " unlock");
            }
        };

        var thread1 = new Thread(runnable, "One");
        thread1.start();
        var thread2 = new Thread(runnable, "Two");
        thread2.start();

        Thread.sleep(10000);
        thread1.interrupt();
        thread2.interrupt();
    }
}
