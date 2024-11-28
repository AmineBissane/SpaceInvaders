package com.spaceinvaders.spaceinvaders;

import javafx.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.apache.commons.lang3.builder.ToStringBuilder;
import javafx.event.ActionEvent;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.awt.*;
import java.io.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.spaceinvaders.spaceinvaders.SpaceInvaders.*;
import static com.spaceinvaders.spaceinvaders.SpaceInvaders.HEIGHT;

public class LoadSave implements Serializable{
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";
    String key = "1234567890123456";
    @FXML
    public ListView<HBox> listView;

    @FXML
    public Button loadButton;

    @FXML
    public void initialize() {
        loadFileNames();
    }

    private void loadFileNames() {
        File saveDirectory = new File("src/saves/");
        File miniatureDirectory = new File("src/miniatures/");

        if (saveDirectory.exists() && saveDirectory.isDirectory() && miniatureDirectory.exists() && miniatureDirectory.isDirectory()) {
            File[] saveFiles = saveDirectory.listFiles();
            if (saveFiles != null) {
                int count = 0;
                for (File saveFile : saveFiles) {
                    if (saveFile.isFile() && saveFile.getName().endsWith(".dat")) {
                        String baseName = saveFile.getName().substring(0, saveFile.getName().lastIndexOf('.'));
                        File correspondingImage = new File(miniatureDirectory, baseName + ".png");

                        if (correspondingImage.exists()) {
                            if (count < 5) {
                                Image image = new Image(correspondingImage.toURI().toString());
                                ImageView imageView = new ImageView(image);
                                imageView.setFitWidth(50);
                                imageView.setFitHeight(50);
                                HBox hbox = new HBox(10, imageView, new javafx.scene.control.Label(saveFile.getName()));
                                hbox.setAlignment(Pos.CENTER_LEFT);

                                listView.setCellFactory(param -> new ListCell<HBox>() {
                                    @Override
                                    protected void updateItem(HBox item, boolean empty) {
                                        super.updateItem(item, empty);
                                        if (empty || item == null) {
                                            setGraphic(null);
                                        } else {
                                            setGraphic(item);
                                        }
                                    }
                                });
                                listView.getItems().add(hbox);
                                count++;
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }


    @FXML
    public void loadsavefile(ActionEvent event) {
        HBox selectedItem = listView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String filename = ((javafx.scene.control.Label) selectedItem.getChildren().get(1)).getText();
            try {
                decrypt(key, "src/saves/" + filename, "decryptedd.dat");
                try (FileInputStream fileIn = new FileInputStream("decryptedd.dat");
                     ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {

                    Object readObject = objectIn.readObject();

                    // Detailed logging
                    System.out.println("Deserialized object class: " + readObject.getClass());
                    System.out.println("Is SaveData? " + (readObject instanceof SaveData));

                    if (readObject instanceof SaveData) {
                        SaveData saveData = (SaveData) readObject;
                        player = new Rocket(saveData.posX, saveData.posY, saveData.size);
                        player.score = saveData.score;
                        player.explosionStep = saveData.explosionStep;
                        player.imgIndex = saveData.imgIndex;
                        player.destroyed = saveData.destroyed;

                        getMethods().setBombs(saveData.bombs);
                        getMethods().setShots(saveData.shots);

                        System.out.println("Game state successfully loaded");
                    } else {
                        System.out.println("Error: The deserialized object is not of type SaveData.");
                        System.out.println("Actual object type: " + readObject.getClass());
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    showError("Error loading save data: " + e.getMessage());
                    return;
                }

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();

                File file = new File("decryptedd.dat");
                if (file.exists()) {
                    file.delete();
                }

                getMethods().resumeGame();

            } catch (Exception e) {
                e.printStackTrace();
                showError("Error during file decryption: " + e.getMessage());
            }
        }
    }

    private void showError(String message) {
        System.err.println(message);
    }


    public static void decrypt(String key, String inputFile, String outputFile) throws Exception {
        doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
        System.out.println("Logs [ "+ Instant.now()   +" ] :"+"File decrypted successfully: " + outputFile);
    }

    private static void doCrypto(int cipherMode, String key, String inputFile, String outputFile) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(cipherMode, secretKey);
        System.out.println("Logs [ "+ Instant.now()   +" ] :"+"Cipher initialized with key and mode.");

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
                System.out.println("Logs [ "+ Instant.now()   +" ] :"+"No data was written to the decrypted file.");
            }
        }

        File outFile = new File(outputFile);
        if (outFile.exists() && outFile.length() > 0) {
            System.out.println("Logs [ "+ Instant.now()   +" ] :"+"Output file size after " + (cipherMode == Cipher.ENCRYPT_MODE ? "encryption" : "decryption") + ": " + outFile.length());
        } else {
            System.out.println("Logs [ "+ Instant.now()   +" ] :"+"Output file is empty or not created.");
        }
    }

    public void closeloadsavefile(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
        SpaceInvaders.getMethods().resumeGame();
    }
}