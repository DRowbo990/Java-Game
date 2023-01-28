package a10;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.util.Random;

public class BadSpecialPlane extends Actor
{

	private boolean isColliding;

	public BadSpecialPlane(Double startingPosition, Double initHitbox, BufferedImage img, int health, int coolDown,
			double speed, int attackDamage)
	{
		super(startingPosition, initHitbox, img, health, coolDown, speed, attackDamage);
		isColliding = false;
		// TODO Auto-generated constructor stub
	}

	/**
	 * Move if not colliding. Only moves in x.
	 */
	@Override
	public void move()
	{
		if (!isColliding)
			shiftPosition(new Point2D.Double(getSpeed(), 0));
	}

	/**
	 * Set the collision status
	 * 
	 * @param collisionStatus
	 */
	public void setColliding(boolean collisionStatus)
	{
		isColliding = collisionStatus;
	}

	/**
	 * Get the collision status.
	 * 
	 * @return
	 */
	public boolean getColliding()
	{
		return isColliding;
	}

	/**
	 * Set collision status on this if it overlaps with other.
	 * 
	 * @param other
	 */
	@Override
	public void setCollisionStatus(Actor other)
	{
		Random rand = new Random();
		int specialAbility = rand.nextInt(100);

		if (specialAbility < 45)
		{
			if (other instanceof Plane && this.isCollidingOther(other)
					|| other instanceof SpecialPlane && this.isCollidingOther(other))
				setColliding(true);
		}
	}

	/**
	 * Update the internal state of the Actor. This means reset the collision status
	 * to false and decrement the cool down counter.
	 */
	public void update()
	{
		isColliding = false;
		decrementCooldown();
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
		if (other instanceof Plane || other instanceof SpecialPlane)
		{
			super.attack(other);
		}
	}
}
