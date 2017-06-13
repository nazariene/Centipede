package ru.nazariene.seabattle;

import java.util.concurrent.ThreadLocalRandom;

public class Field {

    private boolean field[];
    private int ships;

    public Field(int fieldSize) {
        this(fieldSize, 0);
    }

    public Field(int fieldSize, int shipsAmount) {
        this.field = generateField(fieldSize, shipsAmount);
        this.ships = shipsAmount;
    }

    public boolean hit(int position) {
        if (field[position]) {
            markField(position, false);
            ships--;
            return true;
        }

        return false;
    }

    public int getShips() {
        return ships;
    }

    public void markField(int position, boolean mark) {
        field[position] = mark;
    }

    private boolean[] generateField(int fieldSize, int shipsAmount) {
        field = new boolean[fieldSize];

        for (int i = 0; i < shipsAmount; i++) {
            addShip();
        }

        return field;
    }

    private void addShip() {
        int position = getRandomEmptyCell();

        if (position != -1) {
            markField(position, true);
        } else throw new IllegalStateException("No more free space on board!");
    }

    public int getRandomEmptyCell() {
        for (int i = 0; i < 10; i++) {
            int position = ThreadLocalRandom.current().nextInt(0, field.length);
            if (!field[position]) {
                return position;
            }
        }

        return getSequentialEmptyCell();
    }

    private int getSequentialEmptyCell() {
        for (int i = 0; i < field.length; i++) {
            if (!field[i]) {
                return i;
            }
        }

        return -1;
    }
}
