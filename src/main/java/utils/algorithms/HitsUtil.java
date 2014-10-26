package utils.algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.ILabeler;
import utils.general.Pair;

public class HitsUtil {
	/**
	 * 
	 * @param set
	 * @param labelMethod
	 * @return a list of int array stands for [label, hits] with descending
	 *         order by hits.
	 */
	public static <E> List<Pair<String, Integer>> getOrderedHits(Collection<E> set, ILabeler<E> labelMethod) {
		Map<String, Integer> hitsMap = getHitsMap(set, labelMethod);

		List<Pair<String, Integer>> result = new ArrayList<Pair<String, Integer>>();
		for (String label : hitsMap.keySet()) {
			Pair<String, Integer> labelHits = new Pair<String, Integer>(label, hitsMap.get(label));
			result.add(labelHits);
		}
		Collections.sort(result, Collections.reverseOrder(new Comparator<Pair<String, Integer>>() {
			public int compare(Pair<String, Integer> arg0, Pair<String, Integer> arg1) {
				return arg0.getV2().compareTo(arg1.getV2());
			}
		}));
		return result;
	}

	/**
	 * 
	 * @param set
	 * @param labelMethod
	 * @return most hit label integer pair stands for [label, hits]
	 */

	public static <E> Pair<String, Integer> getMostHits(Collection<E> set, ILabeler<E> labelMethod) {
		Map<String, Integer> hitsMap = getHitsMap(set, labelMethod);
		Pair<String, Integer> result = null;
		for (String label : hitsMap.keySet()) {
			int hits = hitsMap.get(label);
			if (null == result || hits > result.getV2()) {
				result = new Pair<String, Integer>(label, hits);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param set
	 * @param labelMethod
	 * @return hitmap mapping from label to hits.s
	 */

	public static <E> Map<String, Integer> getHitsMap(Collection<E> set, ILabeler<E> labelMethod) {
		Map<String, Integer> hitsMap = new HashMap<String, Integer>();
		for (E e : set) {
			String label = labelMethod.getLabel(e);
			if (!hitsMap.containsKey(label))
				hitsMap.put(label, 1);
			else
				hitsMap.put(label, hitsMap.get(label) + 1);
		}
		return hitsMap;
	}
}
