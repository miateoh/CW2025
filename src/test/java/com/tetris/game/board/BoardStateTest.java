package com.tetris.game.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardStateTest {

    private BoardState boardState;

    @BeforeEach
    void setup() {
        boardState = new BoardState(10, 20); // width=10, height=20
    }

    @Test
    void testInitialBoardIsEmpty() {
        int[][] matrix = boardState.getMatrix();
        // matrix is [height][width] = [20][10]
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 10; x++) {
                assertEquals(0, matrix[y][x]); // FIXED: [y][x] not [x][y]
            }
        }
    }

    @Test
    void testResetClearsBoard() {
        int[][] matrix = boardState.getMatrix();
        matrix[0][0] = 1;
        matrix[10][5] = 2; // FIXED: [y][x] not [x][y]

        boardState.reset();

        matrix = boardState.getMatrix();
        assertEquals(0, matrix[0][0]);
        assertEquals(0, matrix[10][5]); // FIXED: [y][x] not [x][y]
    }

    @Test
    void testMergeBrickAddsShapeToBoard() {
        int[][] shape = {
                {1, 1},
                {1, 1}
        };

        boardState.mergeBrick(shape, 4, 10);

        int[][] matrix = boardState.getMatrix();
        // FIXED: All should be [y][x]
        assertEquals(1, matrix[10][4]); // row 10, col 4
        assertEquals(1, matrix[10][5]); // row 10, col 5
        assertEquals(1, matrix[11][4]); // row 11, col 4
        assertEquals(1, matrix[11][5]); // row 11, col 5
    }

    @Test
    void testClearRowsRemovesFullRow() {
        int[][] matrix = boardState.getMatrix();

        // Fill bottom row (y=19)
        for (int x = 0; x < 10; x++) {
            matrix[19][x] = 1; // FIXED: [y][x] not [x][y]
        }

        ClearRow result = boardState.clearRows();
        assertEquals(1, result.getCount());
    }

    @Test
    void testClearRowsShiftsRowsDown() {
        int[][] matrix = boardState.getMatrix();

        matrix[18][0] = 5;  // FIXED: row 18, col 0

        // Fill bottom row
        for (int x = 0; x < 10; x++) {
            matrix[19][x] = 1; // FIXED: [y][x]
        }

        boardState.clearRows();

        int[][] newMatrix = boardState.getMatrix();
        assertEquals(5, newMatrix[19][0]);  // FIXED: moved down to bottom
        assertEquals(0, newMatrix[18][0]);  // FIXED: old position now empty
    }

    @Test
    void testClearRowsReturnsNewMatrixInstance() {
        int[][] before = boardState.getMatrix();
        boardState.clearRows();
        int[][] after = boardState.getMatrix();
        assertNotSame(before, after);
    }
}