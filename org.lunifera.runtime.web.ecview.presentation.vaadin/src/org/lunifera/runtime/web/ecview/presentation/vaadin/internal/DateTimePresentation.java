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

import java.util.Date;
import java.util.Locale;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFObservables;
import org.lunifera.ecview.core.common.editpart.IElementEditpart;
import org.lunifera.ecview.core.common.model.core.YEmbeddableBindingEndpoint;
import org.lunifera.ecview.core.common.model.core.YEmbeddableValueEndpoint;
import org.lunifera.ecview.core.common.model.datatypes.YDatatype;
import org.lunifera.ecview.core.extension.model.datatypes.YDateTimeDatatype;
import org.lunifera.ecview.core.extension.model.datatypes.YDateTimeFormat;
import org.lunifera.ecview.core.extension.model.datatypes.YDateTimeResolution;
import org.lunifera.ecview.core.extension.model.extension.ExtensionModelPackage;
import org.lunifera.ecview.core.extension.model.extension.YDateTime;
import org.lunifera.ecview.core.ui.core.editparts.extension.IDateTimeEditpart;
import org.lunifera.runtime.web.ecview.presentation.vaadin.common.AbstractFieldWidgetPresenter;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.util.Util;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;

/**
 * This presenter is responsible to render a text area on the given layout.
 */
