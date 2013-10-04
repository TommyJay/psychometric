package cz.cuni.mff.jandeckt.psychometric.math.optimizer.ejml;

import org.ejml.data.D1Matrix64F;
import org.ejml.data.DenseMatrix64F;
import org.ejml.data.RowD1Matrix64F;
import org.ejml.ops.CommonOps;
import org.ejml.ops.MatrixFeatures;

import cz.cuni.mff.jandeckt.psychometric.math.function.ejml.MultiVariableFunction;
import cz.cuni.mff.jandeckt.psychometric.math.function.ejml.VectorFunction;

public class ConjugateBFGSOptimizer implements ConvergingOptimizer {

	private static final double LINE_C1 = 0.0001d;
	private static final double LINE_C2 = 0.9d;
	private static final double DEFAULT_ALPHA_LINE_SEARCH_GUESS = 3d;
	private static final double LSR = 0.6d;

	private MultiVariableFunction function;
	private VectorFunction grad;

	private DenseMatrix64F y;
	
	private DenseMatrix64F x;
	private DenseMatrix64F x_plus;
	
	private DenseMatrix64F C;
	private DenseMatrix64F C_T;
	
	private DenseMatrix64F g;
	private DenseMatrix64F g_plus;
	
	private DenseMatrix64F d;
	private DenseMatrix64F d_T;
	
	private DenseMatrix64F p;
	private DenseMatrix64F p_T;
	
	private DenseMatrix64F z;
	private DenseMatrix64F z_T;

	// temporary matrices
	private DenseMatrix64F alpha_p;
	private DenseMatrix64F s_2;
	private DenseMatrix64F dz;
	private DenseMatrix64F s_3;
	private DenseMatrix64F dd;
	private DenseMatrix64F ddd;
	private DenseMatrix64F dddz;
	private DenseMatrix64F s_f;

	// line search temporaries
	private DenseMatrix64F ap;
	private DenseMatrix64F xap;
	private DenseMatrix64F pdFxap;
	private DenseMatrix64F pd;
	
	private double f;
	private double alpha;
	private int n;
	private double tau;

	public ConjugateBFGSOptimizer(MultiVariableFunction function, VectorFunction gradient, DenseMatrix64F initialGuess, double tau) {
		this.x = initialGuess;
		this.function = function;
		this.grad = gradient;
		this.tau = tau;
		
		this.initialize();
	}
	
	protected void initialize() {
		int size = this.x.getNumRows();
		this.x_plus = new DenseMatrix64F(size, 1);
		
		this.C = CommonOps.identity(size);
		this.C_T = CommonOps.identity(size);

		this.g = new DenseMatrix64F(size, 1);
		this.g_plus = new DenseMatrix64F(size, 1);

		this.d = new DenseMatrix64F(size, 1);
		this.d_T = new DenseMatrix64F(1, size);
		
		this.y = new DenseMatrix64F(size, 1);

		this.p = new DenseMatrix64F(size, 1);
		this.p_T = new DenseMatrix64F(1, size);

		this.z = new DenseMatrix64F(size, 1);
		this.z_T = new DenseMatrix64F(1, size);
		
		// temporary matrices
		this.alpha_p = new DenseMatrix64F(size, 1);
		this.s_2 = new DenseMatrix64F(size, size);
		this.dz = new DenseMatrix64F(1, 1);
		this.s_3 = new DenseMatrix64F(size, size);
		this.dd = new DenseMatrix64F(1, 1);
		this.ddd = new DenseMatrix64F(1, size);
		this.dddz = new DenseMatrix64F(1, 1);
		this.s_f = new DenseMatrix64F(size, size);
		
		// line search
		this.ap = new DenseMatrix64F(size, 1);
		this.xap = new DenseMatrix64F(size, 1);
		this.pdFxap = new DenseMatrix64F(1, 1);
		this.pd = new DenseMatrix64F(1, 1);
	}

	@Override
	public MultiVariableFunction getFunction() {
		return this.function;
	}

	@Override
	public DenseMatrix64F runOptimization() {
		while(!this.isConverged())
			this.doStep();
		return this.x_plus;
	}

