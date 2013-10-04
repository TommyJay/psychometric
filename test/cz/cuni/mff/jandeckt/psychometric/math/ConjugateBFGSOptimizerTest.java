package cz.cuni.mff.jandeckt.psychometric.math;

import static org.junit.Assert.*;

import org.ejml.data.DenseMatrix64F;
import org.junit.Test;

import cz.cuni.mff.jandeckt.psychometric.math.function.ejml.MultiVariableFunction;
import cz.cuni.mff.jandeckt.psychometric.math.function.ejml.VectorFunction;
import cz.cuni.mff.jandeckt.psychometric.math.optimizer.ejml.ConjugateBFGSOptimizer;

public class ConjugateBFGSOptimizerTest {
	
	@Test
	public void testConvergenceOnSimpleQuadraticFunction() {
		DenseMatrix64F initialGuess = new DenseMatrix64F(2, 1, false, 1, 1);
		VectorFunction gradient = new VectorFunction() {
			@Override
			public DenseMatrix64F valueAt(DenseMatrix64F vector) {
				double x = vector.get(0,0);
				double y = vector.get(1,0);
				
				return new DenseMatrix64F(2, 1, false, 2d*(2d*x + y - 2d), 2d*(x + 2d*y - 1d));
			}
		};
		MultiVariableFunction function = new MultiVariableFunction() {
			@Override
			public double valueAt(DenseMatrix64F vector) {
				double x = vector.get(0,0);
				double y = vector.get(1,0);
				
				double pow_1 = Math.pow(x - 2, 2);
				double pow_2 = Math.pow(y - 1, 2);
				double pow_3 = Math.pow(x + y, 2);
				
				return pow_1 + pow_2 + pow_3;
			}
			@Override
			public int getSize() {
				return 2;
			}
		};
		ConjugateBFGSOptimizer optimizer = new ConjugateBFGSOptimizer(function , gradient , initialGuess , 0.001d);
		for(int i = 0; i < 10; i++) {
			DenseMatrix64F optima = optimizer.getCurrentOptima();
			optimizer.doStep();
		}
	}

}
