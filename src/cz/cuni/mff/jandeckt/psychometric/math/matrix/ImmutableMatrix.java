package cz.cuni.mff.jandeckt.psychometric.math.matrix;

import org.apfloat.Apfloat;
import org.apfloat.FixedPrecisionApfloatHelper;

public class ImmutableMatrix implements Matrix {
	
	private Apfloat[][] values;
	protected FixedPrecisionApfloatHelper ap;

	public ImmutableMatrix(Apfloat[][] values, long precision) {
		this(values, new FixedPrecisionApfloatHelper(precision));
	}
	
	public ImmutableMatrix(Matrix matrix, FixedPrecisionApfloatHelper ap) {
		this(matrix.toArray(), ap);
	}
	
	public ImmutableMatrix(Apfloat[][] values, FixedPrecisionApfloatHelper ap) {
		if(values == null) {
			throw new IllegalArgumentException("Argument values must not be null."); //$NON-NLS-1$
		}
		if(values.length < 1) {
			throw new IllegalArgumentException("Argument values must have entries."); //$NON-NLS-1$
		}
		this.ap = ap;
		int secondarySize = -1;
		for (int i = 0; i < values.length; i++) {
			if(values[i] == null) {
				throw new IllegalArgumentException("Argument values[i] must not be null."); //$NON-NLS-1$
			}
			if(secondarySize >= 0 && values[i].length != secondarySize) {
				throw new IllegalArgumentException("Argument values must have uniform rows."); //$NON-NLS-1$
			}
			secondarySize = values[i].length;
		}
		this.values = values;
	}
	
	@Override
	public int getWidth() {
		return this.values.length;
	}

	@Override
	public int getHeight() {
		return this.values[0].length;
	}

	@Override
	public Apfloat getComponent(int widthIndex, int heightIndex) {
		// TODO check bounds
		return this.values[widthIndex][heightIndex];
	}

	@Override
	public Matrix add(Matrix otherMatrix) {
		if(this.getHeight() != otherMatrix.getHeight()) {
			throw new MatrixFormatException("Cannot add matrices with different height."); //$NON-NLS-1$
		}
		if(this.getWidth() != otherMatrix.getWidth()) {
			throw new MatrixFormatException("Cannot add matrices with different width."); //$NON-NLS-1$
		}
		Apfloat[][] values = new Apfloat[this.getWidth()][this.getHeight()];
		for (int i = 0; i < this.values.length; i++) {
			for (int j = 0; j < this.values[i].length; j++) {
				values[i][j] = this.ap.add(this.getComponent(i, j), otherMatrix.getComponent(i, j));
			}
		}
		return new ImmutableMatrix(values, this.ap);
	}

	@Override
	public Matrix multiply(Matrix otherMatrix) {
		return new ImmutableMatrix(getMultipliedValues(otherMatrix), this.ap);
	}

	private Apfloat[][] getMultipliedValues(Matrix otherMatrix) {
		if(this.getWidth() != otherMatrix.getHeight()) {
			throw new MatrixFormatException("Cannot multiply matrix of width w with matrix of different height h."); //$NON-NLS-1$
		}
		Apfloat[][] values = new Apfloat[otherMatrix.getWidth()][this.getHeight()];
		int opLength = this.getWidth();
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[i].length; j++) {
				values[i][j] = Apfloat.ZERO;
				for (int k = 0; k < opLength; k++) {
					values[i][j] = this.ap.add(values[i][j], this.ap.multiply(this.getComponent(k, j), otherMatrix.getComponent(i, k)));
				}
			}
		}
		return values;
	}

	@Override
	public Matrix transform() {
		Apfloat[][] values = new Apfloat[this.getHeight()][this.getWidth()];
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[0].length; j++) {
				values[i][j] = this.getComponent(j, i);
			}
		}
		return new ImmutableMatrix(values, this.ap);
	}

	@Override
	public Matrix multiply(Apfloat scalar) {
		Apfloat[][] values = new Apfloat[this.getWidth()][this.getHeight()];
		for (int i = 0; i < this.values.length; i++) {
			for (int j = 0; j < this.values[i].length; j++) {
				values[i][j] = this.ap.multiply(this.getComponent(i, j), scalar);
			}
		}
		return new ImmutableMatrix(values, this.ap);
	}

	@Override
	public Matrix subtract(Matrix otherMatrix) {
		if(this.getHeight() != otherMatrix.getHeight()) {
			throw new MatrixFormatException("Cannot subtract matrices with different height."); //$NON-NLS-1$
		}
		if(this.getWidth() != otherMatrix.getWidth()) {
			throw new MatrixFormatException("Cannot subtract matrices with different width."); //$NON-NLS-1$
		}
		Apfloat[][] values = new Apfloat[this.getWidth()][this.getHeight()];
		for (int i = 0; i < this.values.length; i++) {
			for (int j = 0; j < this.values[i].length; j++) {
				values[i][j] = this.ap.subtract(this.getComponent(i, j), otherMatrix.getComponent(i, j));
			}
		}
		return new ImmutableMatrix(values, this.ap);
	}

	@Override
	public Matrix minus() {
		Apfloat[][] values = new Apfloat[this.getWidth()][this.getHeight()];
		for (int i = 0; i < this.values.length; i++) {
			for (int j = 0; j < this.values[i].length; j++) {
				values[i][j] = this.ap.subtract(Apfloat.ZERO, this.getComponent(i, j));
			}
		}
		return new ImmutableMatrix(values, this.ap);
	}

	@Override
	public Vector multiply(Vector vector) {
		return new ImmutableVector(this.getMultipliedValues(vector)[0], this.ap);
	}

	@Override
	public Matrix divide(Apfloat scalar) {
		// TODO not zero division safe
		Apfloat[][] values = new Apfloat[this.getWidth()][this.getHeight()];
		for (int i = 0; i < this.values.length; i++) {
			for (int j = 0; j < this.values[i].length; j++) {
				values[i][j] = this.ap.divide(this.getComponent(i, j), scalar);
			}
		}
		return new ImmutableMatrix(values, this.ap);
	}

	@Override
	public Apfloat toScalar() {
		if(this.getWidth() != 1 || this.getHeight() != 1)
			throw new ArithmeticException("Only can transform to scalar if size is 1x1."); //$NON-NLS-1$
		return this.getComponent(0, 0);
	}

	@Override
	public long getPrecision() {
		return this.ap.precision();
	}

	@Override
	public Apfloat[][] toArray() {
		return this.values;
	}

}
