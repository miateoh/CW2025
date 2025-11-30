package com.tetris.game.logic;

import com.tetris.NextShapeInfo;
import com.tetris.game.board.BoardState;
import com.tetris.game.board.MatrixOperations;
import com.tetris.game.bricks.BrickRotator;

import java.awt.*;

public class BrickMovementController {

    private final BoardState boardState;
    private final BrickRotator brickRotator;

    private Point currentOffset;

    public BrickMovementController(BoardState boardState, BrickRotator brickRotator) {
        this.boardState = boardState;
        this.brickRotator = brickRotator;
        this.currentOffset = new Point(4, 10); // default spawn point
    }

    public Point getCurrentOffset() {
        return currentOffset;
    }

    public void setCurrentOffset(Point p) {
        this.currentOffset = p;
    }

    public boolean moveDown() {
        return tryMove(0, 1);
    }

    public boolean moveLeft() {
        return tryMove(-1, 0);
    }

    public boolean moveRight() {
        return tryMove(1, 0);
    }

    public boolean rotateLeft() {
        int[][] currentMatrix = MatrixOperations.copy(boardState.getMatrix());

        NextShapeInfo nextShape = brickRotator.getNextShape();
        boolean conflict = MatrixOperations.intersect(
                currentMatrix,
                nextShape.getShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY()
        );

        if (conflict) return false;

        brickRotator.setCurrentShape(nextShape.getPosition());
        return true;
    }

    private boolean tryMove(int dx, int dy) {
        int[][] currentMatrix = MatrixOperations.copy(boardState.getMatrix());

        Point p = new Point(currentOffset);
        p.translate(dx, dy);

        boolean conflict = MatrixOperations.intersect(
                currentMatrix,
                brickRotator.getCurrentShape(),
                (int) p.getX(),
                (int) p.getY()
        );

        if (conflict) return false;

        currentOffset = p;
        return true;
    }
}
