/**
 * Copyright (c) 2012 Florian Pirchner (Vienna, Austria) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Florian Pirchner - initial API and implementation
 */
package org.lunifera.web.ecp.uimodel.presentation.vaadin.internal;

import org.eclipse.emf.ecp.ecview.common.editpart.IElementEditpart;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YTextField;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.ITextFieldEditpart;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;

/**
 * This presenter is responsible to render a text field on the given layout.
 */
public class TextFieldPresentation extends
		AbstractVaadinWidgetPresenter<Component> {

	private final ModelAccess modelAccess;
	private CssLayout componentBase;
	private TextField text;

	/**
	 * Constructor.
	 * 
	 * @param editpart
	 *            The editpart of that presenter
	 */
	public TextFieldPresentation(IElementEditpart editpart) {
		super((ITextFieldEditpart) editpart);
		this.modelAccess = new ModelAccess((YTextField) editpart.getModel());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component createWidget(Object parent) {
		if (componentBase == null) {
			componentBase = new CssLayout();
			componentBase.addStyleName(CSS_CLASS__CONTROL_BASE);
			if (modelAccess.isCssIdValid()) {
				componentBase.setId(modelAccess.getCssID());
			} else {
				componentBase.setId(getEditpart().getId());
			}

			text = new TextField();
			text.addStyleName(CSS_CLASS__CONTROL);
			text.setSizeFull();

			// creates the binding for the field
			createBindings(modelAccess.yText, text);

			text.setEnabled(modelAccess.yText.isInitialEnabled());
			text.setReadOnly(!modelAccess.yText.isInitialEditable());
			text.setVisible(!modelAccess.yText.isInitialVisible());

			componentBase.addComponent(text);

			if (modelAccess.isCssClassValid()) {
				text.addStyleName(modelAccess.getCssClass());
			}

			if (modelAccess.isLabelValid()) {
				text.setCaption(modelAccess.getLabel());
			}
		}
		return componentBase;
	}

	protected void createBindings(YTextField yEmbeddable, TextField field) {
		super.createBindings(yEmbeddable, field);

	}

	@Override
	public Component getWidget() {
		return componentBase;
	}

	@Override
	public boolean isRendered() {
		return componentBase != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unrender() {
		if (componentBase != null) {
			ComponentContainer parent = ((ComponentContainer) componentBase
					.getParent());
			if (parent != null) {
				parent.removeComponent(componentBase);
			}
			componentBase = null;
			text = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalDispose() {
		// unrender the ui component
		unrender();
	}

	/**
	 * A helper class.
	 */
	private static class ModelAccess {
		private final YTextField yText;

		public ModelAccess(YTextField yText) {
			super();
			this.yText = yText;
		}

		/**
		 * @return
		 * @see org.eclipse.emf.ecp.ecview.ui.core.model.core.YCssAble#getCssClass()
		 */
		public String getCssClass() {
			return yText.getCssClass();
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
			return yText.getCssID();
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
			return yText.getDatadescription() != null
					&& yText.getDatadescription().getLabel() != null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabel() {
			return yText.getDatadescription().getLabel();
		}
	}
}
