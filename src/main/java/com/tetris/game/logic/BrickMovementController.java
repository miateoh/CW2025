/**
 * Handles all movement logic for the active falling brick.
 *
 * Responsibilities:
 * - Attempts left, right, down movement.
 * - Performs rotation with collision checks.
 * - Delegates final placement to BoardState when a brick lands.
 *
 * This class contains no UI code and represents pure game logic.
 */
package com.tetris.game.logic;

import com.tetris.game.board.BoardState;
import com.tetris.game.board.MatrixOperations;
import com.tetris.game.bricks.BrickRotator;

import java.awt.Point;

public class BrickMovementController {

    private final BoardState boardState;
    private final BrickRotator brickRotator;

    private Point currentOffset = new Point(4, 0);

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

        int shapeRows = shape.length;
        int shapeCols = shape[0].length;

        int boardHeight = board.length;
        int boardWidth  = board[0].length;

        for (int sy = 0; sy < shapeRows; sy++) {
            for (int sx = 0; sx < shapeCols; sx++) {

                if (shape[sy][sx] != 0) {
                    int bx = newX + sx;
                    int by = newY + sy;

                    if (bx < 0 || bx >= boardWidth)  return false;
                    if (by < 0 || by >= boardHeight) return false;

                    if (board[by][bx] != 0) return false;
                }
            }
        }

        currentOffset = new Point(newX, newY);
        return true;
    }

    /** GHOST landing Y — accurate bottom collision */
    public int getGhostLandingY() {
        int[][] board = boardState.getMatrix();
        int[][] shape = brickRotator.getCurrentShape();

        int shapeH = shape.length;
        int shapeW = shape[0].length;
        int boardH = board.length;

        int drop = 0;

        while (true) {
            for (int sy = 0; sy < shapeH; sy++) {
                for (int sx = 0; sx < shapeW; sx++) {
                    if (shape[sy][sx] != 0) {
                        int testX = currentOffset.x + sx;
                        int testY = currentOffset.y + sy + drop + 1;

                        // If we've hit the bottom
                        if (testY >= boardH) {
                            return Math.max(currentOffset.y + drop, 0); // Ensure non-negative
                        }

                        // If we've hit a filled cell
                        if (testY >= 0 && testX >= 0 && testX < board[0].length &&
                                board[testY][testX] != 0) {
                            return Math.max(currentOffset.y + drop, 0); // Ensure non-negative
                        }
                    }
                }
            }
            drop++;
        }
    }
}
