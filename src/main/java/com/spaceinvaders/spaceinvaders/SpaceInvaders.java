package com.spaceinvaders.spaceinvaders;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class SpaceInvaders extends Application {
	private static Stage mainStage;
	private static SpaceInvaders methods;

	public SpaceInvaders() {
		methods = this;
	}
	public static SpaceInvaders getMethods() {
		return methods;
	}
	static final Random RAND = new Random();
	static final int WIDTH = 800;
	static final int HEIGHT = 600;
	static final int PLAYER_SIZE = 60;
	static final Image PLAYER_IMG = new Image("file:images/player.png");
	static final Image EXPLOSION_IMG = new Image("file:images/explosion.png");
	static final int EXPLOSION_W = 128;
	static final int EXPLOSION_ROWS = 3;
	static final int EXPLOSION_COL = 3;
	static final int EXPLOSION_H = 128;
	static final int EXPLOSION_STEPS = 15;
	static GraphicsContext gc;
	private boolean isGamePaused = false;

	static final Image BOMBS_IMG[] = {
			new Image("file:images/1.png"),
			new Image("file:images/2.png"),
			new Image("file:images/3.png"),
			new Image("file:images/4.png"),
			new Image("file:images/5.png"),
			new Image("file:images/6.png"),
			new Image("file:images/7.png"),
			new Image("file:images/8.png"),
			new Image("file:images/9.png"),
			new Image("file:images/10.png"),
	};
	final int MAX_BOMBS = 10, MAX_SHOTS = MAX_BOMBS * 2;
	boolean gameOver = false;

	static Rocket player;
	List<Shot> shots;
	List<Universe> univ;
	List<Bomb> Bombs;

	private double mouseX;

	public void start(Stage stage) throws IOException {
		mainStage = stage;
		HiloMusical hilo = HiloMusical.getInstance();
		hilo.run();
		Canvas canvas = new Canvas(WIDTH, HEIGHT);
		gc = canvas.getGraphicsContext2D();
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> run(gc)));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
		canvas.setCursor(Cursor.MOVE);
		canvas.setOnMouseMoved(e -> mouseX = e.getX());
		canvas.setOnMouseClicked(e -> {
			if (shots.size() < MAX_SHOTS) shots.add(player.shoot());
			if (gameOver) {
				gameOver = false;
				setup();
			}
		});
		setup();

		Scene scene = new Scene(new StackPane(canvas));
		stage.setScene(scene);
		stage.setTitle("Space Invaders");
		stage.show();
		scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.P) {
				pauseGame();
				hilo.pauseMusic();
			} else if (event.getCode() == KeyCode.O) {
				resumeGame();
				hilo.resumeMusic();
			}else if (event.getCode() == KeyCode.ESCAPE) {
				openMenu(stage);
			}
		});
		scene.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				SoundEffects.getInstance().shotsound();
			}
		});


		stage.setOnCloseRequest(event -> {
			System.out.println("Logs [ "+ Instant.now()   +" ] :"+"music thread closed");
			hilo.stopThread();

		});
		BufferedReader reader = new BufferedReader(new FileReader("src/musicsave/musicsave.dat"));
		String line = reader.readLine();
		if (line!=null && line.equals("true")) {
			hilo.resumeMusic();
		} else {
			hilo.pauseMusic();
		}
		reader.close();
	}

	public void pauseGame() {
		isGamePaused = true;
		System.out.println("Logs [ "+ Instant.now()   +" ] :"+"Game paused");
	}

	public void resumeGame() {
		isGamePaused = false;
		System.out.println("Logs [ "+ Instant.now()   +" ] :"+"Game resumed.");
	}



	private void setup() {
		univ = new ArrayList<>();
		shots = new ArrayList<>();
		Bombs = new ArrayList<>();
		player = new Rocket(WIDTH / 2, HEIGHT - PLAYER_SIZE, PLAYER_SIZE);
		player.score = 0;
		IntStream.range(0, MAX_BOMBS).mapToObj(i -> this.newBomb()).forEach(Bombs::add);
	}

	private void run(GraphicsContext gc) {
		if (isGamePaused) {
			gc.setFont(Font.font(35));
			gc.fillText("Game paused", WIDTH / 2, HEIGHT / 3.7);
			return;
		}

		gc.setFill(Color.grayRgb(20));
		gc.fillRect(0, 0, WIDTH, HEIGHT);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setFont(Font.font(20));
		gc.setFill(Color.WHITE);
		gc.fillText("Score: " + player.score, 60, 20);

		if (gameOver) {
			gc.setFont(Font.font(35));
			gc.setFill(Color.YELLOW);
			gc.fillText("Game Over \n Your Score is: " + player.score + " \n Click to play again", WIDTH / 2, HEIGHT / 2.5);
			return;
		}
		univ.forEach(Universe::draw);
		player.update();
		player.draw();
		player.posX = (int) mouseX;


		Bombs.stream().peek(Rocket::update).peek(Rocket::draw).forEach(e -> {
			if (player.colide(e) && !player.exploding) {
				player.explode();
				SoundEffects.getInstance().gameoversound();
			}
		});

		for (int i = shots.size() - 1; i >= 0; i--) {
			Shot shot = shots.get(i);
			if (shot.posY < 0 || shot.toRemove) {
				shots.remove(i);
				continue;
			}
			shot.update();
			shot.draw();
			for (Bomb bomb : Bombs) {
				if (shot.colide(bomb) && !bomb.exploding) {
					player.score++;
					SoundEffects.getInstance().explosionsound();
					bomb.explode();
					SoundEffects.getInstance().explosionsound();
					shot.toRemove = true;
				}
			}
		}

		for (int i = Bombs.size() - 1; i >= 0; i--) {
			if (Bombs.get(i).destroyed) {
				Bombs.set(i, newBomb());
			}
		}

		gameOver = player.destroyed;
		if (RAND.nextInt(10) > 2) {
			univ.add(new Universe());
		}
		for (int i = 0; i < univ.size(); i++) {
			if (univ.get(i).posY > HEIGHT)
				univ.remove(i);
		}
	}


	private Bomb newBomb() {
		return new Bomb(50 + RAND.nextInt(WIDTH - 100), 0, PLAYER_SIZE, RAND.nextInt(BOMBS_IMG.length));
	}

	static int distance(int x1, int y1, int x2, int y2) {
		return (int) Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
	}

	public static void main(String[] args) {
		launch();
	}

	public List<Bomb> getBombs() {
		return Bombs;
	}

	public List<Shot> getShots() {
		return shots;
	}
	public void setBombs(List newBombs) {
		this.Bombs = newBombs;
	}

	public void setShots(List newShots) {
		this.shots = newShots;
	}

	public List<Universe> getUniverse() {
		return univ;
	}


	public void openMenu(Window stage) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
		Stage menuStage = new Stage();
		Scene scene = null;
		try {
			scene = new Scene(loader.load());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		scene.setFill(Color.TRANSPARENT);
		menuStage.setScene(scene);
		menuStage.initStyle(StageStyle.TRANSPARENT);
		menuStage.setTitle("Game Menu");
		menuStage.setWidth(820);
		menuStage.setHeight(640);
		pauseGame();
		menuStage.setAlwaysOnTop(true);
		menuStage.setOnHidden(e -> resumeGame());
		menuStage.show();
	}
	public static Stage getMainStage() {
		return mainStage;
	}
	public void takeMainPageScreenshot(String nombre) {
		try {
			Stage mainStage = SpaceInvaders.getMainStage();
			WritableImage image = mainStage.getScene().snapshot(null);
			File file = new File("src/miniatures/"+nombre+".png");
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
			System.out.println("Logs [ "+ Instant.now()   +" ] :"+"Main page screenshot saved: " + file.getAbsolutePath());
		} catch (IOException ex) {
			System.err.println("Error saving the main page screenshot: " + ex.getMessage());
		}
	}
}
