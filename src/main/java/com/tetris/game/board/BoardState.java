package com.tetris.game.board;

public class BoardState {

    private final int width;
    private final int height;

    private int[][] matrix;

    public BoardState(int width, int height) {
        this.width = width;
        this.height = height;
        this.matrix = new int[width][height];
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void reset() {
        this.matrix = new int[width][height];
    }

    public void mergeBrick(int[][] brickShape, int x, int y) {
        matrix = MatrixOperations.merge(matrix, brickShape, x, y);
    }

    public ClearRow clearRows() {
        ClearRow result = MatrixOperations.checkRemoving(matrix);
        matrix = result.getNewMatrix();
        return result;
    }
}
