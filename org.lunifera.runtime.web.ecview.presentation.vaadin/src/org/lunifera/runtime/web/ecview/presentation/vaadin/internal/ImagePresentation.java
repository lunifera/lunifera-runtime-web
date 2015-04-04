/**
 * Copyright (c) 2011 - 2015, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *         Florian Pirchner - Initial implementation
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin.internal;

import java.util.Locale;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFObservables;
import org.lunifera.ecview.core.common.editpart.IElementEditpart;
import org.lunifera.ecview.core.extension.model.extension.ExtensionModelPackage;
import org.lunifera.ecview.core.extension.model.extension.YImage;
import org.lunifera.ecview.core.ui.core.editparts.extension.IImageEditpart;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.util.Util;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Embedded;

/**
 * This presenter is responsible to render a text area on the given layout.
 */
public class ImagePresentation extends
		AbstractEmbeddedWidgetPresenter<Component> {

	private final ModelAccess modelAccess;
	private Embedded image;

	/**
	 * Constructor.
	 * 
	 * @param editpart
	 *            The editpart of that presenter
	 */
	public ImagePresentation(IElementEditpart editpart) {
		super((IImageEditpart) editpart);
		this.modelAccess = new ModelAccess((YImage) editpart.getModel());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component doCreateWidget(Object parent) {
		if (image == null) {

			image = new Embedded();
			image.addStyleName(CSS_CLASS_CONTROL);
			image.setImmediate(true);
			setupComponent(image, getCastedModel());

			associateWidget(image, modelAccess.yField);
			if (modelAccess.isCssIdValid()) {
				image.setId(modelAccess.getCssID());
			} else {
				image.setId(getEditpart().getId());
			}

			// creates the binding for the field
			createBindings(modelAccess.yField, image, null);

			if (modelAccess.isCssClassValid()) {
				image.addStyleName(modelAccess.getCssClass());
			}

			// set the captions
			applyCaptions();
		}
		return image;
	}

	/**
	 * Creates the bindings from the ECView EMF model to the given UI element.
	 * 
	 * @param yEmbeddable
	 * @param container
	 * @param field
	 * @return Binding - the created binding
	 */
	protected void createBindings(YImage yField, AbstractComponent widget,
			AbstractComponent container) {

		IObservableValue modelObservable = EMFObservables.observeValue(yField,
				ExtensionModelPackage.Literals.YIMAGE__VALUE);
		IObservableValue uiObservable = VaadinObservables.observeSource(image);

		IConverter stringToResourceConverter = new Converter(String.class,
				Resource.class) {
			@Override
			public Object convert(Object fromObject) {
				return (fromObject != null && !fromObject.equals("")) ? new ThemeResource(
						(String) fromObject) : null;
			}
		};
		registerBinding(createBindings(uiObservable, modelObservable,
				new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER),
				new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE)
						.setConverter(stringToResourceConverter)));

		super.createBindings(yField, widget, container);
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
				modelAccess.getLabelI18nKey(), getLocale(), image);
	}

	@Override
	public Component getWidget() {
		return image;
	}

	@Override
	public boolean isRendered() {
		return image != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doUnrender() {
		if (image != null) {

			// unbind all active bindings
			unbind();

			// remove assocations
			unassociateWidget(image);

			ComponentContainer parent = ((ComponentContainer) image.getParent());
			if (parent != null) {
				parent.removeComponent(image);
			}
			image = null;
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
		private final YImage yField;

		public ModelAccess(YImage yField) {
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
