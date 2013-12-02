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
package org.lunifera.runtime.web.ecview.presentation.vaadin.internal;

import org.eclipse.emf.ecp.ecview.common.editpart.IElementEditpart;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YDecimalField;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.IDecimalFieldEditpart;
import org.lunifera.runtime.web.vaadin.components.fields.DecimalField;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;

/**
 * This presenter is responsible to render a text area on the given layout.
 */
public class DecimalFieldPresentation extends
		AbstractVaadinWidgetPresenter<Component> {

	private final ModelAccess modelAccess;
	private CssLayout componentBase;
	private DecimalField decimalField;

	/**
	 * Constructor.
	 * 
	 * @param editpart
	 *            The editpart of that presenter
	 */
	public DecimalFieldPresentation(IElementEditpart editpart) {
		super((IDecimalFieldEditpart) editpart);
		this.modelAccess = new ModelAccess((YDecimalField) editpart.getModel());
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

			decimalField = new DecimalField();
			decimalField.addStyleName(CSS_CLASS__CONTROL);
			decimalField.setSizeFull();

			// creates the binding for the field
			createBindings(modelAccess.yDecimalField, decimalField);

			componentBase.addComponent(decimalField);

			if (modelAccess.isCssClassValid()) {
				decimalField.addStyleName(modelAccess.getCssClass());
			}

			if (modelAccess.isLabelValid()) {
				decimalField.setCaption(modelAccess.getLabel());
			}

			decimalField.setPrecision(modelAccess.getPrecision());
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
			ComponentContainer parent = ((ComponentContainer) componentBase
					.getParent());
			if (parent != null) {
				parent.removeComponent(componentBase);
			}
			componentBase = null;
			decimalField = null;
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
		private final YDecimalField yDecimalField;

		public ModelAccess(YDecimalField yDecimalField) {
			super();
			this.yDecimalField = yDecimalField;
		}

		/**
		 * @return
		 * @see org.eclipse.emf.ecp.ecview.ui.core.model.core.YCssAble#getCssClass()
		 */
		public String getCssClass() {
			return yDecimalField.getCssClass();
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
			return yDecimalField.getCssID();
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
			return yDecimalField.getDatadescription() != null
					&& yDecimalField.getDatadescription().getLabel() != null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabel() {
			return yDecimalField.getDatadescription().getLabel();
		}

		/**
		 * Returns the precision of the decimal field.
		 * 
		 * @return
		 */
		public int getPrecision() {
			return yDecimalField.getDatatype() != null ? yDecimalField
					.getDatatype().getPrecision() : 0;
		}
	}
}
