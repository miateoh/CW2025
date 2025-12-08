/**
 * Utility class containing all low-level matrix manipulation logic
 * used for collision detection, merging shapes, and removing full rows.
 *
 * This class is stateless and only provides pure static helper methods.
 */

package com.tetris.game.board;

import java.util.List;
import java.util.stream.Collectors;

public class MatrixOperations {

    private MatrixOperations() {}

    // -------------------------------------------------------------------------
    // INTERSECTION CHECK
    // -------------------------------------------------------------------------
    public static boolean intersect(int[][] board, int[][] shape, int offsetX, int offsetY) {

        int shapeRows = shape.length;
        int shapeCols = shape[0].length;

        int boardHeight = board.length;
        int boardWidth = board[0].length;

        for (int sy = 0; sy < shapeRows; sy++) {
            for (int sx = 0; sx < shapeCols; sx++) {

                if (shape[sy][sx] != 0) {

                    int bx = offsetX + sx;
                    int by = offsetY + sy;

                    if (bx < 0 || bx >= boardWidth || by < 0 || by >= boardHeight)
                        return true;

                    if (board[by][bx] != 0)
                        return true;
                }
            }
        }

        return false;
    }

    // -------------------------------------------------------------------------
    // MERGE SHAPE INTO BOARD
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

                    if (bx >= 0 && bx < boardWidth && by >= 0 && by < boardHeight) {
                        board[by][bx] = shape[sy][sx];
                    }
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // CLEAR ROWS — supports particle animation
    // -------------------------------------------------------------------------
    public static ClearRow checkRemoving(int[][] board) {

        int boardHeight = board.length;
        int boardWidth = board[0].length;

        int removedCount = 0;
        java.util.List<Integer> clearedRows = new java.util.ArrayList<>();

        int[][] newBoard = new int[boardHeight][boardWidth];
        int newRow = boardHeight - 1;

        for (int y = boardHeight - 1; y >= 0; y--) {

            boolean full = true;

            for (int x = 0; x < boardWidth; x++) {
                if (board[y][x] == 0) {
                    full = false;
                    break;
                }
            }

            if (!full) {
                for (int x = 0; x < boardWidth; x++) {
                    newBoard[newRow][x] = board[y][x];
                }
                newRow--;
            } else {
                removedCount++;
                clearedRows.add(y);
            }
        }

        return new ClearRow(
                newBoard,
                removedCount,
                clearedRows.stream().mapToInt(i -> i).toArray()
        );
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
