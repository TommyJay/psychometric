package cz.cuni.mff.jandeckt.psychometric.math.matrix;

import org.apfloat.Apfloat;
import org.apfloat.FixedPrecisionApfloatHelper;

public class IdentityMatrix extends ImmutableMatrix {

	public IdentityMatrix(int size, long precision) {
		super(generateValues(size, precision), precision);
	}
	
	public IdentityMatrix(int size, FixedPrecisionApfloatHelper ap) {
		super(generateValues(size, ap.precision()), ap);
	}

	private static Apfloat[][] generateValues(int size, long precision) {
		if(size < 1)
			throw new IllegalArgumentException("Argument size must be positive."); //$NON-NLS-1$
		
		Apfloat[][] values = new Apfloat[size][size];
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[0].length; j++) {
				if(i==j)
					values[i][j] = new Apfloat(1, precision);
				else
					values[i][j] = Apfloat.ZERO;
			}
		}
		return values;
	}


}
