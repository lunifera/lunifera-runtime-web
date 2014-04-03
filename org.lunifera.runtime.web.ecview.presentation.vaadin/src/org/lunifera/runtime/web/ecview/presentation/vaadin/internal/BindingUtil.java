package org.lunifera.runtime.web.ecview.presentation.vaadin.internal;

import org.eclipse.core.databinding.Binding;
import org.eclipse.emf.ecp.ecview.common.editpart.DelegatingEditPartManager;
import org.eclipse.emf.ecp.ecview.common.editpart.binding.IBindingEditpart;
import org.eclipse.emf.ecp.ecview.common.model.binding.YBinding;
import org.eclipse.emf.ecp.ecview.common.model.core.YValueBindable;

public class BindingUtil {

	/**
	 * Returns the value binding for the given bindable.
	 * 
	 * @param bindable
	 * @return
	 */
	public static Binding getValueBinding(YValueBindable bindable) {
		if (bindable == null || bindable.getValueBindingEndpoint() == null) {
			return null;
		}

		YBinding yBinding = bindable.getValueBindingEndpoint().getBinding();
		IBindingEditpart bindingEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yBinding);
		Binding binding = bindingEditpart.getBinding();
		return binding;
	}

}
