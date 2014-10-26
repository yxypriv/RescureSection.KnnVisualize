package visualization;

import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import utils.io.CSVFileParser;
import visualization.actionListeners.LoadFileActionListener;
import visualization.interfaces.ActionCallback;
import algorithm.KNNAlgorithm;
import algorithm.interfaces.Visualizable;
import algorithm.models.KNNPoint;

public class KNNVisualizationFrame extends JFrame {

	private static final long serialVersionUID = 8800250890245894108L;

	private KNNCoordinatePanel coordinatePanel = null;

	public KNNVisualizationFrame() {
		initFrame();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void initFrame() {
		setTitle("KNN Visualization");
		setSize(1024, 768);
		setLocationRelativeTo(null);
		initMenu();
		initPanel();
		repaint();
	}

	public void initMenu() {
		JMenuBar bar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		JMenuItem openSourceItem = new JMenuItem("Open");
		openSourceItem.setMnemonic(KeyEvent.VK_O);

		final KNNVisualizationFrame frame = this;
		openSourceItem.addActionListener(new LoadFileActionListener(//
				"sourceFile", frame, new ActionCallback() {
					public void process(String path) {
						if (null != path) {
							List<Visualizable> sourceData = CSVFileParser.parseOutsource(path, KNNPoint.class);
							coordinatePanel.setAlgorithm(new KNNAlgorithm(sourceData));
						}
					}
				}));
		fileMenu.add(openSourceItem);
		bar.add(fileMenu);
		setJMenuBar(bar);
	}

	public void initPanel() {
		coordinatePanel = new KNNCoordinatePanel(this);
		add(coordinatePanel);
		coordinatePanel.repaint();
	}

	public static void main(String[] args) {
		// KNNVisualizationFrame frame = new KNNVisualizationFrame();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				KNNVisualizationFrame frame = new KNNVisualizationFrame();
				frame.setVisible(true);
			}
		});
	}
}
