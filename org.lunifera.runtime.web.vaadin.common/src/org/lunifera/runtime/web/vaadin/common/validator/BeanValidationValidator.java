/**
 * Copyright (c) 2011 - 2015, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *         Florian Pirchner - Initial implementation
 */
package org.lunifera.runtime.web.vaadin.common.validator;

import com.vaadin.data.validator.AbstractValidator;

@SuppressWarnings("serial")
public class BeanValidationValidator<T> extends AbstractValidator<T> {

	public BeanValidationValidator(String errorMessage) {
		super(errorMessage);
		
	}

	@Override
	protected boolean isValidValue(T value) {
		return false;
	}

	@Override
	public Class<T> getType() {
		return null;
	}

}
