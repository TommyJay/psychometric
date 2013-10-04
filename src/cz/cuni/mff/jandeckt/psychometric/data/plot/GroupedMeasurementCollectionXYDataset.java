package cz.cuni.mff.jandeckt.psychometric.data.plot;

import java.util.List;

import org.jfree.data.DomainOrder;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.AbstractXYDataset;

import cz.cuni.mff.jandeckt.psychometric.data.GroupedMeasurementCollection;
import cz.cuni.mff.jandeckt.psychometric.data.Measurement;

public class GroupedMeasurementCollectionXYDataset extends AbstractXYDataset implements StructuredXYDataset {
	
	private class FlattenedXYDataset extends AbstractXYDataset implements SortedXYDataset {

		private List<Measurement> measurements;

		public FlattenedXYDataset(List<Measurement> measurements) {
			this.measurements = measurements;
		}

		@Override
		public int getItemCount(int arg0) {
			return this.measurements.size();
		}

		@Override
		public Number getX(int arg0, int arg1) {
			return this.measurements.get(arg1).getSignalStrength();
		}

		@Override
		public Number getY(int arg0, int arg1) {
			return this.measurements.get(arg1).getAnswer();
		}

		@Override
		public Number getFirstX() {
			return this.measurements.get(0).getSignalStrength();
		}

		@Override
		public Number getFirstY() {
			return this.measurements.get(0).getAnswer();
		}

		@Override
		public Number getLastX() {
			return this.measurements.get(this.measurements.size()-1).getSignalStrength();
		}

		@Override
		public Number getLastY() {
			return this.measurements.get(this.measurements.size()-1).getAnswer();
		}

		@Override
		public int getSeriesCount() {
			return 1;
		}

		@Override
		public Comparable getSeriesKey(int arg0) {
			return "---"; //$NON-NLS-1$
		}
		
	}
	
	private GroupedMeasurementCollection measurementGroup;

	public GroupedMeasurementCollectionXYDataset(GroupedMeasurementCollection group) {
		super();
		if(group == null) {
			throw new IllegalArgumentException("Argument group must not be null."); //$NON-NLS-1$
		}
		this.measurementGroup = group;
	}

	@Override
	public int getItemCount(int arg0) {
		// only one series, therefore disregarding series index
		return this.measurementGroup.getGroupCount();
	}

	@Override
	public Number getX(int arg0, int arg1) {
		// only one series, therefore disregarding series index
		return this.measurementGroup.getGroupedSignalStrength(arg1);
	}

	@Override
	public Number getY(int arg0, int arg1) {
		// only one series, therefore disregarding series index
		return this.measurementGroup.getGroupedCorrectness(arg1);
	}

	@Override
	public int getSeriesCount() {
		return 1;
	}

	@Override
	public Comparable getSeriesKey(int arg0) {
		// only one series, therefore disregarding series index
		return this.measurementGroup.getName();
	}

	@Override
	public Number getFirstX() {
		return this.measurementGroup.getGroupedSignalStrength(0);
	}

	@Override
	public Number getFirstY() {
		return this.measurementGroup.getGroupedCorrectness(0);
	}

	@Override
	public synchronized Number getLastX() {
		return this.measurementGroup.getGroupedSignalStrength(this.measurementGroup.getGroupCount()-1);
	}

	@Override
	public synchronized Number getLastY() {
		return this.measurementGroup.getGroupedCorrectness(this.measurementGroup.getGroupCount()-1);
	}

	@Override
	public SortedXYDataset getFlattened() {
		return new FlattenedXYDataset(this.measurementGroup.getMeasurements());
	}

}
