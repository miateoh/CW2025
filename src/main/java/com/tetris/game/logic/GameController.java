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

    public GameController(GuiController c) {
        viewGuiController = c;
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
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
        // Updated to include ghost Y (via SimpleBoard's getViewData)
        return new DownData(clearRow, board.getViewData());
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();  // Now includes ghost Y
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();  // Now includes ghost Y
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();  // Now includes ghost Y
    }

    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }

    public DownData onHardDropEvent(MoveEvent event) {
        DownData data = board.hardDrop();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        // Updated to include ghost Y for new brick
        return new DownData(data.getClearRow(), board.getViewData());
    }

}