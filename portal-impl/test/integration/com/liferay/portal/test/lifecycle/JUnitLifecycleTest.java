/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.test.lifecycle;

import com.liferay.portal.kernel.test.ExecutionTestListeners;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Manuel de la Peña
 */

@ExecutionTestListeners(listeners = {JUnitLifecycleExecutionTestListener.class})
@RunWith(JUnitLifecycleTestRunner.class)
public class JUnitLifecycleTest {

	@Before
	public void setUp() {
		System.out.println("[Test Class] before class");
	}

	@BeforeClass
	public static void setUpClass() {
		System.out.println("[Test Class] before method");
	}

	@After
	public void tearDown() {
		System.out.println("[Test Class] after method");
	}

	@AfterClass
	public static void tearDownClass() {
		System.out.println("[Test Class] after class");
	}

	@Test
	public void testJUnitLifecycle() {
		System.out.println("In the middle of the test");
	}

}