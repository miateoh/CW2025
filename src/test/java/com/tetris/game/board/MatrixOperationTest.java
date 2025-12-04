package com.tetris.game.board;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MatrixOperationsTest {

    @Test
    void testIntersectDetectsCollisionWithBoard() {
        int[][] board = new int[20][10];
        board[5][3] = 1; // obstacle

        int[][] shape = {{1, 1}, {1, 1}};

        assertTrue(MatrixOperations.intersect(board, shape, 2, 4));
    }

    @Test
    void testIntersectReturnsFalseWhenNoCollision() {
        int[][] board = new int[20][10];
        int[][] shape = {{1, 1}, {1, 1}};

        assertFalse(MatrixOperations.intersect(board, shape, 0, 0));
    }

    @Test
    void testIntersectDetectsOutOfBounds() {
        int[][] board = new int[20][10];
        int[][] shape = {{1, 1}};

        // Try to place shape outside left boundary
        assertTrue(MatrixOperations.intersect(board, shape, -1, 0));

        // Try to place shape outside right boundary
        assertTrue(MatrixOperations.intersect(board, shape, 10, 0));

        // Try to place shape outside bottom boundary
        assertTrue(MatrixOperations.intersect(board, shape, 0, 20));
    }

    @Test
    void testMergeAddsShapeToBoard() {
        int[][] board = new int[20][10];
        int[][] shape = {{2, 2}};

        MatrixOperations.merge(board, shape, 3, 5);

        assertEquals(2, board[5][3]);
        assertEquals(2, board[5][4]);
    }

    @Test
    void testCheckRemovingIdentifiesFullRow() {
        int[][] board = new int[20][10];
        // Fill bottom row
        for (int x = 0; x < 10; x++) {
            board[19][x] = 1;
        }

        ClearRow result = MatrixOperations.checkRemoving(board);
        assertEquals(1, result.getCount());
    }

    @Test
    void testCheckRemovingHandlesMultipleRows() {
        int[][] board = new int[20][10];
        // Fill two rows
        for (int x = 0; x < 10; x++) {
            board[18][x] = 1;
            board[19][x] = 1;
        }

        ClearRow result = MatrixOperations.checkRemoving(board);
        assertEquals(2, result.getCount());
    }

    @Test
    void testCopyCreatesIndependentCopy() {
        int[][] original = {{1, 2}, {3, 4}};
        int[][] copy = MatrixOperations.copy(original);

        // Modify the copy
        copy[0][0] = 99;

        // Original should be unchanged
        assertEquals(1, original[0][0]);
        assertEquals(99, copy[0][0]);
    }
}