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

import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.emf.ecp.ecview.common.context.ILocaleChangedService;
import org.eclipse.emf.ecp.ecview.common.context.IViewContext;
import org.eclipse.emf.ecp.ecview.common.disposal.AbstractDisposable;
import org.eclipse.emf.ecp.ecview.common.editpart.IDialogEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.IEmbeddableEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.IViewEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.binding.IBindableEndpointEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.datatypes.IDatatypeEditpart.DatatypeChangeEvent;
import org.eclipse.emf.ecp.ecview.common.editpart.visibility.IVisibilityPropertiesEditpart;
import org.eclipse.emf.ecp.ecview.common.model.core.YView;
import org.eclipse.emf.ecp.ecview.common.presentation.IViewPresentation;
import org.eclipse.emf.ecp.ecview.common.services.IUiKitBasedService;
import org.eclipse.emf.ecp.ecview.common.tooling.IWidgetMouseClickService;
import org.eclipse.emf.ecp.ecview.util.emf.ModelUtil;
import org.lunifera.runtime.web.ecview.presentation.vaadin.IConstants;
import org.lunifera.runtime.web.ecview.presentation.vaadin.services.internal.WidgetMouseClickService;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Window;

/**
 * This presenter is responsible to render a text field on the given layout.
 */
public class ViewPresentation extends AbstractDisposable implements
		IViewPresentation<Component>, ILocaleChangedService.LocaleListener {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ViewPresentation.class);

	private ModelAccess modelAccess;
	private final IViewEditpart editpart;
	private CssLayout componentBase;
	private CssLayout component;
	private IEmbeddableEditpart content;

	/**
	 * Constructor.
	 * 
	 * @param editpart
	 *            The editpart for that presentation.
	 */
	public ViewPresentation(IViewEditpart editpart) {
		this.editpart = editpart;
		this.modelAccess = new ModelAccess((YView) editpart.getModel());
	}

	@Override
	public Object getModel() {
		return editpart.getModel();
	}

	/**
	 * Returns the editpart the presenter will render for.
	 * 
	 * @return editpart
	 */
	public IViewEditpart getEditpart() {
		checkDisposed();
		return editpart;
	}

	@Override
	public void render(Map<String, Object> options) {
		checkDisposed();
		this.content = editpart.getContent();
		ComponentContainer parent = (ComponentContainer) editpart.getContext()
				.getRootLayout();
		createWidget(parent);
	}

	/**
	 * Is called to render the content.
	 */
	protected void renderContent() {
		if (!isRendered()) {
			return;
		}

		component.removeAllComponents();

		if (content != null) {
			Component contentComponent = (Component) content.render(component);
			component.addComponent(contentComponent);
		} else {
			LOGGER.warn("Content is null");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component createWidget(Object parent) {
		if (componentBase == null) {
			// create component base with grid layout to enable margins
			//
			componentBase = new CssLayout();
			componentBase.setSizeFull();
			componentBase.addStyleName(IConstants.CSS_CLASS_CONTROL_BASE);

			ComponentContainer parentContainer = (ComponentContainer) parent;
			parentContainer.addComponent(componentBase);

			// create the component
			component = new CssLayout();
			component.addStyleName(IConstants.CSS_CLASS_CONTROL);
			component.setSizeFull();
			componentBase.addComponent(component);

			if (modelAccess.isCssIdValid()) {
				component.setId(modelAccess.getCssID());
			} else {
				component.setId(editpart.getId());
			}

			if (modelAccess.isCssClassValid()) {
				component.addStyleName(modelAccess.getCssClass());
			}

			if (modelAccess.isMargin()) {
				component.addStyleName(IConstants.CSS_CLASS_MARGIN);
			}

			// render the content
			//
			renderContent();

			// register as an locale change listener
			IViewContext context = ModelUtil.getViewContext(modelAccess.yView);
			ILocaleChangedService service = context
					.getService(ILocaleChangedService.ID);
			if (service != null) {
				service.addLocaleListener(this);
			}

		}
		return componentBase;
	}

	@Override
	public Component getWidget() {
		return componentBase;
	}

	@Override
	public boolean isRendered() {
		return componentBase != null;
	}

	@Override
	public Set<Binding> getUIBindings() {
		return Collections.emptySet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unrender() {
		if (componentBase != null) {
			// unregister as an locale change listener
			IViewContext context = ModelUtil.getViewContext(modelAccess.yView);
			ILocaleChangedService service = context
					.getService(ILocaleChangedService.ID);
			if (service != null) {
				service.removeLocaleListener(this);
			}

			ComponentContainer parent = ((ComponentContainer) componentBase
					.getParent());
			if (parent != null) {
				parent.removeComponent(componentBase);
			}
			componentBase = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalDispose() {
		if (componentBase != null) {
			unrender();
		}
	}

	@Override
	public void setContent(IEmbeddableEditpart editpart) {
		this.content = editpart;
		renderContent();
	}

	@Override
	public IEmbeddableEditpart getContent() {
		return content;
	}

	@Override
	public IViewContext getViewContext() {
		return getEditpart().getContext();
	}

	@Override
	public IObservable getObservableValue(Object model) {
		throw new UnsupportedOperationException("Must be overridden!");
	}

	@Override
	public void exec(Runnable runnable) {
		componentBase.getUI().accessSynchronously(runnable);
	}

	@Override
	public Future<?> execAsync(Runnable runnable) {
		return componentBase.getUI().access(runnable);
	}

	@Override
	public void apply(IVisibilityPropertiesEditpart properties) {

	}

	@Override
	public void resetVisibilityProperties() {

	}

	@Override
	public void notifyDatatypeChanged(DatatypeChangeEvent event) {

	}

	@Override
	public void localeChanged(Locale locale) {
		// pass the locale to the root element
		component.setLocale(locale);
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public <A extends IUiKitBasedService> A createService(Class<A> serviceClass) {
		if (serviceClass == IWidgetMouseClickService.class) {
			final WidgetMouseClickService service = new WidgetMouseClickService(
					getViewContext());
			service.activate();
			return (A) service;
		}

		throw new IllegalArgumentException(String.format(
				"%s is not a supported service.", serviceClass.getName()));
	}

	public void click(ClickEvent event) {

	}

	@Override
	public void openDialog(IDialogEditpart dialogEditpart, IBindableEndpointEditpart inputData) {
		if (!isRendered()) {
			return;
		}

//		VaadinObservables.activateRealm(navigationView.getUI());
		// set the input data to the child nav page
		dialogEditpart.setInputDataBindingEndpoint(inputData);
		Window dialog = (Window) dialogEditpart.render(null);
		componentBase.getUI().addWindow(dialog);
	}

	@Override
	public void closeDialog(IDialogEditpart dialogEditpart) {
		if (!isRendered()) {
			return;
		}

		Window dialog = (Window) dialogEditpart.getWidget();
		if (dialog != null) {
			componentBase.getUI().removeWindow(dialog);
			dialogEditpart.unrender();
		}
	}

	/**
	 * An internal helper class.
	 */
	private static class ModelAccess {
		private final YView yView;

		public ModelAccess(YView yView) {
			super();
			this.yView = yView;
		}

		/**
		 * @return
		 * @see org.eclipse.emf.ecp.ecview.ui.core.model.core.YCssAble#getCssClass()
		 */
		public String getCssClass() {
			return yView.getCssClass();
		}

		/**
		 * Returns true, if the css class is not null and not empty.
		 * 
		 * @return
		 */
		public boolean isCssClassValid() {
			return getCssClass() != null && !getCssClass().equals("");
		}

		/**
		 * @return
		 * @see org.eclipse.emf.ecp.ecview.ui.core.model.core.YCssAble#getCssID()
		 */
		public String getCssID() {
			return yView.getCssID();
		}

		/**
		 * Returns true, if the css id is not null and not empty.
		 * 
		 * @return
		 */
		public boolean isCssIdValid() {
			return getCssID() != null && !getCssID().equals("");
		}

		/**
		 * @return
		 * @see org.eclipse.emf.ecp.ecview.ui.core.model.extension.YGridLayout#isMargin()
		 */
		public boolean isMargin() {
			return yView.isMargin();
		}
	}
}
