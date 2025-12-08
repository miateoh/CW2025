package com.tetris.sound;

import javafx.scene.media.AudioClip;

import java.net.URL;
import java.util.HashMap;

public class SoundManager {

    private static final HashMap<String, AudioClip> sounds = new HashMap<>();

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
    }

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
