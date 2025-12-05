package com.tetris.game.logic;

import com.tetris.ui.views.ViewData;
import com.tetris.game.board.*;
import com.tetris.game.bricks.*;
import com.tetris.game.data.Score;

import java.awt.Point;

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
    public boolean moveBrickDown() { return movementController.moveDown(); }

    @Override
    public boolean moveBrickLeft() { return movementController.moveLeft(); }

    @Override
    public boolean moveBrickRight() { return movementController.moveRight(); }

    @Override
    public boolean rotateLeftBrick() { return movementController.rotateLeft(); }

    @Override
    public boolean createNewBrick() {
        Brick brick = brickManager.moveToNextBrick();
        brickRotator.setBrick(brick);

        int[][] shape = brickRotator.getCurrentShape();

        int startX = (width - shape[0].length) / 2;
        movementController.setCurrentOffset(new Point(startX, 0));

        return MatrixOperations.intersect(
                boardState.getMatrix(),
                shape,
                startX,
                0
        );
    }

    @Override
    public int[][] getBoardMatrix() {
        return boardState.getMatrix();
    }

    @Override
    public ViewData getViewData() {
        int[][] currentShape = brickRotator.getCurrentShape();
        Point offset = movementController.getCurrentOffset();
        int[][] nextShape = brickManager.getNextBrick().getShapeMatrix().get(0);

        int ghostY = movementController.getGhostLandingY();

        return new ViewData(
                currentShape,
                offset.x,
                offset.y,
                nextShape,
                ghostY
        );
    }

    @Override
    public void mergeBrickToBackground() {
        boardState.mergeBrick(
                brickRotator.getCurrentShape(),
                movementController.getCurrentOffset().x,
                movementController.getCurrentOffset().y
        );
    }

    @Override
    public ClearRow clearRows() {
        return boardState.clearRows();
    }

    @Override
    public Score getScore() { return score; }

    @Override
    public void newGame() {
        boardState.reset();
        score.reset();
        createNewBrick();
    }

    @Override
    public DownData hardDrop() {
        int distance = 0;

        while (moveBrickDown()) {
            distance++;
        }

        mergeBrickToBackground();

        ClearRow clearRow = clearRows();
        if (clearRow.getLinesRemoved() > 0) {
            score.add(clearRow.getScoreBonus());
        }

        score.add(distance * 2);

        boolean gameOver = createNewBrick();

        return new DownData(clearRow, getViewData());
    }
}

