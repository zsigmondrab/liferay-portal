package com.liferay.portal.test.annotations;

import java.util.Arrays;
import java.util.Collection;
 
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
 
@RunWith(value = Parameterized.class)
public class JUnitParametersTest {

	private int number;

	public JUnitParametersTest(int number) {
		 this.number = number;
	}

	/*
	 * The "JUnit" way have to be followed to declare the parameter,
	 * and the parameter has to pass into constructor in order to initialize
	 * the class member as parameter value for testing.
	 * The return type of parameter class is "List []", data type has been
	 * limited to String or primitive value.
	 */
	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { 1 }, { 2 }, { 3 }, { 4 } };
		return Arrays.asList(data);
	}

	@Test
	public void testPush() {
		System.out.println("Parameterized Number is : " + number);
	}
}
