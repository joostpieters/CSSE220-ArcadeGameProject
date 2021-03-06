import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

/*
 * Hero character that is controlled by the arrow keys during the game
 * Collects Emeralds and coins for points
 * Dies when an enemy touches it.
 * Shoots out a bullet to kill a Monster
 * 
 */
public class Hero extends Character {
	private static final Color Hero_Color = Color.WHITE;
	private int score;
	private int lives;
	private Point2D initialPoint;
	private int deathTime = 0;
	private BufferedImage image = null;
	private BufferedImage filter = new BufferedImage(72, 72,
			BufferedImage.TYPE_4BYTE_ABGR);

	public Hero(World world, Point2D upLeftPoint) {
		super(world, upLeftPoint);
		this.setColor(Hero_Color);
		this.setSpeed(3);
		this.canDig = true;
		this.initialPoint = this.getPosition();
		try {
			image = ImageIO.read(new File("hero.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get the hero's score
	 * 
	 * @return score
	 */
	public int getScore() {
		return this.score;
	}

	/**
	 * add hero's score
	 * 
	 * @param score
	 *            to be added
	 */
	public void addScore(int score) {
		this.score += score;
	}

	/**
	 * set the hero's score
	 * 
	 * @param score
	 *            to be set
	 */
	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public void timePassed() {
		if (this.getIsAlive()) {
			this.setDirection(this.getWorld().keymanager.getDirection());

			// Handles what to do if it encounters a bag
			Bag bag = this.characterEncounteredWithBag();
			if (bag != null) {
				// Hero moving up or down
				if (this.getDirection() == 'r' || this.getDirection() == 'l') {
					return;
				}

				// Moves bag in correct direction
				int deltaX = 0;
				if (this.getPosition().getX() > bag.getPosition().getX())
					deltaX -= this.getSpeed();
				if (this.getPosition().getX() < bag.getPosition().getX())
					deltaX += this.getSpeed();
				bag.moveTo(new Point2D.Double(
						bag.getPosition().getX() + deltaX, bag.getPosition()
								.getY()));
			} else {
				// If this runs, the movement is available and moves the Hero
				this.move(this.getDirection());
			}

			// Adds empty space if it doesn't already exist
			if (!this.getWorld().getTunnel().isInEmptySpace(this)) {
				this.getWorld()
						.getFootprintsToAdd()
						.add(new FootPrint(this.getWorld(), this.getPosition()));
			}
		} else {
			this.deathTime++;
		}
	}

	/**
	 * let the hero fire a bullet
	 */
	public void fire() {
		Point2D bulletPoint = null;
		switch (this.getHeadingDirection()) {
		case 'u':
			bulletPoint = new Point2D.Double(this.getPosition().getX() - 72,
					this.getPosition().getY());
			break;
		case 'd':
			bulletPoint = new Point2D.Double(this.getPosition().getX() + 72,
					this.getPosition().getY());
			break;
		case 'l':
			bulletPoint = new Point2D.Double(this.getPosition().getX(), this
					.getPosition().getY() - 72);
			break;
		case 'r':
			bulletPoint = new Point2D.Double(this.getPosition().getX(), this
					.getPosition().getY() + 72);
			break;
		}
		Bullet bullet = new Bullet(this.getWorld(), bulletPoint, this);
		this.getWorld().addBullet(bullet);
	}

	@Override
	public void die() {
		this.lives--;
		if (this.lives == 0) {
			this.setIsAlive(false);
		} else {
			this.setPosition(this.initialPoint);
			for (Monster m : this.getWorld().getEnemies()) {
				m.die();
			}
		}
	}

	/*
	 * Returns the number of lives of this hero
	 */
	public int getLives() {
		return this.lives;
	}

	/*
	 * Sets the number of lives of this hero
	 */
	public void setLives(int lives) {
		this.lives = lives;
	}

	/*
	 * Returns this hero's current death time
	 */
	public int getDeathTime() {
		return this.deathTime;
	}

	/*
	 * Sets this.deathTime to a new value
	 */
	public void setDeathTime(int deathTime) {
		this.deathTime = deathTime;
	}

	@Override
	public void drawOn(Graphics2D g) {
		super.drawOn(g);
		double rotateAngle = 0;
		switch (this.getHeadingDirection()) {
		case 'l':
			break;
		case 'r':
			rotateAngle = 180;
			break;
		case 'd':
			rotateAngle = 90;
			break;
		case 'u':
			rotateAngle = 270;
			break;
		}
		g.translate(this.getPosition().getX() + 36,
				this.getPosition().getY() + 36);
		g.rotate(Math.toRadians(rotateAngle));
		g.drawImage(image, -36, -36, 36, 36, 0, 0, image.getWidth(),
				image.getHeight(), null);
		g.rotate(-Math.toRadians(rotateAngle));
		g.translate(-this.getPosition().getX() - 36,
				-this.getPosition().getY() - 36);
	}

}
