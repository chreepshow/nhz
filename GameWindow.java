import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class GameWindow extends JFrame {
	private List<Player> players = new ArrayList<Player>();

	public static final int screenX = 1440;
	public static final int screenY = 900;

	public static int width;
	public static int maxPiecesHorizontal, maxPiecesVertical;

	public static List<ArrayList<Hexagon>> map = new ArrayList<ArrayList<Hexagon>>();

	FileReader fr;
	BufferedReader br;

	public GameWindow() throws IOException {
		setSize(screenX, screenY);
		setTitle("Battle!");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);

		mapInit();
	}

	@Override
	public void paint(Graphics g) {
		// double buffering - a célja, hogy ne mutassuk azt az állapotot,
		// amikor már letöröltük az elõzõ kört, de még nem rajzoltuk ki az
		// újat
		if (getBufferStrategy() == null)
			createBufferStrategy(2);
		g = getBufferStrategy().getDrawGraphics();
		mergeMap();
		for (int row = 0; row < maxPiecesHorizontal; ++row) {
			for (int column = 0; column < maxPiecesVertical; ++column) {
				Hexagon current = map.get(row).get(column);
				if (current.isThisInGame()) {
					Polygon tmp = new Polygon(current.getX(), current.getY(), 6);
					g.setColor(current.getFillColor());
					g.fillPolygon(tmp);
					g.setColor(current.getBorderColor());
					g.drawPolygon(tmp);
				}
			}
		}
		// csak double buffering esetén van rá szükség (mindkét sorra)
		getBufferStrategy().show();
		g.dispose();
	}

	public void addPlayer(int id, Color c, boolean ai) {
		players.add(new Player(id, c, ai));
		System.out.println("player's id: " + id + " player's color: " + c + "AI: " + ai);
	}

	public void mapInit() throws IOException {
		width = screenY / 30;
		maxPiecesVertical = (int) (screenY / (2.15 * width));
		maxPiecesHorizontal = (int) (screenX / (1.65 * width));
		for (int row = 0; row < maxPiecesHorizontal; ++row) {
			map.add(new ArrayList<Hexagon>());
			for (int column = 0; column < maxPiecesVertical; ++column) {
				map.get(row).add(new Hexagon(row, column, width));
			}
		}

		fr = new FileReader("map0.txt");
		br = new BufferedReader(fr);
		while (true) {
			int coordX, coordY, playerID;
			String line = br.readLine();
			if (line == null)
				break;
			String[] coords = line.split(" ");
			coordX = Integer.parseInt(coords[0]);
			coordY = Integer.parseInt(coords[1]);
			playerID = Integer.parseInt(coords[2]);
			map.get(coordX).get(coordY).makeThisInGame();
			map.get(coordX).get(coordY).setOwnerID(playerID);
		}
	}

	public void mergeMap() {
		Hexagon currentHex;
		boolean playerWithThatIdExists = false;
		for (int row = 0; row < maxPiecesHorizontal; ++row) {
			for (int column = 0; column < maxPiecesVertical; ++column) {
				currentHex = map.get(row).get(column);
				if (currentHex.ownerID() != 0) {
					for (int k = 0; k < players.size(); ++k) {
						if (currentHex.ownerID() == players.get(k).getID()) {
							currentHex.setFillColor(players.get(k).getColor());
							
							playerWithThatIdExists = true;
						}
					}
					if (!playerWithThatIdExists) {
						currentHex.setOwnerID(0);
					}
				}
			}
		}
	}
}
