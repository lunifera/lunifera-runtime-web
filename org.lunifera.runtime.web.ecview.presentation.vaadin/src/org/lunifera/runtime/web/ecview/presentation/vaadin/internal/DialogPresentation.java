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
import java.util.Set;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.EObject;
import org.lunifera.ecview.core.common.binding.IECViewBindingManager;
import org.lunifera.ecview.core.common.context.II18nService;
import org.lunifera.ecview.core.common.context.ILocaleChangedService;
import org.lunifera.ecview.core.common.context.IViewContext;
import org.lunifera.ecview.core.common.disposal.AbstractDisposable;
import org.lunifera.ecview.core.common.editpart.IDialogEditpart;
import org.lunifera.ecview.core.common.editpart.IElementEditpart;
import org.lunifera.ecview.core.common.editpart.IEmbeddableEditpart;
import org.lunifera.ecview.core.common.editpart.binding.IBindableEndpointEditpart;
import org.lunifera.ecview.core.common.editpart.binding.IBindableValueEndpointEditpart;
import org.lunifera.ecview.core.common.editpart.datatypes.IDatatypeEditpart.DatatypeChangeEvent;
import org.lunifera.ecview.core.common.editpart.visibility.IVisibilityPropertiesEditpart;
import org.lunifera.ecview.core.common.model.core.CoreModelPackage;
import org.lunifera.ecview.core.common.model.core.YDialog;
import org.lunifera.ecview.core.common.presentation.IDialogPresentation;
import org.lunifera.ecview.core.util.emf.ModelUtil;
import org.lunifera.runtime.web.ecview.presentation.vaadin.IConstants;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;

/**
 * This presenter is responsible to render a text field on the given layout.
 */
