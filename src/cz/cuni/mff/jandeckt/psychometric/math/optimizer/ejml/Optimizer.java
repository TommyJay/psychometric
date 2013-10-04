package cz.cuni.mff.jandeckt.psychometric.math.optimizer.ejml;

import org.ejml.data.DenseMatrix64F;

import cz.cuni.mff.jandeckt.psychometric.math.function.ejml.MultiVariableFunction;

public interface Optimizer {
	MultiVariableFunction getFunction();
	DenseMatrix64F runOptimization();
}
