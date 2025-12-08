/**
 * Manages game level progression and difficulty scaling.
 *
 * Features:
 * - Level increases every 10 lines cleared
 * - Speed increases with each level
 * - Level-up bonuses
 * - Classic Tetris difficulty curve
 */
package com.tetris.game.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public final class Level {

    private static final int LINES_PER_LEVEL = 10;
    private static final int BASE_SPEED_MS = 600;      // Starting speed (level 1)
    private static final int MIN_SPEED_MS = 80;         // Fastest speed (level 10+)
    private static final int SPEED_DECREASE_PER_LEVEL = 80;  // Speed increase per level

    private final IntegerProperty level = new SimpleIntegerProperty(1);
    private final IntegerProperty linesCleared = new SimpleIntegerProperty(0);

    public Level() {
        // Auto-update level when lines cleared changes
        linesCleared.addListener((obs, oldVal, newVal) -> {
            int newLevel = (newVal.intValue() / LINES_PER_LEVEL) + 1;
            if (newLevel != level.get()) {
                level.set(newLevel);
            }
        });
    }

    /**
     * Add lines cleared and automatically update level
     */
    public void addLines(int lines) {
        linesCleared.set(linesCleared.get() + lines);
    }

    /**
     * Get current level
     */
    public int getCurrentLevel() {
        return level.get();
    }

    /**
     * Get total lines cleared
     */
    public int getTotalLinesCleared() {
        return linesCleared.get();
    }

    /**
     * Get lines needed for next level
     */
    public int getLinesUntilNextLevel() {
        int currentLines = linesCleared.get();
        int nextLevelThreshold = level.get() * LINES_PER_LEVEL;
        return nextLevelThreshold - currentLines;
    }

    /**
     * Calculate fall speed in milliseconds based on current level
     * Speed gets faster as level increases
     */
    public int getSpeedMs() {
        int speed = BASE_SPEED_MS - ((level.get() - 1) * SPEED_DECREASE_PER_LEVEL);
        return Math.max(speed, MIN_SPEED_MS);
    }

    /**
     * Get level-up bonus points
     */
    public int getLevelUpBonus() {
        return level.get() * 100;  // 100 points per level
    }

    /**
     * Check if player just leveled up (for notifications)
     */
    public boolean justLeveledUp(int previousLevel) {
        return level.get() > previousLevel;
    }

    /**
     * Reset to level 1
     */
    public void reset() {
        level.set(1);
        linesCleared.set(0);
    }

    /**
     * Property for UI binding
     */
    public IntegerProperty levelProperty() {
        return level;
    }

    /**
     * Property for lines cleared UI binding
     */
    public IntegerProperty linesClearedProperty() {
        return linesCleared;
    }
}