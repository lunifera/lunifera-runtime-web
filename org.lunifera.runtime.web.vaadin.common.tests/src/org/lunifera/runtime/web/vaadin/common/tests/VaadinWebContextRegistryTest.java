/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributor:
 * 		Florian Pirchner - initial implementation
 * 
 *******************************************************************************/
package org.lunifera.runtime.web.vaadin.common.tests;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.common.IWebContext;
import org.lunifera.runtime.web.common.IWebContextRegistry;

public class VaadinWebContextRegistryTest {

	protected boolean called;

	@Before
	public void setup() {

	}

	/**
	 * Test creating the context and its disposal.
	 */
	@Test
	public void test_createAndDisposeContext() {
		IWebContextRegistry registry = ContextRegistryHelper.registry;
		Assert.assertEquals(0, registry.size());
		IWebContext context = registry.createContext("user1", null);
		Assert.assertEquals(1, registry.size());
		IWebContext context2 = registry.createContext("user1", null);
		Assert.assertEquals(2, registry.size());
		context.dispose();
		Assert.assertEquals(1, registry.size());
		context2.dispose();
		Assert.assertEquals(0, registry.size());
	}

	/**
	 * Test async exeucte.
	 */
	@Test
	public void test_getContext() {
		IWebContextRegistry registry = ContextRegistryHelper.registry;
		IWebContext context = registry.createContext("user1", null);
		IWebContext context2 = registry.createContext("user1", null);

		Assert.assertSame(context, registry.getContext(context.getId()));
		Assert.assertNotSame(context, registry.getContext(context2.getId()));
	}
}
