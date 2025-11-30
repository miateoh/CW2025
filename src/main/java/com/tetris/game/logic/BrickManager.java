package com.tetris.game.logic;

import com.tetris.game.bricks.Brick;
import com.tetris.game.bricks.BrickGenerator;

public class BrickManager {

    private final BrickGenerator brickGenerator;

    private Brick currentBrick;
    private Brick nextBrick;

    public BrickManager(BrickGenerator brickGenerator) {
        this.brickGenerator = brickGenerator;

        // Initialize first two bricks
        this.currentBrick = brickGenerator.getBrick();
        this.nextBrick = brickGenerator.getBrick();
    }

    public Brick getCurrentBrick() {
        return currentBrick;
    }

    public Brick getNextBrick() {
        return nextBrick;
    }

    /**
     * Moves next → current and generates a new next brick.
     * Called every time a new brick spawns.
     */
    public Brick moveToNextBrick() {
        currentBrick = nextBrick;
        nextBrick = brickGenerator.getBrick();
        return currentBrick;
    }
}
