package ru.nazariene;

public class InterruptedTest {

    public static void main(String[] args) {
        new InterruptedTest().run(args);
    }
    public void run(String[] args) {
        new Thread(new InterruptedThread()).start();
    }

    public class InterruptedThread implements Runnable {

        @Override
        public void run() {
            Thread.currentThread().interrupt();
            synchronized (this) {
                try {
                    wait();
                    System.out.println(Thread.currentThread().isInterrupted());
                } catch (InterruptedException e) {
                    System.out.println("Was interrupted!");
                } catch (IllegalMonitorStateException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
