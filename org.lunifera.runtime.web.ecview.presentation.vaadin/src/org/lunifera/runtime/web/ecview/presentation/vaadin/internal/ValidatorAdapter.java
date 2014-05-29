package org.lunifera.runtime.web.ecview.presentation.vaadin.internal;

import org.eclipse.emf.ecp.ecview.common.validation.IStatus;
import org.eclipse.emf.ecp.ecview.common.validation.IValidator;

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
