import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;

/**
 *  Note: Normally the ButtonPanel and DrawingArea would not be static classes.
 *  This was done for the convenience of posting the code in one class and to
 *  highlight the differences between the two approaches. All the differences
 *  are found in the DrawingArea class.
 */
public class DrawOnImage2
{	
	//List Containing the points for the line
	public static ArrayList<PointList> pts = new ArrayList<PointList>();

	public static void main(String[] args)
	{
		//Creates and runs the interface
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	private static void createAndShowGUI()
	{
		//Creates an instance of the drawing area and a button
		DrawingArea drawingArea = new DrawingArea();
		ButtonPanel buttonPanel = new ButtonPanel( drawingArea );
		
		//Sets up the frame and adds the contents to it
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("Draw On Image");
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.getContentPane().add(drawingArea);
		frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		frame.setSize(400, 400);
		frame.setLocationRelativeTo( null );
		frame.setVisible(true);
	}

	static class ButtonPanel extends JPanel implements ActionListener
	{
		private DrawingArea drawingArea;
		//creates button on the drawing area
		public ButtonPanel(DrawingArea drawingArea)
		{
			this.drawingArea = drawingArea;

			add( createButton("	", Color.BLACK) );
			add( createButton("	", Color.RED) );
			add( createButton("	", Color.GREEN) );
			add( createButton("	", Color.BLUE) );
			add( createButton("	", Color.ORANGE) );
			add( createButton("     ",Color.YELLOW) );
			add( createButton("Clear Drawing", null) );
		}

		//creates the button
		private JButton createButton(String text, Color background)
		{
			JButton button = new JButton( text );
			button.setBackground( background );
			button.addActionListener( this );

			return button;
		}

		//determine what happens if a button is pressed
		public void actionPerformed(ActionEvent e)
		{
			JButton button = (JButton)e.getSource();

			if ("Clear Drawing".equals(e.getActionCommand()))
				drawingArea.clear();			
			else
				drawingArea.setForeground( button.getBackground() );
		}
	}

	static class DrawingArea extends JPanel
	{
		// creates an image, graphics and two points
		BufferedImage image;
		Graphics2D g2d;
		Point startPoint = null;
		Point endPoint = null;

		public DrawingArea()
		{
			setBackground(Color.WHITE);

			MyMouseListener ml = new MyMouseListener();
			addMouseListener(ml);
			addMouseMotionListener(ml);
		}
		
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);

			//  Custom code to support painting from the BufferedImage

			if (image == null)
			{
				createEmptyImage();
			}

			g.drawImage(image, 0, 0, null);
			
			//draws the line using the list
			for(int i = 0; i < (pts.size() - 1); i++){
				if(pts.get(i).getEnd() == true || pts.get(i+1).getEnd() == true ){}
				else{
				g.setColor(pts.get(i).getColor());	
				g.drawLine(pts.get(i).getPoint().x, pts.get(i).getPoint().y, pts.get(i+1).getPoint().x, pts.get(i+1).getPoint().y);				
				}
			}			
		}

		private void createEmptyImage()
		{
			image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
			g2d = (Graphics2D)image.getGraphics();
			g2d.setColor(Color.BLACK);
		}

		public void clear()
		{
			pts.clear();
			createEmptyImage();
			repaint();	
		}

		

		class MyMouseListener extends MouseInputAdapter
		{
			

			// gets starting point of where clicked
			public void mousePressed(MouseEvent e)
			{
				startPoint = e.getPoint();
				endPoint = startPoint;
				
			}

			public void mouseDragged(MouseEvent e)
			{		
				//gets the mouses location and adds to list before repainting the window		
				Point pnt = e.getPoint();
				pts.add(new PointList(pnt, e.getComponent().getForeground()));
				repaint();				
			}

			public void mouseReleased(MouseEvent e)
			{
				//  creates an end of line object and repaints window
				pts.add(new PointList());				
				repaint();
								

			}
			
			
		}
	}

}
