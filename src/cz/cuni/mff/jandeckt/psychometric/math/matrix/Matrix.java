package cz.cuni.mff.jandeckt.psychometric.math.matrix;


import java.math.MathContext;

import org.apfloat.Apfloat;

public interface Matrix {
	
	int getWidth();
	int getHeight();
	Apfloat getComponent(int widthIndex, int heightIndex);
	Matrix add(Matrix otherMatrix);
	Matrix subtract(Matrix otherMatrix);
	Matrix multiply(Matrix otherMatrix);
	Vector multiply(Vector vector);
	Matrix multiply(Apfloat scalar);
	Matrix divide(Apfloat scalar);
	Matrix transform();
	Matrix minus();
	Apfloat toScalar();
	long getPrecision();
	Apfloat[][] toArray();
	
}
