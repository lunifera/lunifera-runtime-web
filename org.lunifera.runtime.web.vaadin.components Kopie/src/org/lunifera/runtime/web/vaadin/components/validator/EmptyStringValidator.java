/*******************************************************************************
 * Copyright (c) 2011-2013 Committers of lunifera.org
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Florian Pirchner - initial API and implementation
 *******************************************************************************/
package org.lunifera.runtime.web.vaadin.components.validator;

import com.vaadin.data.validator.StringLengthValidator;

@SuppressWarnings("serial")
public class EmptyStringValidator extends StringLengthValidator {

	public EmptyStringValidator(String errorMessage) {
		super(errorMessage, 1, -1, true);
	}

	/**
	 * Checks if empty values are allowed
	 * 
	 * @param allowed
	 *            the value to set
	 * @return
	 */
	public EmptyStringValidator setEmptyAllowed(boolean allowed) {
		if (allowed) {
			setMinLength(0);
		} else {
			setMinLength(1);
		}
		return this;
	}

}
