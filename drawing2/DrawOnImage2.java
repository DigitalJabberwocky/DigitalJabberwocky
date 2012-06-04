import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;

/**
 *  Note: Normally the ButtonPanel and DrawingArea would not be static classes.
 *  This was done for the convenience of posting the code in one class.
 */

public class DrawOnImage2
{	
	//List Containing the points for the line
	public static ArrayList<PointList> pts = new ArrayList<PointList>();
	public static ArrayList<PointList> hotspot = new ArrayList<PointList>();
	public static String[] part = new String[]{"head", "body", "larm", "rarm", "legs", ""};
	public static Color newColor = Color.BLACK;
	public static String bodypart = part[0];
	public static ListCombine LC = new ListCombine();
		
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
			
			add( createButton("Load", null));
			add( createButton("Save",null) );
			add( createButton("Clear", null) );
			add( createButton("Colour", null));
			add( createButton("Undo", null));
			add( createButton("Eraser", null));
			add( createButton("Done", null));
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

			if ("Clear".equals(e.getActionCommand()))
				drawingArea.clear();	
			else if ("Save".equals(e.getActionCommand()))
				drawingArea.save();
			else if("Colour".equals(e.getActionCommand()))
				drawingArea.colour();
			else if ("Load".equals(e.getActionCommand()))
				drawingArea.load();
			else if("Undo".equals(e.getActionCommand()))
				drawingArea.undo();
			else if("Eraser".equals(e.getActionCommand()))
				drawingArea.erase();
			else
				drawingArea.completion();
		
		}
	}

	static class DrawingArea extends JPanel
	{
		// creates an image, graphics and two points
		BufferedImage image;
		public Graphics2D g2d;
		Point startPoint = null;
		Point endPoint = null;
		//variables used for erasing image
		boolean erase = false;
		Color tmp = Color.BLACK;
		//used to determine if croping required
		boolean loaded = false, connected = true;
		int count =0, partc = 0;
		BasicStroke[] brushes = new BasicStroke[] {new BasicStroke(1), new BasicStroke(5), new BasicStroke(15)}; 
		

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
			g.drawString(bodypart, 0, 20);
			if(bodypart.equals("head")){
				g.drawLine(0, (image.getHeight() - 50), image.getWidth(), image.getHeight()- 50);
				g.drawLine(0, (image.getHeight()-10), image.getWidth(), image.getHeight()- 10);
			}
			else if(bodypart.equals("body")){
				for(int i = 0; i < hotspot.size() - 1; i++){
					g.setColor(hotspot.get(i).getColor());
					g.drawLine(hotspot.get(i).getPoint().x, hotspot.get(i).getPoint().y,
							hotspot.get(i+1).getPoint().x,hotspot.get(i+1).getPoint().y);
				}
				g.drawLine(0, (image.getHeight() - 50), image.getWidth(), image.getHeight()- 50);
				g.drawLine(0, (image.getHeight()-10), image.getWidth(), image.getHeight()- 10);
			}
			else if(bodypart.equals("legs")){
				for(int i = 0; i < hotspot.size() - 1; i++){
					g.setColor(hotspot.get(i).getColor());
					g.drawLine(hotspot.get(i).getPoint().x, hotspot.get(i).getPoint().y,
							hotspot.get(i+1).getPoint().x,hotspot.get(i+1).getPoint().y);
				}
			}
			
						
			//draws the line using the list
			
			for(int i = 0; i < (pts.size() - 1); i++){
				if(pts.get(i).getEnd() == true || pts.get(i+1).getEnd() == true ){}
				else{
				g.setColor(pts.get(i).getColor());	
				g.drawLine(pts.get(i).getPoint().x, pts.get(i).getPoint().y, pts.get(i+1).getPoint().x, pts.get(i+1).getPoint().y);				
				}
			}			
		}

		//creates new image
		private void createEmptyImage()
		{
			image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
			g2d = (Graphics2D)image.getGraphics();
			g2d.setColor(Color.WHITE);
			g2d.setBackground(Color.WHITE);							
			count++;
			
		}

		//clears the canvas
		public void clear()
		{			
			createEmptyImage();
			pts.clear();
			repaint();				
			erase = true;
			erase();
			loaded = false;
		}

		//saves image to file
		public void save(){
			try{		
				
				g2d.setColor(new Color(255,255,255));
				g2d.fillRect(0, 0, image.getWidth(),image.getHeight());
				
				
				//draws lines to image
				for (int i = 0; i < (pts.size()-1); i++){
					if(pts.get(i).getEnd() == true || pts.get(i+1).getEnd() == true){}
					else{
						g2d.setColor(pts.get(i).getColor());
						g2d.drawLine(pts.get(i).getPoint().x, pts.get(i).getPoint().y, pts.get(i+1).getPoint().x, pts.get(i+1).getPoint().y);
					}
						
				}
				BufferedImage im = image;
				//if image was completly drawn crop image
			if(loaded == false)
				im = crop(image);
				//saves image in default location
			String f = "Image_" + bodypart + "_" + Integer.toString(count) + ".png";
			ImageIO.write(im, "PNG", new File(f));		
			}
			catch(Exception e){
				System.err.println(e.getMessage());
			}
		}
		
		//crops image
		private BufferedImage crop(BufferedImage i){
			
			try{
				//initilises starting mins and maxs
			int xmin = i.getWidth(); 
			int xmax = 0; 
			int ymin = i.getHeight();
			int ymax = 0;
			
			//loops through point list to get min and max
			for(int h = 0; h < pts.size(); h++){
				//if end identifier do nothing
				if(pts.get(h).getEnd() == true){}
				else{
					//gets point
				Point v = pts.get(h).getPoint();
				
					//checks for larger and smaller and sets values accordingly
				if(v.x < xmin)
					xmin = v.x;
				
				if(v.x > xmax)
					xmax = v.x;
				
				if(v.y < ymin)
					ymin = v.y;

				if (v.y > ymax)
					ymax = v.y;
				}
			}
			
			//creates new image
			BufferedImage c = null;
			
			//crops image 
			if((xmin - 5) > 0 && (ymin - 5) > 0)
				c = i.getSubimage(xmin-1, ymin-1, (xmax - xmin + 5),(ymax - ymin + 25));
			else
				c = i.getSubimage(0, 0,(xmax - xmin + 5), (ymax - ymin + 25));
			
			//returns croped image
			return c;
			}
			catch(Exception e){
				//if error occurs return original image
				System.err.println(e.getMessage());
				return i;
			}
			
		}
		
		//loads image
		public void load(){
			try{
				//gets default file name
				File f = new File("Image_"+ bodypart + "_1.png");
				//loads image and draws to canvas 
				image = ImageIO.read(f);
				loaded = true;
				g2d.drawImage(image, 0, 0, null);
				repaint();
			}
			catch(Exception e){
				System.err.println(e.getMessage());
			}
		}
		
		//chages the color of the line
		public void colour(){						
			newColor = JColorChooser.showDialog(DrawingArea.this, "Pick a Color", Color.WHITE);
		}
		
		//undos previous work
		public void undo(){
		//removes eraser if on
			erase = true;
			erase();	
		//checks if there is stuff inside list
		if(pts.size() >= 2){	
			//sets up pointer and removes last entry
		int count = (pts.size()-2);
		pts.remove(pts.size()-1);
		//loops through list removing entry to first end of line indetifier or end of list
		while(pts.get(count).getEnd() == false && count > 0){
			pts.remove(count);
			count--;		
		}
		//sets last value in list to be a end of line identifier
		if(pts.size() == 1)
			pts.get(0).setEnd(true);
		//repaints the picture
		repaint();
		}
		
		}
		
		//erases part of image
		public void erase(){
			//if eraser if off
			if(erase == false){
				//turns eraser on stores old colour in temp variable
				erase = true;
				tmp = newColor;
				//sets line colour to background image colour
				newColor = g2d.getBackground();				
			}
			else{
				//turns eraser off and returns line colour to old colour				
				erase = false;
				newColor = tmp;
			}
		}
		
		public void completion(){
			try{				
				
			if(partc == 4){	
				LC.addPart(pts);
				LC.printtype();
				clear();
				LC.combine();
				BufferedImage im = LC.getImage();
				ImageIO.write(im,"PNG", new File("ComboImage_123.png"));
				image = im;
				repaint();
			}
			else{	
				
				
				
				
					LC.addPart(pts);
					LC.printtype();					
					partc++;
					bodypart = part[partc];
					clear();
				
			}
			}
			catch(Exception e){
				System.err.println(e.getMessage());
			}
			
			
		}
		
		private void getShared(){
			int ymax = 0, ymin = 0;
			
			if(bodypart.equals("head") || bodypart.equals("body")){
				hotspot.clear();
				ymin = image.getHeight()- 50;				
				for(int i = 0; i < pts.size(); i++){
					if(pts.get(i).getEnd() == false){
						if (pts.get(i).getPoint().y > ymin ){
						hotspot.add(pts.get(i));
						}
					}
				}				
			}
			
		}
		
		private void stingifiy(){
			String text = Integer.toString(pts.get(0).getPoint().x) + "," + Integer.toString(pts.get(0).getPoint().y) + "," 
		+ pts.get(0).getType() + "," + pts.get(0).getColor() + "," + pts.get(0).getEnd() + ",";
			
			for(int i = 1; i < pts.size(); i++){
				text = text + Integer.toString(pts.get(i).getPoint().x) + "," + Integer.toString(pts.get(i).getPoint().y) + "," 
						+ pts.get(i).getType() + "," + pts.get(i).getColor() + "," + pts.get(i).getEnd() + ",";
			}
		}
		
		private void listifiy(String t){
			pts.clear();
			String[] text = t.split(",");
			for(int i = 0; i< text.length; i += 5){
				Point p = new Point(Integer.parseInt(text[i]), Integer.parseInt(text[i+1]));
				String type = text[i+2];
				Color c = Color.getColor(text[i+3]);
				boolean e = false;
				
				if(text[i+4].equals("true")){
					e = true;
				}
			
				if (e == false){
					pts.add(new PointList(p, c, type));
				}
				else
					pts.add(new PointList());
			}
				
		}
		
		private void saveList(){
			try{
				String filename = "Points_"+ bodypart + ".txt";
				FileWriter write = new FileWriter(filename);
				BufferedWriter bwrite = new BufferedWriter(write);
				for( int i = 0; i < pts.size(); i++){
					String text = Integer.toString(pts.get(i).getPoint().x) + "," + Integer.toString(pts.get(i).getPoint().y) + "," 
							+ pts.get(i).getType() + "," + pts.get(i).getColor() + "," + pts.get(i).getEnd();
					bwrite.write(text);
					bwrite.newLine();
				}
				bwrite.close();
				write.close();				
			}
			catch(Exception e){
				System.err.println(e.getMessage());
			}			
		}
		
		private void loadList(){
			try{
				
				
				
			}
			catch(Exception e){
				
			}
			
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
