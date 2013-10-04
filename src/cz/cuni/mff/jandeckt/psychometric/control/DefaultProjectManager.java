package cz.cuni.mff.jandeckt.psychometric.control;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.jandeckt.psychometric.control.listener.ProjectListener;
import cz.cuni.mff.jandeckt.psychometric.data.GroupedMeasurementCollection;
import cz.cuni.mff.jandeckt.psychometric.data.Project;

public class DefaultProjectManager implements ProjectManager {

	private List<Project> projects = new ArrayList<Project>();
	private Project activeProject;
	
	private List<ProjectListener> listeners = new ArrayList<ProjectListener>();
	private GroupedMeasurementCollection activeMeasurementCollection;

	@Override
	public List<Project> getOpenProjects() {
		return this.projects ;
	}

	@Override
	public Project getActiveProject() {
		return this.activeProject;
	}

	@Override
	public GroupedMeasurementCollection getActiveMeasurementCollection() {
		return this.activeMeasurementCollection;
	}

	@Override
	public void openProject(Project project) {
		this.projects.add(project);
		if(this.projects.size() == 1) {
			this.setActiveProject(project);
		}
		this.fireProjectsChanged();
	}

	@Override
	public void closeProject(Project project) {
		this.projects.remove(project);
		if(project == this.activeProject) {
			this.activeProject = null;
		}
		this.fireProjectsChanged();
	}

	@Override
	public void setActiveProject(Project project) {
		if(project != this.activeProject) {
			if(!this.projects.contains(project)) {
				this.projects.add(project);
				this.fireProjectsChanged();
			}
			this.activeProject = project;
			this.activeMeasurementCollection = project;
			this.fireActiveProjectChanged();
			this.fireActiveMeasurementCollectionChanged();
		}
	}

	@Override
	public void setActiveMeasurementCollection(
			GroupedMeasurementCollection measurementCollection) {
		// TODO check what to do with parent project
		if(measurementCollection != this.activeMeasurementCollection) {
			this.activeMeasurementCollection = measurementCollection;
			fireActiveMeasurementCollectionChanged();
		}
	}

	@Override
	public void addProjectListener(ProjectListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void removeProjectListener(ProjectListener listener) {
		this.listeners.remove(listener);
	}
	
	protected void fireActiveProjectChanged() {
		for (ProjectListener listener : this.listeners) {
			listener.activeProjectChanged();
		}
	}
	
	protected void fireProjectsChanged() {
		for (ProjectListener listener : this.listeners) {
			listener.projectsChanged();
		}
	}
	
	protected void fireActiveMeasurementCollectionChanged() {
		for (ProjectListener listener : this.listeners) {
			listener.activeMeasurementCollectionChanged();
		}
	}
}
