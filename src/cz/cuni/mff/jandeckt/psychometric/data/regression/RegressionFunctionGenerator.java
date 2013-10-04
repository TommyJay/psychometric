package cz.cuni.mff.jandeckt.psychometric.data.regression;

import cz.cuni.mff.jandeckt.psychometric.data.plot.SortedXYDataset;

public interface RegressionFunctionGenerator {

	RegressionFunction computeRegressionFunction(SortedXYDataset data);
	
}
