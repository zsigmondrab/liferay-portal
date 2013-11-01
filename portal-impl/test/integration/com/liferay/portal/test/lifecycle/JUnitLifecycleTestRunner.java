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

import com.liferay.portal.kernel.test.AbstractIntegrationJUnitTestRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * @author Manuel de la Peña
 */
public class JUnitLifecycleTestRunner
	extends AbstractIntegrationJUnitTestRunner {

	/**
	 * Creates a BlockJUnit4ClassRunner to run {@code clazz}
	 *
	 * @throws org.junit.runners.model.InitializationError
	 *          if the test class is malformed.
	 */
	public JUnitLifecycleTestRunner(Class<?> clazz) throws InitializationError {
		super(clazz);

		System.out.println("[TestRunner] JUnitLifecycleTestRunner constructor");
	}

	public void initApplicationContext() {
		System.out.println("[TestRunner] creating application context");
	}

	@Override
	protected Statement withAfters(
		FrameworkMethod frameworkMethod, Object instance, Statement statement) {

		System.out.println("[TestRunner] with afters");

		return super.withAfters(frameworkMethod, instance, statement);
	}

	@Override
	protected Statement withAfterClasses(Statement statement) {
		System.out.println("[TestRunner] with after class");

		return super.withAfterClasses(statement);
	}

	@Override
	protected Statement withBeforeClasses(Statement statement) {
		System.out.println("[TestRunner] with before class");

		return super.withBeforeClasses(statement);
	}

	@Override
	protected Statement withBefores(
		FrameworkMethod frameworkMethod, Object instance, Statement statement) {

		System.out.println("[TestRunner] with befores");

		return super.withBefores(frameworkMethod, instance, statement);
	}

}
