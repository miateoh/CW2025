/**
 * High-level wrapper for board-related helpers, supporting interactions
 * between board logic and other components like BoardState.
 */
package com.tetris.game.board;

import com.tetris.game.data.Score;
import com.tetris.game.data.Level;
import com.tetris.ui.views.ViewData;
import com.tetris.game.bricks.DownData;

public interface Board {

    boolean moveBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    boolean createNewBrick();

    int[][] getBoardMatrix();

    ViewData getViewData();

    void mergeBrickToBackground();

    ClearRow clearRows();

    Score getScore();

    Level getLevel();

    void newGame();

    DownData hardDrop();

    boolean holdPiece();
}

