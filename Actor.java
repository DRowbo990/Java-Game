package a10;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * The actor class extends sprite to add more player character capabilities.
 * This class adds health, an attack strength, and a cool down counter for
 * activities that should not happen every frame.
 * 
 * @author dejohnso
 *
 */
public class Actor extends Sprite implements Attack
{

	private int health; // Current health of an Actor object
	private int fullHealth; // The max health if healed. Used in the drawn health bar.
	protected int attackDamage; // Damage this Actor does to another Actor.
	private int coolDownCounter;// Current count of the cooldown.
	private int coolDown; // Starting cool down value
	private double speed; // The speed at which it moves

	/**
	 * Construct a new Actor. It needs the info to make the Sprite part of itself.
	 * 
	 * @param startingPosition
	 * @param initHitbox
	 * @param img
	 * @param health
	 * @param coolDown
	 * @param speed
	 * @param attackDamage
	 */
	public Actor(Point2D.Double startingPosition, Point2D.Double initHitbox, BufferedImage img, int health,
			int coolDown, double speed, int attackDamage)
	{
		super(startingPosition, initHitbox, img);
		this.health = health;
		this.fullHealth = health;
		this.coolDownCounter = coolDown;
		this.coolDown = coolDown;
		this.speed = speed;
		this.attackDamage = attackDamage;
	}

	/**
	 * Only moves in x.
	 */
	public void move()
	{
		shiftPosition(new Point2D.Double(speed, 0));
	}

	/**
	 * Getter for speed
	 * 
	 * @return the speed
	 */
	public double getSpeed()
	{
		return speed;
	}

	/**
	 * Count down the cool down.
	 */
	public void decrementCooldown()
	{
		coolDownCounter--;
	}

	/**
	 * Update the internal state of the Actor. This means decrement the cool down
	 * counter.
	 */
	public void update()
	{
		decrementCooldown();
	}

	/**
	 * If the cooldown counter hits 0, the Actor is ready to do something.
	 * 
	 * @return
	 */
	public boolean readyForAction()
	{
		if (coolDownCounter <= 0)
			return true;
		return false;
	}

	/**
	 * Reset the cool down counter to its starting value.
	 */
	public void resetCoolDown()
	{
		coolDownCounter = coolDown;
	}

	/**
	 * Modify the health by change value.
	 * 
	 * @param change
	 */
	public void changeHealth(int change)
	{
		health += change;
	}

	/**
	 * Check whether the Actor still has some health.
	 * 
	 * @return
	 */
	public boolean isAlive()
	{
		return health > 0;
	}

	/**
	 * Since we don't have an easy way of showing health using a nice series of
	 * images, provide health feedback with a health status bar.
	 * 
	 * @param g
	 */
	public void drawHealthBar(Graphics g)
	{
		Point2D.Double pos = this.getPosition();
		Point2D.Double box = this.getHitbox();
		g.setColor(Color.BLACK);
		g.drawRect((int) pos.getX(), (int) pos.getY() - 10, (int) box.getX(), 5);
		g.setColor(Color.RED);
		g.fillRect((int) pos.getX(), (int) pos.getY() - 10, (int) (box.getX() * this.health / (double) this.fullHealth),
				5);
	}

	/**
	 * If something should happen when the Actor is dead, do it here (or override
	 * it).
	 * 
	 * @param others
	 */
	public void removeAction(ArrayList<Actor> others)
	{
		
	}

//	/**
//	 * An Actor doesn't need to set collision status, but a badPlane can override this.
//	 * @param other
//	 */
	public void setCollisionStatus(Actor other)
	{
		
	}

	/**
	 * An attack means the two hotboxes are overlapping and the Actor is ready to
	 * attack again (based on its cooldown).
	 * 
	 * @param other
	 */
	@Override
	public void attack(Actor other)
	{
		if (this != other && this.isCollidingOther(other))
		{
			if (this.readyForAction())
			{
				other.changeHealth(-attackDamage);
				this.resetCoolDown();
			}
		}
	}

}
