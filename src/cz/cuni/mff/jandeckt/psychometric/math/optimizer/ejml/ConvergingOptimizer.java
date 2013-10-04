package cz.cuni.mff.jandeckt.psychometric.math.optimizer.ejml;

import org.ejml.data.DenseMatrix64F;

public interface ConvergingOptimizer extends Optimizer {

	void doStep();
	double getStepSize();
	DenseMatrix64F getCurrentOptima();
	long getIterationCount();
	boolean isConverged();
	
}