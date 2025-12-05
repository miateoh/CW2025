/**
 * Data transfer object for rendering the current view state.
 * Contains falling brick shape, position, next brick preview, and ghost landing position.
 */
package com.tetris.ui.views;

import com.tetris.game.board.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

public class ViewData {
    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final List<int[][]> nextBricksData;  // Changed to List for multiple pieces
    private final int[][] holdBrickData;
    private final int ghostLandingY;

    public ViewData(
            int[][] brickData,
            int xPosition,
            int yPosition,
            List<int[][]> nextBricksData,  // Now accepts List
            int[][] holdBrickData,
            int ghostLandingY
    ) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBricksData = nextBricksData;
        this.holdBrickData = holdBrickData;
        this.ghostLandingY = ghostLandingY;
    }

    // Constructor for backward compatibility
    public ViewData(
            int[][] brickData,
            int xPosition,
            int yPosition,
            int[][] nextBrickData,  // Single piece
            int[][] holdBrickData,
            int ghostLandingY
    ) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBricksData = new ArrayList<>();
        if (nextBrickData != null) {
            this.nextBricksData.add(MatrixOperations.copy(nextBrickData));
        }
        this.holdBrickData = holdBrickData;
        this.ghostLandingY = ghostLandingY;
    }

    public int[][] getBrickData() { return MatrixOperations.copy(brickData); }
    public int getxPosition() { return xPosition; }
    public int getyPosition() { return yPosition; }

    // Get first next brick (for backward compatibility)
    public int[][] getNextBrickData() {
        return nextBricksData != null && !nextBricksData.isEmpty()
                ? MatrixOperations.copy(nextBricksData.get(0))
                : null;
    }

    // Get all next bricks
    public List<int[][]> getNextBricksData() {
        List<int[][]> copies = new ArrayList<>();
        if (nextBricksData != null) {
            for (int[][] brick : nextBricksData) {
                copies.add(MatrixOperations.copy(brick));
            }
        }
        return copies;
    }

    // Get specific next brick by index
    public int[][] getNextBrickData(int index) {
        if (nextBricksData != null && index >= 0 && index < nextBricksData.size()) {
            return MatrixOperations.copy(nextBricksData.get(index));
        }
        return null;
    }

    public int[][] getHoldBrickData() {
        return holdBrickData != null ? MatrixOperations.copy(holdBrickData) : null;
    }

    public int getGhostLandingY() {
        return ghostLandingY;
    }
}
