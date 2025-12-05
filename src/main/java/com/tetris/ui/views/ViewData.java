/**
 * Data transfer object for rendering the current view state.
 * Contains falling brick shape, position, next brick preview, and ghost landing position.
 */
package com.tetris.ui.views;

import com.tetris.game.board.MatrixOperations;

public class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;
    private final int ghostLandingY;

    public ViewData(int[][] brickData, int xPosition, int yPosition,
                    int[][] nextBrickData, int ghostLandingY) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.ghostLandingY = ghostLandingY;
    }

    public int[][] getBrickData() { return MatrixOperations.copy(brickData); }
    public int getxPosition() { return xPosition; }
    public int getyPosition() { return yPosition; }
    public int[][] getNextBrickData() { return MatrixOperations.copy(nextBrickData); }
    public int getGhostLandingY() { return ghostLandingY; }
}