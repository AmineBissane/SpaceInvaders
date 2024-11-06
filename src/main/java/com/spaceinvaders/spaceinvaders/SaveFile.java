package com.spaceinvaders.spaceinvaders;

import javafx.event.ActionEvent;
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

import static com.spaceinvaders.spaceinvaders.SpaceInvaders.*;
import static com.spaceinvaders.spaceinvaders.SpaceInvaders.HEIGHT;

public class SaveFile {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";
    String key = "1234567890123456";
    public TextField text;
    public void submitSaveName(ActionEvent event) {
        String filename = text.getText();
        String dataToSave = player.score + "," + player.posX + "," + player.posY + "," +
                player.size + "," + player.explosionStep + "," + player.imgIndex + "," + player.destroyed;
        if (filename.length()!=0){

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("savegame.dat"))) {
            writer.write(dataToSave);
            System.out.println("Data written to savegame.dat: " + dataToSave);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        try {
            encrypt(key, "savegame.dat", "src/saves/"+filename+".dat");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        gc.setFont(Font.font(35));
        gc.setFill(Color.YELLOW);
        gc.fillText("Game Saved!", WIDTH / 2, HEIGHT / 2.5);
        Node source = (Node) event.getSource();
        Window window = source.getScene().getWindow();
        if (window instanceof Stage) {
            ((Stage) window).close();
        }
        File file = new File("savegame.dat");
        file.delete();
        SpaceInvaders.getMethods().resumeGame();
        }
    }

    public static void encrypt(String key, String inputFile, String outputFile) throws Exception {
        doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
        System.out.println("File encrypted successfully: " + outputFile);
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

    public void closesavemenu(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
        SpaceInvaders.getMethods().resumeGame();
    }
}
