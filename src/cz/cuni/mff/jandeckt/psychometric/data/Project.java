package cz.cuni.mff.jandeckt.psychometric.data;

import java.util.List;

public interface Project extends GroupedMeasurementCollection {
		
	List<GroupedMeasurementCollection> getMeasurementGroups();
	
}
