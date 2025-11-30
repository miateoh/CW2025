package com.tetris.game.board;

public class ClearRow {

    private final int[][] newMatrix;
    private final int count;
    private final int scoreBonus;

    public ClearRow(int[][] newMatrix, int count) {
        this.newMatrix = newMatrix;
        this.count = count;

        // Score bonus logic (standard Tetris scoring)
        switch (count) {
            case 1: scoreBonus = 100; break;
            case 2: scoreBonus = 300; break;
            case 3: scoreBonus = 500; break;
            case 4: scoreBonus = 800; break;
            default: scoreBonus = 0; break;
        }
    }

    public int[][] getNewMatrix() {
        return newMatrix;
    }

    public int getCount() {
        return count;
    }

    public int getLinesRemoved() {
        return count;
    }

    public int getScoreBonus() {
        return scoreBonus;
    }
}
