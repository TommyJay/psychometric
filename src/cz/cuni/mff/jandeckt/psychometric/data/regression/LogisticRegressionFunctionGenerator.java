package cz.cuni.mff.jandeckt.psychometric.data.regression;

import cc.mallet.optimize.InvalidOptimizableException;
import cc.mallet.optimize.LimitedMemoryBFGS;
import cc.mallet.optimize.Optimizable.ByGradientValue;
import cc.mallet.optimize.OptimizationException;
import cz.cuni.mff.jandeckt.psychometric.data.plot.SortedXYDataset;

public class LogisticRegressionFunctionGenerator implements
		RegressionFunctionGenerator {

	private class MLE implements ByGradientValue {

		private static final int ALPHA_BUFFER_INDEX = 0;
		private static final int BETA_BUFFER_INDEX = 1;
		private SortedXYDataset data;
		private double[] parameters;

		public MLE(SortedXYDataset data) {
			this.data = data;
			
			// doing linear regression to estimate initial a, b
//			double Ax = data.getFirstX().doubleValue();
//			double Ay = data.getFirstY().doubleValue();
//			double Bx = data.getLastX().doubleValue();
//			double By = data.getLastY().doubleValue();
//			final double a = (By-Ay)/(Bx-Ax);
//			final double b = Ay - a * Ax;
			
			this.parameters = new double[] {-8,1};
		}

		@Override
		public int getNumParameters() {
			return 2;
		}

		@Override
		public void getParameters(double[] buffer) {
			buffer[ALPHA_BUFFER_INDEX] = this.parameters[ALPHA_BUFFER_INDEX];
			buffer[BETA_BUFFER_INDEX] = this.parameters[BETA_BUFFER_INDEX];
		}

		@Override
		public double getParameter(int index) {
			return this.parameters[index];
		}

		@Override
		public void setParameters(double[] params) {
			this.parameters[ALPHA_BUFFER_INDEX] = params[ALPHA_BUFFER_INDEX];
			this.parameters[BETA_BUFFER_INDEX] = params[BETA_BUFFER_INDEX];
		}

		@Override
		public void setParameter(int index, double value) {
			this.parameters[index] = value;
		}

		@Override
		public void getValueGradient(double[] buffer) {
			int SERIES = 0;
			double a = this.parameters[ALPHA_BUFFER_INDEX];
			double b = this.parameters[BETA_BUFFER_INDEX];
			int n = this.data.getItemCount(SERIES);
			
			double sum_a = 0d;
			double sum_b = 0d;
			for (int i = 0; i < n; i++) {
				double x = this.data.getXValue(SERIES, i);
				double y = this.data.getYValue(SERIES, i);
				sum_a += 1/(1+Math.exp(a+b*x))+y-1;
				sum_b += x/(1+Math.exp(a+b*x))+x*(y-1);
			}

			buffer[ALPHA_BUFFER_INDEX] = -sum_a;
			buffer[BETA_BUFFER_INDEX] = -sum_b;
		}

		@Override
		public double getValue() {
			int SERIES = 0;
			double a = this.parameters[ALPHA_BUFFER_INDEX];
			double b = this.parameters[BETA_BUFFER_INDEX];
			int n = this.data.getItemCount(SERIES);
			
			double sum = 0d;
			for (int i = 0; i < n; i++) {
				double x = this.data.getXValue(SERIES, i);
				double y = this.data.getYValue(SERIES, i);
				double p = 1/(1+Math.exp(-a-b*x));
				sum += Math.log(Math.pow(p,y) * Math.pow(1-p,1-y));
			}
			return -sum;
		}
		
	}
	
	@Override
	public RegressionFunction computeRegressionFunction(SortedXYDataset data) {
		MLE optimizable = new MLE(data);
		LimitedMemoryBFGS optimizer = new LimitedMemoryBFGS(optimizable);
		
		try {
			optimizer.optimize();
		} catch (OptimizationException e) {
			e.printStackTrace();
		}
		
		return new LogisticRegressionFunction("Test", optimizable.getParameter(0), optimizable.getParameter(1)); //$NON-NLS-1$
	}

}
