package cz.cuni.mff.jandeckt.psychometric.data;

public interface DataPair extends Comparable<DataPair> {
	Number getSignalStrength();
	Number getAnswer();
}
