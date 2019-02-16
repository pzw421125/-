package tedu.shoot;
import java.util.Random;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Enemy {
	static final int STATUS_NORMAL = 0;
	static final int STATUS_EXPLODE = 1;
	static final int STATUS_DEAD =2;
	
	BufferedImage imgs[];
	BufferedImage img;
	int x;
	int y;
	int stepPx;
	int life;
	int curIndex;
	int status = STATUS_NORMAL;
	int damage;
	int score;
	public void step(){
		
		//移除画面重新出来
		if(status==STATUS_NORMAL){
			y += stepPx;
		}else if(status==STATUS_EXPLODE){
			if(GamePanel.counter%6==0) {
				curIndex++;
				if(curIndex >= 5){
					status = STATUS_DEAD;
					return;
				}
				img = imgs[curIndex];
			}
		}
	}

	public boolean isOut(){
		return x <= -img.getWidth() || x>=400 || y>=654;
	}
	
	public void paint(Graphics g){
		g.drawImage(img,x,y,null);
	}
	
	//初始化敌机位置
	public void init(){
		y = -img.getHeight();
		x = new Random().nextInt(400-img.getWidth());
	}
	
	public boolean hit(Bullet b){
		int r = img.getHeight()/2;
		//中心点
		int x1 = x +img.getWidth()/2;
		int y1 = y +img.getHeight()/2;
		int dx=x1-b.x;
		int dy = y1 - b.y;
		boolean bo1 = Math.sqrt(dx*dx+dy*dy)<=r;
		if(bo1){
			life--;
			if(life <=0){
				life = 0;
				status = STATUS_EXPLODE;
			}
		}
		return bo1;
	}
	
	public boolean hit(Hero hero){
		int r1 = img.getHeight()/2;
		//中心点
		int x1 = x +img.getWidth()/2;
		int y1 = y +img.getHeight()/2;
		int r2 = hero.img.getHeight()/2;//中心点
		int x2 = hero.x +hero.img.getWidth()/2;
		int y2 = hero.y +hero.img.getHeight()/2;
		int dx = x1-x2;
		int dy = y1-y2;
		boolean bol= (Math.sqrt(dx*dx+dy*dy)<=(r1+r2));
		if(bol){
			life = 0;
			status = STATUS_EXPLODE;
			hero.life -= damage;
			if(hero.life<=0){
				hero.life = 0;
				hero.status = Hero.STATUS_EXPLODE;
			}else{
				hero.status = Hero.STATUS_HURT;
			}
		}
		return bol;
	}
	
	public boolean isNormal(){
		return status == STATUS_NORMAL;
	}
	
	public boolean  isExplode(){
		return status == STATUS_EXPLODE;
	}
	
	public boolean isDead(){
		return status == STATUS_DEAD;
	}
}
