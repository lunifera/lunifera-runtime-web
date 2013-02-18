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

import java.io.IOException;
import java.net.MalformedURLException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.common.dispose.IDisposable;
import org.lunifera.runtime.common.user.IUserInfo;
import org.lunifera.runtime.web.common.IWebContext;
import org.lunifera.runtime.web.common.IWebContextRegistry;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class VaadinWebContextTest {

	protected boolean called;

	@Before
	public void setup() {

		// Register the user location
		//
		try {
			Activator.registerUserLocation();
		} catch (MalformedURLException e) {
			Assert.fail(e.toString());
		} catch (IOException e) {
			Assert.fail(e.toString());
		}

	}

	/**
	 * Test creating the context and its disposal.
	 */
	@Test
	public void test_createAndDispose() {
		IWebContextRegistry registry = ContextRegistryHelper.registry;
		IWebContext context = registry.createContext("user1", null);

		Assert.assertFalse(context.isDisposed());
		context.dispose();
		Assert.assertTrue(context.isDisposed());
	}

	/**
	 * Test async exeucte.
	 */
	@Test
	public void test_asyncExec() {
		IWebContextRegistry registry = ContextRegistryHelper.registry;
		IWebContext context = registry.createContext("user1", null);

		try {
			context.asyncExec(null);
			Assert.fail();
		} catch (UnsupportedOperationException e) {
		}

		context.dispose();
	}

	/**
	 * Test sync exeucte.
	 */
	@Test
	public void test_syncExec() {
		IWebContextRegistry registry = ContextRegistryHelper.registry;
		IWebContext context = registry.createContext("user1", null);

		try {
			context.syncExec(null);
			Assert.fail();
		} catch (UnsupportedOperationException e) {
		}

		context.dispose();
	}

	/**
	 * Tests the dispose listener.
	 */
	@Test
	public void test_disposeListener() {
		IWebContextRegistry registry = ContextRegistryHelper.registry;
		IWebContext context = registry.createContext("user1", null);

		called = false;
		context.addDisposeListener(new IDisposable.Listener() {
			@Override
			public void notifyDisposed(IDisposable notifier) {
				called = true;
			}
		});
		context.dispose();

		Assert.assertTrue(called);
	}

	/**
	 * Tests accessing the user info.
	 */
	@Test
	public void test_userInfo() {
		IWebContextRegistry registry = ContextRegistryHelper.registry;
		IWebContext context = registry.createContext("user1", null);
		IUserInfo info = context.getUserInfo();

		Assert.assertEquals("user1", info.getId());
		// Assert.assertNotNull(info.getLocation());

		Preferences preferences = info.getPreferences("tests");
		Assert.assertNotNull(preferences);

		context.dispose();
	}

	/**
	 * Tests the user preferences.
	 */
	@Test
	public void test_userPreferences() {
		IWebContextRegistry registry = ContextRegistryHelper.registry;
		IWebContext context1_1 = registry.createContext("user1", null);
		IUserInfo info1_1 = context1_1.getUserInfo();

		Assert.assertEquals("user1", info1_1.getId());
		// Assert.assertNotNull(info1_1.getLocation());

		Preferences preferences1_1 = info1_1.getPreferences("tests");
		preferences1_1.put("test1", "user_1__test1");
		try {
			preferences1_1.flush();
		} catch (BackingStoreException e) {
			Assert.fail();
		}

		// user 2
		//
		IWebContext context2_1 = registry.createContext("user2", null);
		IUserInfo info2_1 = context2_1.getUserInfo();
		Preferences preferences2_1 = info2_1.getPreferences("tests");
		Assert.assertNull(preferences2_1.get("test1", null));

		// user 1
		//
		IWebContext context1_2 = registry.createContext("user1", null);
		IUserInfo info1_2 = context1_2.getUserInfo();
		Preferences preferences1_2 = info1_2.getPreferences("tests");
		Assert.assertEquals("user_1__test1", preferences1_2.get("test1", null));

		context1_1.dispose();
		context2_1.dispose();
		context1_2.dispose();
	}

}
