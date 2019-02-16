package tedu.shoot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Bullet {
	BufferedImage img = Main.bullet;
	int x;
	int y;
	int stepPx = 6;
	public void step(){
		y -= stepPx;
	}
	
	public void paint(Graphics g){
		g.drawImage(img,x,y,null);
	}
	
	public boolean isOut(){
		return y <= -img.getHeight();
	}
}
