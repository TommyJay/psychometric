package cz.cuni.mff.jandeckt.psychometric.data;

import java.util.Map;

public class DefaultMeasurement implements Measurement {
	
	private Number signalStrength;
	private Number answer;
	private boolean isAnswerCorrect;
	private Map<String, Object> annotations;

	public DefaultMeasurement(Number signalStrength, Number answer, boolean isAnswerCorrect, Map<String, Object> annotations) {
		this.signalStrength = signalStrength;
		this.answer = answer;
		this.isAnswerCorrect = isAnswerCorrect;
		this.annotations = annotations;
	}

	@Override
	public Number getSignalStrength() {
		return this.signalStrength;
	}

	@Override
	public Number getAnswer() {
		return this.answer;
	}

	@Override
	public boolean isAnswerCorrect() {
		return this.isAnswerCorrect;
	}

	@Override
	public Object getAnnotation(String key) {
		return this.annotations.get(key);
	}
	
	@Override
	public int compareTo(DataPair that) {
		if(this == that) return 0;
		
		double thisSignal = this.getSignalStrength().doubleValue();
		double thatSignal = that.getSignalStrength().doubleValue();
		if(thisSignal < thatSignal)
			return -1;
		if(thisSignal > thatSignal)
			return 1;
		else
			return 0;
	}

}
