/**
 * Handles persistent storage and retrieval of high scores for all
 * game modes, ensuring sorted and validated score data.
 */
package com.tetris.game.data;

import java.io.*;
import java.util.*;

public class HighScoreManager {
    private static final String HIGH_SCORE_FILE = "highscores.txt";
    private static final int MAX_SCORES = 5;
    private List<Integer> highScores;

    public HighScoreManager() {
        highScores = new ArrayList<>();
        loadHighScores();
    }

    public List<Integer> getHighScores() {
        return new ArrayList<>(highScores);
    }

    public boolean isHighScore(int score) {
        if (highScores.size() < MAX_SCORES) return true;
        return score > highScores.get(highScores.size() - 1);
    }

    public void addScore(int score) {
        if (!isHighScore(score)) return;

        highScores.add(score);
        highScores.sort(Collections.reverseOrder());

        if (highScores.size() > MAX_SCORES) {
            highScores = highScores.subList(0, MAX_SCORES);
        }

        saveHighScores();
    }

    private void loadHighScores() {
        File file = new File(HIGH_SCORE_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null && highScores.size() < MAX_SCORES) {
                try {
                    int score = Integer.parseInt(line.trim());
                    highScores.add(score);
                } catch (NumberFormatException e) {
                    // Skip invalid lines
                }
            }
            highScores.sort(Collections.reverseOrder());
        } catch (IOException e) {
            System.err.println("Error loading high scores: " + e.getMessage());
        }
    }

    private void saveHighScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HIGH_SCORE_FILE))) {
            for (int score : highScores) {
                writer.write(String.valueOf(score));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving high scores: " + e.getMessage());
        }
    }

    public void resetHighScores() {
        highScores.clear();
        saveHighScores();
    }
}