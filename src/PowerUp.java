import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class PowerUp extends GameObject{
    private final PowerupEnum type;

    public PowerUp(Point2D position, BufferedImage image, PowerupEnum type) {
        super(position, image);
        this.type = type;
    }

    @Override
    public void update(double deltaTime) {
        setPosition(new Point2D.Double(getPosition().getX(), getPosition().getY() + 2));
    }

    public PowerupEnum getType() {
        return type;
    }
}
