package com.tetris.game.logic;

import com.tetris.game.events.MoveEvent;
import com.tetris.ui.views.ViewData;
import com.tetris.game.bricks.DownData;

public interface InputEventListener {

    DownData onDownEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    void createNewGame();
}
