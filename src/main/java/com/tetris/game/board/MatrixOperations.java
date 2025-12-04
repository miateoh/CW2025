package com.tetris.game.board;

import java.util.List;
import java.util.stream.Collectors;

public class MatrixOperations {

    private MatrixOperations() {}

    // -------------------------------------------------------------------------
    // INTERSECTION CHECK — consistent row-major (board[y][x])
    // -------------------------------------------------------------------------
    public static boolean intersect(int[][] board, int[][] shape, int offsetX, int offsetY) {

        int shapeRows = shape.length;          // y dimension
        int shapeCols = shape[0].length;       // x dimension

        int boardHeight = board.length;        // rows
        int boardWidth  = board[0].length;     // columns

        for (int sy = 0; sy < shapeRows; sy++) {
            for (int sx = 0; sx < shapeCols; sx++) {

                if (shape[sy][sx] != 0) {

                    int bx = offsetX + sx;     // board X
                    int by = offsetY + sy;     // board Y

                    // Out of bounds → collision
                    if (bx < 0 || bx >= boardWidth ||
                            by < 0 || by >= boardHeight) {
                        return true;
                    }

                    // Filled cell → collision
                    if (board[by][bx] != 0) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // -------------------------------------------------------------------------
    // MERGE SHAPE INTO BOARD — row-major (board[y][x])
    // -------------------------------------------------------------------------
    public static void merge(int[][] board, int[][] shape, int offsetX, int offsetY) {

        int shapeRows = shape.length;
        int shapeCols = shape[0].length;

        int boardHeight = board.length;
        int boardWidth  = board[0].length;

        for (int sy = 0; sy < shapeRows; sy++) {
            for (int sx = 0; sx < shapeCols; sx++) {

                if (shape[sy][sx] != 0) {

                    int bx = offsetX + sx;
                    int by = offsetY + sy;

                    if (bx >= 0 && bx < boardWidth &&
                            by >= 0 && by < boardHeight) {

                        board[by][bx] = shape[sy][sx];
                    }
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // CLEAR ROWS — also updated to row-major (board[y][x])
    // -------------------------------------------------------------------------
    public static ClearRow checkRemoving(int[][] board) {

        int boardHeight = board.length;     // rows
        int boardWidth  = board[0].length;  // columns

        int removedCount = 0;

        int[][] newBoard = new int[boardHeight][boardWidth];
        int newRow = boardHeight - 1;

        // Scan from bottom up
        for (int y = boardHeight - 1; y >= 0; y--) {

            boolean full = true;

            for (int x = 0; x < boardWidth; x++) {
                if (board[y][x] == 0) {
                    full = false;
                    break;
                }
            }

            if (!full) {
                // Keep this row
                for (int x = 0; x < boardWidth; x++) {
                    newBoard[newRow][x] = board[y][x];
                }
                newRow--;
            } else {
                removedCount++;
            }
        }

        return new ClearRow(newBoard, removedCount);
    }

    // -------------------------------------------------------------------------
    // DEEP COPY UTILITIES
    // -------------------------------------------------------------------------
    public static int[][] copy(int[][] original) {
        int[][] result = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = original[i].clone();
        }
        return result;
    }

    public static List<int[][]> deepCopyList(List<int[][]> list) {
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }
}
