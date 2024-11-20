package com.spaceinvaders.spaceinvaders;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.time.Instant;

public class HiloMusical implements Runnable {
    private static HiloMusical instance;
    private Clip currentMusic;
    private Long clipPosition;
    private boolean isPaused = false;
    private boolean isManuallyPaused = false;
    private HiloMusical() {}

    public static synchronized HiloMusical getInstance() {
        if (instance == null) {
            instance = new HiloMusical();
        }
        return instance;
    }

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
                if (event.getType() == LineEvent.Type.STOP && !isManuallyPaused) {
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
                if (event.getType() == LineEvent.Type.STOP && !isManuallyPaused) {
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

    public synchronized void pauseMusic() {
        if (currentMusic != null && currentMusic.isRunning()) {
            clipPosition = currentMusic.getMicrosecondPosition();
            isManuallyPaused = true;
            currentMusic.stop();
            isPaused = true;
            System.out.println("Logs [ " + Instant.now() + " ] : music paused at position " + clipPosition);
        } else {
            System.out.println("Logs [ " + Instant.now() + " ] : No music is playing to pause.");
        }
    }

    public synchronized void resumeMusic() {
        if (currentMusic != null && clipPosition != null && isPaused) {
            isManuallyPaused = false;
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
