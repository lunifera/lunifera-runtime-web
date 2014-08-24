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

package org.lunifera.runtime.web.common.context;

import org.lunifera.runtime.common.user.IUserInfo;


public abstract class AbstractUserInfo implements IUserInfo {

	private final String userId;

	AbstractUserInfo(String userId) {
		this.userId = userId;
	}

	@Override
	public String getId() {
		return userId;
	}

}
