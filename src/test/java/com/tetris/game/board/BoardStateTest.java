package com.tetris.game.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardStateTest {

    private BoardState boardState;

    @BeforeEach
    void setup() {
        boardState = new BoardState(10, 20);
    }

    @Test
    void testInitialBoardIsEmpty() {
        int[][] matrix = boardState.getMatrix();
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[0].length; y++) {
                assertEquals(0, matrix[x][y]);
            }
        }
    }

    @Test
    void testResetClearsBoard() {
        // Fill some values manually
        int[][] matrix = boardState.getMatrix();
        matrix[0][0] = 1;
        matrix[5][10] = 2;

        boardState.reset();

        matrix = boardState.getMatrix();
        assertEquals(0, matrix[0][0]);
        assertEquals(0, matrix[5][10]);
    }

    @Test
    void testMergeBrickAddsShapeToBoard() {
        int[][] shape = {
                {1, 1},
                {1, 1}
        };

        boardState.mergeBrick(shape, 4, 10);

        int[][] matrix = boardState.getMatrix();

        assertEquals(1, matrix[4][10]);
        assertEquals(1, matrix[5][11]);
    }

    @Test
    void testMergeDoesNotModifyOriginalShape() {
        int[][] shape = {
                {2, 2},
                {2, 2}
        };

        int[][] originalCopy = {
                {2, 2},
                {2, 2}
        };

        boardState.mergeBrick(shape, 3, 3);

        // Shape matrix should remain unchanged
        assertArrayEquals(originalCopy, shape);
    }

    @Test
    void testClearRowsRemovesFullRow() {
        int[][] matrix = boardState.getMatrix();

        // Fill row y=19
        for (int x = 0; x < 10; x++) {
            matrix[x][19] = 1;
        }

        ClearRow result = boardState.clearRows();

        assertEquals(1, result.getCount());
    }

    @Test
    void testClearRowsShiftsRowsDown() {
        int[][] matrix = boardState.getMatrix();

        // Place something on row 18
        matrix[0][18] = 5;

        // Fill row 19 completely
        for (int x = 0; x < 10; x++) {
            matrix[x][19] = 1;
        }

        boardState.clearRows();

        // After clear, row 18 moves to row 19
        assertEquals(5, boardState.getMatrix()[0][19]);
    }

    @Test
    void testClearRowsReturnsNewMatrixInstance() {
        int[][] before = boardState.getMatrix();
        ClearRow clearRow = boardState.clearRows();
        int[][] after = boardState.getMatrix();

        assertNotSame(before, after);
    }
}

