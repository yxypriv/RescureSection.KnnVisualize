package algorithm.interfaces;

public interface CanComputeDistance {
	<T extends CanComputeDistance> double getDistance(T t);
}
