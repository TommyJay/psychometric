package cz.cuni.mff.jandeckt.psychometric.view;

import java.util.HashMap;
import java.util.Map;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;

import cz.cuni.mff.jandeckt.psychometric.control.ProjectManager;
import cz.cuni.mff.jandeckt.psychometric.control.listener.ProjectListener;
import cz.cuni.mff.jandeckt.psychometric.data.GroupedMeasurementCollection;
import cz.cuni.mff.jandeckt.psychometric.data.ProjectTreeModel;
import cz.cuni.mff.jandeckt.psychometric.data.plot.GroupedMeasurementCollectionXYDataset;
import cz.cuni.mff.jandeckt.psychometric.data.plot.SortedXYDataset;
import cz.cuni.mff.jandeckt.psychometric.data.plot.StructuredXYDataset;
import cz.cuni.mff.jandeckt.psychometric.data.regression.RegressionFunction;
import cz.cuni.mff.jandeckt.psychometric.data.regression.RegressionFunctionGenerator;

public class ScatterRegressionPlot extends XYPlot {

	private class ProjectListenerImpl implements ProjectListener {

		@Override
		public void activeProjectChanged() {
			// nothing to do
		}

		@Override
		public void projectsChanged() {
			// nothing to do
		}

		@Override
		public void activeMeasurementCollectionChanged() {
			GroupedMeasurementCollection activeMeasurementCollection =
					ScatterRegressionPlot.this.projectManager.getActiveMeasurementCollection();
			SortedXYDataset dataset = ScatterRegressionPlot.this.datasetBuffer.get(activeMeasurementCollection);
			if(dataset == null) {
				dataset = new GroupedMeasurementCollectionXYDataset(
						activeMeasurementCollection);
				ScatterRegressionPlot.this.datasetBuffer.put(activeMeasurementCollection, dataset);
			}
			ScatterRegressionPlot.this.setScatterDataset(dataset);
			ScatterRegressionPlot.this.recomputeRegressionFunction();
		}
	}
	
	private Map<GroupedMeasurementCollection, SortedXYDataset> datasetBuffer = new HashMap<GroupedMeasurementCollection, SortedXYDataset>();
	private static final int SCATTER_DATASET_INDEX = 0;
	private static final int REGRESSION_DATASET_INDEX = 1;
	private static final int REGRESSION_FUNCTION_SAMPLE_FREQUENCY = 100;
	private RegressionFunctionGenerator regressionFunctionGenerator;
	private RegressionFunction regressionFunction;
	private XYDotRenderer scatterRenderer;
	private XYSplineRenderer regressionFunctionRenderer;
	private ProjectManager projectManager;
	
	public ScatterRegressionPlot(RegressionFunctionGenerator regressionFunctionGenerator, ProjectManager projectManager) {
		super();
		if(regressionFunctionGenerator == null) {
			throw new IllegalArgumentException("Argument regressionFunctionGenerator must not be null."); //$NON-NLS-1$
		}
		if(projectManager == null) {
			throw new IllegalArgumentException("Argument projectManager must not be null."); //$NON-NLS-1$
		}
		this.regressionFunctionGenerator = regressionFunctionGenerator;
		this.projectManager = projectManager;
		
		this.scatterRenderer = new XYDotRenderer();
		this.scatterRenderer.setDotHeight(5);
		this.scatterRenderer.setDotWidth(5);
		this.setRenderer(SCATTER_DATASET_INDEX, this.scatterRenderer);
		
		this.regressionFunctionRenderer = new XYSplineRenderer();
		this.regressionFunctionRenderer.setSeriesShapesVisible(0, false);
		this.setRenderer(REGRESSION_DATASET_INDEX, this.regressionFunctionRenderer);
		
		this.setDomainAxis(new LogAxis());
		this.setRangeAxis(new NumberAxis());
		
		this.projectManager.addProjectListener(new ProjectListenerImpl());
	} 
	
	public void clearRegressionDataset() {
		this.setDataset(REGRESSION_DATASET_INDEX, null);
	}
	
	public void setScatterDataset(SortedXYDataset dataset) {
		this.setDataset(SCATTER_DATASET_INDEX, dataset);
	}

	public SortedXYDataset getScatterDataset() {
		return (SortedXYDataset) this.getDataset(SCATTER_DATASET_INDEX);
	}
	

	public RegressionFunctionGenerator getRegressionFunctionGenerator() {
		return this.regressionFunctionGenerator;
	}

	public void setRegressionFunctionGenerator(
			RegressionFunctionGenerator regressionFunctionGenerator) {
		if(regressionFunctionGenerator == null) {
			throw new IllegalArgumentException("Argument regressionFunctionGenerator must not be null."); //$NON-NLS-1$
		}
		this.regressionFunctionGenerator = regressionFunctionGenerator;
		this.regressionFunctionGeneratorChanged();
	}

	private void regressionFunctionGeneratorChanged() {
		this.recomputeRegressionFunction();
	}

	private void recomputeRegressionFunction() {
		StructuredXYDataset dataset = (StructuredXYDataset) this.getDataset(SCATTER_DATASET_INDEX);
		if(dataset != null) {
			this.regressionFunction = this.regressionFunctionGenerator.computeRegressionFunction(dataset.getFlattened());
			this.setRegressionFunctionDataset(this.regressionFunction);
		}
	}

	private void setRegressionFunctionDataset(RegressionFunction regressionFunction) {
		// XXX: Set interval dynamically
		SortedXYDataset dataset = (SortedXYDataset) this.getDataset(SCATTER_DATASET_INDEX);
		double sampleIntervalStart = dataset.getFirstX().doubleValue();
		double sampleIntervalEnd = dataset.getLastX().doubleValue();
		int samples = (int) Math.ceil((sampleIntervalEnd - sampleIntervalStart) * REGRESSION_FUNCTION_SAMPLE_FREQUENCY);
		this.setDataset(REGRESSION_DATASET_INDEX, DatasetUtilities.sampleFunction2D(regressionFunction,
				sampleIntervalStart, sampleIntervalEnd, samples, regressionFunction.getName()));
	}
	
	@Override
	public int getDatasetCount() {
		if(this.getDataset(REGRESSION_DATASET_INDEX) == null) {
			return 1;
		} else
			return super.getDatasetCount();
	}
}
