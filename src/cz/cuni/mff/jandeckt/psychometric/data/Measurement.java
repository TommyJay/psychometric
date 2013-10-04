package cz.cuni.mff.jandeckt.psychometric.data;

public interface Measurement extends DataPair {

	boolean isAnswerCorrect();
	
	Object getAnnotation(String key);
}
