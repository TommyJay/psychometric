package cz.cuni.mff.jandeckt.psychometric.data;

import java.util.Collection;
import java.util.List;

public interface GroupedMeasurementCollection {

	String getName();
	List<Measurement> getMeasurements();
	void addMeasurement(Measurement measurement);
	void removeMeasurement(Measurement measurement);
	void addAllMeasurements(Collection<Measurement> measurements);
	
	int getGroupCount();
	/**
	 * @param index
	 * @return The signal of the data pair at index. 
	 * An ascending sorted collection is expected as the indexing base.
	 */
	Number getGroupedSignalStrength(int index);
	
	/**
	 * @param index
	 * @return The correctness of the data pair at index.
	 * An ascending sorted collection is expected as the indexing base.
	 */
	Number getGroupedCorrectness(int index);
	
}
