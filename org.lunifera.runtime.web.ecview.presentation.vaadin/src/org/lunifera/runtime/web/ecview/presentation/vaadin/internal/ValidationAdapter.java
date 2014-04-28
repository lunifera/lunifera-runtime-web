package org.lunifera.runtime.web.ecview.presentation.vaadin.internal;

import org.eclipse.emf.ecp.ecview.common.validation.IStatus;
import org.eclipse.emf.ecp.ecview.common.validation.IValidator;

import com.vaadin.data.validator.AbstractValidator;

public class ValidationAdapter extends AbstractValidator {

	private final IValidator wrappedValidator;

	public ValidationAdapter(IValidator wrappedValidator) {
		super("");
		this.wrappedValidator = wrappedValidator;
	}

	@Override
	protected boolean isValidValue(Object value) {
		IStatus status = wrappedValidator.validate(value);
		return status.isOK();
	}

	@Override
	public Class getType() {
		return wrappedValidator.getType();
	}

}
