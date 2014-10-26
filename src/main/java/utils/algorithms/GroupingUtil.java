package utils.algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import utils.ILabeler;


public class GroupingUtil {
	public static <E> Map<String, ArrayList<E>> getLabelGroupingList(Collection<E> set, ILabeler<E> labeler) {
		Map<String, ArrayList<E>> result = new HashMap<String, ArrayList<E>>();
		for (E e : set) {
			String label = labeler.getLabel(e);
			if (!result.containsKey(label))
				result.put(label, new ArrayList<E>());
			result.get(label).add(e);
		}
		return result;
	}
}
