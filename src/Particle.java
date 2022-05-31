import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class Particle extends GameObject {
    private final boolean forExplosion;
    private boolean removeable;
    private int counter;

    public Particle(Point2D position, BufferedImage image, boolean forExplosion) {
        super(position, image);
        this.forExplosion = forExplosion;
        if (forExplosion) {
            this.counter = 0;
            this.removeable = false;
        }
    }

    /*
    Randomizes particle image, location and gravity so the outburst of the plane gets an effect.
     */
    @Override
    public void update(double deltaTime) {
        if (!forExplosion) {
            if (Main.RANDOM.nextBoolean()) {
                setPosition(new Point2D.Double(getPosition().getX() - Main.RANDOM.nextInt(3), getPosition().getY() + Main.RANDOM.nextInt(3)));
            } else {
                setPosition(new Point2D.Double(getPosition().getX() + Main.RANDOM.nextInt(3), getPosition().getY() + Main.RANDOM.nextInt(3)));
            }
        } else {
            this.counter++;
            int move = 40;
            if (Main.RANDOM.nextBoolean()) {
                setPosition(new Point2D.Double(getPosition().getX() - Main.RANDOM.nextInt(move), getPosition().getY() - Main.RANDOM.nextInt(move)));
                if (Main.RANDOM.nextBoolean()) {
                    setPosition(new Point2D.Double(getPosition().getX() - Main.RANDOM.nextInt(move), getPosition().getY() + Main.RANDOM.nextInt(move)));
                } else {
                    setPosition(new Point2D.Double(getPosition().getX() + Main.RANDOM.nextInt(move), getPosition().getY() + Main.RANDOM.nextInt(move)));
                }
            } else {
                setPosition(new Point2D.Double(getPosition().getX() + Main.RANDOM.nextInt(move), getPosition().getY() - Main.RANDOM.nextInt(move)));
            }
            if (counter >= 50) {
                this.removeable = true;
            }
        }
    }

    public boolean isRemoveable() {
        return removeable;
    }
}
