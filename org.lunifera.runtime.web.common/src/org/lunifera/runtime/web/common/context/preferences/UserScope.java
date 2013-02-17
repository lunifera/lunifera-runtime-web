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
package org.lunifera.runtime.web.common.context.preferences;

import org.lunifera.runtime.common.user.IUserInfo;


/**
 * Object representing the project scope in the Eclipse preferences hierarchy.
 * Can be used as a context for searching for preference values (in the
 * <code>org.eclipse.core.runtime.IPreferencesService</code> APIs) or for
 * determining the correct preference node to set values in the store.
 * <p>
 * User preferences are stored on a per user basis in the user's content area as
 * specified by <code>IUserInfo#getLocation</code>.
 * </p>
 * <p>
 * The path for preferences defined in the user scope hierarchy is as follows:
 * <code>/user/&lt;username&gt;/&lt;qualifier&gt;</code>
 * </p>
 * <p>
 * This class is not intended to be subclassed. This class may be instantiated.
 * </p>
 * 
 * @see IUserInfo#getLocation()
 */
public final class UserScope {

	// implements IScopeContext {

	// /**
	// * String constant (value of <code>"user"</code>) used for the scope name
	// * for this preference scope.
	// */
	//	public static final String SCOPE = "user"; //$NON-NLS-1$
	//
	// private final IPreferencesService preferencesService;
	// private final String datalocation;
	// private final String userId;
	//
	// /**
	// * Create and return a new user scope for the given user info. The given
	// * context must not be <code>null</code>.
	// *
	// * @param preferencesService
	// * @param datalocation
	// * the datalocation
	// * @param userId
	// * the userId
	// * @exception IllegalArgumentException
	// * if the the information is <code>null</code>
	// */
	// public UserScope(IPreferencesService preferencesService,
	// String datalocation, String userId) {
	// super();
	// if (preferencesService == null || datalocation == null
	// || datalocation.equals("") || userId == null)
	// throw new IllegalArgumentException();
	// this.preferencesService = preferencesService;
	// this.datalocation = datalocation;
	// this.userId = userId;
	// }
	//
	// public IEclipsePreferences getNode(String qualifier) {
	// if (qualifier == null)
	// throw new IllegalArgumentException();
	// return (IEclipsePreferences) preferencesService.getRootNode()
	// .node(SCOPE).node(userId).node(qualifier);
	// }
	//
	// public IPath getLocation() {
	// return new Path(datalocation).append(".settings");
	// }
	//
	// public String getName() {
	// return SCOPE;
	// }
	//
	// @Override
	// public int hashCode() {
	// final int prime = 31;
	// int result = 1;
	// result = prime * result
	// + ((datalocation == null) ? 0 : datalocation.hashCode());
	// result = prime * result + ((userId == null) ? 0 : userId.hashCode());
	// return result;
	// }
	//
	// @Override
	// public boolean equals(Object obj) {
	// if (this == obj)
	// return true;
	// if (obj == null)
	// return false;
	// if (getClass() != obj.getClass())
	// return false;
	// UserScope other = (UserScope) obj;
	// if (datalocation == null) {
	// if (other.datalocation != null)
	// return false;
	// } else if (!datalocation.equals(other.datalocation))
	// return false;
	// if (userId == null) {
	// if (other.userId != null)
	// return false;
	// } else if (!userId.equals(other.userId))
	// return false;
	// return true;
	// }

}
