package com.spaceinvaders.spaceinvaders;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.time.Instant;

public class menu {

    public void close(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void continuee(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void saveas(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("saveFile.fxml"));
        Stage stage = new Stage();
        try {
            Scene scene = new Scene(loader.load());
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
            stage.setScene(scene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        stage.setTitle("Save as :");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        SpaceInvaders.getMethods().pauseGame();

    }
    public void loadas(ActionEvent event) {
        System.out.println("Logs [ "+ Instant.now()   +" ] : Menu de Load abierto !");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loadsave.fxml"));
        Stage stage = new Stage();
        try {
            Scene scene = new Scene(loader.load());
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
            stage.setScene(scene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        stage.setTitle("Load Saves");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        System.out.println("Logs [ "+ Instant.now()   +" ] : Main Menu cerrado !");

        SpaceInvaders.getMethods().pauseGame();

    }

    public void music(ActionEvent event) {
        System.out.println("Logs [ "+ Instant.now()   +" ] : Menu de musica abierto !");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("musicmenu.fxml"));
        Stage stage = new Stage();
        try {
            Scene scene = new Scene(loader.load());
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
            stage.setScene(scene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        stage.setTitle("Load Saves");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setOnHidden(e -> SpaceInvaders.getMethods().resumeGame());
        stage.show();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        System.out.println("Logs [ "+ Instant.now()   +" ] : Main Menu cerrado !");


        SpaceInvaders.getMethods().pauseGame();
    }


}

