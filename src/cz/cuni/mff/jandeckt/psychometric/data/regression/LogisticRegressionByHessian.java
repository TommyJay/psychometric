package cz.cuni.mff.jandeckt.psychometric.data.regression;

import org.ejml.data.DenseMatrix64F;
import org.jfree.data.xy.XYDataset;

import cz.cuni.mff.jandeckt.psychometric.math.optimizer.ejml.OptimizableByHessian;

public class LogisticRegressionByHessian implements OptimizableByHessian {

	private static final int ALPHA_BUFFER_INDEX = 0;
	private static final int BETA_BUFFER_INDEX = 1;
	private DenseMatrix64F varVect;
	private DenseMatrix64F gradient;
	private DenseMatrix64F hessian;
	private XYDataset data;
	private double value;
	private boolean dirty_value;
	private boolean dirty_gradient;
	private boolean dirty_hessian;

	public LogisticRegressionByHessian(XYDataset data, double initialAlpha, double initialBeta) {
		this.varVect = new DenseMatrix64F(2, 1, false, initialAlpha, initialBeta);
		this.data = data;
		
		this.setDirty(true);
	}

	private void calculateValue() {
		int SERIES = 0;
		double[] vars = this.varVect.getData();
		double a = vars[ALPHA_BUFFER_INDEX];
		double b = vars[BETA_BUFFER_INDEX];
		int n = this.data.getItemCount(SERIES);
		
		double sum = 0d;
		for (int i = 0; i < n; i++) {
			double x = this.data.getXValue(SERIES, i);
			double y = this.data.getYValue(SERIES, i);
			double p = 1/(1+Math.exp(-a-b*x));
			sum += Math.log(Math.pow(p,y) * Math.pow(1-p,1-y));
		}
		
		this.value = -sum;
	}

	private void calculateHessian() {
		int SERIES = 0;
		double[] vars = this.varVect.getData();
		double a = vars[ALPHA_BUFFER_INDEX];
		double b = vars[BETA_BUFFER_INDEX];
		int n = this.data.getItemCount(SERIES);

		double sum_aa = 0d;
		double sum_ab = 0d;
		double sum_bb = 0d;
		double sum_ba = 0d;
		for (int i = 0; i < n; i++) {
			double x = this.data.getXValue(SERIES, i);
			// double y = this.data.getYValue(SERIES, i); // falls off as a constant
			sum_aa += -(Math.exp(a+b*x)/Math.pow(1+Math.exp(a+b*x),2));
			sum_ab += -(Math.exp(a+b*x)*x)/Math.pow(1+Math.exp(a+b*x),2);
			sum_bb += -(Math.exp(a+b*x)*Math.pow(x,2))/Math.pow(1+Math.exp(a+b*x),2);
			sum_ba += -(Math.exp(a+b*x)*x)/Math.pow(1+Math.exp(a+b*x),2);
		}

		this.hessian = new DenseMatrix64F(2, 2, true, -sum_aa, -sum_ab, -sum_ba, -sum_bb);
	}

	private void calculateGradient() {
		int SERIES = 0;
		double[] vars = this.varVect.getData();
		double a = vars[ALPHA_BUFFER_INDEX];
		double b = vars[BETA_BUFFER_INDEX];
		int n = this.data.getItemCount(SERIES);
		
		double sum_a = 0d;
		double sum_b = 0d;
		for (int i = 0; i < n; i++) {
			double x = this.data.getXValue(SERIES, i);
			double y = this.data.getYValue(SERIES, i);
			sum_a += 1/(1+Math.exp(a+b*x))+y-1;
			sum_b += x/(1+Math.exp(a+b*x))+x*(y-1);
		}

		this.gradient = new DenseMatrix64F(2, 1, false, -sum_a, -sum_b);
	}

	@Override
	public DenseMatrix64F getGradient() {
		if(this.dirty_gradient)
			calculateGradient();
		return this.gradient;
	}

	@Override
	public int getVariableCount() {
		return 2;
	}

	@Override
	public void setVariables(DenseMatrix64F vars) {
		if(vars.getNumRows() != 2 || vars.getNumCols() != 1)
			throw new IllegalArgumentException("Logistic regression requires exactly two arguments."); //$NON-NLS-1$
		this.varVect = vars;
		this.setDirty(true);
	}

	private void setDirty(boolean b) {
		this.dirty_value = true;
		this.dirty_gradient = true;
		this.dirty_hessian = true;
	}

	@Override
	public DenseMatrix64F getVariables() {
		return this.varVect;
	}

	@Override
	public double getValue() {
		if(this.dirty_value)
			calculateValue();
		return this.value;
	}

	@Override
	public DenseMatrix64F getHessian() {
		if(this.dirty_hessian)
			calculateHessian();
		return this.hessian;
	}

}
