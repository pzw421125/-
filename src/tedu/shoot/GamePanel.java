package tedu.shoot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public  class GamePanel extends JPanel{
	static final int STATUS_NOT_START= 0;
	static final int STATUS_RUN = 1;
	static final int STATUS_OVER = 2;
	static final int STATUS_PAUSE = 3;
	Background bg = new Background();
	Hero hero = new Hero();
	//存放所有敌人的列表
	ArrayList<Enemy> enemys = new ArrayList<>();
	ArrayList<Bullet> bullets = new ArrayList<>();
	//静态帧记录变量
	int status = STATUS_NOT_START;
	static int counter;
	
	public GamePanel(){
		setPreferredSize(new Dimension(400,654));
	}
	@Override
	public void paint(Graphics g) {
		   bg.paint(g);
		   hero.paint(g);
		  //airplane.paint(g);
		  //bigplane.paint(g);
		  //bee.paint(g); 
		 paintEnemy(g);
		 paintBullet(g);
		 //绘制得分和英雄生命
		 paintScore(g);
		 if(status == STATUS_NOT_START){
			 g.drawImage(Main.start,0,0,null);
		 }else if(status == STATUS_OVER){
			 g.drawImage(Main.over,0,0,null);
		 }else if(status == STATUS_PAUSE){
			 g.drawImage(Main.pause,0,0,null);
		 }
			
	}
	
	private void paintScore(Graphics g) {
		Font font = new Font(
				Font.SANS_SERIF,Font.BOLD,14);
			g.setFont(font);
			g.setColor(Color.BLACK);
			g.drawString("生命", 8, 20);
			g.drawString(""+hero.life, 160, 20);
			g.drawString("得分"+hero.score,8,38);
			
			g.setColor(Color.WHITE);
			g.drawString("生命", 8-1, 20-1);
			g.drawString(""+hero.life, 160-1, 20-1);
			g.drawString("得分"+hero.score,8-1,38-1);
			g.setColor(Color.BLACK);
			g.drawRect(50, 8, 100, 14);
			g.setColor(Color.RED);
			g.fillRect(52,9,98*hero.life/10, 12);	
	}
	
	private void paintBullet(Graphics g){
		for (Iterator it = bullets.iterator(); it.hasNext();) {
			Bullet b = (Bullet) it.next();
			b.paint(g);
		}
	}
	private void paintEnemy(Graphics g){
		for (Iterator it = enemys.iterator(); it.hasNext();) {
			Enemy e = (Enemy) it.next();
			e.paint(g);
		}
	}
	
	
	public void action(){
		//监听器，监听鼠标在面板上的动作；
		addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				// 设置英雄要移动的目标位置
				if(status==STATUS_RUN||status==STATUS_NOT_START)
				
				hero.moveTo(e.getX(),e.getY());
			}
		});
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				if(status == STATUS_NOT_START){
					status = STATUS_RUN;
				}else if (status == STATUS_OVER){
					status = STATUS_NOT_START;
					hero = new Hero();
					enemys.clear();
					bullets.clear();
				}
			}
			public void mouseEntered(MouseEvent e){
				if(status ==STATUS_PAUSE){
					status = STATUS_RUN;
				}
			}
			public void mouseExited(MouseEvent e){
				if(status ==STATUS_RUN){
					status = STATUS_PAUSE;}
			}
		});
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run(){
				counter++;
				if(status == STATUS_NOT_START){
					bg.step();
					bulletIn();
					bulletStep();
					hero.step();
				 }else if(status == STATUS_RUN){
					    bg.step();
						hero.step();
						if(hero.isDead()){
							status = STATUS_OVER;
						}
						enemyIn();
						enemyStep();
						bulletIn();
						bulletStep();
						//碰撞检测
						pengzhuang();
				 }else if(status == STATUS_OVER){
					 bg.step();
					 enemyIn();
					 enemyStep();
				 }else if(status == STATUS_OVER){
					 bg.step();
					 enemyIn();
					 enemyStep();
				 }

				repaint();	
			}
		}, 0,1000/60);
	}
	
	protected void pengzhuang() {
		for (Iterator it1 = enemys.iterator(); it1.hasNext();) {
			Enemy e = (Enemy) it1.next();
			if(e.isExplode()||e.isDead()){
				continue;
			}
			for (Iterator it2 = bullets.iterator(); it2.hasNext();) {
				Bullet b = (Bullet) it2.next();
				if(e.hit(b)){
					it2.remove();//子弹移除
				}
			}
			if(hero.isNormal()&&e.hit(hero));
				//敌人e是不是接口的子类型
				if( e instanceof Award){
					Award a = (Award) e;
					hero.life += a.getLife();
					if(hero.life>10){
						hero.life = 10;
					}
					if(hero.level<Main.weapons.length-1){
						hero.weapon = a.getWeapon(++hero.level);
					}
					}else{//判断英雄级别还没到0
					if(hero.level>0){
						hero.weapon = Main.weapons[--hero.level];
					}
			}
		}	
	}
	
	protected void enemyStep() {
		//迭代遍历
		for (Iterator it = enemys.iterator(); it.hasNext();) {
			Enemy e = (Enemy) it.next();
			
			if(e.isDead()){
				hero.score += e.score;
				it.remove();
				continue;
			}
			e.step();
			if(e.isOut()){
				it.remove();
			}
		}
	}
	
	protected void enemyIn() {
		if(counter%60 != 0) return;
		double d = Math.random();
		if(d<0.6){
			enemys.add(new Airplane());
		}else if(d<0.8){
			enemys.add(new Bigplane());
		}else{
			enemys.add(new Bee());
		}
	}
	protected void bulletIn(){
		if(hero.isDead()){
			return;
		}
		if(counter%15 != 0) return;
		Bullet[] a = hero.shoot();
		for (int i = 0; i < a.length; i++) {
			Bullet b = a[i];
			if(b != null) {
				bullets.add(b);
			}
		}
	}
	
	protected void bulletStep(){
		for (Iterator it = bullets.iterator(); it.hasNext();) {
			Bullet b = (Bullet) it.next();
			b.step();
			if(b.isOut()){
				it.remove();
			}
		}
	}
	
}
