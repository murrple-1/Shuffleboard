import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class ScoreBoard {

	private ShuffleBoardPanel parent;
	private Font font;
	private Point startPoint;
	private Rectangle size;

	public ScoreBoard(ShuffleBoardPanel parent, Point startPoint,
			Rectangle size, Font font) {
		this.parent = parent;
		this.startPoint = startPoint;
		this.size = size;
		this.font = font;
	}

	public void draw(Graphics aPen) {
		int y = startPoint.y;
		aPen.setFont(font);
		for (Player p : parent.getPlayers()) {

			aPen.setColor(Color.darkGray);
			aPen.fillRect(startPoint.x, y, size.width, size.height);

			aPen.setColor(Color.black);
			aPen.drawRect(startPoint.x, y, size.width, size.height);

			int points = 0;
			for (Weight w : p.getWeights()) {
				points += parent.getPointValue(w);
			}
			aPen.setColor(p.getColor());
			StringBuilder sb = new StringBuilder();
			sb.append(points);
			aPen.drawString(sb.toString(), startPoint.x + 5, y + font.getSize());

			y += size.height;
		}
	}
}
