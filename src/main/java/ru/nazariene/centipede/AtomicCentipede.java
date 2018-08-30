package ru.nazariene.centipede;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCentipede {

    public static AtomicInteger monitor = new AtomicInteger(0);

    public static void main(String[] args) {
        for (int i = 0; i < 30; i++) {
            new Thread(new Leg(i)).start();
        }
    }

    public static class Leg implements Runnable {
        private int number;

        public Leg(int number) {
            this.number = number;
        }

        @Override
        public void run() {
            while (true) {
                if (monitor.intValue() == number) {
                    System.out.println("Leg " + number);
                    monitor.incrementAndGet();

                    if (monitor.intValue() >= 30) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        monitor.compareAndSet(30, 0);
                    }
                }
            }
        }
    }
}
