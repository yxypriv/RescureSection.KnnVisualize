package visualization;

import java.awt.Graphics2D;

public class Coordinates3D {
	public static final int OPTION_SCALE = 1;
	public static final int OPTION_RANGE = 2;

	private static final double arcsInit[] = new double[] { 0, -120 * 2 * Math.PI / 360, 90 * 2 * Math.PI / 360 };

	private static int minimumCoordinateIntervel = 20;
	private static int padding = 50;

	int rotate = 0;
	int axisLength = 400;
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

	// -----------------------actions-----------------------------------
	public void extendRange(double[] point) {
		for (int i = 0; i < 3; i++) {
			if (range[i] == null) {
				range[i] = new double[] { point[i], point[i] };
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

	public void rotate(double[] angle) {
		rotate += angle[0];
		if (rotate > 180) {
			rotate -= 360;
		} else if (rotate <= -180) {
			rotate += 360;
		}
	}

	public void resetRotate() {
		rotate = 0;
	}

	private void computScaleRange() {
		// int[][] axisRange = getAxisRange();
		if (option == OPTION_RANGE) {
			for (int i = 0; i < 3; i++) {
				double[] normalizedRange = new double[] { Math.floor(getRange()[i][0]), Math.ceil(getRange()[i][1]) };
				// if (i == 0) {
				// System.out.println("normalized x : \t" + normalizedRange[0] +
				// "\t" + normalizedRange[1]);
				// }
				int rangeContain = (int) (normalizedRange[1] - normalizedRange[0] + 1);
				// double plotAxisLength = Math.sqrt(Math.pow(axisRange[i][0] -
				// axisRange[i][2], 2) + Math.pow(axisRange[i][1] -
				// axisRange[i][3], 2));
				scale[i] = axisLength / rangeContain;
			}
		} else {
			// TODO: OPTION_SCALE
		}
	}

	private double[][] getRange() {
		if (range == null || range[0] == null) {
			double[][] defaultRange = new double[3][];
			for (int i = 0; i < range.length; i++)
				defaultRange[i] = new double[] { 0, 10 * (i + 1) };
			return defaultRange;
		}
		return range;
	}

	private int[][] getAxisInitRange() {
		int[][] axisEnd = new int[3][2];
		for (int i = 0; i < 3; i++) {
			axisEnd[i][0] = (int) (origin_x + Math.cos(arcsInit[i]) * axisLength);
			axisEnd[i][1] = (int) (origin_y - Math.sin(arcsInit[i]) * axisLength);
		}

		int[][] axisRanges = new int[3][4];
		for (int i = 0; i < 3; i++) {
			axisRanges[i] = new int[] { origin_x, origin_y, axisEnd[i][0], axisEnd[i][1] };
		}
		return axisRanges;
	}

	private int[][] getAxisRange() {
		int[][] axisInitRange = getAxisInitRange();
		double[] axis = new double[2]; // x, y
		for (int i = 0; i < 2; i++) {
			axis[i] = getRange()[i][0] * 0.9;
			// axis[i] = (getRange()[i][0] + getRange()[i][1]) / 2;
			// axis[i] = getRange()[i][1];
		}
		double[] newOriginValue = getRotateValueAroundAxis(axis, //
				new double[] { getRange()[0][0], getRange()[1][0], getRange()[2][0] }, rotate);
		int[] originPlot = getPlotPointGivingValueInAxisRange(newOriginValue, getRange(), axisInitRange);

		double rotateArc = rotate * 2 * Math.PI / 360;
		int[][] axisEnd = new int[3][2];
		for (int i = 0; i < 3; i++) {
			if (i < 2) {
				axisEnd[i][0] = (int) (originPlot[0] + Math.cos(arcsInit[i] + rotateArc) * axisLength);
				axisEnd[i][1] = (int) (originPlot[1] - Math.sin(arcsInit[i] + rotateArc) * axisLength);

			} else {
				axisEnd[i][0] = (int) (originPlot[0] + Math.cos(arcsInit[i]) * axisLength);
				axisEnd[i][1] = (int) (originPlot[1] - Math.sin(arcsInit[i]) * axisLength);
			}
		}

		int[][] axisRanges = new int[3][4];
		for (int i = 0; i < 3; i++) {
			axisRanges[i] = new int[] { originPlot[0], originPlot[1], axisEnd[i][0], axisEnd[i][1] };
		}

		// int panel_top = panelTop + padding;
		// int panel_left = panelLeft + padding;
		// int panel_right = panelLeft + panelWidth - padding;
		// int panel_bottom = panelTop + panelHeight - padding;
		//
		// axisRanges[0] = new int[] { originPlot[0], originPlot[1],
		// panel_right, originPlot[1] };
		// axisRanges[1] = new int[] { originPlot[0], originPlot[1], panel_left,
		// panel_bottom };
		// axisRanges[2] = new int[] { originPlot[0], originPlot[1],
		// originPlot[0], panel_top };

		return axisRanges;
	}

	private double[] getRotateValueAroundAxis(double[] axis, double[] originValue, int angle) {
		double[] newValue = new double[3];
		double theta0 = Math.atan((originValue[1] - axis[1]) / (originValue[0] - axis[0]));
		double theta = angle * 2 * Math.PI / 360;
		double xyLength = Math.sqrt(Math.pow(originValue[0] - axis[0], 2) + //
				Math.pow(originValue[1] - axis[1], 2));
		newValue[0] = originValue[0] + xyLength * (Math.cos(theta0) - Math.cos(theta0 + theta));
		newValue[1] = originValue[1] + xyLength * (Math.sin(theta0 + theta) - Math.sin(theta0));
		newValue[2] = originValue[2];
		return newValue;
	}

	private int[] getPlotPointGivingValueInAxisRange(double[] value, double[][] axisValueRange, int[][] axisRange) {

		int delta_x = 0;
		int delta_y = 0;
		for (int i = 0; i < 3; i++) {
			double delta = value[i] - axisValueRange[i][0];
			double ratio = delta / (axisValueRange[i][1] - axisValueRange[i][0]);
			int vector_x = (int) ((axisRange[i][2] - axisRange[i][0]) * ratio);
			int vector_y = (int) ((axisRange[i][3] - axisRange[i][1]) * ratio);
			delta_x += vector_x;
			delta_y += vector_y;
		}
		int plot_x = origin_x + delta_x;
		int plot_y = origin_y + delta_y;
		return new int[] { plot_x, plot_y };
	}

	// --------------paint----------------

	public void paintCoordinates(Graphics2D g2) {
		computScaleRange();
		int[][] axisRange = getAxisRange();

		for (int i = 0; i < 3; i++) {
			// System.out.println(axisRange[i][2] + "\t" + axisRange[i][3]);
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
					g2.drawString(new Integer((int) (getRange()[i][0] + j * skip)).toString(), plot_x - 5, plot_y + 20);
				}
			} else {
				for (int j = 1; j * scale[i] * skip < plotAxisLength; j++) {
					double delta_length = j * scale[i] * skip;
					double ratio = delta_length / plotAxisLength;
					int plot_x = (int) (axisRange[i][0] + (axisRange[i][2] - axisRange[i][0]) * ratio);
					int plot_y = (int) (axisRange[i][1] + (axisRange[i][3] - axisRange[i][1]) * ratio);
					g2.drawLine(plot_x - 5, plot_y, plot_x + 5, plot_y);
					g2.drawString(new Integer((int) (getRange()[i][0] + j * skip)).toString(), plot_x - 20, plot_y);
				}
			}
		}
	}

	public int[] paintPoint(Graphics2D g2, double[] point, int radius) {
		extendRange(point);
		// System.out.println("x range:" + range[0][0] + "\t" + range[0][1]);
		computScaleRange();
		int[][] axisRange = getAxisRange();
		int[] plot = getPlotPointGivingValueInAxisRange(point, getRange(), axisRange);
		g2.drawOval(plot[0] - radius, plot[1] - radius, 2 * radius + 1, 2 * radius + 1);
		return plot;
	}

	public int[] paintRect(Graphics2D g2, double[] point, int radius, boolean fill) {
		extendRange(point);
		// System.out.println("x range:" + range[0][0] + "\t" + range[0][1]);
		computScaleRange();
		int[][] axisRange = getAxisRange();
		int[] plot = getPlotPointGivingValueInAxisRange(point, getRange(), axisRange);
		if (fill)
			g2.fillRect(plot[0] - radius, plot[1] - radius, 2 * radius + 1, 2 * radius + 1);
		else
			g2.drawRect(plot[0] - radius, plot[1] - radius, 2 * radius + 1, 2 * radius + 1);
		return plot;
	}
	
	public void paintLine(Graphics2D g2, double[] point1, double[] point2) {
		extendRange(point1);
		extendRange(point2);
		// System.out.println("x range:" + range[0][0] + "\t" + range[0][1]);
		computScaleRange();
		int[][] axisRange = getAxisRange();
		int[] plot1 = getPlotPointGivingValueInAxisRange(point1, getRange(), axisRange);
		int[] plot2 = getPlotPointGivingValueInAxisRange(point2, getRange(), axisRange);
		g2.drawLine(plot1[0],   plot1[1], plot2[0], plot2[1]);
	}
}
