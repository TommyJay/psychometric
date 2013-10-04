package cz.cuni.mff.jandeckt.psychometric.math.function;

import org.apfloat.Apfloat;

import cz.cuni.mff.jandeckt.psychometric.math.matrix.Vector;

public interface MultiVariableFunction {

	Apfloat valueAt(Vector vector);
	
}
