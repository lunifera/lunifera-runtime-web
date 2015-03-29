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

import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.lunifera.ecview.core.common.editpart.IElementEditpart;
import org.lunifera.ecview.core.extension.model.extension.ExtensionModelPackage;
import org.lunifera.ecview.core.extension.model.extension.YBrowser;
import org.lunifera.ecview.core.extension.model.extension.YBrowserStreamInput;
import org.lunifera.ecview.core.ui.core.editparts.extension.IBrowserEditpart;
import org.lunifera.runtime.web.ecview.presentation.vaadin.IBindingManager;
import org.lunifera.runtime.web.ecview.presentation.vaadin.common.AbstractEmbeddedWidgetPresenter;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.AbstractEmbedded;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * This presenter is responsible to render a text area on the given layout.
 */
public class BrowserPresentation extends
		AbstractEmbeddedWidgetPresenter<Component> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BrowserPresentation.class);

	private final ModelAccess modelAccess;
	private SourceConverter sourceConverter;
	private BrowserFrame browser;

	/**
	 * Constructor.
	 * 
	 * @param editpart
	 *            The editpart of that presenter
	 */
	public BrowserPresentation(IElementEditpart editpart) {
		super((IBrowserEditpart) editpart);
		this.modelAccess = new ModelAccess((YBrowser) editpart.getModel());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component doCreateWidget(Object parent) {
		if (browser == null) {

			browser = new BrowserFrame();
			browser.addStyleName(CSS_CLASS_CONTROL);
			browser.setImmediate(true);
			setupComponent(browser, getCastedModel());

			associateWidget(browser, modelAccess.yField);

			if (modelAccess.isCssIdValid()) {
				browser.setId(modelAccess.getCssID());
			} else {
				browser.setId(getEditpart().getId());
			}

			initialize(browser, getCastedModel());
			
			// creates the binding for the field
			createBindings(modelAccess.yField, browser, null);

			if (modelAccess.isCssClassValid()) {
				browser.addStyleName(modelAccess.getCssClass());
			}

			// set the captions
			applyCaptions();
		}
		return browser;
	}

	/**
	 * Creates the bindings for the given values.
	 * 
	 * @param yField
	 * @param field
	 */
	protected void createBindings(YBrowser yField, BrowserFrame field,
			AbstractComponentContainer parent) {

		// bind the model to the source converter
		registerBinding(createBindingsSource(castEObject(getModel()),
				ExtensionModelPackage.Literals.YBROWSER__VALUE, field));

		super.createBindings(yField, field, parent);
	}

	protected Binding createBindingsSource(EObject model,
			EStructuralFeature modelFeature, AbstractEmbedded field) {
		IBindingManager bindingManager = getViewContext()
				.getService(
						org.lunifera.ecview.core.common.binding.IECViewBindingManager.class
								.getName());
		if (bindingManager != null) {

			sourceConverter = new SourceConverter();

			// bind the value of yText to textRidget
			IObservableValue modelObservable = EMFObservables.observeValue(
					model, modelFeature);
			IObservableValue uiObservable = PojoObservables.observeValue(
					sourceConverter, "source");
			return bindingManager.bindValue(uiObservable, modelObservable,
					new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER),
					new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
		}
		return null;
	}

	@Override
	protected void doUpdateLocale(Locale locale) {
		// no need to set the locale to the ui elements. Is handled by vaadin
		// internally.

		// update the captions
		applyCaptions();
	}

	/**
	 * Applies the labels to the widgets.
	 */
	protected void applyCaptions() {
		Util.applyCaptions(getI18nService(), modelAccess.getLabel(),
				modelAccess.getLabelI18nKey(), getLocale(), browser);
	}

	@Override
	public Component getWidget() {
		return browser;
	}

	@Override
	public boolean isRendered() {
		return browser != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doUnrender() {
		if (browser != null) {

			// unbind all active bindings
			unbind();

			// remove assocations
			unassociateWidget(browser);

			ComponentContainer parent = ((ComponentContainer) browser
					.getParent());
			if (parent != null) {
				parent.removeComponent(browser);
			}
			browser = null;
			sourceConverter = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalDispose() {
		try {
			unrender();
		} finally {
			super.internalDispose();
		}
	}

	/**
	 * A helper class.
	 */
	private static class ModelAccess {
		private final YBrowser yField;

		public ModelAccess(YBrowser yField) {
			super();
			this.yField = yField;
		}

		/**
		 * @return
		 * @see org.lunifera.ecview.core.ui.core.model.core.YCssAble#getCssClass()
		 */
		public String getCssClass() {
			return yField.getCssClass();
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
			return yField.getCssID();
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
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabel() {
			return yField.getDatadescription() != null ? yField
					.getDatadescription().getLabel() : null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabelI18nKey() {
			return yField.getDatadescription() != null ? yField
					.getDatadescription().getLabelI18nKey() : null;
		}
	}

	@SuppressWarnings("serial")
	private class SourceConverter {

		private Object source;

		@SuppressWarnings("unused")
		public Object getSource() {
			return source;
		}

		@SuppressWarnings("unused")
		public void setSource(Object source) {
			this.source = source;

			if (browser == null) {
				LOGGER.error("Browser instance is null, but binding still active!");
				return;
			}

			Resource resource = null;
			if (source instanceof String) {
				resource = new ExternalResource((URL) source);
			} else if (source instanceof YBrowserStreamInput) {
				YBrowserStreamInput input = (YBrowserStreamInput) source;
				final InputStream stream = input.getInputStream();
				StreamResource tempResource = new StreamResource(
						new StreamResource.StreamSource() {
							@Override
							public InputStream getStream() {
								return stream;
							}
						}, input.getFilename());
				tempResource.setMIMEType(input.getMimeType());
				resource = tempResource;
			}

			browser.setSource(resource);
		}
	}
}
