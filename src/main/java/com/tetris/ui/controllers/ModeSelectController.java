package com.tetris.ui.controllers;

import com.tetris.Main;
import com.tetris.game.logic.GameController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.application.Platform;

public class ModeSelectController {

    @FXML private Button marathonButton;
    @FXML private Button timeTrialButton;
    @FXML private Button backButton;

    @FXML
    public void initialize() {
        marathonButton.setOnAction(e -> startMarathon());
        timeTrialButton.setOnAction(e -> startTimeTrial());
        backButton.setOnAction(e -> goBack());
    }

    private void startMarathon() {
        loadGame(false);
    }

    private void startTimeTrial() {
        loadGame(true);
    }

    private void loadGame(boolean timeTrial) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getClassLoader().getResource("gameLayout.fxml")
            );
            Parent root = loader.load();
            GuiController gui = loader.getController();

            Stage stage = (Stage) timeTrialButton.getScene().getWindow();
            Scene scene = new Scene(root, 500, 500);
            stage.setScene(scene);

            // Run AFTER scene is attached
            Platform.runLater(() -> {
                gui.enableTimeTrialMode(timeTrial);
                new GameController(gui);
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getClassLoader().getResource("main_menu.fxml")
            );

            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root, 500, 500));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
