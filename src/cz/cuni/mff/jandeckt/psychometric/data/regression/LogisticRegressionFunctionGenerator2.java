package cz.cuni.mff.jandeckt.psychometric.data.regression;

import org.ejml.data.DenseMatrix64F;

import cz.cuni.mff.jandeckt.psychometric.data.plot.SortedXYDataset;
import cz.cuni.mff.jandeckt.psychometric.math.optimizer.ejml.NewtonRaphsonOptimizer;

public class LogisticRegressionFunctionGenerator2 implements
		RegressionFunctionGenerator {

	@Override
	public RegressionFunction computeRegressionFunction(SortedXYDataset data) {

		LogisticRegressionByHessian optimizable = new LogisticRegressionByHessian(data, -5, 1);
		NewtonRaphsonOptimizer optimizer = new NewtonRaphsonOptimizer(optimizable);
		
		for (int i = 0; i < 3; i++) {
			optimizer.doStep();
		}
		double[] vars = optimizable.getVariables().getData();
		System.out.println("ALPHA: " + vars[0]); //$NON-NLS-1$
		System.out.println("BETA: " + vars[1]); //$NON-NLS-1$
		return new LogisticRegressionFunction("TEST", vars[0], vars[1]); //$NON-NLS-1$
	}

}
