package tedu.shoot;

public class Airplane extends Enemy{
	public Airplane(){
		imgs = Main.airplanes;
		img = Main.airplanes[0];
		stepPx = 4;
		init();
		life =2;
		damage = 1;
		score = 1;
	}
}
