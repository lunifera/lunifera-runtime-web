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
package org.lunifera.runtime.web.common;

import org.osgi.service.prefs.Preferences;

/**
 * Provides information about the current user.
 */
public interface IUserInfo {

	/**
	 * Returns the id of the user.
	 * 
	 * @return
	 */
	String getId();

	// /**
	// * Returns the location where all user specific settings are stored. If no
	// * location is available, empty string will be returned.
	// *
	// * @return
	// */
	// String getLocation();

	/**
	 * Returns the user preferences. If no user preferences are available then
	 * <code>null</code> is returned.
	 * 
	 * @param qualifier
	 *            The qualifier to access the preference node.
	 * 
	 * @return The preferences for the given qualifier or <code>null</code> if
	 *         no proper preferences could be found.
	 */
	Preferences getPreferences(String qualifier);

}
