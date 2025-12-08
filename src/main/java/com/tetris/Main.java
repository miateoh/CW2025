/**
 * Entry point of the application responsible for launching the JavaFX
 * environment and loading initial UI screens.
 */
package com.tetris;

import com.tetris.game.logic.GameController;
import com.tetris.sound.SoundManager;
import com.tetris.ui.controllers.GuiController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        SoundManager.load();

        SoundManager.playBGM("background_music.wav");

        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("main_menu.fxml")
        );

        Parent root = loader.load();

        primaryStage.setTitle("TetrisJFX");
        primaryStage.setScene(new Scene(root, 700, 700));
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
