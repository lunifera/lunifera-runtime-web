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

import org.eclipse.emf.ecp.ui.model.core.uimodel.extension.YUiTextArea;
import org.eclipse.emf.ecp.ui.uimodel.core.editparts.IUiElementEditpart;
import org.eclipse.emf.ecp.ui.uimodel.core.editparts.extension.IUiTextAreaEditpart;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextArea;

/**
 * This presenter is responsible to render a text area on the given layout.
 */
public class TextAreaPresentation extends AbstractSWTWidgetPresenter {

	private final ModelAccess modelAccess;
	private CssLayout componentBase;
	private TextArea textArea;

	/**
	 * Constructor.
	 * 
	 * @param editpart The editpart of that presenter
	 */
	public TextAreaPresentation(IUiElementEditpart editpart) {
		super((IUiTextAreaEditpart) editpart);
		this.modelAccess = new ModelAccess((YUiTextArea) editpart.getModel());
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

			textArea = new TextArea();
			textArea.addStyleName(CSS_CLASS__CONTROL);
			textArea.setSizeFull();
			componentBase.addComponent(textArea);

			if (modelAccess.isCssClassValid()) {
				textArea.addStyleName(modelAccess.getCssClass());
			}

			if (modelAccess.isLabelValid()) {
				textArea.setCaption(modelAccess.getLabel());
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unrender() {
		if (componentBase != null) {
			ComponentContainer parent = ((ComponentContainer) componentBase.getParent());
			if (parent != null) {
				parent.removeComponent(componentBase);
			}
			componentBase = null;
			textArea = null;
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
		private final YUiTextArea yTextArea;

		public ModelAccess(YUiTextArea yTextArea) {
			super();
			this.yTextArea = yTextArea;
		}

		/**
		 * @return
		 * @see org.eclipse.emf.ecp.ui.model.core.uimodel.YUiCssAble#getCssClass()
		 */
		public String getCssClass() {
			return yTextArea.getCssClass();
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
		 * @see org.eclipse.emf.ecp.ui.model.core.uimodel.YUiCssAble#getCssID()
		 */
		public String getCssID() {
			return yTextArea.getCssID();
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
			return yTextArea.getDatadescription() != null && yTextArea.getDatadescription().getLabel() != null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabel() {
			return yTextArea.getDatadescription().getLabel();
		}
	}
}
