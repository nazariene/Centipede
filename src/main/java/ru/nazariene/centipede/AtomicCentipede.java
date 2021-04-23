package ru.nazariene.centipede;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCentipede {

    public static AtomicInteger MONITOR = new AtomicInteger(0);

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
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }

                //Commented out - alternative variant
                if (MONITOR.intValue() == number) {
                //var result = MONITOR.compareAndSet(this.number, this.number + 1);
                //if (result) {
                    MONITOR.incrementAndGet();
                    System.out.println("Leg " + number);

                    if (MONITOR.intValue() >= 30) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        MONITOR.compareAndSet(30, 0);
                    }
                }
            }
        }
    }
}

