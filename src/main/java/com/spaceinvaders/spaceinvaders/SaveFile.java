package com.spaceinvaders.spaceinvaders;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static com.spaceinvaders.spaceinvaders.SpaceInvaders.*;
import static com.spaceinvaders.spaceinvaders.SpaceInvaders.HEIGHT;

public class SaveFile implements Serializable{
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";
    static String key = "1234567890123456";
    @FXML
    public TextField text;

    public void submitSaveName(ActionEvent event) {
        String filename = text.getText();

        // Create SaveData object
        List<Bomb> bombs = SpaceInvaders.getMethods().getBombs();
        List<Shot> shots = SpaceInvaders.getMethods().getShots();
        SaveData saveData = new SaveData(player, bombs, shots);

        // Serialize SaveData object
        try (FileOutputStream fileOut = new FileOutputStream("savegame.dat");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(saveData);
            System.out.println("Logs [ " + Instant.now() + " ] : Data serialized to savegame.dat");
        } catch (IOException e) {
            System.out.println("Logs [ " + Instant.now() + " ] : Error serializing data: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Encrypt the serialized file
        try {
            encrypt(key, "savegame.dat", "src/saves/" + filename + ".dat");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Optionally delete the original unencrypted file after encryption
        File file = new File("savegame.dat");
        if (file.exists()) {
            file.delete();
        }

        // Resume the game or show a confirmation
        SpaceInvaders.getMethods().resumeGame();
        SpaceInvaders.getMethods().takeMainPageScreenshot(filename);
    }



    public static void encrypt(String key, String inputFile, String outputFile) throws Exception {
        doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
        System.out.println("Logs [ "+ Instant.now()   +" ] :"+"File encrypted successfully: " + outputFile);
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
            System.out.println("Logs [ "+ Instant.now()   +" ] :"+"Output file size after " + (cipherMode == Cipher.ENCRYPT_MODE ? "encryption" : "decryption") + ": " + outFile.length());
        } else {
            System.out.println("Logs [ "+ Instant.now()   +" ] :"+"Output file is empty or not created.");
        }
    }

    public void closesavemenu(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
        SpaceInvaders.getMethods().resumeGame();
    }
}
