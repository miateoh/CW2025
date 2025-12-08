/**
 * Displays the game over summary panel, including score,
 * lines cleared, and options to retry or return to the menu.
 */
package com.tetris.ui.views;

import com.tetris.sound.SoundManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class GameOverPanel extends BorderPane {

    private Button restartButton;
    private Button resetScoresButton; // NEW
    private Button mainMenuButton;

    // High score display components
    private Label finalScoreLabel;
    private VBox highScoreBox;
    private Label highScoreTitle;
    private VBox highScoreContainer;

    private Label sprintTitleLabel;
    private Label sprintInfoLabel;
    private VBox contentBox;

    public GameOverPanel() {
        // Game Over Label
        final Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.setStyle(
                "-fx-text-fill: #2FE4FF;"
                        + "-fx-font-size: 42px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-effect: dropshadow(gaussian, #2FE4FF, 20, 0.4, 0, 0);"
        );
        gameOverLabel.setTextAlignment(TextAlignment.CENTER);

        // Final Score Display
        finalScoreLabel = new Label("Final Score: 0");
        finalScoreLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 24px; -fx-font-weight: bold;");
        finalScoreLabel.setTextAlignment(TextAlignment.CENTER);

        // High Scores Title
        highScoreTitle = new Label("HIGH SCORES");
        highScoreTitle.setStyle(
                "-fx-text-fill: #2FE4FF;"
                        + "-fx-font-size: 20px;"
                        + "-fx-font-weight: bold;"
        );
        highScoreTitle.setTextAlignment(TextAlignment.CENTER);

        // High Scores Container
        highScoreBox = new VBox(5);
        highScoreBox.setAlignment(Pos.CENTER);
        highScoreBox.setPrefWidth(150);

        highScoreContainer = new VBox(10);
        highScoreContainer.setAlignment(Pos.CENTER);
        highScoreContainer.setPrefWidth(200);
        highScoreContainer.getChildren().addAll(highScoreTitle, highScoreBox);
        highScoreContainer.setVisible(false);

        sprintTitleLabel = new Label("SPRINT COMPLETE!");
        sprintTitleLabel.setStyle("-fx-text-fill: #00eaff; -fx-font-size: 34px; -fx-font-weight: bold;");
        sprintTitleLabel.setTextAlignment(TextAlignment.CENTER);
        sprintTitleLabel.setVisible(false);

        sprintInfoLabel = new Label("");
        sprintInfoLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
        sprintInfoLabel.setTextAlignment(TextAlignment.CENTER);
        sprintInfoLabel.setVisible(false);

        // Restart Button
        restartButton = createStyledButton("RESTART", "#4CAF50", "#45a049");

        // NEW: Reset Scores Button (smaller, red accent)
        resetScoresButton = createStyledButton("Reset Scores", "#666", "#ff6666");
        resetScoresButton.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 5 15;" +
                        "-fx-background-color: transparent;" +
                        "-fx-text-fill: #666;" +
                        "-fx-border-color: #666;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 3;" +
                        "-fx-cursor: hand;"
        );

        resetScoresButton.setOnMouseEntered(e ->
                resetScoresButton.setStyle(
                        "-fx-font-size: 12px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 5 15;" +
                                "-fx-background-color: transparent;" +
                                "-fx-text-fill: #ff6666;" +
                                "-fx-border-color: #ff6666;" +
                                "-fx-border-width: 1;" +
                                "-fx-border-radius: 3;" +
                                "-fx-cursor: hand;"
                )
        );
        resetScoresButton.setOnMouseExited(e ->
                resetScoresButton.setStyle(
                        "-fx-font-size: 12px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 5 15;" +
                                "-fx-background-color: transparent;" +
                                "-fx-text-fill: #666;" +
                                "-fx-border-color: #666;" +
                                "-fx-border-width: 1;" +
                                "-fx-border-radius: 3;" +
                                "-fx-cursor: hand;"
                )
        );

        // Hint label
        Label hintLabel = new Label("(or press R)");
        hintLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 12px;");
        hintLabel.setTextAlignment(TextAlignment.CENTER);

        mainMenuButton = createStyledButton("MAIN MENU", "#2196F3", "#1976D2");

// NEW: Button layout
        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER);

// Restart button alone
        buttonBox.getChildren().add(restartButton);

// Main Menu button full width
        mainMenuButton.setPrefWidth(180);
        buttonBox.getChildren().add(mainMenuButton);

