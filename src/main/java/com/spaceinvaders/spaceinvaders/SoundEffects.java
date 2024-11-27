package com.spaceinvaders.spaceinvaders;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.time.Instant;

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
            System.out.println("Logs [ "+ Instant.now()   +" ] :"+"explosion sound activated");
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
            System.out.println("Logs [ "+ Instant.now()   +" ] :"+"shot sound activated");
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
        System.out.println("Logs [ "+ Instant.now()   +" ] :"+"Gameover sound activated");
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