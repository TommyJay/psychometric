package cz.cuni.mff.jandeckt.psychometric.math.optimizer;

import cz.cuni.mff.jandeckt.psychometric.math.optimizer.ejml.Optimizable;

public interface LineSearch<O extends Optimizable> {
	void setOptimizable(O optimizable);
	double computeStepSize();
}
