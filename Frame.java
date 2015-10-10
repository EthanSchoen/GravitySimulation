import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.util.Timer;

public class Frame extends JFrame {

	public final int WIDTHwindow = 1500;
	public final int HEIGHTwindow = 750;
	private int time = 0;

	public Frame() {
		setBounds(200, 100, WIDTHwindow, HEIGHTwindow);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Gravity...in action!");
		Panel p = new Panel();
		add(p);
		setVisible(true);
	}

	public class Panel extends JPanel {

		ArrayList<Matter> universe = new ArrayList<>();

		public Panel() {
			setSize(WIDTHwindow, HEIGHTwindow);

			Random r = new Random();
			for (int i = 0; i < 100; i++) {
				universe.add(new Matter(new Point(r.nextInt(WIDTHwindow - 60) + 30, r.nextInt(HEIGHTwindow - 60) + 30)));
			}

			repaint();
			setVisible(true);

			Timer t = new Timer();
			t.schedule(new TimerTask() {
				@Override
				public void run() {
					time++;
					if(time == 1000000){
						time = 0;
					}
					tick();
					repaint();
				}
			}, 1000, 1000);
		}

		public boolean inBounds(Matter m) {
			return (m.getLoc().getX() - m.getRadius() > 0 && m.getLoc().getX() + m.getRadius() < WIDTHwindow)
					&& (m.getLoc().getY() - m.getRadius() > 0 && m.getLoc().getY() + m.getRadius() < HEIGHTwindow)
							? true : false;
		}

		public int distance(Matter m1, Matter m2) {
			return (int) Math.sqrt(Math.pow(m2.getLoc().getX() - m1.getLoc().getX(), 2)
					+ Math.pow(m2.getLoc().getY() - m1.getLoc().getY(), 2));
		}

		public boolean checkCollision(Matter m1, Matter m2) {// returns true if a collision is detected
			int d = distance(m1, m2);
			return d < m1.getRadius() || d < m2.getRadius() ? true : false;
		}

		public void tick() {
			//Check for and mash matter together
			ArrayList<Matter> temp = universe;
			for (int i = 0; i < temp.size(); i++) {
				for (int j = 0; j < temp.size(); j++) {
					if ( i < temp.size() && temp.get(i) != temp.get(j)) {
						if (checkCollision(temp.get(i), temp.get(j))) {
							Matter t1 = temp.get(i);
							Matter t2 = temp.get(j);
							if(i <= j){
								temp.remove(i);
								temp.remove(j - 1);
							}else{
								temp.remove(j);
								temp.remove(i-1);
							}
							
							int newR = (int) Math.sqrt(Math.pow(t1.getRadius(), 2) + Math.pow(t2.getRadius(), 2));

							temp.add(new Matter(new Point((int) ((t1.getLoc().getX() + t2.getLoc().getX()) / 2),
									(int) ((t1.getLoc().getY() + t2.getLoc().getY()) / 2)), newR));
						}
					}
				}
			}
			universe = temp;

			//Set velocities to go to nearest matter
			if (universe.size() > 1) {
				for (int i = 0; i < universe.size(); i++) {
					Matter mI = universe.get(i);
					Matter mC = new Matter(new Point(999999999, 999999999));
					for (Matter mJ : universe) {
						if (mI != mJ) {
							if (distance(mI, mJ) < distance(mI, mC)) {
								mC = mJ;
							}
						}
					}

					if (mI.getLoc().getX() < mC.getLoc().getX()) {
						universe.get(i).setVelocityX(1);
					}
					if(mI.getLoc().getX() > mC.getLoc().getX()) {
						universe.get(i).setVelocityX(-1);
					}
					if (mI.getLoc().getX() == mC.getLoc().getX()) {
						universe.get(i).setVelocityX(0);
					}
					
					if (mI.getLoc().getY() < mC.getLoc().getY()) {
						universe.get(i).setVelocityY(1);
					}
					if(mI.getLoc().getY() > mC.getLoc().getY() ){
						universe.get(i).setVelocityY(-1);
					}
					if (mI.getLoc().getY() == mC.getLoc().getY()) {
						universe.get(i).setVelocityX(0);
					}
				}
			}

			//alter positions based on velocity
			for (int i = 0; i < universe.size(); i++) {
				Matter before = universe.get(i);
				universe.get(i).setLoc(new Point((int) universe.get(i).getLoc().getX() + universe.get(i).getVelocityX(),
						(int) universe.get(i).getLoc().getY() + universe.get(i).getVelocityY()));

				if (!inBounds(temp.get(i))) {
					universe.remove(i);
					universe.add(i, before);
				}
			}
		}

		public void calculateVelocities(){
			
		}
		
		@Override
		public void paint(Graphics g) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, WIDTHwindow, HEIGHTwindow);
			for (Matter m : universe) {
				g.setColor(m.getColor());
				g.fillOval((int) m.getLoc().getX() - m.getRadius(), (int) m.getLoc().getY() - m.getRadius(),
						m.getRadius(), m.getRadius());
			}
		}
	}
}