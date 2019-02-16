package tedu.shoot;

public class Bigplane  extends Enemy{
	public Bigplane(){
		imgs = Main.bigairplanes;
		img = Main.bigairplanes[0];
		stepPx = 1;
		init();
		life = 4;
		damage = 2;
		score = 2;
 	}
}
