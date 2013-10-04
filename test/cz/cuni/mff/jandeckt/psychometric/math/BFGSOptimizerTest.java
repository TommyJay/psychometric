package cz.cuni.mff.jandeckt.psychometric.math;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.junit.Test;

import cz.cuni.mff.jandeckt.psychometric.math.function.MultiVariableFunction;
import cz.cuni.mff.jandeckt.psychometric.math.function.VectorFunction;
import cz.cuni.mff.jandeckt.psychometric.math.matrix.ImmutableVector;
import cz.cuni.mff.jandeckt.psychometric.math.matrix.Vector;
import cz.cuni.mff.jandeckt.psychometric.math.optimizer.BFGSOptimizer;

public class BFGSOptimizerTest {

	@Test
	public void testConvergenceOnSimpleQuadraticFunction() {
		
		final long precision = 4L;
		
		MultiVariableFunction function = new MultiVariableFunction() {
			
			@Override
			public Apfloat valueAt(Vector vector) {
				if(vector == null)
					throw new IllegalArgumentException("Argument vector must not be null."); //$NON-NLS-1$
				if(vector.getSize() != 2)
					throw new IllegalArgumentException("Function defined for exactly 2 variables."); //$NON-NLS-1$
				Apfloat x = vector.getComponent(0);
				Apfloat y = vector.getComponent(1);
				return ApfloatMath.pow(x.subtract(new Apfloat(2, precision)), 2).add(ApfloatMath.pow(y.subtract(new Apfloat(2, precision)), 2));
			}
		};
		VectorFunction gradientFunction = new VectorFunction() {
			@Override
			public Vector valueAt(Vector vector) {
				if(vector == null)
					throw new IllegalArgumentException("Argument vector must not be null."); //$NON-NLS-1$
				if(vector.getSize() != 2)
					throw new IllegalArgumentException("Function defined for exactly 2 variables."); //$NON-NLS-1$
				Apfloat x = vector.getComponent(0);
				Apfloat y = vector.getComponent(1);
				
				Apfloat two = new Apfloat(2, precision);
				Apfloat xsubtract = x.subtract(two);
				Apfloat xsubtractmultiply = xsubtract.multiply(two);
				Apfloat[] values = new Apfloat[] {
						xsubtractmultiply,
						y.subtract(two).multiply(two),
				};
				return new ImmutableVector(values, precision);
			}
		};
		Vector initGuess = new ImmutableVector(new Apfloat[]{new Apfloat(1, precision),new Apfloat(1, precision)}, precision);
		
		BFGSOptimizer optimizer = new BFGSOptimizer(function, gradientFunction, initGuess, precision);
		Vector optimum = optimizer.runOptimization();
		System.out.println(String.format("Optimum at (%s, %s)", optimum.getComponent(0), optimum.getComponent(1))); //$NON-NLS-1$
		System.out.println(String.format("Iterations needed: %s", optimizer.getIterationCount())); //$NON-NLS-1$
	}

	@Test
	public void testConvergenceOnSimpleExponentialFunction() {
		
		final long precision = 5L;
		
		MultiVariableFunction function = new MultiVariableFunction() {
			
			@Override
			public Apfloat valueAt(Vector vector) {
				if(vector == null)
					throw new IllegalArgumentException("Argument vector must not be null."); //$NON-NLS-1$
				if(vector.getSize() != 2)
					throw new IllegalArgumentException("Function defined for exactly 2 variables."); //$NON-NLS-1$
				Apfloat x = vector.getComponent(0);
				Apfloat y = vector.getComponent(1);
				Apfloat xRes = Apfloat.ZERO.subtract(ApfloatMath.exp(Apfloat.ZERO.subtract(x)).divide(ApfloatMath.pow(Apfloat.ONE.add(ApfloatMath.exp(Apfloat.ZERO.subtract(x))), 2)));
				Apfloat yRes = Apfloat.ZERO.subtract(ApfloatMath.exp(Apfloat.ZERO.subtract(y)).divide(ApfloatMath.pow(Apfloat.ONE.add(ApfloatMath.exp(Apfloat.ZERO.subtract(y))), 2)));
				return xRes.add(yRes);
			}
		};
		VectorFunction gradientFunction = new VectorFunction() {
			@Override
			public Vector valueAt(Vector vector) {
				if(vector == null)
					throw new IllegalArgumentException("Argument vector must not be null."); //$NON-NLS-1$
				if(vector.getSize() != 2)
					throw new IllegalArgumentException("Function defined for exactly 2 variables."); //$NON-NLS-1$
				Apfloat x = vector.getComponent(0);
				Apfloat y = vector.getComponent(1);

				Apfloat xRes = (ApfloatMath.exp(x).multiply((new Apfloat(-1, precision).add(ApfloatMath.exp(x)))).divide(ApfloatMath.pow(Apfloat.ONE.add(ApfloatMath.exp(x)), 3)));
				Apfloat yRes = (ApfloatMath.exp(y).multiply((new Apfloat(-1, precision).add(ApfloatMath.exp(y)))).divide(ApfloatMath.pow(Apfloat.ONE.add(ApfloatMath.exp(y)), 3)));
				Apfloat[] values = new Apfloat[] {
						xRes,
						yRes,
				};
				return new ImmutableVector(values, precision);
			}
		};
		Vector initGuess = new ImmutableVector(new Apfloat[]{new Apfloat(1, precision),new Apfloat(1, precision)}, precision);
		
		BFGSOptimizer optimizer = new BFGSOptimizer(function, gradientFunction, initGuess, precision);
		Vector optimum = optimizer.runOptimization();
		System.out.println(String.format("Optimum at (%s, %s)", optimum.getComponent(0), optimum.getComponent(1))); //$NON-NLS-1$
		System.out.println(String.format("Iterations needed: %s", optimizer.getIterationCount())); //$NON-NLS-1$
	}

}
