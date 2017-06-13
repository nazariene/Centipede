package ru.nazariene.seabattle;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.SynchronousQueue;

public class SeaBattle {

    public static void main(String[] args) throws InterruptedException {
        SynchronousQueue<Integer> queue = new SynchronousQueue<>();
        CyclicBarrier barrier = new CyclicBarrier(2);
        //Thread thread1 = new Thread(new SyncQueuePlayer(queue, 10, 3, true), "P1");
        //Thread thread2 = new Thread(new SyncQueuePlayer(queue, 10, 3, false), "P2");
        Thread thread1 = new Thread(new SyncQueueBarrierPlayer(queue, 10, 3, barrier), "P1");
        Thread thread2 = new Thread(new SyncQueueBarrierPlayer(queue, 10, 3, barrier), "P2");
        //NotifyGame game = new NotifyGame();
        //Thread thread1 = new Thread(new NotifyPlayer(game, 10, 3, true), "P1");
        //Thread thread2 = new Thread(new NotifyPlayer(game, 10, 3, false), "P2");
        thread1.start();
        thread2.start();

        while (true) {
            System.out.println("P1:" + thread1.getState());
            System.out.println("P2:" + thread2.getState());
            Thread.sleep(1000);
        }
    }

}
