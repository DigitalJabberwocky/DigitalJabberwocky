
//This class stores an image with its outermost points of non-white colour. White must be (R,G,B) = (255,255,255)

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class ImageWithCorners {
    private BufferedImage image;
    private Point topLeft;
    private Point topRight;
    private Point bottomLeft;
    private Point bottomRight;
    
    public ImageWithCorners(BufferedImage img) throws Exception {
        image = img;
        boolean finished = false;
        for (int y = 0; y < image.getHeight(); y++) {
            if (finished)
                break;
            for (int x = 0; x < image.getWidth(); x++) {
                Color colour = new Color(image.getRGB(x, y));
                if ((colour.getRed() == 255 && colour.getGreen() == 255 && colour.getBlue() == 255) == false) {
                    topLeft = new Point(x,y);
                    finished = true;
                    break;
                }
                else if (x == image.getWidth() && y == image.getHeight()) {
                    throw new Exception();
                }
            }
        }
        finished = false;
        for (int y = 0; y < image.getHeight(); y++) {
            if (finished)
                break;
            for (int x = image.getWidth() - 1; x >= 0; x--) {
                Color colour = new Color(image.getRGB(x, y));
                if ((colour.getRed() == 255 && colour.getGreen() == 255 && colour.getBlue() == 255) == false) {
                    topRight = new Point(x,y);
                    finished = true;
                    break;
                }
                else if (x == 0 && y == image.getHeight()) {
                    throw new Exception();
                }
            }
        }
        finished = false;
        for (int y = image.getHeight() - 1; y >= 0; y--) {
            if (finished)
                break;
            for (int x = 0; x < image.getWidth(); x++) {
                Color colour = new Color(image.getRGB(x, y));
                if ((colour.getRed() == 255 && colour.getGreen() == 255 && colour.getBlue() == 255) == false) {
                    bottomLeft = new Point(x,y);
                    finished = true;
                    break;
                }
                else if (x == image.getWidth() && y == 0) {
                    throw new Exception();
                }
            }
        }
        finished = false;
        for (int y = image.getHeight() - 1; y >= 0; y--) {
            if (finished)
                break;
            for (int x = image.getWidth() - 1; x >= 0; x--) {
                Color colour = new Color(image.getRGB(x, y));
                if ((colour.getRed() == 255 && colour.getGreen() == 255 && colour.getBlue() == 255) == false) {
                    bottomRight = new Point(x,y);
                    finished = true;
                    break;
                }
                else if (x == 0 && y == 0) {
                    throw new Exception();
                }
            }
        }
    }
    
    public Point topLeft() {
        return topLeft;
    }
    
    public Point topRight() {
        return topRight;
    }
    
    public Point bottomLeft() {
        return bottomLeft;
    }
    
    public Point bottomRight() {
        return bottomRight;
    }
    
    public int leftDistance() {
        return bottomLeft.y - topLeft.y;
    }
    
    public int rightDistance() {
        return bottomRight.y - topRight.y;
    }
    
    public int topDistance() {
        return topRight.x - topLeft.x;
    }
    
    public int bottomDistance() {
        return bottomRight.x - bottomLeft.x;
    }
    
    public int imageHeight() {
        return image.getHeight();
    }
    
    public int imageWidth() {
        return image.getWidth();
    }
    
    public BufferedImage image() {
        return image;
    }
}
