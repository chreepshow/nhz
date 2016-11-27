import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainMenu extends JFrame implements ActionListener {

	public static void main(String[] args) {
		new MainMenu().setVisible(true);
	}

	MainMenu() {
		setSize(400, 300);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JButton newGameButton = new JButton("New Game");
		newGameButton.addActionListener(this);
		newGameButton.setPreferredSize(new Dimension(20, 50));

		JButton loadGameButton = new JButton("Load Game");
		loadGameButton.addActionListener(this);

		JButton createMapButton = new JButton("Create Map");
		createMapButton.addActionListener(this);

		JButton quitButton = new JButton("Quit");
		quitButton.addActionListener(this);

		JPanel buttonContainer = new JPanel();
		buttonContainer.setLayout(new GridLayout(4, 0));
		buttonContainer.add(newGameButton);
		buttonContainer.add(loadGameButton);
		buttonContainer.add(createMapButton);
		buttonContainer.add(quitButton);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout());
		mainPanel.add(buttonContainer);

		add(mainPanel);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		close();
		switch (ae.getActionCommand()) {
		case "New Game":
			NewGameMenu ngm = new NewGameMenu();
			ngm.setVisible(true);
			break;
		case "Load Game":
			LoadGameMenu lgm = new LoadGameMenu();
			lgm.setVisible(true);
			break;
		case "Create Map":
			CreateMapWindow cmw = new CreateMapWindow();
			cmw.setVisible(true);
			break;
		case "Quit":
			close();
			break;
		}
	}

	public void close() {
		setVisible(false);
	}

}
