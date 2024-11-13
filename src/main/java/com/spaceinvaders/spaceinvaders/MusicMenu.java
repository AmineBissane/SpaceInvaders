package com.spaceinvaders.spaceinvaders;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

import java.time.Instant;

public class MusicMenu {
    HiloMusical hiloMusical = new HiloMusical();
    @FXML
    public CheckBox checkbox1;

    public void close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void checkbox1Changed(ActionEvent event) {
        if (checkbox1.isSelected()){
            hiloMusical.pauseMusic();
            System.out.println("Logs [ "+ Instant.now()   +" ] :"+"music is paused");
        } else {
            hiloMusical.resumeMusic();
            System.out.println("Logs [ "+ Instant.now()   +" ] :"+"music is resumed");
        }
    }


}
