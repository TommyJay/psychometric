package cz.cuni.mff.jandeckt.psychometric.math.optimizer.ejml;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;

import cz.cuni.mff.jandeckt.psychometric.math.optimizer.LineSearch;

public class NewtonRaphsonOptimizer {
	
	private OptimizableByHessian optimizable;
	
	private double alpha;
	
	private DenseMatrix64F H_inv;
	private DenseMatrix64F x_plus;
	private DenseMatrix64F x;
	private DenseMatrix64F g;
	private DenseMatrix64F p;
	private DenseMatrix64F p_T;

	private DenseMatrix64F alpha_p;

	private LineSearch<OptimizableByHessian> lineSearch;

	public NewtonRaphsonOptimizer(OptimizableByHessian optimizable, LineSearch<OptimizableByHessian> lineSearch) {
		this.optimizable = optimizable;
		this.lineSearch = lineSearch;
		this.lineSearch.setOptimizable(optimizable);
		
		int n = this.optimizable.getVariableCount();
		this.H_inv = new DenseMatrix64F(n, n);
		this.x_plus = new DenseMatrix64F(n, 1);
		this.g = new DenseMatrix64F(n, 1);
		this.p = new DenseMatrix64F(n, 1);
		this.p_T = new DenseMatrix64F(1, n);
		this.alpha_p = new DenseMatrix64F(n, 1);
	}
	
	public void doStep() {
		this.x = this.optimizable.getVariables();
		DenseMatrix64F H = this.optimizable.getHessian();
		this.g = this.optimizable.getGradient();
		
		if(!CommonOps.invert(H, this.H_inv)) {
			CommonOps.pinv(H, this.H_inv);
		}
		
		CommonOps.mult(this.H_inv, this.g, this.p);
		CommonOps.changeSign(this.p);
		CommonOps.transpose(this.p, this.p_T);

		this.alpha = this.lineSearch.computeStepSize();
		
		CommonOps.scale(this.alpha, this.p, this.alpha_p);
		CommonOps.add(this.x, this.alpha_p, this.x_plus);
		
		this.optimizable.setVariables(this.x_plus);
	}

}
