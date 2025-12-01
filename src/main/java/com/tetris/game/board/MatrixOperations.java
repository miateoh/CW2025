package com.tetris.game.board;

import java.util.List;
import java.util.stream.Collectors;

public class MatrixOperations {

    private MatrixOperations() {}

    // ----------------------------
    // CORRECTED COLLISION CHECK
    // ----------------------------
    public static boolean intersect(int[][] board, int[][] shape, int offsetX, int offsetY) {

        int shapeHeight = shape.length;        // rows (y)
        int shapeWidth = shape[0].length;      // columns (x)

        int boardWidth = board.length;
        int boardHeight = board[0].length;

        for (int y = 0; y < shapeHeight; y++) {
            for (int x = 0; x < shapeWidth; x++) {

                if (shape[y][x] != 0) {

                    int boardX = offsetX + x;
                    int boardY = offsetY + y;

                    // Out of bounds = collision
                    if (boardX < 0 || boardX >= boardWidth ||
                            boardY < 0 || boardY >= boardHeight) {
                        return true;
                    }

                    // Occupied = collision
                    if (board[boardX][boardY] != 0) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // ----------------------------
    // CORRECTED MERGE
    // ----------------------------
    public static void merge(int[][] matrix, int[][] shape, int offsetX, int offsetY) {
        int shapeHeight = shape.length;
        int shapeWidth = shape[0].length;

        for (int y = 0; y < shapeHeight; y++) {
            for (int x = 0; x < shapeWidth; x++) {

                if (shape[y][x] != 0) {

                    int boardX = offsetX + x;
                    int boardY = offsetY + y;

                    if (boardX >= 0 && boardX < matrix.length &&
                            boardY >= 0 && boardY < matrix[0].length) {
                        matrix[boardX][boardY] = shape[y][x];
                    }
                }
            }
        }
    }


    // ----------------------------
    // CLEAR ROWS (y is row index)
    // ----------------------------
    public static ClearRow checkRemoving(int[][] matrix) {

        int width = matrix.length;
        int height = matrix[0].length;

        int removedCount = 0;

        int[][] newMatrix = new int[width][height];

        int newY = height - 1;

        for (int y = height - 1; y >= 0; y--) {

            boolean fullRow = true;
            for (int x = 0; x < width; x++) {
                if (matrix[x][y] == 0) {
                    fullRow = false;
                    break;
                }
            }

            if (fullRow) {
                removedCount++;
            } else {
                for (int x = 0; x < width; x++) {
                    newMatrix[x][newY] = matrix[x][y];
                }
                newY--;
            }
        }

        return new ClearRow(newMatrix, removedCount);
    }

    // ----------------------------
    // SAFE COPY OF MATRIX
    // ----------------------------
    public static int[][] copy(int[][] original) {
        int[][] result = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = original[i].clone();
        }
        return result;
    }

    // ----------------------------
    // SAFE COPY OF SHAPE LIST
    // ----------------------------
    public static List<int[][]> deepCopyList(List<int[][]> list) {
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }
}

