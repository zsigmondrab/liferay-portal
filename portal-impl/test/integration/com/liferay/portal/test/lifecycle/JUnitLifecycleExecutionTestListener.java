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

import com.liferay.portal.kernel.test.ExecutionTestListener;
import com.liferay.portal.kernel.test.TestContext;

/**
 * @author Manuel de la Peña
 */
public class JUnitLifecycleExecutionTestListener
	implements	ExecutionTestListener {

	public void runAfterClass(TestContext testContext) {
		System.out.println("[TestListener] after class");
	}

	public void runAfterTest(TestContext testContext) {
		System.out.println("[TestListener] after test");
	}

	public void runBeforeClass(TestContext testContext) {
		System.out.println("[TestListener] before class");
	}

	public void runBeforeTest(TestContext testContext) {
		System.out.println("[TestListener] before test");
	}

}
