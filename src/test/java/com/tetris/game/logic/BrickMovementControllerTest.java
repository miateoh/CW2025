package com.tetris.game.logic;

import com.tetris.game.board.BoardState;
import com.tetris.game.bricks.BrickRotator;
import com.tetris.game.bricks.IBrick;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Point;

import static org.junit.jupiter.api.Assertions.*;

class BrickMovementControllerTest {

    private BoardState board;
    private BrickRotator rotator;
    private BrickMovementController controller;

    @BeforeEach
    void setup() {
        board = new BoardState(10, 20);
        rotator = new BrickRotator();
        controller = new BrickMovementController(board, rotator);

        rotator.setBrick(new IBrick()); // predictable horizontal I-piece
        controller.setCurrentOffset(new Point(4, 0));
    }

    @Test
    void testMoveDown() {
        assertTrue(controller.moveDown());
        assertEquals(1, controller.getCurrentOffset().y);
    }

    @Test
    void testMoveBlockedByBoard() {
        BoardState board = new BoardState(10, 20);
        BrickRotator rotator = new BrickRotator();
        rotator.setBrick(new IBrick());
        BrickMovementController controller = new BrickMovementController(board, rotator);

        controller.setCurrentOffset(new Point(4, 0));

        // The I-brick at offset (4, 0) has solid cells at board[1][4-7]
        // When it tries to move down to (4, 1), it will check board[2][4-7]
        // So we need to block row 2, not row 1
        board.getMatrix()[2][4] = 9;  // Block at least one cell in the path

        boolean moved = controller.moveDown();

        assertFalse(moved, "Move down should fail when space is occupied");
    }


    @Test
    void testMoveLeft() {
        controller.setCurrentOffset(new Point(4, 0));
        assertTrue(controller.moveLeft());
        assertEquals(3, controller.getCurrentOffset().x);
    }

    @Test
    void testMoveRight() {
        controller.setCurrentOffset(new Point(4, 0));
        assertTrue(controller.moveRight());
        assertEquals(5, controller.getCurrentOffset().x);
    }

    @Test
    void testRotateNoCollision() {
        assertTrue(controller.rotateLeft());
    }
}
