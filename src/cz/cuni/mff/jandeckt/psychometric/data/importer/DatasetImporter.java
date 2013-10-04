package cz.cuni.mff.jandeckt.psychometric.data.importer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.jfree.data.xy.XYDataset;

import cz.cuni.mff.jandeckt.psychometric.data.GroupedMeasurementCollection;

public interface DatasetImporter {
	
	List<GroupedMeasurementCollection> importDataset(File file) throws FileNotFoundException, IOException;
	
}
