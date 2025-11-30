package com.tetris;

import com.tetris.game.board.Board;
import com.tetris.game.board.ClearRow;
import com.tetris.game.board.MatrixOperations;
import com.tetris.game.board.BoardState;
import com.tetris.game.bricks.BrickRotator;
import com.tetris.game.bricks.Brick;
import com.tetris.game.bricks.BrickGenerator;
import com.tetris.game.bricks.RandomBrickGenerator;
import com.tetris.game.logic.BrickManager;
import com.tetris.game.logic.BrickMovementController;

import java.awt.*;

public class SimpleBoard implements Board {

    private final int width;
    private final int height;

    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private final BoardState boardState;
    private final BrickMovementController movementController;
    private final BrickManager brickManager;

    private final Score score;

    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;

        boardState = new BoardState(width, height);
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        brickManager = new BrickManager(brickGenerator);
        movementController = new BrickMovementController(boardState, brickRotator);
        score = new Score();
    }

    @Override
    public boolean moveBrickDown() {
        return movementController.moveDown();
    }

    @Override
    public boolean moveBrickLeft() {
        return movementController.moveLeft();
    }

    @Override
    public boolean moveBrickRight() {
        return movementController.moveRight();
    }

    @Override
    public boolean rotateLeftBrick() {
        return movementController.rotateLeft();
    }

    @Override
    public boolean createNewBrick() {
        Brick newBrick = brickManager.moveToNextBrick();
        brickRotator.setBrick(newBrick);

        movementController.setCurrentOffset(new Point(4, 10));

        return MatrixOperations.intersect(
                boardState.getMatrix(),
                brickRotator.getCurrentShape(),
                (int) movementController.getCurrentOffset().getX(),
                (int) movementController.getCurrentOffset().getY()
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
                (int) movementController.getCurrentOffset()
                        .getX(),
                (int) movementController.getCurrentOffset()
                        .getY(),
                brickManager.getNextBrick().getShapeMatrix().get(0)
        );
    }

    @Override
    public void mergeBrickToBackground() {
        boardState.mergeBrick(
                brickRotator.getCurrentShape(),
                (int) movementController.getCurrentOffset()
                        .getX(),
                (int) movementController.getCurrentOffset()
                        .getY()
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
