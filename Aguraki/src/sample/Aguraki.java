package sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Aguraki extends Application {
	
	//variables
	private static final Random RAND = new Random();
	private static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	private static final int PLAYER_SIZE = 60;
	static final Image PLAYER_IMG = new Image("images/player.png");
	
	static final Image[] ENEMIES_IMG = {
			new Image("images/ennemi1.png"),
			new Image("images/ennemi2.png"),
	};

	final int MAX_ENNEMIES = 10,  MAX_SHOTS = MAX_ENNEMIES * 2;
	private boolean gameOver = false;
	private GraphicsContext gc;
	
	Player player;
	List<Shot> shots;
	List<Universe> univ;
	List<Enemy> enemies;
	StackPane root = new StackPane();
	Scene scene = new Scene(root, WIDTH, HEIGHT);
	Timeline timeline = new Timeline(new KeyFrame(Duration.millis(30), e -> run(gc)));

	public static int score;

	public void start(Stage stage) throws Exception {
		Canvas canvas = new Canvas(WIDTH, HEIGHT);	
		gc = canvas.getGraphicsContext2D();

		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
		setup();

		playerMove(scene);
		root.getChildren().add(canvas);

		stage.setScene(scene);
		stage.setTitle("Aguraki");
		stage.show();
		
	}

	/**
	 * SetUp de l'application
	 */
	private void setup() {
		univ = new ArrayList<>();
		shots = new ArrayList<>();
		enemies = new ArrayList<>();
		player = new Player(WIDTH / 2, HEIGHT - PLAYER_SIZE, PLAYER_SIZE, PLAYER_IMG);
		score = 0;
		IntStream.range(0, MAX_ENNEMIES).mapToObj(i -> this.newEnemy()).forEach(enemies::add);
		timeline.play();
	}

	/**
	 * Le run de l'application
	 * @param gc interface graphique canvas
	 */
	private void run(GraphicsContext gc) {
		gc.setFill(Color.grayRgb(20));
		gc.fillRect(0, 0, WIDTH, HEIGHT);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setFont(Font.font(20));
		gc.setFill(Color.WHITE);
		gc.fillText("Score: " + score, 60,  20);
	
		
		if(gameOver) {
			for (int i = 0; i >= enemies.size() - 1; i--){
				enemies.get(i).explode();
			}
			if(enemies.isEmpty()){
				try {
					Thread.sleep(2000);
					timeline.pause();
				} catch (InterruptedException ex) {
					ex.getCause();
				}
			}

			try {
				Font.loadFont(Aguraki.class.getResource("/fonts/JAPANESE_2020.ttf").toExternalForm(), 100);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			gc.setFont(Font.font("Japanese 2020", 30));
			gc.fillRect(0, 0, WIDTH, HEIGHT);
			gc.setTextAlign(TextAlignment.CENTER);
			gc.setFill(Color.DARKRED);
			//text.setText("LOSE" + "\n Ton score est de : " + score + " !\n Clique sur ESPACE pour rejouer.");
			gc.fillText("LOSE" + "\n Ton score est de : " + score + " !\n Clique sur ESPACE pour rejouer.", WIDTH / 2.0, HEIGHT / 2.0);

			//Relancer le jeu en fin de partie
			scene.setOnKeyPressed(e-> {
						if (e.getCode() == KeyCode.SPACE) {
							gameOver = false;
							setup();
						}
					});

			//gc.setFont(Font.font(35));
			//gc.setFill(Color.YELLOW);
			//gc.fillText("Game Over \n Your Score is: " + score + " \n Click to play again", WIDTH / 2.0, HEIGHT /2.5);
		}
		univ.forEach(o -> o.draw(gc));

		player.update();
		player.draw(gc);

		enemies.stream().peek(Player::update).peek(o -> o.draw(gc)).forEach(e -> {
			if(player.colide(e) && !player.exploding) {
				player.explode();
			}
		});
		
		
		for (int i = shots.size() - 1; i >=0 ; i--) {
			Shot shot = shots.get(i);
			if(shot.posY < 0 || shot.toRemove)  { 
				shots.remove(i);
				continue;
			}
			shot.update();
			shot.draw(gc);
			for (Enemy enemy : enemies) {
				if(shot.colide(enemy) && !enemy.exploding) {
					score++;
					enemy.explode();
					shot.toRemove = true;
				}
			}
		}
		
		for (int i = enemies.size() - 1; i >= 0; i--){
			if(enemies.get(i).destroyed)  {
				enemies.set(i, newEnemy());
			}
		}
	
		gameOver = player.destroyed;
		if(RAND.nextInt(10) > 2) {
			univ.add(new Universe(RAND, WIDTH));
		}
		for (int i = 0; i < univ.size(); i++) {
			if(univ.get(i).posY > HEIGHT)
				try {
					univ.remove(i);
				}catch (ArrayIndexOutOfBoundsException ex){
					ex.getCause();
				}
		}
	}

	/**
	 * @return Un nouvel ennemi
	 */
	Enemy newEnemy() {
		return new Enemy(50 + RAND.nextInt(WIDTH - 100), 0, PLAYER_SIZE, ENEMIES_IMG[RAND.nextInt(ENEMIES_IMG.length)]);
	}

	public void playerMove(Scene scene) {

		scene.setOnKeyPressed(e->{
			if(e.getCode() == KeyCode.D) {
				player.setPosX(player.getPosX() + 5);
			}
			if(e.getCode() == KeyCode.A) {
				player.setPosX(player.getPosX() - 5);
			}
			if(e.getCode() == KeyCode.SPACE) {
				if(shots.size() < MAX_SHOTS) shots.add(player.shoot());
				if(gameOver) {
					gameOver = false;
					setup();
				}
			}
		});
	}

	/**
	 *
	 * @param x1 position X - objet 1
	 * @param y1 position Y - objet 1
	 * @param x2 position X - objet 2
	 * @param y2 position Y - objet 2
	 * @return La distance entre les deux objets
	 */
	public static int distance(int x1, int y1, int x2, int y2) {
		return (int) Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
	}

	public static void main(String[] args) {
		launch();
	}
}
