package com.spaceinvaders.spaceinvaders;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

import java.io.*;
import java.time.Instant;

public class MusicMenu {

    @FXML
    public CheckBox checkbox1;
    HiloMusical hilo = HiloMusical.getInstance();

    public void initialize() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/musicsave/musicsave.dat"));
        String line = reader.readLine();
        if (line!=null && line.equals("true")) {
            checkbox1.setSelected(true);
        } else {
            checkbox1.setSelected(false);
        }
        reader.close();
    }
    public void close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void checkbox1Changed(ActionEvent event) throws IOException {
        HiloMusical hilo = HiloMusical.getInstance();
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/musicsave/musicsave.dat"));

        if (checkbox1.isSelected()){
            writer.write("true");
            writer.close();
            hilo.resumeMusic();
            System.out.println("Logs [ "+ Instant.now()   +" ] :"+"music is paused");
        } else {
            writer.write("false");
            writer.close();
            hilo.pauseMusic();
            System.out.println("Logs [ "+ Instant.now()   +" ] :"+"music is resumed");
        }
    }


}
