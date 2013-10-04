package cz.cuni.mff.jandeckt.psychometric.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultGroupedMeasurementCollection implements GroupedMeasurementCollection {
	
	private List<Measurement> measurements;
	private String name;
	private List<DataPair> groupedMeasurements;
	private boolean dirty;
		
	public DefaultGroupedMeasurementCollection(String name) {
		this(name, new ArrayList<Measurement>());
	}

	public DefaultGroupedMeasurementCollection(String name, List<Measurement> measurements) {
		this.name = name;
		this.measurements = measurements;
		this.regroupMeasurements();
	}

	// O(n*log(n))
	public synchronized void regroupMeasurements() {
		Map<Number,GroupedDataPair> map = new HashMap<Number, GroupedDataPair>();
		for (Measurement measurement : this.measurements) {
			Number signalStrength = measurement.getSignalStrength();
			GroupedDataPair dataPair = map.get(signalStrength);
			if(dataPair == null) {
				dataPair = new GroupedDataPair(signalStrength);
				map.put(signalStrength, dataPair);
			}
			dataPair.addData(measurement.isAnswerCorrect() ? 1 : 0);
		}
		ArrayList<DataPair> values = new ArrayList<DataPair>(map.values());
		Collections.sort(values);
		this.groupedMeasurements = values;
		this.dirty = false;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public List<Measurement> getMeasurements() {
		// TODO check if the commented out section is needed
//		if(this.dirty) {
//			this.regroupMeasurements();
//		}
		return Collections.unmodifiableList(this.measurements);
	}

	@Override
	public int getGroupCount() {
		if(this.dirty) {
			this.regroupMeasurements();
		}
		return this.groupedMeasurements.size();
	}

	@Override
	public Number getGroupedSignalStrength(int index) {
		if(this.dirty) {
			this.regroupMeasurements();
		}
		return this.groupedMeasurements.get(index).getSignalStrength();
	}

	@Override
	public Number getGroupedCorrectness(int index) {
		if(this.dirty) {
			this.regroupMeasurements();
		}
		return this.groupedMeasurements.get(index).getAnswer();
	}

	@Override
	public String toString() {
		return this.getName();
	}

	@Override
	public synchronized void addMeasurement(Measurement measurement) {
		this.dirty = true;
		this.measurements.add(measurement);
	}

	@Override
	public synchronized void removeMeasurement(Measurement measurement) {
		this.dirty = true;
		this.measurements.remove(measurement);
	}

	@Override
	public synchronized void addAllMeasurements(Collection<Measurement> measurements) {
		this.dirty = true;
		this.measurements.addAll(measurements);
	}

}
