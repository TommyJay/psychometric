package cz.cuni.mff.jandeckt.psychometric.math.optimizer.ejml;

import org.ejml.data.DenseMatrix64F;

public interface OptimizableByHessian extends OptimizableByGradient {
	DenseMatrix64F getHessian();
}
