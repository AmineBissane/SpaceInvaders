package com.spaceinvaders.spaceinvaders;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.*;

import static com.spaceinvaders.spaceinvaders.SpaceInvaders.player;

public class menu {
    public int score;
    public void close(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void save(ActionEvent actionEvent) throws IOException {
        FileWriter writer = new FileWriter("savegame.dat");
        writer.write(String.valueOf(player.score));
        writer.close();
    }
    public void continuee(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void loadsave(ActionEvent actionEvent) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("savegame.dat"));
            String line = reader.readLine();
            player.score = Integer.parseInt(line);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