// Reset scores button below main menu
        resetScoresButton.setPrefWidth(180);
        buttonBox.getChildren().add(resetScoresButton);


        // Layout
        contentBox = new VBox(8);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(0, 20, 10, 20));
        contentBox.setPrefWidth(Double.MAX_VALUE);
        contentBox.getChildren().addAll(
                gameOverLabel,
                sprintTitleLabel,
                finalScoreLabel,
                sprintInfoLabel,
                highScoreContainer,
                buttonBox,
                hintLabel
        );

        // Set at top of BorderPane
        setCenter(contentBox);
        setBottom(null);
        BorderPane.setMargin(contentBox, new Insets(0, 0, 0, 0));

        // Semi-transparent background
        setStyle(
                "-fx-background-color: rgba(0,0,0,0.80);"
                        + "-fx-border-color: rgba(0,255,255,0.4);"
                        + "-fx-border-width: 3;"
                        + "-fx-border-radius: 12;"
                        + "-fx-background-radius: 12;"
        );

        setPrefSize(600, 500);
    }

    private Button createStyledButton(String text, String color, String hoverColor) {
        Button button = new Button(text);

        if (text.equals("RESTART")) {
            // Restart button style
            String baseStyle =
                    "-fx-font-size: 20px;"
                            + "-fx-font-weight: bold;"
                            + "-fx-padding: 6 25;"
                            + "-fx-background-color: rgba(0,0,0,0.6);"
                            + "-fx-border-color: #2FE4FF;"
                            + "-fx-border-width: 3;"
                            + "-fx-background-radius: 12;"
                            + "-fx-border-radius: 12;"
                            + "-fx-text-fill: white;"
                            + "-fx-effect: dropshadow(gaussian, #2FE4FF, 15, 0.4, 0, 0);";

            button.setStyle(baseStyle);
            button.setPrefWidth(220);

            button.setOnMouseEntered(e ->
                    button.setStyle(baseStyle.replace(color, hoverColor))
            );
            button.setOnMouseExited(e ->
                    button.setStyle(baseStyle.replace(hoverColor, color))
            );
        } else {
            // Reset button style
            String baseStyle =
                    "-fx-font-size: 20px;"
                            + "-fx-font-weight: bold;"
                            + "-fx-padding: 6 25;"
                            + "-fx-background-color: rgba(0,0,0,0.6);"
                            + "-fx-border-color: #2FE4FF;"
                            + "-fx-border-width: 3;"
                            + "-fx-background-radius: 12;"
                            + "-fx-border-radius: 12;"
                            + "-fx-text-fill: white;"
                            + "-fx-effect: dropshadow(gaussian, #2FE4FF, 15, 0.4, 0, 0);";

            button.setStyle(baseStyle);
            button.setPrefWidth(120);

            button.setOnMouseEntered(e ->
                    button.setStyle(baseStyle.replace("0.6", "0.9"))
            );
            button.setOnMouseExited(e ->
                    button.setStyle(baseStyle)
            );
        }

        return button;
    }

    public void updateHighScores(java.util.List<Integer> highScores, int currentScore) {
        // Update final score
        finalScoreLabel.setText("Final Score: " + currentScore);

        // Clear previous high scores
        highScoreBox.getChildren().clear();

        // Show the high score container
        highScoreContainer.setVisible(true);
        highScoreTitle.setVisible(true);

        // Check if current score is a high score
        boolean isNewHighScore = false;
        if (!highScores.isEmpty()) {
            isNewHighScore = currentScore > highScores.get(0);
        }

        if (isNewHighScore) {
            finalScoreLabel.setStyle("-fx-text-fill: #FF4500; -fx-font-size: 26px; -fx-font-weight: bold; -fx-alignment: center;");
        }

        // Display high scores
        for (int i = 0; i < Math.min(highScores.size(), 5); i++) {
            int score = highScores.get(i);
            Label scoreLabel = new Label((i + 1) + ". " + score);
            scoreLabel.setPrefWidth(120);
            scoreLabel.setTextAlignment(TextAlignment.CENTER);

            // Style based on position
            String style = "-fx-alignment: center; -fx-font-size: ";
            if (i == 0) {
                style += "16px; -fx-font-weight: bold; -fx-text-fill: #FFD700;";
            } else if (i == 1) {
                style += "15px; -fx-text-fill: #C0C0C0;";
            } else if (i == 2) {
                style += "15px; -fx-text-fill: #CD7F32;";
            } else {
                style += "14px; -fx-text-fill: white;";
            }

            // Highlight current score if it's in the list
            if (score == currentScore) {
                style += " -fx-underline: true; -fx-font-weight: bold;";
            }

            scoreLabel.setStyle(style);

            // Center the label
            HBox centeredRow = new HBox();
            centeredRow.setAlignment(Pos.CENTER);
            centeredRow.setPrefWidth(150);
            centeredRow.getChildren().add(scoreLabel);

            highScoreBox.getChildren().add(centeredRow);
        }

        // If no high scores yet, show a message
        if (highScores.isEmpty()) {
            Label noScoresLabel = new Label("No high scores yet!");
            noScoresLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 14px; -fx-alignment: center;");
            noScoresLabel.setPrefWidth(150);
            noScoresLabel.setTextAlignment(TextAlignment.CENTER);

            HBox centeredMessage = new HBox();
            centeredMessage.setAlignment(Pos.CENTER);
            centeredMessage.setPrefWidth(150);
            centeredMessage.getChildren().add(noScoresLabel);

            highScoreBox.getChildren().add(centeredMessage);
        }
    }

    public void clearHighScoresDisplay() {
        highScoreBox.getChildren().clear();
        highScoreContainer.setVisible(false);
        finalScoreLabel.setText("Final Score: 0");
        finalScoreLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 24px; -fx-font-weight: bold; -fx-alignment: center;");
    }

    // ORIGINAL: Sets the action for the restart button
    public void setOnRestart(Runnable action) {
        restartButton.setOnAction(e -> {
            SoundManager.play("restart");   //  play restart sound
            action.run();                   // run actual restart logic
        });
    }

    // NEW: Sets the action for the reset scores button
    public void setOnResetScores(Runnable action) {
        resetScoresButton.setOnAction(e -> {
            // Optional: Add confirmation dialog here
            action.run();
        });
    }

    // ORIGINAL: Gets the restart button
    public Button getRestartButton() {
        return restartButton;
    }

    // NEW: Gets the reset scores button
    public Button getResetScoresButton() {
        return resetScoresButton;
    }

    public Label getFinalScoreLabel() {
        return finalScoreLabel;
    }

    public void hideGameOverTitle() {
        // GAME OVER label is the FIRST element inside contentBox
        Label gameOverLabel = (Label) contentBox.getChildren().get(0);
        gameOverLabel.setVisible(false);
    }

    public void showSprintResult(double timeSeconds, int targetLines, int finalScore) {

        finalScoreLabel.setVisible(true);                 // <-- ADD
        finalScoreLabel.setText("Final Score: " + finalScore);   // <-- ADD

        finalScoreLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 24px; -fx-font-weight: bold;");

        // Hide marathon UI
        highScoreContainer.setVisible(false);
        resetScoresButton.setVisible(false);

        // Show sprint UI
        sprintTitleLabel.setVisible(true);
        sprintInfoLabel.setVisible(true);

        sprintTitleLabel.setText("SPRINT COMPLETE!");
        sprintInfoLabel.setText(
                "Cleared " + targetLines + " lines\n" +
                        "Score: " + finalScore + "\n" +
                        "Time: " + String.format("%.2f seconds", timeSeconds)
        );

        restartButton.setText("PLAY AGAIN");
    }

    public void showSprintFailure(int linesCleared, int finalScore, double timeSeconds) {

        finalScoreLabel.setVisible(true);                         // <-- ADD
        finalScoreLabel.setText("Final Score: " + finalScore);    // <-- ADD

        sprintTitleLabel.setVisible(false);
        highScoreContainer.setVisible(false);
        resetScoresButton.setVisible(false);

        sprintInfoLabel.setVisible(true);
        sprintInfoLabel.setText(
                "Cleared " + linesCleared + " lines\n" +
                        "Score: " + finalScore + "\n" +
                        "Time: " + String.format("%.2f seconds", timeSeconds)
        );

        restartButton.setText("TRY AGAIN");
    }

    public void centerContent() {
        this.setTop(null);      // remove top placement
        this.setCenter(contentBox);  // center entire panel
    }

    public void setOnMainMenu(Runnable action) {
        mainMenuButton.setOnAction(e -> action.run());
    }

}