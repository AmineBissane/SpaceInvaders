package com.spaceinvaders.spaceinvaders;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.time.Instant;

public class HiloMusical implements Runnable {

    private Clip currentMusic;
    private Long clipPosition;
    private boolean isPaused = false;  // Track if music is paused

    @Override
    public void run() {
        playMusic1();
        System.out.println("Logs [ " + Instant.now() + " ] : music Started");
    }

    public void playMusic1() {
        String music1root = "src/Music/music2.wav";

        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(music1root).getAbsoluteFile());
            currentMusic = AudioSystem.getClip();
            currentMusic.open(audioInputStream);
            currentMusic.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    currentMusic.close();
                    playMusic2();
                }
            });
            currentMusic.start();
            isPaused = false;  
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playMusic2() {
        String music2root = "src/Music/music1.wav";

        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(music2root).getAbsoluteFile());
            currentMusic = AudioSystem.getClip();
            currentMusic.open(audioInputStream);
            currentMusic.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    currentMusic.close();
                    playMusic1();
                }
            });
            currentMusic.start();
            isPaused = false;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void pauseMusic() {
        if (currentMusic != null && currentMusic.isRunning()) {
            clipPosition = currentMusic.getMicrosecondPosition();
            currentMusic.stop();
            isPaused = true;
            System.out.println("Logs [ " + Instant.now() + " ] : music paused at position " + clipPosition);
        }
    }

    public void resumeMusic() {
        if (currentMusic != null && clipPosition != null && isPaused) {
            currentMusic.setMicrosecondPosition(clipPosition);
            currentMusic.start();
            isPaused = false;
            System.out.println("Logs [ " + Instant.now() + " ] : music resumed from position " + clipPosition);
        } else {
            System.out.println("Logs [ " + Instant.now() + " ] : No music to resume or it wasn't paused.");
        }
    }

    public Clip getCurrentMusic() {
        return currentMusic;
    }

    public Long getClipPosition() {
        return clipPosition;
    }
}
