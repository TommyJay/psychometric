package cz.cuni.mff.jandeckt.psychometric.math.matrix;

import org.apfloat.Apfloat;

public interface Vector extends Matrix {

	Apfloat getComponent(int index);
	int getSize();
	Vector multiply(Apfloat scalar);
	Vector minus();
	Vector subtract(Vector otherVector);
	Vector add(Vector otherVector);
	
}