public class DateTimePresentation extends
		AbstractFieldWidgetPresenter<Component> {

	private final ModelAccess modelAccess;
	private DateField dateField;
	private Binding binding_valueToUI;
	private ObjectProperty<Date> property;
	private String dateFormat;
	private Resolution resolution;

	/**
	 * Constructor.
	 * 
	 * @param editpart
	 *            The editpart of that presenter
	 */
	public DateTimePresentation(IElementEditpart editpart) {
		super((IDateTimeEditpart) editpart);
		this.modelAccess = new ModelAccess((YDateTime) editpart.getModel());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component doCreateWidget(Object parent) {
		if (dateField == null) {

			dateField = new DateField();
			dateField.addStyleName(CSS_CLASS_CONTROL);
			dateField.setImmediate(true);
			setupComponent(dateField, getCastedModel());

			associateWidget(dateField, modelAccess.yField);

			if (modelAccess.isCssIdValid()) {
				dateField.setId(modelAccess.getCssID());
			} else {
				dateField.setId(getEditpart().getId());
			}

			property = new ObjectProperty<Date>(null, Date.class);
			dateField.setPropertyDataSource(property);

			// creates the binding for the field
			createBindings(modelAccess.yField, dateField);

			if (modelAccess.isCssClassValid()) {
				dateField.addStyleName(modelAccess.getCssClass());
			}

			doApplyDatatype(modelAccess.yField.getDatatype());

			applyCaptions();

			initializeField(dateField);
		}
		return dateField;
	}

	/**
	 * Applies the datatype options to the field.
	 * 
	 * @param yDt
	 */
	protected void doApplyDatatype(YDatatype yDt) {
		if (dateField == null) {
			return;
		}

		calcResolutionAndFormat((YDateTimeDatatype) yDt);

		dateField.setDateFormat(dateFormat);
		dateField.setResolution(resolution);
	}

	private void calcResolutionAndFormat(YDateTimeDatatype yDt) {
		if (yDt == null) {
			dateFormat = "yyyy.MM.dd HH:mm";
			resolution = Resolution.MINUTE;
			return;
		}

		Resolution resolution = calcResolution(yDt);
		YDateTimeFormat yFormat = yDt.getFormat();
		if (yFormat != null) {
			switch (yFormat) {
			case DATE:
				switch (resolution) {
				case YEAR:
					dateFormat = "yyyy";
					break;
				case MONTH:
					dateFormat = "yyyy.MM";
					break;
				case DAY:
					dateFormat = "yyyy.MM.dd";
					break;
				default:
					throw new IllegalArgumentException(resolution
							+ " is not a valid resolution for " + yFormat);
				}
				break;
			case DATE_TIME:
				switch (resolution) {
				case YEAR:
					dateFormat = "yyyy";
					break;
				case MONTH:
					dateFormat = "yyyy.MM";
					break;
				case DAY:
					dateFormat = "yyyy.MM.dd";
					break;
				case HOUR:
					dateFormat = "yyyy.MM.dd HH";
					break;
				case MINUTE:
					dateFormat = "yyyy.MM.dd HH:mm";
					break;
				case SECOND:
					dateFormat = "yyyy.MM.dd  HH:mm:ss";
					break;
				default:
					throw new IllegalArgumentException(resolution
							+ " is not a valid resolution for " + yFormat);
				}
				break;
			case TIME:
				switch (resolution) {
				case HOUR:
					dateFormat = "HH";
					break;
				case MINUTE:
					dateFormat = "HH:mm";
					break;
				case SECOND:
					dateFormat = "HH:mm:ss";
					break;
				default:
					throw new IllegalArgumentException(resolution
							+ " is not a valid resolution for " + yFormat);
				}
				break;
			}
		}
	}

	private Resolution calcResolution(YDateTimeDatatype yDt) {
		YDateTimeFormat yFormat = yDt.getFormat();
		if (yFormat != null) {
			YDateTimeResolution yResolution = yDt.getResolution();
			switch (yFormat) {
			case DATE:
				if (yResolution == YDateTimeResolution.UNDEFINED
						|| yResolution == YDateTimeResolution.SECOND
						|| yResolution == YDateTimeResolution.MINUTE
						|| yResolution == YDateTimeResolution.HOUR) {
					resolution = Resolution.DAY;
				}
				break;
			case DATE_TIME:
				if (yResolution == YDateTimeResolution.UNDEFINED
						|| yResolution == YDateTimeResolution.DAY
						|| yResolution == YDateTimeResolution.MONTH
						|| yResolution == YDateTimeResolution.YEAR) {
					resolution = Resolution.MINUTE;
				}
				break;
			case TIME:
				if (yResolution == YDateTimeResolution.UNDEFINED
						|| yResolution == YDateTimeResolution.DAY
						|| yResolution == YDateTimeResolution.MONTH
						|| yResolution == YDateTimeResolution.YEAR) {
					resolution = Resolution.MINUTE;
				}
				break;
			}
		}

		if (resolution == null) {
			switch (yDt.getResolution()) {
			case SECOND:
				resolution = Resolution.SECOND;
				break;
			case MINUTE:
				resolution = Resolution.MINUTE;
				break;
			case HOUR:
				resolution = Resolution.HOUR;
				break;
			case DAY:
				resolution = Resolution.DAY;
				break;
			case MONTH:
				resolution = Resolution.MONTH;
				break;
			case YEAR:
				resolution = Resolution.YEAR;
				break;
			case UNDEFINED:
				resolution = Resolution.MINUTE;
				break;
			default:
				resolution = Resolution.MINUTE;
			}
		}

		return resolution;
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
				modelAccess.getLabelI18nKey(), getLocale(), dateField);
	}

	@Override
	protected Field<?> doGetField() {
		return dateField;
	}

	@Override
	protected IObservable internalGetObservableEndpoint(
			YEmbeddableBindingEndpoint bindableValue) {
		if (bindableValue == null) {
			throw new IllegalArgumentException(
					"BindableValue must not be null!");
		}

		if (bindableValue instanceof YEmbeddableValueEndpoint) {
			return internalGetValueEndpoint();
		}
		throw new IllegalArgumentException("Not a valid input: "
				+ bindableValue);
	}

	/**
	 * Returns the observable to observe value.
	 * 
	 * @return
	 */
	protected IObservableValue internalGetValueEndpoint() {
		// return the observable value for text
		return EMFObservables.observeValue(castEObject(getModel()),
				ExtensionModelPackage.Literals.YDATE_TIME__VALUE);
	}

	/**
	 * Creates the bindings for the given values.
	 * 
	 * @param yField
	 * @param field
	 */
	protected void createBindings(YDateTime yField, DateField field) {
		// create the model binding from widget to ECView-model
		binding_valueToUI = createBindingsValue(castEObject(getModel()),
				ExtensionModelPackage.Literals.YDATE_TIME__VALUE, field, null,
				null);
		registerBinding(binding_valueToUI);

		super.createBindings(yField, field, null);
	}

	@Override
	public Component getWidget() {
		return dateField;
	}

	@Override
	public boolean isRendered() {
		return dateField != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doUnrender() {
		if (dateField != null) {

			// unbind all active bindings
			unbind();

			ComponentContainer parent = ((ComponentContainer) dateField
					.getParent());
			if (parent != null) {
				parent.removeComponent(dateField);
			}

			// remove assocations
			unassociateWidget(dateField);

			dateField = null;
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

		binding_valueToUI.dispose();
		binding_valueToUI = null;
	}

	/**
	 * A helper class.
	 */
	private static class ModelAccess {
		private final YDateTime yField;

		public ModelAccess(YDateTime yField) {
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

		/**
		 * Returns true, if the date format is valid.
		 * 
		 * @return
		 */
		@SuppressWarnings("unused")
		public boolean isDateformatValid() {
			return yField.getDatadescription() != null
					&& yField.getDatatype().getFormat() != null;
		}
	}
}
