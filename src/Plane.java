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

public class Plane {
    private BufferedImage[] particles;
    private final BufferedImage playerImage;
    private BufferedImage bulletImage;
    private BufferedImage warHeadImage;
    private List<Bullet> bulletList = new ArrayList<>();
    private final List<Particle> particleList = new ArrayList<>();
    private List<WarHead> warHeadList = new ArrayList<>();
    private List<PowerUp> powerUpList = new ArrayList<>();
    private final List<Particle> explosiveParticleList = new ArrayList<>();
    private Point2D position;
    private final Timer timer;

    private final int size = 10;
    private boolean autofire = false;


    public Plane(Point2D position, BufferedImage image) {
        this.position = position;
        this.playerImage = image;
        this.timer = new Timer(2000);

        try {
            this.bulletImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("bullet.png")));

            BufferedImage tempImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("particles.png")));
            this.particles = new BufferedImage[3];

            for (int i = 0; i < 3; i++) {
                this.particles[i] = tempImage.getSubimage(i * size, 0, size, size);
            }

            this.warHeadImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("Warhead.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(double deltaTime, Scene scene) {
        updatePosition(scene);

        List<WarHead> toRemoveWarHead = new ArrayList<>();
        if (!warHeadList.isEmpty()) {
            for (WarHead warHead : warHeadList) {
                if (warHead.HitTarget()) {
                    explosion(new Point2D.Double(warHead.getPosition().getX(), warHead.getPosition().getY() - 50));
                    toRemoveWarHead.add(warHead);
                } else if (warHead.getPosition().getY() <= -80) {
                    toRemoveWarHead.add(warHead);
                } else {
                    warHead.update(deltaTime);
                }
            }
        }

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

        this.particleList.add(new Particle(new Point2D.Double(this.position.getX() + 45, this.position.getY() + 120),
                this.particles[Main.RANDOM.nextInt(3)], false));

        List<Particle> toRemoveParticles = new ArrayList<>();
        for (Particle particle : particleList) {
            if (particle.getPosition().getY() <= size * -1) {
                toRemoveParticles.add(particle);
            } else {
                particle.update(deltaTime);
            }
        }

        warHeadList.removeAll(toRemoveWarHead);
        bulletList.removeAll(toRemoveBullets);
        particleList.removeAll(toRemoveParticles);

        if (autofire && System.currentTimeMillis() <= timer.getDelay()) {
            bulletList.add(new Bullet(new Point2D.Double(this.position.getX() + 14, this.position.getY() + 70), this.bulletImage)); // left
            bulletList.add(new Bullet(new Point2D.Double(this.position.getX() + 50, this.position.getY()), this.bulletImage)); // middle
            bulletList.add(new Bullet(new Point2D.Double(this.position.getX() + 86, this.position.getY() + 70), this.bulletImage)); //right
        } else {
            this.autofire = false;
        }

        if (!explosiveParticleList.isEmpty()) {
            List<Particle> toRemoveExplosiveParticle = new ArrayList<>();
            for (Particle particle : explosiveParticleList) {
                particle.update(deltaTime);
                if (particle.isRemoveable()) {
                    toRemoveExplosiveParticle.add(particle);
                }
            }
            explosiveParticleList.removeAll(toRemoveExplosiveParticle);
        }
    }

    private void explosion(Point2D position) {
        for (int i = 0; i < 100; i++) {
            explosiveParticleList.add(new Particle(position, particles[Main.RANDOM.nextInt(3)], true));
        }
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
                case E:
                    if (!this.powerUpList.isEmpty()) {
                        for (int i = 0; i < this.powerUpList.size(); i++) {
                            if (this.powerUpList.get(i).getType().equals(PowerupEnum.ROCKET)) {
                                this.warHeadList.add(new WarHead(new Point2D.Double(this.position.getX() + 35, this.position.getY()), this.warHeadImage));
                                this.powerUpList.remove(this.powerUpList.get(i));
                                i = this.powerUpList.size() + 10;

                            }
                        }
                    }
                    break;
                case F:
                    if (!this.powerUpList.isEmpty()) {
                        for (int i = 0; i < this.powerUpList.size(); i++) {
                            if (this.powerUpList.get(i).getType().equals(PowerupEnum.AUTOTURRET)) {
                                timer.resetTimer();
                                this.autofire = true;
                                this.powerUpList.remove(this.powerUpList.get(i));
                                i = powerUpList.size() + 10;
                            }
                        }
                    }
                    break;
                /*
                 * Commented code used for debugging for powerup list.
                 */
//                case T:
//                    if (this.powerUpList.isEmpty()) {
//                        System.out.println("List is empty");
//                    } else {
//                        System.out.println("List with powerups: ");
//                        this.powerUpList.forEach(powerUp -> {
//                            System.out.println(powerUp.toString());
//                        });
//                    }
//                    break;
            }
        });
    }

    public List<Bullet> getBulletList() {
        return this.bulletList;
    }

    public void setBulletList(List<Bullet> bulletList) {
        this.bulletList = bulletList;
    }

    public void setWarHeadList(List<WarHead> warHeadList) {
        this.warHeadList = warHeadList;
    }

    public List<WarHead> getWarHeadList() {
        return warHeadList;
    }

    public void draw(FXGraphics2D graphics) {
        if (!warHeadList.isEmpty()) {
            this.warHeadList.forEach(warHead -> warHead.draw(graphics));
        }

        AffineTransform tx = new AffineTransform();
        tx.translate(this.position.getX(), this.position.getY());
        graphics.drawImage(this.playerImage, tx, null);

        if (!bulletList.isEmpty()) {
            this.bulletList.forEach(bullet -> bullet.draw(graphics));
        }

        if (!explosiveParticleList.isEmpty()) {
            this.explosiveParticleList.forEach(particle -> particle.draw(graphics));
        }

        if (!particleList.isEmpty()) {
            this.particleList.forEach(particle -> particle.draw(graphics));
        }
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPowerUpList(List<PowerUp> powerUpList) {
        this.powerUpList = powerUpList;
    }

    public List<PowerUp> getPowerUpList() {
        return powerUpList;
    }
}
