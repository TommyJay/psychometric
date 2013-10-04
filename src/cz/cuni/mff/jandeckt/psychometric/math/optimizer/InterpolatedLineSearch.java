package cz.cuni.mff.jandeckt.psychometric.math.optimizer;

import cz.cuni.mff.jandeckt.psychometric.math.optimizer.ejml.OptimizableByGradient;

public class InterpolatedLineSearch implements LineSearch<OptimizableByGradient> {

	private OptimizableByGradient optimizable;

	@Override
	public double computeStepSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setOptimizable(OptimizableByGradient optimizable) {
		this.optimizable = optimizable;
	}

}
