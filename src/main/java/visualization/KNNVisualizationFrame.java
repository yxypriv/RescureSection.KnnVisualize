package visualization;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import utils.io.CSVFileParser;
import utils.io.CSVFileParser.CSVParseResult;
import visualization.actionListeners.LoadFileActionListener;
import visualization.interfaces.ActionCallback;
import algorithm.KNNAlgorithm;
import algorithm.interfaces.Visualizable;
import algorithm.models.KNNPoint;

public class KNNVisualizationFrame extends JFrame {

	private static final long serialVersionUID = 8800250890245894108L;

	private KNNCoordinatePanel coordinatePanel = null;
	private KNNPointInfoPanel infoPanel = null;

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

	final KNNVisualizationFrame frame = this;

	public void initMenu() {
		JMenuBar bar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		JMenuItem openSourceItem = new JMenuItem("Open Training");
		openSourceItem.setMnemonic(KeyEvent.VK_O);

		openSourceItem.addActionListener(new LoadFileActionListener(//
				"trainingFile", frame, new ActionCallback() {
					public void process(String path) {
						if (null != path) {
							CSVParseResult sourceData = CSVFileParser.parseOutsource(path, KNNPoint.class);
							coordinatePanel.setAlgorithm(new KNNAlgorithm(sourceData));
							coordinatePanel.resetRotate();
							infoPanel.init(sourceData.getTitles());

							frame.repaint();
						}
					}
				}));
		fileMenu.add(openSourceItem);
		bar.add(fileMenu);
		
		JMenuItem openTestItem = new JMenuItem("Open Testing");
		openTestItem.setMnemonic(KeyEvent.VK_T);

		openTestItem.addActionListener(new LoadFileActionListener(//
				"testingFile", frame, new ActionCallback() {
					public void process(String path) {
						if (null != path) {
							CSVParseResult testData = CSVFileParser.parseOutsource(path, KNNPoint.class);
//							coordinatePanel.setAlgorithm(new KNNAlgorithm(sourceData));
//							coordinatePanel.resetRotate();
							coordinatePanel.setTestingData(testData.getData());
//							infoPanel.init(sourceData.getTitles());

							frame.repaint();
						}
					}
				}));
		fileMenu.add(openTestItem);
		bar.add(fileMenu);
		
		setJMenuBar(bar);
	}

	public void initPanel() {
		getContentPane().setLayout(new BorderLayout());

		coordinatePanel = new KNNCoordinatePanel(this, getWidth() - 300, getHeight());
		add(coordinatePanel, BorderLayout.CENTER);

		infoPanel = new KNNPointInfoPanel(270, getHeight(), this);
		add(infoPanel, BorderLayout.LINE_END);

		// coordinatePanel.repaint();
	}

	@Override
	public void paintComponents(Graphics g) {
		coordinatePanel.repaint();
		infoPanel.repaint();
		super.paintComponents(g);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				KNNVisualizationFrame frame = new KNNVisualizationFrame();
				frame.setVisible(true);
			}
		});
	}

	public KNNCoordinatePanel getCoordinatePanel() {
		return coordinatePanel;
	}

	public KNNPointInfoPanel getInfoPanel() {
		return infoPanel;
	}

	public void setCurrentPoint(Visualizable point) {
		infoPanel.setDisplayPoint(point);
	}
}
