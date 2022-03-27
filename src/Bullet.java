import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject {
    private boolean hitTarget = false;

    public Bullet(Point2D position, BufferedImage image) {
        super(position, image);
    }

    @Override
    public void update(double deltaTime) {
        setPosition(new Point2D.Double(getPosition().getX(), getPosition().getY() - 12));
    }

    public void hit() {
        this.hitTarget = true;
    }

    public boolean hitTarget() {
        return hitTarget;
    }
}