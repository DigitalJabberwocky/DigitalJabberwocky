import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import java.io.File;

/**
 *  Note: Normally the ButtonPanel and DrawingArea would not be static classes.
 *  This was done for the convenience of posting the code in one class.
 */

public class DrawOnImage2
{	
	//List Containing the points for the line
	public static ArrayList<PointList> pts = new ArrayList<PointList>();
	public static Color newColor = Color.BLACK;
	public static String bodypart = null;
		
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
		frame.setBackground(Color.WHITE);
		frame.setSize(600, 600);
		frame.setLocationRelativeTo( null );
		frame.setVisible(true);
		frame.setResizable(false);
		
	}

	static class ButtonPanel extends JPanel implements ActionListener
	{
		private DrawingArea drawingArea;
		//creates button on the drawing area
		public ButtonPanel(DrawingArea drawingArea)
		{
			this.drawingArea = drawingArea;
			
			add(createButton("Load Image", null));
			add( createButton("Save Image",null) );
			add( createButton("Clear Drawing", null) );
			add( createButton("Choose Colour", null));
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
			else if ("Save Image".equals(e.getActionCommand()))
				drawingArea.save();
			else if("Choose Colour".equals(e.getActionCommand()))
				drawingArea.colour();
			else
				drawingArea.load();
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
			g2d.setColor(Color.WHITE);
		}

		public void clear()
		{
			pts.clear();
			createEmptyImage();
			repaint();	
		}

		public void save(){
			try{					
			ImageIO.write(image, "PNG", new File("Image.png"));
			}
			catch(Exception e){
				System.err.println(e.getMessage());
			}
		}
		
		public void load(){
			try{
				File f = new File("image.png");
				image = ImageIO.read(f);
			}
			catch(Exception e){
				System.err.println(e.getMessage());
			}
		}
		
		public void colour(){
						
			newColor = JColorChooser.showDialog(DrawingArea.this, "Pick a Color", Color.WHITE);
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
				pts.add(new PointList(pnt, newColor, bodypart));
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
