package ru.nazariene.prodcons;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

public class PCSyncQueueExecutors {

    public static void main(String[] args) {
        BlockingQueue<String> buffer = new SynchronousQueue<>();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(new Producer(buffer));
        executorService.execute(new Consumer(buffer));
    }

    public static class Producer implements Runnable {
        private int counter = 0;
        private BlockingQueue<String> buffer;

        public Producer(BlockingQueue<String> queue) {
            this.buffer = queue;
        }

        @Override
        public void run() {
            while(true) {
                try {
                    Thread.sleep(1000);
                    buffer.put(String.valueOf(counter++));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer implements Runnable {

        private BlockingQueue<String> buffer;

        public Consumer(BlockingQueue<String> queue) {
            this.buffer = queue;
        }

        @Override
        public void run() {
            while(true) {
                try {
                    System.out.println(buffer.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
