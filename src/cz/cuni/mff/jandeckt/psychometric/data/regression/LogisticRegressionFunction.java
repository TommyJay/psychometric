package cz.cuni.mff.jandeckt.psychometric.data.regression;

public class LogisticRegressionFunction implements RegressionFunction {

	private String name;
	private double alpha;
	private double beta;

	public LogisticRegressionFunction(String name, double alpha, double beta) {
		this.name = name;
		this.alpha = alpha;
		this.beta = beta;
	}
	
	@Override
	public double getValue(double x) {
		return 1/(1+Math.exp(-(this.alpha+this.beta*x)));
	}

	@Override
	public String getName() {
		return this.name;
	}

}
