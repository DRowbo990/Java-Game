package a10;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("unused")
public class ExampleInClass extends JPanel implements ActionListener
{

	private static final long serialVersionUID = 1L;
	BufferedImage plantImage; 
	BufferedImage zombieImage;
	ArrayList<Actor> actors;

	/**
	 * Setup the basic info for the example
	 */
	public ExampleInClass()
	{
		super();
		setPreferredSize(new Dimension(600, 400));
		// Define some quantities of the scene

		// Load images
		try
		{
			plantImage = ImageIO.read(new File("src/a10/Animal-Icons/frog-icon.png"));
			zombieImage = ImageIO.read(new File("src/a10/Animal-Icons/chihuahua-icon.png"));
		} catch (IOException e)
		{
			System.out.println("A file was not found");
			System.exit(0);
		}

		Plane plane = new Plane(new Point2D.Double(200.0, 200.0),
				new Point2D.Double(plantImage.getWidth(), plantImage.getHeight()), plantImage, 100, 5, 1);
		BadPlane badPlane = new BadPlane(new Point2D.Double(500.0, 200.0),
				new Point2D.Double(zombieImage.getWidth(), zombieImage.getHeight()), zombieImage, 100, 50, -2, 10);

		actors = new ArrayList<>();
		actors.add(plane);
		actors.add(badPlane);

		Timer timer = new Timer(20, this);
		timer.start();
	}

	/***
	 * Implement the paint method to draw the actors
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		for (Actor actor : actors)
		{
			actor.draw(g, 0);
			actor.drawHealthBar(g);
		}
	}

	/**
	 * 
	 * This is triggered by the timer. It is the game loop of this test.
	 * 
	 * @param e
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		// move the characters
		// attack each other
		// get rid of dead actors
//		System.out.println("Updating");
		for (Actor actor : actors)
			actor.update();

		for (Actor actor : actors)
			for (Actor other : actors)
				actor.attack(other);

		for (Actor actor : actors)
			for (Actor other : actors)
				actor.setCollisionStatus(other);

		for (Actor actor : actors)
			actor.move();

		repaint();
	}

	/**
	 * Make the game and run it
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		// Schedule a job for the event-dispatching thread:
		javax.swing.SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				JFrame app = new JFrame("Plant and Zombie Test");
				app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				ExampleInClass panel = new ExampleInClass();

				app.setContentPane(panel);
				app.pack();
				app.setVisible(true);
			}
		});
	}

}