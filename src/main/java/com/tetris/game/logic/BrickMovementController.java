package com.tetris.game.logic;

import com.tetris.game.board.BoardState;
import com.tetris.game.board.MatrixOperations;
import com.tetris.game.bricks.BrickRotator;

import java.awt.Point;

public class BrickMovementController {

    private final BoardState boardState;
    private final BrickRotator brickRotator;

    // Required by tests
    private Point currentOffset = new Point(4, 0);

    public BrickMovementController(BoardState boardState, BrickRotator brickRotator) {
        this.boardState = boardState;
        this.brickRotator = brickRotator;
    }

    // ---------------------------------------------
    // Accessors required by tests
    // ---------------------------------------------
    public Point getCurrentOffset() {
        return currentOffset;
    }

    public void setCurrentOffset(Point p) {
        this.currentOffset = p;
    }

    // ---------------------------------------------
    // Movement
    // ---------------------------------------------
    public boolean moveDown() {
        return tryMove(0, 1);
    }

    public boolean moveLeft() {
        return tryMove(-1, 0);
    }

    public boolean moveRight() {
        return tryMove(1, 0);
    }

    // ---------------------------------------------
    // Rotation
    // ---------------------------------------------
    public boolean rotateLeft() {
        int[][] nextShape = brickRotator.getNextShape().getShape();

        boolean collides = MatrixOperations.intersect(
                boardState.getMatrix(),
                nextShape,
                currentOffset.x,
                currentOffset.y
        );

        if (collides) {
            return false; // blocked
        }

        brickRotator.setCurrentShape(brickRotator.getNextShape().getPosition());
        return true;
    }

    // ---------------------------------------------
    // Core internal movement check
    // ---------------------------------------------
    private boolean tryMove(int dx, int dy) {
        int[][] board = boardState.getMatrix();
        int[][] shape = brickRotator.getCurrentShape();

        int newX = currentOffset.x + dx;
        int newY = currentOffset.y + dy;

        int shapeWidth = shape.length;
        int shapeHeight = shape[0].length;

        // --- 1. Bounds check ------------------------------------
        for (int sx = 0; sx < shapeWidth; sx++) {
            for (int sy = 0; sy < shapeHeight; sy++) {
                if (shape[sx][sy] != 0) {
                    int boardX = newX + sx;
                    int boardY = newY + sy;

                    // Out of board horizontally
                    if (boardX < 0 || boardX >= board.length)
                        return false;

                    // Out of board vertically
                    if (boardY < 0 || boardY >= board[0].length)
                        return false;

                    // Collision with existing block
                    if (board[boardX][boardY] != 0)
                        return false;
                }
            }
        }

        // --- 2. If all checks passed, update position ------------
        currentOffset = new Point(newX, newY);
        return true;
    }
}
