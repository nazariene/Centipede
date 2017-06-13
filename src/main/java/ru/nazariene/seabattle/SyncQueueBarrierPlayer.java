package ru.nazariene.seabattle;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.SynchronousQueue;

public class SyncQueueBarrierPlayer implements Runnable{

    private Field playerField;
    private Field enemyField;
    private int enemyShipsHit = 0;
    private SynchronousQueue<Integer> queue;
    private int shipsAmount;
    private List<String> logs = new LinkedList<>();
    private CyclicBarrier barrier;

    public SyncQueueBarrierPlayer(SynchronousQueue<Integer> queue, int fieldSize, int shipsAmount, CyclicBarrier barrier) {
        this.playerField = new Field(fieldSize, shipsAmount);
        this.enemyField = new Field(fieldSize);
        this.shipsAmount = shipsAmount;
        this.queue = queue;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        int playerIndex = 0;
        try {
            playerIndex = barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        if (playerIndex == 0) {
            System.out.println(Thread.currentThread().getName() + "attacked!");
            attack();
        }

        while (!gameFinished()) {
            makeMove();
        }

        System.out.println(logs);
    }

    private void makeMove() {
        int result = getResult();
        logs.add(Thread.currentThread().getName() + ": enemy replied with " + result);
        System.out.println(Thread.currentThread().getName() + ": enemy replied with " + result);

        if (result == -1) {
            enemyShipsHit++;
            if (gameFinished()) {
                return;
            }
            attack();
        }
        else if (!checkIfHit(result)) {
            attack();
        }
        else {
            replyHit();
        }
    }

    private void attack() {
        int positionToAttack = enemyField.getRandomEmptyCell();
        enemyField.markField(positionToAttack, true);
        logs.add(Thread.currentThread().getName() + " attacking " + positionToAttack);
        System.out.println(Thread.currentThread().getName() + ": attacking " + positionToAttack);
        sendMessage(positionToAttack);
    }

    private boolean checkIfHit(int position) {
        return (playerField.hit(position));
    }

    private void replyHit() {
        logs.add(Thread.currentThread().getName() + " got hit");
        System.out.println(Thread.currentThread().getName() + " got hit");
        sendMessage(-1);
    }

    private void sendMessage(int position) {
        try {
            System.out.println(Thread.currentThread().getName() + ": sending message - " + position);

            queue.put(position);
        } catch (InterruptedException e) {
            e.printStackTrace();
            sendMessage(position);
        }
    }

    private int getResult() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            return getResult();
        }
    }

    private boolean gameFinished() {
        return !(playerField.getShips() > 0 && enemyShipsHit < shipsAmount);
    }
}
