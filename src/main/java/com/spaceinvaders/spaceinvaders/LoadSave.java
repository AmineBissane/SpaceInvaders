package com.spaceinvaders.spaceinvaders;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.awt.*;
import java.io.*;

import static com.spaceinvaders.spaceinvaders.SpaceInvaders.*;
import static com.spaceinvaders.spaceinvaders.SpaceInvaders.HEIGHT;

public class LoadSave {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";
    String key = "1234567890123456"; // Ensure this key is securely stored and managed
    @FXML
    public ListView<String> listView; // ListView for displaying saved files

    @FXML
    public Button loadButton; // Button to load the selected file

    // Initialize method to load file names into ListView
    @FXML
    public void initialize() {
        loadFileNames();
    }

    // Load all .dat files from the specified directory into the ListView
    private void loadFileNames() {
        File directory = new File("src/saves/"); // Path to the saves directory
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".dat")) {
                        listView.getItems().add(file.getName());
                    }
                }
            }
        } else {
            System.out.println("Directory does not exist or is not a directory.");
        }
    }

    @FXML
    public void loadsavefile(ActionEvent event) {
        String selectedFile = listView.getSelectionModel().getSelectedItem();
        try {
            decrypt(key, "src/saves/"+selectedFile, "src/saves/"+selectedFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/saves/"+selectedFile));
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

