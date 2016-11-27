import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class MapReader {
	FileReader fr;
	BufferedReader br;

	public void mapInit() {
		MapGeneratingWindow.width = MapGeneratingWindow.screenY / 30;
		MapGeneratingWindow.maxPiecesVertical = (int) (MapGeneratingWindow.screenY
				/ (2.15 * MapGeneratingWindow.width));
		MapGeneratingWindow.maxPiecesHorizontal = (int) (MapGeneratingWindow.screenX
				/ (1.65 * MapGeneratingWindow.width));
		for (int row = 0; row < MapGeneratingWindow.maxPiecesHorizontal; ++row) {
			MapGeneratingWindow.map.add(new ArrayList<Hexagon>());
			for (int column = 0; column < MapGeneratingWindow.maxPiecesVertical; ++column) {
				MapGeneratingWindow.map.get(row).add(new Hexagon(row, column, MapGeneratingWindow.width));
			}
		}
		/*
		 * fr = new FileReader("map0.txt"); br = new BufferedReader(fr);
		 * while(true){ int coordX, coordY; String line = br.readLine();
		 * if(line==null) break; String[] coords = line.split(" "); coordX
		 * =Integer.parseInt(coords[0]); coordY =Integer.parseInt(coords[1]);
		 * map.get(coordX).get(coordY).makeThisInGame(); }
		 */
	}
}
