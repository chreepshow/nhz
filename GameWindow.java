import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class GameWindow extends JFrame implements MouseListener, MouseMotionListener {
	private List<Player> players = new ArrayList<Player>();

	public static final int screenX = 1440;
	public static final int screenY = 900;

	public static int width;
	public static int maxPiecesHorizontal, maxPiecesVertical;

	public static List<ArrayList<Hexagon>> map = new ArrayList<ArrayList<Hexagon>>();
	private final int itemsNumber = 5;

	private Rectangle[] itemChooser = new Rectangle[itemsNumber];

	private BufferedImage[] itemsImages = new BufferedImage[itemsNumber];
	private BufferedImage[] itemsImagesMini = new BufferedImage[itemsNumber];
	private BufferedImage hq;

	private boolean mouseInTheItemChooser = false;
	private int whichItem; // which item in the itemChooser
	private boolean itemSelected = false; // true if the user clicked on any
											// item

	FileReader fr;
	BufferedReader br;

	public GameWindow() throws IOException {
		setSize(screenX, screenY);
		setTitle("Battle!");
		setBackground(Color.LIGHT_GRAY);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);

		addMouseListener(this);
		addMouseMotionListener(this);
	}

	@Override
	public void paint(Graphics g) {
		// double buffering
		if (getBufferStrategy() == null)
			createBufferStrategy(2);
		g = getBufferStrategy().getDrawGraphics();

		// Drawing the map

		try {
			init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mergeMap();
		placeHeadquarters();
		for (int row = 0; row < maxPiecesHorizontal; ++row) {
			for (int column = 0; column < maxPiecesVertical; ++column) {
				Hexagon current = map.get(row).get(column);
				if (current.isThisInGame()) {
					Polygon tmp = new Polygon(current.getX(), current.getY(), 6);
					g.setColor(current.getFillColor());
					g.fillPolygon(tmp);
					g.setColor(current.getBorderColor());
					g.drawPolygon(tmp);
					if (current.isThisHeadquarter()) {
						Rectangle tmpRect = tmp.getBounds();
						g.drawImage(hq, (int) tmpRect.getX(), (int) tmpRect.getY(), this);
					}
					Rectangle rect = new Rectangle(tmp.getBounds());
					g.drawString(current.ownerID().toString(), rect.x+width/2, rect.y+width/2);
				}
			}
		}
		/*
		 * Handling the item selection part. (this is on the bottom of the
		 * frame)
		 */
		for (int i = 0; i < itemsNumber; ++i) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect((int) itemChooser[i].getX(), (int) itemChooser[i].getY(), (int) itemChooser[i].getWidth(),
					(int) itemChooser[i].getHeight());
			if (mouseInTheItemChooser || itemSelected) {
				g.setColor(new Color(255, 255, 204));
				g.fillRect((int) itemChooser[whichItem].getX(), (int) itemChooser[whichItem].getY(),
						(int) itemChooser[whichItem].getWidth(), (int) itemChooser[whichItem].getHeight());
			}
			g.setColor(Color.BLACK);
			g.drawRect((int) itemChooser[i].getX(), (int) itemChooser[i].getY(), (int) itemChooser[i].getWidth(),
					(int) itemChooser[i].getHeight());
		}

		g.drawImage(itemsImages[0], (int) itemChooser[0].getX(), (int) itemChooser[0].getY() + 15, this);
		g.drawImage(itemsImages[1], (int) itemChooser[1].getX() + 5, (int) itemChooser[1].getY() + 10, this);
		g.drawImage(itemsImages[2], (int) itemChooser[2].getX() + 5, (int) itemChooser[2].getY() + 5, this);
		g.drawImage(itemsImages[3], (int) itemChooser[3].getX() + 5, (int) itemChooser[3].getY() + 5, this);
		g.drawImage(itemsImages[4], (int) itemChooser[4].getX() + 5, (int) itemChooser[4].getY() + 5, this);
		// Item selection handling ends here.

		// Double buffering
		getBufferStrategy().show();
		g.dispose();
	}

	public void addPlayer(int id, Color c, boolean ai) {
		players.add(new Player(id, c, ai));
		System.out.println("player's id: " + id + " player's color: " + c + "AI: " + ai);
	}

	public void init() throws IOException {
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
		for (int i = 0; i < itemsNumber; ++i) {
			int rectWidth = 60;
			int rectHeight = 100;
			itemChooser[i] = new Rectangle(getWidth() / 2 - itemsNumber / 2 * rectWidth + i * rectWidth,
					getHeight() - rectHeight, rectWidth, rectHeight);
		}
		try {
			itemsImages[0] = ImageIO.read(new File("house_noback.png"));
			itemsImages[1] = ImageIO.read(new File("tower_noback.png"));
			itemsImages[2] = ImageIO.read(new File("peasant_noback.png"));
			itemsImages[3] = ImageIO.read(new File("war1_noback.png"));
			itemsImages[4] = ImageIO.read(new File("war2_noback.png"));
			hq = ImageIO.read(new File("hq_mini_noback.png"));
		} catch (IOException e) {
		}
	}

	/*
	 * This need to be done, because we need to set the valid player spots. We
	 * have places for max 6 players but it's not sure that all the places are
	 * filled.
	 */
	public void mergeMap() {
		Hexagon currentHex;

		for (int row = 0; row < maxPiecesHorizontal; ++row) {
			for (int column = 0; column < maxPiecesVertical; ++column) {
				boolean playerWithThatIdExists = false;
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
	//ezt még azért nézzük át újra
	public void placeHeadquarters() {
		int row = 0;
		int column = 0;
		int found = 0;
		for (int i = 0; i < players.size(); ++i) {
			row = 0;
			boolean hit = false;
			while (row < maxPiecesHorizontal && !hit) {
				column = 0;
				while (column < maxPiecesVertical && !hit) {
					Hexagon currentHex = map.get(row).get(column);
					if (currentHex.ownerID() != 0) {
						if (players.get(i).getID() == currentHex.ownerID()) {
							System.out.println(players.get(i).getID());
							currentHex.setHeadquarter();
							hit = true;
						}
					}
					++column;
				}
				++row;
			}
		}

	}

	private boolean gameIsEnded() {
		return (players.size() <= 1) ? true : false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// Handling the Item Chooser part
		boolean contains = false;
		int i;
		for (i = 0; i < itemsNumber; ++i) {
			if (itemChooser[i].contains(e.getPoint())) {
				contains = true;
				break;
			}
		}
		if (contains && !itemSelected) {
			mouseInTheItemChooser = true;
			whichItem = i;
		} else if (!itemSelected) {
			mouseInTheItemChooser = false;
		}
		// end of it

		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e1) {
		if (e1.getButton() == MouseEvent.BUTTON1) {

			// Handling the Item Chooser part
			boolean contains = false;
			int i;
			for (i = 0; i < itemsNumber; ++i) {
				if (itemChooser[i].contains(e1.getPoint())) {
					contains = true;
					break;
				}
			}
			if (contains) {
				mouseInTheItemChooser = false;
				itemSelected = true;
				whichItem = i;
			} else {
				itemSelected = false;
				mouseInTheItemChooser = false;
			}
			// end of it
			repaint();
		}
	}
}
