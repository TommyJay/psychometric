package cz.cuni.mff.jandeckt.psychometric.data;

import java.util.Collections;
import java.util.List;

public class DefaultProject extends DefaultGroupedMeasurementCollection implements Project {
	
	private List<GroupedMeasurementCollection> measurementGroups;

	public DefaultProject(String name, List<GroupedMeasurementCollection> measurementGroups) {
		super(name);
		if(measurementGroups == null) {
			throw new IllegalArgumentException("Argument measurementGroups must not be null."); //$NON-NLS-1$
		}
		this.measurementGroups = measurementGroups;
		this.updateMeasurements();
	}

	private void updateMeasurements() {
		// TODO: clear measurements?
		for (GroupedMeasurementCollection measurementGroup : this.measurementGroups) {
			this.addAllMeasurements(measurementGroup.getMeasurements());
		}
	}

	@Override
	public List<GroupedMeasurementCollection> getMeasurementGroups() {
		return Collections.unmodifiableList(this.measurementGroups);
	}
	
}
