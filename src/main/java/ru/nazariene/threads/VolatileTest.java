package ru.nazariene.threads;

public class VolatileTest implements Runnable{

    public static volatile long testVar = 5;

    public void run() {
        for (int i = 0; i < 5; i ++) {
            System.out.println(Thread.currentThread().getName() + ": " + i + ", " + testVar);
            testVar = i;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(new VolatileTest(), "1");
        thread1.setName("thread1");
        thread1.start();
        thread1 = new Thread(new VolatileTest(), "2");
        thread1.setName("thread2");
        thread1.start();

    }
}
