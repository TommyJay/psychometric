package cz.cuni.mff.jandeckt.psychometric.data;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.sun.org.apache.bcel.internal.generic.NEW;

import cz.cuni.mff.jandeckt.psychometric.control.ProjectManager;
import cz.cuni.mff.jandeckt.psychometric.control.listener.ProjectListener;

public class ProjectTreeModel implements TreeModel {
	
	private class ProjectListenerImpl implements ProjectListener {

		@Override
		public void activeProjectChanged() {
			// TODO change selection
		}

		@Override
		public void projectsChanged() {
			for (TreeModelListener listener : ProjectTreeModel.this.listeners) {
				listener.treeStructureChanged(new TreeModelEvent(this,
						new ProjectManager[]{ProjectTreeModel.this.projectManager}));
			}
		}

		@Override
		public void activeMeasurementCollectionChanged() {
			// TODO change selection
			
		}
	}
	
	private List<TreeModelListener> listeners = new ArrayList<TreeModelListener>();
	private ProjectManager projectManager;
	private ProjectListener projectListener = new ProjectListenerImpl();

	public ProjectTreeModel(ProjectManager projectManager) {
		this.projectManager = projectManager;
		this.projectManager.addProjectListener(this.projectListener);
	}
	
	@Override
	public void addTreeModelListener(TreeModelListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public Object getChild(Object parent, int index) {
		if(Project.class.isAssignableFrom(parent.getClass())) {
			return ((Project) parent).getMeasurementGroups().get(index);
		} else if(ProjectManager.class.isAssignableFrom(parent.getClass())) {
			return ((ProjectManager) parent).getOpenProjects().get(index);
		} else {
			throw new IllegalStateException("Invalid object class in tree."); //$NON-NLS-1$
		}
	}

	@Override
	public int getChildCount(Object object) {
		if(Project.class.isAssignableFrom(object.getClass())) {
			return ((Project) object).getMeasurementGroups().size();
		} else if(GroupedMeasurementCollection.class.isAssignableFrom(object.getClass())) {
			return 0;
		} else if(ProjectManager.class.isAssignableFrom(object.getClass())) {
			return ((ProjectManager) object).getOpenProjects().size();
		} else {
			throw new IllegalStateException("Invalid object class in tree."); //$NON-NLS-1$
		}
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		if(Project.class.isAssignableFrom(parent.getClass())) {
			return ((Project) parent).getMeasurementGroups().indexOf(child);
		} else if(ProjectManager.class.isAssignableFrom(parent.getClass())) {
			return ((ProjectManager) parent).getOpenProjects().indexOf(child);
		} else {
			throw new IllegalStateException("Invalid object class in tree."); //$NON-NLS-1$
		}
	}

	@Override
	public Object getRoot() {
		return this.projectManager;
	}

	@Override
	public boolean isLeaf(Object object) {
		return !Project.class.isAssignableFrom(object.getClass()) 
				&& !ProjectManager.class.isAssignableFrom(object.getClass());
	}

	@Override
	public void removeTreeModelListener(TreeModelListener listener) {
		this.listeners.remove(listener);
	}

	@Override
	public void valueForPathChanged(TreePath arg0, Object arg1) {
		// TODO Auto-generated method stub
	}

}