public class DialogPresentation extends AbstractDisposable implements
		IDialogPresentation<Component>, ILocaleChangedService.LocaleListener {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DialogPresentation.class);

	private ModelAccess modelAccess;
	private final IDialogEditpart editpart;
	private Window window;
	private IEmbeddableEditpart content;

	private Object inputDataBindingEndpoint;

	private Binding binding;

	/**
	 * Constructor.
	 * 
	 * @param editpart
	 *            The editpart for that presentation.
	 */
	public DialogPresentation(IElementEditpart editpart) {
		this.editpart = (IDialogEditpart) editpart;
		this.modelAccess = new ModelAccess((YDialog) editpart.getModel());
	}

	@Override
	public YDialog getModel() {
		return (YDialog) editpart.getModel();
	}

	/**
	 * Returns the editpart the presenter will render for.
	 * 
	 * @return editpart
	 */
	public IDialogEditpart getEditpart() {
		checkDisposed();
		return editpart;
	}

	/**
	 * Is called to render the content.
	 */
	protected void renderContent() {
		if (!isRendered()) {
			return;
		}

		window.setContent(null);

		if (content != null) {
			Component contentComponent = (Component) content.render(window);
			window.setContent(contentComponent);
		} else {
			LOGGER.warn("Content is null");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("serial")
	@Override
	public Component createWidget(Object parent) {
		if (window == null) {
			// create component base with grid layout to enable margins
			//
			window = new Window();
			window.addStyleName(IConstants.CSS_CLASS_CONTROL);

			window.addCloseListener(new Window.CloseListener() {
				@Override
				public void windowClose(CloseEvent e) {
					getEditpart().getView().closeDialog(getEditpart());
				}
			});

			if (modelAccess.isCssIdValid()) {
				window.setId(modelAccess.getCssID());
			} else {
				window.setId(editpart.getId());
			}

			if (modelAccess.isCssClassValid()) {
				window.addStyleName(modelAccess.getCssClass());
			}

			// render the content
			//
			renderContent();

			createBindings();

			// register as an locale change listener
			IViewContext context = ModelUtil.getViewContext(modelAccess.yDialog
					.getView());
			ILocaleChangedService service = context
					.getService(ILocaleChangedService.ID);
			if (service != null) {
				service.addLocaleListener(this);
			}

			applyCaptions();

		}
		return window;
	}

	/**
	 * Applies the labels to the widgets.
	 */
	protected void applyCaptions() {
		II18nService service = getI18nService();
		if (service != null && modelAccess.isLabelI18nKeyValid()) {
			window.setCaption(service.getValue(modelAccess.getLabelI18nKey(),
					getLocale()));
		} else {
			if (modelAccess.isLabelValid()) {
				window.setCaption(modelAccess.getLabel());
			}
		}
	}

	/**
	 * Returns the active locale for the view.
	 * 
	 * @return
	 */
	protected Locale getLocale() {
		return getViewContext().getLocale();
	}

	/**
	 * Returns the i18n service or <code>null</code> if no service is available.
	 * 
	 * @return
	 */
	protected II18nService getI18nService() {
		return getViewContext().getService(II18nService.ID);
	}

	private void createBindings() {
		if (inputDataBindingEndpoint != null) {
			// if input data is available, then bind values against that input

			if (window.isAttached()) {
				VaadinObservables.activateRealm(UI.getCurrent());
			}
			IECViewBindingManager bindingManager = getViewContext().getService(
					IECViewBindingManager.class.getName());
			IBindableValueEndpointEditpart modelValueEditpart = (IBindableValueEndpointEditpart) inputDataBindingEndpoint;
			IObservableValue modelObservable = modelValueEditpart
					.getObservable();

			IObservableValue targetObservable = EMFObservables.observeValue(
					(EObject) getModel(),
					CoreModelPackage.Literals.YDIALOG__VALUE);
			binding = bindingManager.bindValue(targetObservable,
					modelObservable, new UpdateValueStrategy(
							UpdateValueStrategy.POLICY_NEVER),
					new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
		}
	}

	@Override
	public void setInputDataBindingEndpoint(
			IBindableEndpointEditpart bindingEndpoint) {
		this.inputDataBindingEndpoint = bindingEndpoint;

		if (binding != null) {
			binding.dispose();
			binding = null;
		}

		if (isRendered()) {
			createBindings();
		}

	}

	@Override
	public Component getWidget() {
		return window;
	}

	@Override
	public boolean isRendered() {
		return window != null;
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
		if (window != null) {
			// unregister as an locale change listener
			IViewContext context = ModelUtil.getViewContext(modelAccess.yDialog
					.getView());
			ILocaleChangedService service = context
					.getService(ILocaleChangedService.ID);
			if (service != null) {
				service.removeLocaleListener(this);
			}

			if (binding != null) {
				binding.dispose();
				binding = null;
			}

			ComponentContainer parent = ((ComponentContainer) window
					.getParent());
			if (parent != null) {
				parent.removeComponent(window);
			}
			window = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalDispose() {
		if (window != null) {
			unrender();
		}
	}

	@Override
	public void setContent(IEmbeddableEditpart editpart) {
		this.content = editpart;
		renderContent();
	}

	@Override
	public IViewContext getViewContext() {
		return ModelUtil.getViewContext(getModel().getView());
	}

	@Override
	public IObservable getObservableValue(Object model) {
		throw new UnsupportedOperationException("Must be overridden!");
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
		window.setLocale(locale);
	}

	/**
	 * An internal helper class.
	 */
	private static class ModelAccess {
		private final YDialog yDialog;

		public ModelAccess(YDialog yDialog) {
			super();
			this.yDialog = yDialog;
		}

		/**
		 * @return
		 * @see org.lunifera.ecview.core.ui.core.model.core.YCssAble#getCssClass()
		 */
		public String getCssClass() {
			return yDialog.getCssClass();
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
		 * @see org.lunifera.ecview.core.ui.core.model.core.YCssAble#getCssID()
		 */
		public String getCssID() {
			return yDialog.getCssID();
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
		 * Returns true, if the label is valid.
		 * 
		 * @return
		 */
		public boolean isLabelValid() {
			return yDialog.getDatadescription() != null
					&& yDialog.getDatadescription().getLabel() != null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabel() {
			return yDialog.getDatadescription().getLabel();
		}

		/**
		 * Returns true, if the label is valid.
		 * 
		 * @return
		 */
		public boolean isLabelI18nKeyValid() {
			return yDialog.getDatadescription() != null
					&& yDialog.getDatadescription().getLabelI18nKey() != null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabelI18nKey() {
			return yDialog.getDatadescription().getLabelI18nKey();
		}
	}
}
