package com.tetris.game.bricks;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BrickFactoryTest {

    @Test
    void testCreateBrickReturnsCorrectType() {
        Brick iBrick = BrickFactory.createBrick(BrickFactory.BrickType.I);
        assertTrue(iBrick instanceof IBrick);
    }

    @Test
    void testCreateRandomBrickReturnsNonNull() {
        Brick randomBrick = BrickFactory.createRandomBrick();
        assertNotNull(randomBrick);
        assertNotNull(randomBrick.getShapeMatrix());
    }

    @Test
    void testAllBrickTypesCanBeCreated() {
        for (BrickFactory.BrickType type : BrickFactory.BrickType.values()) {
            Brick brick = BrickFactory.createBrick(type);
            assertNotNull(brick);
        }
    }
}