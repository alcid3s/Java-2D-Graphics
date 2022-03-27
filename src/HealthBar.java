import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class HealthBar extends GameObject {
    private final int gravity;
    private int currentHealth;
    private final int startHealth;
    private final BufferedImage[] visualHealth;

    public HealthBar(Point2D position, int gravity, int health, BufferedImage[] visualHealth) {
        super(position, visualHealth[0]);
        this.gravity = gravity;
        this.currentHealth = health;
        this.startHealth = health;
        this.visualHealth = visualHealth;
    }

    @Override
    public void update(double deltaTime) {
        setPosition(new Point2D.Double(getPosition().getX(), getPosition().getY() + gravity));

        // Couldn't make it more beautiful, this will give a visualisation of how much health is left.
        if (percentage(this.currentHealth, this.startHealth) >= 90) {
            setImage(this.visualHealth[0]);
        } else if (percentage(this.currentHealth, this.startHealth) >= 80) {
            setImage(this.visualHealth[1]);
        } else if (percentage(this.currentHealth, this.startHealth) >= 70) {
            setImage(this.visualHealth[2]);
        } else if (percentage(this.currentHealth, this.startHealth) >= 60) {
            setImage(this.visualHealth[3]);
        } else if (percentage(this.currentHealth, this.startHealth) >= 50) {
            setImage(this.visualHealth[4]);
        } else if (percentage(this.currentHealth, this.startHealth) >= 40) {
            setImage(this.visualHealth[5]);
        } else if (percentage(this.currentHealth, this.startHealth) >= 30) {
            setImage(this.visualHealth[6]);
        } else if (percentage(this.currentHealth, this.startHealth) >= 20) {
            setImage(this.visualHealth[7]);
        } else if (percentage(this.currentHealth, this.startHealth) >= 10) {
            setImage(this.visualHealth[8]);
        } else {
            setImage(this.visualHealth[9]);
        }
    }

    public void setHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    private double percentage(double var1, double var2) {
        return (var1 / var2) * 100;
    }
}
