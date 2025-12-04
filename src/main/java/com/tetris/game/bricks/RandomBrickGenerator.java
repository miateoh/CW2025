package com.tetris.game.bricks;

import java.util.ArrayDeque;
import java.util.Deque;

public class RandomBrickGenerator implements BrickGenerator {

    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    public RandomBrickGenerator() {
        // Use BrickFactory instead of manual instantiation
        nextBricks.add(BrickFactory.createRandomBrick());
        nextBricks.add(BrickFactory.createRandomBrick());
    }

    @Override
    public Brick getBrick() {
        if (nextBricks.size() <= 1) {
            nextBricks.add(BrickFactory.createRandomBrick());
        }
        return nextBricks.poll();
    }

    @Override
    public Brick getNextBrick() {
        return nextBricks.peek();
    }
}