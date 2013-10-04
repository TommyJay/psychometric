package cz.cuni.mff.jandeckt.psychometric.math.matrix;

import org.apfloat.Apfloat;
import org.apfloat.FixedPrecisionApfloatHelper;

public class ImmutableVector extends ImmutableMatrix implements Vector {

	public ImmutableVector(Apfloat[] values, long precision) {
		super(new Apfloat[][] {values}, precision);
	}
	
	public ImmutableVector(Apfloat[] values, FixedPrecisionApfloatHelper ap) {
		super(new Apfloat[][] {values}, ap);
	}

	public ImmutableVector(Vector vector, FixedPrecisionApfloatHelper ap) {
		super(vector.toArray(), ap);
	}

	@Override
	public Apfloat getComponent(int index) {
		return this.getComponent(0, index);
	}

	@Override
	public int getSize() {
		return this.getHeight();
	}

	@Override	
	public Vector multiply(Apfloat scalar) {
		if(scalar == null)
			throw new IllegalArgumentException("Argument scalar must not be null."); //$NON-NLS-1$
		
		Apfloat[] values = new Apfloat[this.getSize()];
		for (int i = 0; i < values.length; i++) {
			values[i] = this.ap.multiply(this.getComponent(i), scalar);
		}
		return new ImmutableVector(values, this.ap);
	}
	
	@Override	
	public Vector subtract(Vector otherVector) {
		if(otherVector == null)
			throw new IllegalArgumentException("Argument otherVector must not be null."); //$NON-NLS-1$
		if(this.getSize() != otherVector.getSize())
			throw new MatrixFormatException("Cannot subtract vectors of different size."); //$NON-NLS-1$
		
		Apfloat[] values = new Apfloat[this.getSize()];
		for (int i = 0; i < values.length; i++) {
			values[i] = this.ap.subtract(this.getComponent(i), otherVector.getComponent(i));
		}
		return new ImmutableVector(values, this.ap);
	}

	@Override
	public Vector minus(){
		Apfloat[] values = new Apfloat[this.getSize()];
		for (int i = 0; i < values.length; i++) {
			values[i] = this.ap.subtract(Apfloat.ZERO, this.getComponent(i));
		}
		return new ImmutableVector(values, this.ap);
	}
	
	@Override	
	public Vector add(Vector otherVector) {
		if(otherVector == null)
			throw new IllegalArgumentException("Argument otherVector must not be null."); //$NON-NLS-1$
		if(this.getSize() != otherVector.getSize())
			throw new MatrixFormatException("Cannot add vectors of different size."); //$NON-NLS-1$
		
		Apfloat[] values = new Apfloat[this.getSize()];
		for (int i = 0; i < values.length; i++) {
			values[i] = this.ap.add(this.getComponent(i), otherVector.getComponent(i));
		}
		return new ImmutableVector(values, this.ap);
	}

	@Override
	public String toString() {
		String str = "V("; //$NON-NLS-1$
		for (int i = 0; i < this.getSize(); i++) {
			str += this.getComponent(i) + " "; //$NON-NLS-1$
		}
		return str + ")"; //$NON-NLS-1$
	}

}
