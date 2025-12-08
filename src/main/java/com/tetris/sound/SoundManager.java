package com.tetris.sound;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;

public class SoundManager {

    private static final HashMap<String, AudioClip> sounds = new HashMap<>();

    // === NEW === gameover uses MediaPlayer so we can skip silence
    private static MediaPlayer gameOverPlayer;

    public static void load() {
        loadSound("rotate", "sounds/click_005.wav");
        loadSound("hardDrop", "sounds/footstep_grass_000.wav");
        loadSound("softDrop", "sounds/select_007.wav");
        loadSound("lineClear", "sounds/confirmation_001.wav");
        loadSound("levelUp", "sounds/confirmation_004.wav");
        loadSound("pause", "sounds/drop_001.wav");
        loadSound("unpause", "sounds/glass_005.wav");
        loadSound("hold", "sounds/select_001.wav");
        loadSound("menu", "sounds/drop_002.wav");
        loadSound("combo", "sounds/confirmation_002.wav");
        loadSound("restart", "sounds/bong_001.wav");

        loadGameOverSound();  // <- MediaPlayer version
    }

    // ===================================
    // GAME OVER SOUND (MediaPlayer)
    // ===================================
    private static void loadGameOverSound() {
        try {
            URL resource = SoundManager.class.getClassLoader()
                    .getResource("sounds/gameover.wav");

            if (resource == null) {
                System.out.println("Failed to load gameover.wav");
                return;
            }

            Media media = new Media(resource.toString());
            gameOverPlayer = new MediaPlayer(media);

        } catch (Exception e) {
            System.out.println("GameOver load error:");
            e.printStackTrace();
        }
    }

    public static void playGameOver() {
        if (gameOverPlayer != null) {
            gameOverPlayer.stop();

            // === Adjust skip amount here (start with 600–900ms) ===
            gameOverPlayer.seek(Duration.millis(1200));

            gameOverPlayer.play();
        }
    }

    public static boolean isBGMPlaying() {
        return bgmStarted;
    }

    // ===================================
    // BACKGROUND MUSIC
    // ===================================

    private static MediaPlayer bgmPlayer;
    private static boolean bgmStarted = false;

    public static void playBGM(String filename) {
        try {
            // If BGM player already exists, just resume if paused
            if (bgmPlayer != null) {
                if (bgmPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
                    bgmPlayer.play();
                }
                return; // DO NOT RESTART
            }

            // Create BGM only once
            URL resource = SoundManager.class.getClassLoader()
                    .getResource("sounds/" + filename);

            if (resource == null) {
                System.out.println("BGM not found: " + filename);
                return;
            }

            Media media = new Media(resource.toString());
            bgmPlayer = new MediaPlayer(media);

            bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            bgmPlayer.setVolume(0.35);
            bgmPlayer.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
            bgmPlayer.dispose();
            bgmPlayer = null;   // <-- CRITICAL: this prevents ghost players
        }
    }

    public static void pauseBGM() {
        if (bgmPlayer != null) bgmPlayer.pause();
    }

    public static void resumeBGM() {
        if (bgmPlayer != null) bgmPlayer.play();
    }

    // ===================================
    // NORMAL SOUND EFFECTS
    // ===================================

    private static void loadSound(String key, String filename) {
        try {
            AudioClip clip = new AudioClip(
                    SoundManager.class.getResource("/" + filename).toExternalForm()
            );
            sounds.put(key, clip);
        } catch (Exception e) {
            System.out.println("Failed to load sound: " + filename);
        }
    }

    public static void play(String key) {
        AudioClip clip = sounds.get(key);
        if (clip != null) clip.play();
    }
}
