/**
 * Handles the detection and removal of completed rows, returning updated
 * board matrices and line clear counts.
 */
package com.tetris.game.board;

public class ClearRow {

    private final int[][] newMatrix;
    private final int count;
    private final int[] clearedRows;

    public ClearRow(int[][] newMatrix, int count, int[] clearedRows) {
        this.newMatrix = newMatrix;
        this.count = count;
        this.clearedRows = clearedRows;
    }

    public int[][] getNewMatrix() { return newMatrix; }
    public int getCount() { return count; }
    public int[] getClearedRows() { return clearedRows; }

    public int getLinesRemoved() { return count; }

    public int getScoreBonus() {
        switch (count) {
            case 1: return 100;
            case 2: return 300;
            case 3: return 500;
            case 4: return 800;
            default: return 0;
        }
    }
}
