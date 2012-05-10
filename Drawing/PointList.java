import java.awt.Color;
import java.awt.Point;

public class PointList{

private Point  p;
private Color c;
//determines if the point is the end of a line
private boolean end = false;

//Creates instance of object sets point and colour of line
public PointList(Point pnt, Color col){
	p = pnt;
	c = col;
	
}

//creates an instance of a end line object
public PointList(){
	p = null;
	c = null;
	end = true;
}

//returns the values of the point colour, and end of line
public Point getPoint(){
	return p;
}

public Color getColor(){
	return c;
}

public boolean getEnd(){
	return end;
}

}