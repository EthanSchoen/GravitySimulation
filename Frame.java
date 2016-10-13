import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.util.Timer;

public class Frame extends JFrame {

    public final int WIDTHwindow = 1500;
    public final int HEIGHTwindow = 750;
    public final Point CENTER = new Point(WIDTHwindow / 2, HEIGHTwindow / 2);
    private int time = 0;

    public Frame() {
        setBounds(200, 100, WIDTHwindow, HEIGHTwindow);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Gravity Simulator");
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
                    if (time == 1000000) {
                        time = 0;
                    }
                    tick();
                    repaint();
                }
            }, 5000, 150);
        }

        public int distance(Matter m1, Matter m2) {
            return (int) Math.sqrt(Math.pow(m2.getLoc().getX() - m1.getLoc().getX(), 2)
                    + Math.pow(m2.getLoc().getY() - m1.getLoc().getY(), 2));
        }

        public boolean checkCollision(Matter m1, Matter m2) {// returns true if a collision is detected
            int d = distance(m1, m2);
            return (d < m1.getRadius() || d < m2.getRadius());
        }

        public void tick() {
            //Check for and mash matter together
            ArrayList<Matter> temp = universe;
            for (int i = 0; i < temp.size(); i++) {
                for (int j = 0; j < temp.size(); j++) {
                    if (i < temp.size() && temp.get(i) != temp.get(j)) {
                        if (checkCollision(temp.get(i), temp.get(j))) {
                            Matter t1 = temp.get(i);
                            Matter t2 = temp.get(j);
                            int newR = (int) Math.sqrt(Math.pow(t1.getRadius(), 2) + Math.pow(t2.getRadius(), 2));
                            if (i == 0) {
                                temp.remove(j);
                                temp.get(i).setRadius(newR);
                                continue;
                            } else if (j == 0) {
                                temp.remove(i);
                                temp.get(j).setRadius(newR);
                                continue;
                            } else if (i <= j) {
                                temp.remove(i);
                                temp.remove(j - 1);
                            } else {
                                temp.remove(j);
                                temp.remove(i - 1);
                            }
                            temp.add(new Matter(
                                            ((t1.getRadius() > t2.getRadius()) ? t1.getLoc() : t2.getLoc()), newR
                                    ));
                        }
                    }
                }
            }
            universe = temp;

            //Set velocities to go to nearest matter
            for (int i = 0; i < universe.size(); i++) {
                Matter mI = universe.get(i);
                if (universe.size() > 1) {
                    Matter mC = null;
                    for (Matter mJ : universe) {
                        if (mI != mJ) {
                            if (mC == null || distance(mI, mJ) < distance(mI, mC)) {
                                mC = mJ;
                            }
                        }
                    }
                    if (mI.getLoc().getX() < mC.getLoc().getX()) {
                        universe.get(i).setVelocityX(2);
                    }
                    if (mI.getLoc().getX() > mC.getLoc().getX()) {
                        universe.get(i).setVelocityX(-2);
                    }
                    if (mI.getLoc().getX() == mC.getLoc().getX()) {
                        universe.get(i).setVelocityX(0);
                    }

                    if (mI.getLoc().getY() < mC.getLoc().getY()) {
                        universe.get(i).setVelocityY(2);
                    }
                    if (mI.getLoc().getY() > mC.getLoc().getY()) {
                        universe.get(i).setVelocityY(-2);
                    }
                    if (mI.getLoc().getY() == mC.getLoc().getY()) {
                        universe.get(i).setVelocityX(0);
                    }
                }
                // mysterious force towards center
                if (mI.getLoc().getX() > CENTER.getX()) {
                    universe.get(i).setVelocityX(-1);
                }
                if (mI.getLoc().getX() < CENTER.getX()) {
                    universe.get(i).setVelocityX(1);
                }
                if (mI.getLoc().getY() > CENTER.getY()) {
                    universe.get(i).setVelocityY(-1);
                }
                if (mI.getLoc().getY() < CENTER.getY()) {
                    universe.get(i).setVelocityY(1);
                }
            }

            //alter positions based on velocity
            for (int i = 0; i < universe.size(); i++) {
                Matter before = universe.get(i);
                universe.get(i).setLoc(new Point((int) universe.get(i).getLoc().getX() + universe.get(i).getVelocityX(),
                        (int) universe.get(i).getLoc().getY() + universe.get(i).getVelocityY()));
            }
            calcColors();
        }

        public void calcColors() {
            for (Matter m : universe) {
                switch ((m.getRadius() / 10) % 10) {
                    case 0: //RGB
                        m.setColor(new Color(210, 57, 14));
                        break;
                    case 1:
                        m.setColor(new Color(210, 110, 25));
                        break;
                    case 2:
                        m.setColor(new Color(210, 157, 38));
                        break;
                    case 3:
                        m.setColor(new Color(255, 218, 47));
                        break;
                    case 4:
                        m.setColor(new Color(255, 242, 22));
                        break;
                    case 5:
                        m.setColor(new Color(246, 255, 236));
                        break;
                    case 6:
                        m.setColor(new Color(176, 222, 255));
                        break;
                    case 7:
                        m.setColor(new Color(133, 189, 255));
                        break;
                    case 8:
                        m.setColor(new Color(75, 122, 210));
                        break;
                    case 9:
                        m.setColor(new Color(0, 110, 210));
                        break;
                }
            }
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