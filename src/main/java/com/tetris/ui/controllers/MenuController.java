package com.tetris.ui.controllers;

import com.tetris.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MenuController {

    @FXML private Button startButton;
    @FXML private Button scoresButton;
    @FXML private Button quitButton;

    @FXML
    public void initialize() {
        startButton.setOnAction(e -> startGame());
        quitButton.setOnAction(e -> System.exit(0));

        scoresButton.setOnAction(e -> openScores());
    }

    private void startGame() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getClassLoader().getResource("gameLayout.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) startButton.getScene().getWindow();
            stage.setScene(new Scene(root, 500, 500));

            // Create GameController AFTER switching scene
            com.tetris.game.logic.GameController gc =
                    new com.tetris.game.logic.GameController(
                            loader.getController()
                    );

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void openScores() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getClassLoader().getResource("high_scores.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) startButton.getScene().getWindow();
            stage.setScene(new Scene(root, 700, 700));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
