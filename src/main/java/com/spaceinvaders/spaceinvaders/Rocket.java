package com.spaceinvaders.spaceinvaders;

import java.io.Serializable;

import static com.spaceinvaders.spaceinvaders.SpaceInvaders.*;

// Player
public class Rocket implements Serializable {
    private static final long serialVersionUID = 1L;
    int score;
    int posX, posY, size;
    boolean exploding;
    boolean destroyed;
    int imgIndex;
    int explosionStep = 0;

    public Rocket(int posX, int posY, int size) {
        this.posX = posX;
        this.posY = posY;
        this.size = size;
    }

    public Shot shoot() {
        return new Shot(posX + size / 2 - Shot.size / 2, posY - Shot.size);
    }

    public void update() {
        if(exploding) explosionStep++;
        destroyed = explosionStep > EXPLOSION_STEPS;
    }

    public void draw() {
        if(exploding) {
            gc.drawImage(EXPLOSION_IMG, explosionStep % EXPLOSION_COL * EXPLOSION_W, (explosionStep / EXPLOSION_ROWS) * EXPLOSION_H + 1,
                    EXPLOSION_W, EXPLOSION_H,
                    posX, posY, size, size);
        }
        else {
            gc.drawImage(PLAYER_IMG, posX, posY, size, size);
        }
    }

    public boolean colide(Rocket other) {
        int d = distance(this.posX + size / 2, this.posY + size /2,
                other.posX + other.size / 2, other.posY + other.size / 2);
        return d < other.size / 2 + this.size / 2 ;
    }

    public void explode() {
        exploding = true;
        explosionStep = -1;
    }

}