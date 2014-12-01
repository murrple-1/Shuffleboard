import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

public class Player {

	private Color color;
	private Color deadColor;
	private Collection<Weight> weights = new ArrayList<Weight>();

	public Player(Color color) {
		this.color = color;
		deadColor = color.darker();
	}

	public Color getColor() {
		return color;
	}

	public Color getDeadColor() {
		return deadColor;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Collection<Weight> getWeights() {
		return weights;
	}

}
