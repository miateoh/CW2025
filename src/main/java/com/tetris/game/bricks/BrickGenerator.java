/**
 * Generates Tetromino pieces using the BrickFactory and manages
 * preview piece sequences.
 */
package com.tetris.game.bricks;

public interface BrickGenerator {

    Brick getBrick();

    Brick getNextBrick();
}
