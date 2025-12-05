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

    // Hold piece variables
    private Brick heldBrick = null;
    private boolean canHold = true;  // Resets when piece locks

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

        int startX = (width - shapeWidth) / 2;
        int startY = 0;

        movementController.setCurrentOffset(new Point(startX, startY));

        // Reset hold ability for new piece
        canHold = true;

        return MatrixOperations.intersect(
                boardState.getMatrix(),
                brickRotator.getCurrentShape(),
                movementController.getCurrentOffset().x,
                movementController.getCurrentOffset().y
        );
    }

    @Override
    public boolean holdPiece() {
        // Can only hold once per piece
        if (!canHold) {
            return false;
        }

        Brick currentBrick = brickRotator.getBrick();

        if (heldBrick == null) {
            // First hold - store current and spawn next
            heldBrick = currentBrick;
            createNewBrick();
        } else {
            // Swap current with held
            Brick temp = heldBrick;
            heldBrick = currentBrick;

            // Spawn the previously held brick
            brickRotator.setBrick(temp);
            int[][] shape = brickRotator.getCurrentShape();
            int shapeWidth = shape[0].length;

            int startX = (width - shapeWidth) / 2;
            int startY = 0;

            movementController.setCurrentOffset(new Point(startX, startY));
        }

        // Disable hold until piece locks
        canHold = false;
        return true;
    }

    @Override
    public int[][] getBoardMatrix() {
        return boardState.getMatrix();
    }

    @Override
    public ViewData getViewData() {

        int[][] currentShape = brickRotator.getCurrentShape();
        Point offset = movementController.getCurrentOffset();

        int[][] nextBrickShape = brickManager.getNextBrick().getShapeMatrix().get(0);
        int[][] holdBrickShape = heldBrick != null ? heldBrick.getShapeMatrix().get(0) : null;

        int ghostY = movementController.getGhostLandingY(); // FIX

        return new ViewData(
                currentShape,
                offset.x,
                offset.y,
                nextBrickShape,
                holdBrickShape,
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
    public Score getScore() {
        return score;
    }

    @Override
    public void newGame() {
        boardState.reset();
        score.reset();
        heldBrick = null;  // Clear held piece
        canHold = true;
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

        ViewData viewData = getViewData();

        return new DownData(clearRow, viewData);
    }
}
