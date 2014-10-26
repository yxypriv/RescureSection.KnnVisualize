package algorithm.models;

import algorithm.interfaces.CanComputeDistance;
import algorithm.interfaces.Visualizable;

public class KNNPoint implements Visualizable {
	double[] features;
	String label;

	public KNNPoint() {
	}

	public KNNPoint(double[] features, String label) {
		super();
		this.features = features;
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("label: ");
		sb.append(label).append(", ");
		sb.append("features: ");
		sb.append("[");
		for (double feature : features)
			sb.append(feature).append(", ");
		sb.delete(sb.length() - 2, sb.length());
		sb.append("]}");
		return sb.toString();
	}

	public <T extends CanComputeDistance> double getDistance(T t) {
		KNNPoint p2 = (KNNPoint) t;
		double squareSum = 0.0;
		for (int i = 0; i < features.length; i++) {
			squareSum += Math.pow(features[i] - p2.features[i], 2);
		}
		return Math.sqrt(squareSum);
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public double[] getPlots() {
		return features;
	}

	public void setPlots(double[] plots) {
		features = plots;
	}

}
