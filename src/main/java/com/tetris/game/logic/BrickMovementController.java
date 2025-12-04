package com.tetris.game.logic;

import com.tetris.game.board.BoardState;
import com.tetris.game.board.MatrixOperations;
import com.tetris.game.bricks.BrickRotator;

import java.awt.Point;

public class BrickMovementController {

    private final BoardState boardState;
    private final BrickRotator brickRotator;

    private Point currentOffset = new Point(4, 0);  // correct spawn

    public BrickMovementController(BoardState boardState, BrickRotator brickRotator) {
        this.boardState = boardState;
        this.brickRotator = brickRotator;
    }

    public Point getCurrentOffset() { return currentOffset; }
    public void setCurrentOffset(Point p) { this.currentOffset = p; }

    public boolean moveDown() { return tryMove(0, 1); }
    public boolean moveLeft() { return tryMove(-1, 0); }
    public boolean moveRight() { return tryMove(1, 0); }

    public boolean rotateLeft() {
        var nextShapeInfo = brickRotator.getNextShape();

        boolean collision = MatrixOperations.intersect(
                boardState.getMatrix(),
                nextShapeInfo.getShape(),
                currentOffset.x,
                currentOffset.y
        );

        if (collision) return false;

        brickRotator.setCurrentShape(nextShapeInfo.getPosition());
        return true;
    }

    private boolean tryMove(int dx, int dy) {
        int[][] board = boardState.getMatrix();
        int[][] shape = brickRotator.getCurrentShape();

        int newX = currentOffset.x + dx;
        int newY = currentOffset.y + dy;

        int shapeRows = shape.length;         // y dimension
        int shapeCols = shape[0].length;      // x dimension

        int boardHeight = board.length;       // number of rows
        int boardWidth  = board[0].length;    // number of columns

        for (int sy = 0; sy < shapeRows; sy++) {
            for (int sx = 0; sx < shapeCols; sx++) {

                if (shape[sy][sx] != 0) {
                    int bx = newX + sx;   // board X
                    int by = newY + sy;   // board Y

                    // check bounds
                    if (bx < 0 || bx >= boardWidth)  return false;
                    if (by < 0 || by >= boardHeight) return false;

                    // check collision
                    if (board[by][bx] != 0) return false;
                }
            }
        }

        // movement allowed
        currentOffset = new Point(newX, newY);
        return true;
    }

}

