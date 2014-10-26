package visualization;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import algorithm.KNNAlgorithm;

public class KNNCoordinatePanel extends JPanel {
	private static final long serialVersionUID = -6439325409737765155L;
	Coordinates3D coordinate = null;
	KNNAlgorithm algorithm = null;
	JFrame parent = null;

	public KNNCoordinatePanel(JFrame parent) {
		this.parent = parent;
		setBorder(BorderFactory.createDashedBorder(Color.black));
		setVisible(true);
		coordinate = new Coordinates3D(0, 0, parent.getWidth(), parent.getHeight(), Coordinates3D.OPTION_RANGE);
	}

	public KNNAlgorithm getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(KNNAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		Graphics2D g2 = (Graphics2D) g;
		coordinate.paintCoordinates(g2);
	}
}
