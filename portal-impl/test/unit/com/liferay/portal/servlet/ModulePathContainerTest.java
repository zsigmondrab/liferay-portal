/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.portal.servlet;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.tools.ToolDependencies;
import com.liferay.portal.util.PortletKeys;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Carlos Sierra Andrés
 * @author Raymond Augé
 */
public class ModulePathContainerTest {

	@BeforeClass
	public static void setUpClass() throws Exception {
		ToolDependencies.wireCaches();
	}

	@Test
	public void testModulePathWithContextPath() {
		String modulePath = _CONTEXT + _PATH;
		Assert.assertEquals(
			PortletKeys.PORTAL, ComboServlet.getModulePortletId(modulePath));
		Assert.assertEquals(
			_PATH, ComboServlet.getResourcePath(modulePath, _CONTEXT));
	}

	@Test
	public void testModulePathWithNoContextPath() {
		Assert.assertEquals(
			PortletKeys.PORTAL, ComboServlet.getModulePortletId(_PATH));
		Assert.assertEquals(
			_PATH, ComboServlet.getResourcePath(_PATH, StringPool.BLANK));
	}

	@Test
	public void testModulePathWithPortletId() {
		String modulePath = PortletKeys.PORTAL + ":" + _PATH;

		Assert.assertEquals(
			PortletKeys.PORTAL, ComboServlet.getModulePortletId(modulePath));
		Assert.assertEquals(
			_PATH, ComboServlet.getResourcePath(modulePath, StringPool.BLANK));
	}

	@Test
	public void testModulePathWithPortletIdAndContext() {
		String modulePath = PortletKeys.PORTAL + ":" + _CONTEXT + _PATH;

		Assert.assertEquals(
			PortletKeys.PORTAL, ComboServlet.getModulePortletId(modulePath));
		Assert.assertEquals(
			_PATH, ComboServlet.getResourcePath(modulePath, _CONTEXT));
	}

	@Test
	public void testModulePathWithPortletIdAndNoResourcePath() {
		String modulePath = PortletKeys.PORTAL + ":";

		Assert.assertEquals(
			PortletKeys.PORTAL, ComboServlet.getModulePortletId(modulePath));
		Assert.assertEquals(
			StringPool.BLANK,
			ComboServlet.getResourcePath(modulePath, StringPool.BLANK));
	}

	@Test
	public void testModulePathWithPortletIdAndNoResourcePathButContext() {
		String modulePath = PortletKeys.PORTAL + ":" + _CONTEXT;

		Assert.assertEquals(
			PortletKeys.PORTAL, ComboServlet.getModulePortletId(modulePath));
		Assert.assertEquals(
			StringPool.BLANK,
			ComboServlet.getResourcePath(modulePath, _CONTEXT));
	}

	private static final String _CONTEXT = "/" + StringUtil.randomString();

	private static final String _PATH = "/js/javascript.js";

}