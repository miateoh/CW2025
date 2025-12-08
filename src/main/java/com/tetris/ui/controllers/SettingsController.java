/**
 * Controls the settings screen, allowing the player to adjust audio options
 * such as music and sound effect volume.
 */
package com.tetris.ui.controllers;

import com.tetris.Main;
import com.tetris.sound.SoundManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class SettingsController {

    @FXML private Slider bgmSlider;
    @FXML private Slider sfxSlider;

    @FXML private CheckBox bgmMute;
    @FXML private CheckBox sfxMute;

    @FXML private Button backButton;

    @FXML
    public void initialize() {

        // === Load current values ===
        bgmSlider.setValue(SoundManager.getBGMVolume());
        sfxSlider.setValue(SoundManager.getSFXVolume());

        bgmMute.setSelected(SoundManager.isBGMMuted());
        sfxMute.setSelected(SoundManager.isSFXMuted());

        // === Listeners ===
        bgmSlider.valueProperty().addListener((obs, oldV, newV) -> {
            SoundManager.setBGMVolume(newV.doubleValue());
        });

        sfxSlider.valueProperty().addListener((obs, oldV, newV) -> {
            SoundManager.setSFXVolume(newV.doubleValue());
        });

        bgmMute.setOnAction(e -> {
            SoundManager.setBGMMute(bgmMute.isSelected());
        });

        sfxMute.setOnAction(e -> {
            SoundManager.setSFXMute(sfxMute.isSelected());
        });

        // BACK TO MAIN MENU
        backButton.setOnAction(e -> goBack());
    }

    private Runnable onBackAction;

    public void setOnBack(Runnable action) {
        this.onBackAction = action;
    }

    private void goBack() {
        if (onBackAction != null) {
            onBackAction.run();
            return;
        }

        // Default behavior (from main menu)
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getClassLoader().getResource("main_menu.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root, 700, 700));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
