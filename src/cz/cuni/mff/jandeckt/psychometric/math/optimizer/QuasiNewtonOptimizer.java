package cz.cuni.mff.jandeckt.psychometric.math.optimizer;

import cz.cuni.mff.jandeckt.psychometric.math.function.VectorFunction;
import cz.cuni.mff.jandeckt.psychometric.math.matrix.Matrix;

public interface QuasiNewtonOptimizer extends ConvergingOptimizer {

	Matrix getHessianApproximation();
	Matrix getInverseHessianApproximation();
	
	VectorFunction getGradientFunction();
	
}
