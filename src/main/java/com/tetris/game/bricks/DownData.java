/**
 * Bundles the results of vertical movement operations, including updated
 * ViewData and scoring information from soft or hard drops.
 */
package com.tetris.game.bricks;

import com.tetris.ui.views.ViewData;
import com.tetris.game.board.ClearRow;

public final class DownData {
    private final ClearRow clearRow;
    private final ViewData viewData;

    public DownData(ClearRow clearRow, ViewData viewData) {
        this.clearRow = clearRow;
        this.viewData = viewData;
    }

    public ClearRow getClearRow() {
        return clearRow;
    }

    public ViewData getViewData() {
        return viewData;
    }
}
