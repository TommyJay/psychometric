package cz.cuni.mff.jandeckt.psychometric.data.regression;

import cz.cuni.mff.jandeckt.psychometric.data.plot.SortedXYDataset;

public class DummyRegressionFunctionGenerator implements
		RegressionFunctionGenerator {

	@Override
	public RegressionFunction computeRegressionFunction(SortedXYDataset data) {
		double Ax = data.getFirstX().doubleValue();
		double Ay = data.getFirstY().doubleValue();
		double Bx = data.getLastX().doubleValue();
		double By = data.getLastY().doubleValue();
		final double a = (By-Ay)/(Bx-Ax);
		final double b = Ay - a * Ax;
		return new RegressionFunction() {
			
			@Override
			public double getValue(double x) {
				return a * x + b;
			}
			
			@Override
			public String getName() {
				return "Dummy"; //$NON-NLS-1$
			}
		};
	}

}
