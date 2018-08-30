package ru.nazariene.centipede;

public class SimpleCentipede {

    public static volatile Integer monitor = 0;

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
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
                if (monitor.equals(number)) {
                    System.out.println("Leg " + number);
                    monitor++;
                }
            }
        }
    }
}
