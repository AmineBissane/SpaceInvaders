package com.spaceinvaders.spaceinvaders;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import javax.crypto.CipherOutputStream;
import static com.spaceinvaders.spaceinvaders.SpaceInvaders.*;

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
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("save as :");
        stage.show();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        SpaceInvaders.getInstance().pauseGame();

    }

    public void loadas(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loadsave.fxml"));
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        stage.setTitle("load saves !");
        stage.show();
        stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
        SpaceInvaders.getInstance().pauseGame();

    }
}

