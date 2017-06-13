package ru.nazariene.seabattle;

import java.util.LinkedList;
import java.util.List;

public class NotifyPlayer implements Runnable {

    private NotifyGame game;
    private Field playerField;
    private Field enemyField;
    private int enemyShipsHit = 0;
    private int shipsAmount;
    private List<String> logs = new LinkedList<>();
    private boolean firstToMove;

    public NotifyPlayer(NotifyGame game, int fieldSize, int shipsAmount, boolean firstToMove) {
        this.playerField = new Field(fieldSize, shipsAmount);
        this.enemyField = new Field(fieldSize);
        this.shipsAmount = shipsAmount;
        this.game = game;
        this.firstToMove = firstToMove;
    }

    @Override
    public void run() {
        if (firstToMove) {
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
        } else if (!checkIfHit(result)) {
            attack();
        } else {
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
        System.out.println(Thread.currentThread().getName() + ": sending message - " + position);

        game.sendMessage(position);
    }

    private int getResult() {
        return game.getLastMoveMade();
    }

    private boolean gameFinished() {
        return !(playerField.getShips() > 0 && enemyShipsHit < shipsAmount);
    }
}
