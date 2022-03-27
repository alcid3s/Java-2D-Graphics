import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Particle extends Structure {

    public Particle(Point2D position, BufferedImage image) {
        super(position, image);
    }

    /*
    Randomizes particle image, location and gravity so the outburst of the plane gets an effect.
     */
    @Override
    public void update(double deltaTime) {
        if (new Random().nextBoolean()) {
            setPosition(new Point2D.Double(getPosition().getX() - new Random().nextInt(3), getPosition().getY() + new Random().nextInt(3)));
        } else {
            setPosition(new Point2D.Double(getPosition().getX() + new Random().nextInt(3), getPosition().getY() + new Random().nextInt(3)));
        }
    }
}
