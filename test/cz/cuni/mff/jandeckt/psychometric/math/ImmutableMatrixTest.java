package cz.cuni.mff.jandeckt.psychometric.math;

import static org.junit.Assert.*;

import org.apfloat.Apfloat;
import org.junit.Test;

import cz.cuni.mff.jandeckt.psychometric.math.matrix.ImmutableMatrix;
import cz.cuni.mff.jandeckt.psychometric.math.matrix.Matrix;
import cz.cuni.mff.jandeckt.psychometric.math.matrix.MatrixFormatException;

public class ImmutableMatrixTest {

	protected static final long PRECISION = 15;

	@Test
	public void testIfGetWidthWorksCorrectly() {
		Apfloat[][] values = new Apfloat[][] {
				{ new Apfloat(2, PRECISION), new Apfloat(1, PRECISION), new Apfloat(3, PRECISION), }, 
				{ new Apfloat(1, PRECISION), new Apfloat(1, PRECISION), new Apfloat(-2, PRECISION), }, 
		};
		assertEquals(2, new ImmutableMatrix(values, PRECISION).getWidth());
	}

	@Test
	public void testIfGetHeightWorksCorrectly() {
		Apfloat[][] values = new Apfloat[][] {
				{ new Apfloat(2, PRECISION), new Apfloat(1, PRECISION), new Apfloat(3, PRECISION), }, 
				{ new Apfloat(1, PRECISION), new Apfloat(1, PRECISION), new Apfloat(-2, PRECISION), }, 
		};
		assertEquals(3, new ImmutableMatrix(values, PRECISION).getHeight());
	}

	@Test
	public void testIfGetComponentWorksCorrectly() {
		Apfloat[][] values = new Apfloat[][] {
				{ new Apfloat(2, PRECISION), new Apfloat(1, PRECISION), new Apfloat(3, PRECISION), }, 
				{ new Apfloat(1, PRECISION), new Apfloat(1, PRECISION), new Apfloat(-2, PRECISION), }, 
		};
		assertEquals(0, new ImmutableMatrix(values, PRECISION).getComponent(1, 2).compareTo(new Apfloat(-2))); 
	}

	@Test(expected=MatrixFormatException.class)
	public void testIfMultiplicationFailsOnWrongBounds() {
		Apfloat[][] values1 = new Apfloat[][] {
				{ new Apfloat(2, PRECISION), new Apfloat(1, PRECISION), new Apfloat(3, PRECISION), }, 
		};
		Apfloat[][] values2 = new Apfloat[][] {
				{ new Apfloat(1, PRECISION), new Apfloat(2, PRECISION), new Apfloat(2, PRECISION) }, 
				{ new Apfloat(3, PRECISION), new Apfloat(4, PRECISION), new Apfloat(2, PRECISION) }, 
		};
		ImmutableMatrix matrix1 = new ImmutableMatrix(values1, PRECISION);
		ImmutableMatrix matrix2 = new ImmutableMatrix(values2, PRECISION);
		
		// exception raised here
		matrix1.multiply(matrix2);
	}
	
	@Test
	public void testIfMultiplicationMultipliesCorrectly() {
		Apfloat[][] values1 = new Apfloat[][] {
				{ new Apfloat(2, PRECISION), new Apfloat(1, PRECISION), new Apfloat(3, PRECISION), }, 
				{ new Apfloat(1, PRECISION), new Apfloat(1, PRECISION), new Apfloat(-2, PRECISION), }, 
		};
		Apfloat[][] values2 = new Apfloat[][] {
				{ new Apfloat(1, PRECISION), new Apfloat(2, PRECISION) }, 
				{ new Apfloat(3, PRECISION), new Apfloat(4, PRECISION) }, 
		};
		ImmutableMatrix matrix1 = new ImmutableMatrix(values1, PRECISION);
		ImmutableMatrix matrix2 = new ImmutableMatrix(values2, PRECISION);

		Apfloat[][] result = new Apfloat[][] {
				{ new Apfloat(4, PRECISION), new Apfloat(3, PRECISION), new Apfloat(-1, PRECISION), }, 
				{ new Apfloat(10, PRECISION), new Apfloat(7, PRECISION), new Apfloat(1, PRECISION), }, 
		};
		ImmutableMatrix resultMatrix = new ImmutableMatrix(result, PRECISION);
		Matrix matrix = matrix1.multiply(matrix2);

		int expectedWidth = resultMatrix.getWidth();
		int actualWidth = matrix.getWidth();
		assertEquals(String.format("Multiplication incorrect: Expected matrix width is %s but was %s.", //$NON-NLS-1$
				expectedWidth, actualWidth), expectedWidth, actualWidth);
		int expectedHeight = resultMatrix.getHeight();
		int actualHeight = matrix.getHeight();
		assertEquals(String.format("Multiplication incorrect: Expected matrix height is %s but was %s.", //$NON-NLS-1$
				expectedHeight, actualHeight), expectedHeight, actualHeight);
		for (int i = 0; i < resultMatrix.getWidth(); i++) {
			for (int j = 0; j < resultMatrix.getHeight(); j++) {
				if(resultMatrix.getComponent(i, j).compareTo(matrix.getComponent(i, j)) != 0) {
					fail(String.format("Multiplication incorrect: Expected value at position (%s, %s) is %s but was %s.", i, j, resultMatrix.getComponent(i, j), matrix.getComponent(i, j))); //$NON-NLS-1$
				}
			}
		}
	}
	
