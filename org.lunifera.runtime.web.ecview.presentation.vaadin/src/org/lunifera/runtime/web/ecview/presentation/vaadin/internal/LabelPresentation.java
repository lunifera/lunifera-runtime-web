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

import org.lunifera.ecview.core.common.editpart.IElementEditpart;
import org.lunifera.ecview.core.extension.model.extension.ExtensionModelPackage;
import org.lunifera.ecview.core.extension.model.extension.YLabel;
import org.lunifera.ecview.core.ui.core.editparts.extension.ILabelEditpart;
import org.lunifera.runtime.web.ecview.presentation.vaadin.common.AbstractEmbeddedWidgetPresenter;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.util.Util;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;

/**
 * This presenter is responsible to render a text field on the given layout.
 */
public class LabelPresentation extends
		AbstractEmbeddedWidgetPresenter<Component> {

	private final ModelAccess modelAccess;
	private Label label;
	private ObjectProperty<String> property;

	/**
	 * Constructor.
	 * 
	 * @param editpart
	 *            The editpart of that presenter
	 */
	public LabelPresentation(IElementEditpart editpart) {
		super((ILabelEditpart) editpart);
		this.modelAccess = new ModelAccess((YLabel) editpart.getModel());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component doCreateWidget(Object parent) {
		if (label == null) {

			label = new Label();
			label.addStyleName(CSS_CLASS_CONTROL);
			label.setImmediate(true);
			setupComponent(label, getCastedModel());
			
			if (modelAccess.isCssIdValid()) {
				label.setId(modelAccess.getCssID());
			} else {
				label.setId(getEditpart().getId());
			}

			associateWidget(label, modelAccess.yField);

			property = new ObjectProperty<String>("", String.class);
			label.setPropertyDataSource(property);

			// creates the binding for the field
			createBindings(modelAccess.yField, label, null);

			if (modelAccess.isCssClassValid()) {
				label.addStyleName(modelAccess.getCssClass());
			}

			applyCaptions();
		}
		return label;
	}

	/**
	 * Creates the bindings for the given values.
	 * 
	 * @param yField
	 * @param field
	 */
	protected void createBindings(YLabel yField, Label field,
			AbstractComponent componentBase) {
		// create the model binding from ridget to ECView-model
		registerBinding(createBindings_Value(castEObject(getModel()),
				ExtensionModelPackage.Literals.YLABEL__VALUE, field));

		super.createBindings(yField, field, componentBase);
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
				modelAccess.getLabelI18nKey(), getLocale(), label);
	}

	@Override
	public Component getWidget() {
		return label;
	}

	@Override
	public boolean isRendered() {
		return label != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doUnrender() {
		if (label != null) {

			// unbind all active bindings
			unbind();

			ComponentContainer parent = ((ComponentContainer) label.getParent());
			if (parent != null) {
				parent.removeComponent(label);
			}

			// remove assocations
			unassociateWidget(label);

			label = null;
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
		private final YLabel yField;

		public ModelAccess(YLabel yField) {
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
			return yField.getDatadescription() != null ? yField.getDatadescription().getLabel() : null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabelI18nKey() {
			return yField.getDatadescription() != null ? yField.getDatadescription().getLabelI18nKey() : null;
		}
	}
}
