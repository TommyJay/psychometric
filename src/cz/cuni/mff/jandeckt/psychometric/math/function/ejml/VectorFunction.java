package cz.cuni.mff.jandeckt.psychometric.math.function.ejml;

import org.ejml.data.DenseMatrix64F;

public interface VectorFunction {
	DenseMatrix64F valueAt(DenseMatrix64F vector);
}
