package cz.cuni.mff.jandeckt.psychometric.math.optimizer;

import org.apfloat.Apfloat;

import cz.cuni.mff.jandeckt.psychometric.math.matrix.Vector;

public interface ConvergingOptimizer extends Optimizer {

	void doStep();
	Apfloat getStepSize();
	Vector getCurrentOptima();
	long getIterationCount();
	boolean isConverged();
	
}
