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
	private BufferedImage[] itemsImagesMini= new BufferedImage[itemsNumber];
	
	private boolean mouseInTheItemChooser = false;
	private int whichItem; //which item in the itemChooser
	private boolean itemSelected = false; // true if the user clicked on any item

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
		
		//Drawing the map
		mergeMap();
		try {
			init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		/*
		 * Handling the item selection part. (this is on south of the frame)
		 */
		for (int i = 0; i < itemsNumber; ++i) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect((int) itemChooser[i].getX(), (int) itemChooser[i].getY(), (int) itemChooser[i].getWidth(),
					(int) itemChooser[i].getHeight());
			if (mouseInTheItemChooser) {
				g.setColor(new Color(255, 255, 204));
				g.fillRect((int) itemChooser[whichItem].getX(), (int) itemChooser[whichItem].getY(),
						(int) itemChooser[whichItem].getWidth(), (int) itemChooser[whichItem].getHeight());
			}
			g.setColor(Color.BLACK);
			g.drawRect((int) itemChooser[i].getX(), (int) itemChooser[i].getY(), (int) itemChooser[i].getWidth(),
					(int) itemChooser[i].getHeight());
		}
		g.drawImage(itemsImages[0], (int) itemChooser[0].getX() + 5, (int) itemChooser[0].getY() + 15, this);
		g.drawImage(itemsImages[1], (int) itemChooser[1].getX() + 5, (int) itemChooser[1].getY() + 10, this);
		g.drawImage(itemsImages[2], (int) itemChooser[2].getX() + 5, (int) itemChooser[2].getY() + 5, this);
		g.drawImage(itemsImages[3], (int) itemChooser[3].getX() + 5, (int) itemChooser[3].getY() + 5, this);
		g.drawImage(itemsImages[4], (int) itemChooser[4].getX() + 5, (int) itemChooser[4].getY() + 5, this);
		//Item selection handling ends here.
		
		//Double buffering
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
		for (int i = 0; i < itemsNumber; ++i) {// rects width: 50 height: 100
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

		} catch (IOException e) {
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

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		boolean contains = false;
		for (int i = 0; i < itemsNumber; ++i) {
			if (itemChooser[i].contains(e.getPoint())) {
				whichItem = i;
				contains = true;
			}
		}
		if (contains) {
			mouseInTheItemChooser = true;
		} else if(!itemSelected) {
			mouseInTheItemChooser = false;
		}
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
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		boolean contains = false;
		for (int i = 0; i < itemsNumber; ++i) {
			if (itemChooser[i].contains(e.getPoint())) {
				whichItem = i;
				contains = true;
			}
		}
		if (contains) {
			mouseInTheItemChooser = true;
			itemSelected = true;
		} else{
			itemSelected = false;
		}
		repaint();
	}
}
