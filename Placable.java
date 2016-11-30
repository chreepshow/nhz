
abstract public class Placable {
	private Type type;
	private int value;
	private int maintenance;

	abstract public void makeImpact();

	public int getValue() {
		return value;
	}

	public int getMaintenance() {
		return maintenance;
	}
}
