import java.awt.Point;
import java.util.ArrayList;
import java.awt.*;
import java.awt.image.*;




public class ListCombine{

private ArrayList<ArrayList<PointList>> partList = new ArrayList<ArrayList<PointList>>();
private BufferedImage comboimage = null;


public ListCombine(){
	partList.clear();
}

public void addPart(ArrayList<PointList> p){
	partList.add(p);
}

public ArrayList<PointList> getPart(int i){
	if (i < partList.size()){
		return partList.get(i);
	}
	else{
		System.err.println("Values does exist");
		return null;
	}		
}

public void remove(int i){
	if(i< partList.size()){
		partList.remove(i);
	}	
}

public BufferedImage getImage(){
	return comboimage;
}

public void printtype(){
	
	for(int i = 0; i < partList.size(); i++){
		System.err.println(partList.size());		
			System.err.println(partList.get(i).get(i).getType());
			System.err.println(Integer.toString(partList.get(i).size()));
		}
}

public void combine(){
	try{
	
	comboimage = new BufferedImage(1800, 2400, BufferedImage.TYPE_INT_ARGB);
	Graphics2D g = comboimage.createGraphics();
	g.setColor(Color.WHITE);
	g.drawRect(0,0 , comboimage.getWidth(), comboimage.getHeight());
	int ystart = 0, y = 0, xmax = 0, xstart = 0;
	
	if(partList.size() > 0){
		
		for(int i = 0; i < partList.size(); i++){
			ArrayList<PointList> p = partList.get(i);
			System.err.println(p.get(i).getType());
			for(int h = 0; h < p.size(); h++){
				if(p.get(h).getEnd() == true || p.get(h+ 1).getEnd() == true ){}
				else{
				g.setColor(p.get(h).getColor());
				Point s =  p.get(h).getPoint();
				Point e =  p.get(h+1).getPoint();
				String type = p.get(h).getType();				
				
				
				//if(partList.get(i).get(h).getType().equals("head")|| partList.get(i).get(h).getType().equals("rarm")){
					//y = getInt(partList.get(i).get(h).getPoint().y, y);
				//}
				//xmax = getInt(partList.get(i).get(h).getPoint().x, xmax);
				
				//paint(g, partList.get(i).get(h).getType(), partList.get(i).get(h).getPoint(), 
						//partList.get(i).get(h+1).getPoint(), y, xstart);
				
				
		    if(type.equals("head")||type.equals("rarm")){
				y = getInt(s.y, y);
			}
		    
			xmax = getInt(s.x, xmax);
			
			paint(g, type, s, e, ystart, xstart);			
				
				
				}
				
				if(partList.get(i+1).get(h).getType().equals("rarm")){
					xstart = xmax;
				}
				else
					xstart = 0;
					
					ystart = y;	
			
			}		
		}
			//comboimage = comboimage.getSubimage(0, 0, xmax + 5, y + 1);			
		System.err.println(Integer.toString(xmax) + " " + Integer.toString(y) );
	}
	}
	catch(Exception e){
		System.err.println(e.getMessage());
	}
	
}

private void paint(Graphics2D g, String type, Point s, Point e, int y, int x){
	try{
	
	if(type.equals("head")){
		g.drawLine((s.x + 600), s.y, (e.x + 600), e.y);		
	}
	else if (type.equals("body") || type.equals("legs")){
		g.drawLine((s.x + 600), (s.y+y), (e.x + 600), (e.y +y));
	}
	else if (type.equals("rarm")){
		g.drawLine((s.x + x + 600), (s.y + y), (e.x + x + 600), (e.y + y));
	}
	else{
		g.drawLine(s.x, (s.y + y), e.x, (e.y + y));
	}
	}
	catch(Exception ex){
		System.err.println(ex.getMessage());
	}
}


private int getInt(int x, int x1){
	try{
		if(x > x1)
			return x;
		else 
			return x1;	
	}
	catch(Exception e){
		System.err.println(e.getMessage());
		return 0;
	}
	}
}
