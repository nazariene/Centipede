package ru.nazariene.seabattle;

public class NotifyGame {

    private boolean firstPlayersTurnToMove = true;
    private volatile int lastMoveMade;

    public synchronized void sendMessage(int value) {
        while (!isItMyTurnToMove()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        lastMoveMade = value;
        firstPlayersTurnToMove = !firstPlayersTurnToMove;
        notifyAll();
    }

    public synchronized int getLastMoveMade() {
        while (!isItMyTurnToMove()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return lastMoveMade;
    }

    private boolean isItMyTurnToMove() {
        if (firstPlayersTurnToMove) {
            return Thread.currentThread().getName().equals("P1");
        } else {
            return Thread.currentThread().getName().equals("P2");
        }
    }
}
