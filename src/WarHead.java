import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class WarHead extends GameObject {
    private boolean hitTarget = false;

    public WarHead(Point2D position, BufferedImage image) {
        super(position, image);
    }

    @Override
    public void update(double deltaTime) {
        setPosition(new Point2D.Double(getPosition().getX(), getPosition().getY() - 7));
    }

    public boolean HitTarget() {
        return hitTarget;
    }

    public void hit() {
        this.hitTarget = true;
    }
}
