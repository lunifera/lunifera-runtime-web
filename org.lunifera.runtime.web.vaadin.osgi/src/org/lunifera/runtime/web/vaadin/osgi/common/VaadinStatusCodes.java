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
package org.lunifera.runtime.web.vaadin.osgi.common;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.lunifera.runtime.web.vaadin.osgi.Activator;

/**
 * A class that contains all status codes of that bundle and helper methods to
 * create the status objects.
 */
public class VaadinStatusCodes {

	public static final int OK = IStatus.OK;

	/**
	 * Indicates that a problem occurred setting the http service. See exception
	 * in status.
	 */
	public static final int SETTING_HTTP_SERVICE = 1000;

	/**
	 * Indicates that a problem occurred starting the http service tracker.
	 */
	public static final int HTTP_SERVICE_TRACKER = 1001;

	/**
	 * Represents the OK status.
	 */
	public static final IStatus OK_STATUS = Status.OK_STATUS;

	/**
	 * Creates a status that indicates that the given alias is already used.
	 * 
	 * @param alias
	 * @return
	 */
	public static IStatus createOK() {
		return Status.OK_STATUS;
	}

	/**
	 * Creates a status that indicates that a problem occurred setting the http
	 * service.
	 * 
	 * @param e
	 *            the thrown exception or <code>null</code>
	 * @return
	 */
	public static IStatus createSettingHttpService(Throwable e) {
		return new Status(IStatus.ERROR, Activator.BUNDLE_NAME,
				SETTING_HTTP_SERVICE, "Alias already used!", e);
	}

	/**
	 * Creates a status that a problem with the http service tracker occured.
	 * 
	 * @param e
	 *            the thrown exception or <code>null</code>
	 * @return
	 */
	public static IStatus createHttpServiceTracker(Throwable e) {
		return new Status(IStatus.ERROR, Activator.BUNDLE_NAME,
				HTTP_SERVICE_TRACKER, "Alias already used!", e);
	}
}
