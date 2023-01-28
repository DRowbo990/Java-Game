package a10;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Define an object to hold an animatable series of images and create methods to
 * check for overlap between Sprites.
 * 
 * @author dejohnso
 *
 */
public class Sprite
{

	private ArrayList<BufferedImage> images;
	private Point2D.Double position; // Define the upper left corner of where the image is drawn.
	private Point2D.Double hitbox; // Define a width and height of the rectangle used for collision.

	/**
	 * A sprite is a sequence of images. A new Sprite always has at least one image
	 * (passed into the constructor). There is an assumption that these images are
	 * all the same size.
	 * 
	 * @param startingPosition the upper left corner of the image on the screen.
	 * @param initHitbox       the width and height of the rectangle to be used for
	 *                         collision detection.
	 * @param img
	 */
	public Sprite(Point2D.Double startingPosition, Point2D.Double initHitbox, BufferedImage img)
	{
		position = startingPosition;
		hitbox = initHitbox;
		images = new ArrayList<BufferedImage>();
		images.add(img);
	}
	
	/***
	 * Add img to the ArrayList of images
	 * 
	 * @param img
	 */
	public void add(BufferedImage img)
	{
		images.add(img);
	}

	/**
	 * Get the image at frameNumber. If frameNumber would be out-of-bounds then mod
	 * it with the number of images.
	 * 
	 * The idea is that we can track the overall frame count of the game, pass it
	 * in, and this would cycle through the sprite images.
	 * 
	 * @param frameNumber
	 * @return the image at frameNumber mod the number of frames.
	 */
	public BufferedImage get(int frameNumber)
	{
		return images.get(frameNumber % images.size());
	}

	/**
	 * @return the upper left corner of the sprite's position.
	 */
	public Point2D.Double getPosition()
	{
		return position;
	}

	/**
	 * Set the upper left corner of the sprite to newPosition.
	 * 
	 * @param newPosition - a Point2D holds an x and y coordinate.
	 */
	public void moveTo(Point2D.Double newPosition)
	{
		position = newPosition;
	}

	/**
	 * Change the current position by offset amount.
	 * 
	 * @param offset - a Point2D holds an x and y coordinate.
	 */
	public void shiftPosition(Point2D.Double offset)
	{
		position.setLocation(position.getX() + offset.getX(), position.getY() + offset.getY());
	}

	/**
	 * This returns the width and height of the collision rectangle, not a fully
	 * defined rectangle at some location.
	 * 
	 * @return the Point2D holding the width and height.
	 */
	public Point2D.Double getHitbox()
	{
		return hitbox;
	}

	/**
	 * Draw the image at its position.
	 * 
	 * @param g           - passed from the drawComponent override of the
	 *                    application
	 * @param frameNumber - defines which image to draw.
	 */
	public void draw(Graphics g, int frameNumber)
	{
		g.drawImage(get(frameNumber), (int) position.getX(), (int) position.getY(), null);
	}

	/**
	 * See if point is within the rectangle from position to position + hitbox
	 * 
	 * @param point
	 * @return true if the point is in the rectangle and false otherwise.
	 */
	public boolean isCollidingPoint(Point2D.Double point)
	{
		return point.getX() >= position.getX() && point.getX() <= position.getX() + hitbox.getX() && // within x range
				point.getY() >= position.getY() && point.getY() <= position.getY() + hitbox.getY(); // within y range
	}

	/**
	 * Check if the hitbox of this sprite overlaps the hitbox of an other sprite.
	 * The basic approach is to see if one is totally to the left, right, above, or
	 * below the other sprite. If it is, it is not overlapping.
	 * 
	 * @param other - the other sprite
	 * @return the collision status.
	 */
	public boolean isCollidingOther(Sprite other)
	{
		// See if this rectangle is above the other
		if (position.getY() + hitbox.getY() < other.position.getY())
			return false;
		// See if this rectangle is below the other
		if (position.getY() > other.position.getY() + other.hitbox.getY())
			return false;

		// See if this rectangle is left of the other
		if (position.getX() + hitbox.getX() < other.position.getX())
			return false;
		// See if this rectangle is right of the other
		if (position.getX() > other.position.getX() + other.hitbox.getX())
			return false;

		// If it is not above or below or left or right of the other, it is colliding.
		return true;
	}

}
