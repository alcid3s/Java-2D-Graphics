import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Main extends Application {
    private BufferedImage playerImage;
    private Plane player;
    private FXGraphics2D g2d;
    private Scene scene;

    private int score = 0;
    private int beginHealth = 20;
    private int frequency = 5000;
    private long nextPhase = System.currentTimeMillis() + 20000;
    private long delay = System.currentTimeMillis() + frequency;
    private List<Asteroid> asteroidList;
    private List<PowerUp> dropPowerupsList = new ArrayList<>();
    private BufferedImage asteroidImage;
    public static final Random RANDOM = new Random();

    public static void main(String[] args) {
        launch(Main.class);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane mainPane = new BorderPane();
        Canvas canvas = new Canvas(800, 600);
        this.g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        mainPane.setCenter(canvas);
        this.scene = new Scene(mainPane);
        this.player = new Plane(new Point2D.Double(canvas.getWidth() / 2, canvas.getHeight() - 200), this.playerImage);
        this.asteroidList = new ArrayList<>();

        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1)
                    last = now;
                update((now - last) / 1000000000.0);
                last = now;
                draw(g2d);
            }
        }.start();

        draw(g2d);

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Space invaders");
        primaryStage.show();
    }

    private void update(double deltaTime) {
        nextPhase(System.currentTimeMillis());
        this.player.update(deltaTime, this.scene);

        if (System.currentTimeMillis() >= this.delay) {
            this.delay = System.currentTimeMillis() + frequency;
            this.asteroidList.add(new Asteroid(new Point2D.Double((Main.RANDOM.nextInt(7) * 100), 0),
                    this.asteroidImage, this.beginHealth, 1));
        }


        if (!asteroidList.isEmpty()) {

            /* If the asteroid is dead, it should be removed. If done in the for-loop it'll give an ConcurrentModificationException,
              therefore a list can keep all items that should be removed. And after the for-loop all items in that list will be removed.
             */
            List<Asteroid> toRemove = new ArrayList<>();
            for (Asteroid asteroid : asteroidList) {
                asteroid.update(deltaTime);
                this.player.setBulletList(asteroid.isHit(this.player.getBulletList()));
                this.player.setWarHeadList(asteroid.isHitByWarHead(this.player.getWarHeadList()));

                if (asteroid.getHealth() <= 0) {
                    if (!asteroid.getPowerUpsList().isEmpty()) {
                        this.dropPowerupsList = asteroid.getPowerUpsList();
                    }
                    toRemove.add(asteroid);
                    this.score += 100;
                }
            }
            this.asteroidList.removeAll(toRemove);
        }

        // For power ups dropping down
        if (!this.dropPowerupsList.isEmpty()) {
            this.dropPowerupsList.forEach(powerUp -> powerUp.update(deltaTime));
        }

        List<PowerUp> toRemovePowerUp = new ArrayList<>();
        List<PowerUp> pickedUp = new ArrayList<>();

        // If the player flew against the powerup, the powerup should be added to the player and removed from the dropPowerupsList.
        for (PowerUp powerUp : dropPowerupsList) {
            if (powerUp.getPosition().getX() >= this.player.getPosition().getX() && powerUp.getPosition().getX() <= this.player.getPosition().getX() + 100) {
                if (powerUp.getPosition().getY() >= this.player.getPosition().getY() && powerUp.getPosition().getY() <= 630) {
                    toRemovePowerUp.add(powerUp);
                    pickedUp.add(powerUp);
                }
            }
        }

        List<PowerUp> temp = this.player.getPowerUpList();
        temp.addAll(pickedUp);
        this.player.setPowerUpList(temp);
        this.dropPowerupsList.removeAll(toRemovePowerUp);
    }

    public void init() {
        try {
            this.playerImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("fighter.png")));
            this.asteroidImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("asteroid.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void draw(FXGraphics2D graphics) {
        graphics.clearRect(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        graphics.setBackground(Color.BLACK);

        graphics.setColor(Color.GREEN);
        graphics.setFont(new Font("Arial", Font.PLAIN, 20));
        graphics.drawString("Score: " + this.score, 670, 30);

        this.player.draw(graphics);

        // For powerups dropping down
        if (!this.dropPowerupsList.isEmpty()) {
            this.dropPowerupsList.forEach(powerUp -> powerUp.draw(graphics));
        }

        if (!asteroidList.isEmpty()) {
            for (Asteroid asteroid : asteroidList) {
                asteroid.draw(graphics);
                asteroid.getHealthBar().draw(graphics);
            }
        }
    }

    /**
     * Every 20 seconds a new fase of the game starts to make it harder.
     *
     * @param millis gives the amount of milliseconds the computer is running, it's constantly updated.
     */
    private void nextPhase(long millis) {
        if (millis >= nextPhase) {
            this.score += 1000;
            this.nextPhase = System.currentTimeMillis() + 20000;
            if (this.frequency != 2000) {
                this.frequency -= 500;
            }
            this.beginHealth += 5;
        }
    }
}