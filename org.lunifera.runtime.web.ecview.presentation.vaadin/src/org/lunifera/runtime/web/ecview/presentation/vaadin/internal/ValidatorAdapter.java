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

package org.lunifera.runtime.web.ecview.presentation.vaadin.internal;

import org.lunifera.ecview.core.common.validation.IStatus;
import org.lunifera.ecview.core.common.validation.IValidator;

import com.vaadin.data.validator.AbstractValidator;

@SuppressWarnings({ "serial", "rawtypes" })
public class ValidatorAdapter extends AbstractValidator implements IValidator {

	private final IValidator wrappedValidator;
	private IStatus status;

	public ValidatorAdapter(IValidator wrappedValidator) {
		super("");
		this.wrappedValidator = wrappedValidator;
	}

	@Override
	protected boolean isValidValue(Object value) {
		status = wrappedValidator.validateValue(value);
		return status.isOK();
	}

	@Override
	public Class<?> getType() {
		return wrappedValidator.getType();
	}

	@Override
	public void updateParameter(Object model) {
		wrappedValidator.updateParameter(model);
	}

	@Override
	public IStatus validateValue(Object value) {
		return wrappedValidator.validateValue(value);
	}

	public String getErrorMessage() {
		return status != null ? status.getMessage() : null;
	}

}
