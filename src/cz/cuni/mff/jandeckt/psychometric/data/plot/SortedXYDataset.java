package cz.cuni.mff.jandeckt.psychometric.data.plot;

import org.jfree.data.xy.XYDataset;

public interface SortedXYDataset extends XYDataset {

	Number getFirstX();
	Number getFirstY();
	Number getLastX();
	Number getLastY();
	
}
