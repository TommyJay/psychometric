package cz.cuni.mff.jandeckt.psychometric.math.optimizer;

import cz.cuni.mff.jandeckt.psychometric.math.function.MultiVariableFunction;
import cz.cuni.mff.jandeckt.psychometric.math.matrix.Vector;

public interface Optimizer {
	
	MultiVariableFunction getFunction();
	Vector runOptimization();
	
}
