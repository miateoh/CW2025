/**
 * Displays and manages the high score screen, loading persistent results
 * across all game modes from the HighScoreManager.
 */

package com.tetris.ui.controllers;

import com.tetris.Main;
import com.tetris.game.data.HighScoreManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class HighScoresController {

    @FXML private VBox scoresList;
    @FXML private Button backButton;

    private final HighScoreManager highScoreManager = new HighScoreManager();

    @FXML
    public void initialize() {
        loadScores();

        backButton.setOnAction(e -> goBack());
    }

    private void loadScores() {
        scoresList.getChildren().clear();

        List<Integer> scores = highScoreManager.getHighScores();

        if (scores.isEmpty()) {
            Label emptyLabel = new Label("No high scores yet!");
            emptyLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18;");
            scoresList.getChildren().add(emptyLabel);
            return;
        }

        int rank = 1;
        for (int score : scores) {
            Label label = new Label(rank + ". " + score);
            label.setStyle("-fx-text-fill: white; -fx-font-size: 20;");
            scoresList.getChildren().add(label);
            rank++;
        }
    }

    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getClassLoader().getResource("main_menu.fxml")
            );

            Parent root = loader.load();
            Stage stage = (Stage) scoresList.getScene().getWindow();
            stage.setScene(new Scene(root, 700, 700));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
