import org.jfree.fx.FXGraphics2D;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public abstract class GameObject {
    private Point2D position;
    private BufferedImage image;

    public GameObject(Point2D position, BufferedImage image) {
        this.position = position;
        this.image = image;
    }

    public void draw(FXGraphics2D graphics) {
        AffineTransform tx = new AffineTransform();
        tx.translate(getPosition().getX(), getPosition().getY());
        graphics.drawImage(getImage(), tx, null);
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public Point2D getPosition() {
        return position;
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public abstract void update(double deltaTime);
}
