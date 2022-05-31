import javafx.application.Platform;

import javax.imageio.ImageIO;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Asteroid extends GameObject {
    private final int gravity;
    private final HealthBar healthBar;
    private int currentHealth;
    private final Enum<PowerupEnum> containsPowerUp;
    private final BufferedImage[] powerupImages = new BufferedImage[2];
    private final List<PowerUp> powerUpsList = new ArrayList<>();

    private boolean flagForWarHead = false;
    private boolean flagForBullet = false;

    public Asteroid(Point2D position, BufferedImage image, int health, int gravity) {
        super(position, image);
        this.currentHealth = health;
        this.gravity = gravity;

        // Creates healthbar above the asteroid.
        BufferedImage[] visualHealth = new BufferedImage[10];
        try {
            BufferedImage temp = ImageIO.read(Objects.requireNonNull(getClass().getResource("healthbar.png")));
            for (int i = 0; i < 10; i++) {
                final Point2D size = new Point2D.Double(80, 10);
                visualHealth[i] = temp.getSubimage(0, (int) (i * size.getY()), (int) size.getX(), (int) size.getY());
            }

            temp = ImageIO.read(Objects.requireNonNull(getClass().getResource("powerups.png")));
            for (int i = 0; i < 2; i++) {
                powerupImages[i] = temp.getSubimage(i * 30, 0, 30, 30);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.healthBar = new HealthBar(new Point2D.Double(position.getX() + 10, position.getY() - 10), gravity, currentHealth, visualHealth);

        if (Main.RANDOM.nextBoolean()) {
            if (Main.RANDOM.nextBoolean()) {
                System.out.println("Contains rocket");
                this.containsPowerUp = PowerupEnum.ROCKET;
            } else {
                System.out.println("Contains turret");
                this.containsPowerUp = PowerupEnum.AUTOTURRET;
            }
        } else {
            this.containsPowerUp = PowerupEnum.FALSE;
        }
    }

    @Override
    public void update(double deltaTime) {
        setPosition(new Point2D.Double(getPosition().getX(), getPosition().getY() + gravity));
        this.healthBar.setHealth(this.currentHealth);
        this.healthBar.update(deltaTime);

        // If the asteroid hits the floor it's game over.
        if (getPosition().getY() + 100 >= 600) {
            Platform.exit();
        }
    }

    public List<WarHead> isHitByWarHead(List<WarHead> warHeadList) {
        for (WarHead warHead : warHeadList) {
            if (warHead.getPosition().getX() >= getPosition().getX() && warHead.getPosition().getX() <= getPosition().getX() + 100) {
                if (warHead.getPosition().getY() <= getPosition().getY() + 100) {
                    this.currentHealth = 0;
                    warHead.hit();

                    if (this.currentHealth <= 0) {
                        flagForWarHead = true;
                    }
                }
            }
        }
        if (flagForWarHead) {
            droppingPowerUp();
        }
        return warHeadList;
    }

    // Checks if a bullet hit an asteroid.
    public List<Bullet> isHit(List<Bullet> bulletList) {
        for (Bullet bullet : bulletList) {
            if (bullet.getPosition().getX() >= getPosition().getX() && bullet.getPosition().getX() <= getPosition().getX() + 100) {
                if (bullet.getPosition().getY() <= getPosition().getY() + 100) {
                    this.currentHealth--;
                    bullet.hit();

                    if (this.currentHealth <= 0) {
                        flagForBullet = true;
                    }
                }
            }
        }
        if (flagForBullet) {
            droppingPowerUp();
        }
        return bulletList;
    }

    public int getHealth() {
        return currentHealth;
    }

    public HealthBar getHealthBar() {
        return healthBar;
    }

    public List<PowerUp> getPowerUpsList() {
        return powerUpsList;
    }

    private void droppingPowerUp() {
        if (this.containsPowerUp.equals(PowerupEnum.AUTOTURRET)) {
            this.powerUpsList.add(new PowerUp(new Point2D.Double(getPosition().getX() + 35, getPosition().getY()), this.powerupImages[1], PowerupEnum.AUTOTURRET));
        } else if (this.containsPowerUp.equals(PowerupEnum.ROCKET)) {
            this.powerUpsList.add(new PowerUp(new Point2D.Double(getPosition().getX() + 35, getPosition().getY()), this.powerupImages[0], PowerupEnum.ROCKET));
        }
    }
}