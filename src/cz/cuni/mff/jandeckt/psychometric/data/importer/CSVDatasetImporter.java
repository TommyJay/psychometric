package cz.cuni.mff.jandeckt.psychometric.data.importer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.xml.internal.bind.v2.model.annotation.AnnotationSource;

import au.com.bytecode.opencsv.CSVReader;

import cz.cuni.mff.jandeckt.psychometric.data.DefaultGroupedMeasurementCollection;
import cz.cuni.mff.jandeckt.psychometric.data.DefaultMeasurement;
import cz.cuni.mff.jandeckt.psychometric.data.GroupedMeasurementCollection;
import cz.cuni.mff.jandeckt.psychometric.data.Measurement;

public class CSVDatasetImporter implements DatasetImporter {

	private static final String DEFAULT_ID_COLUMN_NAME = "id"; //$NON-NLS-1$
	private static final String DEFAULT_SIGNAL_COLUMN_NAME = "signal"; //$NON-NLS-1$
	private static final String DEFAULT_ANSWER_COLUMN_NAME = "answer"; //$NON-NLS-1$
	private static final String DEFAULT_CORRECT_COLUMN_NAME = "correct"; //$NON-NLS-1$
	
	@Override
	public List<GroupedMeasurementCollection> importDataset(File file) throws FileNotFoundException, IOException {
	    CSVReader reader = new CSVReader(new FileReader(file));

	    int idIndex = -1;
	    int signalIndex = -1;
	    int answerIndex = -1;
	    int correctIndex = -1;
	    
	    String [] nextLine;
	    String [] headLine;
	    if((headLine = reader.readNext()) != null) {
	    	for (int i = 0; i < headLine.length; i++) {
				if(headLine[i].equals(DEFAULT_ID_COLUMN_NAME)) {
					idIndex = i;
				} else if(headLine[i].equals(DEFAULT_SIGNAL_COLUMN_NAME)) {
					signalIndex = i;
				} else if(headLine[i].equals(DEFAULT_ANSWER_COLUMN_NAME)) {
					answerIndex = i;
				} else if(headLine[i].equals(DEFAULT_CORRECT_COLUMN_NAME)) {
					correctIndex = i;
				}
			}
	    }
	    
	    if(idIndex < 0 || signalIndex < 0 || answerIndex < 0 || correctIndex < 0) {
	    	throw new InvalidInputException();
	    }
	    
	    HashMap<String,DefaultGroupedMeasurementCollection> map = new HashMap<String, DefaultGroupedMeasurementCollection>();
	    while ((nextLine = reader.readNext()) != null) {
	    	String id = nextLine[idIndex];
	    	if(id == null || id.isEmpty()) {
		    	throw new InvalidInputException();
	    	}
	    	DefaultGroupedMeasurementCollection groupedMeasurementCollection = map.get(id);
	    	if(groupedMeasurementCollection == null) {
	    		groupedMeasurementCollection = new DefaultGroupedMeasurementCollection(id);
	    		map.put(id, groupedMeasurementCollection);
	    	}

	    	Map<String, Object> annotations = new HashMap<String, Object>();
	    	groupedMeasurementCollection.addMeasurement(new DefaultMeasurement(
	    			new Double(nextLine[signalIndex]), new Double(nextLine[answerIndex]),
	    			new Boolean(nextLine[correctIndex]), annotations));
			for (int i = 0; i < nextLine.length; i++) {
				if(i != idIndex && i != signalIndex && i != answerIndex && i != correctIndex) {
					annotations.put(headLine[i], nextLine[i]);
				}
			}
	    }
	    Collection<DefaultGroupedMeasurementCollection> values = map.values();
	    for (DefaultGroupedMeasurementCollection measurementCollection : values) {
			measurementCollection.regroupMeasurements();
		}
		return new ArrayList<GroupedMeasurementCollection>(values);
	}

}
