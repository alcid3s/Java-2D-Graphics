import javax.imageio.ImageIO;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Asteroid extends Structure {
    private final int gravity;
    private final HealthBar healthBar;
    private int currentHealth;
    private BufferedImage[] visualHealth;

    public Asteroid(Point2D position, BufferedImage image, int health, int gravity) {
        super(position, image);
        this.currentHealth = health;
        this.gravity = gravity;

        // Creates healthbar above the asteroid.
        try {
            this.visualHealth = new BufferedImage[10];
            BufferedImage temp = ImageIO.read(Objects.requireNonNull(getClass().getResource("healthbar.png")));

            for (int i = 0; i < 10; i++) {
                final Point2D size = new Point2D.Double(80, 10);
                this.visualHealth[i] = temp.getSubimage(0, (int) (i * size.getY()), (int) size.getX(), (int) size.getY());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.healthBar = new HealthBar(new Point2D.Double(position.getX() + 10, position.getY() - 10), visualHealth[0], gravity, currentHealth, visualHealth);
    }

    @Override
    public void update(double deltaTime) {
        setPosition(new Point2D.Double(getPosition().getX(), getPosition().getY() + gravity));
        this.healthBar.setHealth(this.currentHealth);
        this.healthBar.update(deltaTime);

        // If the asteroid hits the floor it's game over.
        if (getPosition().getY() + 100 >= 600) {
            System.exit(0);
        }
    }

    // Checks if a bullet hit an asteroid.
    public List<Bullet> isHit(List<Bullet> bulletList) {
        for (Bullet bullet : bulletList) {
            if (bullet.getPosition().getX() >= getPosition().getX() && bullet.getPosition().getX() <= getPosition().getX() + 100) {
                if (bullet.getPosition().getY() <= getPosition().getY() + 100) {
                    this.currentHealth--;
                    bullet.hit();
                }
            }
        }
        return bulletList;
    }

    public int getHealth() {
        return currentHealth;
    }

    public HealthBar getHealthBar() {
        return healthBar;
    }
}