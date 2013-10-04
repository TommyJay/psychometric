package cz.cuni.mff.jandeckt.psychometric.math.function.ejml;

import org.ejml.data.DenseMatrix64F;

public interface MultiVariableFunction {

	double valueAt(DenseMatrix64F vector);
	int getSize();
	
}
