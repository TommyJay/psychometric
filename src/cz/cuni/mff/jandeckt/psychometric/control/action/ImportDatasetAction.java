package cz.cuni.mff.jandeckt.psychometric.control.action;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.jfree.data.xy.XYDataset;

import cz.cuni.mff.jandeckt.psychometric.control.ProjectManager;
import cz.cuni.mff.jandeckt.psychometric.data.DefaultProject;
import cz.cuni.mff.jandeckt.psychometric.data.GroupedMeasurementCollection;
import cz.cuni.mff.jandeckt.psychometric.data.importer.DatasetImporter;
import cz.cuni.mff.jandeckt.psychometric.data.importer.InvalidInputException;
import cz.cuni.mff.jandeckt.psychometric.i18n.DefaultResourceBundles;
import cz.cuni.mff.jandeckt.psychometric.i18n.ResourceBundles;
import cz.cuni.mff.jandeckt.psychometric.view.MainFrame;

public class ImportDatasetAction extends AbstractAction {

	private ProjectManager projectManager;
	private JFileChooser fileChooser;
	private DatasetImporter datasetImporter;
	private ResourceBundles resourceBundles;

	public ImportDatasetAction(ProjectManager projectManager, JFileChooser fileChooser,
			ResourceBundles resourceBundles, DatasetImporter datasetImporter) {
		super(resourceBundles.getResourceBundle(DefaultResourceBundles.ACTIONS)
				.getString("import_data")); //$NON-NLS-1$
		this.projectManager = projectManager;
		this.fileChooser = fileChooser;
		this.datasetImporter = datasetImporter;
		this.resourceBundles = resourceBundles;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int returnVal = this.fileChooser.showOpenDialog(MainFrame.getInstance());
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			// TODO: create and add project from dataset
			File selectedFile = this.fileChooser.getSelectedFile();
			try {
				List<GroupedMeasurementCollection> dataset = this.datasetImporter.importDataset(selectedFile);
				this.projectManager.openProject(new DefaultProject(selectedFile.getName(), dataset));
			} catch (FileNotFoundException e1) {
				JOptionPane.showMessageDialog(MainFrame.getInstance(), 
						this.resourceBundles.getResourceBundle(DefaultResourceBundles.ERRORS).getString("file_not_found"), //$NON-NLS-1$
						this.resourceBundles.getResourceBundle(DefaultResourceBundles.ERRORS).getString("file_not_found_title"), //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
			} catch (InvalidInputException e1) {
				JOptionPane.showMessageDialog(MainFrame.getInstance(), 
						this.resourceBundles.getResourceBundle(DefaultResourceBundles.ERRORS).getString("file_format_error"), //$NON-NLS-1$
						this.resourceBundles.getResourceBundle(DefaultResourceBundles.ERRORS).getString("file_format_error_title"), //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(MainFrame.getInstance(), 
						this.resourceBundles.getResourceBundle(DefaultResourceBundles.ERRORS).getString("file_read_error"), //$NON-NLS-1$
						this.resourceBundles.getResourceBundle(DefaultResourceBundles.ERRORS).getString("file_read_error_title"), //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
