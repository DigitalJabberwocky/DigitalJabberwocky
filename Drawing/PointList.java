import java.awt.Color;
import java.awt.Point;

public class PointList{

private Point  p;
private Color c;
//determines if the point is the end of a line
private boolean end = false;
private String type;

//Creates instance of object sets point and colour of line
public PointList(Point pnt, Color col, String t){
	p = pnt;
	c = col;
	type = t;
	
}

//creates an instance of a end line object
public PointList(){
	p = null;
	c = null;
	end = true;
	type = null;
}

//returns the values of the point, colour, and end of line
public Point getPoint(){
	return p;
}

public Color getColor(){
	return c;
}

public boolean getEnd(){
	return end;
}

//sets end of line
public void setEnd(boolean f){
	end = f;
}

//returns the body type
public String getType(){
	return type;
}
}