package cz.cuni.mff.jandeckt.psychometric.data;

import java.util.ArrayList;

public class GroupedDataPair implements DataPair {

	private Number signalStrength;
	private ArrayList<Number> rawData;
	private boolean isDataAverageUpdated;
	private Number dirtyDataAverage;

	public GroupedDataPair(Number signalStrength) {
		this.signalStrength = signalStrength;
		this.rawData = new ArrayList<Number>();
	}

	@Override
	public Number getSignalStrength() {
		return this.signalStrength;
	}

	@Override
	public Number getAnswer() {
		if(!this.isDataAverageUpdated) {
			this.dirtyDataAverage = this.calculateDataAverage();
		}
		return this.dirtyDataAverage;
	}
	
	private Number calculateDataAverage() {
		long sum = 0L;
		for (Number dataPoint : this.rawData) {
			sum += dataPoint.longValue();
		}
		return new Double(sum / (double) this.rawData.size());
	}

	public void addData(Number data) {
		this.isDataAverageUpdated = false;
		this.rawData.add(data);
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
