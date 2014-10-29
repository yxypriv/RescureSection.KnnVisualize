package visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import algorithm.interfaces.Visualizable;

public class KNNPointInfoPanel extends JPanel {

	private static final long serialVersionUID = 1506968596659026166L;

	KNNVisualizationFrame parent = null;

	List<JLabel> featureValueLabelList = null;
	List<JLabel> testingValueLabelList = null;

	private List<String> featureTitles;
	private Map<String, Integer> feature2Index;
	private Map<String, Integer> dimention2Index;

	public KNNPointInfoPanel(int width, int height, KNNVisualizationFrame parent) {
		this.parent = parent;
		setPreferredSize(new Dimension(210, 480));
		setBorder(BorderFactory.createDashedBorder(Color.red));
		setLayout(null);
	}

	public void init(List<String> featureTitles) {
		this.featureTitles = featureTitles;
		if (feature2Index == null)
			feature2Index = new HashMap<String, Integer>();
		else
			feature2Index.clear();
		for (int i = 0; i < featureTitles.size(); i++) {
			feature2Index.put(featureTitles.get(i), i);
		}
		initComponents();
	}

	private void initComponents() {
		this.removeAll();

		JPanel featurePanel = new JPanel();
		featurePanel.setBounds(10, 11, 190, 150);
		add(featurePanel);
		featurePanel.setLayout(null);
		if (null == featureValueLabelList)
			featureValueLabelList = new ArrayList<JLabel>();
		else
			featureValueLabelList.clear();

		for (int i = 0; i < featureTitles.size(); i++) {
			String featureTitle = featureTitles.get(i);
			JLabel lblNewLabel = new JLabel(featureTitle);
			lblNewLabel.setBounds(10, 8 + 25 * i, 70, 20);
			featurePanel.add(lblNewLabel);

			JLabel valueLabel = new JLabel();
			valueLabel.setBounds(90, 8 + 25 * i, 70, 20);
			featurePanel.add(valueLabel);
			featureValueLabelList.add(valueLabel);
		}

		JPanel dimentionPanel = new JPanel();
		dimentionPanel.setBounds(10, 172, 190, 150);
		add(dimentionPanel);
		dimentionPanel.setLayout(null);

		String[] dimentionLabels = { "X", "Y", "Z" };
		if (dimention2Index == null)
			dimention2Index = new HashMap<String, Integer>();
		else
			dimention2Index.clear();
		for (int i = 0; i < dimentionLabels.length; i++)
			dimention2Index.put(dimentionLabels[i], i);
		int[] dimentions = new int[] { 0, 1, 2 };
		for (int i = 0; i < dimentionLabels.length; i++) {
			JLabel lblNewLabel = new JLabel(dimentionLabels[i]);
			lblNewLabel.setBounds(10, 8 + 25 * i, 46, 20);
			dimentionPanel.add(lblNewLabel);

			JComboBox<String> comboBox = new JComboBox<String>();
			for (String featureTitle : featureTitles) {
				comboBox.addItem(featureTitle);
			}
			comboBox.setSelectedIndex(dimentions[i]);
			comboBox.setName(dimentionLabels[i]);
			comboBox.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					@SuppressWarnings("unchecked")
					JComboBox<String> source = ((JComboBox<String>) e.getSource());
					parent.getCoordinatePanel().setDimention(//
							dimention2Index.get(source.getName()), feature2Index.get(e.getItem()));
					parent.repaint();
				}
			});
			comboBox.setBounds(60, 5 + 25 * i, 100, 20);
			dimentionPanel.add(comboBox);
		}

		JPanel testingPanel = new JPanel();
		testingPanel.setBounds(10, 342, 190, 150);
		add(testingPanel);
		testingPanel.setLayout(null);

		if (null == testingValueLabelList)
			testingValueLabelList = new ArrayList<JLabel>();
		else
			testingValueLabelList.clear();

		int lastHeight = 0;
		for (int i = 0; i < featureTitles.size(); i++) {
			String featureTitle = featureTitles.get(i);
			JLabel lblNewLabel = new JLabel(featureTitle);
			lblNewLabel.setBounds(10, 8 + 25 * i, 70, 20);
			featurePanel.add(lblNewLabel);

			JLabel valueLabel = new JLabel();
			valueLabel.setBounds(90, 8 + 25 * i, 70, 20);
			lastHeight = 10 + 8 + 25 * i + 20;
			testingPanel.add(valueLabel);
			testingValueLabelList.add(valueLabel);
		}

		JButton preTestButton = new JButton();
		preTestButton.setText("previous");
		preTestButton.setBounds(10, lastHeight, 70, 20);
		preTestButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.getCoordinatePanel().previousTestingData();
			}
		});
		testingPanel.add(preTestButton);

		JButton nextTestButton = new JButton();
		nextTestButton.setText("next");
		nextTestButton.setBounds(90, lastHeight, 70, 20);
		nextTestButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.getCoordinatePanel().nextTestingData();
			}
		});
		testingPanel.add(nextTestButton);
	}

	public void setDisplayPoint(Visualizable point) {
		if (null != featureValueLabelList) {
			if (null != point) {
				double[] features = point.getPlots();
				for (int i = 0; i < featureValueLabelList.size(); i++) {
					featureValueLabelList.get(i).setText("" + features[i]);
				}
			} else {
				for (int i = 0; i < featureValueLabelList.size(); i++) {
					featureValueLabelList.get(i).setText("");
				}
			}
		}
	}

	@Override
	protected void paintBorder(Graphics g) {
		// System.out.println("border : " + getX());
		super.paintBorder(g);
	}

	@Override
	public void paintAll(Graphics g) {
		super.paintAll(g);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
