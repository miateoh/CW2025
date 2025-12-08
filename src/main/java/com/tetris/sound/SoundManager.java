/**
 * Central audio manager controlling background music, sound effects,
 * and volume settings for the entire game.
 */
package com.tetris.sound;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.net.URL;
import java.util.HashMap;

public class SoundManager {

    private static final HashMap<String, AudioClip> sounds = new HashMap<>();

    // Game over MediaPlayer
    private static MediaPlayer gameOverPlayer;

    // Background music
    private static MediaPlayer bgmPlayer;

    // === Volume settings ===
    private static double bgmVolume = 0.35;
    private static double sfxVolume = 1.0;

    private static boolean mutedBGM = false;
    private static boolean mutedSFX = false;

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

        loadGameOverSound();
    }

    // =====================================
    // GAME OVER SOUND
    // =====================================
    private static void loadGameOverSound() {
        try {
            URL r = SoundManager.class.getClassLoader().getResource("sounds/gameover.wav");

            if (r == null) return;

            Media media = new Media(r.toString());
            gameOverPlayer = new MediaPlayer(media);

        } catch (Exception ignored) {}
    }

    public static void playGameOver() {
        if (gameOverPlayer == null) return;

        gameOverPlayer.stop();
        gameOverPlayer.seek(Duration.millis(1200)); // skip silence
        gameOverPlayer.play();
    }

    public static void pauseBGM() {
        if (bgmPlayer != null) bgmPlayer.pause();
    }

    public static void resumeBGM() {
        if (bgmPlayer != null) bgmPlayer.play();
    }

    public static boolean isBGMPlaying() {
        return bgmPlayer != null && bgmPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    // =====================================
    // BACKGROUND MUSIC
    // =====================================
    public static void playBGM(String filename) {
        try {

            if (bgmPlayer != null) {
                if (bgmPlayer.getStatus() == MediaPlayer.Status.PAUSED)
                    bgmPlayer.play();
                return;
            }

            URL r = SoundManager.class.getClassLoader().getResource("sounds/" + filename);
            if (r == null) return;

            Media media = new Media(r.toString());
            bgmPlayer = new MediaPlayer(media);
            bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);

            applyBGMVolume();
            bgmPlayer.play();

        } catch (Exception ignored) {}
    }

    public static void stopBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
            bgmPlayer = null;
        }
    }

    // =====================================
    // VOLUME + MUTE
    // =====================================

    public static void setBGMVolume(double v) {
        bgmVolume = v;
        applyBGMVolume();
    }

    public static void applyBGMVolume() {
        if (bgmPlayer != null)
            bgmPlayer.setVolume(mutedBGM ? 0 : bgmVolume);
    }

    public static double getBGMVolume() {
        return bgmVolume;
    }

    public static void setBGMMute(boolean mute) {
        mutedBGM = mute;
        applyBGMVolume();
    }

    public static boolean isBGMMuted() {
        return mutedBGM;
    }

    // --- SFX ---
    public static void setSFXVolume(double v) {
        sfxVolume = v;
        for (AudioClip c : sounds.values())
            c.setVolume(mutedSFX ? 0 : sfxVolume);
    }

    public static double getSFXVolume() {
        return sfxVolume;
    }

    public static void setSFXMute(boolean mute) {
        mutedSFX = mute;
        for (AudioClip c : sounds.values())
            c.setVolume(mute ? 0 : sfxVolume);
    }

    public static boolean isSFXMuted() {
        return mutedSFX;
    }

    // =====================================
    // NORMAL SFX
    // =====================================
    private static void loadSound(String key, String filename) {
        try {
            AudioClip clip = new AudioClip(
                    SoundManager.class.getResource("/" + filename).toExternalForm()
            );
            clip.setVolume(sfxVolume);
            sounds.put(key, clip);
        } catch (Exception ignored) {}
    }

    public static void play(String key) {
        if (!mutedSFX && sounds.containsKey(key))
            sounds.get(key).play();
    }
}
