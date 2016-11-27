import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NewGameMenu extends JFrame implements ActionListener {
	private JComboBox[] playersBox;
	private JTextField[] colorsField;
	JColorChooser jcc;

	NewGameMenu() {
		super("Creating new game");
		setSize(800, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		init();
	}

	public void init() {
		JLabel[] playerLabel = new JLabel[6];
		JLabel[] colorLabel = new JLabel[6];

		JButton[] colorB = new JButton[6];
		JButton doneB = new JButton("Done!");

		JPanel[] panelForEach = new JPanel[6];
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

		playersBox = new JComboBox[6];
		colorsField = new JTextField[6];
		for (Integer i = 0; i < 6; ++i) {
			playersBox[i] = new JComboBox();
			playersBox[i].addItem("Empty");
			playersBox[i].addItem("Player" + (i + 1));
			playersBox[i].addItem("AI" + (i + 1));

			colorsField[i] = new JTextField(6);
			colorsField[i].setEnabled(false);
			colorsField[i].setBackground(Color.WHITE);
			colorsField[i].setName("txt" + i);
			playerLabel[i] = new JLabel("Choose player mode: ");
			colorLabel[i] = new JLabel("Choose their color: ");

			colorB[i] = new JButton("Choose color");
			colorB[i].addActionListener(this);
			colorB[i].setName(i.toString());

			panelForEach[i] = new JPanel();
			panelForEach[i].add(playerLabel[i]);
			panelForEach[i].add(playersBox[i]);
			panelForEach[i].add(colorLabel[i]);
			panelForEach[i].add(colorsField[i]);
			panelForEach[i].add(colorB[i]);
			mainPanel.add(panelForEach[i]);
		}
		doneB.addActionListener(this);
		mainPanel.add(doneB);
		add(mainPanel, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("Choose color")) {
			JButton currentB = (JButton) ae.getSource();
			int i = Integer.parseInt(currentB.getName());

			if (!playersBox[i].getSelectedItem().toString().equals("Empty")) {

				jcc = new JColorChooser();
				Color c = jcc.showDialog(this, "Pick a color!", Color.GRAY);

				colorsField[i].setBackground(c);
			}
			if (playersBox[i].getSelectedItem().toString().equals("Empty")) {
				JOptionPane op = new JOptionPane();
				op.showMessageDialog(this, "You must choose a valid player mode before selecting its color!");

			}
		}
		if (ae.getActionCommand().equals("Done!")) {
			int playersNumCounter = 0;
			for (int k = 0; k < 6; ++k) {
				if (!playersBox[k].getSelectedItem().toString().equals("Empty"))
					++playersNumCounter;
			}
			if (playersNumCounter >= 2) {
				boolean missingColor = false;
				for (int l = 0; l < 6; ++l) {
					if (!playersBox[l].getSelectedItem().toString().equals("Empty")
							&& colorsField[l].getBackground() == Color.WHITE) {
						missingColor = true;
					}
				}
				if (missingColor) {
					JOptionPane op = new JOptionPane();
					op.showMessageDialog(this, "Please choose a color for every player.");
				} else {
					this.setVisible(false);
					GameWindow gw = new GameWindow();
					gw.setVisible(true);
					for (int j = 0; j < 6; ++j) {

						if (playersBox[j].getSelectedItem().toString().equals("Player" + (j + 1))) {
							gw.addPlayer(j + 1, colorsField[j].getBackground(), false);
						}
						if (playersBox[j].getSelectedItem().toString().equals("AI" + (j + 1))) {
							gw.addPlayer(j + 1, colorsField[j].getBackground(), true);
						}
					}
				}
			} else {
				JOptionPane op = new JOptionPane();
				op.showMessageDialog(this, "You must choose at least 2 valid player mode before start the game!");
			}
		}
	}
}
