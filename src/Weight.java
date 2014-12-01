import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public class Weight {

	public static int RADIUS = 25;
	public static int DIAMETER = RADIUS * 2;

	@SuppressWarnings("unused")
	private ShuffleBoardPanel parent;
	private Player player;
	private Point2D.Double posi;
	private double vx = 0.0;
	private double vy = 0.0;

	private static double friction = 0.98;

	private static final double MaxVelocity = 6.5;
	private static final double MinVelocity = 0.01;

	public Weight(ShuffleBoardPanel parent, Point2D.Double posi, Player player) {
		this.parent = parent;
		this.posi = posi;
		this.player = player;
	}

	public Weight(ShuffleBoardPanel parent, double x, double y, Player player) {
		this(parent, new Point2D.Double(x, y), player);
	}

	public Player getPlayer() {
		return player;
	}

	public Point2D.Double getPosition() {
		return posi;
	}

	public double getVelocityX() {
		return vx;
	}

	public double getVelocityY() {
		return vy;
	}

	public boolean isAlive() {
		return ShuffleBoardPanel.tableDimensions.contains(posi);
	}

	public double getVelocity() {
		double velo = Math.sqrt((Math.pow(vx, 2.0)) + Math.pow(vy, 2.0));
		return velo;
	}

	public void setPosition(Point2D.Double posi) {
		this.posi = posi;
	}

	public void setVelocityX(double d) {
		if (d >= MaxVelocity) {
			vx = MaxVelocity;
		} else if (d <= -MaxVelocity) {
			vx = -MaxVelocity;
		} else {
			vx = d;
		}
	}

	public void setVelocityY(double d) {
		if (d >= MaxVelocity) {
			vy = MaxVelocity;
		} else if (d <= -MaxVelocity) {
			vy = -MaxVelocity;
		} else {
			vy = d;
		}
	}

	public void stop() {
		vy = 0.0f;
		vx = 0.0f;
	}

	public void advance(double milliseconds) {
		posi.x += vx * milliseconds;
		posi.y += vy * milliseconds;

		setVelocityX(vx * friction);
		setVelocityY(vy * friction);

		if (getVelocity() <= MinVelocity) {
			stop();
		}
	}

	public void draw(Graphics aPen) {
		if (isAlive()) {
			aPen.setColor(player.getColor());
		} else {
			aPen.setColor(player.getDeadColor());
		}

		aPen.fillOval((int) (posi.x - RADIUS), (int) (posi.y - RADIUS),
				DIAMETER, DIAMETER);

		aPen.setColor(Color.gray);
		aPen.fillOval((int) (posi.x - (RADIUS / 2)),
				(int) (posi.y - (RADIUS / 2)), RADIUS, RADIUS);

		aPen.setColor(Color.black);
		aPen.drawOval((int) (posi.x - RADIUS), (int) (posi.y - RADIUS),
				DIAMETER, DIAMETER);
		aPen.drawOval((int) (posi.x - (RADIUS / 2)),
				(int) (posi.y - (RADIUS / 2)), RADIUS, RADIUS);
	}
}
