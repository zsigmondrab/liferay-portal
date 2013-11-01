package com.liferay.portal.test.annotations;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
 
@RunWith(value = Parameterized.class)
public class JUnitParameters2Test {

	@Parameter
	public /* NOT private */ int number;

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { 1 }, { 2 }, { 3 }, { 4 } };
		return Arrays.asList(data);
	}

	@Test
	public void testPush2() {
		System.out.println("Parameterized 2 Number is : " + number);
	}
}