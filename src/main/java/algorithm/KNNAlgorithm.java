package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

import utils.ILabeler;
import utils.algorithms.HitsUtil;
import utils.general.Pair;
import utils.io.CSVFileParser.CSVParseResult;
import algorithm.interfaces.Visualizable;

public class KNNAlgorithm {

	List<Visualizable> points;
	List<String> featureTitles;
	int default_K = 5;

	public KNNAlgorithm(CSVParseResult data) {
		this.points = data.getData();
		this.featureTitles = data.getTitles();
	}

	public String cluster(Visualizable newPoint) {
		return cluster(newPoint, default_K);
	}

	public String cluster(Visualizable newPoint, int K) {
		List<Visualizable> knn = getKNN(newPoint, K);
		Pair<String, Integer> mostHits = HitsUtil.getMostHits(knn, new ILabeler<Visualizable>() {
			public String getLabel(Visualizable e) {
				return e.getLabel();
			}
		});
		return mostHits.getV1();
	}

	/**
	 * PriorityBlockingQueue
	 * 
	 * @param newPoint
	 * @param K
	 * @return
	 */
	public List<Visualizable> getKNN(Visualizable newPoint, int K) {
		Queue<OneWayEdge> tempResult = new PriorityBlockingQueue<KNNAlgorithm.OneWayEdge>(K, Collections.reverseOrder());
		for (Visualizable point : points) {
			double distance = newPoint.getDistance(point);
			if (tempResult.size() < K)
				tempResult.add(new OneWayEdge(point, distance));
			else {
				if (distance < tempResult.peek().distance) {
					tempResult.poll();
					tempResult.add(new OneWayEdge(point, distance));
				}
			}
		}

		List<Visualizable> result = new ArrayList<Visualizable>();
		for (OneWayEdge e : tempResult) {
			result.add(e.end);
		}
		return result;
	}

	/**
	 * manual
	 * 
	 * @param newPoint
	 * @param K
	 * @return
	 */
	public List<Visualizable> getKNN2(Visualizable newPoint, int K) {
		OneWayEdge[] tempResult = new OneWayEdge[K];

		for (Visualizable point : points) {
			double distance = newPoint.getDistance(point);
			int j;
			for (j = K - 1; j >= 0 && (tempResult[j] == null || tempResult[j].distance > distance); j--)
				;
			// now j is either -1 or result[j] <= distance
			j++;
			// now j is the place to put
			for (int loader = K - 1; loader > j; loader--) {
				tempResult[loader] = tempResult[loader - 1];
			}
			tempResult[j] = new OneWayEdge(point, distance);
		}

		List<Visualizable> result = new ArrayList<Visualizable>();
		for (OneWayEdge e : tempResult) {
			result.add(e.end);
		}
		return result;
	}

	private static class OneWayEdge implements Comparable<OneWayEdge> {
		public OneWayEdge(Visualizable end, Double distance) {
			super();
			this.end = end;
			this.distance = distance;
		}

		Visualizable end;
		Double distance;

		public int compareTo(OneWayEdge o) {
			return this.distance.compareTo(o.distance);
		}
	}

	public List<Visualizable> getPoints() {
		return points;
	}

	public void setPoints(List<Visualizable> points) {
		this.points = points;
	}

	public List<String> getFeatureTitles() {
		return featureTitles;
	}

	public void setFeatureTitles(List<String> featureTitles) {
		this.featureTitles = featureTitles;
	}

}
