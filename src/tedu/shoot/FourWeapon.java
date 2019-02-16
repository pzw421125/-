package tedu.shoot;

public class FourWeapon implements Weapon {
	public Bullet[] fire(Hero hero){
		//双发
		Bullet[] a = new Bullet[4];
		a[0] = new Bullet();
		a[1] = new Bullet();
		a[2] = new Bullet();
		a[3] = new Bullet();
		a[0].x = hero.x+ hero.img.getWidth() - 78;
		a[1].x = hero.x+ hero.img.getWidth() - 18 - a[1].img.getWidth();
		a[2].x = a[0].x - 25;
		a[3].x = a[1].x + 20;
		a[0].y = hero.y + 40;
		a[1].y = hero.y + 40;
		a[2].y = hero.y + 40;
		a[3].y = hero.y + 40;
		//两颗子弹是否在画面外
		for (int i = 0; i < a.length; i++) {
			Bullet b = a[i];
			if(b.isOut()){
				a[i] = null;
			}
		}
		return a;
	}
}