	/* Expects the following to be pre-set: x, function, grad, C_T, C
	 * @see cz.cuni.mff.jandeckt.psychometric.math.optimizer.ejml.ConvergingOptimizer#doStep()
	 */
	@Override
	public void doStep() {
		
		// calculate the next x
		this.update_f();
		this.update_g();
		this.update_d();
		this.update_p();
		this.performLineSearch(); // updates alpha and x_plus
		
		// calculate the next C
		this.update_g_plus();
		this.update_y();
		this.update_z();
		this.update_C();
		
		// from x_plus to x
		this.update_x();
		
		// step counter
		this.n++;
	}

	private void update_x() {
		this.x = this.x_plus;
		this.x_plus = new DenseMatrix64F(this.x_plus);
	}

	private void update_z() {
		CommonOps.mult(this.C_T, this.y, this.z);
		CommonOps.transpose(this.z, this.z_T);
	}

	private void update_y() {
		CommonOps.sub(this.g_plus, this.g, this.y);
	}

	private void update_g_plus() {
		this.g_plus = this.grad.valueAt(this.x_plus);
	}

	private void update_p() {
		CommonOps.mult(this.C, this.d, this.p);
		CommonOps.changeSign(this.p);
		CommonOps.transpose(this.p, this.p_T);
	}

	private void update_d() {
		CommonOps.mult(this.C_T, this.g, this.d);
		CommonOps.transpose(this.d, this.d_T);
	}

	private void update_C() {
		CommonOps.mult(this.p, this.z_T, this.s_2);
		CommonOps.mult(this.d_T, this.z, this.dz);
		CommonOps.divide(this.dz.get(0,0), this.s_2);
		
		CommonOps.mult(this.p, this.d_T, this.s_3);
		CommonOps.mult(this.d_T, this.d, this.dd);
		CommonOps.scale(this.dd.get(0, 0), this.d_T, this.ddd);
		CommonOps.mult(this.ddd, this.z, this.dddz);
		CommonOps.divide(Math.sqrt(-this.dddz.get(0,0)/this.alpha), this.s_3);
		
		CommonOps.add(this.C, this.s_2, this.s_f);
		CommonOps.add(this.s_f, this.s_3, this.C);
		
		// transpose
		CommonOps.transpose(this.C, this.C_T);
	}

	private void update_g() {
		this.g = this.grad.valueAt(this.x);
	}

	private void update_f() {
		this.f = this.function.valueAt(this.x);
	}

	
	/**
	 * Requires: p, x, d
	 */
	private void performLineSearch() {
		
		// update alpha
		this.alpha = DEFAULT_ALPHA_LINE_SEARCH_GUESS;
		while (!this.doWolfConditionsHold()/* && this.alpha > this.tau*/) {
			this.alpha = this.alpha * LSR;
		}
		
		// update x_plus
		this.x_plus = this.xap;
		this.xap = new DenseMatrix64F(this.xap);
	}

	
	private boolean doWolfConditionsHold() {
		
		CommonOps.scale(this.alpha, this.p, this.ap);
		CommonOps.add(this.x, this.ap, this.xap);
		
		CommonOps.mult(this.p_T, this.d, this.pd);
		double pdVal = this.pd.get(0, 0);
		
		// Armijo
		double functionXap = this.function.valueAt(this.xap);
		boolean armijo = functionXap <= this.f + LINE_C1 * this.alpha * pdVal;
		
		CommonOps.mult(this.p_T, this.xap, this.pdFxap);
		double pdFxapVal = this.pdFxap.get(0, 0);
		
		// Curvature
		boolean curvature = pdFxapVal >= LINE_C2 * pdVal;
		
		return armijo && curvature;
	}

	@Override
	public double getStepSize() {
		return this.alpha;
	}

	@Override
	public DenseMatrix64F getCurrentOptima() {
		return this.x;
	}

	@Override
	public long getIterationCount() {
		return this.n;
	}

	@Override
	public boolean isConverged() {
		return MatrixFeatures.isEquals(this.g, this.g_plus, this.tau);
	}

}
