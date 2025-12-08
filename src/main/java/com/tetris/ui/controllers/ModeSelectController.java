package com.tetris.ui.controllers;

import com.tetris.Main;
import com.tetris.game.logic.GameController;
import com.tetris.sound.SoundManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ModeSelectController {

    @FXML private Button marathonButton;
    @FXML private Button timeTrialButton;
    @FXML private Button sprintButton;
    @FXML private Button backButton;
    @FXML private javafx.scene.layout.StackPane modeRoot;

    @FXML
    public void initialize() {

        marathonButton.setOnAction(e -> loadGame(false, false, 0));  // normal mode
        timeTrialButton.setOnAction(e -> loadGame(true, false, 0));  // time trial
        sprintButton.setOnAction(e -> openSprintMenu());
        backButton.setOnAction(e -> goBack());
    }

    private void openSprintMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getClassLoader().getResource("sprint_select.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) sprintButton.getScene().getWindow();
            stage.setScene(new Scene(root, 500, 500));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadGame(boolean timeTrial, boolean sprintMode, int sprintTarget) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("gameLayout.fxml"));
            Parent root = loader.load();

            GuiController gui = loader.getController();
            gui.setGameMode(timeTrial, sprintMode, sprintTarget);

            Stage stage = (Stage) modeRoot.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
            stage.setFullScreenExitHint("");
            stage.setFullScreen(stage.isFullScreen());

            new GameController(gui);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getClassLoader().getResource("main_menu.fxml"));

            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root, 500, 500));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
