package cz.cuni.mff.jandeckt.psychometric.view;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

import cz.cuni.mff.jandeckt.psychometric.control.ProjectManager;
import cz.cuni.mff.jandeckt.psychometric.data.GroupedMeasurementCollection;
import cz.cuni.mff.jandeckt.psychometric.data.Project;
import cz.cuni.mff.jandeckt.psychometric.data.ProjectTreeModel;

public class ProjectTree extends JTree {
	
	private class TreeSelectionListenerImpl implements TreeSelectionListener {

		@Override
		public void valueChanged(TreeSelectionEvent e) {
			Object measurementCollection = ProjectTree.this.getLastSelectedPathComponent();
			if(Project.class.isAssignableFrom(measurementCollection.getClass())) {
				ProjectTree.this.projectManager
					.setActiveProject(((Project) measurementCollection));
			} else if(GroupedMeasurementCollection.class.isAssignableFrom(measurementCollection.getClass())) {
				ProjectTree.this.projectManager
					.setActiveMeasurementCollection(((GroupedMeasurementCollection) measurementCollection));
			}
		}
		
	}
	
	private ProjectManager projectManager;
	private TreeSelectionListenerImpl selectionListener;

	public ProjectTree(ProjectManager projectManager) {
		super(new ProjectTreeModel(projectManager));
		if(projectManager == null) {
			throw new IllegalArgumentException("Argument projectManager must not be null."); //$NON-NLS-1$
		}
		this.projectManager = projectManager;
		this.selectionListener = new TreeSelectionListenerImpl();
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.getSelectionModel().addTreeSelectionListener(this.selectionListener);
	}

}
