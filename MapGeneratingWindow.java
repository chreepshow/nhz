import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MapGeneratingWindow extends JFrame {
	public static final int screenX = 1440;
	public static final int screenY = 900;
	
	public static int width;
	public static int maxPiecesHorizontal, maxPiecesVertical;
	
	public static List<ArrayList<Hexagon>> map = new ArrayList<ArrayList<Hexagon>>();
	private Polygon selectedPolygon;
	
	private boolean initialized = false;
	private boolean firstClicked = false;
	
	FileWriter fw;
	PrintWriter pw;
	String filename;
	
	JComboBox playersBox;
	MyComponent myComp = new MyComponent();
	

	public MapGeneratingWindow(String filen) throws IOException {

		super("Creating a map!");

		setSize(screenX, screenY);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// setBackground(new Color(170,101,75));
		mapInit();
		subMenu();
		filename = filen;

		fw = new FileWriter(filename);
		pw = new PrintWriter(fw, true);
		
		add(myComp);
		
		

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
	}

	public void subMenu() {
		JLabel playerLabel = new JLabel("Tiles of this player: ");

		playersBox = new JComboBox();
		playersBox.addItem("Empty");
		playersBox.addItem("1. Player");
		playersBox.addItem("2. Player");
		playersBox.addItem("3. Player");
		playersBox.addItem("4. Player");
		playersBox.addItem("5. Player");
		playersBox.addItem("6. Player");
		playersBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				MapGeneratingWindow.this.repaint();
			}
		});
		JPanel chooserPanel = new JPanel();
		chooserPanel.add(playerLabel);
		chooserPanel.add(playersBox);
		add(chooserPanel, BorderLayout.NORTH);
	}

	private class MyComponent extends JComponent implements MouseListener {
		MyComponent() {
			addMouseListener(this);
		}

		@Override
		public void paint(Graphics g) {
			// double buffering - a c�lja, hogy ne mutassuk azt az �llapotot,
			// amikor m�r let�r�lt�k az el�z� k�rt, de m�g nem rajzoltuk ki az
			// �jat
			// if (getBufferStrategy() == null)
			// createBufferStrategy(2);
			// g = getBufferStrategy().getDrawGraphics();
			for (int row = 0; row < maxPiecesHorizontal; ++row) {
				for (int column = 0; column < maxPiecesVertical; ++column) {
					Hexagon current = map.get(row).get(column);
					// if(current.isThisInGame()){
					Polygon tmp = new Polygon(current.getX(), current.getY(), 6);
					g.setColor(current.getFillColor());
					g.fillPolygon(tmp);
					g.setColor(current.getBorderColor());
					g.drawPolygon(tmp);
					if(firstClicked && current.ownerID() != 0){
						Rectangle rect = new Rectangle(tmp.getBounds());
						g.drawString(current.ownerID().toString(), rect.x+width/2, rect.y+width/2);
					}
					// }
				}
			}
			// csak double buffering eset�n van r� sz�ks�g (mindk�t sorra)
			// getBufferStrategy().show();
			// g.dispose();

		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				Hexagon currentHex = null;
				
				boolean wasValidSelection = false;
				Polygon currentp = new Polygon();
				for (int row = 0; row < maxPiecesHorizontal; ++row)
					for (int column = 0; column < maxPiecesVertical; ++column) {
						currentp = new Polygon(map.get(row).get(column).getX(), map.get(row).get(column).getY(), 6);
						if (currentp.contains(e.getPoint())) {
							map.get(row).get(column).setFillColor(Color.GRAY);
							map.get(row).get(column).makeThisInGame();
							currentHex=map.get(row).get(column);
							wasValidSelection = true;
							selectedPolygon = currentp;
						}
					}
				if (wasValidSelection) {
					if(!playersBox.getSelectedItem().toString().equals("Empty")){
						currentHex.setOwnerID(Integer.parseInt(playersBox.getSelectedItem().toString().substring(0, 1)));
					}
					pw.println(currentHex.getCoordX() + " " +currentHex.getCoordY()+" "+currentHex.ownerID());
					pw.flush();
					firstClicked = true;
					repaint();
				}
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}
	}
}
