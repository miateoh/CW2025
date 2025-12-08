package com.tetris.ui.controllers;

import com.tetris.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import com.tetris.sound.SoundManager;

public class MenuController {

    @FXML private Button startButton;
    @FXML private Button scoresButton;
    @FXML private Button quitButton;
    @FXML private Button settingsButton;

    @FXML
    public void initialize() {
        SoundManager.playBGM("background_music.wav");

        settingsButton.setOnAction(e -> {
            SoundManager.play("menu");
            openSettings();
        });

        // -----------------------
        //   CLICK SOUND
        // -----------------------
        startButton.setOnAction(e -> {
            SoundManager.play("menu");
            openModeSelect();
        });

        scoresButton.setOnAction(e -> {
            SoundManager.play("menu");
            openScores();
        });

        quitButton.setOnAction(e -> {
            SoundManager.play("menu");
            System.exit(0);
            SoundManager.stopBGM();
        });
    }

    /** Play menu sound when hovering over a button */
    private void addHoverSound(Button btn) {
        btn.setOnMouseEntered(e -> SoundManager.play("menu"));
    }

    private void openModeSelect() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getClassLoader().getResource("mode_select.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) startButton.getScene().getWindow();
            stage.setScene(new Scene(root, 700, 700));

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

            Stage stage = (Stage) scoresButton.getScene().getWindow();
            stage.setScene(new Scene(root, 700, 700));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void openSettings() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getClassLoader().getResource("settings.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) settingsButton.getScene().getWindow();
            stage.setScene(new Scene(root, 700, 700));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
