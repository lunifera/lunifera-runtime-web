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

}
