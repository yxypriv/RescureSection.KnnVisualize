package visualization.interfaces;

public interface Projector {
	double[] projectTo2D(double[] point3d, double[] camera, double[] orientation, double[] viewPosition);
}
