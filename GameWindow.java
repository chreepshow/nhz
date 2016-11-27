import java.awt.Color;

import javax.swing.JFrame;

public class GameWindow extends JFrame {
	private Player[] players = new Player[6];
	private int numberOfPlayers = 0;

	public static final int screenX = 1440;
	public static final int screenY = 900;
	public static int width;
	public static int maxPiecesHorizontal, maxPiecesVertical;

	public GameWindow() {
		setSize(screenX, screenY);
		setTitle("Battle!");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);

	}

	public void addPlayer(int id, Color c, boolean ai) {
		players[numberOfPlayers++] = new Player(id, c, ai);
		System.out.println("player's id: " + id + " player's color: " + c + "AI: " + ai);
	}
}
