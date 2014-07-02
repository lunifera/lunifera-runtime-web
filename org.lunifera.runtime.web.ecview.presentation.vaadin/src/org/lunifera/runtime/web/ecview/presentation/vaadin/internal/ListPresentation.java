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

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.ecp.ecview.common.context.II18nService;
import org.eclipse.emf.ecp.ecview.common.editpart.IElementEditpart;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddableBindingEndpoint;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddableCollectionEndpoint;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddableMultiSelectionEndpoint;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddableSelectionEndpoint;
import org.eclipse.emf.ecp.ecview.databinding.emf.model.ECViewModelBindable;
import org.eclipse.emf.ecp.ecview.extension.model.extension.ExtensionModelPackage;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YList;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YSelectionType;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.IListEditpart;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Field;
import com.vaadin.ui.ListSelect;

/**
 * This presenter is responsible to render a list on the given layout.
 */
public class ListPresentation extends AbstractFieldWidgetPresenter<Component> {

	private final ModelAccess modelAccess;
	private CssLayout componentBase;
	private ListSelect list;
	private ObjectProperty property;

	/**
	 * Constructor.
	 * 
	 * @param editpart
	 *            The editpart of that presenter
	 */
	public ListPresentation(IElementEditpart editpart) {
		super((IListEditpart) editpart);
		this.modelAccess = new ModelAccess((YList) editpart.getModel());
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Component doCreateWidget(Object parent) {
		if (componentBase == null) {
			componentBase = new CssLayout();
			componentBase.addStyleName(CSS_CLASS_CONTROL_BASE);
			if (modelAccess.isCssIdValid()) {
				componentBase.setId(modelAccess.getCssID());
			} else {
				componentBase.setId(getEditpart().getId());
			}

			list = new ListSelect();
			list.addStyleName(CSS_CLASS_CONTROL);
			list.setMultiSelect(modelAccess.yList.getSelectionType() == YSelectionType.MULTI);
			list.setImmediate(true);
			list.setSizeFull();

			if (list.isMultiSelect()) {
				property = new ObjectProperty(new HashSet(), Set.class);
			} else {
				property = new ObjectProperty(null, modelAccess.yList.getType());
			}
			list.setPropertyDataSource(property);

			// creates the binding for the field
			createBindings(modelAccess.yList, list);

			componentBase.addComponent(list);

			if (modelAccess.isCssClassValid()) {
				list.addStyleName(modelAccess.getCssClass());
			}

			applyCaptions();

			initializeField(list);
		}
		return componentBase;
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
			componentBase.setCaption(service.getValue(
					modelAccess.getLabelI18nKey(), getLocale()));
		} else {
			if (modelAccess.isLabelValid()) {
				componentBase.setCaption(modelAccess.getLabel());
			}
		}
	}

	@Override
	protected Field<?> doGetField() {
		return list;
	}

	@Override
	protected IObservable internalGetObservableEndpoint(
			YEmbeddableBindingEndpoint bindableValue) {
		if (bindableValue == null) {
			throw new NullPointerException("BindableValue must not be null!");
		}

		if (bindableValue instanceof YEmbeddableCollectionEndpoint) {
			return internalGetCollectionEndpoint();
		} else if (bindableValue instanceof YEmbeddableSelectionEndpoint) {
			return internalGetSelectionEndpoint((YEmbeddableSelectionEndpoint) bindableValue);
		} else if (bindableValue instanceof YEmbeddableMultiSelectionEndpoint) {
			return internalGetMultiSelectionEndpoint();
		}
		throw new IllegalArgumentException("Not a valid input: "
				+ bindableValue);
	}

	/**
	 * Returns the observable to observe the collection.
	 * 
	 * @return
	 */
	protected IObservableList internalGetCollectionEndpoint() {
		// return the observable value for text
		return EMFProperties.list(
				ExtensionModelPackage.Literals.YLIST__COLLECTION).observe(
				getModel());
	}

	/**
	 * Returns the observable to observe the selection.
	 * 
	 * @return
	 */
	@SuppressWarnings({ "restriction" })
	protected IObservableValue internalGetSelectionEndpoint(
			YEmbeddableSelectionEndpoint yEndpoint) {

		String attributePath = ECViewModelBindable.getAttributePath(
				ExtensionModelPackage.Literals.YLIST__SELECTION,
				yEndpoint.getAttributePath());

		// return the observable value for text
		return ECViewModelBindable.observeValue(castEObject(getModel()),
				attributePath, modelAccess.yList.getType());
	}

	/**
	 * Returns the observable to observe the selection.
	 * 
	 * @return
	 */
	protected IObservableList internalGetMultiSelectionEndpoint() {
		// return the observable value for text
		return EMFProperties.list(
				ExtensionModelPackage.Literals.YLIST__MULTI_SELECTION).observe(
				getModel());
	}

	/**
	 * Creates the bindings for the given values.
	 * 
	 * @param yField
	 * @param field
	 */
	protected void createBindings(YList yField, ListSelect field) {
		// create the model binding from ridget to ECView-model
		registerBinding(createBindings_ContainerContents(
				castEObject(getModel()),
				ExtensionModelPackage.Literals.YLIST__COLLECTION, field,
				yField.getType()));

		if (modelAccess.yList.getSelectionType() == YSelectionType.MULTI) {
			// create the model binding from ridget to ECView-model
			registerBinding(createBindingsMultiSelection(
					castEObject(getModel()),
					ExtensionModelPackage.Literals.YLIST__MULTI_SELECTION,
					field, yField.getType()));
		} else {
			// create the model binding from ridget to ECView-model
			registerBinding(createBindingsSelection(castEObject(getModel()),
					ExtensionModelPackage.Literals.YLIST__SELECTION, field,
					yField.getType()));

		}

		super.createBindings(yField, field);
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
	public void doUnrender() {
		if (componentBase != null) {

			// unbind all active bindings
			unbind();

			ComponentContainer parent = ((ComponentContainer) componentBase
					.getParent());
			if (parent != null) {
				parent.removeComponent(componentBase);
			}
			componentBase = null;
			list = null;
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
		private final YList yList;

		public ModelAccess(YList yList) {
			super();
			this.yList = yList;
		}

		/**
		 * @return
		 * @see org.eclipse.emf.ecp.ecview.ui.core.model.core.YCssAble#getCssClass()
		 */
		public String getCssClass() {
			return yList.getCssClass();
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
			return yList.getCssID();
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
			return yList.getDatadescription() != null
					&& yList.getDatadescription().getLabel() != null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabel() {
			return yList.getDatadescription().getLabel();
		}

		/**
		 * Returns true, if the label is valid.
		 * 
		 * @return
		 */
		public boolean isLabelI18nKeyValid() {
			return yList.getDatadescription() != null
					&& yList.getDatadescription().getLabelI18nKey() != null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabelI18nKey() {
			return yList.getDatadescription().getLabelI18nKey();
		}
	}
}
