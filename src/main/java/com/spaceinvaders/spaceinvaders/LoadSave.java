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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.spaceinvaders.spaceinvaders.SpaceInvaders.*;
import static com.spaceinvaders.spaceinvaders.SpaceInvaders.HEIGHT;

public class LoadSave {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";
    String key = "1234567890123456";
    @FXML
    public ListView<String> listView;

    @FXML
    public Button loadButton;

    @FXML
    public void initialize() {
        loadFileNames();
    }

    private void loadFileNames() {
        File directory = new File("src/saves/");
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                int count = 0;
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".dat")) {
                        if (count < 5) {
                            listView.getItems().add(file.getName());
                            count++;
                        } else {
                            break;
                        }
                    }
                }
            }
        }
    }

    @FXML
    public void loadsavefile(ActionEvent event) {
        String selectedFile = listView.getSelectionModel().getSelectedItem();
        if (selectedFile!=null){
            try {
                decrypt(key, "src/saves/"+selectedFile, "decrypted.dat");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                BufferedReader reader = new BufferedReader(new FileReader("decrypted.dat"));
                String line = reader.readLine();
                String[] data = line.split(",");
                player.score = Integer.parseInt(data[0]);
                player.posX = Integer.parseInt(data[1]);
                List<Bomb> bombs = Arrays.stream(data[7].split(";"))
                        .map(bombStr -> {
                            String[] bombData = bombStr.split(":");
                            return new Bomb(Integer.parseInt(bombData[0]), Integer.parseInt(bombData[1]),
                                    Integer.parseInt(bombData[2]), Integer.parseInt(bombData[3]));
                        })
                        .collect(Collectors.toList());
                getMethods().setBombs(bombs);
                List<Shot> shots = Arrays.stream(data[8].split(";"))
                        .map(shotStr -> {
                            String[] shotData = shotStr.split(":");
                            return new Shot(Integer.parseInt(shotData[0]), Integer.parseInt(shotData[1]));
                        })
                        .collect(Collectors.toList());
                getMethods().setShots(shots);
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
            File file = new File("decrypted.dat");
            file.delete();
            getMethods().resumeGame();
        }
    }


    public static void decrypt(String key, String inputFile, String outputFile) throws Exception {
        doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
        System.out.println("File decrypted successfully: " + outputFile);
    }

    private static void doCrypto(int cipherMode, String key, String inputFile, String outputFile) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(cipherMode, secretKey);
        System.out.println("Cipher initialized with key and mode.");

        try (FileInputStream inputStream = new FileInputStream(inputFile);
             FileOutputStream outputStream = new FileOutputStream(outputFile);
             CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            boolean dataWritten = false;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                cipherOutputStream.write(buffer, 0, bytesRead);
                dataWritten = true;
            }

            if (!dataWritten) {
                System.out.println("No data was written to the decrypted file.");
            }
        }

        File outFile = new File(outputFile);
        if (outFile.exists() && outFile.length() > 0) {
            System.out.println("Output file size after " + (cipherMode == Cipher.ENCRYPT_MODE ? "encryption" : "decryption") + ": " + outFile.length());
        } else {
            System.out.println("Output file is empty or not created.");
        }
    }

    public void closeloadsavefile(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
        SpaceInvaders.getMethods().resumeGame();
    }
}