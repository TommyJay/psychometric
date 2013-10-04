package cz.cuni.mff.jandeckt.psychometric.math.optimizer.ejml;

import org.ejml.data.DenseMatrix64F;

public interface Optimizable {
	int getVariableCount();
	void setVariables(DenseMatrix64F vars);
	DenseMatrix64F getVariables();
	double getValue();
}
