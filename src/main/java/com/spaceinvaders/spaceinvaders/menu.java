package com.spaceinvaders.spaceinvaders;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
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
    private static final String ALGORITHM = "AES"; // Encryption algorithm
    private static final String TRANSFORMATION = "AES";
    String key = "1234567890123456";
    public void close(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void save(ActionEvent event) throws Exception {
        String dataToSave = player.score + "," + player.posX + "," + player.posY + "," +
                player.size + "," + player.explosionStep + "," + player.imgIndex + "," + player.destroyed;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("savegame.dat"))) {
            writer.write(dataToSave);
            System.out.println("Data written to savegame.dat: " + dataToSave);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
            e.printStackTrace();
            return;
        }
            encrypt(key, "savegame.dat", "encrypted_savegame.dat");
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
        File file = new File("savegame.dat");
        file.delete();
        gc.setFont(Font.font(35));
        gc.setFill(Color.YELLOW);
        gc.fillText("Game Saved!", WIDTH / 2, HEIGHT / 2.5);
    }


    public void continuee(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void loadsave(ActionEvent event) throws Exception {
        decrypt(key, "encrypted_savegame.dat", "decrypted_savegame.dat");
        try {
            BufferedReader reader = new BufferedReader(new FileReader("decrypted_savegame.dat"));
            String line = reader.readLine();
            String[] data = line.split(",");
            player.score = Integer.parseInt(data[0]);
            player.posX = Integer.parseInt(data[1]);
            player.posY = Integer.parseInt(data[2]);
            player.size = Integer.parseInt(data[3]);
            player.explosionStep = Integer.parseInt(data[4]);
            player.imgIndex = Integer.parseInt(data[5]);
            player.destroyed = Boolean.parseBoolean(data[6]);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
        File file = new File("decrypted_savegame.dat");
        file.delete();
    }

    public static void encrypt(String key, String inputFile, String outputFile) throws Exception {
        doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
        System.out.println("File encrypted successfully: " + outputFile);
    }

    public static void decrypt(String key, String inputFile, String outputFile) throws Exception {
        doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
        System.out.println("File decrypted successfully: " + outputFile);
    }

    private static void doCrypto(int cipherMode, String key, String inputFile, String outputFile) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(cipherMode, secretKey);

        try (FileInputStream inputStream = new FileInputStream(inputFile);
             FileOutputStream outputStream = new FileOutputStream(outputFile);
             CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher)) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                cipherOutputStream.write(buffer, 0, bytesRead);
            }
        }

        File outFile = new File(outputFile);
        if (outFile.exists() && outFile.length() > 0) {
            System.out.println("Output file size after " + (cipherMode == Cipher.ENCRYPT_MODE ? "encryption" : "decryption") + ": " + outFile.length());
        } else {
            System.out.println("Output file is empty or not created.");
        }
    }

}

