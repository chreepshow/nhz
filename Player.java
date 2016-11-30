import java.awt.Color;

public class Player {
	private int ID;
	private boolean isAI;

	private int numberOfTiles = 5;
	private int numberOfPeasants = 0;
	private int numberOfWarriorsLevel1 = 0;
	private int numberOfWarriorslevel2 = 0;
	private int numberOfTrees = 0;
	private int numberOfHouses = 0;
	private int numberOfTowers = 0;
	private int budget = 10;
	private int income = numberOfTiles;

	private Color color;

	Player(int id, Color c, boolean ai) {
		ID = id;
		color = c;
		isAI = ai;
	}

	public Color getColor() {
		return color;
	}

	public int getID() {
		return ID;
	}

	public boolean isAI() {
		return isAI;
	}

	public void decreaseIncome(int with) {
		income -= with;
	}
	
	public void increaseIncome(int with){
		income += with;
	}

	public int getIncom() {
		return income;
	}
	
	public void decreaseBudget(int with){
		budget -= with;
	}
	
	public void increaseBudget(int with){
		budget += with;
	}
	
	public int getBudget() {
		return budget;
	}

}
