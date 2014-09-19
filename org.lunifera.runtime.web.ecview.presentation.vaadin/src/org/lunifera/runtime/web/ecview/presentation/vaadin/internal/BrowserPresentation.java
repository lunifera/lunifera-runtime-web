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

import java.util.Locale;

import org.lunifera.ecview.core.common.context.II18nService;
import org.lunifera.ecview.core.common.editpart.IElementEditpart;
import org.lunifera.ecview.core.extension.model.extension.YBrowser;
import org.lunifera.ecview.core.ui.core.editparts.extension.IBrowserEditpart;

import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * This presenter is responsible to render a text area on the given layout.
 */
public class BrowserPresentation extends
		AbstractEmbeddedWidgetPresenter<Component> {

	private final ModelAccess modelAccess;
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
			
			associateWidget(browser, modelAccess.yBrowser);

			if (modelAccess.isCssIdValid()) {
				browser.setId(modelAccess.getCssID());
			} else {
				browser.setId(getEditpart().getId());
			}

			// creates the binding for the field
			createBindings(modelAccess.yBrowser, browser, null);

			if (modelAccess.isCssClassValid()) {
				browser.addStyleName(modelAccess.getCssClass());
			}

			// set the captions
			applyCaptions();
		}
		return browser;
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
		II18nService service = getI18nService();
		if (service != null && modelAccess.isLabelI18nKeyValid()) {
			browser.setCaption(service.getValue(modelAccess.getLabelI18nKey(),
					getLocale()));
		} else {
			if (modelAccess.isLabelValid()) {
				browser.setCaption(modelAccess.getLabel());
			}
		}
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
		private final YBrowser yBrowser;

		public ModelAccess(YBrowser yBrowser) {
			super();
			this.yBrowser = yBrowser;
		}

		/**
		 * @return
		 * @see org.lunifera.ecview.core.ui.core.model.core.YCssAble#getCssClass()
		 */
		public String getCssClass() {
			return yBrowser.getCssClass();
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
			return yBrowser.getCssID();
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
			return yBrowser.getDatadescription() != null
					&& yBrowser.getDatadescription().getLabel() != null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabel() {
			return yBrowser.getDatadescription().getLabel();
		}

		/**
		 * Returns true, if the label is valid.
		 * 
		 * @return
		 */
		public boolean isLabelI18nKeyValid() {
			return yBrowser.getDatadescription() != null
					&& yBrowser.getDatadescription().getLabelI18nKey() != null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabelI18nKey() {
			return yBrowser.getDatadescription().getLabelI18nKey();
		}
	}
}
