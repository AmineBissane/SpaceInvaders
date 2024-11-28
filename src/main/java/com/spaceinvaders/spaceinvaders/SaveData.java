package com.spaceinvaders.spaceinvaders;

import java.io.Serializable;
import java.util.List;

public class SaveData implements Serializable {
    private static final long serialVersionUID = 1L;
    public int score;
    public int posX;
    public int posY;
    public int size;
    public int explosionStep;
    public int imgIndex;
    public boolean destroyed;
    public List<Bomb> bombs;
    public List<Shot> shots;
    public SaveData() {}
    public SaveData(Rocket player, List<Bomb> bombs, List<Shot> shots) {
        this.score = player.score;
        this.posX = player.posX;
        this.posY = player.posY;
        this.size = player.size;
        this.explosionStep = player.explosionStep;
        this.imgIndex = player.imgIndex;
        this.destroyed = player.destroyed;
        this.bombs = bombs;
        this.shots = shots;
    }
}