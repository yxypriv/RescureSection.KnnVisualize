package visualization.projector;

import visualization.interfaces.Projector;

public class GeneralProjector implements Projector {

	public double[] projectTo2D(double[] point3d, double[] camera, double[] orientation, double[] viewPosition) {
		double[][][] orientationRotation = new double[3][][];
		orientationRotation[0] = new double[][] {//
		{ 1, 0, 0 },//
				{ 0, Math.cos(-orientation[0]), -Math.sin(-orientation[0]) }, //
				{ 0, Math.sin(-orientation[0]), Math.cos(-orientation[0]) } };

		orientationRotation[1] = new double[][] {//
		{ Math.cos(-orientation[1]), 0, Math.sin(-orientation[1]) },//
				{ 0, 1, 0 }, //
				{ -Math.sin(-orientation[1]), 0, Math.cos(-orientation[1]) } };

		orientationRotation[2] = new double[][] {//
		{ Math.cos(-orientation[2]), -Math.sin(-orientation[2]), 0 }, //
				{ Math.sin(-orientation[2]), Math.cos(-orientation[2]), 0 },//
				{ 0, 0, 1 } };//

		double[][] init = { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } };
		for (int i = 0; i < 3; i++) {
			init = matrixMultiply(init, orientationRotation[i]);
		}

		double[][] cameraPlot = new double[1][3];
		for (int i = 0; i < 3; i++) {
			cameraPlot[0][i] = point3d[i] - camera[i];
		}
//		System.out.println(cameraPlot[0][0] + "\t" + cameraPlot[0][1] + "\t" + cameraPlot[0][2]);

		init = matrixMultiply(init, cameraPlot);
		double[] d = new double[3];
		for (int i = 0; i < 3; i++) {
			d[i] = init[0][i];
		}
		System.out.println(d[0] + "\t" + d[1] + "\t" + d[2]);

		double[] b = new double[2];
		b[0] = (viewPosition[2] / d[2]) * d[0] - viewPosition[0];
		b[1] = (viewPosition[2] / d[2]) * d[1] - viewPosition[1];
//		System.out.println(b[0] + "\t" + b[1]);
		return b;
	}

	private double[][] matrixMultiply(double[][] matrixLeft, double[][] matrixRight) {
		double[][] result = new double[matrixRight.length][matrixLeft[0].length];

		for (int i = 0; i < matrixRight.length; i++) {
			if (matrixLeft.length != matrixRight[i].length) {
				System.err.println("matrix column dont match");
				System.out.println(matrixLeft.length + "\t" + matrixRight[i].length);
				return null;
			}
			for (int j = 0; j < matrixLeft[0].length; j++) {
				for (int k = 0; k < matrixRight.length; k++) {
					// result[i][j] += matrixLeft[i][k] * matrixRight[k][j];
					result[i][j] += matrixLeft[k][j] * matrixRight[i][k];
				}
			}
		}
		return result;
	}

}
