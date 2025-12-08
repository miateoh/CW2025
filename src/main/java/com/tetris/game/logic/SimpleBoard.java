/**
 * Legacy representation of the board retained for compatibility. Most
 * responsibilities are now handled by BoardState and MatrixOperations.
 */
package com.tetris.game.logic;

import com.tetris.ui.views.ViewData;
import com.tetris.game.board.*;
import com.tetris.game.bricks.*;
import com.tetris.game.data.Score;
import com.tetris.game.data.Level;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleBoard implements Board {

    private final int width;
    private final int height;

    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private final BoardState boardState;
    private final BrickMovementController movementController;
    private final BrickManager brickManager;

    private final Score score;
    private final Level level;  // NEW: Level management

    // Hold piece variables
    private Brick heldBrick = null;
    private boolean canHold = true;

    // Queue for multiple next pieces
    private final List<Brick> nextBricksQueue = new ArrayList<>();
    private static final int NEXT_PIECES_COUNT = 3;

    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;

        boardState = new BoardState(width, height);
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        brickManager = new BrickManager(brickGenerator);

        movementController = new BrickMovementController(boardState, brickRotator);

        score = new Score();
        level = new Level();  // NEW: Initialize level

        initializeNextPiecesQueue();
    }

    private void initializeNextPiecesQueue() {
        nextBricksQueue.clear();
        for (int i = 0; i < NEXT_PIECES_COUNT; i++) {
            nextBricksQueue.add(brickManager.moveToNextBrick());
        }
    }

    private Brick getNextBrickFromQueue() {
        if (nextBricksQueue.isEmpty()) {
            initializeNextPiecesQueue();
        }

        Brick nextBrick = nextBricksQueue.remove(0);
        nextBricksQueue.add(brickManager.moveToNextBrick());

        return nextBrick;
    }

    private List<Brick> getNextBricksPreview(int count) {
        List<Brick> preview = new ArrayList<>();
        for (int i = 0; i < Math.min(count, nextBricksQueue.size()); i++) {
            preview.add(nextBricksQueue.get(i));
        }
        return preview;
    }

    @Override
    public boolean createNewBrick() {
        Brick brick = getNextBrickFromQueue();
        brickRotator.setBrick(brick);

        int[][] shape = brickRotator.getCurrentShape();
        int shapeWidth = shape[0].length;

        int startX = (width - shapeWidth) / 2;
        int startY = 0;

        movementController.setCurrentOffset(new Point(startX, startY));

        canHold = true;

        return MatrixOperations.intersect(
                boardState.getMatrix(),
                brickRotator.getCurrentShape(),
                movementController.getCurrentOffset().x,
                movementController.getCurrentOffset().y
        );
    }

    @Override
    public ViewData getViewData() {
        int[][] currentShape = brickRotator.getCurrentShape();
        Point offset = movementController.getCurrentOffset();

        List<int[][]> nextBrickShapes = new ArrayList<>();
        List<Brick> nextBricks = getNextBricksPreview(NEXT_PIECES_COUNT);

        for (Brick brick : nextBricks) {
            nextBrickShapes.add(brick.getShapeMatrix().get(0));
        }

        int[][] holdBrickShape = heldBrick != null ? heldBrick.getShapeMatrix().get(0) : null;
        int ghostY = movementController.getGhostLandingY();

        return new ViewData(
                currentShape,
                offset.x,
                offset.y,
                nextBrickShapes,
                holdBrickShape,
                ghostY
        );
    }

    @Override
    public void newGame() {
        boardState.reset();
        score.reset();
        level.reset();  // NEW: Reset level
        heldBrick = null;
        canHold = true;

        initializeNextPiecesQueue();
        createNewBrick();
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
    public boolean holdPiece() {
        if (!canHold) {
            return false;
        }

        Brick currentBrick = brickRotator.getBrick();

        if (heldBrick == null) {
            heldBrick = currentBrick;
            createNewBrick();
        } else {
            Brick temp = heldBrick;
            heldBrick = currentBrick;

            brickRotator.setBrick(temp);
            int[][] shape = brickRotator.getCurrentShape();
            int shapeWidth = shape[0].length;

            int startX = (width - shapeWidth) / 2;
            int startY = 0;

            movementController.setCurrentOffset(new Point(startX, startY));
        }

        canHold = false;
        return true;
    }

    @Override
    public int[][] getBoardMatrix() {
        return boardState.getMatrix();
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
        ClearRow clearRow = boardState.clearRows();

        if (clearRow.getLinesRemoved() > 0) {
            int previousLevel = level.getCurrentLevel();
            level.addLines(clearRow.getLinesRemoved());

            if (level.justLeveledUp(previousLevel)) {
                score.add(level.getLevelUpBonus());
            }

            score.incrementCombo();
        } else {
            score.breakCombo();
        }

        return clearRow;
    }

    @Override
    public Score getScore() {
        return score;
    }

    // NEW: Get level for UI and speed adjustments
    @Override
    public Level getLevel() {
        return level;
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
