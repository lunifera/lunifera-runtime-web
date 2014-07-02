/**
 * Copyright (c) 2011 - 2014, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Florian Pirchner - Initial implementation
 */
package org.lunifera.runtime.web.common.tests.context;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.common.IWebContext;
import org.lunifera.runtime.web.common.IWebContextRegistry;

public class WebContextRegistryTest {

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
