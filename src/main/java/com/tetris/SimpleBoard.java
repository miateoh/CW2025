package com.tetris;

import com.tetris.game.board.Board;
import com.tetris.game.board.ClearRow;
import com.tetris.game.board.MatrixOperations;
import com.tetris.game.board.BoardState;
import com.tetris.game.bricks.BrickRotator;
import com.tetris.game.bricks.Brick;
import com.tetris.game.bricks.BrickGenerator;
import com.tetris.game.bricks.RandomBrickGenerator;

import java.awt.*;

public class SimpleBoard implements Board {

    private final int width;
    private final int height;

    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private final BoardState boardState;

    private Point currentOffset;
    private final Score score;

    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;

        boardState = new BoardState(width, height);
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    @Override
    public boolean moveBrickDown() {
        int[][] currentMatrix = MatrixOperations.copy(boardState.getMatrix());
        Point p = new Point(currentOffset);
        p.translate(0, 1);

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

    @Override
    public boolean moveBrickLeft() {
        int[][] currentMatrix = MatrixOperations.copy(boardState.getMatrix());
        Point p = new Point(currentOffset);
        p.translate(-1, 0);

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

    @Override
    public boolean moveBrickRight() {
        int[][] currentMatrix = MatrixOperations.copy(boardState.getMatrix());
        Point p = new Point(currentOffset);
        p.translate(1, 0);

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

    @Override
    public boolean rotateLeftBrick() {
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

    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);

        // Spawn location
        currentOffset = new Point(4, 10);

        return MatrixOperations.intersect(
                boardState.getMatrix(),
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY()
        );
    }

    @Override
    public int[][] getBoardMatrix() {
        return boardState.getMatrix();
    }

    @Override
    public ViewData getViewData() {
        return new ViewData(
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY(),
                brickGenerator.getNextBrick().getShapeMatrix().get(0)
        );
    }

    @Override
    public void mergeBrickToBackground() {
        boardState.mergeBrick(
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY()
        );
    }

    @Override
    public ClearRow clearRows() {
        return boardState.clearRows();
    }

    @Override
    public Score getScore() {
        return score;
    }

    @Override
    public void newGame() {
        boardState.reset();
        score.reset();
        createNewBrick();
    }
}
