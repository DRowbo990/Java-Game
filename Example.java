package a10;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
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

public class Example extends JPanel implements ActionListener, MouseListener
{

	private static final long serialVersionUID = 1L;
	private Timer timer;
	private ArrayList<Actor> actors; // Planes and badPlanes all go in here
	BufferedImage planeImage;
	BufferedImage specialPlaneImage;
	BufferedImage badPlaneImage;
	BufferedImage badSpecialPlaneImage;
	int numRows;
	int numCols;
	int cellSize;
	int resourceCounter = 250;
	int badPlaneCounter = 0;
	int killCount = 0;
	int specialThreshold = 99;
	int normalThreshold = 98;
	boolean isPlacing;
	String planeType;
	static JButton addPlane;
	static JButton addSpecialPlane;
	static JLabel resourceLabel;
	static JLabel killCountLabel;
	static JLabel endLabel;

	/**
	 * Setup the basic info for the example
	 */
	public Example()
	{
		super();
		addMouseListener(this);

		// Define some quantities of the scene
		numRows = 5;
		numCols = 7;
		cellSize = 75;
		setPreferredSize(new Dimension(50 + numCols * cellSize, 50 + numRows * cellSize));

		// Store all the Planes and badPlanes in here.
		actors = new ArrayList<>();

		// Load images
		try
		{
			planeImage = ImageIO.read(new File("src/a10/Animal-Icons/plane10.png"));
			specialPlaneImage = ImageIO.read(new File("src/a10/Animal-Icons/plane13.png"));
			badPlaneImage = ImageIO.read(new File("src/a10/Animal-Icons/plane8.png"));
			badSpecialPlaneImage = ImageIO.read(new File("src/a10/Animal-Icons/plane11.png"));
		} catch (IOException e)
		{
			System.out.println("A file was not found");
			System.exit(0);
		}

		addPlane = new JButton("Add Plane");
		addPlane.addActionListener(this);
		addSpecialPlane = new JButton("Add Special Plane");
		addSpecialPlane.addActionListener(this);
		resourceLabel = new JLabel();
		endLabel = new JLabel();
		killCountLabel = new JLabel();
		// The timer updates the game each time it goes.
		// Get the javax.swing Timer, not from util.
		timer = new Timer(30, this);
		timer.start();
	}

	/***
	 * Implement the paint method to draw the Planes
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
		resourceLabel.setText("Fuel : " + resourceCounter);
		killCountLabel.setText("Kills : " + killCount);

		if (badPlaneCounter >= 20)
		{
			badPlaneCounter = 0;
			normalThreshold--;
			specialThreshold--;
		}

		for (Actor badPlane : actors)
		{
			if (badPlane instanceof BadPlane || badPlane instanceof BadSpecialPlane)
			{
				if (badPlane.getPosition().x <= 0)
				{
					endLabel.setText("GAME OVER");
					endLabel.setForeground(Color.red);
					timer.stop();
				}
			}
		}

		if (e.getSource() == addPlane)
		{
			isPlacing = true;
			planeType = "regular";
		}
		if (e.getSource() == addSpecialPlane)
		{
			isPlacing = true;
			planeType = "special";
		}

		Random rand = new Random();
		int spawn = rand.nextInt(100);
		int col = rand.nextInt(numCols);
		col *= 50;
		if (spawn > specialThreshold)
		{

			BadSpecialPlane badSpecialPlane = new BadSpecialPlane(new Point2D.Double(500, col),
					new Point2D.Double(planeImage.getWidth(), planeImage.getHeight()), badSpecialPlaneImage, 100, 50,
					-2, 10);
			actors.add(badSpecialPlane);
		}

		else if (spawn > normalThreshold)
		{

			BadPlane badPlane = new BadPlane(new Point2D.Double(500, col),
					new Point2D.Double(planeImage.getWidth(), planeImage.getHeight()), badPlaneImage, 100, 50, -2, 10);
			actors.add(badPlane);
		}

		// This method is getting a little long, but it is mostly loop code
		// Increment their cooldowns and reset collision status
		for (Actor actor : actors)
		{
			actor.update();
		}

		// Try to attack
		for (Actor actor : actors)
		{
			for (Actor other : actors)
			{
				actor.attack(other);
			}
		}

		// Remove planes and badPlanes with low health
		ArrayList<Actor> nextTurnActors = new ArrayList<>();
		for (Actor actor : actors)
		{
			if (actor.isAlive())
				nextTurnActors.add(actor);
			else
			{
				if (actor instanceof BadPlane || actor instanceof BadSpecialPlane)
				{
					killCount++;
					badPlaneCounter++;
					resourceCounter += 25;
				}
				actor.removeAction(actors); // any special effect or whatever on removal
			}
		}

		actors = nextTurnActors;

		// Check for collisions between badPlanes and planes and set collision status
		for (Actor actor : actors)
		{
			for (Actor other : actors)
			{
				actor.setCollisionStatus(other);
			}
		}

		// Move the actors.
		for (Actor actor : actors)
		{
			actor.move(); // for badPlanes, only moves if not colliding.
		}

		// Redraw the new scene
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
				JFrame app = new JFrame("Planes Test");
				app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				Example panel = new Example();
				panel.setLayout(new BorderLayout());

				JPanel panel1 = new JPanel();

				panel1.add(addPlane, BorderLayout.SOUTH);
				panel1.add(addSpecialPlane, BorderLayout.SOUTH);
				panel1.add(resourceLabel, BorderLayout.SOUTH);
				panel1.add(killCountLabel, BorderLayout.SOUTH);
				panel1.add(endLabel, BorderLayout.SOUTH);
				panel.add(panel1, BorderLayout.SOUTH);
				app.setContentPane(panel);
				app.pack();
				app.setVisible(true);

			}
		});
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (isPlacing && resourceCounter > 0)
		{
			int x = e.getX();
			int y = e.getY();

			int row = (y) / 50;
			int col = (x) / 75;

			row = 50 * row;
			col = 75 * col;

			for (Actor actor : actors)
			{
				if (actor.isCollidingPoint(new Point2D.Double(x, y))
						&& !(actor instanceof BadPlane || actor instanceof BadSpecialPlane))
				{
					actor.changeHealth(-5000);
				}
			}

			if (planeType.equals("regular") && resourceCounter >= 50)
			{
				resourceCounter -= 50;
				Plane plane = new Plane(new Point2D.Double(col, row),
						new Point2D.Double(planeImage.getWidth(), planeImage.getHeight()), planeImage, 100, 5, 5);
				actors.add(plane);
			} else if (planeType.equals("special") && resourceCounter >= 100)
			{
				resourceCounter -= 100;
				SpecialPlane plane = new SpecialPlane(new Point2D.Double(col, row),
						new Point2D.Double(planeImage.getWidth(), planeImage.getHeight()), specialPlaneImage, 100, 5,
						1);
				actors.add(plane);
			}
			isPlacing = false;
		}
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{

	}

	@Override
	public void mouseEntered(MouseEvent e)
	{

	}

	@Override
	public void mouseExited(MouseEvent e)
	{

	}
}