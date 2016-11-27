import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CreateMapWindow extends JFrame implements ActionListener {
	private JTextField filename;

	CreateMapWindow() {
		setSize(500, 200);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		init();

	}

	public void init() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());

		JLabel fileLabel = new JLabel("Enter the new file name: ");

		filename = new JTextField(20);
		filename.setEnabled(true);

		JButton makeItButton = new JButton("Make it!");
		makeItButton.addActionListener(this);

		panel.add(fileLabel);
		panel.add(filename);
		panel.add(makeItButton);

		this.add(panel);
	}

	public void actionPerformed(ActionEvent ae) {
		close();
		try {
			MapGeneratingWindow mgw = new MapGeneratingWindow(filename.getText() + ".txt");
			mgw.setVisible(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close() {
		setVisible(false);
	}
}
