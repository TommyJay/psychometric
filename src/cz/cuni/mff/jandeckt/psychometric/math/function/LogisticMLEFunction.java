package cz.cuni.mff.jandeckt.psychometric.math.function;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

import cz.cuni.mff.jandeckt.psychometric.math.matrix.ImmutableVector;
import cz.cuni.mff.jandeckt.psychometric.math.matrix.Vector;

public class LogisticMLEFunction implements SmoothMultiVariableFunction {
	
	private class GradientFunction implements VectorFunction {

		@Override
		public Vector valueAt(Vector vector) {
			if(vector == null)
				throw new IllegalArgumentException("Argument vector must not be null."); //$NON-NLS-1$
			if(vector.getSize() != 2)
				throw new IllegalArgumentException("Function takes exactly two arguments."); //$NON-NLS-1$
			Apfloat alpha = vector.getComponent(0);
			Apfloat beta = vector.getComponent(1);
			
			Apfloat[] values = new Apfloat[] {ZERO, ZERO};
			
			Apfloat pre = ZERO.subtract(ONE.divide(
					new Apfloat(LogisticMLEFunction.this.data.length, LogisticMLEFunction.this.precision)));
			for (int i = 0; i < LogisticMLEFunction.this.data[X_INDEX].length; i++) {
				Apfloat x = LogisticMLEFunction.this.data[X_INDEX][i];
				Apfloat y = LogisticMLEFunction.this.data[Y_INDEX][i];
				Apfloat expf = ApfloatMath.exp(alpha.add(beta.multiply(x)));
				values[0] = values[0].add(ONE.divide(expf.add(ONE)).subtract(y));
				values[1] = values[1].add(x.multiply(ONE.divide(expf.add(ONE)).subtract(y)));
			}
			values[0] = values[0].multiply(pre);
			values[1] = values[1].multiply(pre);
			
			return new ImmutableVector(values, LogisticMLEFunction.this.precision);
		}
		
	}
	
	private Apfloat[][] data;

	private long precision;

	private static final int X_INDEX = 0;
	private static final int Y_INDEX = 1;

	private static Apfloat ZERO;
	private static Apfloat ONE;

	public LogisticMLEFunction(Apfloat[][] data, long precision) {
		if(data == null)
			throw new IllegalArgumentException("Argument data must not be null."); //$NON-NLS-1$
		if(data.length != 2)
			throw new IllegalArgumentException("Argument data must have two rows."); //$NON-NLS-1$
		if(data[0].length != data[1].length)
			throw new IllegalArgumentException("Argument data rows must have the same length."); //$NON-NLS-1$
		this.data = data;
		this.precision = precision;
		this.ZERO = new Apfloat(0L, this.precision);
		this.ONE = new Apfloat(1L, this.precision);
	}

	@Override
	public Apfloat valueAt(Vector vector) {
		if(vector == null)
			throw new IllegalArgumentException("Argument vector must not be null."); //$NON-NLS-1$
		if(vector.getSize() != 2)
			throw new IllegalArgumentException("Function takes exactly two arguments."); //$NON-NLS-1$
		Apfloat alpha = vector.getComponent(0);
		Apfloat beta  = vector.getComponent(1);
		
		Apfloat result = ZERO.subtract(ONE.divide(new Apfloat(this.data.length, this.precision)));
		Apfloat sum = ZERO;
		for (int i = 0; i < this.data[X_INDEX].length; i++) {
			Apfloat x = this.data[X_INDEX][i];
			Apfloat y = this.data[Y_INDEX][i];
			Apfloat logf = ONE.divide(ONE.add(ApfloatMath.exp(alpha.add(beta.multiply(x)))));
			sum = sum.add(ApfloatMath.log(ApfloatMath.pow(logf, y).multiply(ONE.subtract(ApfloatMath.pow(logf, ONE.subtract(y))))));
		}
		return result.multiply(sum);
	}

	@Override
	public VectorFunction getGradientFunction() {
		// TODO Auto-generated method stub
		return null;
	}

}
