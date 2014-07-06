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

import java.net.URI;
import java.util.Locale;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.ecp.ecview.common.binding.observables.ContextObservables;
import org.eclipse.emf.ecp.ecview.common.context.II18nService;
import org.eclipse.emf.ecp.ecview.common.editpart.IElementEditpart;
import org.eclipse.emf.ecp.ecview.common.model.core.YCollectionBindable;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddable;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddableBindingEndpoint;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddableCollectionEndpoint;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddableSelectionEndpoint;
import org.eclipse.emf.ecp.ecview.common.presentation.IWidgetPresentation;
import org.eclipse.emf.ecp.ecview.common.uri.URIHelper;
import org.eclipse.emf.ecp.ecview.databinding.emf.model.ECViewModelBindable;
import org.eclipse.emf.ecp.ecview.extension.model.extension.ExtensionModelPackage;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YMasterDetail;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.IMasterDetailEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.presentation.IMasterDetailPresentation;
import org.lunifera.runtime.web.ecview.presentation.vaadin.IConstants;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * This presenter is responsible to render a text field on the given layout.
 */
public class MasterDetailPresentation extends
		AbstractVaadinWidgetPresenter<ComponentContainer> implements
		IMasterDetailPresentation<ComponentContainer> {

	private final ModelAccess modelAccess;
	private VerticalLayout componentBase;

	private CssLayout masterBase;
	private CssLayout detailBase;
	private IWidgetPresentation<?> masterPresentation;
	private IWidgetPresentation<?> detailPresentation;
	private Binding masterCollectionBinding;

	/**
	 * Constructor.
	 * 
	 * @param editpart
	 *            The editpart of that presenter
	 */
	public MasterDetailPresentation(IElementEditpart editpart) {
		super((IMasterDetailEditpart) editpart);
		this.modelAccess = new ModelAccess((YMasterDetail) editpart.getModel());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ComponentContainer doCreateWidget(Object parent) {
		if (componentBase == null) {
			componentBase = new VerticalLayout();
			componentBase.addStyleName(CSS_CLASS_CONTROL_BASE);
			if (modelAccess.isCssIdValid()) {
				componentBase.setId(modelAccess.getCssID());
			} else {
				componentBase.setId(getEditpart().getId());
			}

			// create the container for master an detail
			masterBase = new CssLayout();
			masterBase.setStyleName(IConstants.CSS_CLASS_MASTER_BASE);
			masterBase.setSizeFull();
			componentBase.addComponent(masterBase);

			detailBase = new CssLayout();
			masterBase.setStyleName(IConstants.CSS_CLASS_DETAIL_BASE);
			detailBase.setSizeFull();
			componentBase.addComponent(detailBase);

			// creates the binding for the field
			createBindings(modelAccess.yMasterDetail);

			// render master an detail
			if (masterPresentation != null) {
				createMasterWidget();
			}

			if (detailPresentation != null) {
				Component detailWidget = (Component) detailPresentation
						.createWidget(detailBase);
				if (detailWidget != null) {
					detailBase.removeAllComponents();
					detailBase.addComponent(detailWidget);
				}
			}

		}
		return componentBase;
	}

	/**
	 * Creates the bindings for the given values.
	 * 
	 * @param yField
	 * @param field
	 */
	protected void createBindings(YMasterDetail yField) {

		// create the model binding from ECView-Model to the collection bean
		// slot
		URI uri = URIHelper.view().bean(getCollectionBeanSlot())
				.fragment("value").toURI();
		IObservableList targetObservableList = ContextObservables.observeList(
				getViewContext(), uri, modelAccess.yMasterDetail.getType());
		IObservableList modelObservable = EMFProperties.list(
				ExtensionModelPackage.Literals.YMASTER_DETAIL__COLLECTION)
				.observe(getModel());
		registerBinding(createBindings(targetObservableList, modelObservable));

	}

	/**
	 * Creates a bean slot that is the input for the detail fields. It is used
	 * by the presentation to bind the detail fields to that slot.
	 * 
	 * @return
	 */
	protected String getSelectionBeanSlot() {
		return "selection_" + getEditpart().getId();
	}

	/**
	 * Create a bean slot which is the input for the master detail. It is used
	 * by the presentation to bind the table, list,... to that slot.
	 */
	protected String getCollectionBeanSlot() {
		return "input_" + getEditpart().getId();
	}

	@Override
	protected void doUpdateLocale(Locale locale) {

		// update the captions
		applyCaptions();
	}

	/**
	 * Applies the labels to the widgets.
	 */
	protected void applyCaptions() {
		II18nService service = getI18nService();
		if (service != null && modelAccess.isLabelI18nKeyValid()) {

		} else {
			if (modelAccess.isLabelValid()) {

			}
		}
	}

	@Override
	protected IObservable internalGetObservableEndpoint(
			YEmbeddableBindingEndpoint bindableValue) {
		if (bindableValue == null) {
			throw new NullPointerException("BindableValue must not be null!");
		}

		if (bindableValue instanceof YEmbeddableSelectionEndpoint) {
			return internalGetSelectionEndpoint((YEmbeddableSelectionEndpoint) bindableValue);
		} else if (bindableValue instanceof YEmbeddableCollectionEndpoint) {
			return internalGetCollectionEndpoint();
		}
		throw new IllegalArgumentException("Not a valid input: "
				+ bindableValue);
	}

	/**
	 * Returns the observable to observe value.
	 * 
	 * @return
	 */
	protected IObservableList internalGetCollectionEndpoint() {
		// delegate the binding to the proper bean slot
		URI uri = URIHelper.view().bean(getCollectionBeanSlot()).toURI();
		IObservableList modelObservableList = ContextObservables.observeList(
				getViewContext(), uri, modelAccess.yMasterDetail.getType());
		return modelObservableList;
	}

	/**
	 * Returns the observable to observe the selection.
	 * 
	 * @return
	 */
	@SuppressWarnings("restriction")
	protected IObservableValue internalGetSelectionEndpoint(
			YEmbeddableSelectionEndpoint yEndpoint) {

		String attributePath = ECViewModelBindable.getAttributePath(
				ExtensionModelPackage.Literals.YMASTER_DETAIL__SELECTION,
				yEndpoint.getAttributePath());

		// return the observable value for text
		return ECViewModelBindable.observeValue(castEObject(getModel()),
				attributePath, modelAccess.yMasterDetail.getType(), modelAccess.yMasterDetail.getEmfNsURI());
	}

	@Override
	public ComponentContainer getWidget() {
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

			if (masterPresentation != null) {
				disposeMasterWidget();
			}

			if (detailPresentation != null) {
				detailPresentation.unrender();
				detailBase.removeAllComponents();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalDispose() {
		try {
			unrender();

			// do NOT dispose. Will be handled by editpart.
			masterPresentation = null;
			detailPresentation = null;

		} finally {
			super.internalDispose();
		}
	}

	@Override
	public IWidgetPresentation<?> getMaster() {
		return masterPresentation;
	}

	@Override
	public IWidgetPresentation<?> getDetail() {
		return detailPresentation;
	}

	@Override
	public void setMaster(IWidgetPresentation<?> master) {
		if (this.masterPresentation == master) {
			return;
		}

		if (this.masterPresentation != null) {
			disposeMasterWidget();
		}

		this.masterPresentation = master;

		if (isRendered()) {
			if (this.masterPresentation != null) {
				createMasterWidget();
			}
		}
	}

	/**
	 * Creates the master widget and does the databinding.
	 */
	public void createMasterWidget() {

		Component masterWidget = (Component) masterPresentation
				.createWidget(masterBase);
		if (masterWidget != null) {
			masterBase.removeAllComponents();
			masterBase.addComponent(masterWidget);
		}

		YEmbeddable yMaster = (YEmbeddable) masterPresentation.getModel();
		if (yMaster instanceof YCollectionBindable) {
			createMasterCollectionBinding((YCollectionBindable) yMaster);
		}
	}

	/**
	 * Disposes the master widget and removes active bindings.
	 */
	protected void disposeMasterWidget() {
		masterPresentation.dispose();
		masterBase.removeAllComponents();

		if (masterCollectionBinding != null) {
			masterCollectionBinding.dispose();
			masterCollectionBinding = null;
		}
	}

	/**
	 * Creates a binding from the master element to the proper bean slot.
	 * 
	 * @param yMaster
	 */
	protected void createMasterCollectionBinding(
			YCollectionBindable yMasterCollectionBindable) {

		YEmbeddableCollectionEndpoint yMasterEndpoint = yMasterCollectionBindable
				.createCollectionEndpoint();
		IObservableList targetObservableList = (IObservableList) this.masterPresentation
				.getObservableValue(yMasterEndpoint);
		URI uri = URIHelper.view().bean(getCollectionBeanSlot())
				.fragment("value").toURI();
		IObservableList modelObservableList = ContextObservables.observeList(
				getViewContext(), uri, modelAccess.yMasterDetail.getType());

		masterCollectionBinding = createBindings(targetObservableList,
				modelObservableList);
	}

	@Override
	public void setDetail(IWidgetPresentation<?> detail) {
		if (this.detailPresentation == detail) {
			return;
		}

		if (this.detailPresentation != null) {
			this.detailPresentation.dispose();
			detailBase.removeAllComponents();
		}

		this.detailPresentation = detail;

		if (isRendered()) {
			if (this.detailPresentation != null) {
				this.detailPresentation.createWidget(detailBase);
			}
		}
	}

	/**
	 * A helper class.
	 */
	private static class ModelAccess {
		private final YMasterDetail yMasterDetail;

		public ModelAccess(YMasterDetail yMasterDetail) {
			super();
			this.yMasterDetail = yMasterDetail;
		}

		/**
		 * @return
		 * @see org.eclipse.emf.ecp.ecview.ui.core.model.core.YCssAble#getCssClass()
		 */
		public String getCssClass() {
			return yMasterDetail.getCssClass();
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
			return yMasterDetail.getCssID();
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
			return yMasterDetail.getDatadescription() != null
					&& yMasterDetail.getDatadescription().getLabel() != null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabel() {
			return yMasterDetail.getDatadescription().getLabel();
		}

		/**
		 * Returns true, if the label is valid.
		 * 
		 * @return
		 */
		public boolean isLabelI18nKeyValid() {
			return yMasterDetail.getDatadescription() != null
					&& yMasterDetail.getDatadescription().getLabelI18nKey() != null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabelI18nKey() {
			return yMasterDetail.getDatadescription().getLabelI18nKey();
		}
	}
}
