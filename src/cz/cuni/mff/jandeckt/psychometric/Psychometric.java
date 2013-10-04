/**
 * 
 */
package cz.cuni.mff.jandeckt.psychometric;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import cz.cuni.mff.jandeckt.psychometric.control.DefaultProjectManager;
import cz.cuni.mff.jandeckt.psychometric.control.ProjectManager;
import cz.cuni.mff.jandeckt.psychometric.data.importer.CSVDatasetImporter;
import cz.cuni.mff.jandeckt.psychometric.data.importer.DatasetImporter;
import cz.cuni.mff.jandeckt.psychometric.i18n.DefaultResourceBundles;
import cz.cuni.mff.jandeckt.psychometric.i18n.ResourceBundles;
import cz.cuni.mff.jandeckt.psychometric.view.MainFrame;

/**
 * @author tj
 *
 */
public class Psychometric {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}
		
		ResourceBundles resourceBundles = new DefaultResourceBundles();
		ProjectManager projectManager = new DefaultProjectManager();
		DatasetImporter datasetImporter = new CSVDatasetImporter();
		MainFrame mainFrame = MainFrame.getInstance(resourceBundles, projectManager, datasetImporter);
		
		mainFrame.setVisible(true);
	}

}
