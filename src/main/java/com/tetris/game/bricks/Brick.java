/**
 * Base class for all Tetromino shapes, storing shape matrices and rotation
 * states shared by all brick types.
 */
package com.tetris.game.bricks;

import java.util.List;

public interface Brick {

    List<int[][]> getShapeMatrix();
}
