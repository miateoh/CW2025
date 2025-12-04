package com.tetris.game.bricks;

import java.util.concurrent.ThreadLocalRandom;

public class BrickFactory {

    public enum BrickType {
        I, L, J, T, O, S, Z
    }

    public static Brick createBrick(BrickType type) {
        return switch(type) {
            case I -> new IBrick();
            case L -> new LBrick();
            case J -> new JBrick();
            case T -> new TBrick();
            case O -> new OBrick();
            case S -> new SBrick();
            case Z -> new ZBrick();
        };
    }

    public static Brick createRandomBrick() {
        BrickType[] types = BrickType.values();
        int index = ThreadLocalRandom.current().nextInt(types.length);
        return createBrick(types[index]);
    }
}