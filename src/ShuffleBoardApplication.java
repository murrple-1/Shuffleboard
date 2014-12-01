import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class ShuffleBoardApplication extends JFrame {

	private static final long serialVersionUID = 3136204890337342302L;

	private ShuffleBoardPanel table;

	public ShuffleBoardApplication(String title) {
		super(title);
		setSize(400, 650);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		table = new ShuffleBoardPanel();
		setLayout(new GridLayout());
		add(table);

		setUpMenu();
	}

	public static void main(String[] args) {
		ShuffleBoardApplication frame = new ShuffleBoardApplication(
				"Shuffle Truffle");
		frame.setVisible(true);
	}

	public void setUpMenu() {
		JMenuBar menu = new JMenuBar();
		setJMenuBar(menu);
		JMenu game = new JMenu("Game");
		menu.add(game);
		JMenuItem reset = new JMenuItem("Reset");
		ActionListener restartL = new RestartListener(this);
		reset.addActionListener(restartL);
		game.add(reset);
		JMenuItem exit = new JMenuItem("Exit");
		ActionListener exitL = new ExitListener();
		exit.addActionListener(exitL);
		game.add(exit);
	}

	private static class RestartListener implements ActionListener {

		private ShuffleBoardApplication app;

		public RestartListener(ShuffleBoardApplication app) {
			this.app = app;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			app.table.restartGame();
		}
	}

	private static class ExitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}

	}
}
