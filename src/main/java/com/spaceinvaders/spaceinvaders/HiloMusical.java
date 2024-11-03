package com.spaceinvaders.spaceinvaders;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;


public class HiloMusical implements Runnable {

    @Override
    public void run() {
        playmusic1();
    }

    public void playmusic1(){
        String music1root = "src/Music/music2.wav";

        {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(music1root).getAbsoluteFile());
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                        playmusic2();
                    }
                });
                clip.start();

            } catch (UnsupportedAudioFileException | IOException e) {
                throw new RuntimeException(e);
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void playmusic2() {
        String music2root = "src/Music/music1.wav";
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(music2root).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                    playmusic1();
                }
            });

            clip.start();
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

    }
}
