package com.tetris.game.board;

public class BoardState {

    private int[][] matrix;
    private final int width;
    private final int height;

    public BoardState(int width, int height) {
        this.width = width;
        this.height = height;
        reset();
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void reset() {
        matrix = new int[width][height];
    }

    /**
     * Merges a brick (shape matrix) into the board matrix.
     */
    public void mergeBrick(int[][] shape, int offsetX, int offsetY) {
        matrix = MatrixOperations.merge(matrix, shape, offsetX, offsetY);
    }

    /**
     * Clears any full rows and returns info about cleared rows.
     */
    public ClearRow clearRows() {
        ClearRow cleared = MatrixOperations.checkRemoving(matrix);
        matrix = cleared.getNewMatrix();
        return cleared;
    }
}