	@Test
	public void testIfAdditionAddsCorrectly() {
		Apfloat[][] values1 = new Apfloat[][] {
				{ new Apfloat(2, PRECISION), new Apfloat(1, PRECISION), new Apfloat(3, PRECISION), }, 
				{ new Apfloat(1, PRECISION), new Apfloat(1, PRECISION), new Apfloat(-2, PRECISION), }, 
		};
		Apfloat[][] values2 = new Apfloat[][] {
				{ new Apfloat(1, PRECISION), new Apfloat(2, PRECISION), new Apfloat(2, PRECISION) }, 
				{ new Apfloat(3, PRECISION), new Apfloat(4, PRECISION), new Apfloat(2, PRECISION) }, 
		};
		ImmutableMatrix matrix1 = new ImmutableMatrix(values1, PRECISION);
		ImmutableMatrix matrix2 = new ImmutableMatrix(values2, PRECISION);

		Apfloat[][] result = new Apfloat[][] {
				{ new Apfloat(3, PRECISION), new Apfloat(3, PRECISION), new Apfloat(5, PRECISION), }, 
				{ new Apfloat(4, PRECISION), new Apfloat(5, PRECISION), new Apfloat(0, PRECISION), }, 
		};
		ImmutableMatrix resultMatrix = new ImmutableMatrix(result, PRECISION);
		Matrix matrix = matrix1.add(matrix2);
		
		int expectedWidth = resultMatrix.getWidth();
		int actualWidth = matrix.getWidth();
		assertEquals(String.format("Addition incorrect: Expected matrix width is %s but was %s.", //$NON-NLS-1$
				expectedWidth, actualWidth), expectedWidth, actualWidth);
		int expectedHeight = resultMatrix.getHeight();
		int actualHeight = matrix.getHeight();
		assertEquals(String.format("Addition incorrect: Expected matrix height is %s but was %s.", //$NON-NLS-1$
				expectedHeight, actualHeight), expectedHeight, actualHeight);
		for (int i = 0; i < resultMatrix.getWidth(); i++) {
			for (int j = 0; j < resultMatrix.getHeight(); j++) {
				if(resultMatrix.getComponent(i, j).compareTo(matrix.getComponent(i, j)) != 0) {
					fail(String.format("Addition incorrect: Expected value at position (%s, %s) is %s but was %s.", i, j, resultMatrix.getComponent(i, j), matrix.getComponent(i, j))); //$NON-NLS-1$
				}
			}
		}
	}
	
	@Test
	public void testIfTransformTransformsCorrectly() {
		Apfloat[][] values1 = new Apfloat[][] {
				{ new Apfloat(2, PRECISION), new Apfloat(1, PRECISION), new Apfloat(3, PRECISION) }, 
				{ new Apfloat(1, PRECISION), new Apfloat(1, PRECISION), new Apfloat(-2, PRECISION) }, 
		};
		ImmutableMatrix matrix1 = new ImmutableMatrix(values1, PRECISION);
		
		Apfloat[][] result = new Apfloat[][] {
				{ new Apfloat(2, PRECISION), new Apfloat(1, PRECISION) }, 
				{ new Apfloat(1, PRECISION), new Apfloat(1, PRECISION) }, 
				{ new Apfloat(3, PRECISION), new Apfloat(-2, PRECISION) }, 
		};
		ImmutableMatrix resultMatrix = new ImmutableMatrix(result, PRECISION);
		Matrix matrix = matrix1.transform();
		
		int expectedWidth = resultMatrix.getWidth();
		int actualWidth = matrix.getWidth();
		assertEquals(String.format("Transformation incorrect: Expected matrix width is %s but was %s.", //$NON-NLS-1$
				expectedWidth, actualWidth), expectedWidth, actualWidth);
		int expectedHeight = resultMatrix.getHeight();
		int actualHeight = matrix.getHeight();
		assertEquals(String.format("Transformation incorrect: Expected matrix height is %s but was %s.", //$NON-NLS-1$
				expectedHeight, actualHeight), expectedHeight, actualHeight);
		for (int i = 0; i < resultMatrix.getWidth(); i++) {
			for (int j = 0; j < resultMatrix.getHeight(); j++) {
				if(resultMatrix.getComponent(i, j).compareTo(matrix.getComponent(i, j)) != 0) {
					fail(String.format("Transformation incorrect: Expected value at position (%s, %s) is %s but was %s.", i, j, resultMatrix.getComponent(i, j), matrix.getComponent(i, j))); //$NON-NLS-1$
				}
			}
		}
	}

}
