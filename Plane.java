package a10;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class Plane extends Actor
{

	public Plane(Point2D.Double startingPosition, Point2D.Double initHitbox, BufferedImage img, int health,
			int coolDown, int attackDamage)
	{
		super(startingPosition, initHitbox, img, health, coolDown, 0, attackDamage);
	}

	/**
	 * An attack means the two hotboxes are overlapping and the Actor is ready to
	 * attack again (based on its cooldown).
	 * 
	 * Plane only attack badPlane.
	 * 
	 * @param other
	 */
	@Override
	public void attack(Actor other)
	{
		if (other instanceof BadPlane || other instanceof BadSpecialPlane)
			super.attack(other);
	}
}
