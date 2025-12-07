package com.tetris.ui.controllers;

import com.tetris.Main;
import com.tetris.game.logic.GameController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class SprintSelectController {

    @FXML private Button sprint10;
    @FXML private Button sprint20;
    @FXML private Button sprint40;
    @FXML private Button backButton;

    @FXML
    public void initialize() {
        sprint10.setOnAction(e -> loadGame(10));
        sprint20.setOnAction(e -> loadGame(20));
        sprint40.setOnAction(e -> loadGame(40));

        backButton.setOnAction(e -> goBack());
    }

    private void loadGame(int targetLines) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("gameLayout.fxml"));
            Parent root = loader.load();

            GuiController gui = loader.getController();
            gui.enableSprintMode(targetLines);

            Stage stage = (Stage) sprint10.getScene().getWindow();
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
            FXMLLoader loader = new FXMLLoader(Main.class.getClassLoader().getResource("mode_select.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root, 500, 500));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
