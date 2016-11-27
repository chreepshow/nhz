
import java.awt.Color;
import java.awt.Polygon;
import java.math.*;

public class Hexagon {
	private int origoX, origoY;
	private int coordx, coordy;
	private int width;
	private int[] x = new int[6];
	private int[] y = new int[6];
	private double centerX, centerY, h;
	private Color fillColor = Color.WHITE;
	private Color borderColor = Color.DARK_GRAY;
	boolean standsOn;
	boolean defended;
	boolean inGame = false;
	private int ownerID;

	public Hexagon(int coordX, int coordY, int w) {

		this.width = w;
		coordx = coordX;
		coordy = coordY;
		origoX = (int) (0 + 75); // about the size of the main label, this is
									// needed in order to see the first column
									// correctly
		origoY = (int) (0 + 120); // about the size of the main label, this is
									// needed in order to see the first row
									// correctly
		h = Math.sqrt(width * width - (width * width / 4));
		this.centerX = coordXToCenterX(coordX, coordY);
		this.centerY = coordYToCenterY(coordX, coordY);

		x[4] = x[0] = (int) this.centerX - width / 2;
		y[1] = y[0] = (int) (this.centerY - h);

		x[1] = x[3] = (int) this.centerX + width / 2;
		y[4] = y[3] = (int) (this.centerY + h);

		x[2] = (int) this.centerX + width;
		y[5] = y[2] = (int) this.centerY;

		x[5] = (int) this.centerX - width;

	}

	public Polygon getPolygon() {
		return new Polygon(x, y, 6);
	}

	private double coordXToCenterX(int coordX, int coordY) {
		return width * (double) coordX * 3 / 2 + origoX;
	}

	private double coordYToCenterY(int coordX, int coordY) {
		double height = width * Math.sqrt(3) / 2;
		int mux = 0;
		if (coordX % 2 == 0) {
			return 2 * height * coordY + height * coordX * mux + origoY;
		} else {
			return coordY * height + height * (coordY + 1) + origoY;
		}
	}

	public int[] getX() {
		return x;
	}

	public int[] getY() {
		return y;
	}
	public int getCoordX(){
		return coordx;
	}
	public int getCoordY(){
		return coordy;
	}

	public void makeThisInGame() {
		inGame = true;
	}

	public boolean isThisInGame() {
		return inGame;
	}

	public void setFillColor(Color c) {
		fillColor = c;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public Color getFillColor() {
		return fillColor;
	}
	public Integer ownerID(){
		return ownerID;
	}
	public void setOwnerID(int x){
		ownerID=x;
	}
	/*
	 * public Player whooseHex(){
	 * 
	 * }
	 * 
	 * public StandOn whatStandsOnIt(){
	 * 
	 * }
	 * 
	 * public boolean defended(){
	 * 
	 * }
	 */
}
