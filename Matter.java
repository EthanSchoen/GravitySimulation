import java.awt.Color;
import java.awt.Point;

public class Matter {

    private final int SPEED_CONST = 25;
	private Point center;
	private int radius;
	private Color color;
	private int velocityX;
	private float vX;
	private int velocityY;
	private float vY;
	private final int C = 1000;
	
	public Matter(Point loc){
		center = loc;
		radius = 10;
		color = Color.WHITE;
		velocityX = 0;
		velocityY = 0;
	}
	
	public Matter(Point loc, int r){
		center = loc;
		radius = r;
		color = Color.WHITE;
		velocityX = 0;
		velocityY = 0;
	}
	
	public Matter(Point loc, int r, Color c){
		center = loc;
		radius = r;
		color = c;
		velocityX = 0;
		velocityY = 0;
	}
	
	public Point getLoc(){
		return center;
	}
	
	public int getRadius(){
		return radius;
	}
	
	public Color getColor(){
		return color;
	}
	
	public int getVelocityX(){
		return velocityX;
	}
	
	public int getVelocityY(){
		return velocityY;
	}
	
	public void setLoc(Point p){
		center = p;
	}
	
	public void setRadius(int r){
		radius = r;
	}
	
	public void setColor(Color c){
		color = c;
	}
	
	public void setVelocityX(int v){
		if(v < C){
            vX += (v*SPEED_CONST)/radius;
            if (v != 0 && vX == 0) {
                vX = (v > 0) ? 1 : -1;
            }
			velocityX = (int)vX;
		}else{
			velocityX = C;
		}
	}
	
	public void setVelocityY(int v){
		if(v < C){
			vY += (v*SPEED_CONST)/radius;
            if (v != 0 && vY == 0) {
                vY = (v > 0) ? 1 : -1;
            }
			velocityY = (int)vY;
		}else{
			velocityY = C;
		}
	}
}
