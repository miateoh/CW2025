package com.tetris.game.logic;

import com.tetris.ui.views.ViewData;
import com.tetris.game.board.*;
import com.tetris.game.bricks.*;
import com.tetris.game.data.Score;

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

        int shapeWidth = shape[0].length;

        int startX = (width - shapeWidth) / 2;  // center horizontally
        int startY = 0;                         // top

        movementController.setCurrentOffset(new Point(startX, startY));

        return MatrixOperations.intersect(
                boardState.getMatrix(),
                brickRotator.getCurrentShape(),
                movementController.getCurrentOffset().x,
                movementController.getCurrentOffset().y
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
                movementController.getCurrentOffset().x,
                movementController.getCurrentOffset().y,
                brickManager.getNextBrick().getShapeMatrix().get(0)
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
    public Score getScore() {
        return score;
    }

    @Override
    public void newGame() {
        boardState.reset();
        score.reset();
        createNewBrick();
    }

    @Override
    public DownData hardDrop() {

        int distance = 0;

        // Move down until a collision stops us
        while (moveBrickDown()) {
            distance++;
        }

        // Lock the piece
        mergeBrickToBackground();

        // Clear rows
        ClearRow clearRow = clearRows();
        if (clearRow.getLinesRemoved() > 0) {
            score.add(clearRow.getScoreBonus());
        }

        // Hard drop scoring bonus
        score.add(distance * 2);

        // Spawn next brick
        boolean gameOver = createNewBrick();

        ViewData viewData = getViewData();

        return new DownData(clearRow, viewData);
    }
}
