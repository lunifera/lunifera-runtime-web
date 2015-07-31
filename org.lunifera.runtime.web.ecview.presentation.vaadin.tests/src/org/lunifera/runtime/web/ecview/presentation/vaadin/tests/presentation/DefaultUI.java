/**
 * Copyright (c) 2012 Committers of lunifera.org.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Florian Pirchner - initial API and implementation
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin.tests.presentation;

import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class DefaultUI extends UI {

	public DefaultUI() {
		try {
			VaadinSession session = new CustomVaadinSession(
					new VaadinServletService(null,
							new CustomDeploymentConfiguration()));
			setSession(session);

			VaadinSession.setCurrent(session);

		} catch (ServiceException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	protected void init(VaadinRequest request) {

	}

	private class CustomVaadinSession extends VaadinSession {

		Lock lock = new ReentrantLock();

		public CustomVaadinSession(VaadinService service) {
			super(service);
		}

		@Override
		public Lock getLockInstance() {
			return lock;
		}

	}

	private static class CustomDeploymentConfiguration implements
			DeploymentConfiguration {

		@Override
		public boolean isProductionMode() {
			return false;
		}

		@Override
		public boolean isXsrfProtectionEnabled() {
			return false;
		}

		@Override
		public boolean isSyncIdCheckEnabled() {
			return false;
		}

		@Override
		public int getResourceCacheTime() {
			return 0;
		}

		@Override
		public int getHeartbeatInterval() {
			return 0;
		}

		@Override
		public boolean isCloseIdleSessions() {
			return false;
		}

		@Override
		public PushMode getPushMode() {
			return PushMode.DISABLED;
		}

		@Override
		public Properties getInitParameters() {
			return new Properties();
		}

		@Override
		public String getApplicationOrSystemProperty(String propertyName,
				String defaultValue) {
			return null;
		}

		@SuppressWarnings("deprecation")
		@Override
		public LegacyProperyToStringMode getLegacyPropertyToStringMode() {
			// TODO Auto-generated method stub
			return LegacyProperyToStringMode.DISABLED;
		}

	}

}
