import javafx.scene.Scene;
import org.jfree.fx.FXGraphics2D;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Plane {
    private BufferedImage[] particles;
    private final BufferedImage playerImage;
    private BufferedImage bulletImage;
    private List<Bullet> bulletList;
    private List<Particle> particleList;
    private Point2D position;

    private final int size = 10;


    public Plane(Point2D position, BufferedImage image) {
        this.position = position;
        this.playerImage = image;

        try {
            this.bulletImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("bullet.png")));
            this.bulletList = new ArrayList<>();

            BufferedImage tempImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("particles.png")));
            this.particles = new BufferedImage[3];
            for (int i = 0; i < 3; i++) {
                this.particles[i] = tempImage.getSubimage(i * size, 0, size, size);
            }
            this.particleList = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(double deltaTime, Scene scene) {
        updatePosition(scene);

        List<Bullet> toRemoveBullets = new ArrayList<>();
        if (!bulletList.isEmpty()) {
            for (Bullet bullet : bulletList) {
                if (bullet.hitTarget() || bullet.getPosition().getY() <= -6) {
                    toRemoveBullets.add(bullet);
                } else {
                    bullet.update(deltaTime);
                }
            }
        }

        particleList.add(new Particle(new Point2D.Double(this.position.getX() + 45, this.position.getY() + 120),
                this.particles[new Random().nextInt(3)]));

        List<Particle> toRemoveParticles = new ArrayList<>();
        for (Particle particle : particleList) {
            if (particle.getPosition().getY() <= size * -1) {
                toRemoveParticles.add(particle);
            } else {
                particle.update(deltaTime);
            }
        }

        bulletList.removeAll(toRemoveBullets);
        particleList.removeAll(toRemoveParticles);
    }

    // Checks for interaction from the user.
    private void updatePosition(Scene scene) {
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case A:
                    if (this.position.getX() - 100 >= 0) {
                        this.position = new Point2D.Double(this.position.getX() - 100, this.position.getY());
                    }
                    break;
                case D:
                    if (this.position.getX() + 100 <= 700) {
                        this.position = new Point2D.Double(this.position.getX() + 100, this.position.getY());
                    }
                    break;
                case SPACE:
                    bulletList.add(new Bullet(new Point2D.Double(this.position.getX() + 14, this.position.getY() + 70), this.bulletImage)); // left
                    bulletList.add(new Bullet(new Point2D.Double(this.position.getX() + 50, this.position.getY()), this.bulletImage)); // middle
                    bulletList.add(new Bullet(new Point2D.Double(this.position.getX() + 86, this.position.getY() + 70), this.bulletImage)); //right
                    break;
            }
        });
    }

    public List<Bullet> getBulletList() {
        return this.bulletList;
    }

    public void setBulletList(List<Bullet> bulletList) {
        this.bulletList = bulletList;
    }

    public void draw(FXGraphics2D graphics) {
        AffineTransform tx = new AffineTransform();
        tx.translate(this.position.getX(), this.position.getY());
        graphics.drawImage(this.playerImage, tx, null);

        if (!bulletList.isEmpty()) {
            for (Bullet bullet : bulletList) {
                bullet.draw(graphics);
            }
        }

        if (!particleList.isEmpty()) {
            for (Particle particle : particleList) {
                particle.draw(graphics);
            }
        }
    }
}
