package cz.cuni.mff.jandeckt.psychometric.control;

import java.util.List;

import cz.cuni.mff.jandeckt.psychometric.control.listener.ProjectListener;
import cz.cuni.mff.jandeckt.psychometric.data.GroupedMeasurementCollection;
import cz.cuni.mff.jandeckt.psychometric.data.Project;

public interface ProjectManager {
	List<Project> getOpenProjects();
	Project getActiveProject();
	GroupedMeasurementCollection getActiveMeasurementCollection();
	void openProject(Project project);
	void closeProject(Project project);
	void setActiveProject(Project project);
	void setActiveMeasurementCollection(GroupedMeasurementCollection measurementCollection);
	void addProjectListener(ProjectListener listener);
	void removeProjectListener(ProjectListener listener);
}
