package com.spaceinvaders.spaceinvaders;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class SoundEffects {
    public static SoundEffects instance;
    public static synchronized SoundEffects getInstance() {
        if (instance == null) {
            instance = new SoundEffects();
        }
        return instance;
    }

    public void explosionsound(){
        String filename = "src/Music/explosion.wav";
        try
        {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File(filename)));
            clip.start();
        }
        catch (Exception exc)
        {
            exc.printStackTrace(System.out);
        }
    }
    public void shotsound() {
        try {
            String filename = "src/Music/shot.wav";
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File(filename)));
            clip.setMicrosecondPosition(900000);
            clip.start();
        } catch (Exception exc) {
            exc.printStackTrace(System.out);
        }
    }
    public void gameoversound(){
        System.out.println("over sound");
        String filename = "src/Music/gameover.wav";
        try
        {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File(filename)));
            clip.start();
        }
        catch (Exception exc)
        {
            exc.printStackTrace(System.out);
        }
    }
}