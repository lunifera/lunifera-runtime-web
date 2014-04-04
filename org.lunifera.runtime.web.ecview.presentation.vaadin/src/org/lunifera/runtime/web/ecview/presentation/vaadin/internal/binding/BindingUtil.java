package org.lunifera.runtime.web.ecview.presentation.vaadin.internal.binding;

import org.eclipse.core.databinding.Binding;
import org.eclipse.emf.ecp.ecview.common.editpart.DelegatingEditPartManager;
import org.eclipse.emf.ecp.ecview.common.editpart.IEmbeddableValueEndpointEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.binding.IBindingEditpart;
import org.eclipse.emf.ecp.ecview.common.model.binding.YBinding;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddableValueEndpoint;
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

	/**
	 * Returns the binding editpart for the given bindable.
	 * 
	 * @param bindable
	 * @return
	 */
	public static IBindingEditpart getValueBindingEditpart(
			YValueBindable bindable) {
		if (bindable == null || bindable.getValueBindingEndpoint() == null) {
			return null;
		}

		YBinding yBinding = bindable.getValueBindingEndpoint().getBinding();
		IBindingEditpart bindingEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yBinding);
		return bindingEditpart;
	}

	/**
	 * Returns the value endpoint editpart for the given bindable.
	 * 
	 * @param bindable
	 * @return
	 */
	public static IEmbeddableValueEndpointEditpart getValueEndpointEditpart(
			YValueBindable bindable) {
		if (bindable == null || bindable.getValueBindingEndpoint() == null) {
			return null;
		}

		YEmbeddableValueEndpoint yBinding = bindable.getValueBindingEndpoint();
		IEmbeddableValueEndpointEditpart bindingEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yBinding);
		return bindingEditpart;
	}

	/**
	 * Returns the value endpoint editpart for the given endpoint.
	 * 
	 * @param bindable
	 * @return
	 */
	public static IEmbeddableValueEndpointEditpart getValueEndpointEditpart(
			YEmbeddableValueEndpoint endpoint) {
		IEmbeddableValueEndpointEditpart bindingEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(endpoint);
		return bindingEditpart;
	}

}
