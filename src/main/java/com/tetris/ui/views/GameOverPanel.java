package com.tetris.ui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class GameOverPanel extends BorderPane {

    private Button restartButton;

    public GameOverPanel() {
        // Game Over Label
        final Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");

        // Restart Button
        restartButton = new Button("RESTART");
        restartButton.getStyleClass().add("restartButton");
        restartButton.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 30;" +
                        "-fx-background-color: #4CAF50;" +
                        "-fx-text-fill: white;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-radius: 5;"
        );

        // Hover effect
        restartButton.setOnMouseEntered(e ->
                restartButton.setStyle(
                        "-fx-font-size: 18px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 10 30;" +
                                "-fx-background-color: #45a049;" +
                                "-fx-text-fill: white;" +
                                "-fx-cursor: hand;" +
                                "-fx-background-radius: 5;"
                )
        );
        restartButton.setOnMouseExited(e ->
                restartButton.setStyle(
                        "-fx-font-size: 18px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 10 30;" +
                                "-fx-background-color: #4CAF50;" +
                                "-fx-text-fill: white;" +
                                "-fx-cursor: hand;" +
                                "-fx-background-radius: 5;"
                )
        );

        // Hint label
        Label hintLabel = new Label("(or press R)");
        hintLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 12px;");

        // Layout
        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(20));
        contentBox.getChildren().addAll(gameOverLabel, restartButton, hintLabel);

        setCenter(contentBox);

        // Semi-transparent background
        setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
    }

    //Sets the action for the restart button
    public void setOnRestart(Runnable action) {
        restartButton.setOnAction(e -> action.run());
    }


    //Gets the restart button
    public Button getRestartButton() {
        return restartButton;
    }
}