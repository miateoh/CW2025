/**
 * Model class storing the player's score using a JavaFX IntegerProperty.
 *
 * Supports:
 * - Incrementing score.
 * - Resetting score.
 * - Binding score to UI components.
 */
package com.tetris.game.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final IntegerProperty combo = new SimpleIntegerProperty(0);

    private static final int COMBO_BONUS = 50;  // Bonus per combo level

    public IntegerProperty scoreProperty() {
        return score;
    }

    public IntegerProperty comboProperty() {
        return combo;
    }

    public void add(int i) {
        score.setValue(score.getValue() + i);
    }

    public int getCombo() {
        return combo.get();
    }

    /**
     * Increments the combo counter when lines are cleared
     */
    public void incrementCombo() {
        combo.setValue(combo.getValue() + 1);
    }

    /**
     * Breaks the combo (resets to 0) when a piece locks without clearing lines
     */
    public void breakCombo() {
        combo.setValue(0);
    }

    /**
     * Calculate and add combo bonus to score
     * Returns the bonus amount for notification
     */
    public int addComboBonus() {
        if (combo.get() > 1) {
            int bonus = combo.get() * COMBO_BONUS;
            add(bonus);
            return bonus;
        }
        return 0;
    }

    public void reset() {
        score.setValue(0);
        combo.setValue(0);
    }
}
