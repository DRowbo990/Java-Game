package a10;

/**
 * @author dejohnso
 *
 * The attack interface means that the class that implements it is
 * capable of checking for collision with other and doing damage to it.
 * This is not a reciprocal method - this attacking other doesn't mean
 * other checks to attack this. That would have to be called directly.
 */
public interface Attack {
	public void attack(Actor other);
}
