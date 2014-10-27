package visualization;

import java.awt.Graphics2D;

public class Coordinates3D {
	public static final int OPTION_SCALE = 1;
	public static final int OPTION_RANGE = 2;

	private static int minimumCoordinateIntervel = 20;
	private static int padding = 50;

	private int option;

	double range[][]; // [0] is x, [1] is y, [2] is z.
	double scale[];
	// double viewPoint[];
	int panelTop = 0;
	int panelLeft = 0;
	int panelWidth = 800;
	int panelHeight = 600;

	Integer origin_x = null;
	Integer origin_y = null;

	public Coordinates3D(int option) {
		this.option = option;
		init();
	}

	public Coordinates3D(int panelTop, int panelLeft, int panelWidth, int panelHeight, int option) {
		super();
		this.panelTop = panelTop;
		this.panelLeft = panelLeft;
		this.panelWidth = panelWidth;
		this.panelHeight = panelHeight;
		this.option = option;
		init();
	}

	private void init() {
		origin_x = (panelWidth - panelLeft) / 3 + panelLeft;
		origin_y = (panelHeight - panelTop) * 2 / 3 + panelLeft;

		range = new double[3][];
		// for (int i = 0; i < range.length; i++)
		// range[i] = new double[] { 0, 10 * (i + 1) };
		 scale = new double[] { 10, 10, 10 };
	}

	public void extendRange(double[] point) {
		for (int i = 0; i < 3; i++) {
			if (range[i] == null) {
				range[i] = new double[]{point[i], point[i]};
			} else {
				if (range[i][0] > point[i])
					range[i][0] = point[i];
				if (range[i][1] < point[i])
					range[i][1] = point[i];
			}
		}
	}

	public double[] getScale() {
		return scale;
	}

	public void setScale(double[] scale) {
		this.scale = scale;
	}

	private void computScaleRange() {
		int[][] axisRange = getAxisRange();
		if (option == OPTION_RANGE) {
			double[][] thisRange = range;
			if (thisRange[0] == null) {
				for (int i = 0; i < range.length; i++)
					thisRange[i] = new double[] { 0, 10 * (i + 1) };
			}
			for (int i = 0; i < 3; i++) {
				thisRange[i] = new double[] { Math.floor(thisRange[i][0]), Math.ceil(thisRange[i][1]) };
				int rangeContain = (int) (thisRange[i][1] - thisRange[i][0] + 1);
				double plotAxisLength = Math.sqrt(Math.pow(axisRange[i][0] - axisRange[i][2], 2) + Math.pow(axisRange[i][1] - axisRange[i][3], 2));
				scale[i] = plotAxisLength / rangeContain;
			}
		} else {
			// TODO: OPTION_SCALE
		}
	}

	private int[][] getAxisRange() {
		int panel_top = panelTop + padding;
		int panel_left = panelLeft + padding;
		int panel_right = panelLeft + panelWidth - padding;
		int panel_bottom = panelTop + panelHeight - padding;

		int[][] axisRanges = new int[3][4];
		axisRanges[0] = new int[] { origin_x, origin_y, panel_right, origin_y };
		axisRanges[1] = new int[] { origin_x, origin_y, panel_left, panel_bottom };
		axisRanges[2] = new int[] { origin_x, origin_y, origin_x, panel_top };
		return axisRanges;
	}

	public void paintCoordinates(Graphics2D g2) {
		computScaleRange();
		int[][] axisRange = getAxisRange();

		for (int i = 0; i < 3; i++) {
			g2.drawLine(axisRange[i][0], axisRange[i][1], axisRange[i][2], axisRange[i][3]);
		}

		for (int i = 0; i < 3; i++) {
			int skip = (int) Math.ceil(minimumCoordinateIntervel / scale[i]);
			double plotAxisLength = Math.sqrt(Math.pow(axisRange[i][0] - axisRange[i][2], 2) + Math.pow(axisRange[i][1] - axisRange[i][3], 2));
			if (i < 2) { // x, y axis
				for (int j = 1; j * scale[i] * skip < plotAxisLength; j++) {
					double delta_length = j * scale[i] * skip;
					double ratio = delta_length / plotAxisLength;
					int plot_x = (int) (axisRange[i][0] + (axisRange[i][2] - axisRange[i][0]) * ratio);
					int plot_y = (int) (axisRange[i][1] + (axisRange[i][3] - axisRange[i][1]) * ratio);
					g2.drawLine(plot_x, plot_y - 5, plot_x, plot_y + 5);
					g2.drawString(new Integer((int) (range[i][0] + j * skip)).toString(), plot_x - 5, plot_y + 20);
				}
			} else {
				for (int j = 1; j * scale[i] * skip < plotAxisLength; j++) {
					double delta_length = j * scale[i] * skip;
					double ratio = delta_length / plotAxisLength;
					int plot_x = (int) (axisRange[i][0] + (axisRange[i][2] - axisRange[i][0]) * ratio);
					int plot_y = (int) (axisRange[i][1] + (axisRange[i][3] - axisRange[i][1]) * ratio);
					g2.drawLine(plot_x - 5, plot_y, plot_x + 5, plot_y);
					g2.drawString(new Integer((int) (range[i][0] + j * skip)).toString(), plot_x - 20, plot_y);
				}
			}
		}
	}

	public void paintPoint(Graphics2D g2, double[] point) {
		extendRange(point);
		computScaleRange();
		int[][] axisRange = getAxisRange();
		int delta_x = 0;
		int delta_y = 0;
		for (int i = 0; i < 3; i++) {
			// int skip = (int) Math.ceil(minimumCoordinateIntervel / scale[i]);
			// double plotAxisLength = Math.sqrt(Math.pow(axisRange[i][0] -
			// axisRange[i][2], 2) + Math.pow(axisRange[i][1] - axisRange[i][3],
			// 2));
			// for (int j = 1; j * scale[i] * skip < plotAxisLength; j++) {
			// double delta_length = j * scale[i] * skip;
			// double ratio = delta_length / plotAxisLength;
			double delta = point[i] - range[i][0];
			double ratio = delta / (range[i][1] - range[i][0]);
			int vector_x = (int) ((axisRange[i][2] - axisRange[i][0]) * ratio);
			int vector_y = (int) ((axisRange[i][3] - axisRange[i][1]) * ratio);
			delta_x += vector_x;
			delta_y += vector_y;
			// }
		}
		int plot_x = origin_x + delta_x;
		int plot_y = origin_y + delta_y;
//		System.out.println("point: " + plot_x + "\t" + plot_y);
		g2.drawOval(plot_x - 1, plot_y - 1, 3, 3);
	}
}
