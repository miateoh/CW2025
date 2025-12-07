/**
 * Central coordinator for running the Tetris game.
 *
 * Responsibilities:
 * - Starts and resets games.
 * - Updates game ticks.
 * - Delegates movement logic to BrickMovementController.
 * - Notifies UI about state changes (game over, score updates, brick updates).
 */

package com.tetris.game.logic;

import com.tetris.ui.views.ViewData;
import com.tetris.game.board.Board;
import com.tetris.game.board.ClearRow;
import com.tetris.game.bricks.DownData;
import com.tetris.game.events.EventSource;
import com.tetris.game.events.MoveEvent;
import com.tetris.ui.controllers.GuiController;

public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(10, 25);

    private final GuiController viewGuiController;
    private int previousLevel = 1;  // NEW: Track level changes

    public GameController(GuiController c) {
        viewGuiController = c;
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());

        // NEW: Bind level to UI
        viewGuiController.bindLevel(board.getLevel().levelProperty());

        // NEW: Set initial speed
        viewGuiController.setGameSpeed(board.getLevel().getSpeedMs());
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;

        if (!canMove) {
            board.mergeBrickToBackground();
            clearRow = board.clearRows();

            if (clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus());

                // NEW: Check if level changed and adjust speed
                int currentLevel = board.getLevel().getCurrentLevel();
                if (currentLevel > previousLevel) {
                    previousLevel = currentLevel;
                    int newSpeed = board.getLevel().getSpeedMs();
                    System.out.println("LEVEL UP! New level: " + currentLevel + ", New speed: " + newSpeed + "ms");
                    viewGuiController.setGameSpeed(newSpeed);
                    viewGuiController.showLevelUpNotification(currentLevel);
                }
            }

            if (board.createNewBrick()) {
                viewGuiController.gameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());

        } else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
        }

        return new DownData(clearRow, board.getViewData());
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    @Override
    public void createNewGame() {
        board.newGame();
        previousLevel = 1;  // NEW: Reset level tracking
        viewGuiController.setGameSpeed(board.getLevel().getSpeedMs());  // NEW: Reset speed
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }

    @Override
    public DownData onHardDropEvent(MoveEvent event) {
        DownData data = board.hardDrop();

        // NEW: Check for level-up after hard drop
        int currentLevel = board.getLevel().getCurrentLevel();
        if (currentLevel > previousLevel) {
            previousLevel = currentLevel;
            viewGuiController.setGameSpeed(board.getLevel().getSpeedMs());
            viewGuiController.showLevelUpNotification(currentLevel);
        }

        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        return new DownData(data.getClearRow(), board.getViewData());
    }

    @Override
    public ViewData onHoldEvent(MoveEvent event) {
        boolean success = board.holdPiece();
        if (success) {
            return board.getViewData();
        }
        return board.getViewData();
    }
}