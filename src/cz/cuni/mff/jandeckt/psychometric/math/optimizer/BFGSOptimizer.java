package cz.cuni.mff.jandeckt.psychometric.math.optimizer;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.apfloat.FixedPrecisionApfloatHelper;

import cz.cuni.mff.jandeckt.psychometric.math.function.MultiVariableFunction;
import cz.cuni.mff.jandeckt.psychometric.math.function.VectorFunction;
import cz.cuni.mff.jandeckt.psychometric.math.matrix.IdentityMatrix;
import cz.cuni.mff.jandeckt.psychometric.math.matrix.ImmutableMatrix;
import cz.cuni.mff.jandeckt.psychometric.math.matrix.ImmutableVector;
import cz.cuni.mff.jandeckt.psychometric.math.matrix.Matrix;
import cz.cuni.mff.jandeckt.psychometric.math.matrix.Vector;

/**
 * @author tj
 * All variables bear the names as specified on 
 * http://en.wikipedia.org/wiki/BFGS_method
 *
 */
public class BFGSOptimizer implements QuasiNewtonOptimizer {

	/**
	 * Current point, approximation.
	 */
	private Vector x;

	/**
	 * Next point, approximation.
	 */
	private Vector x_plus;
	
	/**
	 * Current direction.
	 */
	private Vector p;
	
	/**
	 * Current step taken.
	 */
	private Vector s;
	
	/**
	 * Current step difference.
	 */
	private Vector y;
	
	/**
	 * Current estimation of the Hessian.
	 */
	private Matrix B;
	
	/**
	 * Current estimation of the inversed Hessian.
	 */
	private Matrix B_inv;
	
	/**
	 * Step size.
	 */
	private Apfloat alpha;
	
	/**
	 * Gradient of the function being optimized.
	 */
	private VectorFunction gradF;
	
	/**
	 * Function to be optimized.
	 */
	private MultiVariableFunction f;
	

	private FixedPrecisionApfloatHelper ap;

	private long iterationCounter = 0;

	private long referencePrecision;
	
	private static final Apfloat C1 = new Apfloat("0.0001"); //$NON-NLS-1$
	private static final Apfloat C2 = new Apfloat("0.9"); //$NON-NLS-1$
	
	/**
	 * Line search rate.
	 */
	private static final Apfloat LSR = new Apfloat("0.9"); //$NON-NLS-1$
	
	private static final Apfloat DEFAULT_ALPHA_LINE_SEARCH_GUESS = new Apfloat(3);
	
	public BFGSOptimizer(MultiVariableFunction function, VectorFunction gradientFunction, Vector initGuess, long precision) {
		this.ap = new FixedPrecisionApfloatHelper(precision+((long)Math.ceil(precision/2)));
		this.referencePrecision = precision;
		this.f = function;
		this.gradF = gradientFunction;
		this.x = new ImmutableVector(initGuess, this.ap);
		this.B = new IdentityMatrix(initGuess.getSize(), this.ap);
		this.B_inv = new IdentityMatrix(initGuess.getSize(), this.ap);
	}
	
	private void recalculateDirection() {
		this.p = this.B_inv.multiply(this.gradF.valueAt(this.x).minus());
	}
	
	private void performLineSearch() {
		this.alpha = DEFAULT_ALPHA_LINE_SEARCH_GUESS;
		while (!this.doesArmijoHold()) {
			this.alpha = this.ap.multiply(LSR, this.alpha);
		}
		this.x_plus = this.x.add(this.p.multiply(this.alpha));
	}
	
	private void recalculateStepTaken() {
		this.s = this.p.multiply(this.alpha);
	}
	
	private void recalculateStepDifference() {
		this.y = this.gradF.valueAt(this.x_plus).subtract(this.gradF.valueAt(this.x));
	}
	
	private void recalculateHessianApproximation() {
		Matrix y_T = this.y.transform();
		Matrix s_T = this.s.transform();
		
		Matrix a = this.y.multiply(y_T).divide(y_T.multiply(this.s).toScalar());
		
		Vector bs = this.B.multiply(this.s);
		Matrix bst = bs.multiply(s_T);
		Matrix bstb = bst.multiply(this.B);
		Apfloat div = s_T.multiply(this.y).toScalar();
		Matrix b = bstb.divide(div);
		
		this.B = this.B.add(a).subtract(b);
	}
	
	private void recalculateInverseHessianApproximation() {
		Matrix y_T = this.y.transform();
		Matrix s_T = this.s.transform();
		
		Matrix a = this.s.multiply(s_T).multiply(
								this.ap.add(s_T.multiply(this.y).toScalar()
						,
								y_T.multiply(this.B_inv).multiply(this.y).toScalar()
						)
				).divide(
						this.ap.pow(s_T.multiply(this.y).toScalar(), 2)
						);
		Matrix b = this.B_inv.multiply(this.y).multiply(s_T).add(
				this.s.multiply(y_T).multiply(this.B_inv)
			).divide(s_T.multiply(this.y).toScalar());
		
		this.B_inv = this.B_inv.add(a).subtract(b);
	}

	@Override
	public void doStep() {
		if(this.x_plus != null) this.x = this.x_plus;
		this.recalculateDirection();
		if(this.isNullary(this.p))
			return;
		this.performLineSearch();
		if(this.isNullary(this.x_plus))
			return;
		this.recalculateStepTaken();
		if(this.isNullary(this.s))
			return;
		this.recalculateStepDifference();
		if(this.isNullary(this.y))
			return;
		this.recalculateHessianApproximation();
		this.recalculateInverseHessianApproximation();
		this.iterationCounter ++;
	}

	@Override
	public Apfloat getStepSize() {
		return this.alpha;
	}

	@Override
	public Vector getCurrentOptima() {
		return this.x;
	}

	@Override
	public MultiVariableFunction getFunction() {
		return this.f;
	}

	@Override
	public Vector runOptimization() {
		while(!this.isConverged()) {
			this.doStep();
		}
		return this.x_plus;
	}

	@Override
	public boolean isConverged() {
		if(this.x_plus == null)
			return false;
		for (int i = 0; i < this.x.getSize(); i++) {
			if(this.x.getComponent(i).equalDigits(this.x_plus.getComponent(i)) < this.referencePrecision)
				return false;
		}
		return true;
	}

	@Override
	public Matrix getHessianApproximation() {
		return this.B;
	}

	@Override
	public Matrix getInverseHessianApproximation() {
		return this.B_inv;
	}

	@Override
	public VectorFunction getGradientFunction() {
		return this.gradF;
	}
	
	private boolean doesArmijoHold() {
		Vector grad_f_x = this.gradF.valueAt(this.x);
		Matrix p_T = this.p.transform();
		Vector xap = this.x.add(this.p.multiply(this.alpha));
		boolean firstCondition = 
				1 > 
				this.f.valueAt(xap).compareTo(
				this.ap.add(this.f.valueAt(this.x), p_T.multiply(this.ap.multiply(C1, this.alpha)).multiply(grad_f_x).toScalar())
				);
		boolean secondCondition =
				-1 <
				p_T.multiply(this.gradF.valueAt(xap)).toScalar().compareTo(
				p_T.multiply(C2).multiply(grad_f_x).toScalar()
				);
		return firstCondition && secondCondition;
	}
	
	private boolean isNullary(Vector vector) {
		long precision = this.ap.precision();
		for (int i = 0; i < vector.getSize(); i++) {
			if(vector.getComponent(i).equalDigits(Apfloat.ZERO) <= precision)
				return false;
		}
		return true;
	}

	
	@Override
	public long getIterationCount() {
		return this.iterationCounter;
	}

}
