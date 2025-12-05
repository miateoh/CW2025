package com.tetris.game.board;

import com.tetris.game.data.Score;
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

    void newGame();

    DownData hardDrop();
}
