import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ShuffleBoardPanel extends JPanel implements ActionListener,
		MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 3309139911207780789L;

	// Table Constants
	public static final Rectangle tableDimensions = new Rectangle(
			Weight.DIAMETER, Weight.DIAMETER, 300, 500);
	private static final int[] PointLines = { 400, 300, 200 };
	private static final int NumOfWeights = 3;
	private static final double MouseToVelocityFactor = 100.0;

	private Weight selectedWeight;
	private Point2D.Double broomTip;
	private Point2D.Double broomEnd;

	private BufferedImage img;
	private ScoreBoard score = new ScoreBoard(this, new Point(4, 50),
			new Rectangle(30, 30), new Font("SansSerif", Font.PLAIN, 25));

	private Collection<Player> players = new ArrayList<Player>();

	private static final int milliseconds = 5;
	private Timer timer = new Timer(milliseconds, this);

	public ShuffleBoardPanel() {

		try {
			img = ImageIO.read(new File("WoodPanel.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		players.add(new Player(Color.red));
		players.add(new Player(Color.yellow));

		timer.start();

		addMouseListener(this);
		addMouseMotionListener(this);

		restartGame();
	}

	public Collection<Player> getPlayers() {
		return players;
	}

	public void restartGame() {
		int diffX = Weight.RADIUS;
		boolean left = true;
		for (Player p : players) {
			p.getWeights().clear();

			int x;
			if (left) {
				x = (int) (tableDimensions.getMinX() + diffX);
			} else {
				x = (int) (tableDimensions.getMaxX() - diffX);
			}
			int y = (int) (tableDimensions.getMaxY() - Weight.RADIUS);
			for (int i = 0; i < NumOfWeights; i++) {
				Weight w = new Weight(this, new Point2D.Double(x, y), p);
				p.getWeights().add(w);
				y -= Weight.DIAMETER + 5;
			}

			left = !left;
			if (left) {
				diffX += Weight.DIAMETER;
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		Collection<Pair<Weight, Weight>> handledWeights = new ArrayList<Pair<Weight, Weight>>();
		for (Player p : players) {
			for (Weight w : p.getWeights()) {
				if (w.isAlive()) {
					w.advance(milliseconds);
					handleCollision(handledWeights, w);
					if (!w.isAlive()) {
						killWeight(w);
					}
				}
			}
		}

		repaint();
	}

	private void killWeight(Weight w) {
		w.stop();
		Point2D.Double point = w.getPosition();
		if (point.x <= tableDimensions.getMinX()) {
			point.x = (int) (tableDimensions.getMinX() - Weight.RADIUS);
		} else if (point.x >= tableDimensions.getMaxX()) {
			point.x = (int) (tableDimensions.getMaxX() + Weight.RADIUS);
		}

		if (point.y <= tableDimensions.getMinY()) {
			point.y = (int) (tableDimensions.getMinY() - Weight.RADIUS);
		} else if (point.y >= tableDimensions.getMaxY()) {
			point.y = (int) (tableDimensions.getMaxY() + Weight.RADIUS);
		}
	}

	private static class Pair<A, B> {
		public A first;
		public B second;

		public Pair(A first, B second) {
			this.first = first;
			this.second = second;
		}
	}

	private void handleCollision(
			Collection<Pair<Weight, Weight>> handledWeights, Weight w) {
		for (Player p : players) {
			for (Weight w2 : p.getWeights()) {
				if (w == w2) {
					continue;
				}
				if (w.getPosition().distance(w2.getPosition()) <= Weight.DIAMETER) {
					boolean handled = false;
					for (Pair<Weight, Weight> hw : handledWeights) {
						if ((hw.first == w && hw.second == w2)
								|| (hw.first == w2 && hw.second == w)) {
							handled = true;
							break;
						}
					}
					if (!handled) {
						double w1x = w2.getVelocityX();
						double w1y = w2.getVelocityY();
						double w2x = w.getVelocityX();
						double w2y = w.getVelocityY();

						w.setVelocityX(w1x);
						w.setVelocityY(w1y);
						w2.setVelocityX(w2x);
						w2.setVelocityY(w2y);

						handledWeights.add(new Pair<>(w, w2));
					}

				}
			}
		}
	}

	public void mouseEntered(MouseEvent e) {
		// do nothing
	}

	public void mouseExited(MouseEvent e) {
		// do nothing
	}

	public void mouseMoved(MouseEvent e) {
		// do nothing
	}

	public void mouseClicked(MouseEvent e) {
		// do nothing
	}

	public void mousePressed(MouseEvent e) {
		selectedWeight = weightAt(e.getPoint());
		if (selectedWeight != null && !selectedWeight.isAlive()) {
			selectedWeight = null;
		}
		if (selectedWeight != null) {
			broomTip = selectedWeight.getPosition();
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (broomTip != null && broomEnd != null) {
			double dx = broomTip.getX() - broomEnd.getX();
			double dy = broomTip.getY() - broomEnd.getY();

			selectedWeight.setVelocityY(dy / MouseToVelocityFactor);
			selectedWeight.setVelocityX(dx / MouseToVelocityFactor);
		}

		selectedWeight = null;
		broomTip = null;
		broomEnd = null;

	}

	public void mouseDragged(MouseEvent e) {
		if (broomTip != null) {
			broomEnd = new Point2D.Double(e.getPoint().getX(), e.getPoint()
					.getY());
		}
	}

	public Weight weightAt(Point p) {
		for (Player player : players) {
			for (Weight w : player.getWeights()) {
				double distance = p.distance(w.getPosition());
				if (distance <= Weight.RADIUS) {
					return w;
				}
			}
		}
		return null;
	}

	public int getPointValue(Weight w) {
		if (w.isAlive()) {
			Point2D.Double p = w.getPosition();
			for (int i = PointLines.length - 1; i >= 0; i--) {
				int lineY = PointLines[i];
				if (p.y < lineY) {
					return i + 1;
				}
			}
		}
		return 0;
	}

	@Override
	public void paintComponent(Graphics aPen) {
		super.paintComponent(aPen);

		aPen.setColor(Color.gray);
		aPen.fillRect(0, 0, getWidth(), getHeight());

		aPen.drawImage(img, tableDimensions.x, tableDimensions.y,
				tableDimensions.width, tableDimensions.height, null);

		aPen.setColor(Color.black);
		aPen.drawRect(tableDimensions.x, tableDimensions.y,
				tableDimensions.width, tableDimensions.height);

		for (int i : PointLines) {
			aPen.drawLine(tableDimensions.x, i, tableDimensions.x
					+ tableDimensions.width, i);
		}

		for (Player p : players) {
			for (Weight w : p.getWeights()) {
				w.draw(aPen);
			}
		}

		if (broomTip != null && broomEnd != null) {
			aPen.setColor(Color.black);
			aPen.drawLine((int) broomTip.x, (int) broomTip.y,
					(int) (broomEnd.x + 5), (int) broomEnd.y);
			aPen.drawLine((int) broomTip.x, (int) broomTip.y,
					(int) (broomEnd.x - 5), (int) broomEnd.y);
		}

		score.draw(aPen);
	}
}
