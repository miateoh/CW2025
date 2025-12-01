package com.tetris.game.logic;

import com.tetris.game.board.BoardState;
import com.tetris.game.bricks.Brick;
import com.tetris.game.bricks.BrickRotator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BrickMovementControllerTest {

    private BoardState boardState;
    private BrickRotator rotator;
    private BrickMovementController controller;

    static class TestBrick implements Brick {
        private final List<int[][]> shapes;

        TestBrick(int[][] shape) {
            int[][][] wrapped = new int[][][] { shape };
            this.shapes = List.of(wrapped);
        }

        @Override
        public List<int[][]> getShapeMatrix() {
            return shapes;
        }
    }

    @BeforeEach
    void setup() {
        boardState = new BoardState(10, 20);
        rotator = new BrickRotator();

        int[][] shape = {
                {1, 1, 1},
                {0, 1, 0}
        };

        rotator.setBrick(new TestBrick(shape));
        controller = new BrickMovementController(boardState, rotator);
        controller.setCurrentOffset(new Point(4, 0));
    }

    @Test
    void testMoveDown() {
        assertTrue(controller.moveDown());
    }

    @Test
    void testMoveLeft() {
        controller.setCurrentOffset(new Point(5, 5));
        assertTrue(controller.moveLeft());
    }

    @Test
    void testMoveRight() {
        controller.setCurrentOffset(new Point(5, 5));
        assertTrue(controller.moveRight());
    }

    @Test
    void testRotateNoCollision() {
        controller.setCurrentOffset(new Point(4, 5));
        assertTrue(controller.rotateLeft(), "Rotation should be allowed in open space");
    }

    @Test
    void testMoveBlockedByBoard() {
        // Put a block directly below the brick
        int[][] matrix = boardState.getMatrix();
        matrix[4][1] = 9; // any non-zero value

        controller.setCurrentOffset(new Point(4, 0));

        assertFalse(controller.moveDown(), "Move down should fail when space is occupied");
    }
}
