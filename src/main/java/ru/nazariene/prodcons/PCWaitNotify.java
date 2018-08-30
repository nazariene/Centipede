package ru.nazariene.prodcons;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Producer-consumer demo using wait and notify as sync constructs
 */
public class PCWaitNotify {

    public static void main(String[] args) {
        Queue<String> buffer = new ArrayDeque<>();

        Producer producer = new Producer(buffer);
        Consumer consumer = new Consumer(buffer);
        new Thread(producer).start();
        new Thread(consumer).start();

    }

    public static class Producer implements Runnable {

        private int counter = 0;
        private Queue<String> buffer;

        public Producer(Queue<String> buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            while(true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Producer got interrupted");
                }
                produce();
            }
        }

        private void produce() {
            synchronized (buffer) {
                buffer.add(String.valueOf(counter++));
                buffer.notifyAll();
            }
        }
    }

    public static class Consumer implements Runnable {

        private Queue<String> buffer;

        public Consumer(Queue<String> buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            while(true) {
                consume();
            }
        }

        private void consume() {
            synchronized (buffer) {
                if (buffer.size() > 0) {
                    System.out.println(buffer.poll());
                }
                else {
                    try {
                        buffer.wait();
                    } catch (InterruptedException e) {
                        System.out.println("Consumer interrupted");
                    }
                }
            }
        }
    }
}
