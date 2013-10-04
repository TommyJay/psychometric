package cz.cuni.mff.jandeckt.psychometric.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTree;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultXYDataset;
import cz.cuni.mff.jandeckt.psychometric.control.ProjectManager;
import cz.cuni.mff.jandeckt.psychometric.control.action.ImportDatasetAction;
import cz.cuni.mff.jandeckt.psychometric.data.ProjectTreeModel;
import cz.cuni.mff.jandeckt.psychometric.data.importer.DatasetImporter;
import cz.cuni.mff.jandeckt.psychometric.data.regression.DummyRegressionFunctionGenerator;
import cz.cuni.mff.jandeckt.psychometric.data.regression.LogisticRegressionFunctionGenerator;
import cz.cuni.mff.jandeckt.psychometric.data.regression.LogisticRegressionFunctionGenerator2;
import cz.cuni.mff.jandeckt.psychometric.i18n.DefaultResourceBundles;
import cz.cuni.mff.jandeckt.psychometric.i18n.ResourceBundles;

public class MainFrame extends JFrame {

	private static MainFrame instance;

	private JScrollPane projectBrowserPane;
	private ChartPanel chartPanel;

	private ProjectManager projectManager;
	private DatasetImporter datasetImporter;

	private ResourceBundles resourceBundles;

	private MainFrame(ResourceBundles resourceBundles,
			ProjectManager projectManager, DatasetImporter datasetImporter) {
		if (resourceBundles == null) {
			throw new IllegalArgumentException(
					"Argument resourceBundles must not be null."); //$NON-NLS-1$
		}
		if (projectManager == null) {
			throw new IllegalArgumentException(
					"Argument projectManager must not be null."); //$NON-NLS-1$
		}
		if (datasetImporter == null) {
			throw new IllegalArgumentException(
					"Argument datasetImporter must not be null."); //$NON-NLS-1$
		}
		this.resourceBundles = resourceBundles;
		this.projectManager = projectManager;
		this.datasetImporter = datasetImporter;

		// fullscreen
		this.setExtendedState(MAXIMIZED_BOTH);

		// exit application on close button
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// setting up title
		this.setTitle(this.resourceBundles.getResourceBundle(DefaultResourceBundles.COMMON)
				.getString("application_title")); //$NON-NLS-1$

		// setting up menu bar
		this.configureMenu();

		// setup child components
		BorderLayout borderLayout = new BorderLayout();
		this.setLayout(borderLayout);
		
		this.configureProjectBrowser();
		this.configureChart();
	}

	private void configureChart() {
		ScatterRegressionPlot regressionPlot = new ScatterRegressionPlot(
				new LogisticRegressionFunctionGenerator2(), this.projectManager);
		final JFreeChart chart = new JFreeChart(regressionPlot);
		this.chartPanel = new ChartPanel(chart);
		this.add(this.chartPanel, BorderLayout.CENTER);
	}

	private void configureProjectBrowser() {
		ProjectTree tree = new ProjectTree(this.projectManager);
		tree.setRootVisible(false);
		this.projectBrowserPane = new JScrollPane(tree);
		this.projectBrowserPane.setPreferredSize(new Dimension(300, 600));
		this.add(this.projectBrowserPane, BorderLayout.WEST);
	}

	private void configureMenu() {
		JMenuBar jMenuBar = new JMenuBar();
		JMenu fileMenu = new JMenu(this.resourceBundles.getResourceBundle(DefaultResourceBundles.MENU).getString("file")); //$NON-NLS-1$
		JMenuItem open_project = new JMenuItem(
				this.resourceBundles.getResourceBundle(DefaultResourceBundles.MENU).getString("open_project")); //$NON-NLS-1$
		JMenuItem import_data = new JMenuItem(new ImportDatasetAction(
				this.projectManager, new JFileChooser(), this.resourceBundles,
				this.datasetImporter));
		fileMenu.add(open_project);
		fileMenu.add(new JSeparator());
		fileMenu.add(import_data);
		jMenuBar.add(fileMenu);
		this.setJMenuBar(jMenuBar);
	}

	public static MainFrame getInstance(ResourceBundles resourceBundles,
			ProjectManager projectManager, DatasetImporter datasetImporter) {
		if (instance == null) {
			if (resourceBundles == null)
				throw new IllegalArgumentException(
						"Argument resourceBundles must not be null."); //$NON-NLS-1$
			instance = new MainFrame(resourceBundles, projectManager,
					datasetImporter);
		}
		return instance;
	}

	public static MainFrame getInstance() {
		if (instance == null) {
			throw new IllegalStateException("Main frame not yet initialized."); //$NON-NLS-1$
		}
		return instance;
	}
}
