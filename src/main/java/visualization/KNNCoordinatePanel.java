package visualization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import utils.general.Pair;
import algorithm.KNNAlgorithm;
import algorithm.interfaces.Visualizable;

public class KNNCoordinatePanel extends JPanel {
	private static final long serialVersionUID = -6439325409737765155L;

	private static final Color[] defaultDisplayColor = { Color.red, Color.blue, Color.green, Color.yellow };

	Coordinates3D coordinate = null;
	KNNAlgorithm algorithm = null;
	KNNVisualizationFrame parent = null;
	List<Visualizable> testingData = null;
	Integer testingIndex = null;

	int[] dimentions;

	public KNNCoordinatePanel(KNNVisualizationFrame parent, int width, int height) {
		this.parent = parent;
		dimentions = new int[] { 0, 1, 2 };
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createDashedBorder(Color.black));
		setPreferredSize(new Dimension(width, height));
		setVisible(true);
		coordinate = new Coordinates3D(0, 0, //
				width, height, Coordinates3D.OPTION_RANGE);
		initListener();
	}

	private void initListener() {
		PanelMouseListener listener = new PanelMouseListener(this);
		addMouseListener(listener);
		addMouseMotionListener(listener);
	}

	public KNNAlgorithm getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(KNNAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	public void resetRotate() {
		coordinate.resetRotate();
	}

	public void setDimention(int dimentionIndex, int dimention) {
		dimentions[dimentionIndex] = dimention;
		parent.repaint();
	}

	List<Pair<int[], Visualizable>> plots = new ArrayList<Pair<int[], Visualizable>>();
	Visualizable cursorPoint = null;

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		Graphics2D g2 = (Graphics2D) g;
		Color previousColor = g2.getColor();
		// System.out.println("~paint panel~");
		plots.clear();
		coordinate.paintCoordinates(g2);

		if (null != algorithm && null != algorithm.getPoints()) {
			// System.out.println(algorithm.getPoints().size());
			Map<String, Color> label2ColorMap = new HashMap<String, Color>();

			for (Visualizable point : algorithm.getPoints()) {
				if (!label2ColorMap.containsKey(point.getLabel()))
					label2ColorMap.put(point.getLabel(), defaultDisplayColor[label2ColorMap.size()]);

				g2.setColor(label2ColorMap.get(point.getLabel()));

				int[] paintPoint = coordinate.paintPoint(g2, _getDimentionedPoint(point), 1);
				plots.add(new Pair<int[], Visualizable>(paintPoint, point));
			}

			if (cursorPoint != null) {
				g2.setColor(label2ColorMap.get(cursorPoint.getLabel()));
				coordinate.paintPoint(g2, _getDimentionedPoint(cursorPoint), 4);
			}
			if (null != testingData && null != testingIndex) {
				Visualizable point = testingData.get(testingIndex);
				String cluster = algorithm.cluster(point, 10);

				g2.setColor(label2ColorMap.get(cluster));
				coordinate.paintRect(g2, _getDimentionedPoint(point), 3, true);

				List<Visualizable> knn = algorithm.getKNN(point, 10);
				for (Visualizable knnPoint : knn) {
					g2.setColor(label2ColorMap.get(knnPoint.getLabel()));
					coordinate.paintLine(g2, _getDimentionedPoint(point),//
							_getDimentionedPoint(knnPoint));
				}

			}
		}

		g2.setColor(previousColor);
	}

	public void setTestingData(List<Visualizable> testingData) {
		this.testingData = testingData;
		testingIndex = 0;
	}

	public Visualizable getCurrentTestingData() {
		if (null == testingData)
			return null;
		return testingData.get(testingIndex);
	}

	public void nextTestingData() {
		if (null != testingData) {
			if (null == testingIndex) {
				testingIndex = 0;
			} else {
				testingIndex = (testingIndex + 1) % testingData.size();
			}
		}
		parent.repaint();
	}

	public void previousTestingData() {
		if (null != testingData) {
			if (null == testingIndex) {
				testingIndex = 0;
			} else {
				testingIndex = (testingIndex - 1 + testingData.size()) % testingData.size();
			}
		}
		parent.repaint();
	}

	private static class PanelMouseListener implements MouseListener, MouseMotionListener {
		KNNCoordinatePanel parent = null;

		public PanelMouseListener(KNNCoordinatePanel parent) {
			this.parent = parent;
		}

		int[] lastDragLocation = null;

		public void mouseDragged(MouseEvent e) {
			if (lastDragLocation != null) {
				int deltaX = e.getX() - lastDragLocation[0];
				int deltaY = e.getY() - lastDragLocation[1];
				// System.out.println(deltaX);
				parent.coordinate.rotate(new double[] { deltaX, deltaY });
				parent.parent.repaint();
				// System.out.println(deltaX);
			}
			lastDragLocation = new int[] { e.getX(), e.getY() };
		}

		public void mouseMoved(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			if (parent.plots != null) {
				Pair<int[], Visualizable> closestPair = null;
				double closestDist = -1;
				for (Pair<int[], Visualizable> plotPair : parent.plots) {
					int[] plot = plotPair.getV1();
					double dist = Math.sqrt(Math.pow(plot[0] - x, 2) + Math.pow(plot[1] - y, 2));
					if (null == closestPair || closestDist > dist) {
						closestPair = plotPair;
						closestDist = dist;
					}
				}
				if (closestDist <= 5 && closestPair != null) {
					parent.cursorPoint = closestPair.getV2();
					parent.parent.setCurrentPoint(closestPair.getV2());
				} else {
					parent.cursorPoint = null;
					parent.parent.setCurrentPoint(null);
				}
			}
			parent.parent.repaint();
		}

		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		public void mousePressed(MouseEvent e) {
			lastDragLocation = new int[] { e.getX(), e.getY() };

		}

		public void mouseReleased(MouseEvent e) {
			lastDragLocation = null;
		}

		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

	private double[] _getDimentionedPoint(Visualizable point) {
		double[] result = new double[dimentions.length];
		for (int i = 0; i < dimentions.length; i++) {
			result[i] = point.getPlots()[dimentions[i]];
		}
		return result;
	}
}
